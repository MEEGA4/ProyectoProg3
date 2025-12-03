package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import db.GestorBD;
import domain.Pelicula;
import domain.Serie;

public class VentanaPeliculasSeries extends JFrame {

    private static final long serialVersionUID = 1L;

    JPanel panelCentro, pNorte;
    private GestorBD gestor;

    // Arrays con nombres de películas y series (cargados desde BD)
    private String[] peliculas;
    private String[] series;

    // Índices para el scroll
    private int indiceTodo = 0;
    private int indicePeliculas = 0;
    private int indiceSeries = 0;
    private final int ITEMS_POR_FILA = 6;

    // Paleta de colores de VentanaInicio
    private final Color COLOR_FONDO = new Color(33, 37, 41);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_TEXTO_SECUNDARIO = Color.GRAY;
    private final Color COLOR_BOTON = new Color(50, 50, 50);
    private final Color COLOR_BOTON_HOVER = new Color(80, 80, 80);
    private final Color COLOR_AMARILLO = new Color(255, 193, 7);

    public VentanaPeliculasSeries(GestorBD gestorBD) {
        this.gestor = gestorBD;

        // Cargar películas desde la base de datos
        List<Pelicula> listaPeliculas = gestor.obtenerPeliculas();
        peliculas = new String[listaPeliculas.size()];
        for (int i = 0; i < listaPeliculas.size(); i++) {
            peliculas[i] = listaPeliculas.get(i).getNombre();
        }

        // Cargar series desde la base de datos
        List<Serie> listaSeries = gestor.obtenerSeries();
        series = new String[listaSeries.size()];
        for (int i = 0; i < listaSeries.size(); i++) {
            series[i] = listaSeries.get(i).getNombre();
        }

        System.out.format("\n- VentanaPeliculasSeries cargada con %d películas y %d series\n",
                peliculas.length, series.length);

        setTitle("DEUSTOFILM - Películas y Series");

        // Fondo negro
        getContentPane().setBackground(COLOR_FONDO);

        // Panel central con las tres secciones
        panelCentro = new JPanel(new GridLayout(3, 1, 10, 30));
        panelCentro.setBackground(COLOR_FONDO);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Crear las tres secciones
        panelCentro.add(crearSeccion("Todo", true, indiceTodo));
        panelCentro.add(crearSeccion("Películas", false, indicePeliculas));
        panelCentro.add(crearSeccion("Series", false, indiceSeries));

        // Panel norte con logo
        pNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pNorte.setPreferredSize(new Dimension(900, 80));
        pNorte.setBackground(COLOR_FONDO);
        pNorte.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JLabel logo = new JLabel("DEUSTOFILM");
        logo.setForeground(COLOR_AMARILLO);
        logo.setFont(new Font("Arial", Font.BOLD, 32));
        pNorte.add(logo);

        getContentPane().add(panelCentro, BorderLayout.CENTER);
        getContentPane().add(pNorte, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 850);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel crearSeccion(String titulo, boolean mixto, int indiceInicial) {
        // Panel principal de la sección con BorderLayout
        JPanel seccionPanel = new JPanel(new BorderLayout(10, 10));
        seccionPanel.setBackground(COLOR_FONDO);

        // Etiqueta del título de la sección
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        seccionPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel para los items con FlowLayout
        JPanel itemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        itemsPanel.setBackground(COLOR_FONDO);

        // Agregar los items (películas o series)
        String[] contenido;
        if (titulo.equals("Todo")) {
            contenido = combinarArrays(peliculas, series);
        } else if (titulo.equals("Películas")) {
            contenido = peliculas;
        } else {
            contenido = series;
        }

        // Mostrar solo ITEMS_POR_FILA items
        for (int i = indiceInicial; i < Math.min(indiceInicial + ITEMS_POR_FILA, contenido.length); i++) {
            JPanel item = crearItem(contenido[i]);
            itemsPanel.add(item);
        }

        seccionPanel.add(itemsPanel, BorderLayout.CENTER);

        // Botón de avanzar en el este
        if (contenido.length > ITEMS_POR_FILA) {
            JButton btnAvanzar = new JButton(">");
            btnAvanzar.setPreferredSize(new Dimension(50, 120));
            btnAvanzar.setFont(new Font("Arial", Font.BOLD, 24));
            btnAvanzar.setBackground(COLOR_BOTON);
            btnAvanzar.setForeground(COLOR_TEXTO_SECUNDARIO);
            btnAvanzar.setFocusPainted(false);
            btnAvanzar.setBorder(BorderFactory.createEmptyBorder());

            final String tituloFinal = titulo;
            final String[] contenidoFinal = contenido;

            btnAvanzar.addActionListener(e -> {
                avanzarSeccion(tituloFinal, contenidoFinal);
            });

            btnAvanzar.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnAvanzar.setBackground(COLOR_BOTON_HOVER);
                    btnAvanzar.setForeground(COLOR_TEXTO);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnAvanzar.setBackground(COLOR_BOTON);
                    btnAvanzar.setForeground(COLOR_TEXTO_SECUNDARIO);
                }
            });

            seccionPanel.add(btnAvanzar, BorderLayout.EAST);
        }

        return seccionPanel;
    }

    /**
     * Chat GPT pero con modificaciones
     * Escala una imagen para que se ajuste perfectamente al tamaño especificado
     * 
     * @param rutaImagen Ruta de la imagen a escalar
     * @param ancho      Ancho deseado
     * @param alto       Alto deseado
     * @return ImageIcon con la imagen escalada, o null si hay error
     */
    private ImageIcon escalarImagen(String rutaImagen, int ancho, int alto) {
        try {
            ImageIcon iconoOriginal = new ImageIcon(rutaImagen);

            // Verificar que la imagen se cargó correctamente
            if (iconoOriginal.getIconWidth() <= 0) {
                return null;
            }

            // Escalar la imagen con calidad suave
            java.awt.Image imagenEscalada = iconoOriginal.getImage()
                    .getScaledInstance(ancho, alto, java.awt.Image.SCALE_SMOOTH);

            return new ImageIcon(imagenEscalada);
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + rutaImagen);
            return null;
        }
    }

    // Modifica el método crearItem para usar esta función:
    private JPanel crearItem(String nombre) {
        JPanel item = new JPanel(new BorderLayout());
        item.setPreferredSize(new Dimension(120, 165));
        item.setBackground(COLOR_FONDO);

        // Cuadrado con imagen escalada
        JPanel cuadrado = new JPanel();
        cuadrado.setPreferredSize(new Dimension(120, 120));
        cuadrado.setLayout(new BorderLayout()); // Cambiar layout
        cuadrado.setBackground(COLOR_BOTON);
        cuadrado.setBorder(BorderFactory.createLineBorder(COLOR_TEXTO_SECUNDARIO, 2));

        // Cargar y escalar la imagen
        JLabel imagenPelicula = new JLabel();
        ImageIcon iconoEscalado = escalarImagen("resources/images/" + nombre + ".jpeg", 120, 120);

        if (iconoEscalado != null) {
            imagenPelicula.setIcon(iconoEscalado);
        } else {
            // Si no se puede cargar la imagen, mostrar texto
            imagenPelicula.setText("Sin imagen");
            imagenPelicula.setHorizontalAlignment(SwingConstants.CENTER);
            imagenPelicula.setForeground(COLOR_TEXTO_SECUNDARIO);
        }

        cuadrado.add(imagenPelicula, BorderLayout.CENTER);

        // Nombre debajo del cuadrado
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNombre.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblNombre.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        item.add(cuadrado, BorderLayout.CENTER);
        item.add(lblNombre, BorderLayout.SOUTH);

        return item;
    }

    private String[] combinarArrays(String[] arr1, String[] arr2) {
        String[] resultado = new String[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, resultado, 0, arr1.length);
        System.arraycopy(arr2, 0, resultado, arr1.length, arr2.length);
        return resultado;
    }

    private void avanzarSeccion(String titulo, String[] contenido) {
        if (titulo.equals("Todo")) {
            indiceTodo = (indiceTodo + ITEMS_POR_FILA) % contenido.length;
        } else if (titulo.equals("Películas")) {
            indicePeliculas = (indicePeliculas + ITEMS_POR_FILA) % contenido.length;
        } else {
            indiceSeries = (indiceSeries + ITEMS_POR_FILA) % contenido.length;
        }

        // Refrescar la ventana
        panelCentro.removeAll();
        panelCentro.add(crearSeccion("Todo", true, indiceTodo));
        panelCentro.add(crearSeccion("Películas", false, indicePeliculas));
        panelCentro.add(crearSeccion("Series", false, indiceSeries));
        panelCentro.revalidate();
        panelCentro.repaint();
    }

    public static void main(String[] args) {
        // Para pruebas: crear una instancia temporal de GestorBD
        GestorBD gestorTemp = new GestorBD();
        gestorTemp.initBD("resources/db/videoclub.db");
        new VentanaPeliculasSeries(gestorTemp);
    }
}
package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import db.GestorBD;
import domain.Cliente;
import domain.Pelicula;
import domain.Producto;
import domain.Serie;
import domain.Trabajador;

public class VentanaTrabajador extends JFrame {

    private static final long serialVersionUID = 1L;

    private JButton btnVisualizarClientes, btnVisualizarPeliculas;
    private JButton btnVisualizarSeries, btnVisualizarProductos;
    private JButton btnSesion, btnUsuario, btnVolver;

    private Trabajador trabajador;
    private List<Cliente> clientes;
    private List<Producto> productos;

    private JPanel panelHilo;
    private JLabel labelImagenes;

    private GestorBD gestor;

    public VentanaTrabajador(Trabajador trabajador, GestorBD gestorBD) {
        this.trabajador = trabajador;
        this.gestor = gestorBD;
        inicializarDatos();

        // ===== Colores del tema =====
        Color colorPrincipal = new Color(33, 37, 41); // gris carbón
        Color colorBoton = new Color(255, 193, 7); // dorado cálido
        Color texto = Color.WHITE;
        
        ImageIcon im = new ImageIcon("resources/images/logo.png");
        setIconImage(im.getImage());
        
        // ===== Configuración general =====
        setTitle("Panel Trabajador - " + trabajador.getNombre());
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(colorPrincipal);
        add(panelPrincipal);

        // ===== Barra superior =====
        JPanel barraArriba = new JPanel(new BorderLayout());
        barraArriba.setBackground(colorPrincipal);
        barraArriba.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("PANEL DE TRABAJADOR - " + trabajador.getPuesto().toUpperCase());
        lblTitulo.setForeground(colorBoton);
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        barraArriba.add(lblTitulo, BorderLayout.WEST);

        JButton btnCerrarX = new JButton("✕");
        btnCerrarX.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        btnCerrarX.setForeground(colorBoton);
        btnCerrarX.setBackground(colorPrincipal);
        btnCerrarX.setBorderPainted(false);
        btnCerrarX.setFocusPainted(false);
        btnCerrarX.addActionListener(e -> System.exit(0));
        barraArriba.add(btnCerrarX, BorderLayout.EAST);

        hacerVentanaArrastrable(barraArriba);
        panelPrincipal.add(barraArriba, BorderLayout.NORTH);

        // ===== Panel superior (3 botones grandes) =====
        JPanel panelSuperior = new JPanel(new GridLayout(1, 3, 25, 0));
        panelSuperior.setBackground(colorPrincipal);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 60, 30, 60));

        btnUsuario = crearBotonSuperior("MI PERFIL", colorBoton, texto);
        btnSesion = crearBotonSuperior("CERRAR SESIÓN", colorBoton, texto);
        btnVolver = crearBotonSuperior("SALIR", colorBoton, texto);

        panelSuperior.add(btnUsuario);
        panelSuperior.add(btnSesion);
        panelSuperior.add(btnVolver);

        panelPrincipal.add(panelSuperior, BorderLayout.BEFORE_FIRST_LINE);

        // ===== Panel central =====
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 60, 0));
        panelCentral.setBackground(colorPrincipal);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(40, 60, 60, 60));

        // Panel izquierdo: carrusel de imágenes
        panelHilo = new JPanel(new BorderLayout());
        panelHilo.setBackground(Color.BLACK);
        panelHilo.setBorder(BorderFactory.createLineBorder(colorBoton, 3));
        HiloImagen hilo = new HiloImagen();
        hilo.start();
        panelCentral.add(panelHilo);

        // Panel derecho: botones 2x2
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 20, 20));
        panelBotones.setBackground(colorPrincipal);

        btnVisualizarClientes = crearBotonPrincipal("GESTIONAR CLIENTES", colorBoton, texto);
        btnVisualizarPeliculas = crearBotonPrincipal("GESTIONAR PELÍCULAS", colorBoton, texto);
        btnVisualizarSeries = crearBotonPrincipal("GESTIONAR SERIES", colorBoton, texto);
        btnVisualizarProductos = crearBotonPrincipal("TODOS LOS PRODUCTOS", colorBoton, texto);

        panelBotones.add(btnVisualizarClientes);
        panelBotones.add(btnVisualizarPeliculas);
        panelBotones.add(btnVisualizarSeries);
        panelBotones.add(btnVisualizarProductos);

        panelCentral.add(panelBotones);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // ===== Panel inferior =====
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(colorPrincipal);
        JLabel lblInfo = new JLabel("Trabajador: " + trabajador.getNombre() + " " +
                trabajador.getApellido() + " | Puesto: " + trabajador.getPuesto());
        lblInfo.setForeground(colorBoton);
        lblInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        panelInferior.add(lblInfo);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // ===== Listeners =====
        
        // Botón MI PERFIL - Abre la ventana de perfil del trabajador
        btnUsuario.addActionListener(e -> {
            new VentanaPerfilTrabajador(this, trabajador);
        });
        
        btnSesion.addActionListener(e -> {
            VentanaSeleccionar ventana = new VentanaSeleccionar(gestor);
            ventana.setVisible(true);
            this.dispose();
        });
        btnVisualizarPeliculas.addActionListener(e -> {
            List<Pelicula> pelis = new ArrayList<>();
            for (Producto prod : productos) {
                if (prod instanceof Pelicula)
                    pelis.add((Pelicula) prod);
            }
            List<String> cols = Arrays.asList("Nombre", "Descripción", "Precio", "Stock", "Director", "Género",
                    "Duración");
            new VentanaPeliculasTabla(cols, pelis, gestor, trabajador);
            this.dispose();
        });
        btnVisualizarSeries.addActionListener(e -> {
            List<Serie> series = new ArrayList<>();
            for (Producto prod : productos) {
                if (prod instanceof Serie)
                    series.add((Serie) prod);
            }
            List<String> cols = Arrays.asList("Nombre", "Descripción", "Precio", "Stock", "Género", "Temporadas",
                    "Episodios");
            new VentanaSeriesTabla(cols, series, gestor, trabajador);
            this.dispose();
        });
        btnVolver.addActionListener(e ->{
        	System.exit(0);
        	gestor.closeBD();
        });

        btnVisualizarProductos.addActionListener(e -> {
            List<String> cols = Arrays.asList("Tipo", "Nombre", "Descripción", "Precio", "Stock",
                    "Director", "Género", "Duración", "Temporadas", "Episodios");
            new VentanaProductosTabla(cols, productos, gestor, trabajador);
            this.dispose();
        });

        btnVisualizarClientes.addActionListener(e -> {
            List<String> cols = Arrays.asList("Nombre", "Apellido", "Edad", "Ubicación", "Teléfono", "Email");
            new VentanaClientesTabla(cols, clientes, true, gestor, trabajador);
            this.dispose();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== Métodos auxiliares =====
    private void inicializarDatos() {
        // Cargar clientes desde la base de datos
        clientes = gestor.obtenerClientes();

        // Cargar productos desde la base de datos
        productos = new ArrayList<>();

        // Obtener películas y añadirlas a productos
        List<Pelicula> peliculas = gestor.obtenerPeliculas();
        productos.addAll(peliculas);

        // Obtener series y añadirlas a productos
        List<Serie> series = gestor.obtenerSeries();
        productos.addAll(series);

        System.out.format("\n- Datos cargados desde BD: %d clientes, %d productos\n",
                clientes.size(), productos.size());
    }

    private JButton crearBotonPrincipal(String texto, Color colorBoton, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        boton.setBackground(colorBoton);
        boton.setForeground(textoColor);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(textoColor);
                boton.setForeground(colorBoton);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorBoton);
                boton.setForeground(textoColor);
            }
        });
        return boton;
    }

    private JButton crearBotonSuperior(String texto, Color colorBoton, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18)); // Más grandes
        boton.setBackground(colorBoton);
        boton.setForeground(textoColor);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25)); // Más altos y anchos
        return boton;
    }

    private void hacerVentanaArrastrable(JPanel panel) {
        final int[] posX = new int[1];
        final int[] posY = new int[1];
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                posX[0] = e.getX();
                posY[0] = e.getY();
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - posX[0], e.getYOnScreen() - posY[0]);
            }
        });
    }

    // ===== Hilo de imágenes =====
    private class HiloImagen extends Thread {
        private int currentIndex = 0;

        @Override
        public void run() {
            List<ImageIcon> listaImagenes = new ArrayList<>();
            String[] rutas = {
                    "resources/images/batman.jpeg",
                    "resources/images/rocky.jpeg",
                    "resources/images/titanic.jpeg",
                    "resources/images/Silencio de los Corderos.jpeg"
            };

            for (String ruta : rutas) {
                ImageIcon img = new ImageIcon(ruta);
                Image scaled = img.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH);
                listaImagenes.add(new ImageIcon(scaled));
            }

            try {
                while (true) {
                    SwingUtilities.invokeLater(() -> {
                        panelHilo.removeAll();
                        labelImagenes = new JLabel(listaImagenes.get(currentIndex));
                        labelImagenes.setHorizontalAlignment(JLabel.CENTER);
                        panelHilo.add(labelImagenes, BorderLayout.CENTER);
                        panelHilo.revalidate();
                        panelHilo.repaint();
                        currentIndex = (currentIndex + 1) % listaImagenes.size();
                    });
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                System.out.println("Hilo de imágenes interrumpido");
            }
        }
    }
}
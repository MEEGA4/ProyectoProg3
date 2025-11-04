package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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

    public VentanaTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
        inicializarDatos();

        // ===== Colores del tema =====
        Color colorPrincipal = new Color(33, 37, 41);  // gris carbón
        Color colorBoton = new Color(255, 193, 7);     // dorado cálido
        Color texto = Color.WHITE;

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
        btnSesion.addActionListener(e -> {
            VentanaSeleccion ventana = new VentanaSeleccion();
            ventana.setVisible(true);
            this.dispose();
        });
        btnVolver.addActionListener(e -> System.exit(0));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== Métodos auxiliares =====
    private void inicializarDatos() {
        clientes = new ArrayList<>();
        productos = new ArrayList<>();
        clientes.add(new Cliente("Ana", "Martínez", 28, "1234", "Madrid",
                "611222333", "ana@email.com"));
        clientes.add(new Cliente("Pedro", "Sánchez", 35, "pass", "Barcelona",
                "622333444", "pedro@email.com"));
        productos.add(new Pelicula("Inception", "Thriller de ciencia ficción",
                12.99, 50, "Christopher Nolan", "Sci-Fi", 148));
        productos.add(new Serie("Breaking Bad", "Drama criminal",
                19.99, 30, "Drama", 5));
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
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));  // Más grandes
        boton.setBackground(colorBoton);
        boton.setForeground(textoColor);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));  // Más altos y anchos
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
                    "resources/images/rocky.jpg",
                    "resources/images/titanic.jpg",
                    "resources/images/productos.jpg"
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

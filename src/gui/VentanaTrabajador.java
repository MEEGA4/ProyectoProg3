package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
        
        // Inicializar datos (deberías cargar desde BD)
        inicializarDatos();
        
        // Configuración de la ventana
        setTitle("Panel Trabajador - " + trabajador.getNombre());
        setUndecorated(true);
        
        // Colores del tema
        Color colorRojo = Color.RED;
        Color colorNegro = Color.BLACK;
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        add(panelPrincipal);
        
        // ============= BARRA SUPERIOR (NEGRA) =============
        JPanel barraArriba = new JPanel(new BorderLayout());
        barraArriba.setBackground(colorNegro);
        JLabel lblTitulo = new JLabel("  PANEL DE TRABAJADOR - " + trabajador.getPuesto().toUpperCase());
        lblTitulo.setForeground(colorRojo);
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        barraArriba.add(lblTitulo, BorderLayout.WEST);
        
        // Botón X para cerrar en la barra
        JButton btnCerrarX = new JButton("✕");
        btnCerrarX.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        btnCerrarX.setForeground(colorRojo);
        btnCerrarX.setBackground(colorNegro);
        btnCerrarX.setBorderPainted(false);
        btnCerrarX.setFocusPainted(false);
        btnCerrarX.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrarX.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnCerrarX.setForeground(colorRojo);
            }
        });
        btnCerrarX.addActionListener(e -> System.exit(0));
        barraArriba.add(btnCerrarX, BorderLayout.EAST);
        
        hacerVentanaArrastrable(barraArriba);
        panelPrincipal.add(barraArriba, BorderLayout.NORTH);
        
        // ============= PANEL SUPERIOR CON LOGO Y BOTONES =============
        JPanel panelSuperior = new JPanel(new GridLayout(1, 4, 10, 10));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Logo/Icono
        JLabel labelLogo = new JLabel();
        ImageIcon im = new ImageIcon("resources/images/logo.png");
        labelLogo.setIcon(im);
        labelLogo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        labelLogo.setForeground(colorRojo);
        
        btnSesion = new JButton("CERRAR SESIÓN");
        btnUsuario = new JButton("MI PERFIL");
        btnVolver = new JButton("SALIR");
        
        panelSuperior.add(labelLogo);
        panelSuperior.add(btnUsuario);
        panelSuperior.add(btnSesion);
        panelSuperior.add(btnVolver);
        
        anadirColores(panelSuperior.getComponents(), colorRojo);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // ============= PANEL CENTRAL =============
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel izquierdo: Carrusel de imágenes
        panelHilo = new JPanel(new BorderLayout());
        panelHilo.setBackground(colorNegro);
        panelHilo.setBorder(BorderFactory.createLineBorder(colorRojo, 3));
        HiloImagen hilo = new HiloImagen();
        hilo.start();
        panelCentral.add(panelHilo);
        
        // Panel derecho: Botones principales
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        
        btnVisualizarClientes = new JButton("GESTIONAR CLIENTES");
        btnVisualizarPeliculas = new JButton("GESTIONAR PELÍCULAS");
        btnVisualizarSeries = new JButton("GESTIONAR SERIES");
        btnVisualizarProductos = new JButton("TODOS LOS PRODUCTOS");
        
        panelBotones.add(btnVisualizarClientes);
        panelBotones.add(btnVisualizarPeliculas);
        panelBotones.add(btnVisualizarSeries);
        panelBotones.add(btnVisualizarProductos);
        
        anadirColores(panelBotones.getComponents(), colorRojo);
        panelCentral.add(panelBotones);
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        // ============= PANEL INFERIOR =============
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(colorNegro);
        JLabel lblInfo = new JLabel("Trabajador: " + trabajador.getNombre() + " " + 
            trabajador.getApellido() + " | Puesto: " + trabajador.getPuesto());
        lblInfo.setForeground(colorRojo);
        lblInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        panelInferior.add(lblInfo);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        // ============= LISTENERS DE LOS BOTONES =============
        btnVisualizarClientes.addActionListener(e -> {
            // VentanaClientes ventana = new VentanaClientes(clientes, trabajador);
            // ventana.setVisible(true);
            // this.dispose();
            System.out.println("Abriendo gestión de clientes...");
        });
        
        btnVisualizarPeliculas.addActionListener(e -> {
            // VentanaPeliculas ventana = new VentanaPeliculas(trabajador);
            // ventana.setVisible(true);
            // this.dispose();
            System.out.println("Abriendo gestión de películas...");
        });
        
        btnVisualizarSeries.addActionListener(e -> {
            // VentanaSeries ventana = new VentanaSeries(trabajador);
            // ventana.setVisible(true);
            // this.dispose();
            System.out.println("Abriendo gestión de series...");
        });
        
        btnVisualizarProductos.addActionListener(e -> {
            // VentanaProductos ventana = new VentanaProductos(productos, trabajador);
            // ventana.setVisible(true);
            // this.dispose();
            System.out.println("Abriendo todos los productos...");
        });
        
        btnUsuario.addActionListener(e -> {
            // VentanaPerfilTrabajador ventana = new VentanaPerfilTrabajador(trabajador);
            // ventana.setVisible(true);
            // this.dispose();
            System.out.println("Mostrando perfil de: " + trabajador.getNombre());
        });
        
        btnSesion.addActionListener(e -> {
            VentanaSeleccion ventana = new VentanaSeleccion();
            ventana.setVisible(true);
            this.dispose();
        });
        
        btnVolver.addActionListener(e -> System.exit(0));
        
        // Configuración final de la ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void inicializarDatos() {
        clientes = new ArrayList<>();
        productos = new ArrayList<>();
        
        // Datos de ejemplo
        clientes.add(new Cliente("Ana", "Martínez", 28, "1234", "Madrid", 
            "611222333", "ana@email.com"));
        clientes.add(new Cliente("Pedro", "Sánchez", 35, "pass", "Barcelona", 
            "622333444", "pedro@email.com"));
        
        productos.add(new Pelicula("Inception", "Thriller de ciencia ficción", 
            12.99, 50, "Christopher Nolan", "Sci-Fi", 148));
        productos.add(new Serie("Breaking Bad", "Drama criminal", 
            19.99, 30, "Drama", 5));
    }
    
    private void anadirColores(Component[] components, Color color) {
        for (Component component : components) {
            if (component instanceof JButton) {
                component.setBackground(color);
                component.setForeground(Color.WHITE);
                component.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
                
                component.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        component.setBackground(Color.WHITE);
                        component.setForeground(color);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        component.setBackground(color);
                        component.setForeground(Color.WHITE);
                    }
                });
            } else if (component instanceof JPanel) {
                JPanel panelN = (JPanel) component;
                anadirColores(panelN.getComponents(), color);
            }
        }
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
    
    // Hilo para rotar imágenes automáticamente
    private class HiloImagen extends Thread {
        private int currentIndex = 0;
        
        @Override
        public void run() {
            try {
                while (true) {
                    cambiarImagen();
                    Thread.sleep(4000); // Cambia cada 4 segundos
                }
            } catch (InterruptedException e) {
                System.out.println("Hilo de imágenes interrumpido");
            }
        }
        
        private void cambiarImagen() {
            List<JLabel> imagenes = new ArrayList<>();
            
            // Imagen 1: Películas
            JLabel img1 = crearLabelImagen("🎬", "PELÍCULAS");
            imagenes.add(img1);
            
            // Imagen 2: Series
            JLabel img2 = crearLabelImagen("📺", "SERIES");
            imagenes.add(img2);
            
            // Imagen 3: Clientes
            JLabel img3 = crearLabelImagen("👥", "CLIENTES");
            imagenes.add(img3);
            
            // Imagen 4: Productos
            JLabel img4 = crearLabelImagen("🎥", "PRODUCTOS");
            imagenes.add(img4);
            
            SwingUtilities.invokeLater(() -> {
                panelHilo.removeAll();
                labelImagenes = imagenes.get(currentIndex);
                panelHilo.add(labelImagenes, BorderLayout.CENTER);
                panelHilo.revalidate();
                panelHilo.repaint();
                
                currentIndex++;
                if (currentIndex >= imagenes.size()) {
                    currentIndex = 0;
                }
            });
        }
        
        private JLabel crearLabelImagen(String emoji, String texto) {
            JLabel label = new JLabel("<html><center>" + emoji + "<br>" + texto + "</center></html>", 
                JLabel.CENTER);
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
            label.setForeground(Color.RED);
            label.setOpaque(true);
            label.setBackground(Color.BLACK);
            return label;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Trabajador trabajador = new Trabajador("Carlos", "López", 35, "admin", 
                "Madrid", "Gerente", 35000);
            new VentanaTrabajador(trabajador);
        });
    }
}
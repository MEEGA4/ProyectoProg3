package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import domain.Cliente;
import domain.Trabajador;

public class VentanaSeleccion extends JFrame {
    private static final long serialVersionUID = 1L;
    protected JButton botonCerrar, botonCliente, botonTrabajador;
    protected JPanel pAbajo, pCentro;
    protected JLabel lblBienvenida;

    private ArrayList<Cliente> listaClientes;
    private ArrayList<Trabajador> listaTrabajadores;
    private Object usuarioActual;

    public VentanaSeleccion() {
        // Configuración inicial de la ventana
        ImageIcon im = new ImageIcon("resources/images/logo.png");
        setIconImage(im.getImage());
        setTitle("Sistema de Gestión - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 200, 600, 400);
        
        // Inicialización de componentes
        botonCerrar = new JButton("CERRAR");
        botonCliente = new JButton("SOY CLIENTE");
        botonTrabajador = new JButton("SOY TRABAJADOR");
        lblBienvenida = new JLabel("¿Cómo deseas acceder?");
        lblBienvenida.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        lblBienvenida.setForeground(Color.WHITE);
        
        pAbajo = new JPanel();
        pCentro = new JPanel();
        
        // Configuración de colores de fondo
        pCentro.setBackground(new Color(33, 37, 41));
        pAbajo.setBackground(new Color(33, 37, 41));
        
        // Añadir componentes a los paneles
        pCentro.add(lblBienvenida);
        pCentro.add(botonCliente);
        pCentro.add(botonTrabajador);
        pAbajo.add(botonCerrar);

        // Aplicar estilos a los botones
        anadirColores(pCentro.getComponents(), new Color(255, 193, 7));
        anadirColores(pAbajo.getComponents(), new Color(255, 193, 7)); 
        
        // Añadir paneles al frame
        getContentPane().add(pAbajo, BorderLayout.SOUTH);
        getContentPane().add(pCentro, BorderLayout.CENTER);

        // Inicializar listas
        inicializarListas();

        // Listeners de los botones
        botonCerrar.addActionListener((e) -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea salir?", 
                "Confirmar salida", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        botonTrabajador.addActionListener((e) -> {
            String usuario = JOptionPane.showInputDialog(this, 
                "Ingrese su nombre de usuario:", 
                "Inicio de Sesión - Trabajador", 
                JOptionPane.PLAIN_MESSAGE);
            
            if (usuario != null && !usuario.trim().isEmpty()) {
                String contrasena = JOptionPane.showInputDialog(this, 
                    "Ingrese su contraseña:", 
                    "Inicio de Sesión - Trabajador", 
                    JOptionPane.PLAIN_MESSAGE);
                
                if (contrasena != null && verificarTrabajador(usuario, contrasena)) {
                    JOptionPane.showMessageDialog(this, 
                        "Inicio de sesión exitoso!", 
                        "Bienvenido: " + usuario, 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Abrir ventana de trabajador con el usuario autenticado
                    abrirMenuTrabajador();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Usuario o contraseña incorrectos", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        botonCliente.addActionListener(e -> {
            String usuario = JOptionPane.showInputDialog(this, 
                "Ingrese su nombre de usuario:", 
                "Inicio de Sesión - Cliente", 
                JOptionPane.PLAIN_MESSAGE);
            
            if (usuario != null && !usuario.trim().isEmpty()) {
                String contrasena = JOptionPane.showInputDialog(this, 
                    "Ingrese su contraseña:", 
                    "Inicio de Sesión - Cliente", 
                    JOptionPane.PLAIN_MESSAGE);
                
                if (contrasena != null && verificarCliente(usuario, contrasena)) {
                    JOptionPane.showMessageDialog(this, 
                        "Inicio de sesión exitoso!", 
                        "Bienvenido: " + usuario, 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Abrir ventana de cliente
                    abrirMenuCliente();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Usuario o contraseña incorrectos", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void inicializarListas() {
        listaClientes = new ArrayList<>();
        listaTrabajadores = new ArrayList<>();
        
        // Datos de ejemplo - reemplazar con carga desde BD
        listaClientes.add(new Cliente("Juan", "Pérez", 30, "1234", "Madrid", 
            "123456789", "juan@email.com"));
        listaClientes.add(new Cliente("María", "García", 25, "pass123", "Barcelona", 
            "987654321", "maria@email.com"));
        
        listaTrabajadores.add(new Trabajador("Carlos", "López", 35, "admin", "Madrid", 
            "Gerente", 35000));
        listaTrabajadores.add(new Trabajador("Ana", "Martínez", 28, "1234", "Valencia", 
            "Vendedor", 25000));
    }

    private boolean verificarCliente(String nombre, String contrasena) {
        for (Cliente cliente : listaClientes) {
            if (cliente.getNombre().equals(nombre) && 
                cliente.getContrasena().equals(contrasena)) {
                usuarioActual = cliente;
                return true;
            }
        }
        return false;
    }

    private boolean verificarTrabajador(String nombre, String contrasena) {
        for (Trabajador trabajador : listaTrabajadores) {
            if (trabajador.getNombre().equals(nombre) && 
                trabajador.getContrasena().equals(contrasena)) {
                usuarioActual = trabajador;
                return true;
            }
        }
        return false;
    }

    private void abrirMenuCliente() {
        SwingUtilities.invokeLater(() -> {
            Cliente cliente = (Cliente) usuarioActual;
            new VentanaInicio(cliente); // Pasar el cliente autenticado
            this.dispose(); // Cerrar la ventana de selección
        });
    }

    private void abrirMenuTrabajador() {
        SwingUtilities.invokeLater(() -> {
            Trabajador trabajador = (Trabajador) usuarioActual;
            VentanaTrabajador ventana = new VentanaTrabajador(trabajador);
            // La ventana ya se muestra automáticamente en su constructor
            this.dispose(); // Cerrar la ventana de selección
        });
    }

    private void anadirColores(Component[] components, Color color) {
        for (Component component : components) {
            if (component instanceof JButton) {
                component.setBackground(color);
                component.setForeground(Color.WHITE);
                component.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
                
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
            }
        }
    }
}
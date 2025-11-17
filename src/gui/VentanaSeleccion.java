package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import domain.Cliente;
import domain.Trabajador;

public class VentanaSeleccion extends JFrame {
    private static final long serialVersionUID = 1L;
    protected JButton botonCerrar, botonCliente, botonTrabajador;
    protected JPanel pAbajo, pCentro, pNorte, pFormularios;
    protected JLabel lblBienvenida, lblLogo;
    
    // Campos de texto para cliente
    protected JTextField txtUsuarioCliente;
    protected JPasswordField txtContrasenaCliente;
    protected JLabel lblUsuarioCliente, lblContrasenaCliente;
    
    // Campos de texto para trabajador
    protected JTextField txtUsuarioTrabajador;
    protected JPasswordField txtContrasenaTrabajador;
    protected JLabel lblUsuarioTrabajador, lblContrasenaTrabajador;

    private ArrayList<Cliente> listaClientes;
    private ArrayList<Trabajador> listaTrabajadores;
    private Object usuarioActual;
    
    // Paleta de colores
    private final Color COLOR_FONDO = new Color(33, 37, 41);
    private final Color COLOR_AMARILLO = new Color(255, 193, 7);

    public VentanaSeleccion() {
        // Configuración inicial de la ventana
        ImageIcon im = new ImageIcon("resources/images/logo.png");
        setIconImage(im.getImage());
        setTitle("Sistema de Gestión - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 200, 750, 550);
        
        // Panel Norte - Logo DEUSTOFILM centrado
        pNorte = new JPanel();
        pNorte.setLayout(new BoxLayout(pNorte, BoxLayout.Y_AXIS));
        pNorte.setBackground(COLOR_FONDO);
        pNorte.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        
        lblLogo = new JLabel("DEUSTOFILM");
        lblLogo.setForeground(COLOR_AMARILLO);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 42));
        lblLogo.setAlignmentX(CENTER_ALIGNMENT);
        pNorte.add(lblLogo);
        
        pNorte.add(Box.createVerticalStrut(20));
        
        lblBienvenida = new JLabel("¿Cómo deseas acceder?");
        lblBienvenida.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setAlignmentX(CENTER_ALIGNMENT);
        pNorte.add(lblBienvenida);
        
        // Panel de formularios (Centro)
        pFormularios = new JPanel(new GridLayout(1, 2, 20, 0));
        pFormularios.setBackground(COLOR_FONDO);
        pFormularios.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Formulario Cliente
        JPanel panelCliente = crearPanelFormulario("SOY CLIENTE", true);
        pFormularios.add(panelCliente);
        
        // Formulario Trabajador
        JPanel panelTrabajador = crearPanelFormulario("SOY TRABAJADOR", false);
        pFormularios.add(panelTrabajador);
        
        // Panel Abajo - Botón cerrar
        pAbajo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pAbajo.setBackground(COLOR_FONDO);
        pAbajo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        botonCerrar = new JButton("CERRAR");
        botonCerrar.setPreferredSize(new Dimension(680, 40));
        aplicarEstiloBoton(botonCerrar, COLOR_AMARILLO);
        pAbajo.add(botonCerrar);
        
        // Añadir paneles al frame
        getContentPane().add(pNorte, BorderLayout.NORTH);
        getContentPane().add(pFormularios, BorderLayout.CENTER);
        getContentPane().add(pAbajo, BorderLayout.SOUTH);

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

        botonCliente.addActionListener(e -> {
            String usuario = txtUsuarioCliente.getText();
            String contrasena = new String(txtContrasenaCliente.getPassword());
            
            if (usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, complete todos los campos", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (verificarCliente(usuario, contrasena)) {
                JOptionPane.showMessageDialog(this, 
                    "Inicio de sesión exitoso!", 
                    "Bienvenido: " + usuario, 
                    JOptionPane.INFORMATION_MESSAGE);
                abrirMenuCliente();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Usuario o contraseña incorrectos", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        botonTrabajador.addActionListener((e) -> {
            String usuario = txtUsuarioTrabajador.getText();
            String contrasena = new String(txtContrasenaTrabajador.getPassword());
            
            if (usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, complete todos los campos", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (verificarTrabajador(usuario, contrasena)) {
                JOptionPane.showMessageDialog(this, 
                    "Inicio de sesión exitoso!", 
                    "Bienvenido: " + usuario, 
                    JOptionPane.INFORMATION_MESSAGE);
                abrirMenuTrabajador();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Usuario o contraseña incorrectos", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
    
    private JPanel crearPanelFormulario(String titulo, boolean esCliente) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        
        // Botón de tipo de usuario
        JButton btnTipo = new JButton(titulo);
        btnTipo.setMaximumSize(new Dimension(300, 80));
        btnTipo.setAlignmentX(CENTER_ALIGNMENT);
        aplicarEstiloBoton(btnTipo, COLOR_AMARILLO);
        panel.add(btnTipo);
        
        if (esCliente) {
            botonCliente = btnTipo;
        } else {
            botonTrabajador = btnTipo;
        }
        
        panel.add(Box.createVerticalStrut(20));
        
        // Campo Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuario.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblUsuario);
        
        panel.add(Box.createVerticalStrut(5));
        
        JTextField txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(300, 40));
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(txtUsuario);
        
        panel.add(Box.createVerticalStrut(15));
        
        // Campo Contraseña
        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setForeground(Color.WHITE);
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        lblContrasena.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblContrasena);
        
        panel.add(Box.createVerticalStrut(5));
        
        JPasswordField txtContrasena = new JPasswordField();
        txtContrasena.setMaximumSize(new Dimension(300, 40));
        txtContrasena.setPreferredSize(new Dimension(300, 40));
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContrasena.setBackground(Color.WHITE);
        txtContrasena.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(txtContrasena);
        
        // Guardar referencias
        if (esCliente) {
            txtUsuarioCliente = txtUsuario;
            txtContrasenaCliente = txtContrasena;
            lblUsuarioCliente = lblUsuario;
            lblContrasenaCliente = lblContrasena;
        } else {
            txtUsuarioTrabajador = txtUsuario;
            txtContrasenaTrabajador = txtContrasena;
            lblUsuarioTrabajador = lblUsuario;
            lblContrasenaTrabajador = lblContrasena;
        }
        
        return panel;
    }
    
    private void aplicarEstiloBoton(JButton boton, Color color) {
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(Color.WHITE);
                boton.setForeground(color);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
                boton.setForeground(Color.WHITE);
            }
        });
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
            new VentanaInicio(cliente);
            this.dispose();
        });
    }

    private void abrirMenuTrabajador() {
        SwingUtilities.invokeLater(() -> {
            Trabajador trabajador = (Trabajador) usuarioActual;
            VentanaTrabajador ventana = new VentanaTrabajador(trabajador);
            this.dispose();
        });
    }
}
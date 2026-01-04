package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import domain.Trabajador;

public class VentanaPerfilTrabajador extends JDialog {

    private static final long serialVersionUID = 1L;

    // Paleta de colores (mismo estilo que VentanaTrabajador)
    private final Color COLOR_FONDO = new Color(33, 37, 41);
    private final Color COLOR_DORADO = new Color(255, 193, 7);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_GRIS = new Color(108, 117, 125);

    public VentanaPerfilTrabajador(JFrame parent, Trabajador trabajador) {
        super(parent, "Mi Perfil", true);
        
        setUndecorated(true);
        setSize(500, 450);
        setLocationRelativeTo(parent);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(COLOR_DORADO, 2));
        setContentPane(panelPrincipal);

        // ===== Barra Superior =====
        JPanel barraArriba = new JPanel(new BorderLayout());
        barraArriba.setBackground(COLOR_FONDO);
        barraArriba.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("MI PERFIL");
        lblTitulo.setForeground(COLOR_DORADO);
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        barraArriba.add(lblTitulo, BorderLayout.WEST);

        JButton btnCerrar = new JButton("✕");
        btnCerrar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        btnCerrar.setForeground(COLOR_DORADO);
        btnCerrar.setBackground(COLOR_FONDO);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());
        barraArriba.add(btnCerrar, BorderLayout.EAST);

        hacerVentanaArrastrable(barraArriba);
        panelPrincipal.add(barraArriba, BorderLayout.NORTH);

        // ===== Panel Central con datos del perfil =====
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(COLOR_FONDO);
        panelCentral.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Avatar/Icono del trabajador
        JLabel lblAvatar = new JLabel("👤");
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        lblAvatar.setForeground(COLOR_DORADO);
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblAvatar);

        panelCentral.add(Box.createVerticalStrut(10));

        // Nombre completo
        JLabel lblNombreCompleto = new JLabel(trabajador.getNombre() + " " + trabajador.getApellido());
        lblNombreCompleto.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        lblNombreCompleto.setForeground(COLOR_TEXTO);
        lblNombreCompleto.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblNombreCompleto);

        // Puesto
        JLabel lblPuesto = new JLabel(trabajador.getPuesto());
        lblPuesto.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 16));
        lblPuesto.setForeground(COLOR_DORADO);
        lblPuesto.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblPuesto);

        panelCentral.add(Box.createVerticalStrut(30));

        // Panel de datos detallados
        JPanel panelDatos = new JPanel(new GridLayout(4, 2, 10, 15));
        panelDatos.setBackground(COLOR_FONDO);
        panelDatos.setMaximumSize(new Dimension(400, 200));

        // Edad
        panelDatos.add(crearEtiqueta("Edad:", true));
        panelDatos.add(crearEtiqueta(trabajador.getEdad() + " años", false));

        // Ubicación
        panelDatos.add(crearEtiqueta("Ubicación:", true));
        panelDatos.add(crearEtiqueta(trabajador.getUbicacion(), false));

        // Puesto
        panelDatos.add(crearEtiqueta("Puesto:", true));
        panelDatos.add(crearEtiqueta(trabajador.getPuesto(), false));

        // Salario
        panelDatos.add(crearEtiqueta("Salario:", true));
        panelDatos.add(crearEtiqueta(String.format("%.2f €", trabajador.getSalario()), false));

        panelCentral.add(panelDatos);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // ===== Panel Inferior con botón =====
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInferior.setBackground(COLOR_FONDO);
        panelInferior.setBorder(new EmptyBorder(15, 20, 20, 20));

        JButton btnVolver = crearBotonEstilizado("CERRAR");
        btnVolver.addActionListener(e -> dispose());
        panelInferior.add(btnVolver);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel crearEtiqueta(String texto, boolean esLabel) {
        JLabel label = new JLabel(texto);
        if (esLabel) {
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
            label.setForeground(COLOR_GRIS);
        } else {
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            label.setForeground(COLOR_TEXTO);
        }
        return label;
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        boton.setBackground(COLOR_DORADO);
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_TEXTO);
                boton.setForeground(COLOR_DORADO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_DORADO);
                boton.setForeground(COLOR_TEXTO);
            }
        });
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
}
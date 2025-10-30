package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import domain.Cliente;
import domain.PerfilCliente;

public class VentanaInicio extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel pCentro, panelPerfiles;
    private ArrayList<PerfilCliente> perfiles;
    private static final int MAX_PERFILES = 5;
    private boolean modoEdicion = false;
    private JButton btnGestionarPerfiles;

    public VentanaInicio() {
        ImageIcon im = new ImageIcon("resources/images/logo.png");
        setIconImage(im.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 200, 900, 650);
        setLocationRelativeTo(null);
        
        // Fondo negro
        getContentPane().setBackground(Color.BLACK);

        // Inicializar lista de perfiles con uno por defecto
        perfiles = new ArrayList<>();
        // Cliente por defecto (puedes modificar los datos según necesites)
        Cliente clienteDefault = new Cliente("Usuario", "Principal", 25, "1234", "Bilbao", "123456789", "usuario@deustofilm.com");
        perfiles.add(new PerfilCliente(clienteDefault, new Color(229, 9, 20), null));

        // Panel central con todo el contenido
        pCentro = new JPanel();
        pCentro.setLayout(new BoxLayout(pCentro, BoxLayout.Y_AXIS));
        pCentro.setBackground(Color.BLACK);

        // Espaciado superior
        pCentro.add(Box.createVerticalStrut(30));

        // Pregunta "¿Quién está viendo ahora?"
        JLabel labelInicio = new JLabel("¿Quién está viendo ahora?");
        labelInicio.setForeground(Color.WHITE);
        labelInicio.setFont(new Font("Arial", Font.PLAIN, 28));
        labelInicio.setAlignmentX(CENTER_ALIGNMENT);
        pCentro.add(labelInicio);

        pCentro.add(Box.createVerticalStrut(30));

        // Logo DEUSTOFILM
        JLabel labelMarca = new JLabel("DEUSTOFILM");
        labelMarca.setForeground(new Color(229, 9, 20));
        labelMarca.setFont(new Font("Arial", Font.BOLD, 48));
        labelMarca.setAlignmentX(CENTER_ALIGNMENT);
        pCentro.add(labelMarca);

        pCentro.add(Box.createVerticalStrut(40));

        // Panel con los perfiles
        panelPerfiles = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelPerfiles.setBackground(Color.BLACK);
        
        actualizarPerfiles();

        pCentro.add(panelPerfiles);

        pCentro.add(Box.createVerticalStrut(30));

        // Botón para gestionar perfiles
        btnGestionarPerfiles = new JButton("Administrar perfiles");
        btnGestionarPerfiles.setBackground(new Color(50, 50, 50));
        btnGestionarPerfiles.setForeground(Color.LIGHT_GRAY);
        btnGestionarPerfiles.setFont(new Font("Arial", Font.PLAIN, 14));
        btnGestionarPerfiles.setFocusPainted(false);
        btnGestionarPerfiles.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnGestionarPerfiles.setAlignmentX(CENTER_ALIGNMENT);
        
        btnGestionarPerfiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleModoEdicion();
            }
        });

        btnGestionarPerfiles.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnGestionarPerfiles.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(MouseEvent e) {
                btnGestionarPerfiles.setBackground(new Color(50, 50, 50));
            }
        });

        pCentro.add(btnGestionarPerfiles);

        getContentPane().add(pCentro, BorderLayout.CENTER);

        setVisible(true);
    }

    private void toggleModoEdicion() {
        modoEdicion = !modoEdicion;
        if (modoEdicion) {
            btnGestionarPerfiles.setText("Hecho");
            btnGestionarPerfiles.setForeground(Color.WHITE);
        } else {
            btnGestionarPerfiles.setText("Administrar perfiles");
            btnGestionarPerfiles.setForeground(Color.LIGHT_GRAY);
        }
        actualizarPerfiles();
    }

    private void actualizarPerfiles() {
        panelPerfiles.removeAll();

        // Mostrar perfiles existentes
        for (int i = 0; i < perfiles.size(); i++) {
            PerfilCliente perfil = perfiles.get(i);
            JPanel panelPerfil = crearPerfil(perfil, i);
            panelPerfiles.add(panelPerfil);
        }

        // Botón para añadir nuevo perfil (si no se ha alcanzado el máximo)
        if (perfiles.size() < MAX_PERFILES) {
            JPanel panelAnadir = crearBotonAnadir();
            panelPerfiles.add(panelAnadir);
        }

        panelPerfiles.revalidate();
        panelPerfiles.repaint();
    }

    private JPanel crearPerfil(PerfilCliente perfil, int index) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.BLACK);
        panel.setPreferredSize(new Dimension(150, modoEdicion ? 220 : 180));

        // Avatar (cuadrado con cara sonriente o imagen)
        JButton btnPerfil = new JButton();
        btnPerfil.setPreferredSize(new Dimension(130, 130));
        btnPerfil.setBackground(perfil.getColor());
        btnPerfil.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        btnPerfil.setFocusPainted(false);
        
        // Si tiene imagen, mostrarla, sino mostrar emoji
        if (perfil.getImagen() != null) {
            ImageIcon icon = new ImageIcon(perfil.getImagen().getScaledInstance(130, 130, Image.SCALE_SMOOTH));
            btnPerfil.setIcon(icon);
            btnPerfil.setText("");
        } else {
            btnPerfil.setText("☺");
            btnPerfil.setFont(new Font("Arial", Font.PLAIN, 60));
            btnPerfil.setForeground(Color.WHITE);
        }

        // Acción al hacer clic en el perfil
        if (!modoEdicion) {
            btnPerfil.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    entrarAplicacion(perfil);
                }
            });
        } else {
            btnPerfil.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editarPerfil(index);
                }
            });
        }

        // Efecto hover
        btnPerfil.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnPerfil.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
            }
            public void mouseExited(MouseEvent evt) {
                btnPerfil.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        });

        // Nombre del perfil
        JLabel lblNombre = new JLabel(perfil.getCliente().getNombre());
        lblNombre.setForeground(Color.GRAY);
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNombre.setHorizontalAlignment(JLabel.CENTER);

        panel.add(btnPerfil, BorderLayout.CENTER);
        
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.Y_AXIS));
        panelSur.setBackground(Color.BLACK);
        
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        panelSur.add(lblNombre);

        // Si estamos en modo edición, mostrar botón de editar
        if (modoEdicion) {
            panelSur.add(Box.createVerticalStrut(5));
            
            JButton btnEditar = new JButton("✏ Editar");
            btnEditar.setBackground(new Color(50, 50, 50));
            btnEditar.setForeground(Color.LIGHT_GRAY);
            btnEditar.setFont(new Font("Arial", Font.PLAIN, 12));
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            btnEditar.setAlignmentX(CENTER_ALIGNMENT);
            
            btnEditar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editarPerfil(index);
                }
            });
            
            panelSur.add(btnEditar);
        }

        panel.add(panelSur, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearBotonAnadir() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.BLACK);
        panel.setPreferredSize(new Dimension(150, modoEdicion ? 220 : 180));

        // Botón para añadir perfil
        JButton btnAnadir = new JButton("+");
        btnAnadir.setPreferredSize(new Dimension(130, 130));
        btnAnadir.setBackground(new Color(50, 50, 50));
        btnAnadir.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        btnAnadir.setFocusPainted(false);
        btnAnadir.setFont(new Font("Arial", Font.BOLD, 80));
        btnAnadir.setForeground(Color.GRAY);

        // Acción al hacer clic en añadir
        btnAnadir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anadirNuevoPerfil();
            }
        });

        // Efecto hover
        btnAnadir.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnAnadir.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                btnAnadir.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent evt) {
                btnAnadir.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                btnAnadir.setForeground(Color.GRAY);
            }
        });

        // Etiqueta
        JLabel lblAnadir = new JLabel("Añadir perfil");
        lblAnadir.setForeground(Color.GRAY);
        lblAnadir.setFont(new Font("Arial", Font.PLAIN, 16));
        lblAnadir.setHorizontalAlignment(JLabel.CENTER);

        panel.add(btnAnadir, BorderLayout.CENTER);
        panel.add(lblAnadir, BorderLayout.SOUTH);

        return panel;
    }

    private void anadirNuevoPerfil() {
        String nombrePerfil = JOptionPane.showInputDialog(this, 
            "Introduce el nombre del nuevo perfil:", 
            "Nuevo Perfil", 
            JOptionPane.PLAIN_MESSAGE);

        if (nombrePerfil != null && !nombrePerfil.trim().isEmpty()) {
            // Crear un nuevo cliente para el perfil
            // Puedes ajustar estos valores por defecto o pedir más información
            Cliente nuevoCliente = new Cliente(
                nombrePerfil.trim(), 
                "Apellido", 
                18, 
                "1234", 
                "Bilbao", 
                "000000000", 
                nombrePerfil.toLowerCase() + "@deustofilm.com"
            );

            // Colores disponibles para los perfiles
            Color[] coloresDisponibles = {
                new Color(51, 153, 255),
                new Color(255, 204, 0),
                new Color(229, 9, 20),
                new Color(76, 117, 130),
                new Color(153, 0, 153)
            };

            Color colorPerfil = coloresDisponibles[perfiles.size() % coloresDisponibles.length];

            perfiles.add(new PerfilCliente(nuevoCliente, colorPerfil, null));
            actualizarPerfiles();
        }
    }

    private void editarPerfil(int index) {
        PerfilCliente perfil = perfiles.get(index);
        
        String[] opciones = {"Cambiar nombre", "Cambiar color", "Cambiar imagen", "Eliminar imagen", "Eliminar perfil", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(this,
            "¿Qué deseas modificar de " + perfil.getCliente().getNombre() + "?",
            "Editar Perfil",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);

        switch (seleccion) {
            case 0: // Cambiar nombre
                String nuevoNombre = JOptionPane.showInputDialog(this,
                    "Introduce el nuevo nombre:",
                    perfil.getCliente().getNombre());
                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    perfil.getCliente().setNombre(nuevoNombre.trim());
                    actualizarPerfiles();
                }
                break;

            case 1: // Cambiar color
                Color nuevoColor = JColorChooser.showDialog(this,
                    "Selecciona un color para el perfil",
                    perfil.getColor());
                if (nuevoColor != null) {
                    perfil.setColor(nuevoColor);
                    actualizarPerfiles();
                }
                break;

            case 2: // Cambiar imagen
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleccionar imagen de perfil");

                // 🔹 Establecer carpeta inicial
                File carpetaInicial = new File("resources/images");
                if (carpetaInicial.exists()) {
                    fileChooser.setCurrentDirectory(carpetaInicial);
                }

                FileNameExtensionFilter filtro = new FileNameExtensionFilter(
                    "Imágenes (*.jpg, *.png, *.gif)", "jpg", "png", "gif", "jpeg");
                fileChooser.setFileFilter(filtro);

                int resultado = fileChooser.showOpenDialog(this);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    File archivoSeleccionado = fileChooser.getSelectedFile();
                    ImageIcon imagen = new ImageIcon(archivoSeleccionado.getAbsolutePath());
                    perfil.setImagen(imagen.getImage());
                    actualizarPerfiles();
                }
                break;


            case 3: // Eliminar imagen
                perfil.setImagen(null);
                actualizarPerfiles();
                break;

            case 4: // Eliminar perfil
                if (perfiles.size() > 1) {
                    int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro de que deseas eliminar el perfil '" + perfil.getCliente().getNombre() + "'?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        perfiles.remove(index);
                        actualizarPerfiles();
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Debe haber al menos un perfil.",
                        "No se puede eliminar",
                        JOptionPane.WARNING_MESSAGE);
                }
                break;

            case 5: // Cancelar
            default:
                break;
        }
    }

    private void entrarAplicacion(PerfilCliente perfil) {
        Cliente cliente = perfil.getCliente();
        
        JOptionPane.showMessageDialog(this, 
            "Entrando con el perfil: " + cliente.getNombre() + " " + cliente.getApellido(), 
            "Acceso", 
            JOptionPane.INFORMATION_MESSAGE);
        new VentanaPeliculasSeries();
        this.dispose();
    }

}
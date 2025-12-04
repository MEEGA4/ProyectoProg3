package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import db.GestorBD;
import domain.Cliente;
import domain.Trabajador;
import domain.Persona;

public class ProgressBar extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JProgressBar bar;
    private JButton pausa;
    private Counter counter;
    private JLabel lblTitulo;
    private JLabel lblEstado;
    private Boolean finished = false;
    private Persona usuario;
    private Boolean esCliente;
    private GestorBD gestorBD;
    
    // Paleta de colores (mismo estilo que VentanaSeleccionar)
    private final Color COLOR_FONDO = new Color(33, 37, 41);
    private final Color COLOR_AMARILLO = new Color(255, 193, 7);

    public ProgressBar(Persona usuario, Boolean esCliente, GestorBD gestorBD) {
        this.gestorBD = gestorBD;
        this.esCliente = esCliente;
        this.usuario = usuario;
        
        // Configuración de la ventana
        ImageIcon im = new ImageIcon("resources/images/logo.png");
        setIconImage(im.getImage());
        setTitle("DEUSTOFILM - Cargando");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Logo DEUSTOFILM
        lblTitulo = new JLabel("DEUSTOFILM");
        lblTitulo.setForeground(COLOR_AMARILLO);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 42));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(lblTitulo);
        
        mainPanel.add(Box.createVerticalStrut(40));
        
        // Etiqueta de estado
        lblEstado = new JLabel("Iniciando...");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 16));
        lblEstado.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(lblEstado);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Barra de progreso
        bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setForeground(COLOR_AMARILLO);
        bar.setBackground(new Color(52, 58, 64));
        bar.setFont(new Font("Arial", Font.BOLD, 14));
        bar.setMaximumSize(new Dimension(500, 30));
        bar.setPreferredSize(new Dimension(500, 30));
        bar.setBorder(BorderFactory.createLineBorder(COLOR_AMARILLO, 2));
        bar.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(bar);
        
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Botón pausa
        pausa = new JButton("PAUSAR");
        pausa.setPreferredSize(new Dimension(200, 40));
        pausa.setMaximumSize(new Dimension(200, 40));
        pausa.setAlignmentX(CENTER_ALIGNMENT);
        aplicarEstiloBoton(pausa, COLOR_AMARILLO);
        pausa.setEnabled(true);
        mainPanel.add(pausa);
        
        add(mainPanel);
        
        // Listeners
        pausa.addActionListener(e -> pauseCounter());
        
        // Iniciar contador automáticamente
        startCounter();
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
    
    public void fin() {
        if(finished) {
            if(esCliente) {
                SwingUtilities.invokeLater(() -> {
                    VentanaCliente ventanaCliente = new VentanaCliente((Cliente)usuario, gestorBD);
                    this.dispose();
                });  
            } else {
                SwingUtilities.invokeLater(() -> {
                    VentanaTrabajador ventanaTrabajador = new VentanaTrabajador((Trabajador)usuario, gestorBD);
                    this.dispose();
                }); 
            }
        }
    }

    public void startCounter() {
        if (counter == null || !counter.isAlive()) {
            counter = new Counter();
            counter.start();
        } else {
            counter.resumeCounting();
        }
    }

    public void pauseCounter() {
        if (counter != null) {
            if (counter.isPaused()) {
                counter.resumeCounting();
                pausa.setText("PAUSAR");
            } else {
                counter.pauseCounting();
                pausa.setText("REANUDAR");
            }
        }
    }

    private class Counter extends Thread {
        private int count = 0;
        private boolean paused = false;

        @Override
        public void run() {
            while (count <= 100 && !Thread.currentThread().isInterrupted()) {
                synchronized (this) {
                    while (paused) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }

                // Actualizar texto según progreso
                final String estado;
                if (count >= 0 && count <= 33) {
                    estado = "Cargando Películas...";
                } else if (count >= 34 && count <= 66) {
                    estado = "Cargando Series...";
                } else if (count >= 67 && count < 100) {
                    estado = "Preparando Catálogo...";
                } else {
                    estado = "¡Carga Completa!";
                    finished = true;
                }

                SwingUtilities.invokeLater(() -> {
                    bar.setValue(count);
                    bar.setString(count + "%");
                    lblEstado.setText(estado);
                });

                if (count == 100) {
                    fin();
                }

                count++;

                try {
                    Thread.sleep(30); // Velocidad de la barra
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        public synchronized void pauseCounting() {
            paused = true;
        }

        public synchronized void resumeCounting() {
            paused = false;
            notify();
        }
        
        public synchronized boolean isPaused() {
            return paused;
        }
    }
}
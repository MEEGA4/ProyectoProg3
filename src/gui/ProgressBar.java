package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import db.GestorBD;
import domain.Persona;
import domain.Cliente;
import domain.Trabajador;

public class ProgressBar extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar bar = new JProgressBar(0, 100);
    private JButton start = new JButton("Start");
    private JButton pausa = new JButton("Pause");
    private Counter counter;
    private JLabel label;
    private JLabel iconLabel;
    private Color color;
    private Boolean finished;
    private Persona usuario;
    private Boolean P ;
    private GestorBD gestorBD;

    public ProgressBar(Persona usuario ,Boolean p, GestorBD gestorBD) {
        // Set up the main layout
    	this.gestorBD = gestorBD;
    	 this.P=p;
    	this. usuario =  usuario;
    	 this.color = new Color(6,99,133);
        JPanel panel = new JPanel(new BorderLayout());
        JPanel grid = new JPanel(new BorderLayout());
        anadirColores(grid.getComponents(), color);
        panel.setBackground(Color.white);
        
        // Initialize progress bar and label
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setForeground(color);
        bar.setBackground(Color.white);
        
        // Initialize icon label
        iconLabel = new JLabel();
        
        //URL resource = "hospital.png";
        

        ImageIcon icon = new ImageIcon("resources/images/hospital.png");

     // Scale the image if necessary
     Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
     iconLabel.setIcon(new ImageIcon(scaledIcon));
        // Create an overlay panel to position the icon at the bar's tip
        JLayeredPane layeredPane = new JLayeredPane();
        bar.setBounds(50, 60, 400, 20); // Set progress bar bounds
        iconLabel.setBounds(50, 55, 30, 30); // Initial position of the icon above the bar
        
        layeredPane.add(bar, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(iconLabel, JLayeredPane.PALETTE_LAYER);
        layeredPane.setPreferredSize(new Dimension(500, 120));
        
        // Label for status text
        label = new JLabel("Initializing...", JLabel.CENTER);
        
        // Add components to grid and main panel
        grid.add(label, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.NORTH);
        panel.add(layeredPane, BorderLayout.CENTER);

        // Set up control buttons
        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(start);
     buttonPanel.add(pausa);
        anadirColores(buttonPanel.getComponents(), color);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        anadirColores(panel.getComponents(), color);

        add(panel);

        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Initialize button states and listeners
        start.setEnabled(true);
        pausa.setEnabled(false);

       // start.addActionListener(e -> startCounter());
        pausa.addActionListener(e -> pauseCounter());
        startCounter();
    }
    public void fin() {
        if(finished == true) {
            if(P == true) {
                // Para CLIENTES
                SwingUtilities.invokeLater(() -> {
                    VentanaCliente ventanaCliente = new VentanaCliente((Cliente)usuario, gestorBD);
                    ventanaCliente.setVisible(true);
                    this.dispose();
                });  
            } else if (P == false) {
                // Para TRABAJADORES
                SwingUtilities.invokeLater(() -> {
                    VentanaTrabajador ventanaTrabajador = new VentanaTrabajador((Trabajador)usuario, gestorBD);
                    ventanaTrabajador.setVisible(true);
                    this.dispose();
                }); 
            }
        }
    }

    public void startCounter() {
        start.setEnabled(false);
        pausa.setEnabled(true);

        if (counter == null || !counter.isAlive()) {
            counter = new Counter();
            counter.start();
        } else {
            counter.resumeCounting();
        }
    }

    public void pauseCounter() {
       
        pausa.setEnabled(true);
        if (counter != null) {
            if (counter.isPaused()) {
                counter.resumeCounting();  // Resume the thread
                pausa.setText("Pause");    // Update button text
            } else {
                counter.pauseCounting();   // Pause the thread
                pausa.setText("Resume");   // Update button text
            }
        }
        
    }
	private void anadirColores(Component[] components ,Color color) {
		for(Component component :components) {
			if(component instanceof JButton) {
		
			component.setBackground(color);
			component.setForeground(Color.white);
			component.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
			component.addMouseListener(new MouseAdapter() {
				 
		            @Override
		            public void mouseEntered(MouseEvent e) {
		            	component.setBackground(Color.white); 
		            	component.setForeground(color);
		            }

		            @Override
		            public void mouseExited(MouseEvent e) {
		            	component.setBackground(color); 
		            	component.setForeground(Color.white);
		            }
		        });
			} if(component instanceof JLabel ) {
				component.setBackground(Color.white);
				component.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

			} if(component instanceof JPanel) {
				JPanel componentN = (JPanel)component;
				componentN.setBackground(color.white);
				anadirColores(componentN.getComponents(), color);
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

                if (count >= 0 && count <= 33) {
                    label.setText("Cargando Películas");
                } else if (count >= 34 && count <= 66) {
                    label.setText("Cargando Series");
                } else if (count >= 67 && count < 100) {
                    label.setText("Cargando Catálogo");
                } else if (count == 100) {
                    label.setText("Carga Completa");
                    finished = true;
                    fin();
                }

                SwingUtilities.invokeLater(() -> {
                    bar.setValue(count);
                    bar.setString(count + "%");

                    // Update icon position to move along the tip of the progress bar
                    int barWidth = bar.getWidth();
                    int iconX = bar.getX() + (int) ((barWidth - iconLabel.getWidth()) * (count / 100.0));
                    iconLabel.setLocation(iconX, iconLabel.getY());
                });

                count++;

                try {
                    Thread.sleep(50); // Delay for visual effect
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


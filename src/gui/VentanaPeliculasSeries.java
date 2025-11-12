package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VentanaPeliculasSeries extends JFrame{
	
	
	private static final long serialVersionUID = 1L;
	
	JPanel panelCentro, panelOeste, pNorte;
	JLabel lblTodo, lblPeliculas, lblSeries;
	
	public VentanaPeliculasSeries() {
		
		//Esta ventana mostrará las películas y series disponibles en la plataforma
		
		
		setTitle("Películas y Series");
		
		panelCentro = new JPanel();
		
		
		panelOeste = new JPanel(new GridLayout(3,1, 200, 200));
		
		lblTodo = new JLabel("Todo");
		lblTodo.setHorizontalAlignment(JLabel.CENTER);
		lblPeliculas = new JLabel("Peliculas");
		lblPeliculas.setHorizontalAlignment(JLabel.CENTER);
		lblSeries = new JLabel("Series");
		lblSeries.setHorizontalAlignment(JLabel.CENTER);
		
		panelOeste.add(lblTodo);
		panelOeste.add(lblPeliculas);
		panelOeste.add(lblSeries);
			
		pNorte = new JPanel();
		
		getContentPane().add(panelCentro, BorderLayout.CENTER);
		getContentPane().add(panelOeste, BorderLayout.WEST);
		getContentPane().add(pNorte, BorderLayout.NORTH);

		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 200, 800, 600);
		setLocationRelativeTo(null);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new VentanaPeliculasSeries();
	}
}

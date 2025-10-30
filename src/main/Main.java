package main;

import javax.swing.SwingUtilities;

import gui.VentanaInicio;


public class Main {
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            VentanaInicio ventanaHistorial = new VentanaInicio();
        });
	}
}

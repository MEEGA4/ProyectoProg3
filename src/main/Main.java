package main;

import javax.swing.SwingUtilities;


import gui.VentanaSeleccion;


public class Main {
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            VentanaSeleccion ventana = new VentanaSeleccion();
        });
	}
}

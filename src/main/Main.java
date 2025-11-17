package main;

import javax.swing.SwingUtilities;


import gui.VentanaSeleccionar;


public class Main {
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            VentanaSeleccionar ventana = new VentanaSeleccionar();
        });
	}
}

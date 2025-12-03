package main;

import javax.swing.SwingUtilities;

import db.GestorBD;
import gui.VentanaSeleccionar;


public class Main {

	public static void main(String[] args) {
		// Crear instancia del GestorBD
		GestorBD gestor = new GestorBD();

		// PRIMERO: Inicializar la conexión a la base de datos
		gestor.initBD("resources/db/videoclub.db");

		// SEGUNDO: Crear la base de datos y tablas
		gestor.crearTablas();

		// TERCERO: Cargar datos desde archivos CSV
//		gestor.cargarClientesDesdeCSV("resources/data/clientes.csv");
//		gestor.cargarTrabajadoresDesdeCSV("resources/data/trabajadores.csv");
//		gestor.cargarSeriesDesdeCSV("resources/data/series.csv");
//		gestor.cargarPeliculasDesdeCSV("resources/data/peliculas.csv");

		// Cerrar la conexión después de cargar los datos
		//gestor.closeBD();

		SwingUtilities.invokeLater(() -> {
			VentanaSeleccionar ventana = new VentanaSeleccionar(gestor);
		});
	}
}
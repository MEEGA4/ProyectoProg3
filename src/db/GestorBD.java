package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import domain.Cliente;
import domain.Pelicula;
import domain.Persona;
import domain.Producto;
import domain.Serie;
import domain.Trabajador;

public class GestorBD {
	
	private Connection con; //Nos sirve para conectarnos con la base de datos
	
	/**
	 * Método que realiza la conexión con la base de datos
	 * 
	 * @param nombreBD : Nombre de la base de datos a la que nos vamos a conectar
	 */
	public void initBD(String nombreBD) {
		con = null;

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + nombreBD);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeBD() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void crearTablas() {
		// Tabla Persona (clase padre)
		String sql1 = "CREATE TABLE IF NOT EXISTS Persona ("
		    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
		    + "nombre VARCHAR(50) NOT NULL,"
		    + "apellido VARCHAR(50) NOT NULL,"
		    + "edad INTEGER,"
		    + "contrasena VARCHAR(100) NOT NULL,"
		    + "ubicacion VARCHAR(100))";

		// Tabla Cliente (hereda de Persona)
		String sql2 = "CREATE TABLE IF NOT EXISTS Cliente ("
		    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
		    + "persona_id INTEGER UNIQUE NOT NULL,"
		    + "telefono VARCHAR(15),"
		    + "email VARCHAR(50) UNIQUE NOT NULL,"
		    + "FOREIGN KEY (persona_id) REFERENCES Persona(id) ON DELETE CASCADE)";

		// Tabla Trabajador (hereda de Persona)
		String sql3 = "CREATE TABLE IF NOT EXISTS Trabajador ("
		    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
		    + "persona_id INTEGER UNIQUE NOT NULL,"
		    + "puesto VARCHAR(50),"
		    + "salario DOUBLE,"
		    + "FOREIGN KEY (persona_id) REFERENCES Persona(id) ON DELETE CASCADE)";

		// Tabla Producto (clase padre)
		String sql4 = "CREATE TABLE IF NOT EXISTS Producto ("
		    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
		    + "nombre VARCHAR(100) NOT NULL,"
		    + "descripcion VARCHAR(200),"
		    + "precio DOUBLE,"
		    + "stock INTEGER DEFAULT 0)";

		// Tabla Pelicula (hereda de Producto)
		String sql5 = "CREATE TABLE IF NOT EXISTS Pelicula ("
		    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
		    + "producto_id INTEGER UNIQUE NOT NULL,"
		    + "director VARCHAR(50),"
		    + "genero VARCHAR(30),"
		    + "duracion INTEGER,"
		    + "FOREIGN KEY (producto_id) REFERENCES Producto(id) ON DELETE CASCADE)";

		// Tabla Serie (hereda de Producto)
		String sql6 = "CREATE TABLE IF NOT EXISTS Serie ("
		    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
		    + "producto_id INTEGER UNIQUE NOT NULL,"
		    + "genero VARCHAR(30),"
		    + "temporadas INTEGER,"
		    + "FOREIGN KEY (producto_id) REFERENCES Producto(id) ON DELETE CASCADE)";
		
		try {
			Statement st = con.createStatement();
			st.executeUpdate(sql1);
			st.executeUpdate(sql2);
			st.executeUpdate(sql3);
			st.executeUpdate(sql4);
			st.executeUpdate(sql5);
			st.executeUpdate(sql6);
			st.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ==================== MÉTODOS PARA CARGA DESDE CSV ====================
	
	// Cargar Películas desde CSV
	public void cargarPeliculasDesdeCSV(String rutaArchivo) {
	    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(rutaArchivo))) {
	        String linea;
	        boolean primeraLinea = true;
	        int contador = 0;

	        System.out.println("\n- Cargando películas desde CSV...");

	        while ((linea = br.readLine()) != null) {
	            // Saltar la primera línea (encabezados)
	            if (primeraLinea) {
	                primeraLinea = false;
	                continue;
	            }

	            // Dividir la línea por coma
	            String[] datos = linea.split(",");

	            if (datos.length >= 7) {
	                String nombre = datos[0].trim();
	                String descripcion = datos[1].trim();
	                double precio = Double.parseDouble(datos[2].trim());
	                int stock = Integer.parseInt(datos[3].trim());
	                String director = datos[4].trim();
	                String genero = datos[5].trim();
	                int duracion = Integer.parseInt(datos[6].trim());

	                Pelicula pelicula = new Pelicula(nombre, descripcion, precio, stock, director, genero, duracion);
	                insertarPelicula(pelicula);
	                contador++;
	            }
	        }

	        System.out.format("\n- Se han cargado %d películas desde el archivo CSV", contador);

	    } catch (Exception ex) {
	        System.err.format("\n* Error al cargar películas desde CSV: %s", ex.getMessage());
	        ex.printStackTrace();
	    }
	}

	// Cargar Series desde CSV
	public void cargarSeriesDesdeCSV(String rutaArchivo) {
	    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(rutaArchivo))) {
	        String linea;
	        boolean primeraLinea = true;
	        int contador = 0;

	        System.out.println("\n- Cargando series desde CSV...");

	        while ((linea = br.readLine()) != null) {
	            // Saltar la primera línea (encabezados)
	            if (primeraLinea) {
	                primeraLinea = false;
	                continue;
	            }

	            // Dividir la línea por coma
	            String[] datos = linea.split(",");

	            if (datos.length >= 6) {
	                String nombre = datos[0].trim();
	                String descripcion = datos[1].trim();
	                double precio = Double.parseDouble(datos[2].trim());
	                int stock = Integer.parseInt(datos[3].trim());
	                String genero = datos[4].trim();
	                int temporadas = Integer.parseInt(datos[5].trim());

	                Serie serie = new Serie(nombre, descripcion, precio, stock, genero, temporadas);
	                insertarSerie(serie);
	                contador++;
	            }
	        }

	        System.out.format("\n- Se han cargado %d series desde el archivo CSV", contador);

	    } catch (Exception ex) {
	        System.err.format("\n* Error al cargar series desde CSV: %s", ex.getMessage());
	        ex.printStackTrace();
	    }
	}

	// Cargar Clientes desde CSV
	public void cargarClientesDesdeCSV(String rutaArchivo) {
	    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(rutaArchivo))) {
	        String linea;
	        boolean primeraLinea = true;
	        int contador = 0;

	        System.out.println("\n- Cargando clientes desde CSV...");

	        while ((linea = br.readLine()) != null) {
	            // Saltar la primera línea (encabezados)
	            if (primeraLinea) {
	                primeraLinea = false;
	                continue;
	            }

	            // Dividir la línea por coma
	            String[] datos = linea.split(",");

	            if (datos.length >= 7) {
	                String nombre = datos[0].trim();
	                String apellido = datos[1].trim();
	                int edad = Integer.parseInt(datos[2].trim());
	                String contrasena = datos[3].trim();
	                String ubicacion = datos[4].trim();
	                String telefono = datos[5].trim();
	                String email = datos[6].trim();

	                Cliente cliente = new Cliente(nombre, apellido, edad, contrasena, ubicacion, telefono, email);
	                insertarCliente(cliente);
	                contador++;
	            }
	        }

	        System.out.format("\n- Se han cargado %d clientes desde el archivo CSV", contador);

	    } catch (Exception ex) {
	        System.err.format("\n* Error al cargar clientes desde CSV: %s", ex.getMessage());
	        ex.printStackTrace();
	    }
	}

	// Cargar Trabajadores desde CSV
	public void cargarTrabajadoresDesdeCSV(String rutaArchivo) {
	    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(rutaArchivo))) {
	        String linea;
	        boolean primeraLinea = true;
	        int contador = 0;

	        System.out.println("\n- Cargando trabajadores desde CSV...");

	        while ((linea = br.readLine()) != null) {
	            // Saltar la primera línea (encabezados)
	            if (primeraLinea) {
	                primeraLinea = false;
	                continue;
	            }

	            // Dividir la línea por coma
	            String[] datos = linea.split(",");

	            if (datos.length >= 7) {
	                String nombre = datos[0].trim();
	                String apellido = datos[1].trim();
	                int edad = Integer.parseInt(datos[2].trim());
	                String contrasena = datos[3].trim();
	                String ubicacion = datos[4].trim();
	                String puesto = datos[5].trim();
	                double salario = Double.parseDouble(datos[6].trim());

	                Trabajador trabajador = new Trabajador(nombre, apellido, edad, contrasena, ubicacion, puesto, salario);
	                insertarTrabajador(trabajador);
	                contador++;
	            }
	        }

	        System.out.format("\n- Se han cargado %d trabajadores desde el archivo CSV", contador);

	    } catch (Exception ex) {
	        System.err.format("\n* Error al cargar trabajadores desde CSV: %s", ex.getMessage());
	        ex.printStackTrace();
	    }
	}
	
	// ==================== MÉTODOS PARA CLIENTES ====================

	// Insertar uno o varios clientes
	public void insertarCliente(Cliente... clientes) {
	    try {
	        con.setAutoCommit(false); // Iniciar transacción
	        
	        String sqlPersona = "INSERT INTO Persona (nombre, apellido, edad, contrasena, ubicacion) VALUES (?, ?, ?, ?, ?);";
	        String sqlCliente = "INSERT INTO Cliente (persona_id, telefono, email) VALUES (last_insert_rowid(), ?, ?);";
	        
	        PreparedStatement pstmtPersona = con.prepareStatement(sqlPersona);
	        PreparedStatement pstmtCliente = con.prepareStatement(sqlCliente);
	        
	        System.out.println("- Insertando clientes...");
	        
	        for (Cliente c : clientes) {
	            // Insertar en Persona
	            pstmtPersona.setString(1, c.getNombre());
	            pstmtPersona.setString(2, c.getApellido());
	            pstmtPersona.setInt(3, c.getEdad());
	            pstmtPersona.setString(4, c.getContrasena());
	            pstmtPersona.setString(5, c.getUbicacion());
	            pstmtPersona.executeUpdate();
	            
	            // Insertar en Cliente
	            pstmtCliente.setString(1, c.getTelefono());
	            pstmtCliente.setString(2, c.getEmail());
	            
	            if (1 == pstmtCliente.executeUpdate()) {
	                System.out.format("\n - Cliente insertado: %s", c.toString());
	            } else {
	                System.out.format("\n - No se ha insertado el cliente: %s", c.toString());
	            }
	        }
	        
	        con.commit(); // Confirmar transacción
	        pstmtPersona.close();
	        pstmtCliente.close();
	    } catch (Exception ex) {
	        System.err.format("\n* Error al insertar clientes en la BBDD: %s", ex.getMessage());
	        ex.printStackTrace();
	    }
	}

	// ==================== MÉTODOS PARA TRABAJADORES ====================

	// Insertar uno o varios trabajadores
	public void insertarTrabajador(Trabajador... trabajadores) {
	    try {
	        con.setAutoCommit(false); // Iniciar transacción
	        
	        String sqlPersona = "INSERT INTO Persona (nombre, apellido, edad, contrasena, ubicacion) VALUES (?, ?, ?, ?, ?);";
	        String sqlTrabajador = "INSERT INTO Trabajador (persona_id, puesto, salario) VALUES (last_insert_rowid(), ?, ?);";
	        
	        PreparedStatement pstmtPersona = con.prepareStatement(sqlPersona);
	        PreparedStatement pstmtTrabajador = con.prepareStatement(sqlTrabajador);
	        
	        System.out.println("- Insertando trabajadores...");
	        
	        for (Trabajador t : trabajadores) {
	            // Insertar en Persona
	            pstmtPersona.setString(1, t.getNombre());
	            pstmtPersona.setString(2, t.getApellido());
	            pstmtPersona.setInt(3, t.getEdad());
	            pstmtPersona.setString(4, t.getContrasena());
	            pstmtPersona.setString(5, t.getUbicacion());
	            pstmtPersona.executeUpdate();
	            
	            // Insertar en Trabajador
	            pstmtTrabajador.setString(1, t.getPuesto());
	            pstmtTrabajador.setDouble(2, t.getSalario());
	            
	            if (1 == pstmtTrabajador.executeUpdate()) {
	                System.out.format("\n - Trabajador insertado: %s", t.toString());
	            } else {
	                System.out.format("\n - No se ha insertado el trabajador: %s", t.toString());
	            }
	        }
	        
	        con.commit(); // Confirmar transacción
	        pstmtPersona.close();
	        pstmtTrabajador.close();
	    } catch (Exception ex) {
	        System.err.format("\n* Error al insertar trabajadores en la BBDD: %s", ex.getMessage());
	        ex.printStackTrace();
	    }
	}

	// ==================== MÉTODOS PARA PELICULAS ====================

	// Insertar una o varias películas
	public void insertarPelicula(Pelicula... peliculas) {
	    try {
	        con.setAutoCommit(false); // Iniciar transacción
	        
	        String sqlProducto = "INSERT INTO Producto (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?);";
	        String sqlPelicula = "INSERT INTO Pelicula (producto_id, director, genero, duracion) VALUES (last_insert_rowid(), ?, ?, ?);";
	        
	        PreparedStatement pstmtProducto = con.prepareStatement(sqlProducto);
	        PreparedStatement pstmtPelicula = con.prepareStatement(sqlPelicula);
	        
	        System.out.println("- Insertando películas...");
	        
	        for (Pelicula p : peliculas) {
	            // Insertar en Producto
	            pstmtProducto.setString(1, p.getNombre());
	            pstmtProducto.setString(2, p.getDescripcion());
	            pstmtProducto.setDouble(3, p.getPrecio());
	            pstmtProducto.setInt(4, p.getStock());
	            pstmtProducto.executeUpdate();
	            
	            // Insertar en Pelicula
	            pstmtPelicula.setString(1, p.getDirector());
	            pstmtPelicula.setString(2, p.getGenero());
	            pstmtPelicula.setInt(3, p.getDuracion());
	            
	            if (1 == pstmtPelicula.executeUpdate()) {
	                System.out.format("\n - Película insertada: %s", p.toString());
	            } else {
	                System.out.format("\n - No se ha insertado la película: %s", p.toString());
	            }
	        }
	        
	        con.commit(); // Confirmar transacción
	        pstmtProducto.close();
	        pstmtPelicula.close();
	    } catch (Exception ex) {
	        System.err.format("\n* Error al insertar películas en la BBDD: %s", ex.getMessage());
	        ex.printStackTrace();
	    }
	}

	// ==================== MÉTODOS PARA SERIES ====================

	// Insertar una o varias series
	public void insertarSerie(Serie... series) {
	    try {
	        con.setAutoCommit(false); // Iniciar transacción
	        
	        String sqlProducto = "INSERT INTO Producto (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?);";
	        String sqlSerie = "INSERT INTO Serie (producto_id, genero, temporadas) VALUES (last_insert_rowid(), ?, ?);";
	        
	        PreparedStatement pstmtProducto = con.prepareStatement(sqlProducto);
	        PreparedStatement pstmtSerie = con.prepareStatement(sqlSerie);
	        
	        System.out.println("- Insertando series...");
	        
	        for (Serie s : series) {
	            // Insertar en Producto
	            pstmtProducto.setString(1, s.getNombre());
	            pstmtProducto.setString(2, s.getDescripcion());
	            pstmtProducto.setDouble(3, s.getPrecio());
	            pstmtProducto.setInt(4, s.getStock());
	            pstmtProducto.executeUpdate();
	            
	            // Insertar en Serie
	            pstmtSerie.setString(1, s.getGenero());
	            pstmtSerie.setInt(2, s.getTemporadas());
	            
	            if (1 == pstmtSerie.executeUpdate()) {
	                System.out.format("\n - Serie insertada: %s", s.toString());
	            } else {
	                System.out.format("\n - No se ha insertado la serie: %s", s.toString());
	            }
	        }
	        
	        con.commit(); // Confirmar transacción
	        pstmtProducto.close();
	        pstmtSerie.close();
	    } catch (Exception ex) {
	        System.err.format("\n* Error al insertar series en la BBDD: %s", ex.getMessage());
	        ex.printStackTrace();
	    }
	}
	
}
package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import domain.Cliente;
import domain.Pelicula;
import domain.PerfilSecundario;
import domain.Serie;
import domain.Trabajador;

public class GestorBD {

	private Connection con; // Nos sirve para conectarnos con la base de datos

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

		// Tabla PerfilSecundario (perfiles adicionales para un cliente)
		String sql7 = "CREATE TABLE IF NOT EXISTS PerfilSecundario ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nombre VARCHAR(50) NOT NULL,"
				+ "cliente_id INTEGER NOT NULL,"
				+ "FOREIGN KEY (cliente_id) REFERENCES Cliente(id) ON DELETE CASCADE)";

		try {
			Statement st = con.createStatement();
			st.executeUpdate(sql1);
			st.executeUpdate(sql2);
			st.executeUpdate(sql3);
			st.executeUpdate(sql4);
			st.executeUpdate(sql5);
			st.executeUpdate(sql6);
			st.executeUpdate(sql7);
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ==================== MÉTODOS PARA CARGA DESDE CSV ====================

	// Cargar Películas desde CSV
	public void cargarPeliculasDesdeCSV(String rutaArchivo) {
		try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
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
		try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
			String linea;
			
			int contador = 0;

			System.out.println("\n- Cargando series desde CSV...");

			while ((linea = br.readLine()) != null) {
				

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
		try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
			String linea;
			
			int contador = 0;

			System.out.println("\n- Cargando clientes desde CSV...");

			while ((linea = br.readLine()) != null) {
				

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
		try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
			String linea;
			
			int contador = 0;

			System.out.println("\n- Cargando trabajadores desde CSV...");

			while ((linea = br.readLine()) != null) {
				

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

					Trabajador trabajador = new Trabajador(nombre, apellido, edad, contrasena, ubicacion, puesto,
							salario);
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

	// Obtener todas las películas
	public List<Pelicula> obtenerPeliculas() {
		List<Pelicula> peliculas = new ArrayList<>();

		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return peliculas;
		}

		String sql = "SELECT pl.id, pr.nombre, pr.descripcion, pr.precio, pr.stock, pl.director, pl.genero, pl.duracion "
				+
				"FROM Pelicula pl JOIN Producto pr ON pl.producto_id = pr.id " +
				"WHERE pl.id >= ?";

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, 0);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Pelicula p = new Pelicula();
				p.setId(rs.getInt("id"));
				p.setNombre(rs.getString("nombre"));
				p.setDescripcion(rs.getString("descripcion"));
				p.setPrecio(rs.getDouble("precio"));
				p.setStock(rs.getInt("stock"));
				p.setDirector(rs.getString("director"));
				p.setGenero(rs.getString("genero"));
				p.setDuracion(rs.getInt("duracion"));

				peliculas.add(p);
			}

			System.out.format("\n- Se han recuperado %d películas...\n", peliculas.size());
			rs.close();

		} catch (Exception ex) {
			System.err.format("\n* Error al obtener películas de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}

		return peliculas;
	}

	// Obtener todas las series
	public List<Serie> obtenerSeries() {
		List<Serie> series = new ArrayList<>();

		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return series;
		}

		String sql = "SELECT s.id, pr.nombre, pr.descripcion, pr.precio, pr.stock, s.genero, s.temporadas " +
				"FROM Serie s JOIN Producto pr ON s.producto_id = pr.id " +
				"WHERE s.id >= ?";

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, 0);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Serie s = new Serie();
				s.setId(rs.getInt("id"));
				s.setNombre(rs.getString("nombre"));
				s.setDescripcion(rs.getString("descripcion"));
				s.setPrecio(rs.getDouble("precio"));
				s.setStock(rs.getInt("stock"));
				s.setGenero(rs.getString("genero"));
				s.setTemporadas(rs.getInt("temporadas"));

				series.add(s);
			}

			System.out.format("\n- Se han recuperado %d series...\n", series.size());
			rs.close();

		} catch (Exception ex) {
			System.err.format("\n* Error al obtener series de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}

		return series;
	}

	public List<Cliente> obtenerClientes() {
		List<Cliente> clientes = new ArrayList<>();

		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return clientes;
		}

		String sql = "SELECT c.id, p.nombre, p.apellido, p.edad, p.contrasena, p.ubicacion, c.telefono, c.email " +
				"FROM Cliente c JOIN Persona p ON c.persona_id = p.id " +
				"WHERE c.id >= ?";

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, 0);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Cliente c = new Cliente();
				c.setId(rs.getInt("id"));
				c.setNombre(rs.getString("nombre"));
				c.setApellido(rs.getString("apellido"));
				c.setEdad(rs.getInt("edad"));
				c.setContrasena(rs.getString("contrasena"));
				c.setUbicacion(rs.getString("ubicacion"));
				c.setTelefono(rs.getString("telefono"));
				c.setEmail(rs.getString("email"));

				clientes.add(c);
			}

			System.out.format("\n- Se han recuperado %d clientes...\n", clientes.size());
			rs.close();

		} catch (Exception ex) {
			System.err.format("\n* Error al obtener clientes de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}

		return clientes;
	}

	public List<Trabajador> obtenerTrabajadores() {
		List<Trabajador> trabajadores = new ArrayList<>();

		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return trabajadores;
		}

		String sql = "SELECT t.id, p.nombre, p.apellido, p.edad, p.contrasena, p.ubicacion, t.puesto, t.salario " +
				"FROM Trabajador t JOIN Persona p ON t.persona_id = p.id " +
				"WHERE t.id >= ?";

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, 0);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Trabajador t = new Trabajador();
				t.setId(rs.getInt("id"));
				t.setNombre(rs.getString("nombre"));
				t.setApellido(rs.getString("apellido"));
				t.setEdad(rs.getInt("edad"));
				t.setContrasena(rs.getString("contrasena"));
				t.setUbicacion(rs.getString("ubicacion"));
				t.setPuesto(rs.getString("puesto"));
				t.setSalario(rs.getDouble("salario"));

				trabajadores.add(t);
			}

			System.out.format("\n- Se han recuperado %d trabajadores...\n", trabajadores.size());
			rs.close();

		} catch (Exception ex) {
			System.err.format("\n* Error al obtener trabajadores de la BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}

		return trabajadores;
	}

	// ==================== MÉTODOS PARA ELIMINAR DATOS ====================

	/**
	 * Elimina un cliente de la base de datos por su ID
	 * 
	 * @param id ID del cliente a eliminar
	 */
	public void eliminarCliente(int id) {
		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return;
		}

		try {
			con.setAutoCommit(false);

			// Eliminar de la tabla Cliente (esto también eliminará de Persona por CASCADE)
			String sqlCliente = "DELETE FROM Cliente WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(sqlCliente);
			pstmt.setInt(1, id);

			int filasAfectadas = pstmt.executeUpdate();

			if (filasAfectadas > 0) {
				System.out.format("\n- Cliente con ID %d eliminado correctamente", id);
			} else {
				System.out.format("\n- No se encontró cliente con ID %d", id);
			}

			con.commit();
			pstmt.close();
		} catch (SQLException e) {
			System.err.format("\n* Error al eliminar cliente: %s", e.getMessage());
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Elimina una película de la base de datos por su ID
	 * 
	 * @param id ID de la película a eliminar
	 */
	public void eliminarPelicula(int id) {
		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return;
		}

		try {
			con.setAutoCommit(false);

			// Eliminar de la tabla Pelicula (esto también eliminará de Producto por
			// CASCADE)
			String sqlPelicula = "DELETE FROM Pelicula WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(sqlPelicula);
			pstmt.setInt(1, id);

			int filasAfectadas = pstmt.executeUpdate();

			if (filasAfectadas > 0) {
				System.out.format("\n- Película con ID %d eliminada correctamente", id);
			} else {
				System.out.format("\n- No se encontró película con ID %d", id);
			}

			con.commit();
			pstmt.close();
		} catch (SQLException e) {
			System.err.format("\n* Error al eliminar película: %s", e.getMessage());
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Elimina una serie de la base de datos por su ID
	 * 
	 * @param id ID de la serie a eliminar
	 */
	public void eliminarSerie(int id) {
		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return;
		}

		try {
			con.setAutoCommit(false);

			// Eliminar de la tabla Serie (esto también eliminará de Producto por CASCADE)
			String sqlSerie = "DELETE FROM Serie WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(sqlSerie);
			pstmt.setInt(1, id);

			int filasAfectadas = pstmt.executeUpdate();

			if (filasAfectadas > 0) {
				System.out.format("\n- Serie con ID %d eliminada correctamente", id);
			} else {
				System.out.format("\n- No se encontró serie con ID %d", id);
			}

			con.commit();
			pstmt.close();
		} catch (SQLException e) {
			System.err.format("\n* Error al eliminar serie: %s", e.getMessage());
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}


	// ==================== MÉTODOS PARA PERFILES SECUNDARIOS ====================
	
	/**
	 * Inserta un perfil secundario en la base de datos
	 */
	public void insertarPerfilSecundario(PerfilSecundario perfil) {
		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return;
		}
		
		try {
			String sql = "INSERT INTO PerfilSecundario (nombre, cliente_id) VALUES (?, ?)";
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, perfil.getNombre());
			pstmt.setInt(2, perfil.getClienteId());
			
			if (1 == pstmt.executeUpdate()) {
				System.out.format("\n- Perfil secundario insertado: %s", perfil.toString());
			} else {
				System.out.format("\n- No se ha insertado el perfil secundario: %s", perfil.toString());
			}
			
			pstmt.close();
		} catch (SQLException e) {
			System.err.format("\n* Error al insertar perfil secundario: %s", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtiene todos los perfiles secundarios de un cliente
	 */
	public List<PerfilSecundario> obtenerPerfilesSecundarios(int clienteId) {
		List<PerfilSecundario> perfiles = new ArrayList<>();
		
		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return perfiles;
		}
		
		String sql = "SELECT id, nombre, cliente_id FROM PerfilSecundario WHERE cliente_id = ?";
		
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, clienteId);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				PerfilSecundario perfil = new PerfilSecundario();
				perfil.setId(rs.getInt("id"));
				perfil.setNombre(rs.getString("nombre"));
				perfil.setClienteId(rs.getInt("cliente_id"));
				
				perfiles.add(perfil);
			}
			
			System.out.format("\n- Se han recuperado %d perfiles secundarios para el cliente %d\n", perfiles.size(), clienteId);
			rs.close();
			
		} catch (SQLException e) {
			System.err.format("\n* Error al obtener perfiles secundarios: %s", e.getMessage());
			e.printStackTrace();
		}
		
		return perfiles;
	}
	
	/**
	 * Actualiza el nombre de un perfil secundario
	 */
	public void actualizarPerfilSecundario(int perfilId, String nuevoNombre) {
		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return;
		}
		
		try {
			String sql = "UPDATE PerfilSecundario SET nombre = ? WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, nuevoNombre);
			pstmt.setInt(2, perfilId);
			
			int filasAfectadas = pstmt.executeUpdate();
			
			if (filasAfectadas > 0) {
				System.out.format("\n- Perfil secundario actualizado correctamente: ID %d", perfilId);
			} else {
				System.out.format("\n- No se encontró perfil secundario con ID %d", perfilId);
			}
			
			pstmt.close();
		} catch (SQLException e) {
			System.err.format("\n* Error al actualizar perfil secundario: %s", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Elimina un perfil secundario de la base de datos
	 */
	public void eliminarPerfilSecundario(int perfilId) {
		if (con == null) {
			System.err.println("* La conexión con la BBDD no está inicializada.");
			return;
		}
		
		try {
			String sql = "DELETE FROM PerfilSecundario WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, perfilId);
			
			int filasAfectadas = pstmt.executeUpdate();
			
			if (filasAfectadas > 0) {
				System.out.format("\n- Perfil secundario eliminado correctamente: ID %d", perfilId);
			} else {
				System.out.format("\n- No se encontró perfil secundario con ID %d", perfilId);
			}
			
			pstmt.close();
		} catch (SQLException e) {
			System.err.format("\n* Error al eliminar perfil secundario: %s", e.getMessage());
			e.printStackTrace();
		}
	}

}
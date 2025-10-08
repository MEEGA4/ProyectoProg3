package domain;


public class Pelicula extends Producto{
	private String director;
	private String genero;
	private int duracion; // duracion en minutos
	
	public Pelicula(String nombre, String descripcion, double precio, int stock, String director, String genero,
			int duracion) {
		super(nombre, descripcion, precio, stock);
		this.director = director;
		this.genero = genero;
		this.duracion = duracion;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	@Override
	public String toString() {
		return "Pelicula [director=" + director + ", genero=" + genero + ", duracion=" + duracion + ", nombre="
				+ nombre + ", descripcion=" + descripcion + ", precio=" + precio + ", stock=" + stock + "]";
	}
}

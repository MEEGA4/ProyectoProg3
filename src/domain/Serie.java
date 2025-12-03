package domain;

public class Serie extends Producto {
    private String genero;
    private int temporadas;
    private int numEpisodios;

    // Constructor vacío (para recuperar de la BBDD)
    public Serie() {}

    // Constructor básico
    public Serie(String nombre, String descripcion, double precio, int stock) {
        super(nombre, descripcion, precio, stock);
    }

    // Constructor completo
    public Serie(String nombre, String descripcion, double precio, int stock, String genero, int temporadas) {
        super(nombre, descripcion, precio, stock);
        this.genero = genero;
        this.temporadas = temporadas;
    }

    // Getters y setters
    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }

    public int getNumEpisodios() {
        return numEpisodios;
    }

    public void setNumEpisodios(int numEpisodios) {
        this.numEpisodios = numEpisodios;
    }

    @Override
    public String toString() {
        return "Serie [id=" + id + ", genero=" + genero + ", temporadas=" + temporadas + ", numEpisodios=" + numEpisodios
                + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio + ", stock=" + stock + "]";
    }
}

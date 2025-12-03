package domain;

public class Persona {
    protected int id; // ID de la base de datos
    protected String nombre;
    protected String apellido;
    protected int edad;
    protected String contrasena;
    protected String ubicacion;

    // Constructor vacío (para recuperar de la BBDD)
    public Persona() {}

    // Constructor completo
    public Persona(String nombre, String apellido, int edad, String contrasena, String ubicacion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.contrasena = contrasena;
        this.ubicacion = ubicacion;
    }

    // Getter y setter de ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters y setters del resto de atributos
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        return "Persona [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", edad=" + edad
                + ", contrasena=" + contrasena + ", ubicacion=" + ubicacion + "]";
    }
}

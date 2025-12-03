package domain;

public class Trabajador extends Persona {
    private int id; // ID de la base de datos
    private String puesto;
    private double salario;

    // Constructor vacío (para recuperar de la BBDD)
    public Trabajador() {}

    // Constructor sin puesto y salario
    public Trabajador(String nombre, String apellido, int edad, String contrasena, String ubicacion) {
        super(nombre, apellido, edad, contrasena, ubicacion);
    }

    // Constructor completo
    public Trabajador(String nombre, String apellido, int edad, String contrasena, String ubicacion,
                      String puesto, double salario) {
        super(nombre, apellido, edad, contrasena, ubicacion);
        this.puesto = puesto;
        this.salario = salario;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Trabajador [id=" + id + ", puesto=" + puesto + ", salario=" + salario
                + ", nombre=" + nombre + ", apellido=" + apellido + ", edad=" + edad
                + ", contrasena=" + contrasena + ", ubicacion=" + ubicacion + "]";
    }
}

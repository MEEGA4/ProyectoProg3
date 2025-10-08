package domain;


public class Trabajador extends Persona {
	private String puesto;
	private double salario;
	
	public Trabajador(String nombre, String apellido, int edad, String contrasena, String ubicacion, String puesto, double salario) {
		super(nombre, apellido, edad, contrasena, ubicacion);
		this.puesto = puesto;
		this.salario = salario;
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
		return "Trabajador [puesto=" + puesto + ", salario=" + salario + ", nombre=" + nombre + ", apellido=" + apellido
				+ ", edad=" + edad + ", contrasena=" + contrasena + ", ubicacion=" + ubicacion + "]";
	}

}

package domain;


public class Cliente extends Persona {
	private String telefono;
	private String email;
	
	
	public Cliente(String nombre, String apellido, int edad, String contrasena, String ubicacion, String telefono, String email) {
		super(nombre, apellido, edad, contrasena, ubicacion);
		this.telefono = telefono;
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Cliente [telefono=" + telefono + ", email=" + email + ", nombre=" + nombre + ", apellido=" + apellido
				+ ", edad=" + edad + ", contrasena=" + contrasena + ", ubicacion=" + ubicacion + "]";
	}

}

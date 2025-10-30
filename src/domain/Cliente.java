package domain;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona {
	private String telefono;
	private String email;
	List<Producto> lVistos;
	List<Producto> lFavoritos;
	
	public Cliente(String nombre, String apellido, int edad, String contrasena, String ubicacion, String telefono, String email) {
		super(nombre, apellido, edad, contrasena, ubicacion);
		this.telefono = telefono;
		this.email = email;
		this.lVistos = new ArrayList<>();
		this.lFavoritos = new ArrayList<>();
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

	public List<Producto> getlVistos() {
		return lVistos;
	}

	public void setlVistos(List<Producto> lVistos) {
		this.lVistos = lVistos;
	}

	public List<Producto> getlFavoritos() {
		return lFavoritos;
	}

	public void setlFavoritos(List<Producto> lFavoritos) {
		this.lFavoritos = lFavoritos;
	}

	@Override
	public String toString() {
		return "Cliente [telefono=" + telefono + ", email=" + email + ", lVistos=" + lVistos + ", lFavoritos="
				+ lFavoritos + ", nombre=" + nombre + ", apellido=" + apellido + ", edad=" + edad + ", contrasena="
				+ contrasena + ", ubicacion=" + ubicacion + "]";
	}

	
}

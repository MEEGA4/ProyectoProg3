package domain;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona {
    private int id; // ID de la base de datos
    private String telefono;
    private String email;
    private List<Producto> lVistos;
    private List<Producto> lFavoritos;

    // Constructor vacío (para recuperar de la BBDD)
    public Cliente() {
        this.lVistos = new ArrayList<>();
        this.lFavoritos = new ArrayList<>();
    }

    // Constructor completo
    public Cliente(String nombre, String apellido, int edad, String contrasena, String ubicacion, String telefono, String email) {
        super(nombre, apellido, edad, contrasena, ubicacion);
        this.telefono = telefono;
        this.email = email;
        this.lVistos = new ArrayList<>();
        this.lFavoritos = new ArrayList<>();
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "Cliente [id=" + id + ", telefono=" + telefono + ", email=" + email + ", lVistos=" + lVistos
                + ", lFavoritos=" + lFavoritos + ", nombre=" + nombre + ", apellido=" + apellido
                + ", edad=" + edad + ", contrasena=" + contrasena + ", ubicacion=" + ubicacion + "]";
    }
}

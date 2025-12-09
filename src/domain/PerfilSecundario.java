package domain;

public class PerfilSecundario {
    private int id; 
    private String nombre;
    private int clienteId; 
    
 
    public PerfilSecundario() {}
    

    public PerfilSecundario(String nombre, int clienteId) {
        this.nombre = nombre;
        this.clienteId = clienteId;
    }
    

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
    
    @Override
    public String toString() {
        return "PerfilSecundario [id=" + id + ", nombre=" + nombre + ", clienteId=" + clienteId + "]";
    }
}
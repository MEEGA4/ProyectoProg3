package domain;

import java.awt.Color;
import java.awt.Image;

public class PerfilCliente {
	private Cliente cliente;
    private Color color;
    private Image imagen;

    public PerfilCliente(Cliente cliente, Color color, Image imagen) {
        this.cliente = cliente;
        this.color = color;
        this.imagen = imagen;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }
}

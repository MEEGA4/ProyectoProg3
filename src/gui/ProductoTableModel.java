package gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import domain.Pelicula;
import domain.Producto;
import domain.Serie;

public class ProductoTableModel extends AbstractTableModel {
    private final List<String> columnas;
    private final List<Producto> data;

    public ProductoTableModel(List<String> titulos, List<Producto> productos) {
        this.columnas = titulos != null ? new ArrayList<>(titulos) : new ArrayList<>();
        this.data = productos != null ? new ArrayList<>(productos) : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnas.get(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        String name = columnas.get(columnIndex).toLowerCase();
        switch (name) {
            case "precio":
                return Double.class;
            case "stock":
            case "duración":
            case "duracion":
            case "temporadas":
            case "episodios":
                return Integer.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Producto p = data.get(rowIndex);
        String name = columnas.get(columnIndex).toLowerCase();

        switch (name) {
            case "tipo":
                return p instanceof Pelicula ? "Película" : "Serie";
            case "nombre":
                return p.getNombre();
            case "descripción":
            case "descripcion":
                return p.getDescripcion();
            case "precio":
                return p.getPrecio();
            case "stock":
                return p.getStock();
            case "director":
                return (p instanceof Pelicula) ? ((Pelicula) p).getDirector() : "";
            case "género":
            case "genero":
                if (p instanceof Pelicula)
                    return ((Pelicula) p).getGenero();
                else if (p instanceof Serie)
                    return ((Serie) p).getGenero();
                return "";
            case "duración":
            case "duracion":
                return (p instanceof Pelicula) ? ((Pelicula) p).getDuracion() : null;
            case "temporadas":
                return (p instanceof Serie) ? ((Serie) p).getTemporadas() : null;
            case "episodios":
                return (p instanceof Serie) ? ((Serie) p).getNumEpisodios() : null;
            default:
                return null;
        }
    }

    public void addProducto(Producto p) {
        int idx = data.size();
        data.add(p);
        fireTableRowsInserted(idx, idx);
    }

    public void removeRows(int[] rows) {
        if (rows == null || rows.length == 0)
            return;
        for (int i = rows.length - 1; i >= 0; i--) {
            int r = rows[i];
            if (r >= 0 && r < data.size()) {
                data.remove(r);
            }
        }
        fireTableDataChanged();
    }

    public Producto getProductoAt(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index);
        }
        return null;
    }
}
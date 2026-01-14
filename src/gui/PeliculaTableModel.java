package gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import domain.Pelicula;

public class PeliculaTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8111064390217507876L;
	private final List<String> columnas;
    private final List<Pelicula> data;

    public PeliculaTableModel(List<String> titulos, List<Pelicula> peliculas) {
        this.columnas = titulos != null ? new ArrayList<>(titulos) : new ArrayList<>();
        this.data = peliculas != null ? new ArrayList<>(peliculas) : new ArrayList<>();
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
        Pelicula p = data.get(rowIndex);
        String name = columnas.get(columnIndex).toLowerCase();
        switch (name) {
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
                return p.getDirector();
            case "género":
            case "genero":
                return p.getGenero();
            case "duración":
            case "duracion":
                return p.getDuracion();
            default:
                return null;
        }
    }

    public void addPelicula(Pelicula p) {
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

    public Pelicula getPeliculaAt(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index);
        }
        return null;
    }
}
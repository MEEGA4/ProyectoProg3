package gui;

import javax.swing.table.AbstractTableModel;

import domain.Pelicula;

import java.util.ArrayList;
import java.util.List;

public class PeliculaTablaTableModel extends AbstractTableModel {
    private final String[] columnas = {"ID", "Título", "Duración (min)", "Género", "Año", "Calificación"};
    private List<Pelicula> data = new ArrayList<>();

    public PeliculaTablaTableModel() {}
    public PeliculaTablaTableModel(List<Pelicula> data) { if (data != null) this.data = data; }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return columnas.length; }
    @Override public String getColumnName(int column) { return columnas[column]; }

    @Override public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 2:
            case 4:
                return Integer.class;
            case 5:
                return Double.class;
            default:
                return String.class;
        }
    }

    @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        Pelicula p = data.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getTitulo();
            case 2: return p.getDuracionMin();
            case 3: return p.getGenero();
            case 4: return p.getAnio();
            case 5: return p.getCalificacion();
            default: return null;
        }
    }

    public void setData(List<Pelicula> nuevas) { this.data = (nuevas != null) ? nuevas : new ArrayList<>(); fireTableDataChanged(); }
    public Pelicula getAt(int row) { return data.get(row); }
    public void add(Pelicula p) { data.add(p); int i = data.size() - 1; fireTableRowsInserted(i, i); }
    public void remove(int row) { data.remove(row); fireTableRowsDeleted(row, row); }
}
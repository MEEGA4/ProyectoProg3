package gui;

import javax.swing.table.AbstractTableModel;

import domain.Serie;

import java.util.ArrayList;
import java.util.List;

public class SerieTablaTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnas = {"ID", "Título", "Temporadas", "Género", "Estado"};
    private List<Serie> data = new ArrayList<>();

    public SerieTablaTableModel() {}
    public SerieTablaTableModel(List<Serie> data) { if (data != null) this.data = data; }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return columnas.length; }
    @Override public String getColumnName(int column) { return columnas[column]; }

    @Override public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 2:
                return Integer.class;
            default:
                return String.class;
        }
    }

    @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        Serie s = data.get(rowIndex);
        switch (columnIndex) {
            case 0: return s.getNombre();
            case 1: return s.getDescripcion();
            case 2: return s.getTemporadas();
            case 3: return s.getGenero();
            case 4: return s.getStock();
            default: return null;
        }
    }

    public void setData(List<Serie> nuevas) { this.data = (nuevas != null) ? nuevas : new ArrayList<>(); fireTableDataChanged(); }
    public Serie getAt(int row) { return data.get(row); }
    public void add(Serie s) { data.add(s); int i = data.size() - 1; fireTableRowsInserted(i, i); }
    public void remove(int row) { data.remove(row); fireTableRowsDeleted(row, row); }
}
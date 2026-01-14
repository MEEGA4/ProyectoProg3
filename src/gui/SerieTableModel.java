package gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import domain.Serie;

public class SerieTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final List<String> columnas;
    private final List<Serie> data;

    public SerieTableModel(List<String> titulos, List<Serie> series) {
        this.columnas = titulos != null ? new ArrayList<>(titulos) : new ArrayList<>();
        this.data = series != null ? new ArrayList<>(series) : new ArrayList<>();
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
        Serie s = data.get(rowIndex);
        String name = columnas.get(columnIndex).toLowerCase();
        switch (name) {
            case "nombre":
                return s.getNombre();
            case "descripción":
            case "descripcion":
                return s.getDescripcion();
            case "precio":
                return s.getPrecio();
            case "stock":
                return s.getStock();
            case "género":
            case "genero":
                return s.getGenero();
            case "temporadas":
                return s.getTemporadas();
            case "episodios":
                return s.getNumEpisodios();
            default:
                return null;
        }
    }

    public void addSerie(Serie s) {
        int idx = data.size();
        data.add(s);
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

    public Serie getSerieAt(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index);
        }
        return null;
    }
}
package gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import domain.Cliente;

public class ClienteTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final List<String> columnas;
    private final List<Cliente> data;

    public ClienteTableModel(List<String> titulos, List<Cliente> clientes) {
        this.columnas = titulos != null ? new ArrayList<>(titulos) : new ArrayList<>();
        this.data = clientes != null ? new ArrayList<>(clientes) : new ArrayList<>();
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
            case "edad":
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
        Cliente c = data.get(rowIndex);
        String name = columnas.get(columnIndex).toLowerCase();

        switch (name) {
            case "nombre":
                return c.getNombre();
            case "apellido":
                return c.getApellido();
            case "edad":
                return c.getEdad();
            case "ubicacion":
            case "ubicación":
                return c.getUbicacion();
            case "telefono":
            case "teléfono":
                return c.getTelefono();
            case "email":
            case "correo":
                return c.getEmail();
            default:
                return null;
        }
    }

    public void addCliente(Cliente c) {
        int idx = data.size();
        data.add(c);
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

    public List<Cliente> getClientes() {
        return new ArrayList<>(data);
    }

    public Cliente getClienteAt(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index);
        }
        return null;
    }
}
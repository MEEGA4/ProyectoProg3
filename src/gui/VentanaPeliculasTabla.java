package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import domain.Pelicula;

public class VentanaPeliculasTabla extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel pNorte;
    private JPanel pCentro;
    private JLabel lblAtajos;
    private JTextField txtFiltro;
    private JTable tabla;
    private PeliculaTableModel modelo;
    private TableRowSorter<PeliculaTableModel> sorter;

    public VentanaPeliculasTabla(List<String> titulos, List<Pelicula> peliculas) {
        setTitle("Peliculas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Modelo y tabla
        modelo = new PeliculaTableModel(titulos, peliculas);
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        AdminTableStyler.apply(tabla);

        // Panel centro con tabla
        pCentro = new JPanel(new BorderLayout());
        pCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel norte con etiqueta y filtro
        pNorte = new JPanel(new BorderLayout());
        lblAtajos = new JLabel("Añadir Pelicula(ctrl+k), Eliminar Pelicula(ctrl+t)");
        txtFiltro = new JTextField();
        txtFiltro.setToolTipText("Filtrar por nombre de película");
        pNorte.add(lblAtajos, BorderLayout.WEST);
        pNorte.add(txtFiltro, BorderLayout.CENTER);

        // Filtro por nombre
        txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
            private void updateFilter() {
                String text = txtFiltro.getText();
                if (text == null || text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    int colNombre = findNombreColumnIndex();
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text), colNombre));
                }
            }
            @Override public void insertUpdate(DocumentEvent e) { updateFilter(); }
            @Override public void removeUpdate(DocumentEvent e) { updateFilter(); }
            @Override public void changedUpdate(DocumentEvent e) { updateFilter(); }
        });

        // Atajos de teclado con KeyListener (Ctrl+K añadir, Ctrl+T eliminar)
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_K) {
                    onAddPelicula();
                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_T) {
                    onRemovePeliculas();
                }
            }
        };
        // Registrar en tabla y campo de texto para asegurar captura
        tabla.addKeyListener(keyAdapter);
        txtFiltro.addKeyListener(keyAdapter);
        getContentPane().addKeyListener(keyAdapter);
        setFocusable(true);

        getContentPane().add(pNorte, BorderLayout.NORTH);
        getContentPane().add(pCentro, BorderLayout.CENTER);

        setBounds(300, 200, 900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private int findNombreColumnIndex() {
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            if ("nombre".equalsIgnoreCase(modelo.getColumnName(i))) return i;
        }
        return 0; // fallback
    }

    private void onAddPelicula() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la película:");
        if (nombre == null || nombre.trim().isEmpty()) return;
        String descripcion = JOptionPane.showInputDialog(this, "Descripción:");
        if (descripcion == null) descripcion = "";
        String director = JOptionPane.showInputDialog(this, "Director:");
        if (director == null) director = "";
        String genero = JOptionPane.showInputDialog(this, "Género:");
        if (genero == null) genero = "";

        double precio = 0.0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Precio (número):", "0.0");
            if (inp != null && !inp.isEmpty()) precio = Double.parseDouble(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stock = 0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Stock (entero):", "0");
            if (inp != null && !inp.isEmpty()) stock = Integer.parseInt(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int duracion = 0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Duración (minutos, entero):", "0");
            if (inp != null && !inp.isEmpty()) duracion = Integer.parseInt(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duración inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pelicula p = new Pelicula(nombre, descripcion, precio, stock, director, genero, duracion);
        modelo.addPelicula(p);
    }

    private void onRemovePeliculas() {
        int[] viewRows = tabla.getSelectedRows();
        if (viewRows == null || viewRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una o más filas para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar las películas seleccionadas?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int[] modelRows = new int[viewRows.length];
        for (int i = 0; i < viewRows.length; i++) {
            modelRows[i] = tabla.convertRowIndexToModel(viewRows[i]);
        }
        java.util.Arrays.sort(modelRows);
        modelo.removeRows(modelRows);
    }

    // Ejecución de ejemplo
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<String> cols = Arrays.asList("Nombre", "Descripción", "Precio", "Stock", "Director", "Género", "Duración");
            List<Pelicula> data = new ArrayList<>();
            data.add(new Pelicula("Inception", "Thriller de ciencia ficción", 12.99, 10, "Christopher Nolan", "Sci-Fi", 148));
            data.add(new Pelicula("Titanic", "Drama romántico", 9.99, 5, "James Cameron", "Drama", 195));
            new VentanaPeliculasTabla(cols, data);
        });
    }
}


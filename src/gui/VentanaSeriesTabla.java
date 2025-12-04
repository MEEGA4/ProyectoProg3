package gui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

import db.GestorBD;
import domain.Serie;

public class VentanaSeriesTabla extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel pNorte;
    private JPanel pCentro;
    private JLabel lblAtajos;
    private JTextField txtFiltro;
    private JTable tabla;
    private SerieTableModel modelo;
    private TableRowSorter<SerieTableModel> sorter;
    private GestorBD gestor;

    public VentanaSeriesTabla(List<String> titulos, List<Serie> series, GestorBD gestorBD) {
        this.gestor = gestorBD;
        setTitle("Series");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Modelo y tabla
        modelo = new SerieTableModel(titulos, series);
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        AdminTableStyler.apply(tabla);

        // Panel centro con tabla
        pCentro = new JPanel(new BorderLayout());
        pCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel norte con etiqueta y filtro
        pNorte = new JPanel(new BorderLayout());
        lblAtajos = new JLabel("Añadir Serie(ctrl+k), Eliminar Serie(ctrl+t)");
        txtFiltro = new JTextField();
        txtFiltro.setToolTipText("Filtrar por nombre de serie");
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

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFilter();
            }
        });

        // Atajos de teclado con KeyListener (Ctrl+K añadir, Ctrl+T eliminar)
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_K) {
                    onAddSerie();
                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_T) {
                    onRemoveSeries();
                }
            }
        };
        tabla.addKeyListener(keyAdapter);
        txtFiltro.addKeyListener(keyAdapter);
        getContentPane().addKeyListener(keyAdapter);
        setFocusable(true);

        getContentPane().add(pNorte, BorderLayout.NORTH);
        getContentPane().add(pCentro, BorderLayout.CENTER);

        setBounds(300, 200, 900, 600);
        setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                gestor.closeBD();
            }
        });

        setVisible(true);
    }

    private int findNombreColumnIndex() {
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            if ("nombre".equalsIgnoreCase(modelo.getColumnName(i)))
                return i;
        }
        return 0;
    }

    private void onAddSerie() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la serie:");
        if (nombre == null || nombre.trim().isEmpty())
            return;
        String descripcion = JOptionPane.showInputDialog(this, "Descripción:");
        if (descripcion == null)
            descripcion = "";
        String genero = JOptionPane.showInputDialog(this, "Género:");
        if (genero == null)
            genero = "";

        double precio = 0.0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Precio (número):", "0.0");
            if (inp != null && !inp.isEmpty())
                precio = Double.parseDouble(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stock = 0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Stock (entero):", "0");
            if (inp != null && !inp.isEmpty())
                stock = Integer.parseInt(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int temporadas = 0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Temporadas (entero):", "0");
            if (inp != null && !inp.isEmpty())
                temporadas = Integer.parseInt(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Temporadas inválidas", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int episodios = 0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Episodios (entero):", "0");
            if (inp != null && !inp.isEmpty())
                episodios = Integer.parseInt(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Episodios inválidos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Serie s = new Serie(nombre, descripcion, precio, stock, genero, temporadas);
        s.setNumEpisodios(episodios);
        modelo.addSerie(s);

        // Persistir en la base de datos
        if (gestor != null) {
            gestor.insertarSerie(s);
        }
    }

    private void onRemoveSeries() {
        int[] viewRows = tabla.getSelectedRows();
        if (viewRows == null || viewRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una o más filas para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar las series seleccionadas?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        int[] modelRows = new int[viewRows.length];
        for (int i = 0; i < viewRows.length; i++) {
            modelRows[i] = tabla.convertRowIndexToModel(viewRows[i]);
        }
        java.util.Arrays.sort(modelRows);

        // Eliminar de la base de datos antes de eliminar del modelo
        if (gestor != null) {
            for (int i = 0; i < modelRows.length; i++) {
                Serie serie = modelo.getSerieAt(modelRows[i]);
                if (serie != null && serie.getId() > 0) {
                    gestor.eliminarSerie(serie.getId());
                }
            }
        }

        modelo.removeRows(modelRows);
    }

    // Ejecución de ejemplo
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<String> cols = Arrays.asList("Nombre", "Descripción", "Precio", "Stock", "Género", "Temporadas",
                    "Episodios");
            List<Serie> data = new ArrayList<>();
            Serie s1 = new Serie("Breaking Bad", "Drama criminal", 19.99, 30, "Drama", 5);
            s1.setNumEpisodios(62);
            data.add(s1);
            Serie s2 = new Serie("The Office", "Comedia", 14.99, 20, "Comedia", 9);
            s2.setNumEpisodios(201);
            data.add(s2);
            new VentanaSeriesTabla(cols, data, null);
        });
    }
}

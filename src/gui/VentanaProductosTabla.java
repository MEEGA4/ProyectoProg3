package gui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import db.GestorBD;
import domain.Pelicula;
import domain.Producto;
import domain.Serie;

public class VentanaProductosTabla extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel pNorte;
    private JPanel pCentro;
    private JLabel lblAtajos;
    private JTextField txtFiltro;
    private JTable tabla;
    private ProductoTableModel modelo;
    private TableRowSorter<ProductoTableModel> sorter;
    private GestorBD gestor;

    public VentanaProductosTabla(List<String> titulos, List<Producto> productos, GestorBD gestorBD) {
        this.gestor = gestorBD;
        setTitle("Gestión de Todos los Productos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Modelo y tabla
        modelo = new ProductoTableModel(titulos, productos);
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        AdminTableStyler.apply(tabla);

        // Panel centro con tabla
        pCentro = new JPanel(new BorderLayout());
        pCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel norte con etiqueta y filtro
        pNorte = new JPanel(new BorderLayout());
        lblAtajos = new JLabel("Añadir Producto(ctrl+a), Eliminar Producto(ctrl+b)");
        txtFiltro = new JTextField();
        txtFiltro.setToolTipText("Filtrar por nombre de producto");
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

        // Atajos de teclado (Ctrl+A añadir, Ctrl+B eliminar)
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
                    onAddProducto();
                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_B) {
                    onRemoveProductos();
                }
            }
        };
        tabla.addKeyListener(keyAdapter);
        txtFiltro.addKeyListener(keyAdapter);
        getContentPane().addKeyListener(keyAdapter);
        setFocusable(true);

        getContentPane().add(pNorte, BorderLayout.NORTH);
        getContentPane().add(pCentro, BorderLayout.CENTER);

        setBounds(300, 200, 1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private int findNombreColumnIndex() {
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            if ("nombre".equalsIgnoreCase(modelo.getColumnName(i)))
                return i;
        }
        return 1;
    }

    private void onAddProducto() {
        String[] opciones = { "Película", "Serie", "Cancelar" };
        int tipo = JOptionPane.showOptionDialog(
                this,
                "¿Qué tipo de producto deseas añadir?",
                "Seleccionar tipo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (tipo == 0) {
            addPelicula();
        } else if (tipo == 1) {
            addSerie();
        }
    }

    private void addPelicula() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la película:");
        if (nombre == null || nombre.trim().isEmpty())
            return;

        String descripcion = JOptionPane.showInputDialog(this, "Descripción:");
        if (descripcion == null)
            descripcion = "";

        String director = JOptionPane.showInputDialog(this, "Director:");
        if (director == null)
            director = "";

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

        int duracion = 0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Duración (minutos, entero):", "0");
            if (inp != null && !inp.isEmpty())
                duracion = Integer.parseInt(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duración inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pelicula p = new Pelicula(nombre, descripcion, precio, stock, director, genero, duracion);
        modelo.addProducto(p);

        // Persistir en la base de datos
        if (gestor != null) {
            gestor.insertarPelicula(p);
        }
    }

    private void addSerie() {
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
        modelo.addProducto(s);

        // Persistir en la base de datos
        if (gestor != null) {
            gestor.insertarSerie(s);
        }
    }

    private void onRemoveProductos() {
        int[] viewRows = tabla.getSelectedRows();
        if (viewRows == null || viewRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una o más filas para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar los productos seleccionados?",
                "Confirmar",
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
                Producto producto = modelo.getProductoAt(modelRows[i]);
                if (producto != null && producto.getId() > 0) {
                    if (producto instanceof Pelicula) {
                        gestor.eliminarPelicula(producto.getId());
                    } else if (producto instanceof Serie) {
                        gestor.eliminarSerie(producto.getId());
                    }
                }
            }
        }

        modelo.removeRows(modelRows);
    }
}

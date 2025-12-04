package gui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
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
import domain.Cliente;
import domain.Trabajador;

public class VentanaClientesTabla extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel pNorte;
    private JPanel pCentro;
    private JPanel pSur;
    private JLabel lblAtajos;
    private JTextField txtFiltro;
    private JTable tabla;
    private JButton btnCerrar;
    private ClienteTableModel modelo;
    private TableRowSorter<ClienteTableModel> sorter;
    private GestorBD gestor;
    private Trabajador trabajador;
    
    public VentanaClientesTabla(List<String> titulos, List<Cliente> clientes, GestorBD gestorBD, Trabajador trabajador) {
        this(titulos, clientes, false, gestorBD, trabajador);
    }

    public VentanaClientesTabla(List<String> titulos, List<Cliente> clientes, boolean adminTheme, GestorBD gestorBD, Trabajador trabajador) {
        this.gestor = gestorBD;
        this.trabajador = trabajador;
        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Modelo y tabla
        modelo = new ClienteTableModel(titulos, clientes);
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        if (adminTheme) {
            AdminTableStyler.apply(tabla);
        }

        // Panel centro con tabla
        pCentro = new JPanel(new BorderLayout());
        pCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel norte con etiqueta y filtro
        pNorte = new JPanel(new BorderLayout());
        lblAtajos = new JLabel("Añadir Cliente(ctrl+a), Eliminar Cliente(ctrl+b)");
        txtFiltro = new JTextField();
        txtFiltro.setToolTipText("Filtrar por nombre de cliente");
        pNorte.add(lblAtajos, BorderLayout.WEST);
        pNorte.add(txtFiltro, BorderLayout.CENTER);

        // Panel sur con botón cerrar
        pSur = new JPanel();
        btnCerrar = new JButton("VOLVER");
        btnCerrar.addActionListener((e) -> {
        	new VentanaTrabajador(trabajador, gestorBD);
        	this.dispose();
        });
        pSur.add(btnCerrar);

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
                    onAddCliente();
                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_B) {
                    onRemoveClientes();
                }
            }
        };
        tabla.addKeyListener(keyAdapter);
        txtFiltro.addKeyListener(keyAdapter);
        getContentPane().addKeyListener(keyAdapter);
        setFocusable(true);

        getContentPane().add(pNorte, BorderLayout.NORTH);
        getContentPane().add(pCentro, BorderLayout.CENTER);
        getContentPane().add(pSur, BorderLayout.SOUTH);

        setBounds(300, 200, 900, 600);
        setLocationRelativeTo(null);

        // Removido el windowListener que cerraba la BD
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // Solo se cierra la ventana, no la base de datos
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

    private void onAddCliente() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del cliente:");
        if (nombre == null || nombre.trim().isEmpty())
            return;

        String apellido = JOptionPane.showInputDialog(this, "Apellido:");
        if (apellido == null)
            apellido = "";

        int edad = 0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Edad (entero):", "0");
            if (inp != null && !inp.isEmpty())
                edad = Integer.parseInt(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String contrasena = JOptionPane.showInputDialog(this, "Contraseña:");
        if (contrasena == null)
            contrasena = "";

        String ubicacion = JOptionPane.showInputDialog(this, "Ubicación:");
        if (ubicacion == null)
            ubicacion = "";

        String telefono = JOptionPane.showInputDialog(this, "Teléfono:");
        if (telefono == null)
            telefono = "";

        String email = JOptionPane.showInputDialog(this, "Email:");
        if (email == null)
            email = "";

        Cliente c = new Cliente(nombre, apellido, edad, contrasena, ubicacion, telefono, email);
        modelo.addCliente(c);

        // Persistir en la base de datos
        if (gestor != null) {
            gestor.insertarCliente(c);
        }
    }

    private void onRemoveClientes() {
        int[] viewRows = tabla.getSelectedRows();
        if (viewRows == null || viewRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una o más filas para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar los clientes seleccionados?",
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
                Cliente cliente = modelo.getClienteAt(modelRows[i]);
                if (cliente.getId() > 0) {
                    gestor.eliminarCliente(cliente.getId());
                }
            }
        }

        modelo.removeRows(modelRows);
    }
}
package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import db.GestorBD;
import domain.Cliente;
import domain.Trabajador;

public class VentanaClientesTabla extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel pNorte, pCentro, pSur;
    private JLabel lblAtajos;
    private JTextField txtFiltro;
    private JTable tabla;
    private JButton btnCerrar;
    private ClienteTableModel modelo;
    private TableRowSorter<ClienteTableModel> sorter;
    
    private GestorBD gestor;
    private Trabajador trabajador;
    
    // Paleta de colores
    private final Color COLOR_FONDO = new Color(33, 37, 41);
    private final Color COLOR_ACCENTO = new Color(255, 193, 7);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Font FUENTE_PRINCIPAL = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Font FUENTE_TITULO = new Font(Font.SANS_SERIF, Font.BOLD, 18);

    public VentanaClientesTabla(List<String> titulos, List<Cliente> clientes, GestorBD gestorBD, Trabajador trabajador) {
        this(titulos, clientes, false, gestorBD, trabajador);
    }

    public VentanaClientesTabla(List<String> titulos, List<Cliente> clientes, boolean adminTheme, GestorBD gestorBD, Trabajador trabajador) {
        this.gestor = gestorBD;
        this.trabajador = trabajador;

        ImageIcon im = new ImageIcon("resources/images/logo.png");
        setIconImage(im.getImage());
        // Configuración básica de la ventana
        setUndecorated(true);
        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Panel Principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(COLOR_ACCENTO, 2)); // Borde dorado fino alrededor
        setContentPane(panelPrincipal);

        // ===== Barra Superior Personalizada =====
        JPanel barraArriba = new JPanel(new BorderLayout());
        barraArriba.setBackground(COLOR_FONDO);
        barraArriba.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTituloVentana = new JLabel("GESTIÓN DE CLIENTES");
        lblTituloVentana.setForeground(COLOR_ACCENTO);
        lblTituloVentana.setFont(FUENTE_TITULO);
        barraArriba.add(lblTituloVentana, BorderLayout.WEST);

        JButton btnCerrarX = new JButton("✕");
        btnCerrarX.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        btnCerrarX.setForeground(COLOR_ACCENTO);
        btnCerrarX.setBackground(COLOR_FONDO);
        btnCerrarX.setBorderPainted(false);
        btnCerrarX.setFocusPainted(false);
        btnCerrarX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrarX.addActionListener(e -> cerrarVentana());
        barraArriba.add(btnCerrarX, BorderLayout.EAST);

        hacerVentanaArrastrable(barraArriba);
        panelPrincipal.add(barraArriba, BorderLayout.NORTH);

        // ===== Configuración de Modelo y Tabla =====
        modelo = new ClienteTableModel(titulos, clientes);
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        
        estilizarTabla(tabla); // Aplicar estilo oscuro/dorado a la tabla

        // ===== Panel Norte (Filtros y Atajos) =====
        pNorte = new JPanel(new BorderLayout(15, 0));
        pNorte.setBackground(COLOR_FONDO);
        pNorte.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Etiqueta de atajos estilizada
        lblAtajos = new JLabel("[CTRL+A] Añadir  |  [CTRL+B] Eliminar");
        lblAtajos.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        lblAtajos.setForeground(new Color(200, 200, 200));
        pNorte.add(lblAtajos, BorderLayout.WEST);

        // Campo de texto estilizado
        txtFiltro = new JTextField();
        txtFiltro.setToolTipText("Filtrar por nombre de cliente");
        txtFiltro.setFont(FUENTE_PRINCIPAL);
        txtFiltro.setBackground(new Color(50, 54, 58));
        txtFiltro.setForeground(COLOR_TEXTO);
        txtFiltro.setCaretColor(COLOR_ACCENTO);
        txtFiltro.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_ACCENTO, 1),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        // Panel contenedor para el filtro para darle margen
        JPanel pFiltroContainer = new JPanel(new BorderLayout());
        pFiltroContainer.setBackground(COLOR_FONDO);
        JLabel lblLupa = new JLabel("🔍 ");
        lblLupa.setForeground(COLOR_ACCENTO);
        pFiltroContainer.add(lblLupa, BorderLayout.WEST);
        pFiltroContainer.add(txtFiltro, BorderLayout.CENTER);
        
        pNorte.add(pFiltroContainer, BorderLayout.CENTER);

        // ===== Panel Centro (Tabla con Scroll) =====
        pCentro = new JPanel(new BorderLayout());
        pCentro.setBackground(COLOR_FONDO);
        pCentro.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.getViewport().setBackground(COLOR_FONDO); // Fondo del viewport oscuro
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_ACCENTO, 1));
        pCentro.add(scrollPane, BorderLayout.CENTER);

        // ===== Panel Sur (Botón Volver) =====
        pSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pSur.setBackground(COLOR_FONDO);
        pSur.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        btnCerrar = crearBotonEstilizado("VOLVER");
        btnCerrar.addActionListener((e) -> cerrarVentana());
        pSur.add(btnCerrar);

        // Lógica de Filtro
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

        // Key Listeners
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
        panelPrincipal.addKeyListener(keyAdapter);
        setFocusable(true);

        // Agregar los paneles sub-componentes al panel contenedor principal (para layout interno)
        JPanel contenidoLayout = new JPanel(new BorderLayout());
        contenidoLayout.setBackground(COLOR_FONDO);
        contenidoLayout.add(pNorte, BorderLayout.NORTH);
        contenidoLayout.add(pCentro, BorderLayout.CENTER);
        contenidoLayout.add(pSur, BorderLayout.SOUTH);
        
        panelPrincipal.add(contenidoLayout, BorderLayout.CENTER);

        // Tamaño y posición
        setSize(900, 600);
        setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // Solo se cierra la ventana visual
            }
        });

        setVisible(true);
    }
    
    // ===== Métodos de Estilo =====

    private void estilizarTabla(JTable table) {
        table.setBackground(COLOR_FONDO);
        table.setForeground(COLOR_TEXTO);
        table.setGridColor(new Color(60, 63, 65));
        table.setRowHeight(30);
        table.setFont(FUENTE_PRINCIPAL);
        table.setSelectionBackground(COLOR_ACCENTO);
        table.setSelectionForeground(Color.BLACK);
        
        // Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(COLOR_ACCENTO);
        header.setForeground(Color.BLACK);
        header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        // Centrar celdas (opcional)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(COLOR_FONDO);
        centerRenderer.setForeground(COLOR_TEXTO);
        // table.setDefaultRenderer(Object.class, centerRenderer); // Descomentar si quieres todo centrado
        
        // Renderizador para mantener color al no estar seleccionado
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_FONDO : new Color(43, 47, 51)); // Alternar ligeramente
                    c.setForeground(COLOR_TEXTO);
                } else {
                    c.setBackground(COLOR_ACCENTO);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        boton.setBackground(COLOR_ACCENTO);
        boton.setForeground(COLOR_TEXTO); // Texto blanco sobre dorado (como en el original) o negro si prefieres contraste
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_TEXTO);
                boton.setForeground(COLOR_ACCENTO);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_ACCENTO);
                boton.setForeground(COLOR_TEXTO);
            }
        });
        return boton;
    }

    private void hacerVentanaArrastrable(JPanel panel) {
        final int[] posX = new int[1];
        final int[] posY = new int[1];
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                posX[0] = e.getX();
                posY[0] = e.getY();
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - posX[0], e.getYOnScreen() - posY[0]);
            }
        });
    }
    
    private void cerrarVentana() {
        new VentanaTrabajador(trabajador, gestor);
        this.dispose();
    }

    // ===== Lógica de Negocio (Original) =====

    private int findNombreColumnIndex() {
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            if ("nombre".equalsIgnoreCase(modelo.getColumnName(i)))
                return i;
        }
        return 0;
    }

    private void onAddCliente() {
        // Podríamos estilizar también los JOptionPane con UIManager, pero por simplicidad usamos el estándar
        String nombre = JOptionPane.showInputDialog(this, "Nombre del cliente:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        String apellido = JOptionPane.showInputDialog(this, "Apellido:");
        if (apellido == null) apellido = "";

        int edad = 0;
        try {
            String inp = JOptionPane.showInputDialog(this, "Edad (entero):", "0");
            if (inp != null && !inp.isEmpty()) edad = Integer.parseInt(inp);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String contrasena = JOptionPane.showInputDialog(this, "Contraseña:");
        if (contrasena == null) contrasena = "";

        String ubicacion = JOptionPane.showInputDialog(this, "Ubicación:");
        if (ubicacion == null) ubicacion = "";

        String telefono = JOptionPane.showInputDialog(this, "Teléfono:");
        if (telefono == null) telefono = "";

        String email = JOptionPane.showInputDialog(this, "Email:");
        if (email == null) email = "";

        Cliente c = new Cliente(nombre, apellido, edad, contrasena, ubicacion, telefono, email);
        modelo.addCliente(c);

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
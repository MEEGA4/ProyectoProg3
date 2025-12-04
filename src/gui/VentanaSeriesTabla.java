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
import domain.Serie;
import domain.Trabajador;

public class VentanaSeriesTabla extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel pNorte, pCentro, pSur;
    private JLabel lblAtajos;
    private JTextField txtFiltro;
    private JTable tabla;
    private JButton btnCerrar;
    private SerieTableModel modelo;
    private TableRowSorter<SerieTableModel> sorter;
    
    private GestorBD gestor;
    private Trabajador trabajador;

    // Paleta de colores
    private final Color COLOR_FONDO = new Color(33, 37, 41);
    private final Color COLOR_ACCENTO = new Color(255, 193, 7);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Font FUENTE_PRINCIPAL = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Font FUENTE_TITULO = new Font(Font.SANS_SERIF, Font.BOLD, 18);

    public VentanaSeriesTabla(List<String> titulos, List<Serie> series, GestorBD gestorBD, Trabajador trabajador) {
        this.gestor = gestorBD;
        this.trabajador = trabajador;

        // Configuración básica de la ventana
        setUndecorated(true);
        setTitle("Gestión de Series");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel Principal con borde dorado
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(COLOR_ACCENTO, 2));
        setContentPane(panelPrincipal);

        // ===== Barra Superior Personalizada =====
        JPanel barraArriba = new JPanel(new BorderLayout());
        barraArriba.setBackground(COLOR_FONDO);
        barraArriba.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTituloVentana = new JLabel("GESTIÓN DE SERIES");
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
        modelo = new SerieTableModel(titulos, series);
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        
        // Aplicar estilo visual
        estilizarTabla(tabla);

        // ===== Panel Norte (Filtros y Atajos) =====
        pNorte = new JPanel(new BorderLayout(15, 0));
        pNorte.setBackground(COLOR_FONDO);
        pNorte.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Etiqueta de atajos estilizada
        lblAtajos = new JLabel("[CTRL+K] Añadir  |  [CTRL+T] Eliminar");
        lblAtajos.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        lblAtajos.setForeground(new Color(200, 200, 200));
        pNorte.add(lblAtajos, BorderLayout.WEST);

        // Campo de texto estilizado
        txtFiltro = new JTextField();
        txtFiltro.setToolTipText("Filtrar por nombre de serie");
        txtFiltro.setFont(FUENTE_PRINCIPAL);
        txtFiltro.setBackground(new Color(50, 54, 58));
        txtFiltro.setForeground(COLOR_TEXTO);
        txtFiltro.setCaretColor(COLOR_ACCENTO);
        txtFiltro.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_ACCENTO, 1),
            new EmptyBorder(5, 5, 5, 5)
        ));

        // Contenedor del filtro
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
        scrollPane.getViewport().setBackground(COLOR_FONDO);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_ACCENTO, 1));
        pCentro.add(scrollPane, BorderLayout.CENTER);

        // ===== Panel Sur (Botón Volver) =====
        pSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pSur.setBackground(COLOR_FONDO);
        pSur.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        btnCerrar = crearBotonEstilizado("VOLVER");
        btnCerrar.addActionListener((e) -> cerrarVentana());
        pSur.add(btnCerrar);

        // ===== Listeners de Lógica =====

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

        // Atajos de teclado (Ctrl+K añadir, Ctrl+T eliminar)
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
        panelPrincipal.addKeyListener(keyAdapter); // Listener al panel principal
        setFocusable(true);

        // Agregar sub-paneles al layout interno
        JPanel contenidoLayout = new JPanel(new BorderLayout());
        contenidoLayout.setBackground(COLOR_FONDO);
        contenidoLayout.add(pNorte, BorderLayout.NORTH);
        contenidoLayout.add(pCentro, BorderLayout.CENTER);
        contenidoLayout.add(pSur, BorderLayout.SOUTH);

        panelPrincipal.add(contenidoLayout, BorderLayout.CENTER);

        // Configuración final
        setBounds(300, 200, 900, 600);
        setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // No cerramos la BD aquí para no desconectar la ventana principal
            }
        });

        setVisible(true);
    }
    
    // ===== MÉTODOS VISUALES / ESTILO =====

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

        // Renderizador para filas alternas
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_FONDO : new Color(43, 47, 51));
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
        boton.setForeground(COLOR_TEXTO);
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

    // ===== LÓGICA DE NEGOCIO (Original) =====

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
}
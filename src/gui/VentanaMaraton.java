package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import db.GestorBD;
import domain.Pelicula;
import domain.Serie;

public class VentanaMaraton extends JFrame {

    private static final long serialVersionUID = 1L;

    // Componentes de entrada
    private JTextField txtHoras, txtMinutos;
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboGenero;
    private JSpinner spinnerNumTitulos;
    private JComboBox<String> comboOrdenar;
    private JCheckBox chkPermitirRepetidos;
    private JSpinner spinnerEpisodiosPorSerie;
    
    // Panel de resultados
    private JPanel panelResultados;
    private JScrollPane scrollResultados;
    private JLabel lblContadorCombinaciones;
    
    // Datos
    private List<Pelicula> peliculas;
    private List<Serie> series;
    private GestorBD gestor;
    
    // Almacenar todas las combinaciones encontradas
    private List<List<ItemMaraton>> todasLasCombinaciones;

    // Paleta de colores
    private final Color COLOR_FONDO = new Color(33, 37, 41);
    private final Color COLOR_AMARILLO = new Color(255, 193, 7);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_CAMPO = new Color(50, 54, 58);
    private final Color COLOR_ITEM = new Color(45, 49, 54);
    private final Color COLOR_COMBINACION = new Color(40, 44, 48);
    private final Font FUENTE_PRINCIPAL = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Font FUENTE_TITULO = new Font(Font.SANS_SERIF, Font.BOLD, 18);
    
    // Duración por episodio de serie (en minutos)
    private static final int DURACION_EPISODIO = 45;

    public VentanaMaraton(GestorBD gestorBD, List<Pelicula> peliculas, List<Serie> series) {
        this.gestor = gestorBD;
        this.peliculas = peliculas;
        this.series = series;
        this.todasLasCombinaciones = new ArrayList<>();
        
        // Debug: mostrar contenido cargado
        System.out.println("=== VentanaMaraton inicializada ===");
        System.out.println("Películas cargadas: " + peliculas.size());
        for (Pelicula p : peliculas) {
            System.out.println("  - " + p.getNombre() + " (" + p.getDuracion() + " min) - " + p.getGenero());
        }
        System.out.println("Series cargadas: " + series.size());
        for (Serie s : series) {
            System.out.println("  - " + s.getNombre() + " (" + s.getTemporadas() + " temps) - " + s.getGenero());
        }

        // Configuración de la ventana
        setTitle("DEUSTOFILM - Planificador de Maratón");
        setUndecorated(true);
        setSize(950, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(COLOR_AMARILLO, 2));
        setContentPane(panelPrincipal);

        // ===== Barra Superior =====
        JPanel barraArriba = crearBarraSuperior();
        panelPrincipal.add(barraArriba, BorderLayout.NORTH);

        // ===== Panel de Configuración =====
        JPanel panelConfig = crearPanelConfiguracion();
        panelPrincipal.add(panelConfig, BorderLayout.WEST);

        // ===== Panel de Resultados =====
        JPanel panelResultadosContainer = crearPanelResultados();
        panelPrincipal.add(panelResultadosContainer, BorderLayout.CENTER);

        // ===== Panel Inferior =====
        JPanel panelInferior = crearPanelInferior();
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel crearBarraSuperior() {
        JPanel barraArriba = new JPanel(new BorderLayout());
        barraArriba.setBackground(COLOR_FONDO);
        barraArriba.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("🎬 PLANIFICADOR DE MARATÓN - COMBINACIONES RECURSIVAS");
        lblTitulo.setForeground(COLOR_AMARILLO);
        lblTitulo.setFont(FUENTE_TITULO);
        barraArriba.add(lblTitulo, BorderLayout.WEST);

        JButton btnCerrar = new JButton("✕");
        btnCerrar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        btnCerrar.setForeground(COLOR_AMARILLO);
        btnCerrar.setBackground(COLOR_FONDO);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());
        barraArriba.add(btnCerrar, BorderLayout.EAST);

        hacerVentanaArrastrable(barraArriba);
        return barraArriba;
    }

    private JPanel crearPanelConfiguracion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(300, 0));

        // Título de configuración
        JLabel lblConfig = new JLabel("⚙ CONFIGURACIÓN");
        lblConfig.setForeground(COLOR_AMARILLO);
        lblConfig.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        lblConfig.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblConfig);
        panel.add(Box.createVerticalStrut(20));

        // Tiempo disponible
        JLabel lblTiempo = new JLabel("Tiempo disponible:");
        lblTiempo.setForeground(COLOR_TEXTO);
        lblTiempo.setFont(FUENTE_PRINCIPAL);
        lblTiempo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTiempo);
        panel.add(Box.createVerticalStrut(5));

        JPanel panelTiempo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelTiempo.setBackground(COLOR_FONDO);
        panelTiempo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTiempo.setMaximumSize(new Dimension(280, 40));

        txtHoras = crearCampoNumerico(3);
        JLabel lblH = new JLabel("h");
        lblH.setForeground(COLOR_TEXTO);
        
        txtMinutos = crearCampoNumerico(3);
        JLabel lblM = new JLabel("min");
        lblM.setForeground(COLOR_TEXTO);

        panelTiempo.add(txtHoras);
        panelTiempo.add(lblH);
        panelTiempo.add(Box.createHorizontalStrut(10));
        panelTiempo.add(txtMinutos);
        panelTiempo.add(lblM);
        panel.add(panelTiempo);
        panel.add(Box.createVerticalStrut(15));

        // Tipo de contenido
        JLabel lblTipo = new JLabel("Tipo de contenido:");
        lblTipo.setForeground(COLOR_TEXTO);
        lblTipo.setFont(FUENTE_PRINCIPAL);
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTipo);
        panel.add(Box.createVerticalStrut(5));

        comboTipo = new JComboBox<>(new String[]{"Todo", "Solo Películas", "Solo Series"});
        estilizarComboBox(comboTipo);
        comboTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboTipo.setMaximumSize(new Dimension(280, 35));
        panel.add(comboTipo);
        panel.add(Box.createVerticalStrut(15));

        // Género preferido
        JLabel lblGenero = new JLabel("Género preferido:");
        lblGenero.setForeground(COLOR_TEXTO);
        lblGenero.setFont(FUENTE_PRINCIPAL);
        lblGenero.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblGenero);
        panel.add(Box.createVerticalStrut(5));

        List<String> generos = obtenerGenerosUnicos();
        generos.add(0, "Todos");
        comboGenero = new JComboBox<>(generos.toArray(new String[0]));
        estilizarComboBox(comboGenero);
        comboGenero.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboGenero.setMaximumSize(new Dimension(280, 35));
        panel.add(comboGenero);
        panel.add(Box.createVerticalStrut(15));

        // Número fijo de títulos
        JLabel lblNum = new JLabel("Número de títulos (fijo):");
        lblNum.setForeground(COLOR_TEXTO);
        lblNum.setFont(FUENTE_PRINCIPAL);
        lblNum.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblNum);
        panel.add(Box.createVerticalStrut(5));

        SpinnerNumberModel modeloSpinner = new SpinnerNumberModel(3, 1, 10, 1);
        spinnerNumTitulos = new JSpinner(modeloSpinner);
        spinnerNumTitulos.setFont(FUENTE_PRINCIPAL);
        spinnerNumTitulos.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinnerNumTitulos.setMaximumSize(new Dimension(280, 35));
        panel.add(spinnerNumTitulos);
        panel.add(Box.createVerticalStrut(15));

        // Episodios por serie
        JLabel lblEpisodios = new JLabel("Episodios por serie (45 min c/u):");
        lblEpisodios.setForeground(COLOR_TEXTO);
        lblEpisodios.setFont(FUENTE_PRINCIPAL);
        lblEpisodios.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblEpisodios);
        panel.add(Box.createVerticalStrut(5));

        SpinnerNumberModel modeloEpisodios = new SpinnerNumberModel(2, 1, 10, 1);
        spinnerEpisodiosPorSerie = new JSpinner(modeloEpisodios);
        spinnerEpisodiosPorSerie.setFont(FUENTE_PRINCIPAL);
        spinnerEpisodiosPorSerie.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinnerEpisodiosPorSerie.setMaximumSize(new Dimension(280, 35));
        panel.add(spinnerEpisodiosPorSerie);
        panel.add(Box.createVerticalStrut(15));

        // Checkbox para variaciones (con repetición)
        chkPermitirRepetidos = new JCheckBox("Permitir repetidos (Variaciones)");
        chkPermitirRepetidos.setForeground(COLOR_TEXTO);
        chkPermitirRepetidos.setBackground(COLOR_FONDO);
        chkPermitirRepetidos.setFont(FUENTE_PRINCIPAL);
        chkPermitirRepetidos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(chkPermitirRepetidos);
        panel.add(Box.createVerticalStrut(15));

        // Ordenar por
        JLabel lblOrdenar = new JLabel("Ordenar resultados por:");
        lblOrdenar.setForeground(COLOR_TEXTO);
        lblOrdenar.setFont(FUENTE_PRINCIPAL);
        lblOrdenar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblOrdenar);
        panel.add(Box.createVerticalStrut(5));

        comboOrdenar = new JComboBox<>(new String[]{
            "Menos tiempo sobrante",
            "Mayor duración primero", 
            "Menor duración primero",
            "Orden alfabético"
        });
        estilizarComboBox(comboOrdenar);
        comboOrdenar.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboOrdenar.setMaximumSize(new Dimension(280, 35));
        panel.add(comboOrdenar);
        panel.add(Box.createVerticalStrut(25));

        // Botón calcular
        JButton btnCalcular = crearBotonEstilizado("🎯 CALCULAR COMBINACIONES");
        btnCalcular.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCalcular.setMaximumSize(new Dimension(280, 45));
        btnCalcular.addActionListener(e -> calcularCombinaciones());
        panel.add(btnCalcular);

        return panel;
    }

    private JPanel crearPanelResultados() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(COLOR_FONDO);
        container.setBorder(new EmptyBorder(20, 10, 20, 20));

        // Panel superior con título y contador
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(COLOR_FONDO);
        panelTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblResultados = new JLabel("📋 COMBINACIONES ENCONTRADAS");
        lblResultados.setForeground(COLOR_AMARILLO);
        lblResultados.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        panelTitulo.add(lblResultados, BorderLayout.WEST);

        lblContadorCombinaciones = new JLabel("");
        lblContadorCombinaciones.setForeground(COLOR_TEXTO);
        lblContadorCombinaciones.setFont(FUENTE_PRINCIPAL);
        panelTitulo.add(lblContadorCombinaciones, BorderLayout.EAST);

        container.add(panelTitulo, BorderLayout.NORTH);

        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBackground(COLOR_FONDO);

        // Mensaje inicial
        JLabel lblInicial = new JLabel("Configura los parámetros y pulsa 'Calcular Combinaciones'");
        lblInicial.setForeground(Color.GRAY);
        lblInicial.setFont(FUENTE_PRINCIPAL);
        lblInicial.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelResultados.add(Box.createVerticalGlue());
        panelResultados.add(lblInicial);
        panelResultados.add(Box.createVerticalGlue());

        scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.setBackground(COLOR_FONDO);
        scrollResultados.getViewport().setBackground(COLOR_FONDO);
        scrollResultados.setBorder(BorderFactory.createLineBorder(COLOR_AMARILLO, 1));
        scrollResultados.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollResultados.getVerticalScrollBar().setUnitIncrement(16);
        container.add(scrollResultados, BorderLayout.CENTER);

        return container;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 20, 15, 20));

        JLabel lblInfo = new JLabel("💡 Combinaciones = sin repetición | Variaciones = con repetición (máx. 500 resultados)");
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        panel.add(lblInfo);

        return panel;
    }

    private void calcularCombinaciones() {
        // Validar entrada
        int horas = 0, minutos = 0;
        try {
            String horasStr = txtHoras.getText().trim();
            String minutosStr = txtMinutos.getText().trim();
            
            if (!horasStr.isEmpty()) horas = Integer.parseInt(horasStr);
            if (!minutosStr.isEmpty()) minutos = Integer.parseInt(minutosStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, introduce valores numéricos válidos para el tiempo.",
                "Error de entrada", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int tiempoTotal = horas * 60 + minutos;
        if (tiempoTotal <= 0) {
            JOptionPane.showMessageDialog(this, 
                "El tiempo debe ser mayor que 0 minutos.",
                "Error de entrada", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numTitulos = (int) spinnerNumTitulos.getValue();
        boolean permitirRepetidos = chkPermitirRepetidos.isSelected();

        // Filtrar contenido según configuración
        List<ItemMaraton> itemsDisponibles = filtrarContenido();
        
        // Debug
        System.out.println("=== Items filtrados ===");
        System.out.println("Total items disponibles: " + itemsDisponibles.size());
        for (ItemMaraton item : itemsDisponibles) {
            System.out.println("  - " + item.nombre + " (" + item.duracion + " min) [" + item.tipo + "]");
        }
        
        if (itemsDisponibles.isEmpty()) {
            mostrarMensajeVacio("No hay contenido disponible con los filtros seleccionados.");
            return;
        }

        if (!permitirRepetidos && numTitulos > itemsDisponibles.size()) {
            JOptionPane.showMessageDialog(this, 
                "No hay suficientes títulos (" + itemsDisponibles.size() + ") para formar combinaciones de " + numTitulos + ".",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostrar mensaje de carga
        panelResultados.removeAll();
        JLabel lblCargando = new JLabel("Calculando combinaciones recursivamente...");
        lblCargando.setForeground(COLOR_AMARILLO);
        lblCargando.setFont(FUENTE_PRINCIPAL);
        lblCargando.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelResultados.add(Box.createVerticalGlue());
        panelResultados.add(lblCargando);
        panelResultados.add(Box.createVerticalGlue());
        panelResultados.revalidate();
        panelResultados.repaint();

        // Calcular en un hilo separado
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                todasLasCombinaciones.clear();
                
                if (permitirRepetidos) {
                    // VARIACIONES CON REPETICIÓN (recursivo)
                    generarVariacionesRecursivo(itemsDisponibles, numTitulos, tiempoTotal, new ArrayList<>());
                } else {
                    // COMBINACIONES SIN REPETICIÓN (recursivo)
                    generarCombinacionesRecursivo(itemsDisponibles, numTitulos, tiempoTotal, 0, new ArrayList<>());
                }
                return null;
            }

            @Override
            protected void done() {
                System.out.println("Combinaciones encontradas: " + todasLasCombinaciones.size());
                ordenarCombinaciones(tiempoTotal);
                mostrarTodasLasCombinaciones(tiempoTotal);
            }
        };
        worker.execute();
    }

    private List<ItemMaraton> filtrarContenido() {
        List<ItemMaraton> items = new ArrayList<>();
        String tipo = (String) comboTipo.getSelectedItem();
        String genero = (String) comboGenero.getSelectedItem();
        int episodiosPorSerie = (int) spinnerEpisodiosPorSerie.getValue();

        System.out.println("=== Filtrando contenido ===");
        System.out.println("Tipo seleccionado: " + tipo);
        System.out.println("Género seleccionado: " + genero);
        System.out.println("Episodios por serie: " + episodiosPorSerie);

        // Añadir películas si corresponde
        if ("Todo".equals(tipo) || "Solo Películas".equals(tipo)) {
            System.out.println("Añadiendo películas...");
            for (Pelicula p : peliculas) {
                boolean generoCoincide = "Todos".equals(genero) || 
                                         genero.equalsIgnoreCase(p.getGenero()) ||
                                         (p.getGenero() == null && "Todos".equals(genero));
                
                if (generoCoincide) {
                    int duracion = p.getDuracion();
                    if (duracion <= 0) duracion = 120; // Duración por defecto si no tiene
                    items.add(new ItemMaraton(p.getNombre(), duracion, "Película", p.getGenero()));
                    System.out.println("  + Película añadida: " + p.getNombre() + " (" + duracion + " min)");
                }
            }
        }

        // Añadir series si corresponde
        if ("Todo".equals(tipo) || "Solo Series".equals(tipo)) {
            System.out.println("Añadiendo series...");
            for (Serie s : series) {
                boolean generoCoincide = "Todos".equals(genero) || 
                                         genero.equalsIgnoreCase(s.getGenero()) ||
                                         (s.getGenero() == null && "Todos".equals(genero));
                
                if (generoCoincide) {
                    // Duración = episodios seleccionados x duración por episodio
                    int duracion = episodiosPorSerie * DURACION_EPISODIO;
                    String nombreItem = s.getNombre() + " (" + episodiosPorSerie + " ep.)";
                    items.add(new ItemMaraton(nombreItem, duracion, "Serie", s.getGenero()));
                    System.out.println("  + Serie añadida: " + nombreItem + " (" + duracion + " min)");
                }
            }
        }

        System.out.println("Total items filtrados: " + items.size());
        return items;
    }

    // ==================== ALGORITMOS RECURSIVOS ====================

    /**
     * COMBINACIONES SIN REPETICIÓN - Algoritmo recursivo
     * Genera todas las combinaciones de exactamente 'n' elementos sin repetir
     * 
     * @param items Lista de items disponibles
     * @param n Número de elementos a seleccionar
     * @param tiempoMax Tiempo máximo disponible
     * @param inicio Índice desde donde empezar a buscar (evita repeticiones)
     * @param combinacionActual Combinación que se está construyendo
     */
    private void generarCombinacionesRecursivo(List<ItemMaraton> items, int n, int tiempoMax, 
                                                int inicio, List<ItemMaraton> combinacionActual) {
        // Caso base: si ya tenemos n elementos
        if (combinacionActual.size() == n) {
            // Verificar si la combinación cabe en el tiempo
            int duracionTotal = calcularDuracionTotal(combinacionActual);
            if (duracionTotal <= tiempoMax) {
                // Guardar copia de la combinación válida
                todasLasCombinaciones.add(new ArrayList<>(combinacionActual));
            }
            return; // Terminar esta rama de recursión
        }

        // Limitar resultados
        if (todasLasCombinaciones.size() >= 500) {
            return;
        }

        // Caso recursivo: probar añadir cada elemento desde 'inicio'
        for (int i = inicio; i < items.size(); i++) {
            ItemMaraton item = items.get(i);
            
            // Poda: si este item ya excede el tiempo restante, no seguir
            int duracionActual = calcularDuracionTotal(combinacionActual);
            if (duracionActual + item.duracion > tiempoMax) {
                continue; // Saltar este item, pero probar los siguientes
            }

            // Añadir el item a la combinación actual
            combinacionActual.add(item);
            
            // Llamada recursiva: buscar desde i+1 para evitar repeticiones
            generarCombinacionesRecursivo(items, n, tiempoMax, i + 1, combinacionActual);
            
            // Backtracking: quitar el item para probar otras opciones
            combinacionActual.remove(combinacionActual.size() - 1);
        }
    }

    /**
     * VARIACIONES CON REPETICIÓN - Algoritmo recursivo
     * Genera todas las variaciones de exactamente 'n' elementos permitiendo repetir
     * 
     * @param items Lista de items disponibles
     * @param n Número de elementos a seleccionar
     * @param tiempoMax Tiempo máximo disponible
     * @param variacionActual Variación que se está construyendo
     */
    private void generarVariacionesRecursivo(List<ItemMaraton> items, int n, int tiempoMax,
                                              List<ItemMaraton> variacionActual) {
        // Caso base: si ya tenemos n elementos
        if (variacionActual.size() == n) {
            // Verificar si la variación cabe en el tiempo
            int duracionTotal = calcularDuracionTotal(variacionActual);
            if (duracionTotal <= tiempoMax) {
                // Guardar copia de la variación válida
                todasLasCombinaciones.add(new ArrayList<>(variacionActual));
            }
            return; // Terminar esta rama de recursión
        }

        // Limitar resultados
        if (todasLasCombinaciones.size() >= 500) {
            return;
        }

        // Caso recursivo: probar añadir CUALQUIER elemento (permite repetición)
        for (int i = 0; i < items.size(); i++) {
            ItemMaraton item = items.get(i);
            
            // Poda: si este item ya excede el tiempo restante, no seguir
            int duracionActual = calcularDuracionTotal(variacionActual);
            if (duracionActual + item.duracion > tiempoMax) {
                continue; // Saltar este item
            }

            // Añadir el item a la variación actual
            variacionActual.add(item);
            
            // Llamada recursiva: buscar desde 0 (permite repetir el mismo elemento)
            generarVariacionesRecursivo(items, n, tiempoMax, variacionActual);
            
            // Backtracking: quitar el item para probar otras opciones
            variacionActual.remove(variacionActual.size() - 1);
        }
    }

    // ==================== FIN ALGORITMOS RECURSIVOS ====================

    private void ordenarCombinaciones(int tiempoTotal) {
        String ordenSeleccionado = (String) comboOrdenar.getSelectedItem();
        
        Comparator<List<ItemMaraton>> comparator;
        
        switch (ordenSeleccionado) {
            case "Mayor duración primero":
                comparator = (a, b) -> Integer.compare(calcularDuracionTotal(b), calcularDuracionTotal(a));
                break;
            case "Menor duración primero":
                comparator = (a, b) -> Integer.compare(calcularDuracionTotal(a), calcularDuracionTotal(b));
                break;
            case "Orden alfabético":
                comparator = (a, b) -> {
                    String nombreA = a.isEmpty() ? "" : a.get(0).nombre;
                    String nombreB = b.isEmpty() ? "" : b.get(0).nombre;
                    return nombreA.compareTo(nombreB);
                };
                break;
            case "Menos tiempo sobrante":
            default:
                comparator = (a, b) -> {
                    int sobranteA = tiempoTotal - calcularDuracionTotal(a);
                    int sobranteB = tiempoTotal - calcularDuracionTotal(b);
                    return Integer.compare(sobranteA, sobranteB);
                };
                break;
        }
        
        Collections.sort(todasLasCombinaciones, comparator);
    }

    private int calcularDuracionTotal(List<ItemMaraton> combinacion) {
        int total = 0;
        for (ItemMaraton item : combinacion) {
            total += item.duracion;
        }
        return total;
    }

    private void mostrarTodasLasCombinaciones(int tiempoTotal) {
        panelResultados.removeAll();

        if (todasLasCombinaciones.isEmpty()) {
            mostrarMensajeVacio("No se encontró ninguna combinación válida para los parámetros especificados.\n" +
                               "Prueba a aumentar el tiempo disponible o reducir el número de títulos.");
            lblContadorCombinaciones.setText("");
            return;
        }

        String tipo = chkPermitirRepetidos.isSelected() ? "variaciones" : "combinaciones";
        lblContadorCombinaciones.setText(todasLasCombinaciones.size() + " " + tipo + " encontradas");

        int numeroCombinacion = 1;
        for (List<ItemMaraton> combinacion : todasLasCombinaciones) {
            JPanel panelCombinacion = crearPanelCombinacion(numeroCombinacion, combinacion, tiempoTotal);
            panelCombinacion.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelResultados.add(panelCombinacion);
            panelResultados.add(Box.createVerticalStrut(15));
            numeroCombinacion++;
        }

        panelResultados.revalidate();
        panelResultados.repaint();
        
        // Scroll al inicio
        SwingUtilities.invokeLater(() -> {
            scrollResultados.getVerticalScrollBar().setValue(0);
        });
    }

    private JPanel crearPanelCombinacion(int numero, List<ItemMaraton> combinacion, int tiempoTotal) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_COMBINACION);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_AMARILLO, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200 + (combinacion.size() * 35)));

        // Encabezado de la combinación
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_COMBINACION);

        int duracionTotal = calcularDuracionTotal(combinacion);
        int tiempoSobrante = tiempoTotal - duracionTotal;

        String tipoTexto = chkPermitirRepetidos.isSelected() ? "VARIACIÓN" : "COMBINACIÓN";
        JLabel lblNumeroComb = new JLabel(tipoTexto + " #" + numero);
        lblNumeroComb.setForeground(COLOR_AMARILLO);
        lblNumeroComb.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        panelHeader.add(lblNumeroComb, BorderLayout.WEST);

        JPanel panelStats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelStats.setBackground(COLOR_COMBINACION);

        JLabel lblTitulos = new JLabel("📺 " + combinacion.size() + " títulos");
        lblTitulos.setForeground(COLOR_TEXTO);
        lblTitulos.setFont(FUENTE_PRINCIPAL);
        panelStats.add(lblTitulos);

        JLabel lblDuracion = new JLabel("⏱ " + formatearTiempo(duracionTotal));
        lblDuracion.setForeground(COLOR_TEXTO);
        lblDuracion.setFont(FUENTE_PRINCIPAL);
        panelStats.add(lblDuracion);

        JLabel lblSobrante = new JLabel("⏳ " + formatearTiempo(tiempoSobrante) + " libre");
        lblSobrante.setForeground(tiempoSobrante > 60 ? new Color(255, 150, 150) : new Color(150, 255, 150));
        lblSobrante.setFont(FUENTE_PRINCIPAL);
        panelStats.add(lblSobrante);

        panelHeader.add(panelStats, BorderLayout.EAST);
        panel.add(panelHeader, BorderLayout.NORTH);

        // Lista de items
        JPanel panelItems = new JPanel();
        panelItems.setLayout(new BoxLayout(panelItems, BoxLayout.Y_AXIS));
        panelItems.setBackground(COLOR_COMBINACION);
        panelItems.setBorder(new EmptyBorder(10, 0, 0, 0));

        int orden = 1;
        for (ItemMaraton item : combinacion) {
            JPanel panelItem = crearPanelItemCompacto(orden, item);
            panelItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelItems.add(panelItem);
            panelItems.add(Box.createVerticalStrut(5));
            orden++;
        }

        panel.add(panelItems, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelItemCompacto(int numero, ItemMaraton item) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(COLOR_ITEM);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 64, 68), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Número y nombre
        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelIzq.setBackground(COLOR_ITEM);

        JLabel lblNumero = new JLabel(String.valueOf(numero) + ".");
        lblNumero.setForeground(COLOR_AMARILLO);
        lblNumero.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        panelIzq.add(lblNumero);

        JLabel lblNombre = new JLabel(item.nombre);
        lblNombre.setForeground(COLOR_TEXTO);
        lblNombre.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        panelIzq.add(lblNombre);

        JLabel lblTipo = new JLabel("[" + item.tipo + "]");
        lblTipo.setForeground(Color.GRAY);
        lblTipo.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
        panelIzq.add(lblTipo);

        panel.add(panelIzq, BorderLayout.WEST);

        // Duración
        JLabel lblDuracion = new JLabel(formatearTiempo(item.duracion));
        lblDuracion.setForeground(COLOR_AMARILLO);
        lblDuracion.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        panel.add(lblDuracion, BorderLayout.EAST);

        return panel;
    }

    private void mostrarMensajeVacio(String mensaje) {
        panelResultados.removeAll();
        JLabel lbl = new JLabel("<html><center>" + mensaje.replace("\n", "<br>") + "</center></html>");
        lbl.setForeground(Color.GRAY);
        lbl.setFont(FUENTE_PRINCIPAL);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelResultados.add(Box.createVerticalGlue());
        panelResultados.add(lbl);
        panelResultados.add(Box.createVerticalGlue());
        panelResultados.revalidate();
        panelResultados.repaint();
    }

    private String formatearTiempo(int minutos) {
        if (minutos < 0) minutos = 0;
        int h = minutos / 60;
        int m = minutos % 60;
        if (h > 0) {
            return h + "h " + m + "min";
        }
        return m + " min";
    }

    private List<String> obtenerGenerosUnicos() {
        List<String> generos = new ArrayList<>();
        for (Pelicula p : peliculas) {
            if (p.getGenero() != null && !p.getGenero().isEmpty() && !generos.contains(p.getGenero())) {
                generos.add(p.getGenero());
            }
        }
        for (Serie s : series) {
            if (s.getGenero() != null && !s.getGenero().isEmpty() && !generos.contains(s.getGenero())) {
                generos.add(s.getGenero());
            }
        }
        Collections.sort(generos);
        return generos;
    }

    // ===== Métodos de utilidad para componentes =====

    private JTextField crearCampoNumerico(int columnas) {
        JTextField txt = new JTextField(columnas);
        txt.setFont(FUENTE_PRINCIPAL);
        txt.setBackground(COLOR_CAMPO);
        txt.setForeground(COLOR_TEXTO);
        txt.setCaretColor(COLOR_AMARILLO);
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_AMARILLO, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        txt.setHorizontalAlignment(JTextField.CENTER);
        return txt;
    }

    private void estilizarComboBox(JComboBox<String> combo) {
        combo.setFont(FUENTE_PRINCIPAL);
        combo.setBackground(COLOR_CAMPO);
        combo.setForeground(COLOR_TEXTO);
        combo.setBorder(new LineBorder(COLOR_AMARILLO, 1));
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        boton.setBackground(COLOR_AMARILLO);
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_TEXTO);
                boton.setForeground(COLOR_AMARILLO);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_AMARILLO);
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

    // ===== Clase interna para items del maratón =====
    private static class ItemMaraton {
        String nombre;
        int duracion;
        String tipo;
        String genero;

        ItemMaraton(String nombre, int duracion, String tipo, String genero) {
            this.nombre = nombre;
            this.duracion = duracion;
            this.tipo = tipo;
            this.genero = genero != null ? genero : "Sin género";
        }
    }
}
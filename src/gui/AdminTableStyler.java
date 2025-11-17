package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Utility class to style the administrator tables with a consistent look.
 */
public final class AdminTableStyler {

    private static final Color ROW_EVEN = new Color(45, 49, 54);
    private static final Color ROW_ODD = new Color(38, 43, 48);
    private static final Color SELECTION = new Color(255, 193, 7);
    private static final Color TEXT = Color.WHITE;
    private static final Color TEXT_SELECTION = Color.BLACK;
    private static final Color HEADER = new Color(52, 58, 64);

    private AdminTableStyler() {
    }

    public static void apply(JTable table) {
        if (table == null) {
            return;
        }

        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        table.setForeground(TEXT);
        table.setBackground(ROW_EVEN);
        table.setSelectionBackground(SELECTION);
        table.setSelectionForeground(TEXT_SELECTION);

        AdminTableCellRenderer renderer = new AdminTableCellRenderer();
        table.setDefaultRenderer(Object.class, renderer);
        table.setDefaultRenderer(Number.class, renderer);
        table.setDefaultRenderer(Integer.class, renderer);
        table.setDefaultRenderer(Double.class, renderer);
        table.setDefaultRenderer(Float.class, renderer);

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setBackground(HEADER);
            header.setForeground(TEXT);
            header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
            header.setReorderingAllowed(false);
        }
    }

    private static class AdminTableCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (value instanceof Number) {
                setHorizontalAlignment(RIGHT);
            } else {
                setHorizontalAlignment(LEFT);
            }

            if (isSelected) {
                component.setBackground(SELECTION);
                component.setForeground(TEXT_SELECTION);
            } else {
                boolean even = row % 2 == 0;
                component.setBackground(even ? ROW_EVEN : ROW_ODD);
                component.setForeground(TEXT);
            }

            if (component instanceof JComponent) {
                ((JComponent) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            }

            return component;
        }
    }
}

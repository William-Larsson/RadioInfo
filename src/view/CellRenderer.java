package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A cell renderer for changing color of cells when a program
 * has already aired.
 */
public class CellRenderer extends DefaultTableCellRenderer {

    /**
     * Call default constructor for DefaultTableCellRenderer
     */
    public CellRenderer(){
        super();
    }

    /**
     * Changes the color of rows showing programs that have
     * already aired. Also displays the current program on
     * air in a separate color.
     * @param table = the JTable
     * @param value = the Object in the table cell
     * @param isSelected
     * @param hasFocus
     * @param row = the table row
     * @param column = the table column
     * @return = the component in the table.
     */
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column)
    {
        JLabel component = (JLabel) super.getTableCellRendererComponent (
                table, value, isSelected,
                hasFocus, row, column
        );

        if (value.equals("Pågående")){
            setBackground(Color.yellow);
            setToolTipText ("Rad " + row + ", Pågående");
        } else if (value.equals("Avslutad")) {
            setBackground(Color.lightGray);
            setToolTipText ("Rad " + row + ", Avslutad");
        }
        else {
            setBackground(Color.white);
            setToolTipText ("Rad" + row + ", Kommande");
        }
        return component;
    }
}

package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * A class for creating and managing the JTables in the user interface.
 */
public abstract class BuildJTable {
    JTable table;
    DefaultTableModel tModel;

    public BuildJTable(Object[][] data, String[] columnNames){
        tModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

            @Override
            public Class getColumnClass(int column){
                return getValueAt(0, column).getClass();
            }
        };

        for (String str : columnNames){
            tModel.addColumn(str);
        }
        for (Object[] obj : data){
            this.addNewRow(obj);
        }
        this.table = new JTable(tModel);
        styleTable();
    }


    /**
     * Adds a new row at the bottom of the table with the given data.
     * @param data = the data to be inserted
     */
    public void addNewRow(Object[] data){
       tModel.addRow(data);
    }


    /**
     * Delete one or several consecutive rows in the table.
     * @param startRow = the first (or only row) that you want to delete
     * @param endRow   = the last row that you want to delete.
     */
    public void deleteRow(int startRow, int endRow){
        if (startRow < endRow){
            for (int i = startRow; i < endRow; ++i){
                tModel.removeRow(i);
            }
        } else {
            tModel.removeRow(startRow);
        }
    }


    /**
     * Get the table object.
     * @return = the table
     */
    public JScrollPane getScrollableTable(){
        return new JScrollPane(this.table);
    }


    /**
     * Override some default visual traits to make the
     * table more presentable and modern.
     */
    protected void styleTable(){
        this.table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        this.table.getTableHeader().setOpaque(false);
        this.table.getTableHeader().setBackground(new Color(32, 136, 203));
        this.table.getTableHeader().setForeground(new Color(255, 255, 255));
        this.table.setShowGrid(false);
        this.table.setIntercellSpacing(new Dimension(0,0));
        this.table.setRowHeight(30);
        //this.table.getColumnModel().getColumn(2).setMinWidth(100);
    }
}

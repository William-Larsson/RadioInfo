package view;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class BuildTableauJTable extends BuildJTable{


    /**
     * Build the Tableau table and set custom properties to
     * its cells and the cell data to create a better user experience.
     * @param data = data needed dto build JTable
     * @param columnNames = JTable columns.
     */
    public BuildTableauJTable(Object[][] data, String[] columnNames) {
        super(data, columnNames);
        this.table.getColumnModel().getColumn(3).setCellRenderer(new CellRenderer());
        this.setReadableStartAndEndTimes();
    }


    /**
     * Convert the time-string to a format that is easily readable
     * for the user.
     */
    public void setReadableStartAndEndTimes(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, HH:mm");
        for (int i = 0; i < table.getRowCount(); ++i){
            this.table.setValueAt(ZonedDateTime.parse(
                    (String) table.getValueAt(i, 1)).format(dtf),
                    i, 1
            );
            this.table.setValueAt(ZonedDateTime.parse(
                    (String) table.getValueAt(i, 2)).format(dtf),
                    i, 2);
        }
    }
}

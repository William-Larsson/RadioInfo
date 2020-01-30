package controller;
import model.XMLParser;

import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * SwingWorker for updating and displaying tableau data
 * to the GUI.
 */
public class TableauWorker extends Worker {
    private String actionSource;
    private Object channelImage;
    private String channelDescription;

    /**
     * Init the components and variables needed to
     * update the UI and create a XMlParser
     *
     * @param control        = the GUI component
     * @param primaryXMLNode = the primary node in the XML doc.
     * @param actionSource   = the source of the actionEvent
     */
    public TableauWorker(Controller control, String primaryXMLNode, String actionSource) {
        super(control, primaryXMLNode);
        this.actionSource = actionSource;
        this.channelImage = "";
        this.channelDescription = "";
    }


    /**
     * Get and parse XML. Collects the data into
     * easily accessible Object[][]
     * @return = data for a tableau
     * @throws Exception = in case of interruption or no schedule found
     */
    @Override
    protected Object[][] doInBackground() throws Exception {
        this.parser = new XMLParser(
                this.getTableauPreURL(control.getChannelData()), primaryNode
        );
        return parser.getChannelData(parser.getXmlNodes());
    }


    /**
     * Get the preURL (no date/pagination) for the
     * channel where the action event was called
     * @param data = the list of possible sources
     * @return = the url to the tableau for a channel
     */
    private String getTableauPreURL(Object[][] data)
            throws IllegalArgumentException
    {
        for (Object[] obj : data){
            if (obj[1].equals(actionSource)){
                this.channelImage = obj[0];
                this.channelDescription = obj[2].toString();
                return obj[3].toString();
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Get the data from doInBackground and build a
     * new table for the UI showcasing the tableau.
     * Sets the tableau visible and sets the channels-
     * -panel invisible.
     */
    @Override
    public void done(){
        try {
            Object[][] data = get();
            String[] columnNames = {"Radioprogram", "Starttid", "Sluttid", "Status"};
            control.getUI().initTableauComponents(
                    data, columnNames, this.channelImage, this.channelDescription
            );
            control.getUI().goToTableauView();
        } catch (InterruptedException | ExecutionException e) {
            control.getUI().noScheduleError();
        } finally {
            control.getUI().setWaitCursor(false);
        }
    }
}

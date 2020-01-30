package controller;
import model.XMLParser;
import view.UserInterface;

import java.util.concurrent.ExecutionException;

/**
 * SwingWorker for updating the Channels layout in the GUI.
 */
public class ChannelsWorker extends Worker {
    private String preURL;

    /**
     * Init the components and variables needed to
     * update the UI and create a XMlParser
     *
     * @param control        = the controller component
     * @param preURL         = unfinished URL to the XMl source
     * @param primaryXMLNode = the primary node in the XML doc.
     */
    public ChannelsWorker
    (Controller control, String primaryXMLNode, String preURL) {
        super(control, primaryXMLNode);
        this.preURL = preURL;
    }


    /**
     * Parser and get data from the channels part of the SR API
     * @return = data extracted from XML
     * @throws Exception = in case of IO error
     */
    @Override
    protected Object[][] doInBackground() throws Exception {
        this.parser = new XMLParser(preURL, primaryNode);
        return parser.getChannelData(parser.getXmlNodes());
    }


    /**
     * Update the current channels data for the view, and
     * set and replace the current GUI components with new
     * ones.
     */
    @Override
    public void done(){
        try {
            Object[][] data = get();
            control.setChannelData(data);
            control.getUI().initChannelComponents(data);
            control.getUI().goToChannelView();
            control.setUpUIListeners();
        } catch (InterruptedException | ExecutionException e) {
            UserInterface.IOError();
        } finally {
            control.getUI().setWaitCursor(false);
        }
    }
}

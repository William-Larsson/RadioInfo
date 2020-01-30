package controller;
import model.XMLParser;

import javax.swing.SwingWorker;

/**
 * Abstract SwingWorker class for fetching and parsing XMl
 * using the XMLParser class.
 */
abstract class Worker extends SwingWorker<Object[][], Object> {
    protected Controller control;
    protected String primaryNode;
    protected XMLParser parser;

    /**
     * Init the components and variables needed to
     * update the UI and create a XMlParser
     * @param control = the controller component
     * @param primaryXMLNode = the primary node in the XML doc.
     */
    public Worker (Controller control, String primaryXMLNode){
        this.control      = control;
        this.primaryNode  = primaryXMLNode;
        this.control.getUI().setWaitCursor(true);
    }
}

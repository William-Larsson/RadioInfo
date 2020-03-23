package controller;

import model.XMLParser;

import javax.swing.*;
import java.io.IOException;

public class Worker extends SwingWorker<Object[][], Object> {
    /**
     * Functional interface with sole purpose to signal that
     * the worker is done
     */
    @FunctionalInterface
    public interface WorkerFunctionalInterface {
        void done();
    }

    private WorkerFunctionalInterface workerFunc;
    private XMLParser xmlParser;
    private String primaryNode;
    private final String APIChannelsUrl = "http://api.sr.se/v2/channels/?";
    private final static String APIScheduleUrl = "http://api.sr.se/v2/scheduledepisodes?channelid=";

    /**
     * Init the Swing worker.
     * @param workerFunc = the functional interface method
     */
    public Worker(WorkerFunctionalInterface workerFunc, String primaryNode) {
        this.workerFunc = workerFunc;
        this.primaryNode = primaryNode;
    }

    /**
     * Parse XML and return the parsed values.
     * @return Object[][] = the
     */
    @Override
    protected Object[][] doInBackground() {
        try {
            this.testGUIResponsiveness();
            xmlParser = new XMLParser(APIChannelsUrl, primaryNode);
            return xmlParser.getChannelData(xmlParser.getXmlNodes());
        } catch (IOException e){
            return null;
        }
    }


    @Override
    protected void done() {
        workerFunc.done();
    }


    /**
     * Creates a computation that takes a long time to complete,
     * enough to test gui responsiveness.
     */
    private void testGUIResponsiveness(){
        int computation =0;
        for(int i=0;i<=600;i++) {
            for (int k = 0; k < 1000; k++) {
                computation += 1;
                System.out.println(computation);
            }
        }
    }
}

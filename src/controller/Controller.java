package controller;
import view.UserInterface;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;

public class Controller {
    private UserInterface ui;
    private Worker worker = null;
    private final static String AppName = "RadioInfo";
    private final static String APIUrl = "http://api.sr.se/v2/channels/?";


    public Controller(){
        ui = new UserInterface(AppName);
        startWorkerTimer();
        //TODO: continue to rebuild the controller and the new Worker class
    }

    private void startWorkerTimer(){
        Timer timer = new Timer(60 * 60 * 1000, this::startWorker);
        timer.setInitialDelay(0);
        timer.start();
    }

    private void startWorker(ActionEvent event){
        if (worker == null){
            worker = new Worker(this::executeWorker);
            worker.execute();
        }
    }

    private void executeWorker() {
        try {
            Object[][] data = worker.get(); //Waits if necessary for the computation to complete, and then retrieves its result.
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setup button listeners for the UI components.
     */
    public void setUpUIListeners(){
        ui.setChannelMenuItemListener(actionEvent -> ui.goToChannelView());
        //ui.setUpdateMenuItemListener(actionEvent -> this.updateTableData());
        ui.setAboutMenuItemListener(actionEvent -> ui.openAboutRadioInfoWindow());
        ui.setExitMenuItemListener(actionEvent -> ui.exitProgram());
        //ui.setChannelJButtonListeners(this::startTableauWorker);
    }
}
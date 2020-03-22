package controller;
import view.UserInterface;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;

/**
 * This is the main controller that sits in between the Model classes and
 * the view, directing the input from the view to model classes and finally
 * back to the view with new and updated data.
 *
 *
 * Tableau: "http://api.sr.se/v2/scheduledepisodes?channelid=164", primaryNode: "scheduledepisode"
 * Channels: "http://api.sr.se/v2/channels/? ", primaryNode: "channel"
 */
public class Controller {
    private UserInterface ui;
    private Worker worker = null;
    private final static String AppName = "RadioInfo";
    private final static String APIChannelsUrl = "http://api.sr.se/v2/channels/?";
    private final static String APIScheduleUrl = "http://api.sr.se/v2/scheduledepisodes?channelid=";


    /**
     * Init the controller and graphical user interface.
     */
    public Controller(){
        ui = new UserInterface(AppName);
        startWorkerTimer();
        //TODO: continue to rebuild the controller and the new Worker class.
    }


    /**
     * Immediately start a worker that will update the
     * application and run this same method once every hour.
     */
    private void startWorkerTimer(){
        Timer timer = new Timer(60 * 60 * 1000, this::startWorker);
        timer.setInitialDelay(0);
        timer.start();
    }


    /**
     * If there is no worker currently running, start a new
     * one to update the ui.
     * @param event = The timer or user input.
     */
    private void startWorker(ActionEvent event){
        if (worker == null){
            worker = new Worker(this::updateUI);
            worker.execute();
        }
    }


    /**
     * Update the ui with the latest information.
     * Is used together with the functional interface used
     * by the swing workers.
     */
    private void updateUI() {
        try {
            Object[][] data = worker.get(); //Waits for the computation to complete, then gets the result.
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
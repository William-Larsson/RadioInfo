package controller;
import view.UserInterface;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;

/**
 * This is the main controller that sits in between the Model classes and
 * the view, directing the input from the view to model classes and finally
 * back to the view with new and updated data.
 */
public class Controller {
    private UserInterface ui;
    private Worker worker;
    private Timer updateTimer;
    private final static String AppName = "RadioInfo";
    private final static String APIChannelsPrimaryNode = "channel";
    private final static String APISchedulePrimaryNode = "scheduledepisode";


    /**
     * Init the controller functionality and graphical user interface.
     */
    public Controller(){
        this.ui = new UserInterface(AppName);
        this.worker = null;
        startWorkerTimer();
        setUpMenuListeners();
        //TODO: continue to rebuild the controller and the new Worker class.
    }


    /**
     * Immediately start a worker that will update the
     * application and run this same method once every hour.
     */
    private void startWorkerTimer(){
        if (updateTimer != null){
            if (updateTimer.isRunning()){
                updateTimer.stop();
            }
        }

        //TODO: change timer to  15*1000 for testing!
        updateTimer = new Timer(60*60*1000, this::startWorker);
        updateTimer.setInitialDelay(0);
        updateTimer.start();
    }


    /**
     * If there is no worker currently running, start a new
     * one to update the ui.
     * @param event = The timer or user input.
     */
    private void startWorker(ActionEvent event){
        if (worker == null){
            Object eventSource = event.getSource();
            Class eventClass = eventSource.getClass();

            //TODO: continue to work here...
            ui.setWaitCursor(true);
            if (JButton.class.isAssignableFrom(eventClass)){
                worker = new Worker(this::updateUI, APISchedulePrimaryNode);
            } else {
                worker = new Worker(this::updateUI, APIChannelsPrimaryNode);
            }

            worker.execute();
        }
    }


    /**
     * Update the UI with the latest information.
     * Is used together with the functional interface used
     * by the swing workers.
     */
    private void updateUI() {
        try {
            // .get() waits for the computation to complete, then gets the result.
            Object[][] data = worker.get();

            if (data == null){
                UserInterface.IOError();
            } else {
                ui.initChannelComponents(data);
                ui.goToChannelView();
                // TODO: should I set the listeners for the JButtons somewhere else?
                ui.setChannelJButtonListeners(this::startWorker);
                ui.setVisible(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            ui.setWaitCursor(false);
        }

        worker = null;
    }


    /**
     * Setup button listeners for the JMenu components.
     */
    public void setUpMenuListeners(){
        ui.setChannelMenuItemListener(actionEvent -> ui.goToChannelView());
        ui.setUpdateMenuItemListener(actionEvent -> this.startWorkerTimer());
        ui.setAboutMenuItemListener(actionEvent -> ui.openAboutRadioInfoWindow());
        ui.setExitMenuItemListener(actionEvent -> ui.exitProgram());
    }
}
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
    private Object[][] channelData;
    private final static String AppName = "RadioInfo";
    private final static String APIChannelsPrimaryNode = "channel";
    private final static String APISchedulePrimaryNode = "scheduledepisode";
    private final static String APIChannelsUrl = "http://api.sr.se/v2/channels/?";


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

            if (JButton.class.isAssignableFrom(eventClass)){
                String scheduleURL = this.getTableauPreURL(event);
                worker = new Worker(this::updateTableauUI, scheduleURL, APISchedulePrimaryNode);
            } else {
                worker = new Worker(this::updateChannelUI, APIChannelsUrl, APIChannelsPrimaryNode);
            }

            ui.setWaitCursor(true);
            worker.execute();
        }
    }


    /**
     * Update the channels UI with the latest information.
     * Is used together with the functional interface used
     * by the swing workers.
     */
    private void updateChannelUI() {
        try {
            // .get() waits for the computation to complete, then gets the result.
            Object[][] data = worker.get();

            if (data == null){
                UserInterface.IOError();
            } else {
                this.channelData = data;
                ui.initChannelComponents(data);
                ui.goToChannelView();
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
     * Update the tableau UI with the latest information.
     * Is used together with the functional interface used
     * by the swing workers.
     */
    private void updateTableauUI() {
        try {
            // .get() waits for the computation to complete, then gets the result.
            Object[][] data = worker.get();
            String[] columnNames = {"Radioprogram", "Starttid", "Sluttid", "Status"};

            if (data == null){
                UserInterface.IOError();
            } else {
                //Todo: change this init call!
                ui.initTableauComponents(data, columnNames, this.channelData[0][0], "this is a test");

                ui.goToTableauView();
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


    /**

     */
    /**
     * * Get the preURL (no date/pagination) for the
     * channel where the action event was called
     * @param eventSource = the source that triggered the event
     * @return = the url to the tableau for a channel
     * @throws IllegalArgumentException = the source does not match
     *                                    any channel.
     * @throws NullPointerException = channel data is missing.
     */
    private String getTableauPreURL(ActionEvent eventSource)
            throws IllegalArgumentException, NullPointerException
    {
        if (this.channelData != null){
            String event = eventSource.getActionCommand();
            for (Object[] obj : channelData){
                if (obj[1].equals(event)){

                    // TODO: the commented section was previously used to store the
                    //      channel image/description so these could be displayed at
                    //      the top of the tableau page.
                    //this.channelImage = obj[0];
                    //this.channelDescription = obj[2].toString();

                    return obj[3].toString();
                }
            }
            throw new IllegalArgumentException();
        } else {
            throw new NullPointerException();
        }
    }
}
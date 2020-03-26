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
    private Object[] tableauData;

    /**
     * Init the controller functionality and graphical user interface.
     */
    public Controller(){
        this.ui = new UserInterface("RadioInfo");
        this.worker = null;
        startWorkerTimer();
        setUpMenuListeners();
    }


    /**
     * Immediately start a worker that will update the
     * application and run this same method once every hour.
     * Will reset the UI to the applications page no matter
     * what where the user is in the application.
     */
    private void startWorkerTimer(){
        if (updateTimer != null){
            if (updateTimer.isRunning()){
                updateTimer.stop();
            }
        }
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
                this.setTableauData(event);
                if (this.tableauData != null){
                    String scheduleURL = (String)this.tableauData[2];
                    String APISchedulePrimaryNode = "scheduledepisode";
                    worker = new Worker(this::updateTableauUI, scheduleURL, APISchedulePrimaryNode);
                }
            } else {
                String APIChannelsPrimaryNode = "channel";
                String APIChannelsUrl = "http://api.sr.se/v2/channels/?";
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
            Object[][] data = worker.get();
            String[] columnNames = {"Radioprogram", "Starttid", "Sluttid", "Status"};

            if (data == null){
                UserInterface.noScheduleError();
            } else {
                ui.initTableauComponents(
                    data,
                    columnNames,
                    this.tableauData[0],
                    (String)this.tableauData[1]
                );
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
    private void setUpMenuListeners(){
        ui.setChannelMenuItemListener(actionEvent -> ui.goToChannelView());
        ui.setUpdateMenuItemListener(actionEvent -> this.startWorkerTimer());
        ui.setAboutMenuItemListener(actionEvent -> ui.openAboutRadioInfoWindow());
        ui.setExitMenuItemListener(actionEvent -> ui.exitProgram());
    }


    /**
     * * Get the preURL (no date/pagination) for the
     * channel where the action event was called
     * @param eventSource = the source that triggered the event
     * @throws IllegalArgumentException = the source does not match
     *                                    any channel.
     * @throws NullPointerException = channel data is missing.
     */
    private void setTableauData(ActionEvent eventSource)
            throws IllegalArgumentException, NullPointerException
    {
        if (this.channelData != null){
            Object[] data = new Object[3];
            String event = eventSource.getActionCommand();
            for (Object[] obj : channelData){
                if (obj[1].equals(event)){
                    data[0] = obj[0]; // channel image
                    data[1] = obj[2].toString(); // channel desc.
                    data[2] = obj[3].toString(); // url to schedule
                    this.tableauData = data;
                    return;
                }
            }
            throw new IllegalArgumentException();
        } else {
            throw new NullPointerException();
        }
    }
}
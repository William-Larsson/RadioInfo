package controller;
import model.XMLParser;
import view.UserInterface;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Date;
import java.util.Timer;
import java.util.TimerTask;

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
    UserInterface ui;
    XMLParser radioChannels;
    Object[][] radioChannelsData;

    public Controller(){
        try {
            this.radioChannels = new XMLParser(
                    "http://api.sr.se/v2/channels/?",
                    "channel"
            );
            this.ui = new UserInterface("RadioInfo");
            this.radioChannelsData =
                    radioChannels.getChannelData(radioChannels.getXmlNodes());
            this.ui.initUIComponents(this.radioChannelsData);
            this.setUpUIListeners();
            this.ui.setVisible(true);
            this.setTimerForWorker();
        } catch (IOException e) {
            UserInterface.IOError();
        }
    }


    /**
     * Setup button listeners for the UI components.
     */
    public void setUpUIListeners(){
        ui.setChannelMenuItemListener(actionEvent -> ui.goToChannelView());
        ui.setUpdateMenuItemListener(actionEvent -> this.updateTableData());
        ui.setAboutMenuItemListener(actionEvent -> ui.openAboutRadioInfoWindow());
        ui.setExitMenuItemListener(actionEvent -> ui.exitProgram());
        ui.setChannelJButtonListeners(this::startTableauWorker);
    }



    /**
     * Update the GUI once every hour by invoking the TableauWorker
     * to keep the info fresh as long as the program is running.
     * fresh.
     */
    private void setTimerForWorker(){
        Timer timer = new Timer();
        timer.schedule(new UpdateTask(this) {
            @Override
            public void run(){
                super.run();
            }
        }, 0, 60*60*1000);
    }

    /*
     * Updates the gui by starting a new SwingWorker.
     */
    public synchronized void updateTableData(){
        new ChannelsWorker(
                this,
                "channel",
                "http://api.sr.se/v2/channels/?"
        ).execute();
    }


    /**
     * Start a new SwingWorker for fetching and
     * initializing a new tableau for a given channel.
     * @param event = button event for the given channel
     */
    private synchronized void startTableauWorker(ActionEvent event){
        new TableauWorker(
                this,
                "scheduledepisode",
                event.getActionCommand()
        ).execute();
    }

    public UserInterface getUI(){
        return ui;
    }

    public synchronized Object[][] getChannelData(){
        return radioChannelsData;
    }

    public synchronized void setChannelData(Object[][] data){
        this.radioChannelsData = data;
    }
}

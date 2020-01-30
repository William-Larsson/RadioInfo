package controller;
import java.util.TimerTask;

/**
 * Used to update the GUI within a TimerSchedule so that it can be
 * automatically renewed within a set interval.
 */
public class UpdateTask extends TimerTask {
    private Controller controller;

    public UpdateTask(Controller controller){
        this.controller = controller;
    }

    /**
     * Update the program info
     */
    @Override
    public void run() {
        controller.updateTableData();
    }
}

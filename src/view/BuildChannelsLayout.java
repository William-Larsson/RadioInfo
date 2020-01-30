package view;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A class for build the Channels view. Sets up the layout,
 * and buttons used for traversing the GUI
 */
public class BuildChannelsLayout {
    private ImageCreator imgCreate;
    private Object[][] data;
    private ArrayList<JButton> buttons;
    private JPanel panel;

    public BuildChannelsLayout(Object[][] data){
        this.data = data;
        imgCreate = new ImageCreator();
        buttons   = new ArrayList<>();
        panel     = new JPanel(new GridLayout(0,3, 10, 10));

        for (Object[] obj : data){
            JButton button = new JButton(
                    (String) obj[1],
                    imgCreate.objectToImageIconOfSize(obj[0], 60, 60)
            );
            button.setBackground(new Color(200, 200, 200));
            button.setForeground(new Color(0,0,0));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            buttons.add(button);
            panel.add(button);
        }
    }


    /**
     * The the instances for all JButtons that inside
     * the channels-view panel-
     * @return = JButtons ArrayList
     */
    public ArrayList<JButton> getJButtons(){
        return buttons;
    }


    /**
     * Get the panels as a scrollable object
     * @return = a scroll pane
     */
    public JScrollPane getScrollableChannelsPanel(){
        JScrollPane scroll = new JScrollPane(this.panel);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        return scroll;
    }
}

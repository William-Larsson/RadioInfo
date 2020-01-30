package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * The graphical user interface that will display the program
 * and all of its information ond options to the user.
 */
public class UserInterface {
    private static JFrame frame;
    private BuildJMenuBar buildJMenuBar;
    private BuildChannelsLayout buildChannelsLayout;
    private BuildTableauJTable buildTableauJTable;
    private JPanel tableauPanel;
    private JScrollPane channelsPanel;
    private JScrollPane tableauTable;
    private JMenuBar menuBar;

    /**
     * Setup the basic properties of the user interface.
     * @param title = The title of the window.
     */
    public UserInterface(String title){
        frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(new Dimension(900, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
    }


    /**
     * Build the UI component that will be active at the start
     * of the program.
     * @param data = Data[][] so that a JTable can be built
     *               for the channels table.
     */
    public void initUIComponents(Object[][] data){
        this.buildJMenuBar = new BuildJMenuBar();
        this.menuBar = buildJMenuBar.getMenubar();
        frame.setJMenuBar(this.menuBar);
        initChannelComponents(data);
        frame.setLocationRelativeTo(null);
    }


    /**
     * init and set the components needed to display the
     * channels view.
     * @param data = data for the buttons in the view.
     */
    public void initChannelComponents(Object[][] data) {
        if (channelsPanel != null){
            frame.remove(channelsPanel);
        }
        this.buildChannelsLayout = new BuildChannelsLayout(data);
        this.channelsPanel = this.buildChannelsLayout.getScrollableChannelsPanel();
        frame.add(channelsPanel);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }


    /**
     * Build and initialize all components needed to view
     * a program tableau.
     * @param data
     * @param columnNames
     */
    public void initTableauComponents(
            Object[][] data, String[] columnNames, Object image, String desc)
    {
        this.tableauPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel header = this.buildTableauHeader(image, desc);

        this.buildTableauJTable = new BuildTableauJTable(data, columnNames);
        this.tableauTable = buildTableauJTable.getScrollableTable();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.tableauPanel.add(header, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.tableauPanel.add(this.tableauTable, gbc);

        frame.add(this.tableauPanel);
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }


    /**
     * Build header with image and description used in the
     * tableau-view.
     * @param image = URL or String to image
     * @param desc = the description text
     * @return = JPanel
     */
    public JPanel buildTableauHeader(Object image, String desc){
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));

        ImageCreator imgCreate = new ImageCreator();
        JLabel imgWrapper = new JLabel(
                imgCreate.objectToImageIconOfSize(image, 80, 80));

        JTextArea description = new JTextArea(5, 75);
        description.setLineWrap(true);
        description.append(desc);

        header.add(imgWrapper, BorderLayout.WEST);
        header.add(description, BorderLayout.CENTER);
        return header;
    }


    /**
     * Make the program window visible or not.
     * @param value = true of false
     */
    public void setVisible(boolean value){
        frame.setVisible(value);
    }


    /**
     * Set the channel table to visible and the
     * tableau table to non visible in order to
     * change what is being displayed on the GUI.
     */
    public void goToChannelView() {
        channelsPanel.setVisible(true);
        if (tableauPanel != null){
            tableauPanel.setVisible(false);
        }
    }


    /**
     * Set the channel table to visible and the
     * tableau table to non visible in order to
     * change what is being displayed on the GUI.
     */
    public void goToTableauView() {
        tableauPanel.setVisible(true);
        if (channelsPanel != null){
            channelsPanel.setVisible(false);
        }
    }


    /**
     * Opens a little pop-up window with a little bit of
     * information about the program.
     */
    public void openAboutRadioInfoWindow(){
        JOptionPane.showMessageDialog(
                null,
                "Välkommen till RadioInfo!\n" +
                        "Här kan du enkelt hitta information" +
                        "om alla radiokanaler från SR.\n " +
                        "Du kan även se kanalernas aktuella " +
                        "programtablå.",
                "Om RadioInfo",
                JOptionPane.OK_OPTION
        );
    }


    /**
     * Close down the program.
     */
    public void exitProgram(){
        frame.dispose();
    }


    /**
     * Change cursor state to indicate that page is loading
     * @param on = page is loading
     */
    public void setWaitCursor(boolean on){
        if(on){
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            frame.setCursor(Cursor.getDefaultCursor());
        }
    }


    /**
     * Create actionListener for first menu item
     * in the menu.
     * @param actionListener = the listener to set.
     */
    public void setChannelMenuItemListener(ActionListener actionListener) {
        menuBar.getMenu(0).getItem(0).addActionListener(actionListener);
    }


    /**
     * Create actionListener for second menu item
     * in the menu.
     * @param actionListener = the listener to set.
     */
    public void setUpdateMenuItemListener(ActionListener actionListener) {
        menuBar.getMenu(0).getItem(1).addActionListener(actionListener);
    }


    /**
     * Create actionListener for third menu item
     * in the menu (excluding separators).
     * @param actionListener = the listener to set.
     */
    public void setAboutMenuItemListener(ActionListener actionListener) {
        menuBar.getMenu(0).getItem(3).addActionListener(actionListener);
    }


    /**
     * Create actionListener for forth menu item
     * in the menu (excluding separators).
     * @param actionListener = the listener to set.
     */
    public void setExitMenuItemListener(ActionListener actionListener) {
        menuBar.getMenu(0).getItem(5).addActionListener(actionListener);
    }


    /**
     * listeners for all the channel buttons
     * @param actionListener = the listener to set
     */
    public void setChannelJButtonListeners(ActionListener actionListener){
        for (JButton button: buildChannelsLayout.getJButtons()){
            button.addActionListener(actionListener);
        }
    }


    /**
     * In case there is an issue with the schedule url.
     */
    public void noScheduleError(){
        JOptionPane.showMessageDialog(null,
                "Ingen programtablå kunde hittas för denna kanal.",
                "RadioInfo",
                JOptionPane.ERROR_MESSAGE);
    }


    /**
     * In case there is an issue with the connection to the server.
     */
    public static void IOError(){
        JOptionPane.showMessageDialog(null,
                "Ett fel intäffade vid kommunikation med " +
                        "vår server.\n Vänligen kontrollera din " +
                        "internetanslutning.",
                "RadioInfo",
                JOptionPane.ERROR_MESSAGE);
    }
}
package view;
import javax.swing.*;

/**
 * Builds a JMenuBar with one menu that will be used to
 * to navigate the program
 */
public class BuildJMenuBar {
    private JMenuBar menubar;

    /**
     * Build the JMenubar with a menu and menu items
     */
    public BuildJMenuBar(){
        this.menubar = new JMenuBar();
        JMenu menu   = new JMenu("Meny");
        JMenuItem i1 = new JMenuItem("Alla Kanaler");
        JMenuItem i2 = new JMenuItem("Uppdatera sidan");
        JMenuItem i3 = new JMenuItem("Om RadioInfo");
        JMenuItem i4 = new JMenuItem("Avsluta");

        menu.add(i1);
        menu.add(i2);
        menu.addSeparator();
        menu.add(i3);
        menu.addSeparator();
        menu.add(i4);
        this.menubar.add(menu);
    }


    /**
     * Returns the menu bar.
     * @return = JMenuBar
     */
    public JMenuBar getMenubar(){
        return menubar;
    }
}

import controller.Controller;
import javax.swing.*;

/**
 * A program that displays information regarding radio programs to
 * the user with a GUI. The data is read from and provided by
 * Sveriges Radio using their open API.
 *
 * Author: William Larsson
 * Date: 2020-01-26
 */
public class RadioInfoMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Controller::new);
    }
}

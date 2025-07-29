/*
Emily Yu
December 19, 2024
Wordle Rules Window
 */

import java.awt.EventQueue;
import javax.swing.JFrame;

public class WordleRulesWindow extends JFrame {

    /**
     * Default constructor
     */
    public WordleRulesWindow() {
        //create the User interface
        initUI();

    }

    /**
     * Create the custom JFrame and set some options
     */
    private void initUI() {
        //set title of the JFrame
        setTitle("Wordle Rules");

        //set the size of the window
        setSize(450, 690);

        //make it visible 
        setVisible(true);

        //ensures that when the Wordle window is closed, the main GUI is not closed along with it
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        //makes sure that GUI updates nicely with the rest of the OS
        EventQueue.invokeLater(() -> {
            //create the JFrame
            WordleRulesWindow frame = new WordleRulesWindow();
        });
    }

}

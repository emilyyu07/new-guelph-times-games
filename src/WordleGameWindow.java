/*
Emily Yu
December 19, 2024
Wordle Game Window
*/

import java.awt.EventQueue;
import javax.swing.JFrame;


public class WordleGameWindow extends JFrame {
    
    /**
     * Default constructor
     */
    public WordleGameWindow() {
        //create the User interface
        initUI();
        
    }
    
    /**
     * Create the custom JFrame and set some options
     */
    private void initUI() {        
        //set title of the JFrame
        setTitle("Wordle");
        
        //set the size of the window
        setSize(555, 740);
        
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
            WordleGameWindow frame = new WordleGameWindow();
        });
    }
    
    
}
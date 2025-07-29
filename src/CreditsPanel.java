/*
Emily Yu
January 14, 2025
Credits Drawing Panel
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author emily
 */
public class CreditsPanel extends JPanel {

    /**
     * Does the actual drawing
     *
     * @param g - the Graphics object to draw with
     */
    private void writeCredit(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //create/set new font
        Font font = new Font("Arial", Font.PLAIN, 20);
        g2d.setFont(font);

        setBackground(Color.WHITE);
        
        //write credits message
        g2d.drawString("Brought to you by BEJ Productions", 10, 30);
        g2d.drawString("Developed by Emily Yu, Jeremy Wong, and Bob Zhao", 10, 60);

    }

    /**
     * Overrides paintComponent in JPanel class so that we can do our own custom
     * painting
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//does the necessary work to prepare the panel for drawing
        writeCredit(g); //invoke our custom drawing method
    }

}

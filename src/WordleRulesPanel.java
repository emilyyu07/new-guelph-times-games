/*
Emily Yu
December 19, 2024
Wordle Rules Drawing Panel
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author emily
 */
public class WordleRulesPanel extends JPanel {

    /**
     * Does the actual drawing
     *
     * @param g - the Graphics object to draw with
     */
    private void drawImg(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        setBackground(Color.WHITE);

        Image rules = new ImageIcon(this.getClass().getResource("WordleInstructions.png")).getImage();
        g.drawImage(rules, 10, 10, this);

    }

    /**
     * Overrides paintComponent in JPanel class so that we can do our own custom
     * painting
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//does the necessary work to prepare the panel for drawing
        drawImg(g); //invoke our custom drawing method
    }


}

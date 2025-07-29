/*
Emily Yu
January 10, 2025
Drawing Panel for Wordle statistics
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author emily
 */
public class WordleStatsPanel extends JPanel {

    private WordleStats stats;
    private int gamesPlayed;
    private int gamesWon;
    private ArrayList<Integer> guessDistribution;

    /**
     * Constructor for WordleStatsPanel
     * @param stats - WordleStats object
     */
    public WordleStatsPanel(WordleStats stats) {
        this.stats = stats;
        gamesPlayed = stats.getGamesPlayed();
        gamesWon = stats.getGamesWon();
        guessDistribution = stats.getGuessDistribution();
    }

    /**
     * Write the Wordle Stats
     *
     * @param g - the Graphics object to draw with
     */
    private void writeText(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        setBackground(Color.WHITE);

        //create/set new font
        Font font = new Font("Arial", Font.PLAIN, 15);
        g2d.setFont(font);

        //tracks the number of times the user won the game in 'x' guesses
        //follows the same "format" as the guess distribution statistic in the real Wordle
        int oneGuess = 0;
        int twoGuess = 0;
        int threeGuess = 0;
        int fourGuess = 0;
        int fiveGuess = 0;
        int sixGuess = 0;

        //count the number of guess occurences
        for (int i = 0; i < guessDistribution.size(); i++) {
            int guessNum = guessDistribution.get(i);
            if (guessNum == 1) {
                oneGuess++;
            } else if (guessNum == 2) {
                twoGuess++;
            } else if (guessNum == 3) {
                threeGuess++;
            } else if (guessNum == 4) {
                fourGuess++;
            } else if (guessNum == 5) {
                fiveGuess++;
            } else if (guessNum == 6) {
                sixGuess++;
            }
        }

        setBackground(Color.WHITE);
        g2d.drawString("Games Played: " + gamesPlayed, 10, 30);
        g2d.drawString("Games Won: " + gamesWon, 10, 50);
        g2d.drawString("GUESS DISTRIBUTION: ", 10, 90);
        g2d.drawString("1: " + oneGuess, 10, 110);
        g2d.drawString("2: " + twoGuess, 10, 130);
        g2d.drawString("3: " + threeGuess, 10, 150);
        g2d.drawString("4: " + fourGuess, 10, 170);
        g2d.drawString("5: " + fiveGuess, 10, 190);
        g2d.drawString("6: " + sixGuess, 10, 210);

    }

    /**
     * Overrides paintComponent in JPanel class so that we can do our own custom
     * painting
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//does the necessary work to prepare the panel for drawing
        writeText(g); //invoke our custom drawing method
    }

}

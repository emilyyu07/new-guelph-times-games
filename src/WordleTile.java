/*
Emily Yu
December 17, 2024
WordleTile Class

Note on colour coding:
Green - if correctPos and includedLetter are both true
Yellow - if includedLetter is true and correctPos is false
Dark gray - if neither variable is true
 */

import java.awt.Color;

public class WordleTile extends AbstractTile {

    private boolean correctPos;
    private boolean includedLetter;

    /**
     * Constructor for WordleTile
     *
     * @param letter - letter on tile
     */
    public WordleTile(String letter) {
        super(letter);
        color = Color.WHITE;
        correctPos = false;
        includedLetter = false;
        size = 55;
    }

    /**
     * Constructor for WordleTile
     *
     * @param letter - letter on tile
     * @param size - size of tile
     */
    public WordleTile(String letter, int size) {
        this(letter);
        this.size = size;

    }

    /**
     * Getter for includedLetter
     *
     * @return - whether or not the letter on the tile is included in the target
     * word (true or false)
     */
    public boolean isIncludedLetter() {
        return includedLetter;
    }

    /**
     * Setter for includedLetter
     *
     * @param includedLetter - whether or not the letter on the tile is included
     * in the target word (true or false)
     */
    public void setIncludedLetter(boolean includedLetter) {
        this.includedLetter = includedLetter;
    }

    /**
     * Getter for letter
     *
     * @return - letter on the tile
     */
    public String getLetter() {
        return letter;
    }

    /**
     * Getter for tile colour
     *
     * @return - colour
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter for correctPos
     *
     * @return whether or not tile is in correct position
     */
    public boolean isCorrectPos() {
        return correctPos;
    }

    /**
     * Setter for letter
     *
     * @param letter - letter
     */
    public void setLetter(String letter) {
        this.letter = letter;
    }

    /**
     * Setter for colour
     *
     * @param color - tile colour
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Setter for correctPos
     *
     * @param correctPos - whether or not the tile is in the correct position
     */
    public void setCorrectPos(boolean correctPos) {
        this.correctPos = correctPos;
    }

    
    /**
     * toString method for WordleTile (displays contents of tile)
     *
     * @return - message displaying details of the WordleTile
     */
    @Override
    public String toString() {
        return "WordleTile{" + "correctPos=" + correctPos + ", includedLetter=" + includedLetter + '}';
    }
    
    

    /**
     * Clone method for WordleTile
     *
     * @param other - another WordleTile
     * @return - cloned WordleTile
     */
    public WordleTile clone(WordleTile other) {
        WordleTile cloneTile = new WordleTile(this.letter);
        cloneTile.setColor(this.color);
        return cloneTile;
    }
    
    

}

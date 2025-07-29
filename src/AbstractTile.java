/*
 * Bob Zhao
 * December 17, 2024
 * An abstract Tile class that defines a square tile that holds String and color
 */

import java.awt.Color;

/**
 *
 * @author BoZha7871
 */
abstract public class AbstractTile {

    // declare attributes for the letter and color
    protected String letter;
    protected Color color;
    protected int x, y, size;

    /**
     * Default Constructor
     *
     * @param letter - the word/letter that is stored with the tile (String)
     */
    public AbstractTile(String letter) {
        this.letter=letter;
        color = Color.BLACK;
        // make default x and y 0 and size 5
        x = 0;
        y = 0;
        size = 50;

    }



    /**
     * Secondary constructor
     *
     * @param letter - the letter/word stored with the tile
     * @param c - the color of the tile
     * @param x - the x position of the tile
     * @param y - the y position of the tile
     * @param size - the size of one of the lengths of the square tile
     */
    public AbstractTile(String letter, Color c, int x, int y, int size) {
        this(letter); // chain with the previous constructor
        color = c;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    /**
     * Accessor for the letter 
     *
     * @return - the String letter
     */
    public String getLetter() {
        return letter;
    }

    /**
     * Mutator for the letter variable
     *
     * @param letter - the new letter being stored
     */
    public void setLetter(String letter) {
        this.letter=letter;
    }

    /**
     * Accessor for the Color
     *
     * @return - the color of the tile
     */
    public Color getColor() {
        return color;
    }

    /**
     * Mutator for the color of the tile
     *
     * @param color - the new color being set to
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Accessor for the x coordinate
     *
     * @return - the x position
     */
    public int getXPos() {
        return x;
    }

    /**
     * Mutator for the x coordinate
     *
     * @param x - the new x to be moved to
     */
    public void setXPos(int x) {
        this.x = x;
    }

    /**
     * Accessor for the y coordinate
     *
     * @return - the y coordinate
     */
    public int getYPos() {
        return y;
    }

    /**
     * Mutator for the y coordinate
     *
     * @param y - the new y position
     */
    public void setYPos(int y) {
        this.y = y;
    }

    /**
     * Accessor for the size of the tile
     *
     * @return - the integer size of the tile
     */
    public int getSize() {
        return size;
    }

    /**
     * Mutator for the size of the tile
     *
     * @param size - the new size of the tile
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * A toString method for the attributes of the tile
     *
     * @return - a String representation
     */
    public String toString() {
        String s = "Tile\n"
                + "Text: " + letter + "\n"
                + "Color: " + color + "\n"
                + "Coordinates: (" + x + ", " + y + ")\n"
                + "Size: " + size;
        return s;
    }

}

/*
 * Bob Zhao
 * December 17, 2024
 * An abstract Tile class that defines a square tile that holds String and color
 */
package mouselistenerlearning;

import java.awt.Color;

/**
 *
 * @author BoZha7871
 */
abstract public class AbstractTile {
    // declare attributes for the word and color
    protected String word;
    protected Color color;
    protected int x,y,size;
    
    /**
     * Default Constructor
     * @param word - the word that is stored with the tile
     */
    public AbstractTile(String word){
       this.word = word;
       color = Color.BLACK;
       // make default x and y 0 and size 5
       x = 0;
       y = 0;
       size = 5;
       
    }
    /**
     * Secondary constructor
     * @param word - the word stored with the tile
     * @param c - the color of the tile
     * @param x - the x position of the tile
     * @param y - the y position of the tile
     * @param size - the size of one of the lengths of the square tile
     */
    public AbstractTile(String word, Color c, int x, int y, int size){
       this(word); // chain with the previous constructor
       color = c;
       this.x = x;
       this.y = y;
       this.size = size;
    }
    /**
     * Accessor for the word 
     * @return - the String word
     */
    public String getWord(){
        return word;
    }
    /**
     * Mutator for the word variable
     * @param word - the new word being stored
     */
    public void setWord(String word){
        this.word = word;
    }
    /**
     * Accessor for the Color
     * @return - the color of the tile
     */
    public Color getColor(){
        return color;
    }
    /**
     * Mutator for the color of the tile
     * @param color - the new color being set to
     */
    public void setColor(Color color){
        this.color = color;
    }
    /**
     * Accessor for the x coordinate
     * @return - the x position
     */
    public int getXPos(){
        return x;
    }
    /**
     * Mutator for the x coordinate
     * @param x - the new x to be moved to
     */
    public void setXPos(int x){
        this.x = x;
    }
    /**
     * Accessor for the y coordinate
     * @return - the y coordinate
     */
    public int getYPos(){
        return y;
    }
    /**
     * Mutator for the y coordinate
     * @param y - the new y position 
     */
    public void setYPos(int y){
        this.y = y;
    }
    /**
     * Accessor for the size of the tile
     * @return - the integer size of the tile
     */
    public int getSize(){
        return size;
    }
    /**
     * Mutator for the size of the tile
     * @param size - the new size of the tile
     */
    public void setSize(int size){
        this.size = size;
    }
    /**
     * A toString method for the attributes of the tile
     * @return - a String representation
     */
    public String toString(){
        String s = "Tile\n"
                + "Text: "+word+"\n"
                + "Color: "+color+"\n"
                + "Coordinates: ("+x+", "+y+")\n"
                + "Size: "+size;
        return s;
    }   

}   

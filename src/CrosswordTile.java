/*
 * Bob Zhao
 * December 17, 2024
 * A tile for the crossword game
 */

import java.awt.Color;
import java.util.Objects;

/**
 *
 * @author BoZha7871
 */
public class CrosswordTile extends AbstractTile{
    protected String rowPrompt, colPrompt;
    protected int clueNumV, clueNumH;
    /**
     * Default Constructor
     * @param letter - the word that is stored with the tile
     */
    public CrosswordTile(String letter){
       super(letter);
    }
    /**
     * Secondary constructor
     * @param letter - the word stored with the tile
     * @param c - the color of the tile
     * @param x - the x position of the tile
     * @param y - the y position of the tile
     * @param size - the size of one of the lengths of the square tile
     */
    public CrosswordTile(String letter, Color c, int x, int y, int size){
       super(letter, c, x, y, size);
       rowPrompt = "";
       colPrompt = "";
       clueNumV = 0;
       clueNumH = 0;
    }
    /**
     * Tertiary constructor 
     * @param letter - the word stored with the tile
     * @param c - the color of the tile
     * @param x - the x position of the tile
     * @param y - the y position of the tile
     * @param size - the size of one length of the tile
     * @param rP - the row prompt
     * @param cP - the column prompt
     * @param clue - the clue number
     */
    public CrosswordTile(String letter, Color c, int x, int y, int size, String rP, String cP, int clueV, int clueH){
        this(letter, c, x, y, size);
        rowPrompt = rP;
        colPrompt = cP;
        clueNumV = clueV;
        clueNumH = clueH;
    }

    /**
     * Accessor for the rowPrompt String
     * @return - the prompt for the whole row
     */
    public String getRowPrompt() {
        return rowPrompt;
    }
    /**
     * Accessor for the colPrompt String
     * @return - the prompt for the whole column
     */
    public String getColPrompt() {
        return colPrompt;
    }
    
    /**
     * Accessor for the vertical clue number integer
     * @return - the clue number
     */
    public int getClueNumV(){
        return clueNumV;
    }
    /**
     * Accessor for the horizontal clue number integer
     * @return - the clue number
     */
    public int getClueNumH(){
        return clueNumH;
    }
       
    /**
     * Mutator for the row prompt
     * @param rowPrompt - the new row prompt
     */
    public void setRowPrompt(String rowPrompt) {
        this.rowPrompt = rowPrompt;
    }
    /**
     * Mutator for the column prompt
     * @param colPrompt - the new column prompt
     */
    public void setColPrompt(String colPrompt) {
        this.colPrompt = colPrompt;
    }
    
    /**
     * Mutator for the vertical clue number
     * @param num - the new clue number being set to
     */
    public void setClueNumV(int num){
        clueNumV = num;
    }
    /**
     * Mutator for the horizontal clue number
     * @param num - the new clue number being set to
     */
    public void setClueNumH(int num){
        clueNumH = num;
    }
    /**
     * Create a complete replica of this tile
     * @return - an exact replica of this tile
     */
    @Override
    public CrosswordTile clone(){
        CrosswordTile dolly = new CrosswordTile(letter, color, x, y, size, rowPrompt, colPrompt, clueNumV, clueNumH);
        return dolly;
    }
    
    /**
     * Check if two tiles are the same by comparing if the prompts are the same
     * @return - true if the tiles are the same, and false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CrosswordTile other = (CrosswordTile) obj;
        
        if (!Objects.equals(this.rowPrompt, other.rowPrompt)) {
            return false;
        }
        return Objects.equals(this.colPrompt, other.colPrompt);
    }

    
    /**
     * Create a string representation of the CrosswordTile
     * @return - a String representation
     */
    @Override
    public String toString() {
        return super.toString()+"\nCrosswordTile{" + "rowPrompt=" + rowPrompt + ", colPrompt=" + colPrompt + ", clueNumV=" + clueNumV + ", clueNumH=" + clueNumH + '}';
    }
    
}

/*
Jeremy Wong
Janurary 13, 2025
Connections Tile Class
*/

//import different things that are required to fill out the properties of the connection tile
import java.awt.Color;
import java.util.Objects;

public class ConnectionsTile extends AbstractTile {
    //this is the variable for the solution number
    private int sol;
    //variable for twhether the tile is selected or not
    private boolean isSelected;
    //variable for whcih category the tile belongs to
    private String category;
    //variable for what color the tile will be
    Color color;
    

    /**
     * constructor for if only a words is being placed into the connections tile
     * @param letter - name of the tile
     */
    public ConnectionsTile(String letter) {
        super(letter);
    }
    /**
     * constructor that is used in the tileListPre that only includes some categories
     * @param letter - the name of the word
     * @param sol - the solution number
     * @param category - the category name
     */
    public ConnectionsTile(String letter, int sol, String category) {
        // chain with the previous constructor
        this(letter);
        //sets the solution to the solution number and the category to the category given when the tile is made
        this.sol = sol;
        this.category = category;
    }
    /**
     * 
     * @param letter - the name of the word
     * @param x - x location of tile
     * @param y - y location of the tile
     * @param sol - the solution number
     * @param category - the category name
     * @param color - color of the tile
     */
    public ConnectionsTile(String letter, int x, int y, int sol, String category, Color color) {
        // chain with the previous constructor
        this(letter,sol,category);
        //assign values for x, y and color variables
        this.x = x;
        this.y = y;
        this.color = color;
        //set the tile to be unselected as a default
        isSelected = false;
    }
    /**
     * getter for x variable
     * @return - the x location
     */
    public int getX() {
        return x;
    }
    /**
     * set the x value
     * @param newX - new x value to be assigned
     */
    public void setX(int newX) {
        x = newX;
    }
    /**
     * getter for y variable
     * @return - the y location
     */
    public int getY() {
        return y;
    }
    /**
     * set the y value
     * @param newY - new y value to be assigned
     */
    public void setY(int newY) {
        y = newY;
    }
    /**
     * get a boolean for whether the tile is selected
     * @return - true or false for whether the tile is selected
     */
    public boolean getSelected() {
        return isSelected;
    }
    /**
     * set the variable to true or false
     * @param selected - new value of isSeelcted variable
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    /**
     * getter for the word 
     * @return - the String word
     */
    public String getWord() {
        return letter;
    }
    /**
     * setter for the word variable
     * @param letter - the new word being stored
     */
    public void setWord(String letter){
        this.letter=letter;
    }
    /**
     * getter for the Color
     * @return - the color of the tile
     */
    @Override
    public Color getColor() {
        return color;
    }
    /**
     * setter for the color of the tile
     * @param c - the new color being set to
     */
    @Override
    public void setColor(Color c) {
        color = c;
    }
    /**
     * getter for the solution number
     * @return - the solution number
     */
    public int getSol() {
        return sol;
    }
    /**
     * setter for the solution number
     * @param newSol - the new solution number
     */
    public void setSol(int newSol){
        sol=newSol;
    }
    /**
     * getter for the category of the tile
     * @return - the tile's category String
     */
    public String getCategory() {
        return category;
    }
    /**
     * setter for the category of the tile
     * @param newCategory - new category of the tile
     */
    public void setCategory(String newCategory){
        category=newCategory;
    }
    /**
     * A toString method for the attributes of the tile
     * @return - a String representation
     */
    @Override
    public String toString() {
        return "Word: "+letter+
                "\nX Location: "+x+
                "\nY Location: "+y+
                "\nSolution Number: "+sol+
                "\nCategory: "+category+
                "\nTile Color:"+color;
    }
    
    ///-------------------------------------------------------------------------
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
        final ConnectionsTile other = (ConnectionsTile) obj;
        if (this.sol != other.sol) {
            return false;
        }
        if (this.isSelected != other.isSelected) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        return Objects.equals(this.color, other.color);
    }
    /**
     * clone function that clones a tile
     * @return - the identical tile that was passed to this method
     */
    public ConnectionsTile clone(ConnectionsTile n) {
        ConnectionsTile c = new ConnectionsTile(n.getWord(), n.getX(), n.getY(), n.getSol(), n.getCategory(), n.getColor());
        return c;
    }
}

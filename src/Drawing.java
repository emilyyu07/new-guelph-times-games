/*
Jeremy Wong
January 13, 2025
Drawing Panel for Connections Game
 */

//import all necessary packages to properly display the game
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JOptionPane;

public class Drawing extends JPanel implements MouseListener, Runnable {

    //create boolean variables that will be set to true when needed to draw something
    boolean drawSol1;
    boolean drawSol2;
    boolean drawSol3;
    boolean drawSol4;
    //create another tileList in this class
    private ArrayList<ConnectionsTile> tileList = new ArrayList();
    //animator to continuously refresh the screen
    private Thread animator;
    //variable that tracks how many boxes have been selected
    int totalSelected = 0;
    //variable that tracks how many correct the user has
    int totalCorrect = 0;
    //variable that tracks the amount of lives the user has
    int totalLives = 2;
    //boolean that sets both the game lost and won variables to false
    boolean gameLost = false;
    boolean gameWon = false;

    /**
     * helps continuously refresh the screen and get the tileList from the other
     * class
     *
     * @param parent - the parent of the drawing class
     */
    public Drawing(FinalProjectConnectionClassHolder parent) {
        //chains to its super class
        super();
        //gets the tileList from Drawing's parent class
        tileList = parent.getTileList();
        //the following creates the mouse listener and tracks the mouse's movements
        this.addMouseListener(this);
        this.setFocusable(true);
        this.requestFocus();
    }

    /**
     * draws the window and the things on the JFrame
     *
     * @param g - graphics 2d
     */
    private void doDrawing(Graphics g) {
        //creates different colors
        Color yellow = new Color(255, 220, 108);
        Color green = new Color(168, 196, 92);
        Color blue = new Color(184, 196, 236);
        Color purple = new Color(192, 132, 196);
        //array for the different categories
        String[] solCategories = new String[4];
        //filled the array of category names into the array through the following for loop and if statements
        for (int i = 0; i < 16; i++) {
            //if the solution is 0 the 0 index category name will be the same as the 0 number solution
            if (tileList.get(i).getSol() == 0) {
                solCategories[0] = tileList.get(i).getCategory();
            } else if (tileList.get(i).getSol() == 1) {
                solCategories[1] = tileList.get(i).getCategory();
            } else if (tileList.get(i).getSol() == 2) {
                solCategories[2] = tileList.get(i).getCategory();
            } else if (tileList.get(i).getSol() == 3) {
                solCategories[3] = tileList.get(i).getCategory();
            }
        }
        //the Graphics2D class is the class that handles all the drawing
        //must be casted from older Graphics class in order to have access to some newer methods
        Graphics2D g2d = (Graphics2D) g;
        //draw a string on the panel    
        g2d.drawString("Create four groups of four", 218, 50); //(text, x, y)
        //draw the string for the lives the user has left
        g2d.drawString("Lives: " + totalLives, 25, 750);
        //set the background to white
        setBackground(Color.white);
        //create a color
        Color grey = new Color(240, 236, 228);
        //set color to grey
        g2d.setColor(grey);
        //creates submit button tile
        g2d.fillRect(250, 485, 100, 75);
        //draws all the tiles
        for (int i = 0; i < 16; i++) {
            g2d.setColor(tileList.get(i).getColor());
            g2d.fillRect(tileList.get(i).getX(), tileList.get(i).getY(), 100, 75);
        }
        //sets color to black
        g2d.setColor(Color.black);
        //creates the font that will be used to type the words out
        Font font = new Font("Arial", Font.PLAIN, 12);
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        //draws all the names of the words on the tiles through a for loop for each word
        for (int i = 0; i < 16; i++) {
            // Determine the X coordinate for the text so it is centered
            int xOfWord = tileList.get(i).getX() + (100 - metrics.stringWidth(tileList.get(i).getWord())) / 2;
            // Determine the Y coordinate for the text so it is centered
            int yOfWord = tileList.get(i).getY() + ((75 - metrics.getHeight()) / 2) + metrics.getAscent();
            //draws the actual String using the x and y coordinate being centered
            g2d.drawString(tileList.get(i).getWord(), xOfWord, yOfWord);
        }
        //writes the word submit over the submit button
        g2d.drawString("Submit", 278, 526);
        //drawings when either the game ends or the user guesses the correct categories
        //if any of these booleans become true, the category name will be revealed in a corresponding color to the change in color to the boxes
        if (drawSol1) {
            //set color to yellow
            g2d.setColor(yellow);
            ///draw the name of category
            g2d.drawString(solCategories[0], 350, 600);
        }
        //same for the next three as above
        if (drawSol2) {
            g2d.setColor(green);
            g2d.drawString(solCategories[1], 350, 650);
        }
        if (drawSol3) {
            g2d.setColor(blue);
            g2d.drawString(solCategories[2], 50, 600);
        }
        if (drawSol4) {
            g2d.setColor(purple);
            g2d.drawString(solCategories[3], 50, 650);
        }
        //if the game lost variable becomes true this runs
        if (gameLost) {
            //color set to black
            g2d.setColor(Color.black);
            //tells the user they lose through string on the screen
            g2d.drawString("You are out of lives, better luck next time, close this window to return to main menu", 20, 700);
        }
        //if the gameWon variable becomes true this runs
        if (gameWon) {
            //set color to black
            g2d.setColor(Color.black);
            //tells the user they won and the amount of mistakes they made
            g2d.drawString("Congratulations you win, close this window to return to main menu, you made " + (2 - totalLives) + " mistake(s)", 20, 700);
        }
    }

    /**
     * overrides paintComponent in JPanel class performs custom painting
     *
     * @param g the graphics variable
     */
    @Override
    public void paintComponent(Graphics g) {
        //does the necessary work to prepare the panel for drawing
        super.paintComponent(g);
        //invoke the custom drawing method
        doDrawing(g);
    }

    /**
     * //this method is called only once, when the Thread starts, it is
     * responsible for the refreshing of the screen
     */
    @Override
    public void run() {
        //repaints or refreshes the drawing and screen
        while (true) {
            repaint();
        }
    }

    /**
     *
     * @param e - variable that stores the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        //import colors
        Color grey = new Color(240, 236, 228);
        Color darkGrey = new Color(90, 89, 78);
        //variable that checks what the result of the user hitting submit is
        String output = "";
        //gets the mouse's location when pressed
        int xLoc = e.getX();
        int yLoc = e.getY();
        //if the mouse is right clicked
        //this for loop will run thorugh all the tile's and see which of the x,y value ranges the mouse has been selected
        for (int i = 0; i < 16; i++) {
            int xRange = tileList.get(i).getX();
            int yRange = tileList.get(i).getY();
            //if it is in a specific range, the user has not selected more then 4 boxes, the box has not been already selected and 
            //the user has not run out of lives, this runs
            if (xLoc > xRange && xLoc < xRange + 100 && yLoc > yRange && yLoc < yRange + 75 && totalSelected < 4 && tileList.get(i).getSelected() == false && totalLives > 0 && totalCorrect < 4) {
                //sets the tile property of selected to true
                tileList.get(i).setSelected(true);
                //changes the color of the tile to a darker grey
                tileList.get(i).setColor(darkGrey);
                //add to the total number of tiles selected
                totalSelected++;
                //if the user's live is 0
            } else if (totalLives <= 0) {
                //this runs the lost game method
                lostGame();
                //if the user has selected a valid x,y range but, the tile has already been selected then this runs
            } else if (xLoc > xRange && xLoc < xRange + 100 && yLoc > yRange && yLoc < yRange + 75 && tileList.get(i).getSelected() == true && totalCorrect < 4) {
                //this means that the user wants to deselect a current tile
                //sets the tile's selected status to false
                tileList.get(i).setSelected(false);
                //sets the color back to the original grey
                tileList.get(i).setColor(grey);
                //subtracts one from the total selected page
                totalSelected = totalSelected - 1;
            }
        }
        //if the submit block is selected and the user has more than 0 lives
        if (xLoc > 250 && xLoc < 350 && yLoc > 485 && yLoc < 560 && totalLives > 0 && totalCorrect < 4) {
            //creates variable for solution number
            int sol = 0;
            //creates an array list for the chosenBoxes
            ArrayList<ConnectionsTile> chosenBoxes = new ArrayList();
            //if the there are 4 boxes selected this runs
            if (totalSelected == 4) {
                //finds the selected boxes through a for loop
                for (int i = 0; i < 16; i++) {
                    //if it is selected
                    if (tileList.get(i).getSelected() == true) {
                        //the tile is added to the chosenBoxes array list
                        chosenBoxes.add(tileList.get(i));
                    }
                }
                //variable output is the return from the checkCorrect method
                output = checkCorrect(chosenBoxes);
            }
            //if total selected is less than 4 then this runs
            if (totalSelected < 4) {
                //it is a joption pane that tells the user to select four boxes
                JOptionPane.showMessageDialog(null, "Please select four boxes");
                //if the return from the check correct is correct this runs
            } else if (output.equals("Correct")) {
                //it gets the solution number that the user has gotten correctly
                sol = chosenBoxes.get(0).getSol();
                //this enters the display correct method and instructs it on which solution to reveal
                displayCorrect(sol);
                //resets the total selected to 0
                totalSelected = 0;
                //adds one to the totalCorrect variable
                totalCorrect++;
                //sets all the tiles selected features back to false
                for (int i = 0; i < 16; i++) {
                    tileList.get(i).setSelected(false);
                }
                //tells the user they are correct via joptionpane
                JOptionPane.showMessageDialog(null, "Correct");
                //if the user is one away
            } else if (output.equals("One Away")) {
                //tells the user is one away
                JOptionPane.showMessageDialog(null, "One Away");
                //deducts from the life amount
                totalLives--;
                //if the user gets more than 1 wrong in the guess this runs
            } else {
                //the user is told they got it wrong
                JOptionPane.showMessageDialog(null, "Wrong");
                //total life is subtracted by one
                totalLives--;
            }
        }
        //this runs if the amount of correct categories the user has guessed is all 4 of them
        if (totalCorrect == 4) {
            //gameWon is set to true and the user will be told they won
            gameWon = true;
            //if the user has no lives then this runs
        } else if (totalLives <= 0) {
            //the lostGame method will run
            lostGame();
        }

    }

    /**
     * method that is run once the user has run out of lives
     */
    public void lostGame() {
        //goes through all the categories and displays all the correct boxes
        for (int i = 0; i < 4; i++) {
            displayCorrect(i);
        }
        //sets boolean for when the game is lost to true
        gameLost = true;
    }

    /**
     * displays the correct solutions by color coding boxes and categories on
     * screen
     *
     * @param solNum - this is the solution set that will be displayed as
     * correct
     */
    public void displayCorrect(int solNum) {
        //create the colors for all the color coding
        Color yellow = new Color(255, 220, 108);
        Color green = new Color(168, 196, 92);
        Color blue = new Color(184, 196, 236);
        Color purple = new Color(192, 132, 196);
        //the folowing is identical except they are for each of the solution sets
        //if the solution desired is 0 this runs
        if (solNum == 0) {
            //draws sol1 which will display the correct category name in a specific color
            drawSol1 = true;
            //goes through all the tiles, finds the ones with the same solution number
            for (int i = 0; i < 16; i++) {
                if (tileList.get(i).getSol() == 0) {
                    //changes its box to match the color that the category name has been displayed in
                    tileList.get(i).setColor(yellow);
                }
            }
        } else if (solNum == 1) {
            drawSol2 = true;
            for (int i = 0; i < 16; i++) {
                if (tileList.get(i).getSol() == 1) {
                    tileList.get(i).setColor(green);
                }
            }
        } else if (solNum == 2) {
            drawSol3 = true;
            for (int i = 0; i < 16; i++) {
                if (tileList.get(i).getSol() == 2) {
                    tileList.get(i).setColor(blue);
                }
            }
        } else if (solNum == 3) {
            drawSol4 = true;
            for (int i = 0; i < 16; i++) {
                if (tileList.get(i).getSol() == 3) {
                    tileList.get(i).setColor(purple);
                }
            }
        }
    }

    /**
     * this method is called after the JPanel is added to the JFrame we can
     * perform start up tasks here
     */
    @Override
    public void addNotify() {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    /**
     * this checks whether the boxes the user has selected are correct
     *
     * @param chosenBoxes - the array list with the four chosen boxes the user
     * has selected
     * @return - returns a string of whether it was correct or not
     */
    public String checkCorrect(ArrayList<ConnectionsTile> chosenBoxes) {
        //variable for the return string
        String out;
        //gets the solution number from each of the selected tiles
        int sol1 = chosenBoxes.get(0).getSol();
        int sol2 = chosenBoxes.get(1).getSol();
        int sol3 = chosenBoxes.get(2).getSol();
        int sol4 = chosenBoxes.get(3).getSol();
        //if all four are correct this runs
        if (sol1 == sol2 && sol2 == sol3 && sol3 == sol4) {
            //sets the return variable to the string correct
            out = "Correct";
            //if only three are correct this runs
        } else if ((sol1 == sol2 && sol1 == sol3) || (sol1 == sol2 && sol1 == sol4) || (sol1 == sol3 && sol1 == sol4) || (sol2 == sol3 && sol2 == sol4)) {
            //sets the return variable to the string one away
            out = "One Away";
            //if neither of these are true that means the user has gotten it wrong
        } else {
            //sets the return variable to the string incorrect
            out = "Incorrect";
        }
        //returns string
        return out;
    }

    //the methods below are required by the MouseListener interface, but will not be needed in this program
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

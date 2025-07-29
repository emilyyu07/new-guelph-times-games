/*
Emily Yu
December 19, 2024
Wordle Game Panel (drawing surface for Graphics2D)
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class WordleGamePanel extends JPanel {

    //keyboard rows
    private WordleTile[] topRowKeyboard = new WordleTile[10];
    private WordleTile[] midRowKeyboard = new WordleTile[9];
    private WordleTile[] bottomRowKeyboard = new WordleTile[7];

    //Wordle game (handles game logic)
    private WordleGame game;

    //Wordle game grid
    private WordleTile[][] grid;

    //keep track of user's current input/guess (as they type)
    private ArrayList<String> input = new ArrayList<String>();

    //tiles will be seperated by 15 pixels from one another
    private int space = 10;

    //wordle stats object (holds user's current stats)
    private WordleStats stats;

    /**
     * Constructor for the drawing panel
     *
     * @param game - the Wordle game
     */
    public WordleGamePanel(WordleGame game) {
        //connects the game logic to the visuals 
        this.game = game;
        grid = game.getGrid();
        stats = game.getStats();

        //obtain keyboard arrays
        topRowKeyboard = game.getTopRow();
        midRowKeyboard = game.getMidRow();
        bottomRowKeyboard = game.getBottomRow();

        //set default background colour
        setBackground(Color.WHITE);

        //ensure panel has focus when keys are pressed
        setFocusable(true);
        requestFocusInWindow();

        //PHYSICAL KEYBOARD
        addKeyListener(new KeyAdapter() {
            //detects when a key has been pressed on the keyboard
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                //if backspace key is pressed
                if (keyCode == KeyEvent.VK_BACK_SPACE) {
                    removeLetter();
                    //if a LETTER on the keyboard is pressed
                } else if (Character.isLetter(e.getKeyChar())) {
                    addLetter(e.getKeyChar());
                    //if the ENTER key is pressed
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    //check the user's guess
                    verifyGuess();
                }
            }
        }
        );

        //ONSCREEN KEYBOARD
        addMouseListener(new MouseAdapter() {
            //detects when a letter is clicked on the on-screen keyboard
            @Override
            public void mouseClicked(MouseEvent e) {
                //obtain x and y position of mouse to handle the click 
                //only allow the user to access the keyboard if they haven't won yet
                if (game.isWon() != true) {
                    //handle any on screen letters that are clicked
                    handleKeyboardClick(e.getX(), e.getY());
                    //handle when onscreen backspace is clicked
                    handleOnScreenBackspace(e.getX(), e.getY());
                    //handle when onscreen enter key to clicked
                    handleOnScreenEnter(e.getX(), e.getY());
                }

            }

        });

    }

    
    
    /**
     * Method handles user's mouse clicks on the on-screen keyboard
     *
     * @param mouseX - x position of mouse click
     * @param mouseY - y position of mouse click
     */
    private void handleKeyboardClick(int mouseX, int mouseY) {
        //find the WordleTile in the keyboard that was clicked on
        WordleTile clickedLetter = getClickedLetter(mouseX, mouseY);

        //if a letter was actually clicked
        if (clickedLetter != null) {
            //obtain the letter that was clicked from its WordleTile
            String letter = clickedLetter.getLetter();
            if (!letter.isEmpty()) {
                //Add the letter to the current input on the grid
                addLetter(letter.charAt(0));
            }
        }
    }

    /**
     * Method checks which keyboard tile was clicked based on mouse coordinates
     *
     * @param mouseX - x position of mouse click
     * @param mouseY - y position of mouse click
     * @return - the clicked tile (WordleTile)
     */
    private WordleTile getClickedLetter(int mouseX, int mouseY) {
        //Check which letter in top row was clicked
        WordleTile clickedLetter = findLetterInRow(topRowKeyboard, 37, 470, mouseX, mouseY);
        if (clickedLetter != null) {
            return clickedLetter;
        }

        //Check which letter in middle row was clicked (if not in top row)
        clickedLetter = findLetterInRow(midRowKeyboard, 62, 517, mouseX, mouseY);
        if (clickedLetter != null) {
            return clickedLetter;
        }

        //Check with letter in bottom row was clicked (if not in middle row)
        clickedLetter = findLetterInRow(bottomRowKeyboard, 110, 565, mouseX, mouseY);

        //return the particular letter tile
        return clickedLetter;

    }

    /**
     * Method checks which letter of a particular row was clicked
     *
     * @param keyboardRow - the WordleTile keyboard row (either top, mid, or
     * bottom)
     * @param rowX - x position of the leftmost tile in the row
     * @param rowY - y position of the row
     * @param mouseX - x position of the mouse click
     * @param mouseY - y position of the mouse click
     * @return - the clicked WordleTile (or null if no letter was clicked at
     * all)
     */
    private WordleTile findLetterInRow(WordleTile[] keyboardRow, int rowX, int rowY, int mouseX, int mouseY) {
        //find tile size
        int tileSize = keyboardRow[0].getSize();

        //search through the row
        for (int i = 0; i < keyboardRow.length; i++) {
            //the leftmost side of each tile is (tileSize + space) "units" from the left side of the next tile
            int tileX = rowX + i * (tileSize + space);
            int tileY = rowY;

            //if the mouse position when clicked is within the boundaries of the tile
            if (mouseX >= tileX && mouseX <= tileX + tileSize
                    && mouseY >= tileY && mouseY <= tileY + tileSize) {
                //return the particular letter tile
                return keyboardRow[i];
            }
        }
        //if no letter was clicked
        return null;
    }

    /**
     * Method handles if user clicks on the on-screen backspace button
     *
     * @param mouseX - x position of mouse click
     * @param mouseY - y position of mouse click
     */
    public void handleOnScreenBackspace(int mouseX, int mouseY) {
        /*
        BACKSPACE button coordinates/dimensions:
        Top left corner X: 120
        Top left corner Y: 620
        Length: 169
        Width: 50
         */

        //if the mouse position when clicked is within the boundaries of the button
        if (mouseX >= 120 && mouseX <= (120 + 169)
                && mouseY >= 620 && mouseY <= (620 + 50)) {
            //remove the last letter as usual
            removeLetter();
        }
    }

    /**
     * Method handles if the user clicks on the on-screen enter button
     *
     * @param mouseX - x position of mouse click
     * @param mouseY - y position of mouse click
     */
    public void handleOnScreenEnter(int mouseX, int mouseY) {
        /*
        ENTER button coordinates/dimensions:
        Top left corner X: 310
        Top left corner Y: 620
        Length: 110
        Width: 50
         */

        //if the mouse position when clicked is within the boundaries of the button
        if (mouseX >= 310 && mouseX <= (310 + 110)
                && mouseY >= 620 && mouseY <= (620 + 50)) {
            //check the user's guess as usual
            verifyGuess();
        }
    }

    
    
    
    
    /**
     * Method checks the user's guess
     */
    public void verifyGuess() {
        //whether or not the user's guess is a valid length (5-letters)
        boolean validLength = game.checkLength();

        //whether or not the user's guess is in the word list
        boolean validWord = game.checkWordList();

        //if the guess is too short
        if (!validLength) {
            //display error message
            JOptionPane.showMessageDialog(null, "Not enough letters", "Invalid Guess", JOptionPane.ERROR_MESSAGE);

            //JOptionPane dialog interrupts the display, so the window must regain focus after
            requestFocusInWindow();

            return;

        } else if (!validWord) {
            //if the guess is not in our word list

            //display error message
            JOptionPane.showMessageDialog(null, "Not in word list", "Invalid Guess", JOptionPane.ERROR_MESSAGE);

            //regain window focus
            requestFocusInWindow();

            return;

        } else {
            //otherwise, the guess is valid and each letter can be evaluated (compared with the target word)

            //check the guess
            game.checkGuess();

            //clear the current input for the next guess
            input.clear();

            //redraw the screen
            repaint();

            //check whether or not the user correctly guessed the word
            boolean hasWon = game.checkWin();

            //will be set to the message displayed to the user
            String msg = "";

            if (hasWon) {
                //indicate that user has won (so the user can't access the onscreen keyboard anymore)
                game.setWon(true);

                //change the font size for the message
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 40));

                //display congratulatory message based on the number of tries the user took
                int numTries = game.getCurrentGuess();

                //update stats changes
                stats.increaseGamesWon();
                stats.increaseGamesPlayed();
                ArrayList<Integer> temp = stats.getGuessDistribution();
                temp.add(numTries);
                stats.setGuessDistribution(temp);

                //Set appropriate message according to user's efficiency in guessing the word
                if (numTries == 1) {
                    msg = "Genius!";
                } else if (numTries == 2) {
                    msg = "Magnificent!";
                } else if (numTries == 3) {
                    msg = "Impressive!";
                } else if (numTries == 4) {
                    msg = "Splendid!";
                } else if (numTries == 5) {
                    msg = "Great!";
                } else {
                    msg = "Phew!";
                }

                //display message
                JOptionPane.showMessageDialog(null, msg, "You got it!", JOptionPane.PLAIN_MESSAGE);

                /*
                when user wins, disable the panel's focus for when keys are 
                pressed so that the user cannot enter more guesses. From here,
                the program will no longer recognize the letters typed.
                 */
                setFocusable(false);
            } else if (game.getCurrentGuess() == 6) {
                //set "won" to true (so the user can't access the onscreen keyboard anymore)
                game.setWon(true);

                /*
                when game ends, disable the panel's focus for when keys are 
                pressed so that the user cannot enter more guesses. From here,
                the program will no longer recognize the letters typed.
                 */
                setFocusable(false);

                //update stats
                stats.increaseGamesPlayed();

                //change the font size for the message
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 20));

                //convert the target word (in WordleTiles) back into a String to display in message
                String targetWord = "";
                for (int i = 0; i < 5; i++) {
                    targetWord += game.getTarget()[i].getLetter();
                }
                msg = "Nice try! The Wordle was " + targetWord.toUpperCase();

                //display message
                JOptionPane.showMessageDialog(null, msg, "Maybe next time...", JOptionPane.PLAIN_MESSAGE);
            }
        }

    }

    /**
     * Method removes the last letter in the current input (due to backspace)
     */
    public void removeLetter() {
        //only if the user has already typed some letters
        if (!input.isEmpty()) {
            //remove the last letter in the current input
            input.remove(input.size() - 1);

            //update the grid with changes
            updateGrid();

            //repaint panel to update display
            repaint();
        }
    }

    /**
     * Method adds the letter typed by the user to the current input
     *
     * @param keyChar - the letter typed
     */
    public void addLetter(char keyChar) {
        //only if less than 5 letters have been typed
        if (input.size() < 5) {
            //Set to uppercase letters only (for aesthetics)
            String letter = String.valueOf(Character.toUpperCase(keyChar));

            //add this letter to the existing input
            input.add(letter);

            //update the game with the new input
            updateGrid();

            //repaint panel to update display
            repaint();
        }
    }

    /**
     * Method updates the WordleTile grid with the current input
     *
     * @param input - the letters that the user has currently typed
     */
    private void updateGrid() {
        //number of guesses attempted in the game
        int currentGuess = game.getCurrentGuess();

        int length;

        //set the length to the lower of the two values below:
        //# of characters the user currently entered or the max word length of 5
        if (input.size() < 5) {
            length = input.size();
        } else {
            length = 5;
        }

        //set the letter to the grid
        for (int i = 0; i < length; i++) {
            grid[currentGuess][i].setLetter(input.get(i));
        }

        //if the user currently entered less than 5 letters, fill the tiles with empty spaces 
        if (length < 5) {
            for (int i = length; i < 5; i++) {
                grid[currentGuess][i].setLetter("");
            }
        }

    }

    /**
     * Draws the grid of WordleTiles
     *
     * @param g - the Graphics object to draw with
     */
    private void drawGrid(Graphics g) {
        //the Graphics2D class is the class that handles all the drawing
        //must be casted from older Graphics class in order to have access to some newer methods
        Graphics2D g2d = (Graphics2D) g;

        //colour of outline
        Color lightGray = new Color(172, 174, 175);

        if (grid != null) {
            int tileSize = grid[0][0].getSize();

            //the top left corner of the grid has coordinates (initialX, initialY)
            int initialX = 115;
            int initialY = 50;

            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 5; col++) {
                    WordleTile currentTile = grid[row][col];

                    //determine current x and y pos of the tile
                    int currentX = initialX + col * (space + tileSize);
                    int currentY = initialY + row * (space + tileSize);

                    //adjust the width of the stroke (from default)
                    g2d.setStroke(new BasicStroke(2));

                    //ensures that the row the user is typing in "darkens"
                    if (currentTile.getLetter().equals(" ")) {
                        //set colour to light gray
                        g2d.setColor(lightGray);
                    } else {
                        //set colour to black
                        g2d.setColor(Color.BLACK);
                    }

                    //draw the outline for each tile
                    g2d.drawRect(currentX, currentY, tileSize, tileSize);

                    //create/set new font
                    Font font2 = new Font("Arial", Font.BOLD, tileSize - 17);
                    g2d.setFont(font2);

                    if (currentTile != null) {
                        //fill tile with designated colour
                        g2d.setColor(currentTile.getColor());
                        g2d.fillRect(currentX, currentY, tileSize, tileSize);

                        //set colour for letter 
                        if (currentTile.getColor() == Color.WHITE) {
                            g2d.setColor(Color.BLACK);
                        } else {
                            g2d.setColor(Color.WHITE);
                        }

                        String letter = currentTile.getLetter();
                        g2d.drawString(letter, currentX + (tileSize / 9) + 7, currentY + tileSize - 11);

                    }

                }
            }
        }

    }

    /**
     * Method draws the keyboard (consisting of WordleTiles)
     *
     * @param g - the Graphics object to draw with
     */
    private void drawKeyboard(Graphics g) {
        //the Graphics2D class is the class that handles all the drawing
        //must be casted from older Graphics class in order to have access to some newer methods
        Graphics2D g2d = (Graphics2D) g;

        //set standard tile and spacing size
        int tileSize = topRowKeyboard[0].getSize();
        space = 8;

        //create/set new font
        Font font3 = new Font("Arial", Font.BOLD, tileSize - 18);
        g2d.setFont(font3);

        //set colour
        Color lightGray = new Color(211, 214, 218);
        g2d.setColor(lightGray);

        //draw backspace and enter buttons
        g2d.fillRoundRect(120, 620, 169, tileSize, 18, 18);
        g2d.fillRoundRect(310, 620, 110, tileSize, 18, 18);
        g2d.setColor(Color.BLACK);
        g2d.drawString("BACKSPACE", 125 + (tileSize / 9) + 7, 620 + tileSize - 11);
        g2d.drawString("ENTER", 315 + (tileSize / 9) + 7, 620 + tileSize - 11);

        //coordinates for first tile in top row
        int initialX = 37;
        int initialY = 470;

        //draw top row of the keyboard
        drawRow(initialX, initialY, topRowKeyboard, tileSize, g2d);

        //coordinates for first tile in middle row
        initialX = 62;
        initialY = 517;

        //draw middle row of the keyboard
        drawRow(initialX, initialY, midRowKeyboard, tileSize, g2d);

        //coordinates for the first tile in the last row
        initialX = 110;
        initialY = 565;

        //draw the bottom row of the keyboard
        drawRow(initialX, initialY, bottomRowKeyboard, tileSize, g2d);

    }

    /**
     * Method draws out a row of WordleTiles (primarily used for keyboard)
     *
     * @param initialX - x position of the first tile in the row
     * @param initialY - y position of tiles in the row
     * @param keyboardRow - the specified row of the keyboard (array of
     * WordleTiles)
     * @param tileSize - size of the tile
     * @param g2d - Graphics2D object to draw with
     */
    public void drawRow(int initialX, int initialY, WordleTile[] keyboardRow, int tileSize, Graphics2D g2d) {
        Color lightGray = new Color(211, 214, 218);

        for (int i = 0; i < keyboardRow.length; i++) {
            WordleTile currentTile = keyboardRow[i];

            //determine the current position of the tile
            int currentX = initialX + i * (space + tileSize);

            //draw the tile
            g2d.setColor(currentTile.getColor());
            g2d.fillRoundRect(currentX, initialY, tileSize, tileSize, 18, 18);

            //set colour for letter (if tile is set to default gray, make text black, otherwise make text white)
            if (currentTile.getColor().equals(lightGray)) {
                g2d.setColor(Color.BLACK);
            } else {
                g2d.setColor(Color.WHITE);
            }

            //obtain letter from the tile
            String letter = currentTile.getLetter();

            //draw letter
            g2d.drawString(letter, currentX + (tileSize / 9) + 7, initialY + tileSize - 11);

        }

    }

    /**
     * Accessor for topRowKeyboard
     *
     * @return - WordleTile array of the top row of keyboard letters
     */
    public WordleTile[] getTopRowKeyboard() {
        return topRowKeyboard;
    }

    /**
     * Accessor for midRowKeyboard
     *
     * @return - WordleTile array of the middle row of keyboard letters
     */
    public WordleTile[] getMidRowKeyboard() {
        return midRowKeyboard;
    }

    /**
     * Accessor for bottomRowKeyboard
     *
     * @return - WordleTile array of the bottom row of keyboard letters
     */
    public WordleTile[] getBottomRowKeyboard() {
        return bottomRowKeyboard;
    }

    /**
     * Accessor for game
     *
     * @return - WordleGame object
     */
    public WordleGame getGame() {
        return game;
    }

    /**
     * Accessor for grid
     *
     * @return - grid of WordleTiles
     */
    public WordleTile[][] getGrid() {
        return grid;
    }

    /**
     * Accessor for user's current input
     *
     * @return - list of characters user has currently entered
     */
    public ArrayList<String> getInput() {
        return input;
    }

    /**
     * Accessor for space
     *
     * @return - space in between tiles
     */
    public int getSpace() {
        return space;
    }

    /**
     * Mutator for topRowKeyboard
     *
     * @param topRowKeyboard - WordleTile array of the top row of keyboard
     * letters
     */
    public void setTopRowKeyboard(WordleTile[] topRowKeyboard) {
        this.topRowKeyboard = topRowKeyboard;
    }

    /**
     * Mutator for midRowKeyboard
     *
     * @param midRowKeyboard - WordleTile array of the middle row of keyboard
     * letters
     */
    public void setMidRowKeyboard(WordleTile[] midRowKeyboard) {
        this.midRowKeyboard = midRowKeyboard;
    }

    /**
     * Mutator for bottomRowKeyboard
     *
     * @param bottomRowKeyboard - WordleTile array of the bottom row of keyboard
     * letters
     */
    public void setBottomRowKeyboard(WordleTile[] bottomRowKeyboard) {
        this.bottomRowKeyboard = bottomRowKeyboard;
    }

    /**
     * Mutator for game
     *
     * @param game - WordleGame object
     */
    public void setGame(WordleGame game) {
        this.game = game;
    }

    /**
     * Mutator for grid
     *
     * @param grid - grid of WordleTiles
     */
    public void setGrid(WordleTile[][] grid) {
        this.grid = grid;
    }

    /**
     * Mutator for input
     *
     * @param input - list of user's current character input
     */
    public void setInput(ArrayList<String> input) {
        this.input = input;
    }

    /**
     * Mutator for space
     *
     * @param space - spacing between tiles
     */
    public void setSpace(int space) {
        this.space = space;
    }

    /**
     * Accessor for stats
     *
     * @return - stats object for this wordle game
     */
    public WordleStats getStats() {
        return stats;
    }

    /**
     * Mutator for stats
     *
     * @param stats - stats object for this wordle game
     */
    public void setStats(WordleStats stats) {
        this.stats = stats;
    }

    /**
     * Displays contents of the panel
     * @return - message regarding panel state
     */
    @Override
    public String toString() {
        return "WordleGamePanel{" + "topRowKeyboard=" + topRowKeyboard + ", midRowKeyboard=" + midRowKeyboard + ", bottomRowKeyboard=" + bottomRowKeyboard + ", game=" + game + ", grid=" + grid + ", input=" + input + ", space=" + space + ", stats=" + stats + '}';
    }
    
    

    /**
     * Overrides paintComponent in JPanel class so that we can do our own custom
     * painting
     */
    public void paintComponent(Graphics g) {
        //prepare panel for drawing
        super.paintComponent(g);
        //draw the 5x6 WordleTile grid
        drawGrid(g);
        //draw the keyboard
        drawKeyboard(g);
    }

}

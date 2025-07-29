/**
 * Bob Zhao
 * January 14, 2025
 * Mini Crossword Window
 */


import java.awt.*;
import javax.swing.JOptionPane;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

public class MiniCrosswordFrame extends JFrame implements KeyListener, MouseListener, ActionListener{
    
    private boolean won = false;
    // create a global variable b for the 2d crosswordtile board
    CrosswordTile[][] b;
    
    // the selected tile's coordinates
    private int x = 0; 
    private int y = 0;
    // the miniSize of the square board (usually either 5 or 7)
    private int miniSize;
    // custom colors used
    private final Color PALEBLUE = new Color(173,216,230);
    private final Color PALEYELLOW = new Color(230, 223, 176);
    
    // time-related variables that change every second with the ActionListener
    private int elapsed = 0;
    private int secs = 0;
    private int mins = 0;
    private String secondString; //a format that adds 0 in front of secs when secs < 10

    
    
    // switch for the whether the selector in the puzzle is vertical or sideways 
    boolean across = true;
    // checks whether the board is filled with guesses
    boolean filled = true;
    

    /**
     * Primary Constructor
     * @param b - the CrosswordTile[][] (2d array) that read the data of the puzzle
     */
    MiniCrosswordFrame(CrosswordTile[][] b){
        
        // Create an int[] ArrayList for the coordinates that are blanks 
        // there could be 0 or more
        ArrayList<int[]> blackList = new ArrayList<>();
        // An int[] ArrayList for the coordinates that have the vertical 
        // clue numbers in the corners
        ArrayList<int[]> startListD = new ArrayList<>();
        // Coordinates that have the horizontal clue numbers in the corners
        ArrayList<int[]> startListA = new ArrayList<>();
        
        
        // assign the board (2d array) to the global variable
        this.b = b;
        // the size of the board is written on the first tile in b
        // the length and width of the board (mostly 5 or 7)
        miniSize = b[0][0].getSize();
        // a JLabel array for the vertical clues on the panel right of the UI
        // the number of vertical clues is written on the first tile's YPos
        JLabel[] vClues = new JLabel[b[0][0].getYPos()];
        // a JLabel array for the horizontal clues on the panel right of the UI
        // the number of horizontal clues is written on the first tile's XPos
        JLabel[] hClues = new JLabel[b[0][0].getXPos()];
        // instantiate a grid of labels for the main crossword grid
        JLabel[][] grid = new JLabel[miniSize][miniSize];
        // instantiate another 2d array of labels for the clue number in each square
        JLabel[][] clueNums = new JLabel[miniSize][miniSize];
        
        // int array that stores the coordinates of the edges of the main grid
        int[] gridCoords = new int[miniSize+1];
        // int arrays that store the coordinates of the top and bottom edges of the clue labels on the right
        int[] hPromptCoords = new int[b[0][0].getXPos()+1]; // ACROSS hints
        int[] vPromptCoords = new int[b[0][0].getYPos()+1]; // DOWN hints
        
        // loop through the gridCoords
        for (int i = 0; i <= miniSize; i++) {
            // using the miniSize scale to distribute the coordinates
            gridCoords[i] = i*(500/miniSize);
        }
        // loop through hPromptCoords and vPromptCoords
        for (int i = 0; i <= hClues.length; i++) {
            // 50 is spacing from top, adding spacing based on # of across clues
            hPromptCoords[i] = 50+i*(250/hClues.length); 
        }
        for (int i = 0; i <= vClues.length; i++) {
            // 350 is spacing from top, adding spacing based on # of down clues
            vPromptCoords[i] = 350 + i*(250/vClues.length);
        }
        // Create the JFrame values
        this.setTitle("Mini Crossword");
        // ensures when tab is closed, GUI is not
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1400,800);
        this.setLocationRelativeTo(null); // makes popup come up in the center
        this.setLayout(null); // allows for resizable labels
        // Set values for the JPanel that holds the grid
        JPanel board = new JPanel();
        board.setBounds(200,200,500,500);
        board.setLayout(null);// allows for resizable labels
        
        // Set values for the JPanel that holds the clues
        JPanel cluePanel = new JPanel();
        cluePanel.setBounds(750,100,600,600);
        cluePanel.setLayout(null);// allows for resizable labels
        
        // write some headers for the cluePanel
        Font headerFont = new Font("Times New Roman", Font.BOLD, 24); //header font
        Font clueFont = new Font("Times New Roman", Font.PLAIN, 20); // clue font
        JLabel acrossTitle = new JLabel("ACROSS");
        acrossTitle.setBounds(0,0,600, 45); // 0,0 relative to cluePanel
        acrossTitle.setOpaque(true); 
        acrossTitle.setFont(headerFont); 
        JLabel downTitle = new JLabel("DOWN");
        downTitle.setBounds(0,300,600, 45); // set vertically halfway 
        downTitle.setOpaque(true);
        downTitle.setFont(headerFont);
        cluePanel.add(acrossTitle);
        cluePanel.add(downTitle);
        
        // create borders for the crossword game 
        Border blackLine = new LineBorder(Color.BLACK); // black border
        // create a margin for the letters and hint scaled to the miniSize
        Border margin = new EmptyBorder(50/miniSize,50/miniSize, 0, 0);
        Border border = new CompoundBorder(blackLine, margin); // merged border
        
        // create a label for the hint above the puzzle
        JLabel hint = new JLabel();
        hint.setBounds(200, 100, 500, 90);
        hint.setBackground(PALEBLUE);
        hint.setOpaque(true);
        Font hintFont = new Font("Times New Roman",Font.BOLD, 24);
        hint.setFont(hintFont);
        hint.setBorder(margin);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(hint);
        // create increments for the vertical and horizontal clues
        int k = 0;
        int l = 0;
        // loop through the board
        for (int i = 0; i < miniSize; i++) {
            for (int j = 0; j < miniSize; j++) {
                // create two fonts both scaled to the miniSize 
                Font puzzleFont = new Font("Times New Roman", Font.PLAIN, 375/miniSize); // letters user types
                Font clueNumFont = new Font("Times New Roman", Font.BOLD, 90/miniSize); // superscripts above start of clues
                // create a new label for each square of the board
                grid[i][j] = new JLabel();
                grid[i][j].setBounds(j*(500/miniSize),i*(500/miniSize),(500/miniSize), (500/miniSize)); // scale it
                grid[i][j].setOpaque(true);
                grid[i][j].setBorder(border); // give black lines and margin
                // if we come across a "/" read from b, make a black square 
                if (b[i][j].getLetter().equals("/")) {
                    grid[i][j].setBackground(Color.BLACK); // set the background
                    b[i][j].setColor(Color.BLACK);// set the b color attribute
                    grid[i][j].setText("/"); // set the grid text so that it isn't empty
                    // add the i,j, coordinates to the black list ArrayList
                    int[] coords = {i,j};
                    blackList.add(coords);
                } else{ // it's not an omitted square
                    grid[i][j].setBackground(Color.WHITE);
                    b[i][j].setColor(Color.WHITE);
                    grid[i][j].setFont(puzzleFont);
                }
                
                //a new clueNumber (top left) for each square in the grid
                clueNums[i][j] = new JLabel();
                clueNums[i][j].setBounds(25/miniSize+j*(500/miniSize),25/miniSize+ i*(500/miniSize),125/miniSize, 75/miniSize); // scaled
                clueNums[i][j].setOpaque(true);
                clueNums[i][j].setFont(clueNumFont);
                // if the color of the tile is not black/square is not omitted
                if (!b[i][j].getColor().equals(Color.BLACK)) {
                    // set the background of the superscript to white
                    clueNums[i][j].setBackground(Color.WHITE);
                    // all vertical clue numbers that are not the start of the 
                    // clue have a clue number 0, find the ones that are not 
                    // zero. Linear searching from the top going right and down
                    if (b[i][j].getClueNumV() != 0) {
                        //if found set the cluenumber at that i,j to the b[i][j]
                        clueNums[i][j].setText(""+b[i][j].getClueNumV());
                        // store that coordinate in the ArrayList for the starting down clues 
                        int[] clueCoord = {i,j};
                        startListD.add(clueCoord);
                        
                        // k is a counter for each starting vertical clue number
                        // create a new label for the clue board to the right
                        // replace the D meaning "down" with a space
                        // make sure to use html so it fits in the label
                        vClues[k] = new JLabel("<html><p>"+b[i][j].getColPrompt().replaceFirst("D", " ")+"</p></html>");
                        vClues[k].setBounds(10, 350+(250/vClues.length)*k, 590, 250/vClues.length);
                        vClues[k].setOpaque(true);
                        vClues[k].setFont(clueFont);
                        
                        cluePanel.add(vClues[k]);
                        k++; // increment the k counter
                        
                    }
                    // all horizontal clue numbers that are not the start of the 
                    // clue have a clue number 0, find the ones that are not 
                    // zero. Linear searching from the top going right and down
                    if (b[i][j].getClueNumH() != 0) {
                        //if found set the cluenumber at that i,j to the b[i][j]
                        clueNums[i][j].setText(""+b[i][j].getClueNumH());
                        // store that coordinate in the ArrayList for the starting across clues
                        int[] clueCoord = {i,j};
                        startListA.add(clueCoord);
                        
                        // l is a counter for each starting horizontal clue number
                        // create a new label for the clue board to the right
                        // replace the A meaning "across" with a space
                        // use html so it fits in the label
                        hClues[l] = new JLabel("<html><p>"+b[i][j].getRowPrompt().replaceFirst("A", " ")+"</p></html>");
                        hClues[l].setBounds(10, 50+(250/hClues.length)*l, 590, 250/hClues.length);
                        hClues[l].setOpaque(true);
                        hClues[l].setFont(clueFont);
                        cluePanel.add(hClues[l]); 
                        l++; // increment the l counter
                    }
                    
                }else{ // if the square is a black square
                    // set the cluenumber to the same background
                    clueNums[i][j].setBackground(Color.BLACK);
                }
                // add bot the grid and the clue number to the board panel
                board.add(clueNums[i][j]);
                board.add(grid[i][j]);
                
                
                
            }
        }
        // add a MouseListener that tracks when the mouse is pressed on the puzzle
        board.addMouseListener(new MouseAdapter(){
            
            @Override
            /**
             * MousePressed abstract method that detects where the user first
             * clicks and makes that label yellow, while recording the 
             * coordinates relative to the grid
             */
            public void mousePressed(MouseEvent e){
                
                // two for loops to get every spot in the grid
                for (int i = 0; i < miniSize; i++) {
                        for (int j = 0; j < miniSize; j++) {
                            // if the square color is not black/omitted
                            if (!b[i][j].getColor().equals(Color.BLACK)) {
                                // if the cursor is within the corrdinates of 
                                // the grid coordinates i, i+1 and j, j+1
                                // basically pinpointing the cursor to a box
                                if (e.getX() < gridCoords[j+1] && e.getX() > gridCoords[j] && 
                                    e.getY() < gridCoords[i+1] && e.getY() > gridCoords[i]) {
                                    // double clicking a square swaps the direction of the selector
                                    // if the color of that tile is already yellow
                                    if (b[i][j].getColor().equals(PALEYELLOW)) {
                                        // change the direction of the selector
                                        across = !across;
                                    }                                  
                                    // the selector coordinates should be updated 
                                    x = i;
                                    y = j;
                                }
                            }

                            
                        }
                    }
                // draw the selector with the puzzle and cluePanel updated
                drawSelector(grid, clueNums, vClues, hClues, blackList);
                // update the hint on top of the puzzle
                updateHint(hint);
            } 
        });
        // add a mouseListener to track when the mouse is pressed over the cluePanel
        cluePanel.addMouseListener(new MouseAdapter(){
            /**
             * MousePressed abstract method that detects where the user
             * presses and makes that clue blue, while updating the puzzle
             */
            @Override
            public void mousePressed(MouseEvent e){
                //search through all the DOWN clues for the one pressed
                for (int i = 0; i < vClues.length; i++) {
                    // if the y coordinate of the press was between promptCoords
                    // i and i+1, make that label blue
                    if (e.getY() < vPromptCoords[i+1] && e.getY() > vPromptCoords[i]) {
                        vClues[i].setBackground(PALEBLUE);
                        // since this is a down clue, make the direction of the
                        // selector vertical
                        across = false;
                        // using the startListD arrayList set the selector x,y
                        x = startListD.get(i)[0];
                        y = startListD.get(i)[1];
                        // redraw the selector 
                        drawSelector(grid, clueNums, vClues, hClues, blackList);
                        // update the hint
                        updateHint(hint);
                    } else{ // remove the background if another clue is selected or outside the down clue area
                        vClues[i].setBackground(null);
                    }
                
                }
                //search through all the ACROSS clues for the one pressed
                for (int i = 0; i < hClues.length; i++) {
                    // if the y coordinate of the press was between promptCoords
                    // i and i+1, make that label blue
                    if (e.getY() < hPromptCoords[i+1] && e.getY() > hPromptCoords[i]) {
                        // since this is an across clue, make the direction of the
                        // selector horizontal
                        across = true;
                        hClues[i].setBackground(PALEBLUE);
                        // using the startListA arrayList set the selector x,y
                        x = startListA.get(i)[0];
                        y = startListA.get(i)[1];
                        // redraw the selector and update hint
                        drawSelector(grid, clueNums, vClues, hClues, blackList);
                        updateHint(hint);
                    } else{ // remove background if another is pressed or outside of the across clue area
                        hClues[i].setBackground(null);
                    }
                
                }
            }
        });
        // add the panels to the frame
        this.add(board);
        this.add(cluePanel);
        
        // create a time label ontop of the puzzle
        JLabel time = new JLabel();
        
        time.setBounds(425, 75, 50, 25);
        time.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        time.setOpaque(true);
        // create a timer that updates every 1000 miliseconds (1 sec)
        Timer timer = new Timer(1000, new ActionListener(){
            /**
             * Use the ActionListener to update the stopwatch every second
             */
            @Override
            public void actionPerformed(ActionEvent e){
                // increment the elapsed by a second
                elapsed =  elapsed+1000;
                mins = elapsed / 60000; // divide by to get minutes (60000 ms in 1 min)
                secs = (elapsed /1000)% 60;// divide by miliseconds and modulus 60 to find the remainder seconds in the minute
                secondString = String.format("%02d", secs); //a format that makes the seconds have a 0 before if less than 10
                time.setText(mins+":"+secondString); // set the label text
            }
        });
        
        // start the timer
        timer.start();
        this.add(time); // add to frame
        // add a keyListener to the frame
        this.addKeyListener(new KeyAdapter(){
            /**
             * Tracks the letters pressed on the keyboard to submit letters onto
             * the crossword puzzle, moves forward if letter, and backwards if 
             * backspace
             */
            @Override
            public void keyTyped(KeyEvent e){
                // get the key character integer
                int keyChar = e.getKeyChar();
                // if the selector is a black square to start
                if(grid[x][y].getText().equals("/")){
                    //  move to the next available square
                    moveSelect(grid, true, blackList);
                }
                // if it is a backspace
                else if (keyChar == KeyEvent.VK_BACK_SPACE) {
                    // replace the letter with blank
                    grid[x][y].setText("");
                    // move selector backwards on the grid
                    moveSelect(grid, false, blackList);
                } else{ // actual character
                    // make it uppercase
                    char c = Character.toUpperCase(e.getKeyChar());
                    // set the grid text of the selector to that letter
                    grid[x][y].setText(""+c);
                    
                    // check if the board is filled
                    filled = true;
                    // linear search through the whole grid
                    for (int i = 0; i < miniSize; i++) {
                        for (int j = 0; j < miniSize; j++) {
                            // if there is a blank square set filled to false
                            if (grid[i][j].getText().equals("")) {
                                filled = false;
                            }
                        }
                    }
                    // if the board is complete and answers are correct
                    if (checkBoard(grid)) {
                        // stop the timer
                        timer.stop();
                        
                        // show the user they won and their time
                        JOptionPane.showMessageDialog(rootPane, "You Win! \n Solved in "+time.getText());
                        won = true;
                    }else if(filled){ // if not won, but board is filled
                        // tell the user "not quite"
                        JOptionPane.showMessageDialog(rootPane, "Not Quite", "Mini Crossword", JOptionPane.ERROR_MESSAGE);
                    }else{// otherwise move the selector forward 
                        moveSelect(grid, true, blackList);
                    }
                    
                }
                
                
                // draw and update the grid and hint
                drawSelector(grid, clueNums, vClues, hClues, blackList);
                updateHint(hint);
                
            }

            
        });
        // set the frame visible
        this.setVisible(true);
        
    }
    /**
     * Draw the selector on the grid
     * @param grid - the JLabel grid
     * @param clueNums - the JLabel superscripts on each box (only some have numbers)
     * @param vClues - the vertical clues in the clue panel
     * @param hClues - the horizontal clues in the clue panel
     * @param blackList - the ArrayList<int[]> of omitted square coordinates
     */
    public void drawSelector(JLabel[][] grid, JLabel[][] clueNums, JLabel[] vClues, JLabel[] hClues, ArrayList<int[]> blackList){

        // assuming no omitted squares are selected
        // if the selector is going across
        if (across) {
            // create a string for the across clue in that square
            String rowPrompt = b[x][y].getRowPrompt();
            // if that square does not have a clue
            if (rowPrompt == null) {
                // set the selector to vertical and redraw the selector
                across = !across;
                drawSelector(grid, clueNums, vClues, hClues, blackList);
            }else{ // if it does have a row clue
                //search through the grid
                for (int i = 0; i < miniSize; i++) {
                    for (int j = 0; j < miniSize; j++) {
                        // if the square does not have the same row clue
                        if (!rowPrompt.equals(b[i][j].getRowPrompt())) {
                            // make it white
                           b[i][j].setColor(Color.WHITE); 
                        }else{ // if it has the same row clue
                            // make it blue
                            b[x][j].setColor(PALEBLUE);
                        }
                    }
                }
                
        
            }
    
        }else{// if the selector is going down
            // create a string for the down clue in that square
            String colPrompt = b[x][y].getColPrompt();
            // if that square does not have a clue
            if (colPrompt == null) {
                // set the selector to horizontal and redraw the selector
                across = !across;
                drawSelector(grid, clueNums, vClues, hClues, blackList);
            }else{// if it does have a down clue
                //search through the grid
                for (int i = 0; i < miniSize; i++) {
                    for (int j = 0; j < miniSize; j++) {
                        // if the square does not have the same row clue
                        if (!colPrompt.equals(b[i][j].getColPrompt())) {
                           b[i][j].setColor(Color.WHITE); // make square white
                        }else{// if it has the same row clue
                            b[i][y].setColor(PALEBLUE); // make it blue
                        }
                    }
                }
                
            }
        }
        // make the selected square yellow
        b[x][y].setColor(PALEYELLOW);
        // based on the square, highlight the corresponding clue
        updateCluePanel(vClues, hClues);
        
        // search through the coordinates of the omitted squares
        for (int[] coords: blackList) {
            // set the color to black on the three grids
            grid[coords[0]][coords[1]].setBackground(Color.BLACK); // draw label squares
            b[coords[0]][coords[1]].setColor(Color.BLACK); // base CrosswordTile
            clueNums[coords[0]][coords[1]].setBackground(Color.BLACK); // cluenumbers
        }
        // search through the whole grid again setting all the grid and clueNumber colors to the b color
        for (int i = 0; i < miniSize; i++) {
            for (int j = 0; j < miniSize; j++) {              
                clueNums[i][j].setBackground(b[i][j].getColor());
                grid[i][j].setBackground(b[i][j].getColor());
            }
        }
        // repaint the JFrame
       this.repaint();
        
    }
    /**
     * Update the clue at the top of the puzzle
     * @param hint - the JLabel on top of the puzzle
     */
    public void updateHint(JLabel hint){
        // if the selector is across
        if (across) {
            // set the text to the b RowPrompt using html to add new lines when needed
            hint.setText("<html><p>" + b[x][y].getRowPrompt()+"</p></html>");
        } else{ // if down
            // set the text to the b column Prompt using html to add new lines when needed
            hint.setText("<html><p>" +b[x][y].getColPrompt()+"</p></html>");
        }
    }
    /**
     * Highlight the clue that is being answered in the CluePanel
     * @param vClues - the down clue JLabels
     * @param hClues - the across clue JLabels
     */
    public void updateCluePanel(JLabel[] vClues, JLabel[] hClues){
        // if the selector is vertical
        if (!across) {
            // linear search through the down clues
            for (int i = 0; i < vClues.length; i++) {
                // replace the b clue's indicating letter to a space
                String selectedPrompt = "<html><p>"+b[x][y].getColPrompt().replaceFirst("D", " ")+"</p></html>";
                // if the column prompt matches a clue, highlight it blue
                if (selectedPrompt.equals(vClues[i].getText())) {
                    vClues[i].setBackground(PALEBLUE);
                } else{ // if they don't match remove the background
                    vClues[i].setBackground(null);
                }
                
                
            }
            // remove all the horizontal clue backgrounds
            for (int j = 0; j < hClues.length; j++) {
                hClues[j].setBackground(null);
            }
        }else{// if the selector is not vertical
            // linear search through the across clues
            for (int i = 0; i < hClues.length; i++) {
                // replace the b clue's indicating letter to a space
                String selectedPrompt = "<html><p>"+b[x][y].getRowPrompt().replaceFirst("A", " ")+"</p></html>";
                // if the row prompt matches a clue, highlight it blue
                if (selectedPrompt.equals(hClues[i].getText())) {
                    hClues[i].setBackground(PALEBLUE);
                } else{// if they don't match remove the background
                    hClues[i].setBackground(null);
                }
                
            }
            // remove all the vertical clue backgrounds
            for (int j = 0; j < vClues.length; j++) {
                    vClues[j].setBackground(null);
            }           
        }
    }
    /**
     * Move the selector (yellow) forwards or backwards
     * @param grid - the JLabel puzzle board
     * @param forward - whether it is going forward or not
     * @param blackList - the omitted square coordinates
     */
    public void moveSelect(JLabel[][] grid, boolean forward, ArrayList<int[]> blackList){
        // if moving forwards
        if (forward) {
            // if the selector is horizontal
            if (across) {
                // if the selected tile has not reached the right most in its row
                if (y < grid[0].length-1) {
                    y++;// move it right 1
                } else if(x < grid.length-1){ // if it has reached the right most, but not the bottom most 
                    // bring the selector back to the left
                    y = 0;
                    // move the whole selector down 1
                    x++;
                } else{ // if the selector is at the bottom right corner
                    // bring it back to the top left
                    x=0;
                    y=0;
                }
            } else{ // if the selector is vertical
                // if the selected tile has not reached the lowest row
                if (x < grid.length-1) {
                    x++; // move it down 1
                } else if(y < grid[0].length-1){ // if it has reached the lowest row, but not the right most
                    // bring the selector back to the top
                    x = 0;
                    // move the whole selector 1 to the right
                    y++;
                } else{// if the selector is at the bottom right corner
                    // bring it back to the top left
                    x = 0;
                    y = 0;
                }
            }
            
            // search through the omitted coordinates
            for (int[] coord: blackList) {
                // if the tile selected is black, recurse this method until the next one is not black
                if (coord[0] == x && coord[1] == y) {
                    moveSelect(grid, true, blackList);
                }
            }
            
        }else{ // if the selector is going backwards
            // if the selector is horizontal
            if (across) {
                // if it is not on the left most side
                if (y > 0) {
                    y --; // move left 1
                }else if (x > 0) { // if it is on the left, but not at the top
                    // bring the selected square to the right most square
                    y = grid[0].length-1;
                    // move up 1
                    x--;
                } else{ // if it is on the top left square
                    // bring it to the bottom right
                    x = grid[0].length-1;
                    y = grid.length-1;
                }
                
            } else{ // if the selector is vertical
                // if it is not on the top
                if (x > 0) {
                    x --;// move up 1
                } else if (y > 0) { // if it is at the top, but not on the left most column
                    // bring the selected square to the bottom square
                    x = grid.length-1;
                    // move left 1
                    y--;
                } else{ // if it is on the top left square
                    // bring it to the bottom right
                    x = grid[0].length-1;
                    y = grid.length-1;
                }  
            }
            // search through the omitted coordinates
            for (int[] coord: blackList) {
                // if the tile selected is black, recurse this method until selected is not black
                if (coord[0] == x && coord[1] == y) {
                    moveSelect(grid, false, blackList);
                }
            }
        }
    }
    /**
     * Check the grid with the CrosswordTile[][] board
     * @param grid - the JLabel 2D array that is the puzzle
     * @return - true if all entries are right and false if not
     */
    public boolean checkBoard(JLabel[][] grid) {
        // make a switch that is true
        boolean won = true;
        // interate through the whole puzzle
        for (int i = 0; i < miniSize; i++) {
            for (int j = 0; j < miniSize; j++) {
                // if one does not match the b board, switch to false
                if (!b[i][j].getLetter().equals(grid[i][j].getText())) {
                    won = false;   
                }
                
            }
        }
        
        return won;

    }
    /**
     * Accessor for if the game is won
     * @return - true if it is won, false if not
     */
    public boolean isWon() {
        return won;
    }
    /**
     * Accessor for the elapsed time spent playing the game
     * @return - and integer in miniseconds
     */
    public int getElapsed() {
        return elapsed;
    }
    

    @Override
    public void mouseClicked(MouseEvent e) {
        
        
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    
    }
}

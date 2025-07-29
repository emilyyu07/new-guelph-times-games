/*
Emily Yu
December 19, 2024
WordleGame Class (handles game logic for Wordle)
 */

import java.awt.Color;
import java.util.ArrayList;

public class WordleGame {

    //Wordle Colours
    private Color green = new Color(106, 170, 100);
    private Color yellow = new Color(201, 180, 88);
    private Color darkGray = new Color(120, 124, 126);
    private Color lightGray = new Color(211, 214, 218);

    //keyboard
    private WordleTile[] topRow = new WordleTile[10];
    private WordleTile[] midRow = new WordleTile[9];
    private WordleTile[] bottomRow = new WordleTile[7];

    //target word (represented in WordleTiles)
    private final WordleTile[] TARGET = new WordleTile[5];

    //Maximum number of guesses/attempts possible
    private final int MAX_GUESS = 6;

    //5x6 grid for Wordle
    private WordleTile[][] grid;

    //counter for the number guesses that the user has attempted
    private int currentGuess;

    //whether or not the user has successfully guessed the word
    private boolean won;

    //array of all possible words
    private String[] words;

    //wordle stats object (holds # of games played, # wins, guess distribution for wins)
    private WordleStats stats;

    /**
     * Constructor for WordleGame
     *
     * @param words - list of all possible 5 letter words
     */
    public WordleGame(String[] words, WordleStats stats) {
        this.stats = stats;
        this.words = words;
        currentGuess = 0;
        won = false;

        //select the target word from the list
        String targetWord = selectRandomWord(words);

        //convert the target word into a array of wordle tiles
        for (int i = 0; i < 5; i++) {
            TARGET[i] = new WordleTile(targetWord.substring(i, i + 1));
        }

        //TESTING PURPOSES (reveals correct wordle)
        System.out.print("Wordle Answer: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(TARGET[i].getLetter());
        }

        //Start a empty 6x5 grid/board (create "letterless" tiles)
        grid = new WordleTile[6][5];
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                grid[row][col] = new WordleTile(" ");
            }

        }

        //fill the keyboard arrays with letters according to "qwerty" format
        loadKeyboard();

        //adjust colour of keyboard to match traditional Wordle
        for (int i = 0; i < 10; i++) {
            topRow[i].setColor(lightGray);
        }

        for (int i = 0; i < 9; i++) {
            midRow[i].setColor(lightGray);
        }

        for (int i = 0; i < 7; i++) {
            bottomRow[i].setColor(lightGray);
        }

    }

    /**
     * Method hardcodes each letter into the keyboard arrays (to follow the
     * "qwerty" format)
     */
    public void loadKeyboard() {
        //hardcode the letters into the keyboard
        topRow[0] = new WordleTile("Q", 40);
        topRow[1] = new WordleTile("W", 40);
        topRow[2] = new WordleTile("E", 40);
        topRow[3] = new WordleTile("R", 40);
        topRow[4] = new WordleTile("T", 40);
        topRow[5] = new WordleTile("Y", 40);
        topRow[6] = new WordleTile("U", 40);
        topRow[7] = new WordleTile("I", 40);
        topRow[8] = new WordleTile("O", 40);
        topRow[9] = new WordleTile("P", 40);

        midRow[0] = new WordleTile("A", 40);
        midRow[1] = new WordleTile("S", 40);
        midRow[2] = new WordleTile("D", 40);
        midRow[3] = new WordleTile("F", 40);
        midRow[4] = new WordleTile("G", 40);
        midRow[5] = new WordleTile("H", 40);
        midRow[6] = new WordleTile("J", 40);
        midRow[7] = new WordleTile("K", 40);
        midRow[8] = new WordleTile("L", 40);

        bottomRow[0] = new WordleTile("Z", 40);
        bottomRow[1] = new WordleTile("X", 40);
        bottomRow[2] = new WordleTile("C", 40);
        bottomRow[3] = new WordleTile("V", 40);
        bottomRow[4] = new WordleTile("B", 40);
        bottomRow[5] = new WordleTile("N", 40);
        bottomRow[6] = new WordleTile("M", 40);
    }

    /**
     * Method updates the keyboard tiles with the correct colour with each guess
     *
     * @param letter - particular letter in the user's guess
     * @param color - colour that the tile on keyboard should be set to
     */
    public void checkKeyboard(String letter, Color color) {
        boolean found = false;

        //search through top row of keyboard
        for (int i = 0; i < 10; i++) {
            if (found == false) {
                //if letter has not already been found in this row
                if (letter.equalsIgnoreCase(topRow[i].getLetter())) {
                    //Special case: if the tile is already green, do not set it to yellow
                    Color oldColour = topRow[i].getColor();
                    if (!(oldColour == green && color == yellow)) {
                        topRow[i].setColor(color);
                    }
                    //indicate letter has been found
                    found = true;
                }
            }

        }

        //search through middle row of keyboard (if letter has not already been found)
        if (found == false) {
            for (int i = 0; i < 9; i++) {
                //if letter has not already been found in this row
                if (found == false) {
                    if (letter.equalsIgnoreCase(midRow[i].getLetter())) {
                        //Special case: if the tile is already green, do not set it to yellow
                        Color oldColour = midRow[i].getColor();
                        if (!(oldColour == green && color == yellow)) {
                            midRow[i].setColor(color);
                        }
                        //indicate letter has been found
                        found = true;
                    }
                }

            }

        }

        //search through bottom row of keyboard (if letter has not already been found)
        if (found == false) {
            for (int i = 0; i < 7; i++) {
                //if letter has not already been found in this row
                if (found == false) {
                    if (letter.equalsIgnoreCase(bottomRow[i].getLetter())) {
                        //Special case: if the tile is already green, do not set it to yellow
                        Color oldColour = bottomRow[i].getColor();
                        if (!(oldColour == green && color == yellow)) {
                            bottomRow[i].setColor(color);
                        }
                        //indicate letter has been found
                        found = true;
                    }
                }

            }

        }

    }

    /**
     * method randomly selects a 5 letter word from the given list
     *
     * @param words - alphabetically sorted array of all possible 5 letters
     * words
     * @return - selected word (target)
     */
    public static String selectRandomWord(String[] words) {
        int listLength = words.length;
        int randomPos = (int) (Math.random() * listLength);
        return words[randomPos];
    }

    /**
     * Method checks the user's attempted guess
     *
     */
    public void checkGuess() {
        //tracks which letters of the target that have been properly matched
        boolean[] matched = new boolean[5];

        /*
        Note: other error checking (checks for valid length, if the guess is
        a real word, if the user has won yet) is completed in the WordleGamePanel. 
        These error checks are not completed in this class because the JOptionPane 
        "error" messages interrupt the execution of the game display, so they must 
        be included in the panel class instead (where the display focus can be reset).
         */
        //Stores the letters in the target word that appear more than once
        String[] duplicates = multipleOccurence();

        //Stores the number of times each duplicate letter appears in the target word 
        int[] countDup = countMultiple(duplicates);

        //if the user has not exceeded the limit of 6 guesses 
        if (currentGuess < MAX_GUESS && !won) {

            //Check for exact matches (correct letter and correct position)--> GREEN
            for (int i = 0; i < 5; i++) {
                //if the letter matches and the position is correct
                if (grid[currentGuess][i].getLetter().equalsIgnoreCase(TARGET[i].getLetter())) {
                    //set colour to green
                    grid[currentGuess][i].setColor(green);

                    //indicate that letter is in the correct position
                    grid[currentGuess][i].setCorrectPos(true);

                    //indicate that letter is in the target word
                    grid[currentGuess][i].setIncludedLetter(true);

                    //indicate that this letter of the target is now matched
                    matched[i] = true;

                    //update the colour of the tile with this letter on the keyboard
                    String correctLetter = grid[currentGuess][i].getLetter();
                    checkKeyboard(correctLetter, green);

                }
            }

            //Check for partial matches (correct letter but incorrect position) --> YELLOW
            for (int i = 0; i < 5; i++) {
                //check if the current letter is already an exact match (set to green)
                if (grid[currentGuess][i].getColor() != green) {
                    //if not, check the current letter to see if it is present in the target word at all
                    for (int j = 0; j < 5; j++) {
                        /*
                        Error checking/implementing consistency with existing Wordle rules:
                        1. Check if the letter in the target word has already been matched/used up.
                        For instance, if the target word is "stark" and the guess is "abaca", 
                        the first and last 'a' should be grey and not yellow (since there is 
                        only one 'a' in the target word and it has already been matched). 
                        
                        2. Check whether or not duplicate letters are present if setting the colour
                        of a tile to yellow. For instance, if the target word if "paths" and the guess 
                        is "essay", only one of the 's' should turn yellow, the other should turn gray.
                        Since there is only one 's' in the word, it is misleading to indicate two yellow
                        's'.
                         */

                        if (grid[currentGuess][i].getLetter().equalsIgnoreCase(TARGET[j].getLetter()) && matched[j] != true) {
                            //In the case of duplicate letters in the guess, only set to yellow if the target word also has duplicate letters
                            boolean check = false;
                            for (int e = 0; e < duplicates.length; e++) {
                                if (grid[currentGuess][i].getLetter().equalsIgnoreCase(duplicates[e])) {
                                    if (countDup[e] > 0) {
                                        check = true;
                                        //indicate that one of the duplicate letters have been "used up" in the guess
                                        countDup[e]--;
                                    }
                                }

                            }

                            //if there ARE duplicate letters in the target that haven't been set to yellow yet
                            if (check) {
                                //set colour to yellow
                                grid[currentGuess][i].setColor(yellow);
                            } else {
                                //if NOT, set colour to gray
                                grid[currentGuess][i].setColor(darkGray);
                            }

                            //letter is in the target word (but incorrect position)
                            grid[currentGuess][i].setIncludedLetter(true);

                            //indicate that the letter is yellow on the keyboard
                            String correctLetter = grid[currentGuess][i].getLetter();

                            //update the colour of the tile with this letter on the keyboard (only if the tile is not already green)
                            checkKeyboard(correctLetter, yellow);

                            //once we have found our letter in the target word, we can stop iterating through the rest of the letters (end loop)
                            break;
                        }
                    }
                }

                /*
                After checking for both exact/partial matches, if this tile still has
                not matched with a letter in the target, then it means that this 
                letter in the guess is not present in the target word. Therefore, we set 
                the colour of this tile to GRAY.
                 */
                if (grid[currentGuess][i].isIncludedLetter() == false) {
                    //set colour to dark gray 
                    grid[currentGuess][i].setColor(darkGray);

                    //indicate that the letter is dark gray on the keyboard
                    String correctLetter = grid[currentGuess][i].getLetter();

                    //update the colour of the tile with this letter on the keyboard
                    checkKeyboard(correctLetter, darkGray);
                }
            }

            //increase the number of guesses
            currentGuess++;
        }
    }

    /**
     * Method checks for letters that appear more than once in the target word
     *
     * @return - an array of all "duplicate" letters in the target word
     */
    public String[] multipleOccurence() {
        String[] targetLetters = new String[5];
        //duplicates letters are added to this list as they are found
        ArrayList<String> duplicates = new ArrayList<String>();

        //Convert the array of WordleTiles into an array of Strings
        for (int i = 0; i < 5; i++) {
            targetLetters[i] = TARGET[i].getLetter();
        }

        for (int i = 0; i < 5; i++) {
            //counts the number of times a particular letter appears in the target
            int count = 0;
            //current 
            String letter = TARGET[i].getLetter();
            for (int a = 0; a < 5; a++) {
                //if this letter appears in the target, increase the count by 1
                if (targetLetters[i].equalsIgnoreCase(letter)) {
                    count++;
                }
            }

            //after iterating through the whole target word, if count is greater than 1
            //the letter appears more than once 
            if (count > 1) {
                duplicates.add(letter);
            }
        }

        //convert the ArrayList of duplicates into an array (easier to handle in other parts of the program)
        String[] dup = new String[duplicates.size()];
        for (int i = 0; i < duplicates.size(); i++) {
            dup[i] = duplicates.get(i);
        }

        return dup;
    }

    /**
     * Method counts the number of times each duplicate letter appears in the
     * word
     *
     * @param duplicates - array of "duplicate" letters
     * @return - array containing the number of occurences of each "duplicate"
     * letter
     */
    public int[] countMultiple(String[] duplicates) {
        int[] countMultiple = new int[duplicates.length];
        for (int i = 0; i < 5; i++) {
            //iterate through the target word
            for (int a = 0; a < duplicates.length; a++) {
                //if the letters match
                if (TARGET[i].getLetter().equalsIgnoreCase(duplicates[a])) {
                    //increase the count for this duplicate letter
                    countMultiple[a]++;
                }

            }
        }
        return countMultiple;

    }

    /**
     * Method tracks as duplicate letters in the target have been used up in the
     * guess
     *
     * @param letter - the letter in the guess being checked
     * @param duplicates - array of duplicate letters
     * @param count - array of the number of occurences of each duplicate letter
     */
    public void trackDoubles(String letter, String[] duplicates, int[] count) {
        for (int i = 0; i < duplicates.length; i++) {
            if (letter.equalsIgnoreCase(duplicates[i])) {
                count[i]--;
            }
        }

    }

    /**
     * Method checks whether or not the user has successfully guessed the word
     *
     * @return - whether or not the user has won (true/false)
     */
    public boolean checkWin() {
        boolean hasWon = true;

        //check each letter/tile of the previous guess
        for (int i = 0; i < 5; i++) {
            WordleTile currentTile = grid[currentGuess - 1][i];
            //all letters would have correctPos set to true if the guess was successful
            //otherwise, the user did not win yet
            //if any letter/tile is in the incorrect position, the user hasn't guessed the word yet
            if (currentTile.isCorrectPos() == false) {
                hasWon = false;
                break;
            }
        }

        return hasWon;
    }

    /**
     * Method verifies that the user entered a 5 letter word
     *
     * @return - whether or not the length is valid (true/false)
     */
    public boolean checkLength() {
        for (int i = 0; i < 5; i++) {
            //checks for "empty" tiles
            if (grid[currentGuess][i].getLetter().equals("")) {
                //if any tile in empty, length is invalid (<5)
                return false;
            }
        }
        return true;
    }

    /**
     * Method verifies that the user entered a valid word (included in the word
     * list)
     *
     * @return - whether or not the guess is a real word (true/false)
     */
    public boolean checkWordList() {
        String guess = "";

        //convert the guess (in WordleTiles) to a String
        for (int i = 0; i < 5; i++) {
            guess = guess + grid[currentGuess][i].getLetter();
        }

        //search for the guessed in our word list
        boolean inList = searchWord(0, words.length - 1, guess);

        /*
        If the guessed word is in the list, it is valid.
        In this case, inList would be true, so we return true.
        Otherwise, the guessed word is not valid and inList would be 
        false, so we return false.
         */
        return inList;

    }

    /**
     * Method uses determines if the guessed word is in the word list (through
     * binary search and lexicographical comparisons)
     *
     * @param left - index of the leftmost element in the array
     * @param right - index of the rightmost element in the array
     * @param guess - the user's guess
     * @return - whether or not the guess is in the list (true/false)
     */
    public boolean searchWord(int left, int right, String guess) {
        //Base case: check if there are any elements left to search through in the array
        if (left > right) {
            return false;
        }

        //find the middle index of the array
        int middle = (left + right) / 2;

        //compare the middle element of the array with the guessed word
        int comparison = guess.compareToIgnoreCase(words[middle]);

        /*
        The two strings are lexicographically compared:
        If the guess and middle word match, comparison will be equal to 0 (strings are equal)
        If the guess comes alphabetically AFTER the middle word, comparison
        will be positive. 
        If the guess comes alphabetically BEFORE the middle word, comparison
        will be negative.
         */
        if (comparison == 0) {
            //the guess is successfully found in our word list
            return true;
        } else if (comparison < 0) {
            //the guess comes before the middle element
            //therefore, repeat search through only the first half of the array
            return searchWord(left, middle - 1, guess);
        } else {
            //the guess comes after the middle element 
            //therefore, repeat search through only the second half of the array
            return searchWord(middle + 1, right, guess);
        }

    }

    
    
    //ACCESSORS AND MUTATORS----------------------------------------------------
    /**
     * Accessor for green
     *
     * @return - CORRECT WordleTile colour
     */
    public Color getGreen() {
        return green;
    }

    /**
     * Accessor for yellow
     *
     * @return - PARTIALLY CORRECT WordleTile colour
     */
    public Color getYellow() {
        return yellow;
    }

    /**
     * Accessor for darkGray
     *
     * @return - INCORRECT WordleTile colour
     */
    public Color getDarkGray() {
        return darkGray;
    }

    /**
     * Accessor for lightGray
     *
     * @return - keyboard WordleTile colour
     */
    public Color getLightGray() {
        return lightGray;
    }

    /**
     * Mutator for lightGray
     *
     * @param lightGray - keyboard WordleTile colour
     */
    public void setLightGray(Color lightGray) {
        this.lightGray = lightGray;
    }

    /**
     * Mutator for green
     *
     * @param green - CORRECT WordleTile colour
     */
    public void setGreen(Color green) {
        this.green = green;
    }

    /**
     * Mutator for yellow
     *
     * @param yellow - PARTIALLY CORRECT WordleTile colour
     */
    public void setYellow(Color yellow) {
        this.yellow = yellow;
    }

    /**
     * Mutator for darkGray
     *
     * @param darkGray - INCORRECT WordleTile colour
     */
    public void setDarkGray(Color darkGray) {
        this.darkGray = darkGray;
    }

    /**
     * Accessor for TARGET
     *
     * @return - the target word (in the form of WordleTiles)
     */
    public WordleTile[] getTARGET() {
        return TARGET;
    }

    /**
     * Accessor for MAX_GUESS
     *
     * @return - the maximum number of possible guesses
     */
    public int getMAX_GUESS() {
        return MAX_GUESS;
    }

    /**
     * Accessor for words
     *
     * @return - array of all possible 5 letter words
     */
    public String[] getWords() {
        return words;
    }

    /**
     * Mutator for words
     *
     * @param words - array of all possible 5 letter words
     */
    public void setWords(String[] words) {
        this.words = words;
    }

    /**
     * Accessor for won
     *
     * @return - whether or not the user has won the game
     */
    public boolean isWon() {
        return won;
    }

    /**
     * Mutator for won
     *
     * @param won - whether or not the user has won the game (true/false)
     */
    public void setWon(boolean won) {
        this.won = won;
    }

    /**
     * Mutator for grid
     *
     * @param grid - the Wordle grid of tiles
     */
    public void setGrid(WordleTile[][] grid) {
        this.grid = grid;
    }

    /**
     * Mutator for currentGuess
     *
     * @param currentGuess - the number of guesses the user attempted
     */
    public void setCurrentGuess(int currentGuess) {
        this.currentGuess = currentGuess;
    }

    /**
     * Accessor for target
     *
     * @return - target word
     */
    public WordleTile[] getTarget() {
        return TARGET;
    }

    /**
     * Accessor for maxGuess
     *
     * @return - maximum guesses possible (6)
     */
    public int getMaxGuess() {
        return MAX_GUESS;
    }

    /**
     * Accessor for grid
     *
     * @return - the Wordle grid of tiles
     */
    public WordleTile[][] getGrid() {
        return grid;
    }

    /**
     * Accessor for currentGuess
     *
     * @return - the number of guesses the user has attempted
     */
    public int getCurrentGuess() {
        return currentGuess;
    }

    /**
     * Accessor for the topRow
     *
     * @return - the top row of the keyboard (wordleTile array)
     */
    public WordleTile[] getTopRow() {
        return topRow;
    }

    /**
     * Accessor for the midRow
     *
     * @return - the middle row of the keyboard (wordleTile array)
     */
    public WordleTile[] getMidRow() {
        return midRow;
    }

    /**
     * Accessor for the bottomRow
     *
     * @return - the bottom row of the keyboard (wordleTile array)
     */
    public WordleTile[] getBottomRow() {
        return bottomRow;
    }

    /**
     * Mutator for topRow
     *
     * @param topRow - the top row of the keyboard (array of wordleTiles)
     */
    public void setTopRow(WordleTile[] topRow) {
        this.topRow = topRow;
    }

    /**
     * Mutator for midRow
     *
     * @param midRow - the middle row of the keyboard (array of wordleTiles)
     */
    public void setMidRow(WordleTile[] midRow) {
        this.midRow = midRow;
    }

    /**
     * Mutator for bottomRow
     *
     * @param bottomRow - the bottom row of the keyboard (array of wordleTiles)
     */
    public void setBottomRow(WordleTile[] bottomRow) {
        this.bottomRow = bottomRow;
    }

    /**
     * Accessor for stats
     *
     * @return - wordle stats object
     */
    public WordleStats getStats() {
        return stats;
    }

    /**
     * Mutator for stats
     *
     * @param stats - wordle stats object
     */
    public void setStats(WordleStats stats) {
        this.stats = stats;
    }
    

    //--------------------------------------------------------------------------
    /**
     * Method displays the "contents" of the WordleGame
     *
     * @return - message displaying info about the WordleGame
     */
    public String toString() {
        return "WordleGame{" + "green=" + green + ", yellow=" + yellow + ", darkGray=" + darkGray + ", lightGray=" + lightGray + ", topRow=" + topRow + ", midRow=" + midRow + ", bottomRow=" + bottomRow + ", TARGET=" + TARGET + ", MAX_GUESS=" + MAX_GUESS + ", grid=" + grid + ", currentGuess=" + currentGuess + ", won=" + won + ", words=" + words + '}';
    }

}

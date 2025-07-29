/*
Jeremy Wong
January 13, 2025
Class holder for Connections game
 */


//import necessary packages

import java.awt.Color;
import java.util.Scanner;
import java.awt.EventQueue;
import java.io.InputStream;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Collections;

public class FinalProjectConnectionClassHolder extends JFrame {

    //create global variable for tileList including all the tiles in the game
    private ArrayList<ConnectionsTile> tileList = new ArrayList();

    //constructor that is run once play is hit
    public FinalProjectConnectionClassHolder() {
        //create a second arraylist that will do the initial sorting of words
        ArrayList<ConnectionsTile> tileListPre = new ArrayList();
        //create a 2-d array that will store all of the information in groups dependent on categories
        String[][] words = new String[100][5];
        //calls a function that will read the data file
        readFile(words);
        //create 4 variables that will hold each of the 5 words from a randomly chosen category
        String[] cat1Words = new String[5];
        String[] cat2Words = new String[5];
        String[] cat3Words = new String[5];
        String[] cat4Words = new String[5];
        //holds the category names in an array
        String[] categories = new String[4];
        //holds all the chosen words together in an array
        String[] chosenWords = new String[16];
        //invokes a method that will choose all the words that will be used in the game
        chooseWords(cat1Words, cat2Words, cat3Words, cat4Words, words, chosenWords, categories);
        //this creates a connection tile for each of the chosen words, assigning a solution number an dits category
        for (int i = 0; i < 4; i++) {
            ConnectionsTile c = new ConnectionsTile(chosenWords[i], 0, categories[0]);
            tileListPre.add(c);
        }
        for (int i = 4; i < 8; i++) {
            ConnectionsTile c = new ConnectionsTile(chosenWords[i], 1, categories[1]);
            tileListPre.add(c);
        }
        for (int i = 8; i < 12; i++) {
            ConnectionsTile c = new ConnectionsTile(chosenWords[i], 2, categories[2]);
            tileListPre.add(c);
        }
        for (int i = 12; i < 16; i++) {
            ConnectionsTile c = new ConnectionsTile(chosenWords[i], 3, categories[3]);
            tileListPre.add(c);
        }
        //this shuffels the list so that it is scrambled when it is displayed on screen
        Collections.shuffle(tileListPre);
        //this variable is to keep track of which set they are on so they can add solution, category and names from the tilePreList array
        int current = 0;
        //give all the tiles the same color of grey
        Color grey = new Color(240, 236, 228);
        //for loop that will assign the x and y variables of each of the 16 tiles by adding 120 to the x each time and 95 to the y each time
        for (int i = 70; i < 440; i = i + 120) {
            for (int j = 100; j < 400; j = j + 95) {
                //create the connections tile
                ConnectionsTile n = new ConnectionsTile(tileListPre.get(current).getWord(), i, j, tileListPre.get(current).getSol(), tileListPre.get(current).getCategory(), grey);
                //add it to the global tileList array list
                tileList.add(n);
                //add one to current to keep track of which set of words it is on
                current++;
            }
        }
        //create the User interface
        initUI();
    }

    /**
     * main method that creates the jframe
     *
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            //create the JFrame
            FinalProjectConnectionClassHolder frame = new FinalProjectConnectionClassHolder();
        });
    }

    /**
     * getter for the drawing class to call the tileList that was created
     *
     * @return - tileList which includes all the ConnectionsTiles
     */
    public ArrayList<ConnectionsTile> getTileList() {
        return tileList;
    }

    private void initUI() {
        //set title of the JFrame
        setTitle("Connections");
        //add a custom JPanel to draw on
        add(new Drawing(this));
        //set the size of the window
        setSize(600, 800);
        //make it visible 
        setVisible(true);
        //tell the JFrame what to do when closed
        //this is important if our application has multiple windows
        //ensures that when the Wordle window is closed, the main GUI is not closed along with it
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * reads a data file and fills a 2-d array with the words
     *
     * @param words return is the loaded 2-d array
     */
    public static void readFile(String[][] words) {
        //try to access the file
        try {
            //file path do data file
            InputStream inConnections = NGTGUI.class.getResourceAsStream("ConnectionsWords");

            //place file into scanner
            Scanner s = new Scanner(inConnections);
            
            //read the file into the 2-d array using for loops
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 5; j++) {
                    words[i][j] = s.nextLine();
                }
            }
            //if it fails to find the file and place a scanner into it
        } catch (Exception e) {
            //output error
            System.out.println(e);
        }
    }

    /**
     *
     * @param cat1Words - empty array that will be filled with words from the
     * words array
     * @param cat2Words - empty array that will be filled with words from the
     * words array
     * @param cat3Words - empty array that will be filled with words from the
     * words array
     * @param cat4Words - empty array that will be filled with words from the
     * words array
     * @param words - 2d array that has all the words in the data file
     * @param chosenWords - empty array that will hold all the chosen words
     * together
     * @param categories - empty array that will hold the category names from
     * the words array
     */
    public static void chooseWords(String[] cat1Words, String[] cat2Words, String[] cat3Words, String[] cat4Words, String[][] words, String[] chosenWords, String[] categories) {
        //create four variables and then assign them a random number
        int r1, r2, r3, r4;
        r1 = (int) (Math.random() * 100);
        r2 = (int) (Math.random() * 100);
        //check to make sure they are not the same and re assign number until they are not the same
        while (r1 == r2) {
            r2 = (int) (Math.random() * 100);
        }
        //same as previus but with all three of the numbers
        r3 = (int) (Math.random() * 100);
        while (r3 == r1 || r3 == r2) {
            r3 = (int) (Math.random() * 100);
        }
        //same as previous except with previous three numbers
        r4 = (int) (Math.random() * 100);
        while (r4 == r1 || r4 == r2 || r4 == r3) {
            r4 = (int) (Math.random() * 100);
        }
        //fill the category words arrays with their corresponding random number which is from the 2d array
        cat1Words = words[r1];
        cat2Words = words[r2];
        cat3Words = words[r3];
        cat4Words = words[r4];
        //fill the list of all chosen words using for loops
        for (int i = 0; i < 4; i++) {
            chosenWords[i] = cat1Words[i + 1];
        }
        for (int i = 0; i < 4; i++) {
            chosenWords[i + 4] = cat2Words[i + 1];
        }
        for (int i = 0; i < 4; i++) {
            chosenWords[i + 8] = cat3Words[i + 1];
        }
        for (int i = 0; i < 4; i++) {
            chosenWords[i + 12] = cat4Words[i + 1];
        }
        //fill the category array with the categories from the cat1,2,3 and 4 word arrays
        categories[0] = cat1Words[0];
        categories[1] = cat2Words[0];
        categories[2] = cat3Words[0];
        categories[3] = cat4Words[0];
    }

}

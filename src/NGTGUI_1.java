/*
Emily Yu, Bob Zhao, Jeremy Wong
January 14, 2025
The New Guelph Times GUI
 */

/**
 *
 * @author EmYu0928
 */
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class NGTGUI_1 extends javax.swing.JFrame {

    private String[] wordleWords = new String[5737];
    private static ArrayList leaderboard = new ArrayList();
    private WordleStats stats = new WordleStats();
    private WordleGame game;

    /**
     * Creates new form NGTGUI
     */
    public NGTGUI_1() {
        initComponents();

        try {
            //Reads the file of 5 letter words for WORDLE-----------------------
            InputStream inWordle = NGTGUI.class.getResourceAsStream("wordleWords.txt");
            Scanner sWordle = new Scanner(inWordle);

            for (int i = 0; i < 5737; i++) {
                wordleWords[i] = sWordle.nextLine();
            }

            //sort the words in alphabetical order (for easier searching)
            Arrays.sort(wordleWords);

            //------------------------------------------------------------------
            //------------------------------------------------------------------
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Read the data file into the CrosswordTile board
     *
     * @return - the new board read into
     */
    public static CrosswordTile[][] readCrossword() {
        try {
            // get a random number from 1-10
            int random = (int) (Math.random() * 10) + 1;
            // make an input stream
            InputStream in;
            // based on the random number, read a crossword file
            if (random == 1) {
                // if the random number is 1, read Crossword 1
                in = NGTGUI.class.getResourceAsStream("Crossword_1.txt");
            } else if (random == 2) {
                // if the random number is 2, read Crossword 2
                in = NGTGUI.class.getResourceAsStream("Crossword_2.txt");
            } else if (random == 3) {
                // if the random number is 3, read Crossword 3
                in = NGTGUI.class.getResourceAsStream("Crossword_3.txt");
            } else if (random == 4) {
                // if the random number is 4, read Crossword 4
                in = NGTGUI.class.getResourceAsStream("Crossword_4.txt");
            } else if (random == 5) {
                // if the random number is 5, read Crossword 5
                in = NGTGUI.class.getResourceAsStream("Crossword_5.txt");
            } else if (random == 6) {
                // if the random number is 6, read Crossword 6
                in = NGTGUI.class.getResourceAsStream("Crossword_6.txt");
            } else if (random == 7) {
                // if the random number is 7, read Crossword 7
                in = NGTGUI.class.getResourceAsStream("Crossword_7.txt");
            } else if (random == 8) {
                // if the random number is 8, read Crossword 8
                in = NGTGUI.class.getResourceAsStream("Crossword_8.txt");
            } else if (random == 9) {
                // if the random number is 9, read Crossword 9
                in = NGTGUI.class.getResourceAsStream("Crossword_9.txt");
            } else {
                // if the random number is anything else, read Crossword
                in = NGTGUI.class.getResourceAsStream("Crossword.txt");
            }
            // instantiate a scanner
            Scanner s = new Scanner(in);
            // read the number of rows and columns of the square puzzle
            int size = Integer.parseInt(s.nextLine());
            // read the number of vertical and horizontal hints
            int vClues = Integer.parseInt(s.nextLine());
            int hClues = Integer.parseInt(s.nextLine());
            // instantiate the board
            CrosswordTile[][] b = new CrosswordTile[size][size];

            // instantiate each crossword Tile
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    b[i][j] = new CrosswordTile("");
                    b[i][j].setSize(size);
                }
            }
            int[] vClueNums = new int[vClues];
            String[] vPrompts = new String[vClues];
            int[] hClueNums = new int[hClues];
            String[] hPrompts = new String[hClues];

            // read the prompts and promptclues into the tiles
            // vertical clues
            for (int i = 0; i < vClues; i++) {
                vClueNums[i] = s.nextInt();
                vPrompts[i] = s.nextLine();
            }
            // horizontal clues
            for (int i = 0; i < hClues; i++) {
                hClueNums[i] = s.nextInt();
                hPrompts[i] = s.nextLine();
            }

            // create a correct board for the game
            for (int i = 0; i < size; i++) {
                // read the makeshift board into the array
                String r = s.nextLine();
                // split the string by the spaces to create an array
                String[] correct = r.split(" ");
                for (int j = 0; j < size; j++) {
                    b[i][j].setLetter(correct[j]);
                }

            }
            // count from 1 to the max number of clues
            int a = 1;
            // iterate through the b board
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    // if the square is not an omitted square
                    if (!b[i][j].getLetter().equals("/")) {
                        // if the square has a boundary above or to the left
                        if (under(b, i, j) && right(b, i, j)) {
                            // set the vertical and and horizontal clue number to a
                            b[i][j].setClueNumV(a);
                            b[i][j].setClueNumH(a);
                            a++; // increment a
                        } else if (under(b, i, j)) { // if it is only under a boundary
                            // only set the vertical cluenumber
                            b[i][j].setClueNumV(a);
                            a++;// increment a
                        } else if (right(b, i, j)) { // if it is only right of a boundary
                            b[i][j].setClueNumH(a); // only set horizontal clue
                            a++;// increment a
                        }
                    }
                }
            }
            // iterate through the b board again
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    // this time loop through the vertical clues
                    for (int k = 0; k < vClues; k++) {
                        // if the spot on the board has the vertical cluenumber
                        if (b[i][j].getClueNumV() == vClueNums[k]) {
                            // set the prompt of that square to the corresponding hint
                            b[i][j].setColPrompt("" + vClueNums[k] + "D" + vPrompts[k]);
                        }
                    }
                    // loop through the horizontal clues
                    for (int k = 0; k < hClues; k++) {
                        // if the spot on the board ahs the horizontal cluenumber
                        if (b[i][j].getClueNumH() == hClueNums[k]) {
                            // set the corresponding row prompt
                            b[i][j].setRowPrompt("" + hClueNums[k] + "A" + hPrompts[k]);
                        }
                    }
                    // if the letter is not omitted
                    if (!b[i][j].getLetter().equals("/")) {
                        // and the horizontal cluenumber is 0 
                        if (b[i][j].getClueNumH() == 0) {
                            // set the horizontal clue prompt to the one left of it
                            b[i][j].setRowPrompt(b[i][j - 1].getRowPrompt());
                        }
                        // the vertical cluenumber is 0
                        if (b[i][j].getClueNumV() == 0) {
                            // set the vertical clue prompt to the one above it
                            b[i][j].setColPrompt(b[i - 1][j].getColPrompt());
                        }
                    }
                }
            }
            // set the x,y of the first tile to the number of hclues and vclues
            b[0][0].setXPos(hClues);
            b[0][0].setYPos(vClues);

            return b;

            // if the file is not found return null 
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Check if there is a boundary above the square in a grid
     *
     * @param b - the CrosswordTile board being read
     * @param i - the row number
     * @param j - the column number
     * @return
     */
    public static boolean under(CrosswordTile[][] b, int i, int j) {
        // make sure there is at least 2 more spaces down in the word if it is a word
        if (i < b.length - 2) {
            // make sure there isn't an omitted square 2 spaces more into the word
            if (!b[i][j].getLetter().equals("/") && !b[i + 1][j].getLetter().equals("/") && !b[i + 2][j].getLetter().equals("/")) {
                // if the square is in the top row
                if (i == 0) {
                    return true; // there is a boundary on top so return true
                } else {
                    // return if the square above is an omitted square
                    return b[i - 1][j].getLetter().equals("/");
                }
            } else { // the square has an obstacle under it that makes it less than 3 letters long
                return false;
            }
        } else { // the square is too low to form a word that has more than 3 letters
            return false;
        }

    }

    /**
     * Check if there is a boundary left the square in a grid
     *
     * @param b - the CrosswordTile board being read
     * @param i - the row number
     * @param j - the column number
     * @return
     */
    public static boolean right(CrosswordTile[][] b, int i, int j) {
        // make sure there is at least 2 more spaces right in the word 
        if (j < b[0].length - 2) {
            // make sure there isn't an omitted square 2 spaces more into the word
            if (!b[i][j].getLetter().equals("/") && !b[i][j + 1].getLetter().equals("/") && !b[i][j + 2].getLetter().equals("/")) {
                // if the square is in the left column
                if (j == 0) {
                    return true;// there is a boundary on left so return true
                } else {
                    // return if the square left is an omitted square
                    return b[i][j - 1].getLetter().equals("/");
                }
            } else {// the square has an obstacle right that makes it less than 3 letters long
                return false;
            }
        } else {// the square is too far right to form a word that has more than 3 letters
            return false;
        }
    }

    /**
     * insertion Sort method that sorts an int array by their value
     *
     * @param items - the things to sort
     */
    public static void insertionSort(int[] items) {
        // Initialise the variables
        int numItems = items.length;

        // Repeat for each item in the list, starting at the second item
        for (int index = 1; index < numItems; index++) {

            // Get the value of the next item to insert
            int itemToInsert = items[index];

            // Get the current position of the last sorted item
            int position = index - 1;

            // Repeat while there are still items in the array to check
            // and the current sorted item is greater than the item to insert
            while (position >= 0 && items[position] > itemToInsert) {

                // Copy the value of the sorted item up one place
                items[position + 1] = items[position];

                // Get the position of the next sorted item
                position = position - 1;
            }
            // Copy the value of the item to insert into the correct position
            items[position + 1] = itemToInsert;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHome = new javax.swing.JPanel();
        lblNGTTitle = new javax.swing.JLabel();
        pnlConnections = new javax.swing.JPanel();
        pnlConnections2 = new javax.swing.JPanel();
        btnPlayConnections = new javax.swing.JButton();
        lblConnectionsIcon = new javax.swing.JLabel();
        pnlWordle = new javax.swing.JPanel();
        pnlWordle2 = new javax.swing.JPanel();
        btnRulesWordle = new javax.swing.JButton();
        btnPlayWordle = new javax.swing.JButton();
        btnStatsWordle = new javax.swing.JButton();
        lblWordleIcon = new javax.swing.JLabel();
        pnlCrossword = new javax.swing.JPanel();
        pnlCrossword2 = new javax.swing.JPanel();
        btnPlayCrossword = new javax.swing.JButton();
        lblCrosswordIcon = new javax.swing.JLabel();
        lblNGTLogo = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlHome.setBackground(new java.awt.Color(255, 255, 255));

        lblNGTTitle.setFont(new java.awt.Font("Perpetua Titling MT", 1, 33)); // NOI18N
        lblNGTTitle.setText("The New Guelph Times Games");

        pnlConnections.setBackground(new java.awt.Color(180, 168, 255));

        btnPlayConnections.setBackground(new java.awt.Color(179, 171, 251));
        btnPlayConnections.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPlayConnections.setText("PLAY");
        btnPlayConnections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayConnectionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlConnections2Layout = new javax.swing.GroupLayout(pnlConnections2);
        pnlConnections2.setLayout(pnlConnections2Layout);
        pnlConnections2Layout.setHorizontalGroup(
            pnlConnections2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConnections2Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(btnPlayConnections, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlConnections2Layout.setVerticalGroup(
            pnlConnections2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConnections2Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(btnPlayConnections, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        lblConnectionsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ConnectionsImg.png"))); // NOI18N

        javax.swing.GroupLayout pnlConnectionsLayout = new javax.swing.GroupLayout(pnlConnections);
        pnlConnections.setLayout(pnlConnectionsLayout);
        pnlConnectionsLayout.setHorizontalGroup(
            pnlConnectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConnections2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConnectionsLayout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(lblConnectionsIcon)
                .addGap(10, 10, 10))
        );
        pnlConnectionsLayout.setVerticalGroup(
            pnlConnectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConnectionsLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(lblConnectionsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlConnections2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlWordle.setBackground(new java.awt.Color(227, 227, 225));

        btnRulesWordle.setBackground(new java.awt.Color(107, 171, 99));
        btnRulesWordle.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRulesWordle.setText("RULES");
        btnRulesWordle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRulesWordleActionPerformed(evt);
            }
        });

        btnPlayWordle.setBackground(new java.awt.Color(107, 171, 99));
        btnPlayWordle.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPlayWordle.setText("PLAY");
        btnPlayWordle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayWordleActionPerformed(evt);
            }
        });

        btnStatsWordle.setBackground(new java.awt.Color(107, 171, 99));
        btnStatsWordle.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnStatsWordle.setText("STATS");
        btnStatsWordle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatsWordleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlWordle2Layout = new javax.swing.GroupLayout(pnlWordle2);
        pnlWordle2.setLayout(pnlWordle2Layout);
        pnlWordle2Layout.setHorizontalGroup(
            pnlWordle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWordle2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnPlayWordle)
                .addGap(12, 12, 12)
                .addComponent(btnRulesWordle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnStatsWordle)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlWordle2Layout.setVerticalGroup(
            pnlWordle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWordle2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlWordle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPlayWordle, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRulesWordle, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStatsWordle, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        lblWordleIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/WordleImg.png"))); // NOI18N

        javax.swing.GroupLayout pnlWordleLayout = new javax.swing.GroupLayout(pnlWordle);
        pnlWordle.setLayout(pnlWordleLayout);
        pnlWordleLayout.setHorizontalGroup(
            pnlWordleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWordle2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlWordleLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(lblWordleIcon)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlWordleLayout.setVerticalGroup(
            pnlWordleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWordleLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(lblWordleIcon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlWordle2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlCrossword.setBackground(new java.awt.Color(144, 184, 242));

        btnPlayCrossword.setBackground(new java.awt.Color(148, 188, 244));
        btnPlayCrossword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPlayCrossword.setText("PLAY");
        btnPlayCrossword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayCrosswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCrossword2Layout = new javax.swing.GroupLayout(pnlCrossword2);
        pnlCrossword2.setLayout(pnlCrossword2Layout);
        pnlCrossword2Layout.setHorizontalGroup(
            pnlCrossword2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCrossword2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPlayCrossword, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        pnlCrossword2Layout.setVerticalGroup(
            pnlCrossword2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCrossword2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnPlayCrossword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        lblCrosswordIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CrosswordImg.png"))); // NOI18N

        javax.swing.GroupLayout pnlCrosswordLayout = new javax.swing.GroupLayout(pnlCrossword);
        pnlCrossword.setLayout(pnlCrosswordLayout);
        pnlCrosswordLayout.setHorizontalGroup(
            pnlCrosswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCrossword2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlCrosswordLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblCrosswordIcon)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        pnlCrosswordLayout.setVerticalGroup(
            pnlCrosswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCrosswordLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblCrosswordIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlCrossword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblNGTLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NGTLogo.png"))); // NOI18N

        jButton1.setBackground(new java.awt.Color(232, 231, 231));
        jButton1.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jButton1.setText("CREDITS");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHomeLayout.createSequentialGroup()
                        .addComponent(pnlConnections, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlWordle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlCrossword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlHomeLayout.createSequentialGroup()
                        .addGroup(pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNGTTitle)
                            .addGroup(pnlHomeLayout.createSequentialGroup()
                                .addGap(192, 192, 192)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(51, 51, 51)
                        .addComponent(lblNGTLogo)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGroup(pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHomeLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(lblNGTTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHomeLayout.createSequentialGroup()
                        .addContainerGap(25, Short.MAX_VALUE)
                        .addComponent(lblNGTLogo)
                        .addGap(20, 20, 20)))
                .addGroup(pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlWordle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlConnections, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlCrossword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(pnlHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //CONNECTIONS GAMES
    private void btnPlayConnectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayConnectionsActionPerformed
        EventQueue.invokeLater(() -> {
            //create the JFrame
            FinalProjectConnectionClassHolder frame = new FinalProjectConnectionClassHolder();
        });


    }//GEN-LAST:event_btnPlayConnectionsActionPerformed

    //WORDLE RULES
    private void btnRulesWordleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRulesWordleActionPerformed
        WordleRulesWindow wordleRulesW = new WordleRulesWindow();
        WordleRulesPanel wordleRulesP = new WordleRulesPanel();

        wordleRulesW.add(wordleRulesP);    }//GEN-LAST:event_btnRulesWordleActionPerformed

    //WORDLE GAME
    private void btnPlayWordleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayWordleActionPerformed
        //when the play button is clicked, create a new wordle game
        game = new WordleGame(wordleWords, stats);

        //open the graphics2D window for the Wordle game
        WordleGameWindow graphicsWindow = new WordleGameWindow();

        //pass the Wordle game into the panel (to be drawn)
        WordleGamePanel drawingPanel = new WordleGamePanel(game);

        //add drawing panel to the JFrame window
        graphicsWindow.add(drawingPanel);

    }//GEN-LAST:event_btnPlayWordleActionPerformed

    //CROSSWORD GAME
    private void btnPlayCrosswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayCrosswordActionPerformed
        // read the CrosswordTile[][] board from a file
        CrosswordTile[][] board = readCrossword();
        if (board == null) {
            JOptionPane.showMessageDialog(null, "Unable to load crossword puzzle data.", "Crossword Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // create a new MiniCrosswordFrame using that board
        MiniCrosswordFrame myFrame = new MiniCrosswordFrame(board);

        // add a window listener for when the window closes to the frame
        myFrame.addWindowListener(new WindowAdapter() {
            /**
             * When the window closes, use JOptionPane to show the user the top
             * scores
             */
            @Override
            public void windowClosing(WindowEvent e) {
                // create an int array to store the best times
                int[] bestTimes;
                // if the game is won
                if (myFrame.isWon()) {
                    // add the elapsed time to a global array list leaderboard
                    leaderboard.add(myFrame.getElapsed());
                    // make that arraylist into an object array
                    Object[] lead = leaderboard.toArray();
                    // make the int array the same size
                    bestTimes = new int[lead.length];
                    // assign each value in the int array to the elapsed time
                    for (int i = 0; i < lead.length; i++) {
                        // make the elapsed time an int
                        bestTimes[i] = Integer.parseInt(lead[i].toString());
                    }
                    // sort the int array using insertion sort
                    insertionSort(bestTimes);
                    // create an output string
                    String output = "Best Times:\n";
                    // iterate through the array again 
                    for (int i = 0; i < bestTimes.length; i++) {
                        // get the minutes and seconds
                        // there are 60000 miliseconds in a minute, so divide to get minutes
                        int mins = bestTimes[i] / 60000;
                        // there are 1000 miliseconds in a seconds, divide, then take a modulus of 60 if greater than 60
                        int secs = (bestTimes[i] / 1000) % 60;
                        // make a string format that puts a 0 in front of the units digit in seconds if less than 10
                        String secsString = String.format("%02d", secs);
                        // add this all to the output string
                        output += mins + ":" + secsString + "\n";
                    }
                    // display using JOptionPane
                    JOptionPane.showMessageDialog(null, output);
                }

            }

        });


    }//GEN-LAST:event_btnPlayCrosswordActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        CreditsWindow creditsW = new CreditsWindow();
        CreditsPanel creditsP = new CreditsPanel();
        creditsW.add(creditsP);
    }//GEN-LAST:event_jButton1ActionPerformed

    //WORDLE STATS
    private void btnStatsWordleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatsWordleActionPerformed
        WordleStatsWindow statsW = new WordleStatsWindow();
        WordleStatsPanel statsP = new WordleStatsPanel(stats);
        statsW.add(statsP);

        //create an instance of JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        //set title
        fileChooser.setDialogTitle("Select or Create a File");

        //allow users to select existing files
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        boolean validFileSelected = false;
        File selectedFile = null;

        while (validFileSelected != true) {
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                //obtain the selected file
                selectedFile = fileChooser.getSelectedFile();

                //check if there is an existing file with same name
                if (selectedFile.isDirectory()) {
                    //display error message
                    JOptionPane.showMessageDialog(null, "Please select a valid file", "Error", JOptionPane.WARNING_MESSAGE);
                } else if (selectedFile.exists()) {
                    validFileSelected = true;

                    //if the file chosen exists, load old stats from it
                    stats.loadFromFile(selectedFile);
                    stats.saveToFile(selectedFile);

                    JOptionPane.showMessageDialog(null, "File chosen successfully.");

                } else {
                    //if the file doesn't exist, create it for the user
                    int confirm = JOptionPane.showConfirmDialog(null, "File does not exist. Would you like to create it?", "Create File", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            if (selectedFile.createNewFile()) {
                                PrintWriter writer = new PrintWriter(new FileWriter(selectedFile));
                                stats.saveToFile(selectedFile);

                                validFileSelected = true;
                                JOptionPane.showMessageDialog(null, "New file created successfully.");

                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to create file.", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } catch (IOException e) {
                            System.out.println(e);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "File selection Canceled");
                        //exit loop
                        break;
                    }

                }
            } else if (userSelection == JFileChooser.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(null, "File creation canceled.");
                //exit the loop
                break;
            }

        }


    }//GEN-LAST:event_btnStatsWordleActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NGTGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NGTGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NGTGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NGTGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NGTGUI_1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPlayConnections;
    private javax.swing.JButton btnPlayCrossword;
    private javax.swing.JButton btnPlayWordle;
    private javax.swing.JButton btnRulesWordle;
    private javax.swing.JButton btnStatsWordle;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel lblConnectionsIcon;
    private javax.swing.JLabel lblCrosswordIcon;
    private javax.swing.JLabel lblNGTLogo;
    private javax.swing.JLabel lblNGTTitle;
    private javax.swing.JLabel lblWordleIcon;
    private javax.swing.JPanel pnlConnections;
    private javax.swing.JPanel pnlConnections2;
    private javax.swing.JPanel pnlCrossword;
    private javax.swing.JPanel pnlCrossword2;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlWordle;
    private javax.swing.JPanel pnlWordle2;
    // End of variables declaration//GEN-END:variables
}

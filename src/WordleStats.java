/*
Emily Yu
January 10, 2025
Wordle Stats Class
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class WordleStats {

    //Statistics
    private int gamesPlayed;
    private int gamesWon;
    private ArrayList<Integer> guessDistribution;
    private File selectedFile;
    private boolean fileAlreadyChosen;

    
    /**
     * Constructor for WordleStats
     */
    public WordleStats() {
        gamesPlayed = 0;
        gamesWon = 0;
        //list holds the number of guesses each "win" took
        guessDistribution = new ArrayList<Integer>();
        fileAlreadyChosen = false;
    }

    
    /**
     * Accessor for gamesPlayed
     * @return - the number of wordles played
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Accessor for gamesWon
     * @return - the number of wordles correctly guessed
     */
    public int getGamesWon() {
        return gamesWon;
    }

    /**
     * Accessor for guessDistribution
     * @return - list containing the number of guesses each "win" took
     */
    public ArrayList<Integer> getGuessDistribution() {
        return guessDistribution;
    }

    /**
     * Mutator for gamesPlayed
     * @param gamesPlayed - the number of wordles played
     */
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /**
     * Mutator for gamesWon
     * @param gamesWon - the number of wordles correctly guessed
     */
    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    /**
     * Mutator for guessDistribution
     * @param guessDistribution - list containing the number of guesses each "win" took
     */
    public void setGuessDistribution(ArrayList<Integer> guessDistribution) {
        this.guessDistribution = guessDistribution;
    }

    /**
     * Accessor for file
     * @return - the file that stats are written to
     */
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * Accessor for fileAlreadyChosen
     * @return - whether or not the user has set a file since opening the program
     */
    public boolean isFileAlreadyChosen() {
        return fileAlreadyChosen;
    }

    /**
     * Mutator for selectedFile
     * @param selectedFile - file that stats are written to
     */
    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    /**
     * Mutator for fileAlreadyChosen
     * @param fileAlreadyChosen - whether or not the user has set a file since opening the program
     */
    public void setFileAlreadyChosen(boolean fileAlreadyChosen) {
        this.fileAlreadyChosen = fileAlreadyChosen;
    }
    
    
    
    

    /**
     * Method saves the current statistics to the file
     *
     * @param fileName - name of the file
     */
    public void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(selectedFile));
            writer.println(gamesPlayed);
            writer.println(gamesWon);
            for (int i = 0; i < guessDistribution.size(); i++) {
                writer.println(guessDistribution.get(i));
            }
            writer.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void loadFromFile() {
        try {
            
            if (!fileAlreadyChosen){

            //create an instance of JFileChooser
            JFileChooser fileChooser = new JFileChooser();

            //set title
            fileChooser.setDialogTitle("Select or Create a Wordle Stats File");

            //allow users to select existing files
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            boolean validFileSelected = false;
            selectedFile = null;

            int userSelection = fileChooser.showOpenDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();

                //if file chosen is valid
                if (selectedFile.exists()) {
                    validFileSelected = true;
                    //display confirmation message
                    JOptionPane.showMessageDialog(null, "File chosen successfully.");
                    fileAlreadyChosen = true;

                } else {
                    //allow user to choose to make a new file
                    //if the file doesn't exist, create it for the user
                    int confirm = JOptionPane.showConfirmDialog(null, "File does not exist. Would you like to create it?", "Create File", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (selectedFile.createNewFile()) {
                            JOptionPane.showMessageDialog(null, "New file created successfully.");
                            fileAlreadyChosen=true;

                        }
                    }
                }

                if (validFileSelected) {
                    //read the file the user chose
                    InputStream wordleStats = new FileInputStream(selectedFile);

                    //initialize scanner
                    Scanner sc = new Scanner(wordleStats);

                    if (sc.hasNextLine()) {
                        //read the first line (total number of games played)
                        gamesPlayed = Integer.parseInt(sc.nextLine());
                        //read the second line (total number of wins)
                        gamesWon = Integer.parseInt(sc.nextLine());
                        //the third line and on are the number of guesses each win took
                        while (sc.hasNextLine()) {
                            guessDistribution.add(Integer.valueOf(sc.nextLine()));
                        }

                    }
                }

            } else {
                //if user denies new file creation
                JOptionPane.showMessageDialog(null, "File selection Canceled");

            }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    /**
     * Method increases the number of games played by 1
     */
    public void increaseGamesPlayed() {
        gamesPlayed++;

    }

    /**
     * Method increases the number of games won by 1
     */
    public void increaseGamesWon() {
        gamesWon++;
    }

    public void updateGuessDistribution(int numGuess) {
        guessDistribution.add(numGuess);
    }

}

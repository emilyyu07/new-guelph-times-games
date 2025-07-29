/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author bobzh
 */
public class MouseListenerLearning {

    // make a 2D board array
    private static CrosswordTile[][] board;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        board = readCrossword();
        int size = board[0][0].getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j].getLetter());
            }
            System.out.println("");
        }
        new MiniCrosswordFrame(board);
    }
    
    /**
     * Read the data file into the CrosswordTile board
     * @return - the new board read into 
     */
    public static CrosswordTile[][] readCrossword(){
        try{
            // Call the file and scanner
            InputStream in = MouseListenerLearning.class.getResourceAsStream("Crossword.txt");
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
            
            int a = 1;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (!b[i][j].getLetter().equals("/")) {
                        if (under(b, i , j) && right(b, i, j)) {
                            b[i][j].setClueNumV(a);
                            b[i][j].setClueNumH(a);
                            a++;
                        } else if (under(b, i, j)) {
                            b[i][j].setClueNumV(a);
                            a++;
                        }
                        else if (right(b, i, j)) {
                            b[i][j].setClueNumH(a);
                            a++;
                        }
                        
                    }
                }
            }
            
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < vClues; k++) {
                        if (b[i][j].getClueNumV() == vClueNums[k]) {
                            b[i][j].setColPrompt(""+vClueNums[k] +"D"+vPrompts[k]);
                        }
                    }
                    for (int k = 0; k < hClues; k++) {
                        if (b[i][j].getClueNumH() == hClueNums[k]) {
                            b[i][j].setRowPrompt(""+hClueNums[k]+"A"+hPrompts[k]);
                        }
                    }
                    if (!b[i][j].getLetter().equals("/")) {
                        if (b[i][j].getClueNumH() == 0) {
                            b[i][j].setRowPrompt(b[i][j-1].getRowPrompt());
                        }
                        if (b[i][j].getClueNumV() == 0) {
                            b[i][j].setColPrompt(b[i-1][j].getColPrompt());
                        }
                    }
                }
            }
            b[0][0].setXPos(hClues);
            b[0][0].setYPos(vClues);
            
            return b;
            
            // if the file is not found return null 
        } catch(Exception e){
            return null;
        }
        
    }
    public static boolean under(CrosswordTile[][] b, int i, int j){
        if (i < b.length-2) {
            if (!b[i][j].getLetter().equals("/") && !b[i+1][j].getLetter().equals("/") && !b[i+2][j].getLetter().equals("/")) {
                if (i == 0) {
                    return true;
                } else {
                    return b[i-1][j].getLetter().equals("/");
                }
            } else{
                return false;
            }
        }else{
            return false;
        }
        
    }
    
    public static boolean right(CrosswordTile[][] b, int i, int j){
        if (j < b[0].length-2) {
            if (!b[i][j].getLetter().equals("/") && !b[i][j+1].getLetter().equals("/") && !b[i][j+2].getLetter().equals("/")) {
                if (j == 0) {
                    return true;
                } else {
                    return b[i][j-1].getLetter().equals("/");
                }
            } else{
                return false;
            }
        }else{
            return false;
        }
    }
}

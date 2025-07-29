
package mouselistenerlearning;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

public class MyFrame extends JFrame implements KeyListener, MouseListener{
    JLabel hint;
    JPanel board = new JPanel();
    JPanel cluePanel = new JPanel();
    JLabel[] hClues, vClues;
    JLabel acrossTitle, downTitle;
    JLabel[][] grid;
    JLabel[][] clueNums;
    JLabel time, winLabel, scoreLabel;
    CrosswordTile[][] b;
    int[] gridCoords;
    int[] vPromptCoords;
    int[] hPromptCoords;
    ArrayList<int[]> blackList = new ArrayList<>();
    ArrayList<int[]> startListD = new ArrayList<>();
    ArrayList<int[]> startListA = new ArrayList<>();
    boolean across = true;
    int x = 0;
    int y = 0;
    int size;
    Color paleBlue = new Color(173,216,230);
    Color paleYellow = new Color(230, 223, 176);
    long start = System.currentTimeMillis();
    /**
     * Primary Constructor
     * @param b - the CrosswordTile[][] (2d array) that read the data of the puzzle
     */
    MyFrame(CrosswordTile[][] b){

        // assign the board to the global variable
        this.b = b;
        size = b[0][0].getSize();
        vClues = new JLabel[b[0][0].getYPos()];
        hClues = new JLabel[b[0][0].getXPos()];
        grid = new JLabel[size][size];
        clueNums = new JLabel[size][size];
        gridCoords = new int[size+1];
        vPromptCoords = new int[b[0][0].getYPos()+1];
        hPromptCoords = new int[b[0][0].getXPos()+1];
        
        
        for (int i = 0; i <= size; i++) {
            gridCoords[i] = i*(500/size);
        }
        for (int i = 0; i <= hClues.length; i++) {
            hPromptCoords[i] = 50+i*(250/hClues.length); 
        }
        for (int i = 0; i <= vClues.length; i++) {
            vPromptCoords[i] = 350 + i*(250/vClues.length);
        }
        // Create the JFrame values
        this.setTitle("Mini Crossword");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500,1000);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        // Set values for the JPanel that holds the grid
        board.setBounds(200,200,500,500);
        board.setLayout(null);
        board.setOpaque(true);
        
        // Set values for the JPanel that holds the grid
        cluePanel.setBounds(750,100,600,600);
        cluePanel.setLayout(null);
        cluePanel.setOpaque(true);
        
        Font section = new Font("Times New Roman", Font.BOLD, 24);
        Font words = new Font("Times New Roman", Font.PLAIN, 20);
        acrossTitle = new JLabel("ACROSS");
        acrossTitle.setBounds(0,0,600, 45);
        acrossTitle.setOpaque(true);
        acrossTitle.setFont(section);
        downTitle = new JLabel("DOWN");
        downTitle.setBounds(0,300,600, 45);
        downTitle.setOpaque(true);
        downTitle.setFont(section);
        cluePanel.add(acrossTitle);
        cluePanel.add(downTitle);
        
        // create borders for the labels
        Border blackLine = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(50/size,50/size, 0, 0);
        Border border = new CompoundBorder(blackLine, margin);
        hint = new JLabel();
        hint.setBounds(200, 100, 500, 90);
        hint.setBackground(paleBlue);
        hint.setOpaque(true);
        Font font2 = new Font("Times New Roman",Font.BOLD, 24);
        hint.setFont(font2);
        hint.setBorder(margin);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(hint);
        int k = 0;
        int l = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Font font1 = new Font("Times New Roman", Font.PLAIN, 375/size);
                Font font3 = new Font("Times New Roman", Font.BOLD, 90/size);
                grid[i][j] = new JLabel();
                grid[i][j].setBounds(j*(500/size),i*(500/size),(500/size), (500/size));
                grid[i][j].setOpaque(true);
                grid[i][j].setBorder(border);
                if (b[i][j].getWord().equals("/")) {
                    grid[i][j].setBackground(Color.BLACK);
                    b[i][j].setColor(Color.BLACK);
                    grid[i][j].setText("/");
                    int[] coords = {i,j};
                    blackList.add(coords);
                } else{
                    grid[i][j].setBackground(Color.WHITE);
                    b[i][j].setColor(Color.WHITE);
                    grid[i][j].setFont(font1);
                }
                
                clueNums[i][j] = new JLabel();
                clueNums[i][j].setBounds(25/size+j*(500/size),25/size+ i*(500/size),125/size, 75/size);
                clueNums[i][j].setOpaque(true);
                clueNums[i][j].setFont(font3);
                if (!b[i][j].getColor().equals(Color.BLACK)) {
                    clueNums[i][j].setBackground(Color.WHITE);
                    if (b[i][j].getClueNumV() != 0) {
                        clueNums[i][j].setText(""+b[i][j].getClueNumV());
                        int[] clueCoord = {i,j};
                        startListD.add(clueCoord);
                        vClues[k] = new JLabel(""+b[i][j].getColPrompt().replaceFirst("D", " "));
                        vClues[k].setBounds(10, 350+(250/vClues.length)*k, 590, 250/vClues.length);
                        vClues[k].setOpaque(true);
                        vClues[k].setFont(words);
                        
                        cluePanel.add(vClues[k]);
                        k++;
                        
                    }
                    if (b[i][j].getClueNumH() != 0) {
                        clueNums[i][j].setText(""+b[i][j].getClueNumH());
                        int[] clueCoord = {i,j};
                        startListA.add(clueCoord);
                        hClues[l] = new JLabel(""+b[i][j].getRowPrompt().replaceFirst("A", " "));
                        hClues[l].setBounds(10, 50+(250/hClues.length)*l, 590, 250/hClues.length);
                        hClues[l].setOpaque(true);
                        hClues[l].setFont(words);
                        cluePanel.add(hClues[l]);
                        l++;
                    }
                    
                }else{
                    clueNums[i][j].setBackground(Color.BLACK);
                }
                board.add(clueNums[i][j]);
                board.add(grid[i][j]);
                
                
                
            }
        }
        
        // add a MouseListener that tracks when the mouse is pressed
        board.addMouseListener(new MouseAdapter(){
            
            @Override
            /**
             * MousePressed abstract method that detects where the user first
             * clicks and makes that label yellow, while recording the 
             * coordinates relative to the grid
             */
            public void mousePressed(MouseEvent e){
                
                // two for loops to get every spot in the grid
                for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (!b[i][j].getColor().equals(Color.BLACK)) {
                                if (e.getX() < gridCoords[j+1] && e.getX() > gridCoords[j] && 
                                    e.getY() < gridCoords[i+1] && e.getY() > gridCoords[i]) {
                                    if (b[i][j].getColor().equals(paleYellow)) {
                                        across = !across;
                                    }                                  
                                    
                                    x = i;
                                    y = j;
                                }
                            }

                            
                        }
                    }
                drawSelector();
                updateHint();
            } 
        });
        
        cluePanel.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                for (int i = 0; i < vClues.length; i++) {
                    if (e.getY() < vPromptCoords[i+1] && e.getY() > vPromptCoords[i]) {
                        vClues[i].setBackground(paleBlue);
                        across = false;
                        x = startListD.get(i)[0];
                        y = startListD.get(i)[1];
                        drawSelector();
                        updateHint();
                    } else{
                        vClues[i].setBackground(null);
                    }
                
                }
                for (int i = 0; i < hClues.length; i++) {
                    if (e.getY() < hPromptCoords[i+1] && e.getY() > hPromptCoords[i]) {
                        across = true;
                        hClues[i].setBackground(paleBlue);
                        x = startListA.get(i)[0];
                        y = startListA.get(i)[1];
                        drawSelector();
                        updateHint();
                    } else{
                        hClues[i].setBackground(null);
                    }
                
                }
            }
        });
        this.add(board);
        this.add(cluePanel);
        this.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                int keyCode = e.getKeyChar();
                if (keyCode == KeyEvent.VK_BACK_SPACE) {
                    grid[x][y].setText("");
                    moveSelect(false);
                } else{
                    char c = Character.toUpperCase(e.getKeyChar());
                    grid[x][y].setText(""+c);
                    if (checkBoard()) {
                        
                        winScreen(time.getText());
                    }else{
                        moveSelect(true);
                    }
                    
                }
                
                
                
                drawSelector();
                updateHint();
                
            }

            
        });
        
        this.setVisible(true);
        runBoard();
        
    }
    public void drawSelector(){
        
        if (across) {
            String rowPrompt = b[x][y].getRowPrompt();
            if (rowPrompt == null) {
                across = !across;
                drawSelector();
            }else{
                for (int i = 0; i < size; i++) {

                    for (int j = 0; j < size; j++) {
                        if (!rowPrompt.equals(b[i][j].getRowPrompt())) {
                           b[i][j].setColor(Color.WHITE); 
                        }else{
                            b[x][j].setColor(paleBlue);
                        }


                    }
                }
                b[x][y].setColor(paleYellow);
                updateCluePanel();
        
            }
            
            
            
        }else{
            String colPrompt = b[x][y].getColPrompt();
            if (colPrompt == null) {
                across = !across;
                drawSelector();
            }else{
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (!colPrompt.equals(b[i][j].getColPrompt())) {
                           b[i][j].setColor(Color.WHITE); 
                        }else{
                            b[i][y].setColor(paleBlue);
                        }


                    }
                }
                b[x][y].setColor(paleYellow);
                updateCluePanel();
        
            }
        }
        for (int[] coords: blackList) {

            grid[coords[0]][coords[1]].setBackground(Color.BLACK);
            b[coords[0]][coords[1]].setColor(Color.BLACK);
            clueNums[coords[0]][coords[1]].setBackground(Color.BLACK);
        }
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {              
                clueNums[i][j].setBackground(b[i][j].getColor());
                grid[i][j].setBackground(b[i][j].getColor());
            }
        }
       this.repaint();
        
    }
    public void runBoard(){
        time = new JLabel();
        time.setBounds(425, 75, 50, 25);
        time.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        time.setOpaque(true);
        this.add(time);
        while(!checkBoard()){
            long end = System.currentTimeMillis();
            long elapsed = (end - start)/1000;
            long mins = elapsed / 60;
            long secs = elapsed % 60;
            if (secs < 10) {
               time.setText(mins+":0"+secs); 
            } else{
                time.setText(mins+":"+secs);
            }
            
        }
        
    }
    public void updateHint(){
        if (across) {
            hint.setText("<html><p>" + b[x][y].getRowPrompt()+"</p></html>");
        } else{
            hint.setText("<html><p>" +b[x][y].getColPrompt()+"</p></html>");
        }
    }
    public void updateCluePanel(){
        
        if (!across) {
            for (int i = 0; i < vClues.length; i++) {
                b[x][y].setColPrompt(b[x][y].getColPrompt().replaceFirst("D", " "));
                if (b[x][y].getColPrompt().equals(vClues[i].getText())) {
                    vClues[i].setBackground(paleBlue);
                } else{
                    vClues[i].setBackground(null);
                }
                b[x][y].setColPrompt(b[x][y].getColPrompt().replaceFirst(" ", "D"));
                
            }
            for (int j = 0; j < hClues.length; j++) {
                hClues[j].setBackground(null);
            }
        }else{
            for (int i = 0; i < hClues.length; i++) {
                b[x][y].setRowPrompt(b[x][y].getRowPrompt().replaceFirst("A", " "));
                if (b[x][y].getRowPrompt().equals(hClues[i].getText())) {
                    hClues[i].setBackground(paleBlue);
                } else{
                    hClues[i].setBackground(null);
                }
                b[x][y].setRowPrompt(b[x][y].getRowPrompt().replaceFirst(" ", "A"));
            }
            for (int j = 0; j < vClues.length; j++) {
                    vClues[j].setBackground(null);
            }
            
        }
        
        
    }
    public void moveSelect(boolean forward){
        if (forward) {
            if (across) {
                if (y < grid[0].length-1) {
                    y++;
                } else if(x < grid.length-1){
                    y = 0;
                    x++;
                } else{
                    x=0;
                    y=0;
                }
            } else{
                if (x < grid.length-1) {
                    x++;
                } else if(y < grid[0].length-1){
                    x = 0;
                    y++;
                } else{
                    x = 0;
                    y = 0;
                }
            }
            for (int[] coord: blackList) {
                if (coord[0] == x && coord[1] == y) {
                    moveSelect(true);
                }
            }
            boolean filled = true;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (grid[i][j].getText().equals("")) {
                        filled = false;
                    }
                }
            }
            if (filled) {
                JOptionPane.showMessageDialog(null, "Not Quite", "Mini Crossword", JOptionPane.ERROR_MESSAGE);
            }
            else if (!grid[x][y].getText().equals("")){
                moveSelect(true);
            }
            
        }else{
            if (across) {
                if (y > 0) {
                    y --;
                }else if (x > 0) {
                    y = grid[0].length-1;
                    x--;
                } 
                
            } else{
                if (x > 0) {
                    x --;
                } else if (y > 0) {
                    x = grid.length-1;
                    y--;
                }
                
                
            }
            for (int[] coord: blackList) {
                if (coord[0] == x && coord[1] == y) {
                    moveSelect(false);
                }
            }
        }
        
    }
    public boolean checkBoard() {
        boolean won = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                
                if (!b[i][j].getWord().equals(grid[i][j].getText())) {
                    won = false;
                    
                    
                }
                
            }
        }
        
        return won;

    }
    
    public void endGame(){
        System.out.println("not quiet");
    }
    public void winScreen(String time){
        

    }
    

    @Override
    public void mouseClicked(MouseEvent e) {
        //
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("released");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("exited");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        hint.setText(""+e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("You pressed key char: "+e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("You released key char: "+e.getKeyChar());
    }
}


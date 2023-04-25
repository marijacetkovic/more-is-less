// import libraries
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;

// define a GameField class which extends JPanel class
public class GameField extends JPanel {

    // variables to hold number rows, columns, targ val, sum, moves
    int n, m, targetValue, currentSum, numOfMoves;

    // variables to hold references to two buttons of the game field
    GameButton buttonA, buttonB;

    // 2D GameButton array to store all the buttons on the game field
    GameButton[][] buttons = new GameButton[n][m];

    // create a new JLabel to display the current sum of the selected buttons
    JLabel currentSumLabel = new JLabel(String.valueOf(currentSum));

    // create a new JLabel to display the target value
    JLabel targetLabel = new JLabel();

    // create new JPanels for the main field, right panel and left panel
    JPanel gamePanel, rightPanel, leftPanel;

    // string array to hold the different operations that can be performed on the buttons
    String[] operations = {"+","-","*","/"};
    String currentOperation;

    // array of JLabels to display the different operations
    JLabel[] operationLabels ;

    // create random for generating random numbers
    Random rand = new Random();

    // constructor for the GameField class
    GameField() {

        // set the preferred size of the GameField
        this.setPreferredSize(new Dimension(800,500));

        // set the layout of GameField to BorderLayout with horizontal and vertical gaps of 12 pixels
        this.setLayout(new BorderLayout(12,12));

        // set the background color of GameField
        this.setBackground(new Color(23));

        // creates a new JPanel for the main game panel where the matrix of GameButtons is
        this.gamePanel = new JPanel();
        this.n=10;
        this.m=10;
        this.gamePanel.setLayout(new GridLayout(n,m));
        this.buttons = new GameButton[n][m];
        //generate the matrix again
        generateButtons(n,m);
        //update target, by default normal mode
        updateTarget(currentSum+rand.nextInt(currentSum/3),50);
        this.gamePanel.revalidate();


        //---------------------------------------------------------------------------------------
        //right panel
        //--------------------------------------------------------------------------------------

        //initialize right panel which holds operations and set its size
        rightPanel=new JPanel(new GridLayout(4,1));
        rightPanel.setPreferredSize(new Dimension(50, 500));

        //initialize opLabels array of JLabels of length 4
        operationLabels = new JLabel[4];

        //loop through the operations array
        for (int i = 0; i < 4; i++) {

            //initialize the operation label with the corresponding (same index) operation from the operations array
            operationLabels[i]=new JLabel(operations[i]);
            //set the color of the label to red, set font and add the label to the panel
            operationLabels[i].setForeground(new Color(0xF30606));
            operationLabels[i].setFont(new Font("ARIAL", Font.PLAIN, 30));
            rightPanel.add(operationLabels[i]);
        }
        //add the right panel to the gamefield layout
        this.add(rightPanel,BorderLayout.EAST);
        //---------------------------------------------------------------------------------------


        //left panel
        //---------------------------------------------------------------------------------------

        // initialize left JPanel
        leftPanel=new JPanel();
        // set size to panel
        leftPanel.setPreferredSize(new Dimension(90,500));
        // set layout
        leftPanel.setLayout(new FlowLayout());
        // initialize JButtons
        JButton newGame = new JButton("New Game");
        JButton saveGame = new JButton("Save game");
        JButton loadGame = new JButton("Load game");

        //source: https://www.javatpoint.com/java-jmenuitem-and-jmenu
        //initialize menu
        JMenu optionsMenu = new JMenu("Options");

        //add buttons to menu, menu accepts menuitems
        JMenuItem setSize = new JMenuItem("Set size");
        JMenuItem numMoves = new JMenuItem("Set number of moves");
        JMenuItem targVal = new JMenuItem("Set target value");
        JMenuItem chooseDiff = new JMenuItem("Choose difficulty");

        //add them to menu
        optionsMenu.add(setSize);
        optionsMenu.add(numMoves);
        optionsMenu.add(targVal);
        optionsMenu.add(chooseDiff);

        //menu must be added to menubar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(0xCBC2E7));
        menuBar.add(optionsMenu);

        //set color to everything
        newGame.setBackground(new Color(0xCBC2E7));
        setSize.setBackground(new Color(0xCBC2E7));
        numMoves.setBackground(new Color(0xCBC2E7));
        targVal.setBackground(new Color(0xCBC2E7));
        chooseDiff.setBackground(new Color(0xCBC2E7));
        saveGame.setBackground(new Color(0xCBC2E7));
        loadGame.setBackground(new Color(0xCBC2E7));

        //---------------------------------------ACTIONLISTENERS--------------------------------------
        //add act listeners to all the buttons/menuitems

        newGame.addActionListener(e -> {
            //display to user that the game will be restarted
            String message = "The game will be restarted.";
            JOptionPane.showMessageDialog(null, message, "New Game", JOptionPane.INFORMATION_MESSAGE);
            //call restart method
            restart();
        });


        setSize.addActionListener(e -> {
            //remove everything that was beforehand on gamepanel
            this.gamePanel.removeAll();
            //prompt user to enter desired row and column
            int newRows = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter desired number of rows"));
            int newCols = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter desired number of columns"));

            //update the size of the grid
            this.n = newRows;
            this.m = newCols;
            //update layout and initialize new sized matrix
            this.gamePanel.setLayout(new GridLayout(n,m));
            this.buttons = new GameButton[n][m];
            //generate the matrix again
            generateButtons(n,m);
            //update target, by default normal mode
            updateTarget(currentSum+rand.nextInt(currentSum/3),50);
            //revalidate so everything shows up correctly
            this.gamePanel.revalidate();
        });

        numMoves.addActionListener(e -> {
            //prompts user to enter desired numofMoves, parses it to int
            int numOfMoves = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter desired number of moves"));
            //updates the target label with the new number of moves
            updateTarget(targetValue,numOfMoves);
        });

        targVal.addActionListener(e -> {
            //prompts user to enter desired target value, parses it to int
            int targetValue = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter desired target value"));
            //updates the target label with the new target value
            updateTarget(targetValue,targetValue);
        });

        chooseDiff.addActionListener(e -> {
            //prompts user to enter desired difficulty
            String difficulty = JOptionPane.showInputDialog(null,"EASY, NORMAL, HARD?");
            //switch case according to difficulty
            switch(difficulty) {
                //for easy targ value is at most currsum+ currsum/4, normal currsum/3, hard currsum/2
                case "EASY":
                    updateTarget(currentSum+rand.nextInt(currentSum/4),80);
                    break;
                case "NORMAL":
                    updateTarget(currentSum+rand.nextInt(currentSum/3),50);
                    break;
                case "HARD":
                    updateTarget(currentSum+rand.nextInt(currentSum/2),30);
                    break;
                default: break;
            }
        });

        saveGame.addActionListener(e -> {
            //call save game state method
            saveGameState();
        });

        loadGame.addActionListener(e -> {
            //call load game state method
            loadGameState();
        });

        //----------------------------------------------------------------------------------------------------

        //add components to leftpanel
        leftPanel.add(menuBar);
        leftPanel.add(newGame);
        leftPanel.add(saveGame);
        leftPanel.add(loadGame);
        //add panel to gameframe
        this.add(leftPanel,BorderLayout.WEST);

    }//endconstructor

    //--------------------------------METHODS------------------------------------------------
    //---------------------------------------------------------------------------------------


    //generate buttons function which takes in the dimensions of the grid (x and y)
    public void generateButtons(int x, int y){
        //initialize current sum to 0
        this.currentSum=0;
        //loop through rows
        for (int i = 0; i < x; i++) {
            //loop through columns
            for (int j = 0; j < y; j++) {
                //create new button at coordinates (i,j) and add it to the buttons array
                buttons[i][j] = new GameButton(i, j, this);
                //add buttons to the game panel
                this.gamePanel.add(buttons[i][j]);
                //sum up all buttons
                this.currentSum += buttons[i][j].number;
            }
        }
        //add the game panel with buttons to the center of the GameField
        this.add(this.gamePanel,BorderLayout.CENTER);

        //update the current sum label to show the current sum
        this.currentSumLabel.setText("Current sum: "+currentSum);

        //set the text color, size of the label and add it to bottom of gameField
        this.currentSumLabel.setForeground(new Color(0x7A7ABE));
        this.currentSumLabel.setFont(new Font(null, 0, 18));
        this.add(currentSumLabel,BorderLayout.SOUTH);
    }//endgeneratebuttons


    //playing function
    public void playing(GameButton currentButton) {

        // if were starting the game buttonA is empty
        if (buttonA == null) {
            //if its empty set it to current button
            buttonA = currentButton;
            //make it select the first operation that will be performed once button B is clicked
            currentOperation = getOp();
            //set that operation to green
            select(currentOperation);

        } else {
            //if buttonA alr exists, set currentButton to buttonB
            buttonB = currentButton;

            //my logic was that button cannot perform operation on itself so, return playing function when buttonA==buttonB
            if (buttonA==buttonB){
                return;
            }

            //remove buttonA value from current sum and add it again when its changed
            currentSum -= buttonA.number;

            //perform operation
            performOp(currentOperation);

            //add value again
            currentSum += buttonA.number;

            //decrease numofmoves
            numOfMoves--;

            //displaycurrent sum
            currentSumLabel.setText("Current sum: "+currentSum);
            //update targetlabel
            updateTarget(targetValue,numOfMoves);

            //--------------------------WIN LOSE CONDITION-------------------------------------------

            //if current sum equals targ value user won
            if(currentSum==targetValue){
                //display him the message
                String message = "Yaaay! You won the game!";
                JOptionPane.showMessageDialog(null,message,"YOU WINNN",JOptionPane.INFORMATION_MESSAGE);
                //end the program
                System.exit(0);
            }
            //if he lost moves and went over target value he lost, display message
            else if (numOfMoves==0 && currentSum>targetValue){
                JOptionPane.showMessageDialog(null,"You LOST!","No moves left.",JOptionPane.INFORMATION_MESSAGE);
                //restart so he can play again
                restart();
            }
            //if he lost moves display difference targetvalue-sum
            else if (numOfMoves<=0){
                String message = "No moves left. Difference between target value and current sum was "+(targetValue-currentSum);
                JOptionPane.showMessageDialog(null,message,"THE END",JOptionPane.INFORMATION_MESSAGE);
                //play again
                restart();
            }


            //when button B is pressed and operation is performed, then button B becomes buttonA i.e.
            //first button that will get its label changed according to operation performed
            buttonA = buttonB;
            //empty button B - make it null , so the next button selected can take its place
            buttonB = null;
            //unselect all labels
            unSelect();
            ///get a new operation that will be used when the next button is clicked
            currentOperation=getOp();
            //make it green - visible to user
            select(currentOperation);

        }//endelse

        //-------------------------ROWCOLUMN CHECK-----------------------------------------------
        //---------------------------------------------------------------------------------------

        for(int i=0; i < n; i++) { // goes to num of rows
            for (int j = 0; j < m; j++) { //to num of columns
                //if not in row and column of currentButton disable them and change background
                if (i != currentButton.row && j != currentButton.column) {
                    buttons[i][j].setEnabled(false);
                    buttons[i][j].setBackground(new Color(0xA0AC99DA));
                }
                else {//if it is in row and column of selected button enable them and change background
                    buttons[i][j].setEnabled(true);
                    buttons[i][j].setBackground(new Color(0x695897));
                }

            }//endinnerfor
        }//endouterfor


    }//endplaying



    //function used to update target label
    public void updateTarget(int targetValue, int numOfMoves){
        //set target value, numofmoves to parameters supplied
        this.targetValue = targetValue;
        this.numOfMoves = numOfMoves;
        //set font foreground and text of target label
        this.targetLabel.setFont(new Font(null, 0, 18));
        this.targetLabel.setForeground(new Color(0x7A7ABE));
        this.targetLabel.setText("Target value:"+targetValue+"                         " +
                "                                                                 " +
                "                                                        Number of moves:"+numOfMoves);
        //and add it to the gamefield
        this.add(targetLabel,BorderLayout.NORTH);
    }//endupdatetarget

    // function to get a random operation
    public String getOp(){
        // return operation at random index 0-3 from operation array
        return operations[rand.nextInt(4)];
    }

    // function to perform operation
    public void performOp(String operation){
        //switch case statement
        //for every operation change the formula
        switch(operation) {
            case "+":
                buttonA.setNumber((buttonB.number + buttonA.number) % 10);
                break;
            case "-":
                buttonA.setNumber((buttonA.number - buttonB.number) % 10);
                break;
            case "*":
                buttonA.setNumber((buttonB.number * buttonA.number) % 10);
                break;
            case "/":
                if (buttonB.number!=0) {
                    buttonA.setNumber((buttonA.number / buttonB.number) % 10);
                }
                // if trying to divide with 0 do nothing
                else break;
                break;
            default:
                break;
        }//endswitchcase

    }//endperformop

    //function to make all labels go red i.e. unselect operation labels
    public void unSelect(){
        for (int i = 0; i < 4; i++) {
            operationLabels[i].setForeground(new Color(0xF30606));
        }
    }//endunselect

    //function to select label i.e. change it to green
    public void select(String operation){
        //loop through operation array, if it equals to the supplied operation
        //change operationlabel at same index to green and break for loop
        for (int i = 0; i < operations.length; i++) {
            if (operations[i].equals(operation)) {
                operationLabels[i].setForeground(new Color(0xC872D907));
                break;
            }
        }//endfor
    }//endselect

    // method to save the current game state
    public void saveGameState() {
        try {

            String fileName = "game-state.txt";
            // Create a FileWriter and BufferedWriter to write to the file
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);

            // Write the current state of the game to the file

            bw.write(n+","+m+","+ targetValue+","+currentSum+ ","+numOfMoves);
            bw.newLine();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    bw.write(buttons[i][j].getNumber() + ",");
                }
                bw.newLine();
            }

            // Close the writers to save the file
            bw.close();
            fw.close();

            // Show a message to the user to confirm the save
            JOptionPane.showMessageDialog(null, "Game saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end savegamestate

    public void loadGameState(){
        int[][] data= null;
        try{
            //initialize buffered reader and set file name to game state.txt
            BufferedReader bufferedReader = new BufferedReader(new FileReader("game-state.txt"));
            //store first line
            String firstLine              = bufferedReader.readLine();
            //our desired variables are at positions 0,1,2,3,4 respectively
            //parse them to int and split the string at ','
            int nLoad                         = Integer.parseInt( firstLine.split(",")[0]);
            int mLoad                         = Integer.parseInt(firstLine.split(",")[1]);
            int targetLoad = Integer.parseInt(firstLine.split(",")[2]);
            int sumLoad = Integer.parseInt(firstLine.split(",")[3]);
            int movesLoad = Integer.parseInt(firstLine.split(",")[4]);
            //show user success
            JOptionPane.showMessageDialog(null, "Game loaded successfully!");

            //set rows and columns to extracted from file
            this.n = nLoad;
            this.m = mLoad;
            //remove all from before
            this.gamePanel.removeAll();

            // update the size of the grid
            this.gamePanel.setLayout(new GridLayout(n,m));
            this.buttons = new GameButton[n][m];
            //generate buttons again
            generateButtons(n,m);
            updateTarget(targetLoad,movesLoad);
            this.currentSum= sumLoad;
            this.gamePanel.repaint();
            this.gamePanel.revalidate();
            data = new int [n][m];
            for(int i=0; i<n; i++){
                String line = bufferedReader.readLine();
                String[] tokens = line.split(",");
                for(int j=0; j<tokens.length; j++){
                    data[i][j] = Integer.parseInt(tokens[j]);
                    buttons[i][j].setText(String.valueOf(data[i][j]));
                }//end inner for
            }//end outer for

        }catch (IOException e){
            e.printStackTrace();
        }//end of catch

    }//endloadgame

    //source for savegame and loadgame: lecture source code

    public void restart(){
        Main.frame.dispose();
        Main.main(null);
    }

}//endclass

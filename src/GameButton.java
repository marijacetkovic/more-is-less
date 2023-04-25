//import libraries
import javax.swing.*;
import java.awt.*;
import java.util.Random;

// define a GameButton class which extends JButton class
public class GameButton extends JButton {

    // create random to generate random numbers
    Random rand = new Random();
    // variable to hold the random number assigned to the button
    int number;
    // variables to hold the row and column position of the button on the game field
    int row, column;
    // variable to hold a reference to the GameField class
    GameField GameField;
    // constructor for the GameButton class, which takes in the row, column, and GameField as parameters
    GameButton(int row, int column, GameField GameField){

        // sets the size of the button
        this.setSize(100,60);

        // assigns the GameField parameter to GameField variable of GameButton
        this.GameField = GameField;

        // set a random number between 0 and 9 and assign it to number
        this.number=rand.nextInt(10);

        // set the background color
        this.setBackground(new Color(0xA0AC99DA));

        // set the text color of button
        this.setForeground(new Color(0xFFFFFF));

        // set the text of the button to string value of number
        this.setText(String.valueOf(number));

        // set row parameter to row variable of GameButton
        this.row = row;

        // set column parameter to column variable of GameButton
        this.column = column;

        // adds an action listener to the button, which calls the playing method of the GameField class when the button is clicked
        this.addActionListener(e -> {
            GameField.playing(this);

        });
    }

    // set number method
    public void setNumber(int number){
        // set the number as well as text on button
        this.number = number;
        this.setText(String.valueOf(number));
    }

    // get number method
    public int getNumber() {
        // return number of game button
        return number;
    }
}

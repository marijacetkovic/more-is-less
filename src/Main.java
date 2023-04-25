// import libraries
import javax.swing.*;
import java.awt.*;

public class Main {
    // declare a frame that will be used to display the game
    // static for easier access by gamefield class
    public static JFrame frame;

    public static void main(String[] args) {

        // initialize a new jframe with a title
        frame = new JFrame("More Or Less");

        // set size
        frame.setPreferredSize(new Dimension(1100,700));

        // add object of GameField class to the frame
        frame.add(new GameField());

        // disable resizing the frame
        frame.setResizable(false);

        // pack frame and make it visible
        frame.pack();
        frame.setVisible(true);

        // set default close operation to exit the program when frame is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }//endmain
}//endclass
import javax.swing.*;
import java.awt.*;

/**
 * Class to create an option pane to be displayed when a game is complete.
 * A message will be displayed depending on win/loss with a button allowing the
 * player to play again or quit the game.
 * @author Amber Smith
 */
public class GameEndGUI {

    /** images to display for a win or loss */
    private static final ImageIcon winImage = new ImageIcon(GameEndGUI.class.getResource("/HappyLorax.png"));
    private static final ImageIcon lossImage = new ImageIcon(GameEndGUI.class.getResource("/SadLorax.png"));

    /**
     * Open an option pane that will display if game was won/lost, and allow
     * the player to start a new game or close the program
     * @param gameWon whether the game was won
     * @return option pane
     */
    public static int showGameEnd(boolean gameWon) {
        String message;
        ImageIcon image;
        if (gameWon) {
            message = "You Win! The Lorax Would be Proud :')";
            image = winImage;
        } else {
            message = "Don't Chop Down the Truffula Tree!";
            image = lossImage;
        }

        Object[] options = {"Play Again", "Close Game"};
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));

        return JOptionPane.showOptionDialog(
                null,
                messageLabel,
                gameWon ? "You Win!!!" : "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                image,
                options,
                options[1]
        );
    }
}

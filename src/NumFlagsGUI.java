import javax.swing.*;
import java.awt.*;

/**
 * Class to create a panel for the number of flags. The panel will display how
 * many flags are remaining, which is the total number of mines minus the flags
 * already placed.
 * @author Amber Smith
 */
public class NumFlagsGUI {

    /** panel and label for the number of flags used */
    private final JPanel flags = new JPanel();
    private final JLabel flagLabel;

    /** stores the number of flags currently on the board */
    int numFlagsUsed;

    /**
     * Used to make the number of flags panel that adjusts for flags used
     * @param color color of panel
     * @param frameWidth used to determine the size of the panel
     * @param numFlags starting number of flags
     */
    public NumFlagsGUI(Color color, int frameWidth, int numFlags) {
        flagLabel = new JLabel(numFlags + " Flags");
        flagLabel.setFont(new Font("Arial", Font.BOLD, 24));
        flags.add(flagLabel);
        flags.setBackground(color);
        flags.setPreferredSize(new Dimension(frameWidth/4,50));

        this.numFlagsUsed = numFlags;
    }

    /**
     * Change the number of flags displayed based on action
     * @param flagPlaced whether the flag was placed or removed
     */
    public void editFlags(boolean flagPlaced) {
        if (flagPlaced) {
            numFlagsUsed--;
        } else {
            numFlagsUsed++;
        }
        flagLabel.setText(numFlagsUsed + " Flags");
        flags.repaint();
    }

    /**
     * get the panel to display the number of flags used
     * @return number of flags panel
     */
    public JPanel getPanel() {
        return flags;
    }
}
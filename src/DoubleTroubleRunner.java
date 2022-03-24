import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DoubleTroubleRunner {
    static JFrame frame = new JFrame("Double Trouble");

    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new JPanel();
        newContentPane.setLayout(new BoxLayout(newContentPane, BoxLayout.Y_AXIS));
        JLabel header = new JLabel("Double Trouble");
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setFont(new Font("", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        JButton playGameButton = new JButton("Play Game");
        playGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playGameButton.setBorder(border);
        playGameButton.addActionListener(actionEvent -> chooseStartPlayerScreen(false));

        JButton playTournamentButton = new JButton("Play Tournament");
        playTournamentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playTournamentButton.setBorder(border);
        playTournamentButton.addActionListener(actionEvent -> chooseStartPlayerScreen(true));

        newContentPane.add(header);
        newContentPane.add(playGameButton);
        newContentPane.add(playTournamentButton);

        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }

    private static void chooseStartPlayerScreen(boolean isTournament) {
        JComponent newContentPane = new JPanel();
        newContentPane.setLayout(new BoxLayout(newContentPane, BoxLayout.Y_AXIS));
        JLabel header = new JLabel("Double Trouble");
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setFont(new Font("", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        JButton playerButton = new JButton("Player First");
        playerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerButton.setBorder(border);
        playerButton.addActionListener((event) -> startGame(false, isTournament));

        JButton computerButton = new JButton("Computer First");
        computerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        computerButton.setBorder(border);
        computerButton.addActionListener((event) -> startGame(true, isTournament));

        newContentPane.add(header);
        newContentPane.add(playerButton);
        newContentPane.add(computerButton);

        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);

    }

    private static void startGame(boolean computerFirst, boolean isTournament) {
        DoubleTroubleGame.createAndShowGUI(frame, isTournament, computerFirst, 0, 0);
    }
}

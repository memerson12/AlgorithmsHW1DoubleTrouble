import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

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
        playGameButton.addActionListener(DoubleTroubleRunner::startGame);

        JButton playTournamentButton = new JButton("Play Tournament");
        playTournamentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playTournamentButton.setBorder(border);
        playTournamentButton.addActionListener(DoubleTroubleRunner::startTournament);

        newContentPane.add(header);
        newContentPane.add(playGameButton);
        newContentPane.add(playTournamentButton);

        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }

    private static void startGame(ActionEvent actionEvent) {
        DoubleTroubleGame.createAndShowGUI(frame, false, 0, 0);
    }

    private static void startTournament(ActionEvent actionEvent) {
        DoubleTroubleGame.createAndShowGUI(frame, true, 0, 0);
    }
}

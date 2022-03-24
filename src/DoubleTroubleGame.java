import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

enum GameColors {
    GREEN,
    YELLOW,
    ORANGE
}

public class DoubleTroubleGame extends JPanel implements ActionListener {
    private static final ArrayList<ArrayList<GameTile>> uncapturedGameTiles = new ArrayList<>();
    private static final ArrayList<GameTile> uncapturedGreenTiles = new ArrayList<>();
    private static final ArrayList<GameTile> uncapturedYellowTiles = new ArrayList<>();
    private static final ArrayList<GameTile> uncapturedOrangeTiles = new ArrayList<>();
    private static JFrame mainFrame;
    private static Random rand;
    private static boolean isTournament;
    private static boolean computerFirst;
    private static int computerScore;
    private static int playerScore;
    private final ArrayList<GameTile> currentlySelectedTiles = new ArrayList<>();
    private GameColors currentlySelectedColor = null;

    public DoubleTroubleGame(boolean isTournament, boolean computerFirst, int computerScore, int playerScore) {
        super(new BorderLayout());
        this.isTournament = isTournament;
        this.computerScore = computerScore;
        this.playerScore = playerScore;
        this.computerFirst = computerFirst;
        rand = new Random();
        uncapturedGameTiles.add(uncapturedGreenTiles);
        uncapturedGameTiles.add(uncapturedYellowTiles);
        uncapturedGameTiles.add(uncapturedOrangeTiles);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        if (isTournament) {
            JLabel score = new JLabel("Computer: " + computerScore + " | You " + playerScore);
            score.setAlignmentX(Component.CENTER_ALIGNMENT);
            score.setFont(new Font("", Font.BOLD, 20));
            score.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
            add(score, BorderLayout.CENTER);
        }
        add(buttonsPanel, BorderLayout.CENTER);
        for (int row = 0; row < 5; row++) {
            JPanel gamePanel = new JPanel(new FlowLayout());
            for (int col = 0; col < row + 1; col++) {
                GameTile tile = new GameTile(row, col, uncapturedGameTiles);
                tile.addActionListener(this);
                gamePanel.add(tile);
            }
            buttonsPanel.add(gamePanel);
        }
        Button doneButton = new Button("End Turn");
        doneButton.addActionListener((actionEvent) -> {
            if (currentlySelectedTiles.size() == 0) return;
            currentlySelectedTiles.forEach(GameTile::capture);
            currentlySelectedColor = null;
            currentlySelectedTiles.clear();
            enableAllUncaptured();
            if (uncapturedGameTiles.size() <= 0) endGame("You");
            else computerPlay();
        });
        buttonsPanel.add(doneButton);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public static void createAndShowGUI(JFrame MainFrame, boolean isTournament, boolean computerFirst, int computerScore, int playerScore) {
        //Create and set up the content pane.
        mainFrame = MainFrame;
        JComponent newContentPane = new DoubleTroubleGame(isTournament, computerFirst, computerScore, playerScore);
        newContentPane.setOpaque(true); //content panes must be opaque
        MainFrame.setContentPane(newContentPane);

        //Display the window.
        MainFrame.pack();
        MainFrame.setVisible(true);
        if (computerFirst) computerPlay();
    }

    private static void computerPlay() {
        int greenSize = uncapturedGreenTiles.size();
        int yellowSize = uncapturedYellowTiles.size();
        int orangeSize = uncapturedOrangeTiles.size();
        if ((greenSize ^ yellowSize ^ orangeSize) != 0) {
            System.out.println("Optimal Play Available");
            int amountToCapture;
            ArrayList<GameTile> tiles;
            if ((greenSize ^ orangeSize) < yellowSize) {
                System.out.println("Taking from yellow");
                tiles = uncapturedYellowTiles;
                amountToCapture = yellowSize - (greenSize ^ orangeSize);
            } else if ((yellowSize ^ orangeSize) < greenSize) {
                System.out.println("Taking from green");
                tiles = uncapturedGreenTiles;
                amountToCapture = greenSize - (yellowSize ^ orangeSize);
            } else if ((greenSize ^ yellowSize) < orangeSize) {
                System.out.println("Taking from orange");
                tiles = uncapturedOrangeTiles;
                amountToCapture = orangeSize - (greenSize ^ yellowSize);
            } else {
                computerRandomPlay();
                return;
            }
            System.out.println("Taking " + amountToCapture + " from " + tiles.get(0).color + " which is of size " + tiles.size());
            for (int i = 0; i < amountToCapture; i++) {
                tiles.get(rand.nextInt(tiles.size())).capture();
            }
            if (tiles.size() == 0) uncapturedGameTiles.remove(tiles);
            if (uncapturedGameTiles.size() == 0) endGame("Computer");
        } else {
            computerRandomPlay();
        }
    }

    private static void computerRandomPlay() {
        int color = rand.nextInt(uncapturedGameTiles.size());
        ArrayList<GameTile> colorList = uncapturedGameTiles.get(color);
        System.out.println(colorList);
        int amount = rand.nextInt(colorList.size());
        if (amount == 0) amount = 1;
        System.out.println("Selecting " + amount + " from size of " + colorList.size());
        for (int i = 0; i < amount; i++) {
            GameTile tile = colorList.get(0);
            tile.capture();
        }
        if (colorList.size() == 0) uncapturedGameTiles.remove(colorList);
        if (uncapturedGameTiles.size() == 0) endGame("Computer");
    }

    private static void endGame(String player) {
        JOptionPane.showMessageDialog(null, player + " won!");
        if (isTournament) {
            if (player.equals("Computer")) computerScore++;
            if (player.equals("You")) playerScore++;
            System.out.println("Computer Score: " + computerScore);
            if (computerScore >= 2) {
                JOptionPane.showMessageDialog(null, "The Computer won the tournament!");
            } else if (playerScore >= 2) {
                JOptionPane.showMessageDialog(null, "You won the tournament!");
            } else {
                createAndShowGUI(mainFrame, true, !computerFirst, computerScore, playerScore);
                return;
            }
        }
        quitGame();
    }

    private static void quitGame() {
        String[] args = {};
        DoubleTroubleRunner.main(args);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof GameTile) {
            GameTile tile = (GameTile) actionEvent.getSource();
            if (!tile.isSelected()) {
                currentlySelectedTiles.add(tile);
            } else {
                currentlySelectedTiles.remove(tile);
            }
            if (currentlySelectedTiles.size() > 0 && currentlySelectedColor == null) {
                currentlySelectedColor = tile.color;
                disableOtherColors(tile.color);
            } else if (currentlySelectedTiles.size() == 0) {
                currentlySelectedColor = null;
                enableOtherColors(tile.color);
            }
        }
    }

    private void disableOtherColors(GameColors color) {
        for (ArrayList<GameTile> list : uncapturedGameTiles) {
            for (GameTile tile : list) {
                if (tile.color != color && !tile.captured) {
                    tile.setEnabled(false);
                }
            }
        }
    }

    private void enableOtherColors(GameColors color) {
        for (ArrayList<GameTile> list : uncapturedGameTiles) {
            for (GameTile tile : list) {
                if (tile.color != color && !tile.captured) {
                    tile.setEnabled(true);
                }
            }
        }
    }

    private void enableAllUncaptured() {
        for (ArrayList<GameTile> list : uncapturedGameTiles) {
            for (GameTile tile : list) {
                if (!tile.captured) {
                    tile.setEnabled(true);
                }
            }
        }
    }
}

class GameTile extends JCheckBox {
    int row;
    int col;
    GameColors color;
    boolean captured;
    ArrayList<GameTile> tileList;
    ArrayList<ArrayList<GameTile>> allUncapturedTiles;

    public GameTile(int row, int col, ArrayList<ArrayList<GameTile>> uncapturedTiles) {
        this.row = row;
        this.col = col;
        this.captured = false;
        this.setSelected(true);
        this.allUncapturedTiles = uncapturedTiles;
        switch (row + 1) {
            case 1:
            case 2:
                tileList = uncapturedTiles.get(0);
                tileList.add(this);
                this.color = GameColors.GREEN;
                this.setBackground(Color.GREEN);
                break;
            case 3:
            case 4:
                tileList = uncapturedTiles.get(1);
                tileList.add(this);
                this.color = GameColors.YELLOW;
                this.setBackground(Color.YELLOW);
                break;
            case 5:
                tileList = uncapturedTiles.get(2);
                tileList.add(this);
                this.color = GameColors.ORANGE;
                this.setBackground(Color.ORANGE);
                break;
        }
        this.setOpaque(true);
    }

    public void capture() {
        this.setEnabled(false);
        this.setSelected(false);
        this.tileList.remove(this);
        if (this.tileList.size() == 0) allUncapturedTiles.remove(this.tileList);
        this.captured = true;
    }

    @Override
    public String toString() {
        return "GameTile{" +
                "row=" + row +
                ", col=" + col +
                ", color=" + color +
                ", captured=" + captured +
                '}';
    }
}


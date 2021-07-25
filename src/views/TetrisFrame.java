package views;

import javax.swing.*;
import java.awt.*;

/**
 * This class constructs the main frame for the tetris game
 * It builds the gameboard, add the statusbar, labels, and instruction text
 */
public class TetrisFrame extends JFrame {
    private JLabel statusBar, gameLabel, previewLabel;
    private JTextArea instructionText;
    private TetrisBoard gameBoard;

    /**
     * Contructor to set up the GUI
     */
    public TetrisFrame() {
        statusBar = new JLabel(" Game score is 0");
        statusBar.setHorizontalTextPosition(SwingConstants.CENTER);

        instructionText = new JTextArea();
        instructionText.setText("Instructions: Pressed left or right to move the Tetris, Pressed Up to rotate left, " +
                "Pressed down to rotate right, Pressed p to pause the game, Pressed spacebar to immediately drop the piece" +
                ", Pressed D to drop the piece faster");
        instructionText.setWrapStyleWord(true);
        instructionText.setLineWrap(true);
        instructionText.setOpaque(false);
        instructionText.setEditable(false);
        instructionText.setFocusable(false);
        instructionText.setBackground(UIManager.getColor("Label.background"));
        instructionText.setFont(UIManager.getFont("Label.font"));
        instructionText.setBorder(UIManager.getBorder("Label.border"));

        gameLabel = new JLabel("Game Board");
        previewLabel = new JLabel("Preview of Next Piece");

        gameBoard = new TetrisBoard(this);
        JPanel background = new JPanel();

        setLayout(new BorderLayout());
        statusBar.setBounds(150, 0,200,30);
        add(statusBar);

        //instructionText.setBounds(50,30,400,100);
        add(instructionText, BorderLayout.SOUTH);
        gameLabel.setBounds(50,30,150,30);
        add(gameLabel);
        previewLabel.setBounds(300,30,150,30);
        add(previewLabel);

        gameBoard.setBounds(25,60,400,480);
        add(gameBoard, BorderLayout.CENTER);
        gameBoard.run();

        add(background);

        setPreferredSize(new Dimension(500, 650));
        setTitle("Tetris Game by CSJ");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(false);
    }

    /**
     * Getter for statusBar
     * @return Jlabel
     */
    JLabel getStatusBar() {
        return statusBar;
    }
}


package views;

import controllers.TetrisController;
import models.Tetronimo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * TetrisBoard.java:
 * Class to model the tetris board
 *
 * @author Chin Shiang Jin
 * @version 1.0 December 13, 2020
 *
 */
public class TetrisBoard extends JPanel implements ActionListener
{
    /**
     * Constant to represent the width of the board
     */
    public static final int BOARD_WIDTH = 10;

    /**
     * Constant to represent the height of the board
     */
    public static final int BOARD_HEIGHT = 24;


    private JLabel statusBar;
    private final TetrisController CONTROLLER;

    /**
     * Contructor class, take in the JFrame instance
     * @param parent
     */
    public TetrisBoard(TetrisFrame parent)
    {
        setFocusable(true);
        CONTROLLER = new TetrisController( BOARD_WIDTH, BOARD_HEIGHT,this );
        statusBar = parent.getStatusBar();
        addKeyListener(new TetrisAdapter());
    }

    /**
     * Call the Controller to start
     */
    public void run()
    {
        CONTROLLER.start();
    }

    /**
     * Action performed, basically continue the gameAction
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        CONTROLLER.gameAction();
    }

    /**
     * Call the controller paint function to paint the gameboard using size information
     * @param g
     */
    public void paint(Graphics g){
        super.paint(g);
        CONTROLLER.paint(g,getSize().getWidth(),getSize().getHeight());
    }

    private int squareWidth(){
        return (int)getSize().getWidth()/(BOARD_WIDTH*2);
    }

    private int squareHeight(){
        return(int)getSize().getHeight()/BOARD_HEIGHT;
    }

    /**
     * Draw the Square using given info
     * @param g graphics object
     * @param x x-coordinate
     * @param y y-coordinate
     * @param shape shape of the Tetrominoes
     */
    public void drawSquare(Graphics g, int x, int y, Tetronimo.Tetrominoes shape) {
        Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0)
        };

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 2, y + 2, squareWidth() - 4, squareHeight() - 4);

        g.setColor(Color.BLACK);
        g.drawRect(x,y,squareWidth(),squareHeight());

        g.setColor(color.brighter());
        g.drawLine(x+1, y + squareHeight()-2 , x+1, y+1);
        g.drawLine(x+1, y+1, x + squareWidth()-2 , y+1);

        g.setColor(color.darker());
        g.drawLine(x + 2, y + squareHeight() -2,
                x + squareWidth() -2 , y + squareHeight()-2 );
        g.drawLine(x + squareWidth() - 2, y + squareHeight() - 2,
                x + squareWidth() - 2, y + 2);
    }

    /**
     * Setting game status
     * @param status Game Status to be displayed
     */
    public void setGameStatus(String status) {
        statusBar.setText(status);
    }

    /**
     * An adapter class to read the users action
     */
    private class TetrisAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            if (!CONTROLLER.isFallStarted() || CONTROLLER.isCurrentPieceNoShape()) {
                return;
            }

            int keycode = e.getKeyCode();

            if (keycode == 'p' || keycode == 'P') {
                CONTROLLER.gamepause();
                return;
            }

            if (CONTROLLER.isGamePaused())
                return;

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    CONTROLLER.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    CONTROLLER.moveRight();
                    break;
                case KeyEvent.VK_DOWN:
                    CONTROLLER.rotateRight();
                    break;
                case KeyEvent.VK_UP:
                    CONTROLLER.rotateLeft();
                    break;
                case KeyEvent.VK_SPACE:
                    CONTROLLER.immediateDrop();
                    break;
                case 'd':
                case 'D':
                    CONTROLLER.tetroOneLineDown();
                    break;
            }   //End of switch
        }       //End of keypressed method
    }       //End of TetrisAdapter class
}   //End of TetrisBoard class

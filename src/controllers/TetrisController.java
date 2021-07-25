package controllers;

import models.Tetronimo;
import views.TetrisBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * TetrisController.java:
 * Class to hold all of the game logic for tetris
 *
 * @author Chin Shiang Jin
 * @version 1.0 December 13, 2020
 */
public class TetrisController {
    private TetrisBoard tetrisBoard;
    private int boardWidth;
    private int boardHeight;
    private boolean isLineFull = false;
    private boolean isFallStarted = false;
    private boolean isGamePaused = false;

    private int gameScore = 0;

    //currentX and currentY will store the location of falling piece in the gameboard
    private int currentX = 0;
    private int currentY = 0;

    //previewX and previewY will store the location of upcoming piece in previewboard
    private int previewX = 0;
    private int previewY = 0;

    private Timer timer;

    //current piece information is stored in currentTetromino and previewBoard
    //upcoming piece information will be stored in newTetromino and previewBoard,
    private Tetronimo currentTetromino;
    private Tetronimo newTetromino;
    private Tetronimo.Tetrominoes[] gameBoard;

    /**
     * Constructor to take in a tetris board so the controller and the board can communicate
     * @param boardWidth default value is 10
     * @param boardHeight default value is 24
     * @param tetrisBoard A tetris board instance
     */
    public TetrisController(int boardWidth, int boardHeight, TetrisBoard tetrisBoard) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tetrisBoard = tetrisBoard;
        newTetromino = new Tetronimo();
        currentTetromino = new Tetronimo();

        timer = new Timer(300, tetrisBoard);
        timer.start();
        gameBoard = new Tetronimo.Tetrominoes[boardWidth * boardHeight];
        clearBoard();
    }

    /**
     * gameAction continuously run when Timer is running
     */
    public void gameAction() {
        if (isLineFull) {
            isLineFull = false;
            nextTetrominoFall();
        } else {
            tetroOneLineDown();
        }
    }


    //return isFallStarted
    public boolean isFallStarted() {
        return isFallStarted;
    }

    //return isFallStarted
    public boolean isGamePaused() {
        return isGamePaused;
    }

    //return isCurrentPieceNoShape
    public boolean isCurrentPieceNoShape() {
        return currentTetromino.getShape() == Tetronimo.Tetrominoes.BlankShape;
    }

    /**
     * Start the game
     */
    public void start() {
        if (isGamePaused) {
            return;
        }
        isFallStarted = true;
        isLineFull = false;
        gameScore = 0;
        clearBoard();
        getNextTetromino();     //Get the first Tetromino
        nextTetrominoFall();    //First Tetromino start falling
        timer.start();
    }

    /**
     * Pause the game
     */
    public void gamepause() {
        //return if fall havent started
        if (!isFallStarted) {
            return;
        }

        isGamePaused = !isGamePaused;   //switch paused status
        if (isGamePaused) {
            timer.stop();
            tetrisBoard.setGameStatus("paused");
        } else {
            timer.start();
            tetrisBoard.setGameStatus("Scores: " + String.valueOf(gameScore));
        }
        tetrisBoard.repaint();
    }

    /**
     * Drop the tetromino by one line if nextdrop is legal
     */
    public void tetroOneLineDown() {
        if (!nextDropLegal(currentTetromino, currentX, currentY - 1)) {
            tetroLanded();
        }
    }

    /**
     * clear the game Board
     */
    private void clearBoard() {
        for (int i = 0; i < boardHeight * boardWidth; ++i) {
            gameBoard[i] = Tetronimo.Tetrominoes.BlankShape;
        }

    }

    /**
     * Get the nextTetromino with random shape, store it in newTetromino so we can preview it
     */
    private void getNextTetromino() {
        newTetromino.setRandomShape();
    }

    /**
     * drop the nextTetromino
     */
    private void nextTetrominoFall() {
        currentTetromino.setShape(newTetromino.getShape());

        //Call for next upcoming piece
        getNextTetromino();
        currentX = boardWidth / 2 + 1;
        currentY = boardHeight - 1 + currentTetromino.minOfY();

        if (!nextDropLegal(currentTetromino, currentX, currentY)) {
            currentTetromino.setShape(Tetronimo.Tetrominoes.BlankShape);
            timer.stop();
            isFallStarted = false;
            tetrisBoard.setGameStatus("game over!");
        }
    }

    /**
     * Return the tetronimo at current gameboard location
     * @param x horizontal position of the tetro
     * @param y vertical position of the tetro
     * @return the Tetrominoes
     */
    private Tetronimo.Tetrominoes shapeAt(int x, int y) {
        return gameBoard[(y * boardWidth) + x];
    }

    /**
     * check if next drop is legal
     * @param newTetromino  Tetro to be droppec
     * @param newX      New horizontal position
     * @param newY      New vertical position
     * @return  a boolean value
     */
    private boolean nextDropLegal(Tetronimo newTetromino, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newTetromino.x(i);
            int y = newY - newTetromino.y(i);
            if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
                return false;
            }
            if (shapeAt(x, y) != Tetronimo.Tetrominoes.BlankShape)
                return false;
        }

        currentTetromino = newTetromino;
        currentX = newX;
        currentY = newY;
        tetrisBoard.repaint();
        return true;
    }

    /**
     * Actions to performed when the tetro landed
     */
    private void tetroLanded() {
        for (int i = 0; i < 4; ++i) {
            int x = currentX + currentTetromino.x(i);
            int y = currentY - currentTetromino.y(i);
            gameBoard[(y * boardWidth) + x] = currentTetromino.getShape();
        }

        removeFullLines();

        if (!isLineFull) {
            nextTetrominoFall();
        }
    }   //End of method Tetro drop

    /**
     * remove the fullline, also update the gamescore
     */
    private void removeFullLines() {
        int numFullLines = 0;

        for (int y = boardHeight - 1; y >= 0; --y) {
            boolean lineIsFull = true;

            //traverse the line to check if there is BlankShape - means line not full
            for (int x = 0; x < boardWidth; ++x) {
                if (shapeAt(x, y) == Tetronimo.Tetrominoes.BlankShape) {
                    lineIsFull = false;
                    break;
                }
            }

            //Action if line is full
            if (lineIsFull) {
                ++numFullLines;
                for (int k = y; k < boardHeight - 1; ++k) {
                    for (int x = 0; x < boardWidth; ++x) {
                        gameBoard[(k * boardWidth + x)] = shapeAt(x, k + 1);
                    }
                }
            }   //End of if
        }   //End of For Loop

        //Update Game Score
        if (numFullLines > 0) {
            if (numFullLines == 4) {
                gameScore += 800;
            } else {
                gameScore += 100 * numFullLines;
            }
            tetrisBoard.setGameStatus("Game Score = "+String.valueOf(gameScore));
            isLineFull= true;
            currentTetromino.setShape(Tetronimo.Tetrominoes.BlankShape);
            tetrisBoard.repaint();
        }
    }   //End of removeFull line

    /**
     * Action to immediately drop the piece
     */
    public void immediateDrop() {
        int newY = currentY;
        while (newY > 0) {
            if (!nextDropLegal(currentTetromino, currentX, newY - 1)) {
                break;
            }
            --newY;
        }
        tetroLanded();
    }

    /**
     * Action to move left
     */
    public void moveLeft(){
        nextDropLegal(currentTetromino,currentX-1,currentY);
    }

    /**
     * Action to move Right
     */
    public void moveRight(){
        nextDropLegal(currentTetromino, currentX + 1, currentY);
    }

    /**
     * Action to rotate Left
     */
    public void rotateLeft(){
        nextDropLegal(currentTetromino.rotateLeft(), currentX, currentY);
    }

    /**
     * Action to rotate Right
     */
    public void rotateRight(){
        nextDropLegal(currentTetromino.rotateRight(), currentX,currentY);
    }

    /**
     * This will paint the board
     * @param g a graphics object
     * @param width width of the actual view JPanel
     * @param height height of the actual view JPanel
     */
    public void paint(Graphics g, double width, double height) {
        int squareWidth = (int) width / (boardWidth*2);
        int squareHeight = (int) height / boardHeight;
        int boardTop = (int) height - boardHeight * squareHeight;

        //This for loop paint the gameboard
        for (int y = 0; y < boardHeight; ++y) {
            for (int x = 0; x < boardWidth; ++x) {
                Tetronimo.Tetrominoes shape = shapeAt(x, boardHeight - y - 1);
                if (shape != Tetronimo.Tetrominoes.BlankShape) {
                    tetrisBoard.drawSquare(g, x * squareWidth, boardTop + y * squareHeight, shape);
                }
                else{
                    g.setColor(Color.BLACK);
                    g.drawRect(x * squareWidth, boardTop + y * squareHeight,squareWidth,squareHeight);
                }
            }
        }   //End of outer for loop

        //This for loop paint the falling piece
        if (currentTetromino.getShape() != Tetronimo.Tetrominoes.BlankShape) {
            for (int i = 0; i < 4; ++i) {
                int x = currentX + currentTetromino.x(i);
                int y = currentY - currentTetromino.y(i);
                tetrisBoard.drawSquare(g, x * squareWidth, boardTop + (boardHeight - y - 1) * squareHeight, currentTetromino.getShape());
            }
        }

        //This for loop paint the preview piece
        if (newTetromino.getShape() != Tetronimo.Tetrominoes.BlankShape) {
            for (int i = 0; i < 4; ++i) {
                int x = previewX + newTetromino.x(i);
                int y = previewY + newTetromino.y(i);
                tetrisBoard.drawSquare(g, x * squareWidth + (int)width*4/5,
                        y * squareHeight + (int)height/4, newTetromino.getShape());
            }
        }
    }   //End of method paint
}   //End of class

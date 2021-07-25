package models;

import java.util.Random;

/**
 * Tetronimo.java:
 * An abstract class to model the base capabilities of a tetronimo
 *
 * @author Chin Shiang Jin
 * @version 1.0 December 13, 2020
 */
public class Tetronimo
{
    public enum Tetrominoes {
        BlankShape(new int[][]{{0, 0}, {0, 0}, {0, 0}, {0, 0}}),  //blank shape
        ZShape(new int[][]{{0, -1}, {0, 0}, {-1, 0}, {-1, 1}}),      //ZShape
        SShape(new int[][]{{0, -1}, {0, 0}, {1, 0}, {1, 1}}),    //SShape
        StraightLineShape(new int[][]{{0, -1}, {0, 0}, {0, 1}, {0, 2}}),      //LineShape
        TShape(new int[][]{{-1, 0}, {0, 0}, {1, 0}, {0, 1}}),   //TShape
        SquareShape(new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}}),      //SquareShape
        LShape(new int[][]{{1, -1}, {0, -1}, {0, 0}, {0, 1}}),      //LShape
        InvertedLShape(new int[][]{{-1, -1}, {0, -1}, {0, 0}, {0, 1}});      //LShape inverted

        public int[][] coordinates; //coordinates for tetrominors

        Tetrominoes(int[][] coordinates) {
            this.coordinates = coordinates;
        }
    }   //End of enum Tetrominos

    private Tetrominoes typeShape;
    //private Tetrominoes newTypeShape;
    private int [][] coordinates;
    //private int [][] newShapeCoords;

    /**
     * Constructor class
     */
    public Tetronimo() {
        coordinates = new int[4][2]; //Coordinates for each new Tetrominoes is stored in 4 x 2 array
        //newShapeCoords = new int[4][2];
        setShape(Tetrominoes.BlankShape);
    }

    /**
     * Setting the shape of the Tetrominoes
     * @param shape hold the type of shape
     */
    public void setShape(Tetrominoes shape) {
        for (int i = 0; i < 4; i++){        //iterate from index 0 to index 3 for the four coords
            for (int j = 0; j < 2; j++){    //iterate from 0 to 1 for the x and y coords
                coordinates[i][j]= shape.coordinates[i][j]; //set the coordinates
            }
        }   //end of for loop
        this.typeShape = shape;  //set the shape of the Tetrominoes
    }

    /**
     * Set the X coordinate of the Tetro, //The setX & setY method is required for shape rotation
     * @param index
     * @param x
     */
    private void setX (int index, int x){
        coordinates[index][0] = x;
    }

    /**
     * Set the y coordinate of the Tetro
     * @param index
     * @param y
     */
    private void setY (int index, int y){
        coordinates[index][1] = y;
    }

    /**
     * Return the x coordinate of the tetro
     * @param index
     * @return
     */
    public int x(int index){
        return coordinates[index][0];
    }

    /**
     * Return the y coordinate of the tetro
     * @param index
     * @return
     */
    public int y(int index){
        return coordinates[index][1];
    }

    /**
     * Get the shape
     * @return Tetrominoes
     */
    public Tetrominoes getShape(){
        return typeShape;
    }

    /**
     * Set a new random shape
     */
    public void setRandomShape(){
        Random r = new Random();
        int shapeIndex = Math.abs(r.nextInt())%7 + 1;
        Tetrominoes [] values = Tetrominoes.values();
        setShape(values[shapeIndex]);
    }   //End of setRandomShape

    /**
     * Find the lowest point
     * @return int
     */
    public int minOfY(){
        int minY = coordinates[0][0];
        for (int i = 0; i < 4; i++){
            minY = Math.min(minY,coordinates[i][1]);
        }
        return minY;
    }

    /**
     * Rotate the Tetro left
     * @return Tetronimo
     */
    public Tetronimo rotateLeft() {
        if(typeShape == Tetrominoes.SquareShape)
            return this;

        Tetronimo resultshape = new Tetronimo();
        resultshape.typeShape = typeShape;

        for (int i = 0; i < 4; ++i ){
            resultshape.setX(i, y(i));
            resultshape.setY(i,-x(i));
        }
        return resultshape;
    }

    /**
     * Rotate the Tetro right
     * @return Tetronimo
     */
    public Tetronimo rotateRight() {
        if(typeShape == Tetrominoes.SquareShape)
            return this;

        Tetronimo resultshape = new Tetronimo();
        resultshape.typeShape = typeShape;

        for (int i = 0; i < 4; ++i ){
            resultshape.setX(i, -y(i));
            resultshape.setY(i,x(i));
        }
        return resultshape;
    }
}

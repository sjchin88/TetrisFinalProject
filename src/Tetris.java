import views.TetrisFrame;

/**
 * Tetris.java:
 * Main class for tetris, the program starts from here
 *
 * @author Chin Shiang Jin
 * @version 1.0 December 13, 2020
 */
public class Tetris
{
    /**
     * Function main begins with program execution
     *
     * @param args The command line args (not used in this program)
     */
    public static void main( String[] args )
    {
        TetrisFrame game = new TetrisFrame();
        game.setLocationRelativeTo(null);
    }
}
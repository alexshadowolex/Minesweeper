
public class Minesweeper{

    public static void main( String args[] ){
        try{
            new MinesweeperController();
        } catch ( Exception e ){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
// import java.util.Arrays;
import java.util.Random;

public class MinesweeperModel{

    private int mines;
    private int currentFlags;
    private int width;
    private int height;
    private int [][]fields;
    
    public MinesweeperModel(){
        width = height = mines = currentFlags = 0;   
    }

    public void initGame( int width, int height, int mines ){
        this.width = width;
        this.height = height;
        this.mines = currentFlags = mines;
        //init the data
        fields = new int[ height ][ width ];
        for( int i = 0; i < fields.length; i++ )
            for( int j = 0; j < fields[i].length; j++ )
                fields[i][j] = 0;

        int setMines = 0;
        Random rnd = new Random();
        //set mines randomly
        while( setMines < mines ){
            int rndWidth = rnd.nextInt( width );
            int rndHeight = rnd.nextInt( height );

            //if there is a mine set already, try again
            if( fields[ rndHeight ][ rndWidth ] == -1 )
                continue;

            //set mine
            fields[ rndHeight ][ rndWidth ] = -1;
            setMines++;
        }

        //calc the fields values
        for( int i = 0; i < fields.length; i++ ){
            for( int j = 0; j < fields[i].length; j++ ){
                //if there is no bomb, count the bombs around that field
                if( fields[i][j] != -1 ){
                    int currentValue = 0;
                    for( int k = i - 1; k <= i + 1; k++ )
                        for( int l = j - 1; l <= j + 1; l++ ){
                            try{
                                if( fields[k][l] == -1 )
                                    currentValue++;
                            } catch ( IndexOutOfBoundsException e ){}
                        }
                    //set field value
                    fields[i][j] = currentValue;
                }
            }
        }

        // System.out.println( Arrays.deepToString( fields ) );
        
    }

    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getFieldValue( int width, int height ){
        return fields[ height ][ width ];
    }
    
    public int getMines(){
        return width;
    }

    public int getCurrentFlags(){
        return currentFlags;
    }

    public void incCurrentFlags(){
        currentFlags++;
    }

    public void decCurrentFlags(){
        currentFlags--;
    }
}
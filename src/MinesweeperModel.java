import java.util.Arrays;
import java.util.Random;

public class MinesweeperModel{

    private int mines;
    private int currentMines;
    private int width;
    private int height;
    private int [][]fields;
    
    public MinesweeperModel(){
        width = height = mines = currentMines = 0;   
    }

    public void initGame( int width, int height, int mines ){
        this.width = width;
        this.height = height;
        this.mines = currentMines = mines;
        fields = new int[ height ][ width ];
        for( int i = 0; i < fields.length; i++ )
            for( int j = 0; j < fields[i].length; j++ )
                fields[i][j] = 0;

        int setMines = 0;
        Random rnd = new Random();
        while( setMines < mines ){
            int rndWidth = rnd.nextInt( width );
            int rndHeight = rnd.nextInt( height );

            if( fields[ rndHeight ][ rndWidth ] == -1 )
                continue;

            fields[ rndHeight ][ rndWidth ] = -1;
            setMines++;
        }

        for( int i = 0; i < fields.length; i++ ){
            for( int j = 0; j < fields[i].length; j++ ){
                if( fields[i][j] != -1 ){
                    int currentValue = 0;
                    for( int k = i - 1; k <= i + 1; k++ )
                        for( int l = j - 1; l <= j + 1; l++ ){
                            try{
                                if( fields[k][l] == -1 )
                                    currentValue++;
                            } catch ( IndexOutOfBoundsException e ){}
                        }
                    fields[i][j] = currentValue;
                }
            }
        }

        System.out.println( Arrays.deepToString( fields ) );
        
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

    public int getCurrentMines(){
        return currentMines;
    }

    public void incCurrentMines(){
        currentMines++;
    }

    public void decCurrentMines(){
        currentMines--;
    }
}
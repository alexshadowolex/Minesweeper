import java.util.Arrays;
import java.util.Random;

public class MinesweeperModel{

    private int mines;
    private int currentFlags;
    private int width;
    private int height;
    private int [][]fields;
    private final int MAX_MINES = 668;
    private final int MAX_WIDTH = 30;
    private final int MAX_HEIGHT = 24;
    private final int MIN_MINES = 1;
    private final int MIN_WIDTH = 6;
    private final int MIN_HEIGHT = 6;
    private String usedTime;
    // private Timer timer;
    private Thread thread;
    private boolean goTime = true;

    private class Timer implements Runnable{
        private long startTime;

        public Timer(){}

        public void run(){
            startTime = System.currentTimeMillis();
            while( goTime ){
                long now = System.currentTimeMillis();
                usedTime = String.valueOf( now - startTime );
            }
        }
    }
    
    public MinesweeperModel(){
        width = height = mines = currentFlags = 0;   
        usedTime = "0";
    }

    public void initGame( int width, int height, int mines ){
        this.width = width;
        this.height = height;
        this.mines = currentFlags = mines;
        thread = new Thread( new Timer() );
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

        String output = Arrays.deepToString( fields );
        output = "1: " + output.substring( 1, output.length() - 1 );
        int iterator = 2;
        while( output.indexOf("],") != -1 ){
            output = output.substring( 0, output.indexOf("],") + 1 ) + "\n" + iterator + ":" + output.substring( output.indexOf("],") + 2 );
            iterator++;
        }
        System.out.println( output + "\n================" );
        
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
        return mines;
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

    public int getMaxMines(){
        return MAX_MINES;
    }

    public int getMaxWidth(){
        return MAX_WIDTH;
    }

    public int getMaxHeight(){
        return MAX_HEIGHT;
    }

    public int getMinMines(){
        return MIN_MINES;
    }

    public int getMinWidth(){
        return MIN_WIDTH;
    }

    public int getMinHeight(){
        return MIN_HEIGHT;
    }

    public void setGoTime( boolean b ){
        goTime = b; //Stops the timer, when set to false
    }

    public void startTimer(){
        thread.run();
    }

    public String getUsedTime(){
        return usedTime;
    }
}
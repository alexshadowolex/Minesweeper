import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MinesweeperModel{

    private int mines;
    private int currentFlags;
    private int width;
    private int height;
    private int [][]fields;
    public final String seperator = ";$;";
    private final int MAX_MINES = 668;
    private final int MAX_WIDTH = 30;
    private final int MAX_HEIGHT = 24;
    private final int MIN_MINES = 1;
    private final int MIN_WIDTH = 6;
    private final int MIN_HEIGHT = 6;
    private final String pathToFile = "D:/Dateien/Wichtiges/Programme/java/Minesweeper/data/highscores.txt";
    private ArrayList<ArrayList<HighScoreHolder>> highScoresBasicGames = new ArrayList<ArrayList<HighScoreHolder>>();

    public class HighScoreHolder{
        private String userName;
        private int neededTime;
        private String whichGame;
        
        public HighScoreHolder( String userName, int neededTime, String whichGame ){
            this.userName = userName;
            this.neededTime = neededTime;
            this.whichGame = whichGame;
        }

        public String getUserName(){
            return userName;
        }

        public int getNeededTime(){
            return neededTime;
        }

        public String getWhichGame(){
            return whichGame;
        }

        @Override
        public String toString(){
            return neededTime + "s - " + userName;
        }
    }
    
    public MinesweeperModel(){
        width = height = mines = currentFlags = 0;  
        readHighScores(); 
        // writeHighScores();
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

        String output = Arrays.deepToString( fields );
        output = "1: " + output.substring( 1, output.length() - 1 );
        int iterator = 2;
        while( output.indexOf("],") != -1 ){
            output = output.substring( 0, output.indexOf("],") + 1 ) + "\n" + iterator + ":" + output.substring( output.indexOf("],") + 2 );
            iterator++;
        }
        System.out.println( output + "\n================" );
        
    }

    public void readHighScores(){
        try{
            File file = new File( pathToFile );
            FileReader fr = new FileReader( file );
            BufferedReader br = new BufferedReader( fr );
            String tmpLine;
            String currentWhich = "";
            ArrayList<HighScoreHolder> currentList = new ArrayList<HighScoreHolder>();
            do{
                tmpLine = br.readLine();
                if( tmpLine != null ){
                    int index = tmpLine.indexOf( seperator );
                    String tmpName = tmpLine.substring( 0, index );
                    tmpLine = tmpLine.substring( index + seperator.length() );
                    index = tmpLine.indexOf( seperator );
                    int tmpTime = Integer.parseInt( tmpLine.substring( 0, tmpLine.indexOf( seperator ) ) );
                    String tmpWhich = tmpLine.substring( index + seperator.length() );

                    if( currentWhich.equals("") )
                        currentWhich = tmpWhich;
                    if( !currentWhich.equals( tmpWhich ) ){
                        highScoresBasicGames.add( currentList );
                        currentList = new ArrayList<HighScoreHolder>();
                    }

                    currentList.add( new HighScoreHolder( tmpName, tmpTime, tmpWhich ) );
                } else {
                    highScoresBasicGames.add( currentList );
                } 

            }while( tmpLine != null );

            br.close();

        } catch ( FileNotFoundException e ){}
          catch ( IOException e ){}
    }

    public void writeHighScores(){
        try{
            File file = new File( pathToFile );
            FileWriter fw = new FileWriter( file );
            BufferedWriter bw = new BufferedWriter( fw );

            for( int i = 0; i < highScoresBasicGames.size(); i++ ){
                ArrayList<HighScoreHolder> tmpList = highScoresBasicGames.get(i);
                for( int j = 0; j < tmpList.size(); j++ ){
                    HighScoreHolder tmp = tmpList.get(j);
                    bw.write( tmp.getUserName() + seperator + tmp.neededTime + seperator + tmp.whichGame );
                    if( i != highScoresBasicGames.size() - 1 || j != tmpList.size() - 1 )
                        bw.write( "\n" );
                }
            }
            bw.flush();

            bw.close();
        } catch ( IOException e ){}
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

    public List<HighScoreHolder> getHighScores( int which ){
        if( which > highScoresBasicGames.size() - 1 || which < 0 )
            return null;

        return highScoresBasicGames.get( which );
    }
}
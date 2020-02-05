import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
    public final String seperator = ";\\$;";
    private final int MAX_MINES = 668;
    private final int MAX_WIDTH = 30;
    private final int MAX_HEIGHT = 24;
    private final int MIN_MINES = 1;
    private final int MIN_WIDTH = 6;
    private final int MIN_HEIGHT = 6;
    private final String pathToFile = "D:/Dateien/Wichtiges/Programme/java/Minesweeper/data/highscores.txt";
    //List holding all highscore lists
    private ArrayList<ArrayList<HighScoreHolder>> highScoresBasicGames = new ArrayList<ArrayList<HighScoreHolder>>();

    //Highscore entry
    public class HighScoreHolder implements Comparable<HighScoreHolder>{
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
            return "  " + userName + " - " + neededTime + "s";
        }
        @Override
        public int compareTo( HighScoreHolder o ) {
            return Integer.compare( this.neededTime, ((HighScoreHolder) o).neededTime );
        }
    }
    
    public MinesweeperModel(){
        width = height = mines = currentFlags = 0;  
        readHighScores(); 
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
        //Debug
        while( output.indexOf("],") != -1 ){
            output = output.substring( 0, output.indexOf("],") + 1 ) + "\n" + iterator + ":" + output.substring( output.indexOf("],") + 2 );
            iterator++;
        }
        System.out.println( output + "\n================" );
        
    }

    public void readHighScores(){
        try{
            File file = new File( pathToFile );
            file.createNewFile();
            FileReader fr = new FileReader( file );
            BufferedReader br = new BufferedReader( fr );
            String tmpLine;
            String currentWhich = "";
            ArrayList<HighScoreHolder> currentList = new ArrayList<HighScoreHolder>();
            //Read all highscores from highscores.txt
            do{
                tmpLine = br.readLine();
                if( tmpLine != null ){
                    String []seperated = tmpLine.split( seperator );
                    String tmpWhich = seperated[ seperated.length - 1 ];

                    //Get the lines current list
                    if( currentWhich.equals("") )
                        currentWhich = tmpWhich;
                    if( !currentWhich.equals( tmpWhich ) ){
                        //Add the list, if a new list began
                        highScoresBasicGames.add( currentList );
                        currentList = new ArrayList<HighScoreHolder>();
                    }

                    currentList.add( new HighScoreHolder( seperated[0], Integer.parseInt( seperated[1] ), seperated[2] ) );
                } else {
                    if( currentList.size() != 0  )
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

            //Write highscores to highscores.txt
            for( int i = 0; i < highScoresBasicGames.size(); i++ ){
                ArrayList<HighScoreHolder> tmpList = highScoresBasicGames.get(i);
                for( int j = 0; j < tmpList.size(); j++ ){
                    HighScoreHolder tmp = tmpList.get(j);
                    //Write data as needed
                    bw.write( tmp.getUserName() + seperator + tmp.neededTime + seperator + tmp.whichGame );
                    //Not if it's the last entry
                    if( i != highScoresBasicGames.size() - 1 || j != tmpList.size() - 1 )
                        bw.write( "\n" );
                }
            }
            bw.flush();

            bw.close();
        } catch ( IOException e ){}
    }
    
    public ArrayList<HighScoreHolder> getHighScores( int which ){
        if( which > highScoresBasicGames.size() - 1 || which < 0 )
            return null;
        //Return the needed highscore list
        return highScoresBasicGames.get( which );
    }
    
    public boolean isTopTen( int time, String which ){
        //Check if the new time is inside of the top ten
        ArrayList<HighScoreHolder> current = new ArrayList<HighScoreHolder>();
        if( current.size() < 10 )
            return true;

        //Get the certain list
        for( int i = 0; i < highScoresBasicGames.size(); i++ ){
            if( highScoresBasicGames.get(i).get(0).whichGame.equals( which ) ){
                current = highScoresBasicGames.get(i);
                break;
            }
        }

        //Check if one of the ten values is greater
        for( int i = 0; i < current.size(); i++ ){
            if( time < current.get(i).neededTime )
                return true;
        }

        return false;
    }
    
    public void addToHighScores( HighScoreHolder newEntry ){
        int index = -1;
        //Add the new entry
        //Get the index of the list
        if( highScoresBasicGames.size() != 0 ){
            for( int i = 0; i < highScoresBasicGames.size(); i++ ){
                if( highScoresBasicGames.get(i).get(0).whichGame.equals( newEntry.whichGame ) ){
                    index = i;
                    break;
                }
            }
        }
        
        if( index == -1 ){
            //No list exists
            if( highScoresBasicGames.size() == 0 )
                index = 0;
            else
                //some lists exist
                index = highScoresBasicGames.size();
        }
        highScoresBasicGames.add( new ArrayList<HighScoreHolder>() );
        highScoresBasicGames.get( index ).add( newEntry );

        //Sort by time used
        Collections.sort( highScoresBasicGames.get( index ) );
        //Remove the last (11th) entry
        if( highScoresBasicGames.get( index ).size() > 10 )
            highScoresBasicGames.get( index ).remove( highScoresBasicGames.get( index ).size() - 1 );
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
}
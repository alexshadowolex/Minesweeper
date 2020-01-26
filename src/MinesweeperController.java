// import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class MinesweeperController{
    private MinesweeperView view;
    private MinesweeperModel model;
    private boolean finishedGame = false;
    private boolean firstClick = true;
    private boolean goTime = false;
    // private TimeControl timer;
    private Thread thread;

    private class TimeControl implements Runnable{
        
        public TimeControl(){}
        public void run(){
            while( goTime ){
                view.setTimeText( model.getUsedTime() );
            }
        }
    }

    public MinesweeperController(){
        view = new MinesweeperView();   //initialize GUI
        model = new MinesweeperModel(); //initialize data

        JRadioButton []radioButtons = view.getRadioButtons();
        JTextField   []textFields   = view.getCustomTextFields();
        for( int i = 0; i < radioButtons.length; i++ ){
            radioButtons[i].addActionListener( new ActionListener(){
                @Override
                public void actionPerformed( ActionEvent ae ){
                    boolean enabled = false;
                    // System.out.println("Pressed " + ((JRadioButton) ae.getSource()).getText() );
                    //if the custom radio button is selected, enable textfields
                    if( ((JRadioButton) ae.getSource()).equals( radioButtons[ radioButtons.length - 1 ] ) ){
                        enabled = true;
                    } 
                    
                    for( int j = 0; j < textFields.length; j++ ){
                            textFields[j].setEnabled( enabled );
                    }
                }
            } );
        }
        view.getGoButton().addActionListener( new ActionListener(){
            @Override
            public void actionPerformed( ActionEvent ae ){
                int width = 0;
                int height = 0;
                int mines = 0;
                // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                //Max amount of mines depending on screenSize
                // System.out.println("Height: " + screenSize.getHeight() / 30 + "\nWidth: " + screenSize.getWidth() / 30 );
                boolean worked = true;
                for( int i = 0; i < radioButtons.length; i++ ){
                    if( radioButtons[i].isSelected() ){
                        switch (i){
                            case 0:{
                                //First radioButton selected
                                width = height = 9;
                                mines = 10;
                                break;
                            }
                            case 1:{
                                //Second radioButton selected
                                width = height = 16;
                                mines = 40;
                                break;
                            }
                            case 2:{
                                //Third radioButton selected
                                width = 32;
                                height = 16;
                                mines = 99;
                                break;
                            }
                            case 3:{
                                //custom radioButton selected
                                try{
                                    //Get custom numbers
                                    height = Integer.parseInt( textFields[0].getText() );
                                    width  = Integer.parseInt( textFields[1].getText() );
                                    mines  = Integer.parseInt( textFields[2].getText() );
                                } catch ( NumberFormatException e ){
                                    //non-number found
                                    textFields[0].setText("invalid");
                                    textFields[1].setText("number");
                                    textFields[2].setText("found");
                                    worked = false;
                                }
                                if( worked ){
                                    if( mines > (height * width) - 1 ){
                                        //field should not be only mines
                                        textFields[0].setText("too");
                                        textFields[1].setText("many");
                                        textFields[2].setText("mines");
                                        worked = false;
                                    }
                                    if( height > model.getMaxHeight() || width > model.getMaxWidth() || mines > model.getMaxMines() ){
                                        //if at least on number is too big
                                        textFields[0].setText("max " + model.getMaxHeight() );
                                        textFields[1].setText("max " + model.getMaxWidth() );
                                        textFields[2].setText("max " + model.getMaxMines() );
                                        worked = false;
                                    }
                                    if( mines < model.getMinMines() || width < model.getMinWidth() || height < model.getMinHeight() ){
                                        //if at least one number is too small
                                        textFields[0].setText("min " + model.getMinHeight() );
                                        textFields[1].setText("min " + model.getMinWidth() );
                                        textFields[2].setText("min " + model.getMinMines() );
                                        worked = false;
                                    }
                                }
                                break;
                            }
                        }
                        // view.setFrameName( height + "x" + width );
                        // System.out.println("Pressed " + ((JButton) ae.getSource()).getText() + " | Selected: " + i );
                        break;
                    }
                }
                if( worked ){
                    //if everything worked, initialize the game
                    initNewGame( width, height, mines );
                    buildAndSetCounter();
                }
            }
        } );


        view.getAbortButton().addActionListener( new ActionListener(){
            @Override
            public void actionPerformed( ActionEvent ae ){
                int []widthHeight = view.getStartWidthHeight();
                //Change to the menuPanel with start width and height
                view.repaintFrame( true, widthHeight[0], widthHeight[1] );
            }
        } );

        view.getResetButton().addActionListener( new ActionListener(){
            @Override
            public void actionPerformed( ActionEvent ae ){
                //init a new game with current saved data
                initNewGame( model.getWidth(), model.getHeight(), model.getMines() );
                buildAndSetCounter();
            }
        } );

    }

    private void initNewGame( int width, int height, int mines ){
        model.initGame( width, height, mines );
        finishedGame = false;
        firstClick = true;
        thread = new Thread( new TimeControl() );
        //init a new game and change to the gamePanel
        view.repaintFrame( false, model.getWidth(), model.getHeight() );
        JButton [][]buttons = view.getGameButtons();
        for( int i = 0; i < buttons.length; i++ ){
            for( int j = 0; j < buttons[i].length; j++ ){
                buttons[i][j].addMouseListener( new MouseListener(){
                    //add mouse listener to each gameButton
                    @Override
                    public void mouseEntered( MouseEvent me ){

                    }

                    @Override
                    public void mouseExited( MouseEvent me ){

                    }

                    @Override
                    public void mousePressed( MouseEvent me ){

                    }

                    @Override
                    public void mouseReleased( MouseEvent me ){
                        if( !finishedGame ){    //If not lost, keep going
                            //action happens on mouseRelease
                            JButton pressedButton = (JButton) me.getSource();
                            pressedButton.setFocusable(false);
                            //Get the pressed button
                            int currentWidth = -1;
                            int currentHeight = -1;
                            for( int i = 0; i < buttons.length; i++ ){
                                boolean done = false;
                                for( int j = 0; j < buttons[i].length; j++ ){
                                    //find the position of the button
                                    if( buttons[i][j].equals( pressedButton ) ){
                                        currentHeight = i;
                                        currentWidth = j;
                                        done = true;
                                        break;
                                    }
                                }
                                if( done )
                                    break;
                            }
                            if( me.getButton() == 1 ){  //Left click
                                //Left click reveales the field
                                int value = model.getFieldValue( currentWidth, currentHeight );
                                if( firstClick ){
                                    firstClick = false;
                                    // goTime = true;
                                    goTime = false;
                                    thread.run();
                                    // model.startTimer();
                                }
                                if( !view.setRevealed( value, currentWidth, currentHeight ) ){
                                    model.incCurrentFlags();
                                    buildAndSetCounter();
                                }
                                if( value == 0 ){
                                    revealZero( currentWidth, currentHeight );
                                }
                                
                                if( value == -1 ){
                                    finishedGame = true;
                                    goTime = false;
                                    model.setGoTime( false );
                                    view.setRevealed( -2, currentWidth, currentHeight );
                                    //Reveal all mines
                                    for( int i = 0; i < buttons.length; i++ ){
                                        for( int j = 0; j < buttons[i].length; j++ ){
                                            int tmpValue = model.getFieldValue( j, i );
                                            if( tmpValue == -1 && (currentHeight != i || currentWidth != j) ){
                                                view.setRevealed( tmpValue, j, i );
                                            }
                                        }
                                    }
                                    view.setCounterText("LOSE");
                                }

                                if( value != -1 ){
                                    int countNotRevealedFields = 0;
                                    for( int i = 0; i < buttons.length; i++ ){
                                        for( int j = 0; j < buttons[i].length; j++ ){
                                            //Count all not revealed fields
                                            if( !view.isRevealed( j, i ) )
                                                countNotRevealedFields++;
                                        }
                                    }
                                    if( countNotRevealedFields == model.getMines() ){
                                        finishedGame = true;
                                        goTime = false;
                                        model.setGoTime( false );
                                        view.setCounterText("WIN");
                                    }
                                }
                            } 
                            if( me.getButton() == 3 ){  //Right click
                                //right click sets a flag or turns it into a "?" or turns a "?" into a blank field 
                                int ret = view.setFlag( currentWidth, currentHeight);
                                switch (ret){
                                    case 1:{
                                        //"?" into a blank field -> inc currentFlags
                                        model.incCurrentFlags();
                                        break;
                                    }
                                    case -1:{
                                        //flag set -> dec currentFlags
                                        model.decCurrentFlags();
                                        break;
                                    }
                                }
                                buildAndSetCounter();
                            }
                        }
                    }

                    @Override
                    public void mouseClicked( MouseEvent me ) {
                        
                    }
                } );
                int value = model.getFieldValue( j, i );
                if( value != 0 && value != -1 )
                    view.setNumberColors( value, j, i );
            }
        }
    }

    private void buildAndSetCounter(){
        String tmp = Integer.toString( model.getCurrentFlags() );
        //Build a string for the label
        if( model.getCurrentFlags() >= 0 )
            while( tmp.length() < Integer.toString( model.getMaxMines() ).length() )
                tmp = "0" + tmp;
        else
            while( tmp.length() < Integer.toString( model.getMaxMines() ).length() + 1 )
                tmp = "-0" + tmp.substring(1);

        view.setCounterText( tmp );
    }

    private void revealZero( int width, int height ){
        //Reveal current field
        view.setRevealed( model.getFieldValue( width, height ), width, height );
        for( int i = height - 1; i <= height + 1; i++ ){
            for( int j = width - 1; j <= width + 1; j++ ){
                //Check all 8 fields around the current field
                try {
                    //Check if flag or "?"
                    //Check if got revealed already
                    int value = model.getFieldValue( j, i );
                    if( !view.isSetFlag( j, i ) && 
                        !view.isSetQuestionMark( j, i ) && 
                        value == 0 && 
                        !view.isRevealed( j, i ) ){
                        //reveal if so
                            revealZero( j, i );
                    }
                    if( !view.isSetFlag( j, i ) && !view.isSetQuestionMark( j, i ) )
                        //if it's a number, reveal it
                        view.setRevealed( value, j, i );
                } catch ( IndexOutOfBoundsException e ) {}
            }
        }
    }
}
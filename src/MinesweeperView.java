import java.awt.*;
// import java.awt.event.*;
import javax.swing.*;
// import java.util.*;
import java.io.*;

public class MinesweeperView{
    
    private JFrame frame;
    private JButton [][]buttons;
    private JPanel game;
    private JPanel gameView;
    private JPanel gameMenu;
    private JPanel gameMenuHigher;
    private JPanel gameMenuLower;
    private JPanel mainMenu;
    private JButton abortButton;
    private final String abortButtonToolTip = "Abort Game";
    private JButton resetButton;
    private final String resetButtonToolTip = "Reset Game";
    private JLabel countLabel;
    private final String countLabelToolTip = "Possible Flags";
    private JLabel timeLabel;
    private final String timeLabelToolTip = "Used Time";
    private JLabel headline;
    private JButton highScoreButton;
    private Dimension screenSize;
    private Rectangle windowSize;

    private final String highScoreTitle = "High Scores";
    private JPanel highScorePanel;
    private JLabel highScoreHeadline;
    private final String highScoreHeadlineText = "High Scores";
    private JLabel []highScores = new JLabel[ 10 ];
    private String []highScoreToolTips = new String[ highScores.length ];
    private JButton closeHighScores;

    private final int FACTOR_WINDOW_SIZE = 40;
    private final Font generalFont = new Font("Arial", Font.BOLD, 14 );
    private final Font gameButtonFont = new Font("Arial", Font.PLAIN, 20 );
    private final String headlineText = "Chose Field-Type:";
    private final int startWidth = 350;
    private final int startHeight = 500;
    public final int MAIN_MENU = 0;
    public final int GAME = 1;
    public final int HIGH_SCORES = 2;
    private final Color COLOR_REVEALED = Color.LIGHT_GRAY;
    private final Color []numberColors = new Color[]{
        new Color( 0, 47, 254 ),     //1: Blue
        new Color( 14, 100, 46 ),    //2: Green
        new Color( 253, 54, 31 ),    //3: Red
        new Color( 0, 18, 105 ),     //4: Dark blue
        new Color( 92, 6, 1 ),       //5: Dark red
        new Color( 0, 162, 163 ),    //6: Cyan
        new Color( 112, 0, 108 ),    //7: Purple
        new Color( 158, 79, 0 )      //8: Dark orange
    };

    private final String pathToXIcon = "D:/Dateien/Wichtiges/Programme/java/Minesweeper/icon/emoji_x.png";
    private final String pathToResetIcon = "D:/Dateien/Wichtiges/Programme/java/Minesweeper/icon/emoji1.jpg";
    private ImageIcon flagIcon = new ImageIcon("D:/Dateien/Wichtiges/Programme/java/Minesweeper/icon/flag.png");
    private final int SIZE_GAME_ICONS = 30;
    private final int SIZE_MENU_ICONS = 15;
    private ImageIcon bombIcon = new ImageIcon("D:/Dateien/Wichtiges/Programme/java/Minesweeper/icon/bomb.png");
    private final String menuTitle = "Minesweeper Menu";
    private final String []radioButtonText = new String[]{ 
        "Beginner (9x9, 10 Mines)", 
        "Intermediate (16x16, 40 Mines)", 
        "Expert (16x32, 99 Mines)", 
        "Custom (Enter below: Height, Width, Mines)" 
    };
    private String []radioButtonToolTip = new String[ radioButtonText.length ];
    private JRadioButton radio9x9;
    private JRadioButton radio16x16;
    private JRadioButton radio16x32;
    private JRadioButton radioCustom;
    private JRadioButton []radioButtons = new JRadioButton[]{
        radio9x9,
        radio16x16,
        radio16x32,
        radioCustom,
    };
    
    private JPanel customValues;
    private JTextField customHeight;
    private JTextField customWidth;
    private JTextField customBombs;
    private JTextField []customSettings = new JTextField[]{
        customHeight,
        customWidth,
        customBombs
    };
    private final String []textFieldToolTip = new String[]{
        "Height",
        "Width",
        "Mines"
    };

    private ButtonGroup bg;
    private JButton goButton;

    public MinesweeperView(){
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //Build the frame
        frame = new JFrame( menuTitle );
        frame.setFont( generalFont );
        frame.setVisible(true);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setResizable(false);
        //Build the main menu
        mainMenu = new JPanel( new GridLayout( 8, 1 ) );
        headline = new JLabel( headlineText , SwingConstants.CENTER );
        headline.setFont( generalFont );
        mainMenu.add( headline );

        highScoreButton = new JButton("High Scores");
        highScoreButton.setFont( generalFont );
        mainMenu.add( highScoreButton );

        highScorePanel = new JPanel( new GridLayout( highScores.length + 2, 1 ) );
        highScoreHeadline = new JLabel( highScoreHeadlineText, SwingConstants.CENTER );
        highScoreHeadline.setFont( generalFont );
        highScorePanel.add( highScoreHeadline );
        for( int i = 0; i < highScores.length; i++ ){
            highScores[i] = new JLabel( String.valueOf( i ) );
            highScores[i].setFont( generalFont );
            highScorePanel.add( highScores[i] );
        }
        closeHighScores = new JButton("Close");
        closeHighScores.setFont( generalFont );
        highScorePanel.add( closeHighScores );

        bg = new ButtonGroup();

        for( int i = 0; i < radioButtonText.length; i++ ){
            radioButtonToolTip[i] = radioButtonText[i].substring( 0, radioButtonText[i].indexOf("(") - 1 );
        }

        for( int i = 0; i < radioButtons.length; i++ ){
            //Initialize the radiobuttons
            radioButtons[i] = new JRadioButton( radioButtonText[i] );
            radioButtons[i].setToolTipText( radioButtonToolTip[i] );
            radioButtons[i].setFont( generalFont );
            bg.add( radioButtons[i] );
            //First one is selected
            if( i == 0 )
                radioButtons[i].setSelected(true);
            mainMenu.add( radioButtons[i] );
        }
        customValues = new JPanel( new GridLayout( 1, 5 ) );
        int where = 0;
        //Build the fields for the custom settings
        for( int i = 0; i < 5; i++ ){
            if( i == 1 ){
                customValues.add( new JLabel("X", SwingConstants.CENTER ) );
                continue;
            }
            if( i == 3 ){
                customValues.add( new JLabel(";", SwingConstants.CENTER ) );
                continue;
            }
            customSettings[ where ] = new JTextField();
            customSettings[ where ].setFont( generalFont );
            customSettings[ where ].setHorizontalAlignment( JTextField.CENTER );
            customSettings[ where ].setEnabled(false);
            customSettings[ where ].setToolTipText( textFieldToolTip[ where ] );
            customValues.add( customSettings[ where ] );
            where++;
        }
        mainMenu.add( customValues );
        goButton = new JButton("Start Game");
        goButton.setFont( generalFont );
        mainMenu.add( goButton );

        //Reshape some icons
        ImageIcon iconAbort = new ImageIcon( pathToXIcon );
        ImageIcon iconReset = new ImageIcon( pathToResetIcon );
        flagIcon.setImage( flagIcon.getImage().getScaledInstance( SIZE_GAME_ICONS, SIZE_GAME_ICONS, Image.SCALE_DEFAULT ) );
        bombIcon.setImage( bombIcon.getImage().getScaledInstance( SIZE_GAME_ICONS, SIZE_GAME_ICONS, Image.SCALE_DEFAULT ) );
        iconAbort.setImage( iconAbort.getImage().getScaledInstance( SIZE_MENU_ICONS, SIZE_MENU_ICONS, Image.SCALE_DEFAULT ) );
        iconReset.setImage( iconReset.getImage().getScaledInstance( SIZE_MENU_ICONS, SIZE_MENU_ICONS, Image.SCALE_DEFAULT ) );

        Font font = null;
        try{
            //get a new font for the counter and timer
            String filename="D:/Dateien/Wichtiges/Programme/java/Minesweeper/font/digital-7.ttf";
            //Link for including files in jar:
            //http://stackoverflow.com/questions/13796331/jar-embedded-resources-nullpointerexception/13797070#13797070
            font = Font.createFont( Font.TRUETYPE_FONT, new File( filename ) );
            font = font.deriveFont( Font.BOLD, 22 );
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont( font );
        } catch ( FontFormatException ffe ){}
          catch ( IOException ioe ){}

        //Build the game panel
        gameView = new JPanel( new BorderLayout() );
        gameMenu = new JPanel( new GridLayout( 2, 1 ) );
        gameMenuHigher = new JPanel( new GridLayout( 1, 2 ) );
        abortButton = new JButton( iconAbort );
        abortButton.setToolTipText( abortButtonToolTip );
        resetButton = new JButton( iconReset );
        resetButton.setToolTipText( resetButtonToolTip );
        gameMenuHigher.add( abortButton );
        gameMenuHigher.add( resetButton );

        gameMenuLower = new JPanel( new GridLayout( 1, 2 ) );
        timeLabel = new JLabel(" TimeLabel", SwingConstants.LEFT );
        timeLabel.setToolTipText( timeLabelToolTip );
        timeLabel.setFont( font );
        timeLabel.setForeground( Color.RED );
        gameMenuLower.add( timeLabel );

        countLabel = new JLabel("CountLabel ", SwingConstants.RIGHT );
        countLabel.setToolTipText( countLabelToolTip );
        countLabel.setFont( font );
        countLabel.setForeground( Color.RED );
        gameMenuLower.add( countLabel );

        game = new JPanel( new GridLayout(1,1) );

        gameMenu.add( gameMenuHigher );
        gameMenu.add( gameMenuLower );

        gameView.add( gameMenu, BorderLayout.NORTH );
        gameView.add( game, BorderLayout.CENTER );
        //Paint the main menu
        repaintFrame( MAIN_MENU, 0, 0 );
    }

    public void repaintFrame( int which, int width, int height ){
        int w = width * FACTOR_WINDOW_SIZE;
        int h = height * FACTOR_WINDOW_SIZE;

        Dimension current = new Dimension( w, h );
        switch ( which ){
            case MAIN_MENU:{
                //Going back to menu
                current = new Dimension( startWidth, startHeight );
                frame.setTitle( menuTitle );
                frame.remove( gameView );
                frame.remove( highScorePanel );
                frame.add( mainMenu );
                break;
            }
            case GAME:{
                //Starting new game
                gameView.remove( game );
                game = new JPanel( new GridLayout( height, width ) );
                buttons = new JButton[ height ][ width ];

                for( int i = 0; i < buttons.length; i++ ){
                    for( int j = 0; j < buttons[i].length; j++ ){
                        //Initialize every button
                        buttons[i][j] = new JButton();
                        buttons[i][j].setBackground( Color.WHITE );
                        buttons[i][j].setFont( gameButtonFont );
                        buttons[i][j].setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
                        game.add( buttons[i][j] );
                    }
                }
                gameView.add( game, BorderLayout.CENTER );
                frame.setTitle( height + "x" + width );
                frame.remove( mainMenu );
                frame.remove( highScorePanel );
                frame.add( gameView );
                break;
            }
            case HIGH_SCORES:{
                current = new Dimension( startWidth, startHeight );
                frame.setTitle( highScoreTitle );
                frame.remove( gameView );
                frame.remove( mainMenu );
                frame.add( highScorePanel );
                break;
                
            }
        }
        //Reshape and relocate
        frame.setPreferredSize( current );
        frame.pack();
        windowSize = frame.getBounds();
        frame.setLocation( (int) ((screenSize.getWidth() / 2) - (windowSize.getWidth() / 2)), (int) ((screenSize.getHeight() / 2) - (windowSize.getHeight() / 2 )) ); 
        
        frame.repaint();
    }

    //set a field as revealed by left clicking it
    public boolean setRevealed( int value, int width, int height ){
        JButton button = buttons[ height ][ width ];
        button.setBackground( COLOR_REVEALED );
        boolean retValue = true;
        if( button.getIcon() != null || button.getText().equals("?") ){
            button.setIcon(null);
            retValue = false;
        }
        
        if( value == -1 ){
            //If it was a bomb, show the bomb icon
            button.setIcon( bombIcon );
            // retValue = false;
        }
        if( value == -2 ){
            //The clicked bomb, which caused the loss, gets a red background
            button.setBackground( Color.RED );
            button.setIcon( bombIcon );
        }

        if( value > 0 ){
            //Show the value
            button.setText( Integer.toString( value ) );
        }
        
        return retValue;
    }


    public int setFlag( int width, int height ){
        //Set a flag by right clicking
        JButton button = buttons[ height ][ width ];
        if( !button.getBackground().equals( COLOR_REVEALED ) ){
            //If the button is not revealed yet            
            if( button.getText().equals("?") ){
                //If the button is a "?", remove it
                button.setText("");
                return 1;
            } else {
                if( button.getIcon() != null ){
                    //Reset the icon
                    button.setIcon(null);
                    button.setText("?");
                } else {
                    //Set the icon
                    button.setIcon( flagIcon );
                    return -1;
                }
            }
        }
        return 0;
    }

    public void setNumberColors( int value, int width, int height ){
        buttons[ height ][ width ].setForeground( numberColors[ value - 1 ] );
    }

    public JTextField[] getCustomTextFields(){
        return customSettings;
    }

    public JRadioButton[] getRadioButtons(){
        return radioButtons;
    }

    public JButton getGoButton(){
        return goButton;
    }

    public JButton getResetButton(){
        return resetButton;
    }

    public JButton getAbortButton(){
        return abortButton;
    }

    public JButton[][] getGameButtons(){
        return buttons;
    }

    public int[] getStartWidthHeight(){
        return new int[]{ startWidth, startHeight };
    }

    public boolean isRevealed( int width, int height ){
        return buttons[ height ][ width ].getBackground().equals( COLOR_REVEALED );
    }

    public boolean isSetFlag( int width, int height ){
        return buttons[ height ][ width ].getIcon() != null;
    }

    public boolean isSetQuestionMark( int width, int height ){
        return buttons[ height ][ width ].getText().equals("?");
    }

    public String getCounterText(){
        return countLabel.getText();
    }

    public void setCounterText( String text ){
        countLabel.setText( text + " " );
    }

    public String getTimeText(){
        return timeLabel.getText();
    }

    public void setTimeText( String text ){
        timeLabel.setText( " " + text );
    }

    public JButton getHighScoreButton(){
        return highScoreButton;
    }

    public JButton getCloseButton(){
        return closeHighScores;
    }
}
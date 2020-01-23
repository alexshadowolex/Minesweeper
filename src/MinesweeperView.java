import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class MinesweeperView{
    
    private JFrame frame;
    private JButton [][]buttons;
    private JPanel game;
    private JPanel gameView;
    private JPanel gameMenu;
    private JPanel mainMenu;
    private JButton abortButton;
    private JButton resetButton;
    private JLabel countLabel;
    private Dimension screenSize;
    private Rectangle windowSize;
    private int startWidth = 350;
    private int startHeight = 500;
    private boolean WITH_MAIN_MENU = true;
    private Color COLOR_REVEALED = Color.LIGHT_GRAY;
    private JLabel headline;

    private String pathToXIcon = "D:/Dateien/Wichtiges/Programme/java/Minesweeper/Icons/emoji_x.png";
    private String pathToResetIcon = "D:/Dateien/Wichtiges/Programme/java/Minesweeper/Icons/emoji1.jpg";
    private ImageIcon flagIcon = new ImageIcon("D:/Dateien/Wichtiges/Programme/java/Minesweeper/Icons/flag.png");
    private ImageIcon bombIcon = new ImageIcon("D:/Dateien/Wichtiges/Programme/java/Minesweeper/Icons/bomb.png");
    private String menuTitle = "Minesweeper Menu";
    private String []radioButtonText = new String[]{ 
        "Beginner (9x9, 10 Mines)", 
        "Intermediate (16x16, 40 Mines)", 
        "Expert (16x32, 99 Mines)", 
        "Custom (Enter below: Height, Width, Mines)" 
    };
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

    private ButtonGroup bg;
    private JButton goButton;

    public MinesweeperView(){
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame( menuTitle );
        frame.setVisible(true);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setResizable(false);
        mainMenu = new JPanel( new GridLayout( 7, 1 ) );
        headline = new JLabel("Chose Field-Type:", SwingConstants.CENTER );
        mainMenu.add( headline );
        bg = new ButtonGroup();
        for( int i = 0; i < radioButtons.length; i++ ){
            radioButtons[i] = new JRadioButton( radioButtonText[i] );
            bg.add( radioButtons[i] );
            if( i == 0 )
                radioButtons[i].setSelected(true);
            mainMenu.add( radioButtons[i] );
        }
        customValues = new JPanel( new GridLayout( 1, 5 ) );
        int where = 0;
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
            customSettings[ where ].setEnabled(false);
            customValues.add( customSettings[ where ] );
            where++;
        }
        mainMenu.add( customValues );
        goButton = new JButton("Start Game");
        mainMenu.add( goButton );

        ImageIcon iconAbort = new ImageIcon( pathToXIcon );
        ImageIcon iconReset = new ImageIcon( pathToResetIcon );
        flagIcon.setImage( flagIcon.getImage().getScaledInstance( 30, 30, Image.SCALE_DEFAULT ) );
        bombIcon.setImage( bombIcon.getImage().getScaledInstance( 30, 30, Image.SCALE_DEFAULT ) );
        iconAbort.setImage( iconAbort.getImage().getScaledInstance( 15, 15, Image.SCALE_DEFAULT ) );
        iconReset.setImage( iconReset.getImage().getScaledInstance( 15, 15, Image.SCALE_DEFAULT ) );

        Font font = null;
        try{
            String filename="D:/Dateien/Wichtiges/Programme/java/Minesweeper/Font/digital-7.ttf";
            //this is for testing normally we would store the font file in our app (knows as an embedded resource), see this for help on that http://stackoverflow.com/questions/13796331/jar-embedded-resources-nullpointerexception/13797070#13797070
            font = Font.createFont( Font.TRUETYPE_FONT, new File( filename ) );
            font = font.deriveFont( Font.BOLD, 22 );
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont( font );
        } catch ( FontFormatException ffe ){}
          catch ( IOException ioe ){}

        gameView = new JPanel( new BorderLayout() );
        gameMenu = new JPanel( new GridLayout( 1, 3 ) );
        abortButton = new JButton( iconAbort );
        resetButton = new JButton( iconReset );
        countLabel = new JLabel("CountLabel", SwingConstants.RIGHT );
        countLabel.setFont( font );
        gameMenu.add( abortButton );
        gameMenu.add( resetButton );
        gameMenu.add( countLabel );

        game = new JPanel( new GridLayout(1,1) );

        gameView.add( gameMenu, BorderLayout.NORTH );
        gameView.add( game, BorderLayout.CENTER );

        repaintFrame( WITH_MAIN_MENU, 0, 0 );
    }

    private void repaintFrame( boolean menu, int width, int height ){
        int w = width * 40;
        int h = height * 40;
        // if( width != height ){
        //     w = (w / 30) * 20;
        // }
        Dimension current = new Dimension( w, h );
        if( menu ){
            current = new Dimension( startWidth, startHeight );
            frame.setTitle( menuTitle );
            frame.remove( gameView );
            frame.add( mainMenu );
        } else {
            gameView.remove( game );
            game = new JPanel( new GridLayout( height, width ) );
            buttons = new JButton[ height ][ width ];
            // for( int i = 0; i < width * height; i++ )
            //     game.add( new JLabel("L" + i ) );
            // Random rnd = new Random();
            for( int i = 0; i < buttons.length; i++ ){
                for( int j = 0; j < buttons[i].length; j++ ){
                    buttons[i][j] = new JButton( /*Integer.toString( rnd.nextInt( 10 ) )*/ );
                    buttons[i][j].setBackground( Color.WHITE );
                    buttons[i][j].setFont( new Font( "Arial", Font.PLAIN, 20 ) );
                    buttons[i][j].setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
                    game.add( buttons[i][j] );
                }
            }
            gameView.add( game, BorderLayout.CENTER );
            frame.setTitle( height + "x" + width );
            frame.remove( mainMenu );
            frame.add( gameView );
        }
        frame.setPreferredSize( current );
        frame.pack();
        windowSize = frame.getBounds();
        frame.setLocation( (int) ((screenSize.getWidth() / 2) - (windowSize.getWidth() / 2)), (int) ((screenSize.getHeight() / 2) - (windowSize.getHeight() / 2 )) ); 
        
        frame.repaint();
    }

    public void changePanel( boolean menu, int width, int height ){
        repaintFrame( menu, width, height );
    }

    public void setRevealed( int value, int width, int height ){
        buttons[ height ][ width ].setBackground( COLOR_REVEALED );
        if( value == -1 )
            buttons[ height ][ width ].setIcon( bombIcon );
        else
            buttons[ height ][ width ].setText( Integer.toString( value ) );
        // buttons[ height ][ width ].setEnabled(false);    //maybe do this TODO
    }

    public void setFieldArea( int value, int width, int height ){
        buttons[ height ][ width ].setText( Integer.toString( value ) );
    }

    public void setFlag( int width, int height ){
        JButton button = buttons[ height ][ width ];
        if( !button.getBackground().equals( COLOR_REVEALED ) ){
            
            if( button.getText().equals("?") ){
                button.setText("");
            } else {
                if( button.getIcon() != null ){
                    button.setIcon(null);
                    button.setText("?");
                } else {
                    button.setIcon( flagIcon );
                }
            }
        }
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
}
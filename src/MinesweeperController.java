import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class MinesweeperController{
    private MinesweeperView view;
    private MinesweeperModel model;

    public MinesweeperController(){
        view = new MinesweeperView();
        model = new MinesweeperModel();

        JRadioButton []radioButtons = view.getRadioButtons();
        JTextField   []textFields   = view.getCustomTextFields();
        for( int i = 0; i < radioButtons.length; i++ ){
            radioButtons[i].addActionListener( new ActionListener(){
                @Override
                public void actionPerformed( ActionEvent ae ){
                    boolean enabled = false;
                    System.out.println("Pressed " + ((JRadioButton) ae.getSource()).getText() );
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
                boolean worked = true;
                for( int i = 0; i < radioButtons.length; i++ ){
                    if( radioButtons[i].isSelected() ){
                        switch (i){
                            case 0:{
                                width = height = 9;
                                mines = 10;
                                break;
                            }
                            case 1:{
                                width = height = 16;
                                mines = 40;
                                break;
                            }
                            case 2:{
                                width = 32;
                                height = 16;
                                mines = 99;
                                break;
                            }
                            case 3:{
                                try{
                                    height = Integer.parseInt( textFields[0].getText() );
                                    width  = Integer.parseInt( textFields[1].getText() );
                                    mines  = Integer.parseInt( textFields[2].getText() );
                                } catch ( NumberFormatException e ){
                                    textFields[0].setText("invalid");
                                    textFields[1].setText("number");
                                    textFields[2].setText("found");
                                    worked = false;
                                }
                                if( mines > (height * width) - 1 ){
                                    textFields[0].setText("too");
                                    textFields[1].setText("many");
                                    textFields[2].setText("mines");
                                    worked = false;
                                }
                                break;
                            }
                        }
                        // view.setFrameName( height + "x" + width );
                        System.out.println("Pressed " + ((JButton) ae.getSource()).getText() + " | Selected: " + i );
                        break;
                    }
                }
                if( worked ){
                    initNewGame( width, height, mines );
                }
            }
        } );


        view.getAbortButton().addActionListener( new ActionListener(){
            @Override
            public void actionPerformed( ActionEvent ae ){
                int []widthHeight = view.getStartWidthHeight();
                view.changePanel( true, widthHeight[0], widthHeight[1] );
            }
        } );

        view.getResetButton().addActionListener( new ActionListener(){
            @Override
            public void actionPerformed( ActionEvent ae ){
                initNewGame( model.getWidth(), model.getHeight(), model.getMines() );
            }
        } );

    }

    private void initNewGame( int width, int height, int mines ){
        model.initGame( width, height, mines );
        view.changePanel( false, model.getWidth(), model.getHeight() );
        JButton [][]buttons = view.getGameButtons();
        for( int i = 0; i < buttons.length; i++ ){
            for( int j = 0; j < buttons[i].length; j++ ){
                buttons[i][j].addMouseListener( new MouseListener(){
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
                        JButton pressedButton = (JButton) me.getSource();
                        int currentWidth = -1;
                        int currentHeight = -1;
                        for( int i = 0; i < buttons.length; i++ ){
                            boolean done = false;
                            for( int j = 0; j < buttons[i].length; j++ ){
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
                            view.setRevealed( model.getFieldValue( currentWidth, currentHeight ), currentWidth, currentHeight );
                        } 
                        if( me.getButton() == 3 ){  //Right click
                            //Count flags. New function "setFlag". New label for flag count
                            view.setFlag( currentWidth, currentHeight);
                        }
                    }

                    @Override
                    public void mouseClicked( MouseEvent me ) {
                        
                    }
                } );
            }
        }
    }
}
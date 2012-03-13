/*
 * Menu for the game, contains options to start new game, use custom levels, 
 * options etc...
 */
package golf;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import golf.editor.Editor;
/**
 * @author Paul
 */
public class Menu extends JFrame
{ 
    //Size of Menu
    private final int WIDTH = 200;
    private final int HEIGHT = 300;
    //JButtons used
    private JButton newGame, levelEditor, credits, exit;
    
    
    /**
     * Constructor for the menu, initialises the whole game
     */
    public Menu()
    {
        //Call JFrame with the title
        super("Golf");
        //Set size and position in middle of screen
        setSize(WIDTH, HEIGHT);
        setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 - WIDTH/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - HEIGHT/2));
        //Make the Menu exit on close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Disable the maximise button
        setResizable(false);
        //Make the menu visible
        setVisible(true);
        //Set layout
        setLayout(new GridLayout(6, 0, 5, 5));
        
        
        //New Game Button
        newGame = new JButton("New Game");
        newGame.addActionListener(new MenuButtonListener(this, "newgame"));
        add(newGame);
        
        add(new JButton("Load"));
        
        levelEditor = new JButton("Level Editor");
        levelEditor.addActionListener(new MenuButtonListener(this, "leveleditor"));
        add(levelEditor);
        
        add(new JButton("Options"));
        //Credits Button
        credits = new JButton("Credits");
        credits.addActionListener(new MenuButtonListener(this, "credits"));
        add(credits);
        //Exit Button
        exit = new JButton("Exit");
        exit.addActionListener(new MenuButtonListener(this, "exit"));
        add(exit);
    }
    
    private class MenuButtonListener implements ActionListener
    {
        //String to store what pressed the button
        private String buttonPressed;
        //Menu that the MenuButtonListener is operating in
        private Menu menu;
        
        public MenuButtonListener(Menu menu, String buttonPressed)
        {
            this.buttonPressed = buttonPressed;
            this.menu = menu;
        }
        
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if(buttonPressed == "newgame") {
                menu.setVisible(false);
                new GameManager(menu);
            } else if(buttonPressed == "load") {
            } else if(buttonPressed == "leveleditor") {
                new Editor();
            } else if(buttonPressed == "options") {
            } else if(buttonPressed == "credits") {
                JOptionPane.showMessageDialog(null, "Designed and programmed by Paul Hogg & James Southworth 2012", "Credits", JOptionPane.INFORMATION_MESSAGE);
            } else if(buttonPressed == "exit") {
                //Exit normally
                System.exit(0);
            }
        }
        
    }
    
    /**
     * Main method, creates a new menu for the game
     */
    public static void main(String[] args)
    {
        new Menu();
    }
    
}

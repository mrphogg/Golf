/*
 * Manages a game of golf, handles leveling up and the players score throughout
 * the game.
 * 
 * When displayed initially, shows all the levels that can be played
 */
package golf;

import golf.LevelDescription;
import golf.entities.TileType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.*;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


/**
 * @author Paul
 */
public class GameManager extends JFrame
{
    //Menu that spawned the GameManager
    private Menu menu;
    //Width and height of the game manager window
    private final int WIDTH = 400;
    private final int HEIGHT = 150;
    //Two JLabels giving info on game
    private JLabel levelInfo, levelScore;
    
    //Currently selected level
    private int selectedLevel = 1;
    //How many levels have been unlocked
    private int maxLevel = 10;
    //How many levels are created
    private int levelCount = 2;
    
    //Arraylist of level descriptions
    private ArrayList<LevelDescription> levels;
    //Lowest pars for each level
    private ArrayList<Integer> levelScores;
    //Current game being player
    private Game currentGame;
    
    
    /**
     * Constructor for Game
     */
    public GameManager(Menu menu)
    {
        //Set JFrame title
        super("Golf");
        //Set the spawning menu
        this.menu = menu;
        //Set size
        setSize(WIDTH, HEIGHT);
        //Position in middle of screen
        setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 - WIDTH/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - HEIGHT/2));
        //Set layout to GridLayout
        setLayout(new GridLayout(3, 0, 5, 5));
        //Stop the user resizing the window
        setResizable(false);
        //Set the closing operation to the method exitGameManager
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener(){
            @Override
            public void windowOpened(WindowEvent we) {}
            @Override
            public void windowClosing(WindowEvent we)
            {
                 exitGameManager();
            }
            @Override
            public void windowClosed(WindowEvent we){}
            @Override
            public void windowIconified(WindowEvent we){}
            @Override
            public void windowDeiconified(WindowEvent we){}
            @Override
            public void windowActivated(WindowEvent we){}
            @Override
            public void windowDeactivated(WindowEvent we){}
        });
        //Create the levels array
        levels = new ArrayList<LevelDescription>();
        
        //Setup levels
        try {
            setupLevels();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found");
//            Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //Make the level scores array
        levelScores = new ArrayList<Integer>();
        //Fill it with 0 indicating no par yet achieved
        for(int i=0; i<maxLevel; i++) {
            levelScores.add(0);
        }
        
        //Add level info
        levelInfo = new JLabel();
        add(levelInfo);
        levelScore = new JLabel();
        add(levelScore);
        updateLevelLabels();
        
        
        //Buttons panel (<, Play Level, >)
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(new JButton("Play Level"){
            {
                this.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae)
                    {
                        playSelectedLevel();
                    }
                    
                });
            }
        }, BorderLayout.CENTER);
        
        //Previous level button
        buttonPanel.add(new JButton("<"){
            {
                this.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent ae)
                    {
                        selectPreviousLevel();
                    }
                    
                });
            }
        }, BorderLayout.WEST);
        
        
        //Next level button
        buttonPanel.add(new JButton(">"){
            {
                this.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent ae)
                    {
                        selectNextLevel();
                    }
                    
                });
            }
        }, BorderLayout.EAST);
        
        add(buttonPanel);
        
        //Show the Game window
        setVisible(true);
    }
    
    /**
     * Starts the level the player is currently on
     */
    private void playSelectedLevel()
    {
        setVisible(false);
        currentGame = new Game(levels.get(selectedLevel-1), this);
    }
    
    /**
     * Selects the previous level if it exists and then updates the display
     */
    private void selectPreviousLevel()
    {
        if(selectedLevel > 1) {
            selectedLevel--;
        }
        updateLevelLabels();
    }
    
    /**
     * Selects the next level if it exists and then updates the display
     */
    private void selectNextLevel()
    {
        if(selectedLevel < levels.size() && selectedLevel < maxLevel) {
            selectedLevel++;
        }
        updateLevelLabels();
    }
    
    /**
     * Exits the current level (Usually is called from the actual current level)
     * If the game was exited because the player won, the par-amater
     * represents how many hits the player made.
     * If the game was lost or quit then the par is -1
     */
    public void exitCurrentLevel(int par)
    {
        //Update the par if the level was completed
        System.out.println(par);
        System.out.println(levelScores.get(selectedLevel-1));
        if(par >= 0) {
            //If the level's never been played before
            if(levelScores.get(selectedLevel-1) == 0) {
                levelScores.set(selectedLevel - 1, par);
            } else if(par < levelScores.get(selectedLevel-1)) {
                levelScores.set(selectedLevel-1, par);
            }
        }
        //Update labels to reflect new par
        updateLevelLabels();
        
        currentGame.stopGameRunning();
        currentGame.dispose();
        //Display this screen again
        setVisible(true);
    }
    
    /**
     * Updates the labels in the frame (Level description and par)
     */
    private void updateLevelLabels()
    {
        levelInfo.setText("Level: "+selectedLevel+" - "+levels.get(selectedLevel-1).getName());
        levelScore.setText("Best Par: "+levelScores.get(selectedLevel-1)+" / "+levels.get(selectedLevel-1).getPar());
    }
    
    /**
     * Exits the current game manager and brings the menu back up
     */
    private void exitGameManager()
    {
        menu.setVisible(true);
        dispose();
    }
    
    /**
     * Populates the levels array with all the LevelDescription objects of the
     * levels in the game.
     * 
     * Game levels are located in levels/l[0-n].lvl
     * 
     * Only loads in levelCount levels
     */
    private void setupLevels() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        for(int i=1; i<=levelCount; i++) {
            String levelFileName = "levels/l"+i+".lvl";
            FileInputStream fis = new FileInputStream(levelFileName);
            ObjectInputStream input = new ObjectInputStream(fis);

            LevelDescription level = (LevelDescription) input.readObject();

            input.close();
            fis.close();


            levels.add(level);
        }
    }
    
    /**
     * Class for the GameManager frame that handles the pressing of buttons (eg. Selects
     * different levels by pressing left, right, A & D and plays the level by pressing
     * space)
     */
    private class GameManagerKeyListener implements KeyListener
    {

        @Override
        public void keyTyped(KeyEvent e){}

        @Override
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                selectPreviousLevel();
            } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                selectNextLevel();
            } else if(key == KeyEvent.VK_SPACE) {
                playSelectedLevel();
            }
        }

        @Override
        public void keyReleased(KeyEvent e){}
        
    }
}

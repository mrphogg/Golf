/*
 * Creates a level of golf based on a LevelDescription passed in and plays it
 */
package golf;

import golf.editor.Editor;
import golf.entities.Ball;
import golf.entities.Tile;
import golf.entities.TileType;
import golf.entities.tiles.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author Paul
 */
public class Game extends JFrame
{
    
    //Padding values to account for size of frame being measured from top left corner
    private final int WIDTH_PADDING = 7;
    private final int HEIGHT_PADDING = 29;
    //Amount of time to delay for between redrawing frames
    private final int REFRESH_TIME = 20;
    //Pixel height & width of game
    private int pixelWidth, pixelHeight;
    
    //Level Description object for game
    private LevelDescription level;
    //GameManager which spawned this Game
    private Object containerObject;
    //Size of game in tiles
    private int WIDTH, HEIGHT;
    //Name of game
    private String levelName;
    //Position of ball in pixels
    private int ballX, ballY;
    //Par of the level
    private int par;
    //Score of the player
    private int score;
    
    //Arraylist of Tile
    private ArrayList<Tile> tiles;
    //Golf ball for the game
    private Ball ball;
    //Info panel background
    private BufferedImage tempInfoBackground;
    //Stretched info panel
    private Image infoBackground;
    
    //GameEngine object the Game uses
    private GameEngine gameEngine;
    //GamePanel object the Game draws to
    private GamePanel gamePanel;
    //Whether the game is running
    private boolean gameRunning = true;
    //Whether the game is paused
    private boolean gamePaused = false;
    
    //Stage of which the player is aiming:
    private AimingStage aimingStage = AimingStage.NOT_AIMING;
    //Direction the aiming arrow is moving
    private AimingDirection aimingDirection = AimingDirection.STATIONARY;
    //Angle which the player is aiming in
    private int aimingAngle = 0;
    //Speed the player is aimiing
    private int aimingSpeed = 20;
    //Which way the speed meter is moving
    private int aimingSpeedChange = 2;
    
    public Game(LevelDescription level, Object containerObject)
    {
        //Set the title of the JFrame to the name of the level
        super(level.getName());
        //Store the level description
        this.level = level;
        //Store the GameManager
        this.containerObject = containerObject;
        //Set the WIDTH and HEIGHT variables
        WIDTH = level.getWidth();
        HEIGHT = level.getHeight();
        //Calculate the pixel size of the game
        pixelWidth = WIDTH*25;
        pixelHeight = HEIGHT*25;
        
        //Set the size of the appropriately (Each tile is 25 px)
        setSize(pixelWidth+WIDTH_PADDING, pixelHeight+HEIGHT_PADDING+50);
        //Position in middle of screen
        setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 - (WIDTH*25)/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - (HEIGHT*25)/2));
        //Stop the user resizing
        setResizable(false);
        //Show the window
        setVisible(true);
        
        //Setup variables from levelDescription
        this.levelName = level.getName();
        this.ballX = level.getBallX();
        this.ballY = level.getBallY();
        this.par = level.getPar();
        
        //Set up array of tiles
        tiles = new ArrayList<Tile>();
        //Create ball
        ball = new Ball(0, 0);
        //Create the background of the info panel
        try {
            tempInfoBackground = ImageIO.read(new File("resources/infopanel.png"));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Stretch infoBackground 
        infoBackground = tempInfoBackground.getScaledInstance(WIDTH*25, 50, Image.SCALE_SMOOTH);
        
        setUpGame();
        //Setup game engine and start it
        gameEngine = new GameEngine(this);
        gameEngine.start();
        //Setup game panel
        gamePanel = new GamePanel(this);
        //Add the panel to the Game window
        add(gamePanel);
        //Add a listener for the keys to the window
        addKeyListener(new GameKeyListener(this));
        //Stop the window closing when you press the X button
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Add a window listener to deal with closing the window
        addWindowListener(new GameWindowListener());
    }
    
    /**
     * Sets up the game by:
     * Setting the tiles array to contain all the correct tiles
     * Setting the x and y of the ball
     * Setting the score to 0
     */
    public void setUpGame()
    {
        //Clear the tiles array for the case that this is being run for the second time
        tiles.clear();
        for(int y=0; y<HEIGHT; y++) {
            for(int x=0; x<WIDTH; x++) {
                //Create and add the correct class with appropriate paramters to the tiles array
                TileType type = level.getTiles().get((y*WIDTH)+x);
                //Calculate the tile x and y position once here and use later
                int tileX = x*25;
                int tileY = y*25;
                if(type == TileType.GRASS) {
                    tiles.add(new Grass(tileX, tileY));
                } else if(type == TileType.HILL_UP) {
                    tiles.add(new Hill(tileX, tileY, HillDirection.UP));
                } else if(type == TileType.HILL_DOWN) {
                    tiles.add(new Hill(tileX, tileY, HillDirection.DOWN));
                } else if(type == TileType.HILL_RIGHT) {
                    tiles.add(new Hill(tileX, tileY, HillDirection.RIGHT));
                } else if(type == TileType.HILL_LEFT) {
                    tiles.add(new Hill(tileX, tileY, HillDirection.LEFT));
                } else if(type == TileType.WALL) {
                    tiles.add(new Wall(tileX, tileY));
                } else if(type == TileType.WATER) {
                    tiles.add(new Water(tileX, tileY));
                } else if(type == TileType.SAND) {
                    tiles.add(new Sand(tileX, tileY));
                } else if(type == TileType.ICE) {
                    tiles.add(new Ice(tileX, tileY));
                } else if(type == TileType.HOLE) {
                    tiles.add(new Hole(tileX, tileY));
                } else if(type == TileType.SWITCH_ON) {
                    tiles.add(new Switch(tileX, tileY, true));
                } else if(type == TileType.SWITCH_OFF) {
                    tiles.add(new Switch(tileX, tileY, false));
                } else if(type == TileType.SWITCH_WALL_ON) {
                    tiles.add(new SwitchWall(tileX, tileY, true));
                } else if(type == TileType.SWITCH_WALL_OFF) {
                    tiles.add(new SwitchWall(tileX, tileY, false));
                } else if(type == TileType.BLACKHOLE) {
                    tiles.add(new Blackhole(tileX, tileY));
                } else if(type == TileType.FORCEFIELD) {
                    tiles.add(new Forcefield(tileX, tileY));
                }
            }
        }
        //Set correct ball position
        
        ball.setX((level.getBallX()-1)*25+5);
        ball.setY((level.getBallY()-1)*25+5);
        ball.resetSpeed();
        //How many times the player's hit the ball
        score = 0;
    }
    /**
     * Private class to deal with running the game that's passed in
     */
    private class GameEngine extends Thread {
        
        //Current Game object which is using the GameEngine
        private Game game;
        
        public GameEngine(Game game)
        {
            this.game = game;
        }
        
        @Override
        public void run()
        {
            //Only update if the game is running
            while(game.gameRunning) {
                if(!gamePaused) {
                    //Update all the tiles
                    for(int i=0; i<tiles.size(); i++) {
                        Tile t = tiles.get(i);
                        //Update each tile (Possibly removable)
                        t.update();
                        //If the tile's bounding rectangle contains the centre of the ball
                        if(t.contains((int) game.ball.getCentreX(), (int) game.ball.getCentreY())) {
                            //Set friction of the ball if the tile has a friction value
                            if(t.getFriction()!=0) {
                                game.ball.setFriction(t.getFriction());
                            }
                            //Specific collision rules for tiles
                            if(t instanceof Hill) {
                                //t needs to be cast to access Hill specific methods
                                game.ball.changeXSpeed(((Hill)t).getHillXSpeed());
                                game.ball.changeYSpeed(((Hill)t).getHillYSpeed());
                            } else if(t instanceof Water) {
                                //IN THE DRINK
                                loseGame();
                            } else if(t instanceof Hole) {
                                double distance = Math.sqrt(Math.pow(t.getCentreX()-ball.getCentreX(), 2) + Math.pow(t.getCentreY()-ball.getCentreY(), 2));
                                if(distance < 8 && ball.getSpeed() < 4) {
                                    winGame();
                                }

                                ball.changeXSpeed((t.getCentreX()-ball.getCentreX())/20);
                                ball.changeYSpeed((t.getCentreY()-ball.getCentreY())/20);
                            }
                        }
                        //Flip switches if the ball roles over them
                        if(t instanceof Switch) {
                            if(t.contains((int) game.ball.getCentreX(), (int) game.ball.getCentreY())) {
                                if(!((Switch)t).isBallHitting()) {
                                    ((Switch)t).flip();
                                    for(int j=0; j<tiles.size(); j++) {
                                        Tile sw = tiles.get(j);
                                        if(sw instanceof SwitchWall) {
                                            ((SwitchWall)sw).flip();
                                        }
                                    }
                                    ((Switch)t).setBallHitting(true);
                                }
                            } else {
                                if(((Switch)t).isBallHitting()) {
                                    ((Switch)t).setBallHitting(false);
                                }
                            }
                        }
                        //Specific collisions for bouncing off walls
                        if(t instanceof Wall || (t instanceof SwitchWall && ((SwitchWall)t).isSwitchedOn())) {
                            //Left side of ball
                            if(t.contains(ball.getX(),ball.getCentreY())) {
                                ball.flipXSpeed();
                            }
                            //Right side of ball
                            if(t.contains(ball.getMaxX(),ball.getCentreY())) {
                                ball.flipXSpeed();
                            }
                            //Top side of ball
                            if(t.contains(ball.getCentreX(),ball.getY())) {
                                ball.flipYSpeed();
                            }
                            //Bottom side of ball
                            if(t.contains(ball.getCentreX(),ball.getMaxY())) {
                                ball.flipYSpeed();
                            }   
                        }
                        //Pull the ball into a blackhole
                        if(t instanceof Blackhole) {
                            //Calculate distance from ball
                            double distance = Math.sqrt(Math.pow(ball.getCentreX()-t.getCentreX(), 2) + Math.pow(ball.getCentreY() - t.getCentreY(), 2));
                            //Calculate strength of pull (less as gets further away)
                            double factor = 5/distance;
                            //Calculate angle towards ball
                            double xDif = t.getCentreX() - ball.getCentreX();
                            double yDif = t.getCentreY() - ball.getCentreY();
                            double direction = Math.atan2(yDif, xDif)+(Math.PI/2);
                            //Only pull in ball if within 10 and 100 pixels away
                            if(distance <100 && distance > 10) {
                                ball.changeXSpeed(factor*Math.sin(direction));
                                ball.changeYSpeed(factor*Math.cos(direction)*-1);
                            }
                        }
                        //Push the ball away from a forcefield
                        if(t instanceof Forcefield) {
                            //Calculate distance from ball
                            double distance = Math.sqrt(Math.pow(ball.getCentreX()-t.getCentreX(), 2) + Math.pow(ball.getCentreY() - t.getCentreY(), 2));
                            //Calculate strength of pull (less as gets further away)
                            double factor = 10/distance;
                            //Calculate angle away from ball
                            double xDif = t.getCentreX() - ball.getCentreX();
                            double yDif = t.getCentreY() - ball.getCentreY();
                            double direction = Math.atan2(yDif, xDif)-(Math.PI/2);
                            //Only pull in ball if within 10 and 100 pixels away
                            if(distance <100 && distance > 10) {
                                ball.changeXSpeed(factor*Math.sin(direction));
                                ball.changeYSpeed(factor*Math.cos(direction)*-1);
                            }
                        }
                    }


                    //Bounce off edges of screen
                    if(game.ball.getX() < 0) {
                        ball.setX(0);
                        ball.flipXSpeed();
                    } else if(ball.getMaxX() > pixelWidth) {
                        ball.setX(pixelWidth - ball.getWidth());
                        ball.flipXSpeed();
                    }
                    if(ball.getY() < 0) {
                        ball.setY(0);
                        ball.flipYSpeed();
                    } else if(ball.getMaxY() > pixelHeight) {
                        ball.setY(pixelHeight - ball.getHeight());
                        ball.flipYSpeed();
                    }

                    //Update the ball if the player's not aiming
                    if(game.aimingStage == AimingStage.NOT_AIMING) {
                        //Move the ball (+speed and *friction)
                        game.ball.update();
                    } else if(game.aimingStage == AimingStage.AIMING_DIRECTION) {
                        //Move the arrow if the aiming stage is 1
                        if(game.aimingDirection == AimingDirection.CLOCKWISE) {
                            game.aimingAngle += 4;
                        } else if(game.aimingDirection == AimingDirection.ANTICLOCKWISE) {
                            game.aimingAngle -= 4;
                        }
                    } else if(game.aimingStage == AimingStage.AIMING_SPEED) {
                        //Control choosing the speed of the ball
                        //Make the aimingSpeed go up and down from 0 to 100
                        if(game.aimingSpeed+game.aimingSpeedChange > 100) {
                            game.aimingSpeedChange *= -1;
                        } else if(game.aimingSpeed+game.aimingSpeedChange <0) {
                            game.aimingSpeedChange *= -1;
                        }
                        game.aimingSpeed += game.aimingSpeedChange;
                    }


                    //Repaing the game
                    game.repaint();


                    //Waiting for a bit
                    try {
                        Thread.sleep(REFRESH_TIME);
                    } catch (InterruptedException ex) {
                        System.err.println("Interrupted while sleeping");
                    }
                }
            }
        }
    }
    
    /**
     * JPanel class that just draws all the stuff in the game passed in
     */
    private class GamePanel extends JPanel {
        
        //Current Game object which is using the GamePanel
        private Game game;
        
        public GamePanel(Game game)
        {
            this.game = game;
            setDoubleBuffered(true);
        }
        
        @Override
        public void paintComponent(Graphics g)
        {
            //Cast g to a Graphics2D object for great justice
            Graphics2D g2 = (Graphics2D) g;
            //Paint each tile
            for(Tile t : game.tiles) {
                t.paintComponent(g2);
            }
            //Change the pen thickness and colour to dark red
            g2.setColor(new Color(0, 75, 140));
            g2.setStroke(new BasicStroke(4));
            //Paint an arrow on the ball if the player is aiming
            if(game.aimingStage != AimingStage.NOT_AIMING) {
                //Draw a line from the centre of the ball to 50 px away in the direction of aimingAngle
                g2.drawLine((int) game.ball.getCentreX(), (int) game.ball.getCentreY(), getPointFromBall(game.aimingAngle, game.aimingSpeed).x, getPointFromBall(game.aimingAngle, game.aimingSpeed).y);   
                g2.drawLine(getPointFromBall(game.aimingAngle, game.aimingSpeed).x, getPointFromBall(game.aimingAngle, game.aimingSpeed).y, getPointFromBall(game.aimingAngle+15, game.aimingSpeed*0.8).x, getPointFromBall(game.aimingAngle+15, game.aimingSpeed*0.8).y);   
                g2.drawLine(getPointFromBall(game.aimingAngle, game.aimingSpeed).x, getPointFromBall(game.aimingAngle, game.aimingSpeed).y, getPointFromBall(game.aimingAngle-15, game.aimingSpeed*0.8).x, getPointFromBall(game.aimingAngle-15, game.aimingSpeed*0.8).y);   
            }
            //Set the colour back to black
            g2.setColor(Color.BLACK);
            //Paint the ball
            ball.paintComponent(g2);
            //Paint the infopanel background
            g2.drawImage(infoBackground, 0, game.pixelHeight, null);
            //Draw the level name on
            g2.drawString("Level: " + game.levelName, 10, game.pixelHeight+30);
            //Draw the par
            g2.drawString("Par: " + game.score + " / " + game.par, 200, game.pixelHeight+30);
            
            
            //Code below is for drawing the power bar
            //Set the stroke of the pen to thickness of 2 for the power bar
            g2.setStroke(new BasicStroke(2));
            //Define the power bar coordinates relative to a start position and dimensions
            //PowerBarX and Y are the bottom left coordinates
            int powerBarX = 300;
            int powerBarY = game.pixelHeight+35;
            //Experiment 
            //int powerBarX = (int) ball.getX()+30;
            //int powerBarY = (int) ball.getY()-10;
            //Width of full powerbar
            int powerBarWidth = 80;
            //Heights of powerbar. (minHeight is the height on the left, maxHeight on the right)
            int powerBarMinHeight = 10;
            int powerBarMaxHeight = 20;
            //Draw the filled in polygon if the player is choosing the speed
            if(aimingStage == AimingStage.AIMING_SPEED) {
                //Set colour to depending on aimingSpeed (0=Red, 100=Green) add a bit of blue for lulz
                g2.setColor(new Color((int)(aimingSpeed*2.55), (int)((100-aimingSpeed)*2.55), 30));
                //Array of x and y points for filled bar
                int powerBarFillXValues[] = {powerBarX, powerBarX, powerBarX+((powerBarWidth*aimingSpeed)/100), powerBarX+((powerBarWidth*aimingSpeed)/100)};
                int powerBarFillYValues[] = {powerBarY, powerBarY-powerBarMinHeight, powerBarY-powerBarMinHeight-((powerBarMaxHeight-powerBarMinHeight)*aimingSpeed)/100, powerBarY};
                //Fill in a polygon
                g2.fillPolygon(powerBarFillXValues, powerBarFillYValues, 4);
                //Set colour back to black
                g2.setColor(Color.BLACK);
            }
            //Make the array of x points and y points
            int powerBarXValues[] = {powerBarX, powerBarX, powerBarX+powerBarWidth, powerBarX+powerBarWidth};
            int powerBarYValues[] = {powerBarY, powerBarY-powerBarMinHeight, powerBarY-powerBarMaxHeight, powerBarY};
            //Draw the outline of the powerbar
            g2.drawPolygon(powerBarXValues, powerBarYValues, 4);
            
            
        }
    }
    
    /**
     * Returns a Point object that is distance pixels away from the centre of the
     * golf ball in the specified direction (in degrees with north being 0 degrees)
     * @param direction     Direction of point
     * @param distance      How far away the point is
     * @return              Point object describing the position
     */
    private Point getPointFromBall(int direction, double distance)
    {
        return new Point((int) (ball.getCentreX() + (Math.sin((Math.PI/180)*direction)) * distance), (int) (ball.getCentreY() - (Math.cos((Math.PI/180)*direction)) * distance));
    }
    
    /**
     * Deals with the Key pressed in the game
     */
    private class GameKeyListener implements KeyListener {
        
        //Game the class is running in
        private Game game;
        
        public GameKeyListener(Game game)
        {
            this.game = game;
        }

        @Override
        public void keyTyped(KeyEvent e){}

        @Override
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            //Only move the arrow if the player is aiming (Aiming stage 1)
            if(aimingStage == AimingStage.AIMING_DIRECTION) {
                if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                    game.aimingDirection = AimingDirection.ANTICLOCKWISE;
                } else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    game.aimingDirection = AimingDirection.CLOCKWISE;
                }
            }
            //Shoot the ball
            if(key == KeyEvent.VK_SPACE) {
                if(game.aimingStage == AimingStage.NOT_AIMING) {
                    //If ball is still enough to be hit
                    if(game.ball.getSpeed() < 0.1) {
                        game.aimingStage = AimingStage.AIMING_DIRECTION;
                    }
                } else if(game.aimingStage == AimingStage.AIMING_DIRECTION) {
                    //Change from aiming direction to aiming speed
                    game.aimingStage = AimingStage.AIMING_SPEED;
                } else if(game.aimingStage == AimingStage.AIMING_SPEED) {
                    //Hit the ball
                    ball.hit(game.aimingAngle, game.aimingSpeed/7);
                    //Add 1 hit to player par (score)
                    game.score ++;
                    //Reset aiming vars
                    game.aimingSpeed = 20;
                    game.aimingSpeedChange = 2;
                    game.aimingStage = AimingStage.NOT_AIMING;
                }
            }
            //Cancel aiming completely
            if(key == KeyEvent.VK_ESCAPE) {
                //Only if player is aiming cancel the aiming
                if(game.aimingStage != AimingStage.NOT_AIMING) {
                    //Reset aiming vars
                    game.aimingSpeed = 20;
                    game.aimingSpeedChange = 2;
                    game.aimingStage = AimingStage.NOT_AIMING;
                } else {
                    //Exit the game
                    exitGamePrompt();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                if(game.aimingDirection == AimingDirection.ANTICLOCKWISE) {
                    game.aimingDirection = AimingDirection.STATIONARY;
                }
            } else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                if(game.aimingDirection == AimingDirection.CLOCKWISE) {
                    game.aimingDirection = AimingDirection.STATIONARY;
                }
            }
        }
    }
    
    /**
     * Deal with game over from dying
     */
    public void loseGame()
    {
        //Stop the game engine thread from running
        gameRunning = false;
        //Options for the JOptionPane
        String[] options = {"Replay", "Exit"};
        //Make a JOptionPane and store the response
        Object response = JOptionPane.showOptionDialog(null,
                            "Game over...", "Game over man",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                            null, options, "Replay");
        
        //Deal with it
        if(response.equals(0)) {
            //Replay
            setUpGame();
            gameRunning = true;
        } else if(response == 1) {
            if(containerObject instanceof GameManager) {
                ((GameManager)containerObject).exitCurrentLevel(-1);
            } else if(containerObject instanceof Editor) {
                gameRunning = false;
                dispose();
            }
        }
    }
    
    /**
     * Deal with winning the game
     */
    public void winGame()
    {
        //Stop game from running
        gameRunning = false;
        
        JOptionPane.showMessageDialog(null, "Congratulations!", "You win", JOptionPane.INFORMATION_MESSAGE);
        
        
        if(containerObject instanceof GameManager) {
            ((GameManager)containerObject).exitCurrentLevel(score);
        } else if (containerObject instanceof Editor) {
            gameRunning = false;
            dispose();
        }
    }
    
    /**
     * Exit the game (by closing the window or pressing escape)
     */
    public void exitGamePrompt()
    {
        
        //Are you sure you want to exit?
        Object response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit Level", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if(response == 0) {
            if(containerObject instanceof GameManager) {
                //Tell the gameManager to close the window and that the level was not completed
                ((GameManager)containerObject).exitCurrentLevel(-1);
            } else if(containerObject instanceof Editor) {
                gameRunning = false;
                dispose();
            }
        } else if(response == 1) {
            
        } else {
            System.err.println("Invalid exit choice");
        }
    }
    
    /**
     * Stops the GameEngine thread from running when called
     */
    public void stopGameRunning()
    {
        gameRunning = false;
    }
    
    /**
     * Window listener class to deal with exiting
     */
    private class GameWindowListener implements WindowListener
    {

        @Override
        public void windowOpened(WindowEvent we){}

        @Override
        public void windowClosing(WindowEvent we) {
            exitGamePrompt();
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
        
    }
    
    /**
     * The directions the player can move the aiming arrow in
     */
    private enum AimingDirection {
        CLOCKWISE, ANTICLOCKWISE, STATIONARY;
    }
    
    /**
     * The stages which the aiming process can be in
     * Not aiming = Ball is rolling or resting
     * Aiming direction = player is controlling the arrow clockwise or anticlockwise
     * aiming speed = player is choosing a speed for the ball
     */
    private enum AimingStage {
        NOT_AIMING, AIMING_DIRECTION, AIMING_SPEED;
    }
}

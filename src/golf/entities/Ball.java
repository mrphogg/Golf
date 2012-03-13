/*
 * Golf ball class
 */
package golf.entities;

import golf.entities.GameEntity;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Paul
 */
public class Ball extends GameEntity
{
    //Vars for the ball
    private double xSpeed, ySpeed;
    private double friction;
    private BufferedImage image;
    
    public Ball(double x, double y)
    {
        super(x, y, 15, 15);
        
        this.xSpeed = 0;
        this.ySpeed = 0;
        friction = 0.98;
        try {
            image = ImageIO.read(new File("resources/ball.png"));
        } catch (IOException ex) {
            Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Fires the ball in a specified direction and speed
     * @param angle     Angle of firing in degrees
     * @param speed     Speed to fire ball
     */
    public void hit(int angle, double speed)
    {
        xSpeed = (Math.sin(angle*Math.PI/180)*speed);
        ySpeed = (-Math.cos(angle*Math.PI/180)*speed);
    }
    
    /**
     * Resets the balls speed to still
     */
    public void resetSpeed()
    {
        xSpeed = 0;
        ySpeed = 0;
    }
    
    
    
    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(image, (int) getX(), (int) getY(), null);
    }
    
    @Override
    public void update()
    {
        setX(getX() + xSpeed);
        setY(getY() + ySpeed);
        xSpeed *= friction;
        ySpeed *= friction;
    }
    
    /**
     * Return the vector length of the speed
     * @return     Speed of the ball
     */
    public double getSpeed()
    {
        return Math.sqrt(Math.pow(xSpeed, 2)+Math.pow(ySpeed, 2));
    }
    
    public void setFriction(double newFriction)
    {
        friction = newFriction;
    }
    
    public void changeXSpeed(double deltaX)
    {
        xSpeed += deltaX;
    }
    
    public void changeYSpeed(double deltaY)
    {
        ySpeed += deltaY;
    }
    
    public void flipXSpeed()
    {
        xSpeed *= -1;
    }
    
    public void flipYSpeed()
    {
        ySpeed *=  -1;
    }
}

/*
 * Describes a generic object in the game
 */
package golf.entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author Paul
 */
public abstract class GameEntity
{
    //Position variables
    private double x, y;
    //Size
    private int width, height;
    
    public GameEntity(double x, double y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Returns the Rectangle bounding the GameEntity
     */
    public Rectangle getRectangle()
    {
        return new Rectangle((int) x, (int) y, width, height);
    }
    
    /**
     * Calculates if this GameEntity intersects with another
     * @return whether this intersects argument
     * @param ge GameEntity to test for collision
     */
    public boolean intersects(GameEntity ge)
    {
        return getRectangle().intersects(ge.getRectangle());
    }
    
    /**
     * Calculates if the bounding rectangle of the entity contains a point
     * @param x         X position to check for
     * @param y         Y position to check for
     * @return          True if the bounding rectangle contains the point (x, y)
     */
    public boolean contains(double x, double y)
    {
        return getRectangle().contains(x, y);
    }
    
    /**
     * Paints a component to a graphics object
     * @param g Graphics object to paint on
     */
    public void paintComponent(Graphics g)
    {
        //To be overridden by individual components
    }
    
    /**
     * Moves the object one unit
     */
    public void update()
    {
        //To be overridden by individual components
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }
    
    public double getMaxX()
    {
        return x+width;
    }
    
    public double getMaxY()
    {
        return y+height;
    }
    
    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }
    
    public double getCentreX()
    {
        return x+(width/2);
    }
    
    public double getCentreY()
    {
        return y+(height/2);
    }
    
}

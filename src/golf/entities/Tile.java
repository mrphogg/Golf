/*
 * Describes a general tile in the game
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
 * @author Paul
 */
public class Tile extends GameEntity
{
    //Name of the tile's file
    private TileType fileName;
    //Type of the tile (Can be different from fileName because of things like HILL_UP = HILL
    private TileType tileType;
    
    private BufferedImage image;
    //A friction of 0 means the tile has no friction property
    private double friction = 0;
    
    /**
     * Constructor for a Tile object
     * @param x         x position of tile
     * @param y         y position of tile
     * @param tileType  type of the tile
     *                  In this constructor the filename of the image is set
     *                  as the name of the tileType (i.e. the file is called the
     *                  type of the tile, no funny business with HILL_UP etc)
     */
    public Tile(int x, int y, TileType tileType)
    {
        //Call GameEntity with x, y and tile size
        super(x, y, 30, 30);
        
        //Set the filename to the tile type
        this.tileType = fileName = tileType;
        
        //Load the image
        try {
            image = ImageIO.read(new File("resources/"+fileName.name().toLowerCase()+".png"));
        } catch (IOException ex) {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Constructor for a Tile object where the filename is different from the
     * tile type
     * @param x             x position of tile
     * @param y             y position of tile
     * @param tileType      type of the tile
     * @param fileName      filename of the tile
     */
    public Tile(int x, int y, TileType tileType, TileType fileName)
    {
        //Call GameEntity with x, y and tile size
        super(x, y, 30, 30);
        
        //Set the tile type and file name separately
        this.tileType = tileType;
        this.fileName = fileName;
        
        try {
            image = ImageIO.read(new File("resources/"+fileName.name().toLowerCase()+".png"));
        } catch (IOException ex) {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
    public void setImage(TileType fileName)
    {
        try {
            image = ImageIO.read(new File("resources/"+fileName.name().toLowerCase()+".png"));
        } catch (IOException ex) {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    **/
    public TileType getTileType()
    {
        return tileType;
    }
    
    /**
    public void setTileType(TileType tileType)
    {
        this.tileType = tileType;
    }
    **/
    public void setFriction(double newFriction)
    {
        friction = newFriction;
    }
    
    public double getFriction()
    {
        return friction;
    }
    /**
    public TileType getFileName()
    {
        return fileName;
    }
    **/
    
    
    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(image, (int) getX(), (int) getY(), null);
    }
}

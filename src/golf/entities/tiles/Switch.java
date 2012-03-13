/*
 * Switch tile
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;
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
public class Switch extends Tile
{
    private boolean switchedOn;
    //Whether the ball is hitting the switch
    private boolean ballHitting = false;
    
    /**
     * Create two new images for off and on.
     * Perhaps a bit wasteful creating new images instead of using the one
     * in Tile but it's neater and easier
     **/
    private BufferedImage onImage, offImage;
    
    
    
    public Switch(int x, int y, boolean switchedOn)
    {
        super(x, y, TileType.SWITCH, switchedOn? TileType.SWITCH_ON : TileType.SWITCH_OFF);
        
        this.switchedOn = switchedOn;
        
        setFriction(Grass.grassFriction);
        
        //Load the two images
        try {
            onImage = ImageIO.read(new File("resources/switch_on.png"));
            offImage = ImageIO.read(new File("resources/switch_off.png"));
        } catch (IOException ex) {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void flip()
    {
        switchedOn = switchedOn?false:true;
    }
    
    //Override paintComponent in Tile to draw the correct switch position tile
    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(switchedOn?onImage:offImage, (int) getX(), (int) getY(), null);
    }

    public boolean isBallHitting()
    {
        return ballHitting;
    }

    public void setBallHitting(boolean ballHitting)
    {
        this.ballHitting = ballHitting;
    }
    
    
}

/*
 * A wall in the game controlled by a switch. Can be on or off
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
public class SwitchWall extends Tile
{
    private boolean switchedOn;
    
    /**
     * Create two new images for off and on.
     * Perhaps a bit wasteful creating new images instead of using the one
     * in Tile but it's neater and easier
     **/
    private BufferedImage onImage, offImage;
    
    
    
    public SwitchWall(int x, int y, boolean switchedOn)
    {
        super(x, y, TileType.SWITCH_WALL, switchedOn?TileType.SWITCH_WALL_ON:TileType.SWITCH_WALL_OFF);
        
        this.switchedOn  = switchedOn;
        
        //Load the two images
        try {
            onImage = ImageIO.read(new File("resources/switch_wall_on.png"));
            offImage = ImageIO.read(new File("resources/switch_wall_off.png"));
        } catch (IOException ex) {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void flip()
    {
        switchedOn = switchedOn ? false : true;
    }

    public boolean isSwitchedOn()
    {
        return switchedOn;
    }
    
    /**
     * Override paintComponent method in tile to draw the tile correctly
     */
    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(switchedOn?onImage:offImage, (int) getX(), (int) getY(), null);
    }
}

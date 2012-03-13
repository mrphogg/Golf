/*
 * Standard Grass tile
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;

/**
 *
 * @author Paul
 */
public class Grass extends Tile
{
    
    public static double grassFriction = 0.98;
    
    public Grass(int x, int y)
    {
        super(x, y, TileType.GRASS);
        
        setFriction(grassFriction);
    }
}

/*
 * Forcefield tile
 * Pushes the ball away from the tile
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;

/**
 *
 * @author Paul
 */
public class Forcefield extends Tile
{
    private double pushStrength = 1;
    
    public Forcefield(int x, int y)
    {
        super(x, y, TileType.FORCEFIELD);
        
        setFriction(Grass.grassFriction);
    }

    public double getPushStrength()
    {
        return pushStrength;
    }
    
}

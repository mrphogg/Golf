/*
 * Golf hole tile
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;

/**
 *
 * @author Paul
 */
public class Hole extends Tile
{
    public Hole(int x, int y)
    {
        super(x, y, TileType.HOLE);
        
        
        setFriction(Grass.grassFriction);
    }
}

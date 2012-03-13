/*
 * Water tile
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;

/**
 *
 * @author Paul
 */
public class Water extends Tile
{
    public Water(int x, int y)
    {
        super(x, y, TileType.WATER);
        
    }
}

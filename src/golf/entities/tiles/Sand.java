/*
 * Sand tile
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;

/**
 *
 * @author Paul
 */
public class Sand extends Tile
{
    public Sand(int x, int y)
    {
        super(x, y, TileType.SAND);
        
        setFriction(0.9);
    }
}

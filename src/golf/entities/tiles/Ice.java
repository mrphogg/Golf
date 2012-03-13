/*
 * Ice tile
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;

/**
 *
 * @author Paul
 */
public class Ice extends Tile
{
    public Ice(int x, int y)
    {
        super(x, y, TileType.ICE);
        
        //setTileType(TileType.ICE);
        setFriction(0.99);
    }
}

/*
 * Wall tile
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;

/**
 *
 * @author Paul
 */
public class Wall extends Tile
{
    public Wall(int x, int y)
    {
        super(x, y, TileType.WALL);
        
    }
}

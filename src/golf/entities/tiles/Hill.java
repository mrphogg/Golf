/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;

/**
 *
 * @author Paul
 */
public class Hill extends Tile
{
    private HillDirection direction;
    private double hillSpeed = 0.1;
    private double hillXSpeed = 0;
    private double hillYSpeed = 0;
    
    
    public Hill(int x, int y, HillDirection hillDirection)
    {
        //Slight hack
        super(x, y, TileType.HILL, hillDirection == HillDirection.UP ? TileType.HILL_UP : hillDirection == HillDirection.LEFT ? TileType.HILL_LEFT : hillDirection == HillDirection.RIGHT ? TileType.HILL_RIGHT : TileType.HILL_DOWN);
        
        
        if(hillDirection == HillDirection.UP) {
            hillYSpeed = -hillSpeed;
        } else if(hillDirection == HillDirection.DOWN) {
            hillYSpeed = hillSpeed;
        } else if(hillDirection == HillDirection.LEFT) {
            hillXSpeed = -hillSpeed;
        } else if(hillDirection == HillDirection.RIGHT) {
            hillXSpeed = hillSpeed;
        }
        
        direction = hillDirection;
        
        setFriction(Grass.grassFriction);
    }
    
    
    public double getHillXSpeed()
    {
        return hillXSpeed;
    }

    public double getHillYSpeed()
    {
        return hillYSpeed;
    }
}

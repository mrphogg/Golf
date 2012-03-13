/*
 * Blackhole tile - makes the ball travel towards it with an inverse linear law
 */
package golf.entities.tiles;

import golf.entities.Tile;
import golf.entities.TileType;

/**
 *
 * @author Paul
 */
public class Blackhole extends Tile
{
    
    private double pullStrength = 0.01;
    
    public Blackhole(int x, int y)
    {
        super(x, y, TileType.BLACKHOLE);
        
        setFriction(Grass.grassFriction-0.2);
    }
    
    public double getPullStrength()
    {
        return pullStrength;
    }
    
}

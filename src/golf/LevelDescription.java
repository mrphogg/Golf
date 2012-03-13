/*
 * Contains all the data to describe a level of golf
 */
package golf;

import golf.entities.TileType;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Paul
 */
public class LevelDescription implements Serializable
{
    //All the level descriptions
    private String name;
    private int width, height; 
    private ArrayList<TileType> tiles;
    private int ballX, ballY;
    private int par;
    
    public LevelDescription(String name, int width, int height, ArrayList<TileType> tiles, int ballX, int ballY, int par)
    {
        //Set object variables to ones passed in
        this.name = name;
        this.width = width;
        this.height = height;
        this.tiles = tiles;
        this.ballX = ballX;
        this.ballY = ballY;
        this.par = par;
    }
    
    //Getters for all variables
    public int getBallX()
    {
        return ballX;
    }

    public int getBallY()
    {
        return ballY;
    }

    public int getHeight()
    {
        return height;
    }

    public String getName()
    {
        return name;
    }

    public int getPar()
    {
        return par;
    }

    public ArrayList<TileType> getTiles()
    {
        return tiles;
    }

    public int getWidth()
    {
        return width;
    }

    public void setBallX(int ballX)
    {
        this.ballX = ballX;
    }

    public void setBallY(int ballY)
    {
        this.ballY = ballY;
    }
    
    //Setters
    
    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPar(int par)
    {
        this.par = par;
    }

    public void setTiles(ArrayList<TileType> tiles)
    {
        this.tiles = tiles;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
    
}

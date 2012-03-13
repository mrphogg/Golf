package golf.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import golf.entities.TileType;

public class EditorTile implements Serializable
{
	TileType type;
	BufferedImage image;
	
	public EditorTile(TileType type)
	{
		this.type = type;
		setImage();
	}
	
	public void setType(TileType type){
		this.type = type;
		setImage();
	}
	
	private void setImage() {
		String imageToGet;
		/*switch(type)
		{
		case GRASS: imageToGet="Grass"; break;
		case SAND: imageToGet="Sand"; break;
		case WALL: imageToGet="Wall"; break;
		case WATER: imageToGet="Water"; break;
		case HILL_UP: imageToGet="Hill_Up"; break;
		case HILL_DOWN: imageToGet="Hill_Down"; break;
		case HILL_LEFT: imageToGet="Hill_Left"; break;
		case HILL_RIGHT: imageToGet="Hill_Right"; break;
		case SWITCH_ON: imageToGet="Switch_On"; break;
		case SWITCH_OFF: imageToGet="Switch_Off"; break;
		case ICE: imageToGet="ice"; break;
		case HOLE: imageToGet="hole"; break;
		default: imageToGet="Blank"; break;
		}*/
                imageToGet = type.name().toLowerCase();
		try {
			image =	ImageIO.read(new File("resources/"+imageToGet+".png"));
			// Do something with the image.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public TileType getType() {
		return type;
	}
}

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

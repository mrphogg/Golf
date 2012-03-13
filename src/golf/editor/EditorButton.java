package golf.editor;

import java.awt.Color;

import golf.entities.TileType;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;

public class EditorButton extends JButton {
	TileType type;
	boolean selected;
	Border defaultBorder, leftSelectBorder, rightSelectBorder;

	public EditorButton(Icon icon, TileType type){
            super(icon);
            this.type = type;
            selected = false;
            defaultBorder = this.getBorder();
            leftSelectBorder = BorderFactory.createLineBorder(Color.RED, 2);
            rightSelectBorder = BorderFactory.createLineBorder(Color.BLUE, 2);
	}
	
	public TileType getTileType() {
            return type;
	}
	
	public void setSelected(boolean b){
            if(b){
                this.setBorder(leftSelectBorder);
            } else {
                this.setBorder(defaultBorder);
            }
	}
}

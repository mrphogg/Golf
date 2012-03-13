package golf.editor;

import golf.Game;
import golf.LevelDescription;
import golf.editor.EditorTile;
import golf.entities.TileType;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;


public class Editor extends JPanel implements MouseListener, MouseMotionListener, ActionListener{

	private ArrayList<EditorTile> tileMap;
        private ArrayList<TileType> notEditorTiles;
	private LevelDescription level;
	private JFrame frame;
	private BufferedImage buffer;
	//private int mapySize, mapxSize;
	private TileType brush;
	EditorButton buttonSelected;
        //Position of ball button
        private JButton ballButton;
        //Whether the  ball is being placed
        private boolean placingBall = false;
        //Ball image to place
        private BufferedImage ballImage;
        //Drop down menus
        private JMenuBar menuBar;
        private JMenu menuFile, menuEdit;
        private JMenuItem saveMenuItem, loadMenuItem, testMenuItem, propertiesMenuItem, resizeMenuItem, resetMapMenuItem;
        
	//File chooser
        private JFileChooser fileChooser;
        //Current editor object
        private Editor editor;
        
        //Whether the ScreenDrawer class is drawing (Used for stopping the thread)
        private boolean isActive = true;
        
	public Editor(int xSize, int ySize){
                editor = this;
		
		tileMap = new ArrayList<EditorTile>();
		
		brush = TileType.GRASS;
		
		level = new LevelDescription("My Custom Level", xSize, ySize, new ArrayList<TileType>(), 1, 1, 4);
                
                //Create the File Chooser
                fileChooser = new JFileChooser();
                
                //Load ball image in
                try {
                    ballImage = ImageIO.read(new File("resources/ball_tile.png"));
                } catch (IOException ex) {
                    Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
		makeFrame();
		initMap();
		//drawScreen();
                ScreenDrawer screenDrawer = new ScreenDrawer();
                screenDrawer.start();
	}
	
        public Editor()
        {
            new Editor(15, 15);
        }
        
	public void makeFrame(){
		frame = new JFrame("Level Editor");
		int frameY = 410;
		while((level.getHeight()*25)+110>frameY){
			frameY+=25;
		}
		frame.setPreferredSize(new Dimension((level.getWidth()*25)+100,frameY));
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		this.setLayout(null);
		
                //Make the array containing TileTypes not for the editor (Switch, Hill, SwitchWall)
                notEditorTiles = new ArrayList<TileType>();
                notEditorTiles.add(TileType.HILL);
                notEditorTiles.add(TileType.SWITCH);
                notEditorTiles.add(TileType.SWITCH_WALL);
		/*
		 * Draw the buttons
		 */
		int i = 0;
		int j = 0;
		EditorButton button;
		for(TileType t : TileType.values())
		{
                    if(!notEditorTiles.contains(t)) {
			button = new EditorButton(new ImageIcon("resources/"+t.name().toLowerCase()+"ICO.jpg"),t);
			button.addActionListener(this);
			this.add(button);
			button.setBounds(((level.getWidth()*25)+20)+(40*(i%2)),20+(40*j),25,25);
			if(t==TileType.GRASS){
				button.setSelected(true);
				buttonSelected = button;
			}
			j+=i%2;
			i++;
                    }
		}
		//Draw the ball button
                ballButton = new JButton(new ImageIcon("resources/ballICO.jpg") {
                    private Border defaultBorder, leftSelectBorder;
                    public void setSelected () {

                    }
                });
                ballButton.setBounds(((level.getWidth()*25)+20)+(40*(i%2)),20+(40*j),25,25);
                ballButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        placingBall = true;
                    }
                });
                add(ballButton);
                
                
		/*
		 * MENUS
		 */
                
		menuBar = new JMenuBar();
		menuFile = new JMenu("File");
		menuEdit = new JMenu("Edit");
		
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuFile.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menuFile);
		menuBar.add(menuEdit);
		
                
                
		saveMenuItem = new JMenuItem("Save");
                saveMenuItem.addActionListener(new EditorMenuListener("file-save"));
                menuFile.add(saveMenuItem);
                
		loadMenuItem = new JMenuItem("Load");
                loadMenuItem.addActionListener(new EditorMenuListener("file-load"));
                menuFile.add(loadMenuItem);
		
		testMenuItem = new JMenuItem("Test");
                testMenuItem.addActionListener(new EditorMenuListener("file-test"));
                menuFile.add(testMenuItem);
                
                
                
                propertiesMenuItem = new JMenuItem("Properties");
                propertiesMenuItem.addActionListener(new EditorMenuListener("edit-properties"));
                menuEdit.add(propertiesMenuItem);
		
                resizeMenuItem = new JMenuItem("Resize Level");
                resizeMenuItem.addActionListener(new EditorMenuListener("edit-resize"));
                menuEdit.add(resizeMenuItem);
                
		resetMapMenuItem = new JMenuItem("Reset Map");
                resetMapMenuItem.addActionListener(new EditorMenuListener("edit-reset"));
                menuEdit.add(resetMapMenuItem);
		
		frame.setJMenuBar(menuBar);
		
		/*
		 * Size and make screen visible
		 */
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private void initMap() {
		for(int i=0; i<(level.getWidth()*level.getHeight()); i++)
		{
			this.tileMap.add(new EditorTile(TileType.GRASS));
		}
	}
	
	private void resetMap() {
		for(int i=0; i<(level.getWidth()*level.getHeight()); i++)
		{
			this.tileMap.get(i).setType(TileType.GRASS);
		}
		//drawScreen();
	}
	
	private void setMap(){
		for(int i=0; i<(level.getWidth()*level.getHeight()); i++)
		{
				this.tileMap.get(i).setType(level.getTiles().get(i));
		}
		redraw();
	}
	
	public void drawScreen(){
		/*
		 * BUFFER
		 */
		buffer = new BufferedImage(level.getWidth()*25,level.getHeight()*25,BufferedImage.TYPE_INT_RGB);
		Graphics2D b = buffer.createGraphics();
		int index;
		for(int i=0; i<level.getHeight(); i++)
		{
			for(int j=0; j<level.getWidth(); j++)
			{
				index = (i*level.getWidth())+j;
				b.drawImage(tileMap.get(index).getImage(), 25*j, 25*i, null);
			}
		}
                //Draw ball
                b.drawImage(ballImage, (int) ((level.getBallX()-1)*25), (int) ((level.getBallY()-1)*25), this);
		b.dispose();
                
		/*
		 * TO SCREEN
		 */
		Graphics2D g = (Graphics2D)this.getGraphics();
		g.drawImage(buffer,0,0,this);
		g.dispose();
	}
	
	public void redraw() {
		//drawScreen();
	}
	
	
	private void outputToFile() throws IOException {
		ArrayList<TileType> tiles = new ArrayList<TileType>();
		for(int i=0; i<(level.getWidth()*level.getHeight()); i++)
		{
			tiles.add(tileMap.get(i).getType());
		}
		
		level.setTiles(tiles);
		
                
                fileChooser.setCurrentDirectory(new File("levels/"));
                //Show the save dialogue and store what button the user presses
                int response = fileChooser.showSaveDialog(this);
                
                
                if(response == JFileChooser.APPROVE_OPTION) {
                    
                    FileOutputStream fos = null;

                    try {
                            fos = new FileOutputStream(fileChooser.getSelectedFile());
                    } catch (FileNotFoundException e) {
                            e.printStackTrace();
                    }

                    ObjectOutputStream output = new ObjectOutputStream(fos);

                    output.writeObject(level);

                    output.close();
                    fos.close();
                }
	}
	
	
	private void inputFromFile() throws IOException, ClassNotFoundException {
		
                fileChooser.setCurrentDirectory(new File("levels/"));
                int response = fileChooser.showOpenDialog(this);
                
                if(response == JFileChooser.APPROVE_OPTION) {
                    
                    FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile());
                    ObjectInputStream input = new ObjectInputStream(fis);
                    
                    //Temp vars for width and height of the old level to compare against
                    int oldWidth = level.getWidth();
                    int oldHeight = level.getHeight();
                    this.level = (LevelDescription) input.readObject();
                    
                    /*if(level.getHeight() == oldHeight && level.getWidth() == oldWidth) {
                            setMap();
                    } else {
//                            mapySize = level.getHeight();
//                            mapxSize = level.getWidth();
                            makeFrame();
                            initMap();
                            setMap();
                    }
*/
                    Editor e = new Editor();
                    //e.setLevel();
                    isActive = false;
                    frame.dispose();
                    
                    input.close();
                    fis.close();
                }
	}
	
        
        
        /*
	public static void main(String[] args){
		Editor e = new Editor();
	}*/

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == this){
			if ((e.getX() < level.getWidth()*25 && e.getX() > 0) && ((e.getY() < level.getHeight()*25) && (e.getY() > 0))){
				int x,y,index;
				x = (e.getX() - (e.getX() % 25))/25;
				y = (e.getY() - (e.getY() % 25))/25;
				index = (y*level.getWidth())+x;
                                if(placingBall) {
                                    level.setBallX( x+1);
                                    level.setBallY(y+1);
                                } else {
                                    this.tileMap.get(index).setType(brush);
                                }
				//this.redraw();
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		EditorButton button = (EditorButton) arg0.getSource();
		if(!button.equals(buttonSelected))
		{
                        placingBall = false;
			this.brush = button.getTileType();
			buttonSelected.setSelected(false);
			buttonSelected = button;
			button.setSelected(true);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		this.mousePressed(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
        
        /**
         * A thread that redraws the screen every few milliseconds
         */
        private class ScreenDrawer extends Thread
        {
            public ScreenDrawer()
            {
                
            }
            
            public void run()
            {
                while(isActive)
                {
                    if(!menuFile.isPopupMenuVisible() && !menuEdit.isPopupMenuVisible()) {
                        drawScreen();
                    }
                    //Delay by an amount
                    delay(50);
                }
            }
            
            private void delay(int delayTime)
            {
                
                try {
                    Thread.sleep(delayTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        /**
         * ActionListener that deals with all the File and Edit buttons
         */
        private class EditorMenuListener implements ActionListener
        {
            private String id;
            private JFrame frame;
            
            public EditorMenuListener(String id)
            {
                this.id = id;
            }
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                switch (id) {
                    case "file-save" :
                        try {
                            outputToFile();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                        //redraw();
                        break;
                        
                    case "file-load" :
                        try {
                            inputFromFile();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        //redraw();
                        break;
                        
                    case "file-test" :
                        ArrayList<TileType> tiles = new ArrayList<TileType>();
                        for(int i=0; i<(level.getWidth()*level.getHeight()); i++)
                        {
                            tiles.add(tileMap.get(i).getType());
                        }
                        level.setBallX(level.getBallX());
                        level.setBallY(level.getBallY());
                        level.setTiles(tiles);
                        new Game(level, editor);
                        break;
                        
                    case "edit-properties" :
                        frame = new JFrame("Level Properties");
                        frame.setSize(250,200);
                        
                        frame.setLayout(null);

                        final JTextField nameField, parField;
                        JLabel nameLabel, parLabel;

                        nameField = new JTextField(level.getName());
                        nameLabel = new JLabel("Name: ");
                        frame.add(nameField);
                        frame.add(nameLabel);
                        nameField.setBounds(80,20,100,20);
                        nameLabel.setBounds(20,23,100,10);

                        parField = new JTextField(String.valueOf(level.getPar()));
                        parLabel = new JLabel("Par: ");
                        parField.setBounds(80,50,100,20);
                        parLabel.setBounds(20,49,100,20);
                        frame.add(parField);
                        frame.add(parLabel);

                        frame.add(new JButton("OK"){
                        {
                            this.setBounds(53, 100, 60, 30);
                            this.addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                        level.setName(nameField.getText());
                                        level.setPar(Integer.parseInt(parField.getText()));
                                        frame.dispose();
                                }
                            });
                        }}

                        );

                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                        
                    case "edit-resize" :
                        frame = new JFrame("Resize Map");
                        frame.setSize(220,200);
                        frame.setLayout(null);

                        final JTextField widthField, heightField;
                        JLabel widthLabel, heightLabel;

                        widthField = new JTextField(String.valueOf(level.getWidth()));
                        widthLabel = new JLabel("Width: ");
                        widthField.setBounds(60,20,100,20);
                        widthLabel.setBounds(20,23,100,20);
                        frame.add(widthField);
                        frame.add(widthLabel);

                        heightField = new JTextField(String.valueOf(level.getHeight()));
                        heightLabel = new JLabel("Height: ");
                        heightField.setBounds(60,50,100,20);
                        heightLabel.setBounds(20,49,100,20);
                        frame.add(heightField);
                        frame.add(heightLabel);

                        frame.add(new JButton("OK"){
                        {
                                this.setBounds(53, 100, 60, 30);
                                this.addActionListener(new ActionListener() {

                                        @Override
                                        public void actionPerformed(ActionEvent arg0) {
                                                        Object[] options = { "OK", "Cancel" };
                                                        int n = JOptionPane.showOptionDialog(null, "Warning: This will reset the map.\n Continue?", 
                                                                        "Canvas Resize",
                                                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                                                    null, options, options[0]);
                                                        if(n==JOptionPane.YES_OPTION) {
                                                                //level.setWidth(Integer.parseInt(widthField.getText()));
                                                                //level.setHeight(Integer.parseInt(heightField.getText()));
                                                            isActive = false;
                                                            new Editor(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
                                                            editor.frame.dispose();
                                                        }
                                                frame.dispose();
                                        }
                                });
                        }}

                        );

                        frame.setVisible(true);
                        frame.setLocationRelativeTo(null);
                    case "edit-reset" :
                        resetMap();
                }
            }
        }
        
}

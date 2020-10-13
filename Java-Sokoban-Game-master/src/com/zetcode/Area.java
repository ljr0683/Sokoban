package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Area extends Actor {
	private Image image;
	private ImageIcon iicon;
	private ImageIcon iiicon;
    public Area(int x, int y) {
        super(x, y);
        
        initArea();
    }
    
    private void initArea() { //Area √ ±‚»≠
    	if(mode==1) {
     		iicon = new ImageIcon("src/resources/area1.png");
			image = iicon.getImage();
			setImage(image);
		}
		else {
			iicon = new ImageIcon("src/resources/area.png");
			image = iicon.getImage();
			setImage(image);
		}
    }
}

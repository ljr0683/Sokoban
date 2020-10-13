package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Baggage extends Actor {
	private Image image;
	private ImageIcon iicon;
	private boolean isEntered = false;

    public Baggage(int x, int y) {
        super(x, y);
        
        initBaggage();
    }
    
    private void initBaggage() { // Baggage √ ±‚»≠
    	if(mode==1) {
        	ImageIcon iicon = new ImageIcon("src/resources/baggage1.png");
			image = iicon.getImage();
			setImage(image);
		}
		else {
			ImageIcon iicon = new ImageIcon("src/resources/baggage.png");
			image = iicon.getImage();
			setImage(image);
    }
    }

    public void move(int x, int y) {
        
        int dx = x() + x;
        int dy = y() + y;
        
        setX(dx);
        setY(dy);
    }
    
    public boolean getIsEntered() {
		return isEntered;
	}

	public void setIsEntered() {
		isEntered = true;
	}
	
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import processing.core.PVector;

public class Bubbles {
    
    PVector pos;
    int size, speed;
    boolean onScreen = true;
	private Color bubbleColor = new Color(0,160,250, 50);
	private Color bubbleColorOutline = new Color(0,200,255, 50);
	private Ellipse2D.Double bubble;
	
	private int bubbleSize;
    
    public Bubbles(int x, int y, int speed, int size) {
        this.pos = new PVector(x, y);
        this.speed = speed;
        this.size = size;
        bubbleSize = size;
        bubble = new Ellipse2D.Double();
        setBubbleAttribute();
    }
    
    private void setBubbleAttribute() {
		bubble.setFrame(0, 0, bubbleSize, bubbleSize);
	}

	public void drawBubble(Graphics g2d){
        Graphics2D g2 = (Graphics2D) g2d;
        
        AffineTransform originalTransform = g2.getTransform();
        g2.translate(this.pos.x, this.pos.y - (this.size * 2));
        
        g2.setColor(bubbleColorOutline);
        ((Graphics2D) g2d).draw(bubble);
        g2.setColor(bubbleColor);
        ((Graphics2D) g2d).fill(bubble);
        
        g2.setTransform(originalTransform);
    }
    
    public void moveBubble() {
        pos.y -= speed;
    }
    
    public boolean isOnScreen(Dimension screenSize) {
        return this.pos.y + this.size > 0;
    }
}

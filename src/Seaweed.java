import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import processing.core.PVector;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Seaweed {
	private PVector pos;
	private int size, width, speed;
	private Color seaweedColor = new Color(51, 102, 0);
	private double angle;
	private Color crabColor = new Color(255, 80, 80);
	private Color darkerCrabColor = new Color(200, 50, 50);

	
    public Seaweed(int x, int y, int size, int angle, int speed) {
    	this.pos = new PVector(x, y);
    	this.size = size;
    	this.width = 10;
    	this.angle = Math.PI/angle;
    	this.speed = speed;
    }
    
    public void drawSeaweed(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
    	
    	
        
        AffineTransform originalTransform = g2.getTransform();
        g2.translate(this.pos.x, this.pos.y);
        g2.scale(0.5, 0.5);
        
        AffineTransform originalTransform4 = g2.getTransform();
        
        g2.setColor(darkerCrabColor);
        g2.setStroke(new BasicStroke(5));
        
        g2.rotate(Math.PI/2);
        
        g2.drawArc(-70, -50, 70, 50, 0, 90);
        
        
        g2.setTransform(originalTransform4);
        
        AffineTransform originalTransform5 = g2.getTransform();
        
        g2.setColor(darkerCrabColor);
        g2.setStroke(new BasicStroke(5));
        
        g2.rotate(Math.PI/2);
        g2.scale(1, -1);
        
        g2.drawArc(-70, -50, 70, 50, 0, 90);
        
        
        g2.setTransform(originalTransform5);
        
        //legs
        AffineTransform originalTransform2 = g2.getTransform();
        g2.setColor(darkerCrabColor);
        g2.rotate(Math.PI/8);
        g2.drawRoundRect(20, 0, 35, 4, 10, 10);
        g2.fillRoundRect(20, 0, 35, 4, 10, 10);
        
        g2.drawRoundRect(20, 0, 35, 4, 10, 10);
        g2.fillRoundRect(20, 0, 35, 4, 10, 10);
        
        
        g2.setTransform(originalTransform2);
        
        AffineTransform originalTransform3 = g2.getTransform();
        g2.setColor(darkerCrabColor);
        g2.rotate(-Math.PI/8);
        g2.drawRoundRect(-55, 0, 35, 4, 10, 10);
        g2.fillRoundRect(-55, 0, 35, 4, 10, 10);
        
        g2.drawRoundRect(-55, 0, 35, 4, 10, 10);
        g2.fillRoundRect(-55, 0, 35, 4, 10, 10);
        
        
        g2.setTransform(originalTransform3);
        
        
        g2.setColor(crabColor);
        g2.drawOval(-50, -25, 100, 50);
        g2.fillOval(-50, -25, 100, 50);
        
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.drawArc(-20, -10, 40, 20, 0, -180);
        g2.drawArc(-40, -8, 20, 10, 20, -70);
        g2.drawArc(20, -8, 20, 10, 160, 70);
        
        g2.drawArc(-15, -40, 2, 30, 0, 100);
        g2.drawArc(15, -40, 2, 30, 0, 100);
        
        g2.setColor(Color.black);
        g2.drawOval(-20, -45, 10, 10);
        g2.setColor(Color.white);
        g2.fillOval(-20, -45, 10, 10);
        g2.setColor(Color.black);
        g2.fillOval(-18, -43, 5, 5);
        
        g2.setColor(Color.black);
        g2.drawOval(10, -45, 10, 10);
        g2.setColor(Color.white);
        g2.fillOval(10, -45, 10, 10);
        g2.setColor(Color.black);
        g2.fillOval(12, -42, 5, 5);
        
       
        
        g2.setTransform(originalTransform);
    }
	
    
    public void move() {
    	pos.x += speed;
    }
    
    public void checkWalls(Dimension size) {
    	if(pos.x > size.width - 60 || pos.x < 60) {
    		this.speed *= -1;
    	}
    	
    }
}

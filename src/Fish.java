import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import processing.core.PVector;

public class Fish {

    private int size;
    private PVector speed;
    public PVector pos;
    private Color color;
    private Ellipse2D.Double body;
    private GeneralPath tail;
    private Ellipse2D.Double eyeInner;
    private Ellipse2D.Double eyeOutter;
    public double scale = 1;
    
    private int bodyStartW;
    private int bodyStartH;
    private int eyeStartW;
    private int eyeStartH;
    
    public Fish(int x, int y, int size, int speedx, int speedy, Color color) {
        pos = new PVector(x, y);
        this.size = size;
        this.speed = new PVector(speedx, speedy);
        this.color = color;
        body = new Ellipse2D.Double();
        tail = new GeneralPath();
        eyeInner = new Ellipse2D.Double();
        eyeOutter = new Ellipse2D.Double();
        bodyStartW = size / 2;
        bodyStartH = size / 4;
        eyeStartW = size / 8;
        eyeStartH = size / 16;
        
        setBodyAttributes();
        setEyeAttributes();
    }

    private void setEyeAttributes() {
        eyeInner.setFrame(bodyStartH, -eyeStartW, eyeStartW, eyeStartW);
        eyeOutter.setFrame(bodyStartH + eyeStartH, -eyeStartW + eyeStartH, eyeStartH, eyeStartH);
    }

    private void setBodyAttributes() {
        body.setFrame(-bodyStartW, -bodyStartH, size, bodyStartW);
    }

    public void draw(Graphics g2d) {
        Graphics2D g2 = (Graphics2D) g2d;

        AffineTransform old = g2.getTransform();

        g2.translate(pos.x, pos.y);
        g2.rotate(speed.heading());
        g2.scale(scale, scale);

        g2d.setColor(color);
        ((Graphics2D) g2d).fill(body);

        tail.moveTo(-size / 2, 0);
        tail.lineTo(-size / 2 - size / 4, -size / 4);
        tail.lineTo(-size / 2 - size / 4, size / 4);
        tail.closePath();
        ((Graphics2D) g2d).fill(tail);

        // Draw fish eye
        g2.setColor(Color.WHITE);
        ((Graphics2D) g2d).fill(eyeInner);
        g2.setColor(Color.BLACK);
        ((Graphics2D) g2d).fill(eyeOutter);

        g2.setTransform(old);
    }

    public void move() {
        pos.add(speed);
    }

    public void checkCollision(Dimension panelSize) {
        int scaledSize = (int) (size * scale) - 20;
        
        if ((pos.x < (scaledSize / 2) + 60) || (pos.x > panelSize.width - (scaledSize / 2) - 60)) {
            speed.x *= -1;

            if (speed.x > 0) {
                pos.x += 20;
            } else {
                pos.x -= 20;
            }
        }

        if ((pos.y < (scaledSize / 2) + 20) || (pos.y > panelSize.height - (scaledSize / 2) - 55)) {
            speed.y *= -1;

            if (speed.y > 0) {
                pos.y += 20;
            } else {
                pos.y -= 20;
            }
        }
    }
    
    public boolean checkMouseHit(MouseEvent e) {
        int scaledSize = (int) ((int) size * scale);
        return  (Math.abs(e.getX() - pos.x) < scaledSize / 2) &&
                (Math.abs(e.getY() - pos.y) < scaledSize / 2);
    }

    public PVector getLocation() {
        return pos;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double d) {
        size += d;
    }

    public PVector getPos() {
        return pos;
    }
}

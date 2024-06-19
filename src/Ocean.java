import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Ocean {
    private Dimension size;
    private Color bgColor = new Color(0, 150, 187);
    private Color sandColor = new Color(237, 201, 175);
    
    private Rectangle2D.Double ocean;
    private Rectangle2D.Double sand;
    
    private final int MARGIN = 50;
    private final int SAND_HEIGHT = 80;
    
    public Ocean(Dimension size) {
        this.size = size;
        ocean = new Rectangle2D.Double();
        sand = new Rectangle2D.Double();
        
        setOceanAttributes();
        setSandAttributes();
    }
    
    private void setOceanAttributes() {
        int oceanWidth = size.width - MARGIN * 2;
        int oceanHeight = size.height - MARGIN * 2;
        ocean.setFrame(MARGIN, MARGIN, oceanWidth, oceanHeight - SAND_HEIGHT);
    }

    private void setSandAttributes() {
        int sandWidth = size.width - MARGIN * 2;
        sand.setFrame(MARGIN, size.height - MARGIN - SAND_HEIGHT, sandWidth, SAND_HEIGHT); 
    }

    public void drawBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(bgColor);
        g2d.fill(ocean);
        
        g2d.setColor(sandColor);
        g2d.fill(sand);
    }

    public void setSize(Dimension size) {
        this.size = size;
        setOceanAttributes();
        setSandAttributes();
    }
}

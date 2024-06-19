import javax.swing.*;
import java.awt.*;

public class OrcaApp extends JFrame {

	
	public OrcaApp(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 800);
        
        OrcaPanel panel = new OrcaPanel(new Dimension(this.getSize()));

        this.add(panel);
        
        this.setVisible(true);
    }

    public static void main(String[] args) {
        OrcaApp app = new OrcaApp("Orca and Friends");
    }
}




import java.awt.Color;

public class Util {

	public static double random(int min, int max) {
		return Math.random()*(max-min)+min;
	}
	
	public static int randomInt(int min, int max) {
		return (int) (Math.random()*(max-min)+min);
	}
	
	public static double random(int max) {
		return Math.random()*max;
	}
	
	public static Color randomColor() {
		int r = (int) random(255);
		int g = (int) random(255);
		int b = (int) random(255);
		
		return new Color(r,g,b);
	}
}

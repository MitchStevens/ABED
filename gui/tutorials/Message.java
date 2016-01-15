package tutorials;

import javafx.scene.text.Font;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import data.Reader;

public interface Message {
	final static double PADDING = 5.0; 
	final static Font	FONT = Reader.loadFont("DejaVuSans-ExtraLight.ttf", 15);
	
	public void display(Double[] pos);
	public void remove();
}

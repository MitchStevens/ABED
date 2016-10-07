package controls;

import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StyleableTextPane extends FlowPane {
	public Font def_font = new Font("Arial", 20);
	
	
	public StyleableTextPane(String s, Font f){
		
	}
	
	public void append(String text, Font f, Color color){
		for(String s : text.split(" ")){
			Text t = new Text(s);
			t.setFont(f);
			t.setFill(color);
			
			this.getChildren().add(t);
		}
	}
	
}

package tutorials;

import panes.CircuitPane;
import panes.Gui;

import com.sun.javafx.tk.FontMetrics;

import data.Reader;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SlideMessage extends Pane implements Message{
	
	public final static Color	DEF_COLOR = Color.web("#45535A");
	public final static double 	MAX_WIDTH = 300;
	public double h, w;
	
	public SlideMessage(String text){		
		Text t = new Text(text);
		t.setWrappingWidth(MAX_WIDTH);
		t.setFont(FONT);
		t.setFill(Color.WHITE);
		t.maxWidth(MAX_WIDTH);
		t.setLayoutX(5);
		t.setLayoutY(20);
		this.getChildren().add(t);
		
		w = t.getBoundsInParent().getWidth()  + 2*PADDING;
		h = t.getBoundsInParent().getHeight() + 2*PADDING;
		
		this.setPrefWidth(w);
		
		Rectangle rekt = new Rectangle(w, h, DEF_COLOR);
		this.getChildren().add(rekt);
		
		t.toFront();
	}

	@Override
	public void display(Double[] pos) {
		CircuitPane.cp.getChildren().add(this);
		
		TranslateTransition slide_in = new TranslateTransition(Duration.seconds(2), this);
		slide_in.setFromX(Gui.boardWidth - Gui.SIDE_BAR_WIDTH - w);
		slide_in.setFromY(Gui.boardHeight);
		slide_in.setToX(Gui.boardWidth - Gui.SIDE_BAR_WIDTH - w);
		slide_in.setToY(Gui.boardHeight - h);
		
		TranslateTransition slide_out = new TranslateTransition(Duration.seconds(2), this);
		slide_out.setDelay(Duration.seconds(3));
		slide_out.setFromX(Gui.boardWidth - Gui.SIDE_BAR_WIDTH - w);
		slide_out.setFromY(Gui.boardHeight - h);
		slide_out.setToX(Gui.boardWidth - Gui.SIDE_BAR_WIDTH - w);
		slide_out.setToY(Gui.boardHeight);
		
		SequentialTransition st = new SequentialTransition();
		st.getChildren().addAll(slide_in, slide_out);
//		st.setOnFinished(e -> {
//			if(GamePane.tutorial != null)
//				GamePane.tutorial.next_step();
//			GamePane.gp.getChildren().remove(this);
//		});
		st.play();
	}

	@Override
	public void remove() {
		//unused
	}
	
}

package tutorials;

import com.sun.javafx.tk.FontMetrics;

import data.Reader;
import gui.panes.CircuitPane;
import gui.panes.Gui;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PointerMessage extends Pane implements Message{
	public final static Color	DEF_COLOR = Color.web("#DDFEFE");
	public final static double 	MAX_WIDTH = 300;
	public final static Font	FONT = Reader.loadFont("DejaVuSans-ExtraLight.ttf", 15);
	
	private double h, w;
	private String s;
	private Text t;
	private double o;
	
	public PointerMessage(String s){
		this.s = s;
		//pos is the coordinate to which the arrow points, not the upper left corner
		t = new Text(s);
		t.setWrappingWidth(MAX_WIDTH);
		t.setFont(FONT);
		t.maxWidth(MAX_WIDTH);
		this.getChildren().add(t);
		
		h = t.getBoundsInParent().getHeight() + 2*PADDING;
		w = t.getBoundsInParent().getWidth()  + 2*PADDING;
	}

	@Override
	public void display(Double[] pos) {
		o =  Math.signum((Gui.board_width - Gui.SIDE_BAR_WIDTH)/2 - pos[0]);
		
		Polygon poly = new Polygon(new double[]{
				o*(w+50), 	-h/2,
				o*50,		-h/2,
				0, 			0,
				o*50, 		h/2,
				o*(w+50),	h/2
		});
		poly.setFill(DEF_COLOR);
		this.getChildren().add(poly);
		
		t.setLayoutX( o == 1.0 ? 50 : PADDING - 50- w );
		t.setLayoutY(20-h/2);
		t.toFront();
		
		this.prefWidth(w);
		this.setLayoutX(pos[0]);
		this.setLayoutY(pos[1]);
		
		CircuitPane.cp.getChildren().add(this);
		
		FadeTransition fade_in = new FadeTransition(Duration.seconds(2), this);
		fade_in.setFromValue(0.0);
		fade_in.setToValue(1.0);
		fade_in.play();
	}

	@Override
	public void remove() {
		FadeTransition fade_out = new FadeTransition(Duration.seconds(2), this);
		fade_out.setFromValue(1.0);
		fade_out.setToValue(0.0);
		fade_out.setOnFinished(e -> {
			CircuitPane.cp.getChildren().remove(this);
		});
		fade_out.play();
	}
}

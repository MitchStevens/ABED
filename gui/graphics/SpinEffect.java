package graphics;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SpinEffect extends Pane{
	//If you get a chance, this really should be a transition.
	ImageView initial, terminal;
	Duration d = Duration.seconds(4);
	
	
	public SpinEffect(Image initial, Image terminal){
		this.initial = new ImageView(initial);
		this.terminal = new ImageView(terminal);
		this.getChildren().addAll(new ImageView(initial), new ImageView(terminal));
	}
	
	public void play(){
		initial.toFront();
		
		RotateTransition rt = new RotateTransition(d, this);
		rt.setToAngle(0);
		rt.setFromAngle(360*20);
		
		FadeTransition fade_out = new FadeTransition(d, initial);
		fade_out.setFromValue(1.0);
		fade_out.setToValue(0.0);
		
		FadeTransition fade_in = new FadeTransition(d, terminal);
		fade_in.setFromValue(0.0);
		fade_in.setToValue(1.0);
		
		ParallelTransition pt = new ParallelTransition();
		pt.getChildren().addAll(rt, fade_in);
		pt.setInterpolator(Interpolator.EASE_BOTH);
		pt.play();
	}


	
}

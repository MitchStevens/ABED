package controls;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Typer extends Text {
	String text = "";
	int speed;
	
	public Typer(String text, int speed){
		super();
		this.setId("typer");
		this.text = text;
		this.speed = speed;
	}
	
	//stolen shamefully from an answer by ItachiUchiha at http://stackoverflow.com/questions/33646317/typing-animation-on-a-text-with-javafx
	public void play(){
		this.setText("");
		
		final IntegerProperty i = new SimpleIntegerProperty(0);
		Timeline timeline = new Timeline();
		timeline.setDelay(Duration.millis(300));
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(speed),
                event -> {
                    if (i.get() > text.length()) {
                        timeline.stop();
                    } else {
                        this.setText(text.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                    
//                    double d = i.get();
//                    System.out.println("speed: "+d);
//                    timeline.setRate(d);
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
	}
	
}

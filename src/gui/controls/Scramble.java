package gui.controls;

import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Scramble extends Label {
	String text;
	
	public Scramble(String text){
		super();
		this.text = text;
	}
	
	//stolen shamefully from an answer by ItachiUchiha at http://stackoverflow.com/questions/33646317/typing-animation-on-a-text-with-javafx
	public void play(){
		this.setText("");
		this.setId("scramble");
		
		final IntegerProperty i = new SimpleIntegerProperty(0);
		Timeline timeline = new Timeline();
		timeline.setDelay(Duration.millis(800));
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(50),
                event -> {
                    if (i.get() > text.length()) {
                        timeline.stop();
                    } else {
                        this.setText(text.substring(0, i.get()) + random_string(text.length() - i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
	}
	
	private String random_string(int length){
		String s = "";
		Random r = new Random();
		for(int i = 0; i < length; i++)
			s += Character.toString((char)(r.nextInt(94)+33));
		return s;
	}
	
}

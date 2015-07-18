package abedgui;

import java.util.Random;

import panes.GamePane;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Circuit;
import logic.Evaluator;
import logic.Reader;

public class WinMessage extends VBox {
	private final static String[] words = new String[]{
		"yes", "you", "did", "thing", "affermative",
		"truly", "expectations", "great", "wow",
		"finally", "correct", "success", "overwhelming",
		"absolute", "glory", "amazement", "acheive",
		"prosperity", "victory", "triumph"
	};
	
	private final static String[] messages = new String[]{
		"You are so handsome!",
		"Your logical strength knows no bounds!",
		"Do they sing of your beauty? Because they should.",
		"HULK LEARN!",
		"GANGSTA!"
	};
	
	public WinMessage(Level l, int size){
		this.setStyle("-fx-background-color: #8FECEC");
		this.setMaxSize(Gui.boardWidth/2, Gui.boardHeight/2);
		this.setPadding(new Insets(30));
		this.setAlignment(Pos.TOP_CENTER);
		
		DropShadow borderGlow= new DropShadow();
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(Color.BLACK);
		borderGlow.setWidth(40);
		borderGlow.setHeight(40);
		this.setEffect(borderGlow);
		
		Text t1 = new Text(WinMessage.get());
		t1.setFont(Reader.loadFont("Aux DotBitC.ttf", 55));
		t1.setWrappingWidth(Gui.boardWidth/2);
		this.getChildren().add(t1);
		
		String earned = "You have Earned:";
		for(Circuit c : l.circuitRewards)
			earned += " "+c.name+",";
		Text t2 = new Text(Evaluator.init(earned)+".");	
		t2.setFont(Reader.loadFont("Aux DotBitC.ttf", 30));
		t2.setWrappingWidth(Gui.boardWidth/2);
		this.getChildren().add(t2);
		
		Separator sep = new Separator();
		sep.setPadding(new Insets(20));
		this.getChildren().add(sep);
		
		Text t3 = new Text();
		String rating = "";
		if(size < l.gameSize)
			rating = "You have completed this level SUPER OPTIMALLY. You goddamn cheater.";
		else if(size == l.gameSize)
			rating = "You have completed this level OPTIMALLY.";
		else
			rating = "You have completed this level SUB OPTIMALLY."
					+ "Complete this level on a "+l.gameSize+"x"+l.gameSize+" board for best score.";
		rating += " Where would you like to go from here?";
		t3.setText(rating);
		t3.setFont(Reader.loadFont("Aux DotBitC.ttf", 20));
		t3.setWrappingWidth(Gui.boardWidth/2);
		this.getChildren().add(t3);
		
		Level next = Level.nextLevel(l);
		HBox buttons = new HBox();
		Button b1 = new Button("next level (N)");
		b1.setDisable(next == null);
		b1.setOnAction(e -> {
			if(next != null){
				GamePane.setLevel(next);
				kill();
			}
		});
		buttons.getChildren().add(b1);
		
		Button b2 = new Button("level select");
		b2.setOnAction(e -> {
			Gui.setCurrentPane(Gui.levelSelectPane);
			kill();
		});
		buttons.getChildren().add(b2);
		
		this.getChildren().add(buttons);
		
		//this should be elsewhere
		Gui.mainPane.getChildren().add(this);
		this.toFront();
		this.play();
	}
	
	public void kill(){
		Gui.mainPane.getChildren().remove(this);
	}
	
	public void play(){
	    FadeTransition ft = new FadeTransition(Duration.millis(1000), this);
	    ft.setFromValue(0.0);
	    ft.setToValue(1.0);
	    ft.setCycleCount(1);
	    ft.setAutoReverse(false);
	 
	    ft.play();
	}
	
	public static String get(){
		Random r = new Random(System.nanoTime());
		return messages[r.nextInt(5)];
	}
	
	//creates a random win message
	public static String random(int len){
		Random r = new Random(System.nanoTime());
		String tbr = "";
		while(len > 0){
			tbr += words[r.nextInt(words.length)]+" ";
			len--;
		}
		
		return tbr.substring(0, 1).toUpperCase() + tbr.substring(1, tbr.length() -1) + "!";
	}
}

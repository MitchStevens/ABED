package controls;

import java.util.Random;

import circuits.Circuit;
import eval.Evaluator;
import panes.CircuitPane;
import panes.Gui;
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
import logic.Level;
import logic.Reader;

public class WinMessage extends VBox {
	private final static double PADDING_GAP 	= 30;
	private final static Font 	MESSAGE_FONT 	= Reader.loadFont("Aux DotBitC.ttf", 20);
	
	
	private final static String[] messages = new String[]{
		"You are so handsome!",
		"Your logical strength knows no bounds!",
		"Do they sing of your beauty? Because they should.",
		"HULK LEARN!",
		"GANGSTA!"
	};
	
	public WinMessage(Level l, int size){
		this.getStylesheets().add("res/css/WinMessage.css");
		this.setId("message");
		this.setMaxSize(Gui.boardWidth/2, Gui.boardHeight/2);
		this.setPrefHeight(0);
		this.setPadding(new Insets(PADDING_GAP));
		this.setAlignment(Pos.TOP_CENTER);
		
		DropShadow borderGlow= new DropShadow();
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(Color.BLACK);
		borderGlow.setWidth(40);
		borderGlow.setHeight(40);
		this.setEffect(borderGlow);
		
		Text t1 = new Text(WinMessage.get());
		t1.setFont(Reader.loadFont("Aux DotBitC.ttf", 40));
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
		sep.setPadding(new Insets(PADDING_GAP, 0, PADDING_GAP, 0));
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
		t3.setFont(MESSAGE_FONT);
		t3.setWrappingWidth(Gui.boardWidth/2);
		this.getChildren().add(t3);
		
		HBox buttons = new HBox();
		Button b1 = new Button("next level (N)");
		b1.setPadding(new Insets(PADDING_GAP, PADDING_GAP/2, 0, 0));
		
		Level next = Level.nextLevel(l);
		if(next == null){
			Text t4 = new Text("You have completed this level set!");
			t4.setFont(MESSAGE_FONT);
			t4.setWrappingWidth(Gui.boardWidth/2);
			this.getChildren().add(t4);
			b1.setDisable(true);
		}
		
		double buttonWidth = (Gui.boardWidth/2 -PADDING_GAP)/2;
		b1.setPrefWidth(buttonWidth);
		b1.setFont(MESSAGE_FONT);
		b1.setOnAction(e -> {
			if(next != null){
				CircuitPane.setLevel(next);
				kill();
			}
		});
		buttons.getChildren().add(b1);
		
		Button b2 = new Button("level select");
		b2.setPadding(new Insets(PADDING_GAP, 0, 0, PADDING_GAP/2));
		b2.setPrefWidth(buttonWidth);
		b2.setAlignment(Pos.CENTER);
		b2.setFont(MESSAGE_FONT);
		b2.setOnAction(e -> {
			Gui.setCurrentPane("level_select_pane");
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

package panes;

import java.util.Random;

import abedgui.Gui;
import data.Reader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class TitlePane extends VBox {
	Label title, subtitle, version;
	private Font adbxtsc = Reader.loadFont("adbxtsc.ttf", 200);
	private Font adbxtra = Reader.loadFont("adbxtra.ttf", 50);
	private final String ver = "2.0";
	private final String[] txt = new String[]{
		"Binary operations galore!",
		"For CompuSolves Only.",
		"A perfect 10 (in binary)!",
		"Beats out all abstraction based gaming!",
		"Accept most substitutions!",
		"1100011 problems."
	};
	
	{
		this.setId("main");
	}
	
	public TitlePane(){
		this.setMinSize(Gui.boardWidth, Gui.boardHeight);
		
		title = new Label("ABED");
		title.setFont(adbxtsc);
		title.setAlignment(Pos.CENTER);
		
		Random r = new Random();
		subtitle = new Label(txt[r.nextInt(txt.length)]);
		subtitle.setFont(adbxtra);
		subtitle.setAlignment(Pos.CENTER);
		
		this.getChildren().addAll(title, subtitle);
		this.setOnMouseClicked(e -> {
			Gui.mainPane.getChildren().remove(this);
			Gui.mainPane.getChildren().add(Gui.levelSelectPane);
			System.out.println("clicked");
		});
	}
}

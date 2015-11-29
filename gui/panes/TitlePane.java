package panes;

import java.util.Random;

import controls.Scramble;
import controls.Typer;
import abedgui.Gui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.Reader;

public class TitlePane extends VBox {
	private 		Font 		adbxtsc = Reader.loadFont("adbxtsc.ttf", 300);
	private 		Font 		adbxtra = Reader.loadFont("adbxtra.ttf", 50);
	private 		Font 		TYPER_FONT = Reader.loadFont("adbxtra.ttf", 90);
	private final 	String 		ver 	= "2.0";
	private final 	String[] 	txt 	= new String[] {
		"Binary operations galore!",
		"I'm not confusing, you're just stupid!",
		"Learnding!",
		"For CompuSolves Only.",
		"A perfect 10 (in binary)!",
		"Beats out all abstraction based gaming!",
		"Accept most substitutions!",
		"1100011 problems.",
		"Blunts? Nah fam, binary.",
		"Arousing success!",
		"YES DIGGITY."
	};

	public TitlePane() {
		this.getStylesheets().add(Reader.loadCSS("TitlePane.css"));
		this.setMinSize(Gui.boardWidth, Gui.boardHeight);
		this.setPadding(new Insets(10, 10, 10, 10));
		
		Label title = new Label("ABED");
		title.setFont(adbxtsc);
		title.setAlignment(Pos.CENTER);

		Random r = new Random();
		Scramble subtitle = new Scramble(txt[r.nextInt(txt.length)]);
		subtitle.setFont(adbxtra);
		subtitle.setWrapText(true);
		subtitle.setPrefWidth(Gui.boardWidth);
		subtitle.setAlignment(Pos.TOP_LEFT);
		subtitle.play();
		
		Typer play_game = new Typer("> Play Game");
		play_game.setFont(TYPER_FONT);
		play_game.setOnMouseClicked(e -> {
			Gui.setCurrentPane(Gui.levelSelectPane);
		});
		play_game.play();
		
		Typer sand_box = new Typer("> Sandbox");
		sand_box.setFont(TYPER_FONT);
		sand_box.play();
		
		Typer about = new Typer("> About");
		about.setFont(TYPER_FONT);
		about.play();
		
		this.getChildren().addAll(title, subtitle, play_game, sand_box, about);
	}
}

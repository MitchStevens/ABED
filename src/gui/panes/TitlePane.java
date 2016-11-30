package gui.panes;

import java.util.Random;

import data.Reader;
import gui.controls.Scramble;
import gui.controls.Typer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TitlePane extends VBox implements ScreenPane {
	private 		Font 		adbxtsc = Reader.loadFont("adbxtsc.ttf", 300);
	private 		Font 		adbxtra = Reader.loadFont("adbxtra.ttf", 50);
	private 		Font 		TYPER_FONT = Reader.loadFont("adbxtra.ttf", 90);
	private final	int			SPEED = 50;
	private final 	String 		ver 	= "2.0";
	private final	Scramble 	subtitle;
	private final	Typer[]		typers;	
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
		this.setMinSize(Gui.board_width, Gui.board_height);
		
		Label title = new Label("ABED");
		title.setFont(adbxtsc);
		title.setAlignment(Pos.CENTER);

		Random r = new Random();
		subtitle = new Scramble(txt[r.nextInt(txt.length)]);
		subtitle.setFont(adbxtra);
		subtitle.setWrapText(true);
		subtitle.setPrefWidth(Gui.board_width);
		subtitle.setAlignment(Pos.TOP_LEFT);
		subtitle.play();
		
		typers = new Typer[3];
		
		typers[0] = new Typer("> Play Game", SPEED);
		typers[0].setFont(TYPER_FONT);
		typers[0].setOnMouseClicked(e -> {
			Gui.set_pane("level_select_pane");
		});
		typers[0].play();
		
		typers[1] = new Typer("> Sandbox", SPEED);
		typers[1].setFont(TYPER_FONT);
		typers[1].setOnMouseClicked(e -> {
			CircuitPane.unlockAllCircuits = true;
			Gui.set_pane("game_pane");
		});
		typers[1].play();
		
		typers[2] = new Typer("> About", SPEED);
		typers[2].setFont(TYPER_FONT);
		typers[2].setOnMouseClicked(e -> {
			Gui.set_pane("about_pane");
		});
		typers[2].play();
		
		this.getChildren().addAll(title, subtitle, typers[0], typers[1], typers[2]);
		this.setPadding(new Insets(30, 30, 30, 30));
		this.setSpacing(40);
	}

	@Override
	public void onFocus() {
		subtitle.play();
		for(Typer t : typers)
			t.play();
	}

	@Override
	public void offFocus() {}
}

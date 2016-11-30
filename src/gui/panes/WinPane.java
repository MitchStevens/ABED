package gui.panes;

import core.game.Gate;
import core.logic.Level;
import data.Reader;
import gui.graphics.PieceImage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class WinPane extends BorderPane implements ScreenPane {
	private static final Font	TITLE_FONT 		= Reader.loadFont("adbxtsc.ttf", 55);
	private static final Font 	SUBTITLE_FONT 	= Reader.loadFont("adbxtra.ttf", 30);
	private static final Font 	BUTTON_FONT 	= Reader.loadFont("AuX DotBitC.ttf", 25);
	private static final Font 	DEF_FONT 		= Reader.loadFont("DejaVuSans-ExtraLight.ttf", 30);
	private static final double SIDEBAR_WIDTH	= 0.35;
	private static final double PADDING			= 50.0;
	
	private final static String[] MESSAGES = new String[]{
		"You are so handsome!",
		"Your logical strength knows no bounds!",
		"Do they sing of your beauty? Because they should.",
		"HULK LEARN!",
		"GANGSTA!",
		"A challenger approches!",
		"yeah yeah yeah YEAH!"
	};
	
	VBox sidebar = new VBox();
	VBox main = new VBox();
	Level l;
	int size;
	
	public WinPane(){
		this.getStylesheets().add("res/css/WinPane.css");
		this.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
		
		Text title = new Text("LEVEL COMPLETED");
		title.setFont(TITLE_FONT);
	}
	
	public void set_level(Level l, int size){
		this.l = l;
		this.size = size;
		set_main();
		set_sidebar();
		
		this.setCenter(main);
		this.setRight(sidebar);
	}
	
	private void set_main(){
		main = new VBox();
		main.setSpacing(30);
		
		Text level_completed = new Text("Level Completed");
		level_completed.setFont(TITLE_FONT);
		
		Text completed_text = new Text(l.completionText);
		completed_text.setFont(DEF_FONT);
		completed_text.setWrappingWidth((Gui.board_width - 2*PADDING)*(1-SIDEBAR_WIDTH));
		
		HBox buttons = new HBox();
		
		Button next_level = new Button("(N) Next Level");
		next_level.setFont(BUTTON_FONT);
		next_level.setOnAction(e -> {
			Level next = l.nextLevel();
			if(next != null){
				CircuitPane.setLevel(next);
				Gui.set_pane("game_pane");
			}
		});
		
		Button back = new Button("(B) Back to Menu");
		back.setFont(BUTTON_FONT);
		back.setOnAction(e -> {
			Gui.set_pane("level_select_pane");
		});
		
		buttons.getChildren().addAll(next_level, back);
		buttons.setSpacing(50);
		
		main.getChildren().addAll(level_completed, completed_text, buttons);
		
	}
	
	private void set_sidebar(){
		sidebar = new VBox();
		sidebar.setAlignment(Pos.TOP_CENTER);
		sidebar.setSpacing(10);
		
		Text message = new Text(random_message());
		message.setFont(SUBTITLE_FONT);
		message.setWrappingWidth(SIDEBAR_WIDTH*(Gui.board_width - 2*PADDING));
		
		Separator seperator = new Separator();
		
		Text you_win = new Text("You've\nunlocked:");
		you_win.setFont(DEF_FONT);
		you_win.setTextAlignment(TextAlignment.CENTER);
		
		sidebar.getChildren().addAll(message, seperator, you_win);
		
		double circ_size = SIDEBAR_WIDTH*Gui.board_width/2;
		for(Gate c : l.circuitRewards){			
			VBox circ_box = new VBox();
			circ_box.setAlignment(Pos.TOP_CENTER);
			circ_box.setPadding(new Insets(10, 10, 10, 10));
			
			ImageView image = new ImageView(Reader.ALL_IMAGES.get(c.name));
			image.prefHeight(circ_size);
			image.prefWidth(circ_size);
			
			Text circ_name = new Text(c.name);
			circ_name.setFont(DEF_FONT);
			
			circ_box.getChildren().addAll(image, circ_name);
			sidebar.getChildren().add(circ_box);
		}
		
	}
	
	private String random_message(){
		return MESSAGES[(int) (System.currentTimeMillis() % MESSAGES.length)];
	}
	
	
	@Override
	public void onFocus() {}
	@Override
	public void offFocus() {}

}

package panes;

import circuits.Circuit;
import graphics.PieceImage;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import logic.Level;
import logic.Reader;

public class WinPane extends BorderPane implements ScreenPane {
	private static final Font	TITLE_FONT 		= Reader.loadFont("adbxtsc.ttf", 40);
	private static final Font 	SUBTITLE_FONT 	= Reader.loadFont("adbxtra.ttf", 30);
	private static final Font 	DEF_FONT 		= Reader.loadFont("DejaVuSans-ExtraLight.ttf", 15);
	private static final double SIDEBAR_WIDTH	= 0.35;
	
	private final static String[] MESSAGES = new String[]{
		"You are so handsome!",
		"Your logical strength knows no bounds!",
		"Do they sing of your beauty? Because they should.",
		"HULK LEARN!",
		"GANGSTA!",
		"A challenger approches!",
		"POSITIVE REINFORCEMENT TO CREATE EMOTIONAL ATTACHMENT TO \'GAME\'"
	};
	
	VBox sidebar = new VBox();
	VBox main = new VBox();
	Level l;
	int size;
	
	public WinPane(Level l, int size){
		Text title = new Text("LEVEL COMPLETED");
		title.setFont(TITLE_FONT);
		this.l = l;
		this.size = size;
		
		
		
		main.getChildren().add(title);
	}
	
	private void create_sidebar(){
		sidebar = new VBox();
		
		Text message = new Text(random_message());
		message.setFont(SUBTITLE_FONT);
		message.setWrappingWidth(SIDEBAR_WIDTH*Gui.boardWidth);
		
		Separator seperator = new Separator();
		
		Text you_win = new Text("You've\nunlocked:");
		you_win.setTextAlignment(TextAlignment.CENTER);
		
		sidebar.getChildren().addAll(message, seperator, you_win);
		
		double circ_size = SIDEBAR_WIDTH*Gui.boardWidth/2;
		for(Circuit c : l.circuitRewards){
			VBox circ_box = new VBox();
			
			PieceImage image = new PieceImage(c);
			image.setPrefSize(circ_size, circ_size);
			
			Text circ_name = new Text(c.name.toUpperCase());
			
			
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

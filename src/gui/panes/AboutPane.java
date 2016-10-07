package gui.panes;

import data.Reader;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class AboutPane extends Pane implements ScreenPane {
	private static final double TEXT_PANE_WIDTH = 700.0;
	private static final Font	TITLE_FONT 		= Reader.loadFont("adbxtsc.ttf", 200);
	private static final Font 	SUBTITLE_FONT 	= Reader.loadFont("adbxtra.ttf", 60);
	private static final Font 	DEF_FONT 		= Reader.loadFont("DejaVuSans-ExtraLight.ttf", 15);
	
	BorderPane main = new BorderPane();
	Pane left_border, right_border;
	VBox vbox = new VBox();
	Image border = Reader.get_image("border");
	
	public AboutPane(){
		this.getStylesheets().add(Reader.loadCSS("AboutPane.css"));
		vbox.setMaxWidth(TEXT_PANE_WIDTH);

		Text title = new Text("ABOUT");
		title.setId("title");
		title.setFont(TITLE_FONT);
		
		Text who_made_this = new Text("Who made this?");
		who_made_this.setId("subtitle");
		who_made_this.setFont(SUBTITLE_FONT);
		
		Text mitch_text = new Text(""
				+ "I did. Hello. My name's Mitch Stevens, I am a mediocre third year math student."
				+ " I've been making wacky stuff like this for as long as I can remember."
				+ " All the source code for this game is available at page on my github page https://github.com/MitchStevens/ABED."
				+ " On the extremly (un)unlikely chance the this game does something untowards you can either"
				+ "		\n\tHave a cry and a whine, helping no-one but your unhelpful self, or"
				+ "		\n\tSend me an email me at MitchStevens@gmail.com and do the above."
				+ " \nMaybe you just want to give your thoughts or abuse me. That's fine too.");
		mitch_text.setId("text");
		mitch_text.setWrappingWidth(TEXT_PANE_WIDTH);
		mitch_text.setFont(DEF_FONT);
		
		Text art_stuff = new Text("Everything Else");
		art_stuff.setId("subtitle");
		art_stuff.setFont(SUBTITLE_FONT);
		
		Text lachy_text = new Text(""
				+ "<html>"
				+ "I wrote every single line of code<sup><font color=\"blue\"><u>CITATION NEEDED</u></font></sup> but I didn't make any of the arty stuff."
				+ " I was able to wrangle in help for the sprite design and the game music by my dear brother Lachlan."
				+ " He has other stuff hosted on <a href=\"https://soundcloud.com/lstevprods\">SoundCloud</a>."
				+ "</html>");
		lachy_text.setId("text");
		lachy_text.setWrappingWidth(TEXT_PANE_WIDTH);
		lachy_text.setFont(DEF_FONT);
		
		Text but_why = new Text("But Why?");
		but_why.setId("subtitle");
		but_why.setFont(SUBTITLE_FONT);
		
		Text why = new Text(""
				+ "<html"
				+ "People (normies) don't know logic. NORMIES GET OUT REEEEEEEEEEE."
				+ "</html>");
		why.setId("text");
		why.setFont(DEF_FONT);
		
		vbox.getChildren().addAll(title, who_made_this, mitch_text, art_stuff, lachy_text, but_why, why);
	
		main.setLeft(left_border);
		main.setRight(right_border);
		main.setCenter(vbox);
		
		this.getChildren().add(main);
	}

	@Override
	public void onFocus() {}

	@Override
	public void offFocus() {}
	
	
}

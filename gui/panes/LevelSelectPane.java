package panes;

import java.util.ArrayList;
import java.util.List;

import tutorials.*;
import circuits.Circuit;
import abedgui.Gui;
import javafx.collections.SetChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import logic.Level;
import logic.Reader;

public class LevelSelectPane extends Pane implements SetChangeListener<Level> {
	private static final double COL_WIDTH 	= 1.0/3.0;
	private static final Font 	TITLE_FONT 	= Reader.loadFont("adbxtsc.ttf", 45);
	private static final Font 	LEVEL_FONT 	= Reader.loadFont("Aux DotBitC.ttf", 30);
	
	public static final String[] levelTitles = new String[]{
		"Tutorial",
		"Basic",
		"Single",
		"Dual",
		"Quad"
	};
	
	private static List<VBox> levelColumns = new ArrayList<>();
	private static List<Text> levelText = new ArrayList<>();
	private static HBox wrapper;
	
	public LevelSelectPane() {
		this.setId("main");
		this.getStylesheets().add("res/css/LevelSelectPane.css");
		Level.unlockedLevels.addListener(this);
		//Level.completedLevels.addListener(this);
		
		init();
	}
	
	private void init(){
		this.getChildren().clear();
		wrapper = new HBox();
		wrapper.setPrefHeight(Gui.boardHeight);
		
		for(int i = 0; i < levelTitles.length; i++)
			wrapper.getChildren().add(createLevelColumn(i));
		
		this.getChildren().add(wrapper);
	}
	
	private VBox createLevelColumn(int num){
		VBox tbr = new VBox();
		levelColumns.add(tbr);
		tbr.setPrefWidth(Gui.boardWidth*COL_WIDTH);
		
		Label title = new Label(num+". "+levelTitles[num]);
		title.setFont(TITLE_FONT);
		tbr.getChildren().add(title);
		
		tbr.getChildren().add(createLevelBox(null, null, false));
		
		for(Level l : Level.search( lvl -> {return lvl.tuple.i == num;} )){
			Pane p = createLevelBox(l, num+"."+l.tuple.j+": "+l.name, Level.unlockedLevels.contains(l));
			tbr.getChildren().add(p);
		}
		
		return tbr;
	}
	
	private Pane createLevelBox(Level l, String s, boolean unlocked){
		Text txt = new Text(s);
		levelText.add(txt);
		txt.setWrappingWidth(Gui.boardWidth*COL_WIDTH);
		txt.setFont(LEVEL_FONT);
		
		Pane p = new Pane();
		p.getChildren().add(txt);
		if(unlocked){
			p.setOnMouseClicked(e -> {
				GamePane.setLevel(l);
				Gui.setCurrentPane(Gui.gamePane);
			});
		} else {
			txt.setFill(Color.GRAY);
		}
		return p;
	}
	
	public static void onResize(){
		wrapper.setPrefHeight(Gui.boardHeight);
	}

	@Override
	public void onChanged(Change change) {
		//change.
		init();
	}
}

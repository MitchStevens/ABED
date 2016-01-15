package panes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controls.Typer;
import tutorials.*;
import circuits.Circuit;
import data.Reader;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
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

public class LevelSelectPane extends Pane implements SetChangeListener<Level>, ScreenPane {
	private static final double COL_WIDTH 	= 1.0/3.0;
	private static final Font 	TITLE_FONT 	= Reader.loadFont("adbxtsc.ttf", 45);
	private static final Font 	LEVEL_FONT 	= Reader.loadFont("Aux DotBitC.ttf", 20);
	
	private static List<VBox> levelColumns = new ArrayList<>();
	private static List<Typer> levelTypers = new ArrayList<>();
	private static HBox wrapper;
	
	public LevelSelectPane() {
		this.setId("main");
		this.getStylesheets().add(Reader.loadCSS("LevelSelectPane.css"));
		Reader.unlocked_levels.addListener(this);
		//Level.completedLevels.addListener(this);		
		init();
	}
	
	private void init(){
		this.getChildren().clear();
		wrapper = new HBox();
		wrapper.setPrefHeight(Gui.boardHeight);
		
		for(int i = 0; i < Reader.LEVEL_CATEGORIES.size(); i++)
			wrapper.getChildren().add(createLevelColumn(i));
		
		this.getChildren().add(wrapper);
	}
	
	private VBox createLevelColumn(int num){
		VBox tbr = new VBox();
		levelColumns.add(tbr);
		
		Label title = new Label(num+". "+Reader.LEVEL_CATEGORIES.get(num));
		title.setFont(TITLE_FONT);
		tbr.getChildren().add(title);
		
		for(Level l : Level.search( lvl -> {return lvl.tuple.i == num;} )){
			Pane p = createLevelBox(l, num+"."+l.tuple.j+": "+l.name, Reader.unlocked_levels.contains(l));
			tbr.getChildren().add(p);
		}
		
		tbr.setPadding(new Insets(30, 30, 30, 30));
		tbr.setSpacing(20);
		return tbr;
	}
	
	private Pane createLevelBox(Level l, String s, boolean unlocked){
		Typer t = new Typer(s, 15);
		t.setFont(LEVEL_FONT);
		t.setWrappingWidth(Gui.boardWidth*COL_WIDTH);
		levelTypers.add(t);
		if(unlocked){
			t.setId("unlocked");
			t.setOnMouseClicked(e -> {
				CircuitPane.setLevel(l);
				Gui.setCurrentPane("game_pane");
			});
		} else {
			t.setId("locked");
		}
		
		Pane p = new Pane();
		if( l != null)
			p.setId( l.tuple.j % 2 == 0 ? "even_panel" : "odd_panel" );
		p.getChildren().add(t);
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

	@Override
	public void onFocus() {		
		Random random = new Random();
		List<Integer> nums = new ArrayList<>();
		for(int i = 0; i < levelTypers.size(); i++)
			nums.add(i);
		
		try{
			for(int i = 0; i < levelTypers.size(); i++){
				int r = random.nextInt(nums.size());
				levelTypers.get(nums.get(r)).play();
				nums.remove(r);
				//Thread.sleep(500);
			}
		} catch(Exception e){}
		
	
	}

	@Override
	public void offFocus() {}
}

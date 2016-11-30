package gui.panes;

import java.util.HashMap;
import java.util.Map;

import core.game.Game;
import data.Reader;
import data.Writer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.*;
import javafx.stage.Stage;

public class Gui extends Application {
	public final static double 			SIDE_BAR_WIDTH = 300;
	public final static double 			MIN_WIDTH = 800;
	public final static double 			MIN_HEIGHT = 600;
	public final static String			VERSION = "1.0";
	public final static DoubleProperty 	BOARD_HEIGHT = new SimpleDoubleProperty(768);
	public final static DoubleProperty	BOARD_WIDTH	 = new SimpleDoubleProperty(1024); 

	public 				Group 			root;
	private				Scene 			scene;
	
	public static 		Map<String, ScreenPane> screens = new HashMap<>();
	
	public static 		Pane 			mainPane;
	public static 		Gui 			g;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("ABED: Version "+ VERSION);
		root = new Group();
		scene = new Scene(root, BOARD_WIDTH.get(), BOARD_HEIGHT.get());
		
		initalise_board();
		stage.setScene(scene);
		stage.setMinHeight(MIN_HEIGHT);
		stage.setMinWidth(MIN_WIDTH);
		stage.show();
		g = this;
		
		stage.setOnCloseRequest(e -> {
			//Writer.save_all();
		});
	}
	
	private void initalise_board() {	
		mainPane = new StackPane();

		//screens.put("title_pane", new TitlePane());
		//screens.put("level_select_pane", new LevelSelectPane());
		screens.put("game_pane", new GamePane());
		//screens.put("about_pane", new AboutPane());
		//screens.put("win_pane", new WinPane());
		
		mainPane.getChildren().add((Node) screens.get("game_pane"));
		root.getChildren().add(mainPane);
		CircuitPane.new_game(new Game(4));

		resize_listeners();
	}

	public static void set_pane(String s){
		mainPane.getChildren().clear();
		if(screens.containsKey(s)){
			mainPane.getChildren().add((Node) screens.get(s));
			screens.get(s).onFocus();
		}
	}
	
	private void resize_listeners() {
		BOARD_WIDTH.bind(scene.widthProperty());
		BOARD_HEIGHT.bind(scene.heightProperty());
	}

	public static void open() {
		Application.launch();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
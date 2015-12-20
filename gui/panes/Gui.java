package panes;

import java.util.HashMap;
import java.util.Map;

import circuits.Circuit;
import panes.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.Game;
import logic.Reader;

public class Gui extends Application {
	public final static double 			SIDE_BAR_WIDTH = 300;
	public final static double 			MIN_WIDTH = 800;
	public final static double 			MIN_HEIGHT = 600;

	public static 		double 			boardWidth = 1024;
	public static 		double 			boardHeight = 768;

	public 				Group 			root;
	private				Scene 			scene;
	
	public static 		Map<String, ScreenPane> screens = new HashMap<>();
	
	public static 		Pane 			mainPane;
	
	public static 		Gui 			g;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ABED");
		root = new Group();
		scene = new Scene(root, boardWidth, boardHeight);

		initaliseBoard();
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(MIN_HEIGHT);
		primaryStage.setMinWidth(MIN_WIDTH);
		primaryStage.show();
		g = this;
	}
	
	private void initaliseBoard() {
		mainPane = new StackPane();

		TitlePane titlePane = new TitlePane();
		screens.put("title_pane", titlePane);
		
		LevelSelectPane levelSelectPane = new LevelSelectPane();
		screens.put("level_select_pane", levelSelectPane);
		
		GamePane gamePane = new GamePane();
		screens.put("game_pane", gamePane);
		
		AboutPane aboutPane = new AboutPane();
		screens.put("about_pane", aboutPane);
		
		mainPane.getChildren().add(titlePane);
		root.getChildren().add(mainPane);
		CircuitPane.newGame(new Game(7));

		windowResizeListeners();
	}

	public static void setCurrentPane(String s){
		//fix this you damn fool
		mainPane.getChildren().clear();
		if(screens.containsKey(s)){
			mainPane.getChildren().add((Node) screens.get(s));
			screens.get(s).onFocus();
		}
	}
	
	private void windowResizeListeners() {
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(
					ObservableValue<? extends Number> observableValue,
					Number oldSceneWidth, Number newSceneWidth) {
				boardWidth = (double) newSceneWidth;
				CircuitPane.on_resize();
				//LevelSelectPane.onResize();
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(
					ObservableValue<? extends Number> observableValue,
					Number oldSceneHeight, Number newSceneHeight) {
				boardHeight = (double) newSceneHeight;
				CircuitPane.on_resize();
				//LevelSelectPane.onResize();
			}
		});
	}

	public static void open() {
		Application.launch();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
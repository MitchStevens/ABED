package abedgui;

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
import logic.Circuit;
import logic.Game;
import logic.Reader;

public class Gui extends Application {
	public final static double SIDE_BAR_WIDTH = 300;
	public final static double MIN_WIDTH = 800;
	public final static double MIN_HEIGHT = 600;

	public static double boardWidth = 1024;
	public static double boardHeight = 768;

	public Group root;
	Scene scene;

	public static SideBarPane sideBar;
	public static GamePane abedPane;
	public static BorderPane gamePane;

	public static TitlePane titlePane;

	public static LevelSelectPane levelSelectPane;

	public static Pane mainPane;
	public static Gui g;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ABED");
		root = new Group();
		scene = new Scene(root, boardWidth, boardHeight);

		Reader.loadAll();
		initaliseBoard();
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(MIN_HEIGHT);
		primaryStage.setMinWidth(MIN_WIDTH);
		primaryStage.show();
		g = this;
	}
	
	private void initaliseBoard() {
		mainPane = new StackPane();

		sideBar = new SideBarPane();
		abedPane = new GamePane();

		gamePane = new BorderPane();
		gamePane.setLeft(abedPane);
		gamePane.setCenter(sideBar);

		titlePane = new TitlePane();
		levelSelectPane = new LevelSelectPane();
		
		mainPane.getChildren().add(titlePane);
		root.getChildren().add(mainPane);
		GamePane.newGame(new Game(3));

		windowResizeListeners();
	}

	public static void setCurrentPane(Pane p){
		mainPane.getChildren().clear();
		mainPane.getChildren().add(p);
	}
	
	private void windowResizeListeners() {
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(
					ObservableValue<? extends Number> observableValue,
					Number oldSceneWidth, Number newSceneWidth) {
				boardWidth = (double) newSceneWidth;
				GamePane.resizeWidth();
				LevelSelectPane.onResize();
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(
					ObservableValue<? extends Number> observableValue,
					Number oldSceneHeight, Number newSceneHeight) {
				boardHeight = (double) newSceneHeight;
				GamePane.resizeHeight();
				LevelSelectPane.onResize();
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
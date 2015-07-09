package abedgui;

import panes.GamePane;
import panes.SideBarPane;
import data.Reader;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.Circuit;
import logic.Game;

public class Gui extends Application{
    public Group root;
    Scene scene;
	
    public static double boardWidth = 1024;
    public static double boardHeight = 768;
	
    public static SideBarPane sideBar;
    public static GamePane abedPane;
    public static BorderPane gamePane;
    public static Pane levelSelectPane;
    public static Pane mainPane;
    public static Gui g;

    @Override
    public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ABED");
		root = new Group();
		scene = new Scene(root, boardWidth, boardHeight);
		//TODO: use Reader to reference the css file.
//		scene.getStylesheets().add
//			(Reader.class.getResource("Gui.css").toExternalForm());
		
		initaliseBoard();
		primaryStage.setScene(scene);
		primaryStage.show();
		g = this;
	}
	
	private void initaliseBoard(){
		mainPane = new Pane();
		//mainPane.getChildren().add(new GuiLevelSelect());
		
		sideBar = new SideBarPane();
		abedPane = new GamePane();
		
		gamePane = new BorderPane();
		gamePane.setRight(sideBar);
		gamePane.setCenter(abedPane);
		
		mainPane.getChildren().add(gamePane);
		root.getChildren().add(mainPane);
		GamePane.newGame(new Game(6));
	}

	public void getLevelSelectPane(){
		levelSelectPane = new VBox();
	}
    
    public static void open() {Application.launch();}
    public static void main(String[] args) { launch(args); }
}
package testing;

import abedgui.Gui;
import abedgui.PieceImage;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestingPieceImage extends Application{
    public Group root;
    Scene scene;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Testing");
		root = new Group();
		scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void add(PieceImage pi){
		root.getChildren().add(pi);
	}
	
    public static void open() {Application.launch();}
    public static void main(String[] args) { launch(args); }
}

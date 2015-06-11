package abedgui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.Circuit;
import logic.Game;

public class Gui extends Application{
    public Group root;
    Scene scene;

    public static double GAME_MARGIN = 50;
    public static double GAP = 0;
	
    public static double boardWidth = 1024;
    public static double boardHeight = 768;
    public static double tileSize = 0;
    public static int numTiles = 3;
	
    public Game currentGame = new Game(numTiles);
    public static List<Square> allSquares;
	
    public static VBox sideBar;
    public static Pane abedPane;
    public static BorderPane gamePane;
    public static Pane levelSelectPane;
    public static Pane mainPane;
    private static Gui board;

    @Override
    public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ABED");
		root = new Group();
		scene = new Scene(root, boardWidth, boardHeight);
		
		initaliseBoard();
		primaryStage.setScene(scene);
		primaryStage.show();
		board = this;
	}
	
	private void initaliseBoard(){		
		currentGame = new Game(numTiles);
		tileSize = (boardHeight - 2*GAME_MARGIN - (numTiles-1)*GAP)/numTiles;
		mainPane = new Pane();
		
		//new Reader().getGates();
		getSideBar();
		getAbedPane();
		
		gamePane = new BorderPane();
		gamePane.setRight(sideBar);
		gamePane.setCenter(abedPane);
		
		mainPane.getChildren().add(gamePane);
		mainPane.setMinHeight(boardHeight);
		mainPane.setMinWidth(boardWidth);
		root.getChildren().add(mainPane);
	}

	public void getLevelSelectPane(){
		levelSelectPane = new VBox();
	}
	
	public void updateGraphics(){
		abedPane.getChildren().filtered(n -> n instanceof Piece).forEach(p -> ((Piece)p).updateImage());
	}
	
    public void getSideBar(){
    	sideBar = new VBox();
    	TreeItem<Label> root = new TreeItem<>(new Label("Gates"));
        sideBar.setStyle(
            "-fx-background-color: GRAY;"
          + "-fx-min-width:		   "+(boardWidth-boardHeight)+";");
        
        for(int i = 0; i < Circuit.circuitTypes.length; i++)
        	root.getChildren().add(new TreeItem<>(new Label(Circuit.circuitTypes[i])));
        
        for(Circuit c : Circuit.loadedCircuits.values()){
        	Label l = new Label(c.name);
            	
        	l.setOnMouseClicked(e -> {
        		Circuit c1 = c.clone();
//                    addPiece(c1);
          	});
            
        	l.setOnMouseEntered(e -> {
            	l.setFont(new Font(l.getFont().getSize() +2));
      		});
            
        	l.setOnMouseExited(e -> {
        		l.setFont(new Font(l.getFont().getSize() -2));
        	});
        		
            TreeItem<Label> label = new TreeItem<>(l);
            	
            label.setGraphic(new ImageView(c.getSprite()));
            	
            root.getChildren().get(c.type).getChildren().add(label);
        }
        
        TreeView<Label> gateSelector = new TreeView<>(root);
        gateSelector.setShowRoot(false);
        sideBar.getChildren().add(gateSelector);
        
        Button saveGate = new Button();
        saveGate.setOnMouseClicked(e -> {
        	
        });
    }
    
    public void getAbedPane(){
    	abedPane = new Pane();
    	abedPane.setStyle(
    		"-fx-background-color: BLUE;"
    		+ "-fx-padding: "+20+";");
		
    	allSquares = new ArrayList<Square>();
    	for(int i = 0; i < numTiles; i++)
    		for(int j = 0; j < numTiles; j++){
    			Square temp = new Square(
            	GAME_MARGIN + j*(tileSize + GAP),
              	GAME_MARGIN + i*(tileSize + GAP),
             	i,
              	j);
       		if(i == 0 || i == numTiles-1) temp.setIsOnSide();
       		if(j == 0 || j == numTiles-1) temp.setIsOnSide();
       		allSquares.add(temp);
    	}
	
    	abedPane.getChildren().addAll(allSquares);
    }
    
    public static Gui getBoard(){
        return board;
    }
	
    public Square getClosest(double x, double y){
	Square closest = null;
	double minDist = Double.MAX_VALUE;
	for(Square s : allSquares)
		if(s.distance(x, y) < minDist){
			closest = s;
			minDist = s.distance(x, y);
    	}
        return minDist < tileSize? closest: null;				
    }
    
    public static void open() {Application.launch();}
    public static void main(String[] args) { launch(args); }
}
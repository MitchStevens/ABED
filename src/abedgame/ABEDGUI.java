package abedgame;

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

public class ABEDGUI extends Application{
    public Group root;
    Scene scene;

    public static double GAME_MARGIN = 50;
    public static double GAP = 0;
	
    public static double boardWidth = 1024;
    public static double boardHeight = 768;
    public static double tileSize = 0;
    public static int numTiles = 6;
	
    public Game currentGame = new Game(numTiles);
    public static List<Square> allSquares;
	
    public static VBox sideBar;
    public static Pane abedPane;
    public static BorderPane mainPane;
    private static ABEDGUI board;

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
		new Reader().getGates();
		currentGame = new Game(numTiles);
		tileSize = (boardHeight - 2*GAME_MARGIN - (numTiles-1)*GAP)/numTiles;
		mainPane = new BorderPane();
		getSideBar();
		getAbedPane();
		
		mainPane.setRight(sideBar);
		mainPane.setCenter(abedPane);
		
		mainPane.setMinHeight(boardHeight);
		mainPane.setMinWidth(boardWidth);
		root.getChildren().add(mainPane);
	}

	
    public void getSideBar(){
    	sideBar = new VBox();
    	TreeItem<Label> root = new TreeItem<>(new Label("Gates"));
        sideBar.setStyle(
            "-fx-background-color: GRAY;"
          + "-fx-min-width:		   "+(boardWidth-boardHeight)+";");
                
        for(int i = 0; i < Gate.gateNames.length; i++){
            TreeItem<Label> tempTree = new TreeItem<>(new Label(Gate.gateNames[i]));
            for(String s : Gate.gateTypes[i]){
            	Label l = new Label(s);
            	Gate gate = (Gate) Gate.allGates.get(s);
            	ImageView icon  = new ImageView(gate != null ? gate.getSprite() : new Image("/images/emptyGate.bmp"));
            	
            	l.setOnMouseClicked(event -> {
                    Gate g = (Gate) Gate.allGates.get(s).clone();
                    if(g != null){
                        Piece p = new Piece(g);
                        currentGame.placePieceAtEmpty(p);
                    } else System.err.println("gate "+s+" does not exist!");
                });
            
            	l.setOnMouseEntered(EventHandler -> {
            		l.setFont(new Font(l.getFont().getSize() +2));
            	});
            
        		l.setOnMouseExited(event -> {
        			l.setFont(new Font(l.getFont().getSize() -2));
            	});
            	
            	TreeItem<Label> tempGate = new TreeItem<>(l);
            	tempGate.setGraphic(icon);
            	
            	
            	tempTree.getChildren().add(tempGate);
            }
            root.getChildren().add(tempTree);
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
    
    public static ABEDGUI getBoard(){
        return board;
    }
	
    public Object getClosest(double x, double y){
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
package abedgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ABEDGUI extends Application{
    public Group root;
    Scene scene;

    public static double GAME_MARGIN = 50;
    public static double GAP = 0;
	
    public static double boardWidth = 1024;
    public static double boardHeight = 768;
    public static double tileSize = 0;
    public static int numTiles = 8;
	
    public Game currentGame = new Game(numTiles);
    public static List<Square> allSquares;
	
    public static Accordion sideBar;
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
		Gate.readGates();
		
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
	
//	public void displayGame(Game game){
//		root.getChildren().clear();
//		this.currentGame = game;
//		this.currentPieces = new ArrayList<>();
//		for(Gate g : game.allGates){
//			Piece p = new Piece(g.getClass().getSimpleName());
//			currentPieces.add(p);
//			root.getChildren().add(p);
//		}
//	}
	
    public void getSideBar(){
        sideBar = new Accordion();
        sideBar.setStyle(
            "-fx-background-color: GRAY;"
          + "-fx-min-width:		   "+(boardWidth-boardHeight)+";");
        
        Pane info = new Pane();
        info.setMinWidth(boardWidth - boardHeight);
        Text objective = new Text(""
        		+ "Current objective:\n"
        		+ "Try and fix the rotation while dragging bug.\n"
        		+ "Slap on an intro screen for the begining of the game.\n"
        		+ "Fix wire sprites (ugh).");
        objective.setWrappingWidth(boardWidth - boardHeight);
        info.getChildren().add(objective);
        sideBar.getPanes().add(new TitledPane("Game Information", info));
        
        for(int i = 0; i < Gate.gateNames.length; i++){
            VBox tempVBox = new VBox();
            for(String s : Gate.gateTypes[i]){
                Label temp = new Label(s);
                temp.setOnMousePressed(event -> {
                        Gate g = (Gate) Gate.allGates.get(s).clone();
                        if(g != null){
                            Piece p = new Piece(g);
                            currentGame.placePieceAtEmpty(p);
                        } else System.err.println("gate "+s+" does not exist!");
                        
                    });
                
                temp.setOnMouseEntered(EventHandler -> {
                    temp.setFont(new Font(temp.getFont().getSize() +2));
                });
                
                temp.setOnMouseExited(event -> {
                    temp.setFont(new Font(temp.getFont().getSize() -2));
                });
                
                tempVBox.getChildren().add(temp);
            }
            sideBar.getPanes().add(new TitledPane(Gate.gateNames[i], tempVBox));
        }	
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
       		if(i == 0 || i == numTiles-1)
       			temp.setIsOnSide();
       		if(j == 0 || j == numTiles-1)
       			temp.setIsOnSide();
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
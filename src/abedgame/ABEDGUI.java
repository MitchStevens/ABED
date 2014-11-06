package abedgame;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
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
    public static int numTiles = 6;
	
    public Game currentGame = new Game(6);
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
		mainPane = new BorderPane();
		tileSize = (boardHeight - 2*GAME_MARGIN - (numTiles-1)*GAP)/numTiles;
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
        for(int i = 0; i < Gate.gateNames.length; i++){
            VBox tempVBox = new VBox();
            for(String s : Gate.gateTypes[i]){
                Label temp = new Label(s);
                temp.setOnMousePressed(event -> {
                        Gate g = null;
                        try {
                            g = (Gate)Class.forName("abedgame."+s).newInstance();
                        } catch (ClassNotFoundException ex) {
                            System.err.println(s+" is not a class, fuckstick.");
                        } catch (IllegalAccessException ex) {
                            System.err.println("I...have no idea what's wrong.");
                        } catch (InstantiationError ex) {
                            System.err.println("Your class "+s+" doesn't instanciate with 0 parameters, does it? Go fix that.");
                        } catch (InstantiationException ex) {
                            System.err.println("Your class "+s+" doesn't instanciate with 0 parameters, does it? Go fix that.");}
                        Piece p = new Piece(g);
                        root.getChildren().add(p);
                        p.setLayoutX(event.getSceneX());
                        p.setLayoutY(event.getSceneY());
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
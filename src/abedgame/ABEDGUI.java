package abedgame;

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
import static abedgame.Functions.*;

public class ABEDGUI extends Application{
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
		currentGame = new Game(numTiles);
		tileSize = (boardHeight - 2*GAME_MARGIN - (numTiles-1)*GAP)/numTiles;
		mainPane = new Pane();
		
		new Reader().getImages();
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
		List<Node> pieces = filter(n -> n instanceof Piece, abedPane.getChildren());
		pieces.forEach(p -> ((Piece)p).updateImage());
	}
	
    public void getSideBar(){
    	sideBar = new VBox();
    	TreeItem<Label> root = new TreeItem<>(new Label("Gates"));
        sideBar.setStyle(
            "-fx-background-color: GRAY;"
          + "-fx-min-width:		   "+(boardWidth-boardHeight)+";");
        
        //get all the gates.
        try {
			InputStream is = this.getClass().getResourceAsStream("/data/gateData.txt");
			InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(isr);
            String line;
            
            while ((line = bf.readLine()) != null) {
            	if(line.charAt(0) == '/') continue;
            	if(line.charAt(0) == '#'){
            		
            	}
           		Gate g = new Gate(line);
           		Gate.allGates.put(g.name, g);
            }
            bf.close();
   	 	} catch (IOException e) {
   		 System.err.println("reading file is fLIcked");
   	 	}
		Gate.allGates.put("Input", new Input());
		Gate.allGates.put("Output", new Output());
        
                
        for(int i = 0; i < Gate.gateNames.length; i++){
            TreeItem<Label> tempTree = new TreeItem<>(new Label(Gate.gateNames[i]));
            for(String s : Gate.gateTypes[i]){
            	Label l = new Label(s);
            	//Gate gate = (Gate) Gate.allGates.get(s);
            	
            	l.setOnMouseClicked(event -> {
                    Gate g = (Gate) Gate.allGates.get(s).clone();
                    addPiece(g);
                });
            
            	l.setOnMouseEntered(EventHandler -> {
            		l.setFont(new Font(l.getFont().getSize() +2));
            	});
            
        		l.setOnMouseExited(event -> {
        			l.setFont(new Font(l.getFont().getSize() -2));
            	});
        		
            	TreeItem<Label> tempGate = new TreeItem<>(l);
            	Gate g = Gate.allGates.get(s);
            	System.out.println(Gate.allGates.keySet());
            	
            	if(Gate.allSprites.get(s) != null)
            		 tempGate.setGraphic(new ImageView(Gate.allSprites.get(g.defSprite)));
            	else tempGate.setGraphic(new ImageView(Gate.allSprites.get("emptyGate.bmp")));
            	
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
	
    public void addPiece(Gate g){
        if(g != null){
            //currentGame.createGateAtEmpty(g);
            Piece p = new Piece(g);
            p.setVisible(true);
            Square pos = allSquares.get(currentGame.n*g.i + g.j);
            p.setLayoutX(pos.x);
            p.setLayoutY(pos.y);
            abedPane.getChildren().add(p);
        } else System.err.println("gate "+g.name+" does not exist!");
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
package abedgui;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
	
    public static Game currentGame = new Game(numTiles);
    public static Square[][] allSquares;
	
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
		mainPane = new Pane();
		
		getSideBar();
		getAbedPane();
		
		gamePane = new BorderPane();
		gamePane.setRight(sideBar);
		gamePane.setCenter(abedPane);
		
		mainPane.getChildren().add(gamePane);
		root.getChildren().add(mainPane);
		newGame(new Game(7));
	}

	public void getLevelSelectPane(){
		levelSelectPane = new VBox();
	}
	
	public static void addPiece(Piece p){
		Square s = currentGame.nextOpen();
		addPiece(p, s.i, s.j);
	}
	
	public static void addPiece(Piece p, Integer i, Integer j){
		currentGame.add(p.c, i, j);
		p.setLayoutX(allSquares[i][j].x);
		p.setLayoutY(allSquares[i][j].y);
		abedPane.getChildren().add(p);
		updateBoard();
	}
	
	public static void rotatePiece(int i, int j, int rot){
		currentGame.rotate(i, j, rot);
		updateBoard();
	}
	
	public static void movePiece(Piece p, int i, int j){
		currentGame.remove(p.c.i, p.c.j);
		currentGame.add(p.c, i, j);
    	p.setLayoutX(allSquares[i][j].x);
    	p.setLayoutY(allSquares[i][j].y);
		updateBoard();
	}
	
	public static void removePiece(Piece p){
		currentGame.remove(p.c.i, p.c.j);
		abedPane.getChildren().remove(p);
		updateBoard();
	}
	
	public static void updateBoard(){
		for(Node n : abedPane.getChildren())
			if(n instanceof Piece)
				((Piece)n).updateImage();
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
        		addPiece(new Piece(c.clone()));
          	});
            
        	l.setOnMouseEntered(e -> {
            	l.setFont(new Font(l.getFont().getSize() +2));
      		});
            
        	l.setOnMouseExited(e -> {
        		l.setFont(new Font(l.getFont().getSize() -2));
        	});
        		
            TreeItem<Label> label = new TreeItem<>(l);
            	
            label.setGraphic(new PieceImage(c, 20));
            	
            root.getChildren().get(c.type).getChildren().add(label);
        }
        
        TreeView<Label> gateSelector = new TreeView<>(root);
        gateSelector.setShowRoot(false);
        sideBar.getChildren().add(gateSelector);
        
        Button saveGate = new Button("Save Gate");
        saveGate.setOnMouseClicked(e -> {
        	System.out.println(currentGame.toString());
        });
        sideBar.getChildren().add(saveGate);
    }
    
    public void getAbedPane(){
    	abedPane = new Pane();
    	abedPane.setStyle(
    		"-fx-background-color: BLUE;"
    		+ "-fx-padding: "+20+";");
    	abedPane.setPrefSize(boardHeight, boardHeight);
    }
    
    public void newGame(Game g){
    	//loads new game into the gui and sets current game
		currentGame = g;
		numTiles = g.n;
		tileSize = (boardHeight - 2*GAME_MARGIN - (numTiles-1)*GAP)/numTiles;
    	
    	allSquares = new Square[numTiles][numTiles];
    	for(int j = 0; j < numTiles; j++)
    		for(int i = 0; i < numTiles; i++){
    			Square temp = new Square(
            	GAME_MARGIN + i*(tileSize + GAP),
              	GAME_MARGIN + j*(tileSize + GAP),
             	i,
              	j);
       		if(i == 0 || i == numTiles-1) temp.setIsOnSide();
       		if(j == 0 || j == numTiles-1) temp.setIsOnSide();
       		allSquares[i][j] = temp;
       		abedPane.getChildren().add(allSquares[i][j]);
    		}
    	
    	Circuit c = null;
    	for(int j = 0; j < numTiles; j++)
    		for(int i = 0; i < numTiles; i++)
    			if((c = currentGame.circuitAtPos(i, j)) != null){
    				Piece p = new Piece(c);
    				addPiece(p, c.i, c.j);
    			}
    }
    
    public static Gui getBoard(){
        return board;
    }
	
    public Square getClosest(double x, double y){
		Square closest = null;
		double minDist = Double.MAX_VALUE;
		for(Square[] sArray : allSquares)
			for(Square s : sArray)
				if(s.distance(x, y) < minDist){
					closest = s;
					minDist = s.distance(x, y);
				}
		return closest;
    }
    
    public static void open() {Application.launch();}
    public static void main(String[] args) { launch(args); }
}
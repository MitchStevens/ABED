package panes;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import controls.CircuitFinder;
import controls.WinMessage;
import tutorials.Action;
import tutorials.Tute1;
import tutorials.Tutorial;
import circuits.BusIn;
import circuits.BusOut;
import circuits.Cable;
import circuits.Circuit;
import circuits.Coord;
import eval.Evaluator;
import graphics.Piece;
import graphics.Square;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Game;
import logic.Level;
import logic.PathFinder;
import logic.Reader;
import static java.lang.Math.*;

public class CircuitPane extends Pane implements Observer{
	public final static double 			GAME_MARGIN 		= 50;
	public final static double 			GAP 				= 0;
	
	private static CircuitFinder		cf					= new CircuitFinder("");
	
	public static ObservableSet<Circuit> 	unlockedCircuits = Reader.loadUnlockedCircuits();
	public static Set<Circuit>				new_circuits	 = new HashSet<>(unlockedCircuits);
	
	public 	static double 				tileSize 			= 0;
	public 	static int 					numTiles 			= 3;
	public	static Game 				currentGame;
	public	static Map<Coord, Square> 	allSquares;
	public	static boolean 				unlockAllCircuits 	= false;
	public	static Text[]				side_markers		= new Text[4];
	public	static Tutorial				tutorial; 
	
	public	static Level 				currentLevel;
	private static boolean 				gameWon 			= false;
	public	static Coord 				lastClicked 		= null;
	public 	static CircuitPane 			cp;
	
	public CircuitPane() {
		this.getStylesheets().add("res/css/GamePane.css");
		this.setPrefSize(Gui.boardWidth - Gui.SIDE_BAR_WIDTH, Gui.boardHeight);
		
		cp = this;
		cp.setId("main");
		
		cf.setLayoutX(0);
		cf.setLayoutY(0);
		this.getChildren().add(cf);
		cf.setFill(Color.WHITE);
		this.setOnKeyPressed(e -> {
			cf.key_pressed(e.getCode());
		});
		
	}
	
	public static void calcTileSize() {
		tileSize = (min(Gui.boardHeight, Gui.boardWidth - Gui.SIDE_BAR_WIDTH)
				- 2 * GAME_MARGIN - (numTiles - 1) * GAP) / numTiles;
	}

	public static void set_sandbox_mode(boolean b){
		if(b ==  unlockAllCircuits)
			return;
		
		
	}
	
	public static void addPiece(Piece p) {
		Coord coord = currentGame.nextOpen();
		if (coord != null)
			addPiece(p, coord);
	}

	public static void addPiece(Piece p, Coord coord) {
		currentGame.add(p.c, coord);
		p.setLayoutX(allSquares.get(coord).getLayoutX());
		p.setLayoutY(allSquares.get(coord).getLayoutY());
		cp.getChildren().add(p);
	}

	public static void rotatePiece(Coord coord, int rot) {
		currentGame.rotate(coord, rot);
	}

	public static void movePiece(Piece p, Coord coord) {
		if (currentGame.circuitAtPos(coord) == null
				&& allSquares.get(coord).loc != 2) {
			currentGame.remove(p.c.coord);
			currentGame.add(p.c, coord);
			p.setLayoutX(allSquares.get(coord).getLayoutX());
			p.setLayoutY(allSquares.get(coord).getLayoutY());
		} else {
			p.setLayoutX(allSquares.get(p.c.coord).getLayoutX());
			p.setLayoutY(allSquares.get(p.c.coord).getLayoutY());
		}
	}

	public void removePiece(Piece p) {
		currentGame.remove(p.c.coord);
		this.getChildren().remove(p);
	}

	public static void togglePiece(Piece p) {
		currentGame.toggle(p.c.coord);
	}

	public static void setLevel(Level l) {
		currentLevel = l;
		newGame(new Game(l.gameSize));
		SideBarPane.textArea.setText(l.instructionText);
		
//		if(Tutorial.ALL_TUTORIALS.get(l.name) != null){
//			tutorial = Tutorial.ALL_TUTORIALS.get(l.name);
//			tutorial.start();
//		} else {
//			if(tutorial != null)
//				tutorial.end();
//			tutorial = null;
//		}
		
		set_chevrons();
	}
	
	private static void set_chevrons(){
		if(currentLevel == null) return;
		
		for(int side = 0; side < 4; side++){
			if(currentLevel.objective.buses.get(side) == null)
				continue;
			else if(currentLevel.objective.buses.get(side) instanceof BusIn)
				set_chevrons_on_side(side, Circuit.mod4(side - 2));
			else if(currentLevel.objective.buses.get(side) instanceof BusOut)
				set_chevrons_on_side(side, side);
		}
	}
	
	private static void set_chevrons_on_side(int side, int dir){
		int n = numTiles;
		
		switch(side){
		case 0:
			for(int i = 1; i < n-1; i++)
				allSquares.get(new Coord(i, 0)).set_chevron(dir);
			break;
		case 1:
			for(int j = 1; j < n-1; j++)
				allSquares.get(new Coord(n-1, j)).set_chevron(dir);
			break;
		case 2:
			for(int i = 1; i < n-1; i++)
				allSquares.get(new Coord(i, n-1)).set_chevron(dir);
			break;
		case 3:
			for(int j = 1; j < n-1; j++)
				allSquares.get(new Coord(0, j)).set_chevron(dir);
			break;
		}
	}
	
	public static void newGame(Game g) {
		// loads new game into the gui and sets current game
		currentGame = g;
		currentGame.addObserver(cp);
		currentGame.notifyObservers(Action.NEW);

		numTiles = g.n;
		calcTileSize();
		cp.getChildren().clear();
		gameWon = false;
		
		allSquares = new HashMap<>();
		SideBarPane.inc.set(numTiles);
		
		update_squares();

		Circuit c = null;
		for (int j = 0; j < numTiles; j++)
			for (int i = 0; i < numTiles; i++)
				if ((c = currentGame.circuitAtPos(new Coord(i, j))) != null) {
					Piece p = new Piece(c);
					addPiece(p, c.coord);
				}
		
		for(int i = 0; i < 4; i++){
			side_markers[i] = new Text(""+i);
			side_markers[i].setFont(Reader.loadFont("adbxtra.ttf", 50));
			side_markers[i].setFill(Color.WHITE);
			cp.getChildren().add(side_markers[i]);
		}
		
		update_size_markers();
	}

	public static void update_size_markers(){
		double side_gap = 10.0;
		double init = (Gui.boardWidth - Gui.boardHeight - Gui.SIDE_BAR_WIDTH) / 2;
		side_markers[0].setLayoutX((Gui.boardWidth-Gui.SIDE_BAR_WIDTH)/2 -16);
		side_markers[0].setLayoutY(GAME_MARGIN + max(0, -init) - side_gap);
		side_markers[1].setLayoutX(GAME_MARGIN + tileSize*currentGame.n + max(0, init) -5 + side_gap);
		side_markers[1].setLayoutY(Gui.boardHeight/2 + 15);
		side_markers[2].setLayoutX((Gui.boardWidth-Gui.SIDE_BAR_WIDTH)/2 - 16);
		side_markers[2].setLayoutY(GAME_MARGIN + tileSize*currentGame.n + max(0, -init) + 30 + side_gap);
		side_markers[3].setLayoutX(GAME_MARGIN + max(0, init) - side_gap - 30);
		side_markers[3].setLayoutY(Gui.boardHeight/2 + 15);
	}
	
	public static void incSize(int newSize) {
		// increments size of board
		if (newSize > Game.MAX_TILES || newSize == numTiles)
			return;
		int oldSize = numTiles;
		numTiles = newSize;
		calcTileSize();
		update_squares();

		// reposition pieces in scene
		for (Node n : cp.getChildren())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				p.changePos(allSquares.get(p.c.coord));
				p.onResize();
			}
		
		//delete circuits on edges from game
		for(int i = 0; i < oldSize; i++)
			if(currentGame.circuitAtPos(new Coord(i, oldSize-1)) != null)
				currentGame.remove(new Coord(i, oldSize-1));
		
		for(int j = 0; j < oldSize; j++)
			if(currentGame.circuitAtPos(new Coord(oldSize-1, j)) != null)
				currentGame.remove(new Coord(oldSize-1, j));
		
		currentGame.set_size(newSize);
	}

	public static void decSize(int newSize) {
		// decrements size of board by one
		if (newSize < Game.MIN_TILES || newSize == numTiles)
			return;
		numTiles = newSize;
		calcTileSize();

		Game g = new Game(numTiles);
		// reposition pieces+Squares in scene
		update_squares();
		for (Object n : cp.getChildren().toArray())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				if (p.c.coord.i < numTiles && p.c.coord.j < numTiles) {
					p.changePos(allSquares.get(p.c.coord));
					p.onResize();
					g.add(p.c, p.c.coord);
				} else {
					cp.removePiece(p);
				}
				p.changePos(allSquares.get(p.c.coord));
			}
		
		currentGame.set_size(newSize);
	}

	public static void on_resize() {
		calcTileSize();
		cp.setPrefHeight(Gui.boardHeight);
		cp.setPrefWidth(Gui.boardWidth - Gui.SIDE_BAR_WIDTH);
		update_squares();
		
		for (Object n : cp.getChildren().toArray())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				p.changePos(allSquares.get(p.c.coord));
				p.onResize();
			}
		update_size_markers();
	}

	private static void update_squares(){
		int prev_size = (int)Math.sqrt(allSquares.size());
		int curr_size = numTiles;
		
		double init = (Gui.boardWidth - Gui.boardHeight - Gui.SIDE_BAR_WIDTH) / 2;
		double t = tileSize;
		
		for(int i = 0; i < max(prev_size, curr_size); i++)
			for(int j = 0; j < max(prev_size, curr_size); j++)
				if( 		(i < prev_size && i < curr_size) &&
							(j < prev_size && j < curr_size)){
					//needs resizing and recolo0ring
					Square s = allSquares.get(new Coord(i, j));
					s.on_resize(init);
				} else if(	(i >= prev_size && i < curr_size) ||
							(j >= prev_size && j < curr_size)){
					//needs to be made, resized and recolored					
					double x = max(0, init) + CircuitPane.GAME_MARGIN + i
							* (t + CircuitPane.GAP);
					double y = max(0, -init) + CircuitPane.GAME_MARGIN + j
							* (t + CircuitPane.GAP);
					Square s = new Square(x, y, new Coord(i, j));
					allSquares.put(new Coord(i, j), s);
					cp.getChildren().add(s);
					
				} else if(	(i < prev_size && i >= curr_size) ||
							(j < prev_size && j >= curr_size)){
					//needs to be deleted
					cp.getChildren().remove(allSquares.get(new Coord(i, j)));
					allSquares.remove(new Coord(i, j));
				}
		
		set_chevrons();
//		System.out.println("(old, new): ("+ prev_size +", "+ curr_size +")");
//		System.out.println("num squares = "+allSquares.size());
	}
	
	public static Square getClosest(double x, double y) {
		Square closest = null;
		double minDist = Double.MAX_VALUE;
		for (Square s : allSquares.values())
			if (s.euclideanDistance(x, y) < minDist && s.loc != 2) {
				closest = s;
				minDist = s.euclideanDistance(x, y);
			}
		return closest;
	}

	public static void setTrail(Coord start, Coord finish){		
		if(start.equals(finish))
			return;
		
		allSquares.values().forEach(s -> s.setColor());
		
		PathFinder gn = new PathFinder(currentGame);		
		
		List<Coord> trail = gn.getTrail(start, finish);
		
		if(trail == null){
			allSquares.get(start).square.setFill(Square.SELECTED);
			allSquares.get(finish).square.setFill(Square.SELECTED);
		} else
			trail.forEach(c-> allSquares.get(c).square.setFill(Square.SELECTED));
	}
	
	public static void add_cable_path(Coord start, Coord finish){
		if(start.equals(finish))
			return;
		
		allSquares.values().forEach(s -> s.setColor());
		
		PathFinder gn = new PathFinder(currentGame);
		
		List<Cable> path = gn.create_cable_list(start, finish);
		
		if(path == null){
			allSquares.get(start).square.setFill(Square.SELECTED);
			allSquares.get(finish).square.setFill(Square.SELECTED);
		} else
			path.forEach(c -> addPiece(new Piece(c), c.coord));
	}
	
	@Override
	public void update(Observable game, Object arg) {
		System.out.println("updated game causght in gamepane");
		if(currentLevel == null) return;
		if(currentLevel.isComplete(currentGame)){
			onLevelCompletion();
			gameWon = true;
		}
	}
	
	public static void onLevelCompletion(){
		if(gameWon) return;
		WinMessage wm = new WinMessage(currentLevel, currentGame.n);
//		Gui.gamePane.getChildren().add(wm);
//		wm.toFront();
		new_circuits.clear();
		new_circuits.addAll(currentLevel.circuitRewards);
		currentLevel.onCompletion();
	}
}

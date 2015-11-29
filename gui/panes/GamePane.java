package panes;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import controls.WinMessage;
import tutorials.Action;
import tutorials.Tute1;
import tutorials.Tutorial;
import circuits.Cable;
import circuits.Circuit;
import circuits.Coord;
import eval.Evaluator;
import abedgui.CircuitFinder;
import abedgui.Gui;
import abedgui.Piece;
import abedgui.Square;
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

public class GamePane extends Pane implements Observer {
	public final static double 			GAME_MARGIN 		= 50;
	public final static double 			GAP 				= 0;
	
	private static CircuitFinder		cf					= new CircuitFinder("");
	
	public static ObservableSet<Circuit> 	unlockedCircuits = Reader.loadUnlockedCircuits();
	public static Set<Circuit>	new_circuits	 = new HashSet<>(unlockedCircuits);
	
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
	public 	static GamePane 			gp;
	
	public GamePane() {
		this.getStylesheets().add("res/css/GamePane.css");
		this.setPrefSize(Gui.boardWidth - Gui.SIDE_BAR_WIDTH, Gui.boardHeight);
		
		gp = this;
		gp.setId("main");
		
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

	public static void addPiece(Piece p) {
		Coord coord = currentGame.nextOpen();
		if (coord != null)
			addPiece(p, coord);
	}

	public static void addPiece(Piece p, Coord coord) {
		currentGame.add(p.c, coord);
		p.setLayoutX(allSquares.get(coord).x);
		p.setLayoutY(allSquares.get(coord).y);
		gp.getChildren().add(p);
	}

	public static void rotatePiece(Coord coord, int rot) {
		currentGame.rotate(coord, rot);
	}

	public static void movePiece(Piece p, Coord coord) {
		if (currentGame.circuitAtPos(coord) == null
				&& allSquares.get(coord).isSide != 2) {
			currentGame.remove(p.c.coord);
			currentGame.add(p.c, coord);
			p.setLayoutX(allSquares.get(coord).x);
			p.setLayoutY(allSquares.get(coord).y);
		} else {
			p.setLayoutX(allSquares.get(p.c.coord).x);
			p.setLayoutY(allSquares.get(p.c.coord).y);
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
		SideBarPane.textArea.setText(l.goalText);
		
		if(Tutorial.ALL_TUTORIALS.get(l.name) != null){
			tutorial = Tutorial.ALL_TUTORIALS.get(l.name);
			tutorial.start();
		} else {
			if(tutorial != null)
				tutorial.end();
			tutorial = null;
		}
			
			
	}
	
	public static void newGame(Game g) {
		// loads new game into the gui and sets current game
		currentGame = g;
		currentGame.addObserver(gp);
		currentGame.notifyObservers(Action.NEW);

		numTiles = g.n;
		calcTileSize();
		gp.getChildren().clear();
		gameWon = false;
		
		allSquares = new HashMap<>();
		SideBarPane.inc.set(numTiles);

		for (int j = 0; j < numTiles; j++)
			for (int i = 0; i < numTiles; i++) {
				allSquares.put(new Coord(i, j), new Square(i, j));
				gp.getChildren().add(allSquares.get(new Coord(i, j)));
			}

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
			gp.getChildren().add(side_markers[i]);
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
		Map<Coord, Square> temp = new HashMap<>();

		// reposition/create new squares in array
		for (int i = 0; i < numTiles; i++)
			for (int j = 0; j < numTiles; j++){
				Coord pos = new Coord(i, j);
				if (i >= oldSize || j >= oldSize) {
					temp.put(pos, new Square(i, j));
					gp.getChildren().add(temp.get(pos));
				} else {
					temp.put(pos, allSquares.get(pos));
					temp.get(pos).initialise();
				}
			}
		allSquares = temp;

		// reposition pieces+Squares in scene
		for (Node n : gp.getChildren())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				p.changePos(allSquares.get(p.c.coord));
				p.onResize();
			} else if (n instanceof Square) {
				Square s = (Square) n;
				s.initialise();
			}

		Game g = new Game(numTiles);
		for (int i = 0; i < oldSize; i++)
			for (int j = 0; j < oldSize; j++) {
				Circuit c = currentGame.circuitAtPos(new Coord(i, j));
				if (c != null)
					g.add(c, c.coord);
			}
		
		currentGame = g;
		g.addObserver(gp);
	}

	public static void decSize(int newSize) {
		// decrements size of board by one
		if (newSize < Game.MIN_TILES || newSize == numTiles)
			return;
		numTiles = newSize;
		calcTileSize();
		Map<Coord, Square> temp = new HashMap<>();

		Game g = new Game(numTiles);
		// reposition pieces+Squares in scene
		for (Object n : gp.getChildren().toArray())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				if (p.c.coord.i < numTiles && p.c.coord.j < numTiles) {
					p.changePos(allSquares.get(p.c.coord));
					p.onResize();
					g.add(p.c, p.c.coord);
				} else {
					gp.removePiece(p);
				}
				p.changePos(allSquares.get(p.c.coord));
			} else if (n instanceof Square) {
				Square s = (Square) n;
				if (s.coord.i < numTiles && s.coord.j < numTiles) {
					temp.put(s.coord, allSquares.get(s.coord));
					temp.get(s.coord).initialise();

				} else {
					gp.getChildren().remove(s);
				}

				s.initialise();
			}
		
		allSquares = temp;
		currentGame = g;
		g.addObserver(gp);
	}

	public static void resizeHeight() {
		calcTileSize();
		gp.setPrefHeight(Gui.boardHeight);
		for (Object n : gp.getChildren().toArray())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				p.changePos(allSquares.get(p.c.coord));
				p.onResize();
			} else if (n instanceof Square) {
				Square s = (Square) n;
				s.initialise();
				allSquares.put(s.coord, s);
			}
		update_size_markers();
	}

	public static void resizeWidth() {
		calcTileSize();
		gp.setPrefWidth(Gui.boardWidth - Gui.SIDE_BAR_WIDTH);
		for (Object n : gp.getChildren().toArray())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				p.changePos(allSquares.get(p.c.coord));
				p.onResize();
			} else if (n instanceof Square) {
				Square s = (Square) n;
				s.initialise();
			}
		update_size_markers();
	}

	public static Square getClosest(double x, double y) {
		Square closest = null;
		double minDist = Double.MAX_VALUE;
		for (Square s : allSquares.values())
			if (s.euclideanDistance(x, y) < minDist && s.isSide != 2) {
				closest = s;
				minDist = s.euclideanDistance(x, y);
			}
		return closest;
	}

	public static void setTrail(Coord start, Coord finish){		
		if(start.equals(finish))
			return;
		
		allSquares.values().forEach(s -> s.revertColor());
		
		PathFinder gn = new PathFinder(currentGame);		
		
		List<Coord> trail = gn.getTrail(start, finish);
		
		if(trail == null){
			allSquares.get(start).setFill(Square.SELECTED);
			allSquares.get(finish).setFill(Square.SELECTED);
		} else
			trail.forEach(c-> allSquares.get(c).setFill(Square.SELECTED));
	}
	
	public static void add_cable_path(Coord start, Coord finish){
		if(start.equals(finish))
			return;
		
		allSquares.values().forEach(s -> s.revertColor());
		
		PathFinder gn = new PathFinder(currentGame);
		
		List<Cable> path = gn.create_cable_list(start, finish);
		
		if(path == null){
			allSquares.get(start).setFill(Square.SELECTED);
			allSquares.get(finish).setFill(Square.SELECTED);
		} else
			path.forEach(c -> addPiece(new Piece(c), c.coord));
	}
	
	@Override
	public void update(Observable game, Object arg) {
		System.out.println("updated game causght in gamepane");
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

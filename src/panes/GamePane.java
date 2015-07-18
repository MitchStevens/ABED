package panes;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import abedgui.Gui;
import abedgui.Level;
import abedgui.Piece;
import abedgui.Square;
import abedgui.WinMessage;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Circuit;
import logic.Evaluator;
import logic.Game;
import logic.Reader;
import static java.lang.Math.*;

public class GamePane extends Pane implements Observer {
	public final static double GAME_MARGIN = 50;
	public final static double GAP = 0;
	
	public static double tileSize = 0;
	public static int numTiles = 3;
	public static Game currentGame;
	public static Square[][] allSquares;

	public static Level currentLevel;
	private static boolean gameWon = false;

	public static GamePane gp;

	public GamePane() {
		this.getStylesheets().add("res/css/GamePane.css");
		this.setPrefSize(Gui.boardWidth - Gui.SIDE_BAR_WIDTH, Gui.boardHeight);

		gp = this;
		gp.setId("main");
	}

	public static void calcTileSize() {
		tileSize = (min(Gui.boardHeight, Gui.boardWidth - Gui.SIDE_BAR_WIDTH)
				- 2 * GAME_MARGIN - (numTiles - 1) * GAP) / numTiles;
	}

	public static void addPiece(Piece p) {
		Square s = currentGame.nextOpen();
		if (s != null)
			addPiece(p, s.i, s.j);
	}

	public static void addPiece(Piece p, Integer i, Integer j) {
		p.setId("#" + p.c.pos());
		currentGame.add(p.c, i, j);
		p.setLayoutX(allSquares[i][j].x);
		p.setLayoutY(allSquares[i][j].y);
		gp.getChildren().add(p);
		updateBoard();
	}

	public static void rotatePiece(int i, int j, int rot) {
		currentGame.rotate(i, j, rot);
		updateBoard();
	}

	public static void movePiece(Piece p, int i, int j) {
		if (currentGame.circuitAtPos(i, j) == null
				&& allSquares[i][j].isSide != 2) {
			currentGame.remove(p.c.i, p.c.j);
			currentGame.add(p.c, i, j);
			p.setLayoutX(allSquares[i][j].x);
			p.setLayoutY(allSquares[i][j].y);
			updateBoard();
		} else {
			p.setLayoutX(allSquares[p.c.i][p.c.j].x);
			p.setLayoutY(allSquares[p.c.i][p.c.j].y);
		}
	}

	public void removePiece(Piece p) {
		currentGame.remove(p.c.i, p.c.j);
		this.getChildren().remove(p);
		updateBoard();
	}

	public static void togglePiece(Piece p) {
		currentGame.toggle(p.c.i, p.c.j);
		updateBoard();
	}

	public static void setLevel(Level l) {
		currentLevel = l;
		newGame(new Game(l.gameSize));
		SideBarPane.textArea.setText(l.goalText);
	}

	public static void updateBoard() {
		for (Node n : gp.getChildren())
			if (n instanceof Piece)
				((Piece) n).updateImage();
	}

	public static void newGame(Game g) {
		// loads new game into the gui and sets current game
		currentGame = g;
		g.addObserver(gp);
		numTiles = g.n;
		calcTileSize();
		gp.getChildren().clear();
		gameWon = false;
		
		allSquares = new Square[numTiles][numTiles];
		SideBarPane.inc.set(numTiles);

		for (int j = 0; j < numTiles; j++)
			for (int i = 0; i < numTiles; i++) {
				allSquares[i][j] = new Square(i, j);
				gp.getChildren().add(allSquares[i][j]);
			}

		Circuit c = null;
		for (int j = 0; j < numTiles; j++)
			for (int i = 0; i < numTiles; i++)
				if ((c = currentGame.circuitAtPos(i, j)) != null) {
					Piece p = new Piece(c);
					addPiece(p, c.i, c.j);
				}
	}

	public static void incSize(int newSize) {
		// increments size of board
		if (newSize > Game.MAX_TILES || newSize == numTiles)
			return;
		int oldSize = numTiles;
		numTiles = newSize;
		calcTileSize();
		Square[][] temp = new Square[numTiles][numTiles];

		// reposition/create new squares in array
		for (int i = 0; i < numTiles; i++)
			for (int j = 0; j < numTiles; j++)
				if (i >= oldSize || j >= oldSize) {
					temp[i][j] = new Square(i, j);
					gp.getChildren().add(temp[i][j]);
				} else {
					temp[i][j] = allSquares[i][j];
					temp[i][j].initialise();
				}
		allSquares = temp;

		// reposition pieces+Squares in scene
		for (Node n : gp.getChildren())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				p.changePos(allSquares[p.c.i][p.c.j]);
				p.onResize();
			} else if (n instanceof Square) {
				Square s = (Square) n;
				s.initialise();
			}

		Game g = new Game(numTiles);
		for (int i = 0; i < oldSize; i++)
			for (int j = 0; j < oldSize; j++) {
				Circuit c = currentGame.circuitAtPos(i, j);
				if (c != null)
					g.add(c, c.i, c.j);
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
		Square[][] temp = new Square[numTiles][numTiles];

		Game g = new Game(numTiles);
		// reposition pieces+Squares in scene
		for (Object n : gp.getChildren().toArray())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				if (p.c.i < numTiles && p.c.j < numTiles) {
					p.changePos(allSquares[p.c.i][p.c.j]);
					p.onResize();
					g.add(p.c, p.c.i, p.c.j);
				} else {
					gp.removePiece(p);
				}
				p.changePos(allSquares[p.c.i][p.c.j]);
			} else if (n instanceof Square) {
				Square s = (Square) n;
				if (s.i < numTiles && s.j < numTiles) {
					temp[s.i][s.j] = allSquares[s.i][s.j];
					temp[s.i][s.j].initialise();

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
				p.changePos(allSquares[p.c.i][p.c.j]);
				p.onResize();
			} else if (n instanceof Square) {
				Square s = (Square) n;
				s.initialise();
				allSquares[s.i][s.j] = s;
			}
	}

	public static void resizeWidth() {
		calcTileSize();
		gp.setPrefWidth(Gui.boardWidth - Gui.SIDE_BAR_WIDTH);
		for (Object n : gp.getChildren().toArray())
			if (n instanceof Piece) {
				Piece p = (Piece) n;
				p.changePos(allSquares[p.c.i][p.c.j]);
				p.onResize();
			} else if (n instanceof Square) {
				Square s = (Square) n;
				s.initialise();
			}
	}

	public static Square getClosest(double x, double y) {
		Square closest = null;
		double minDist = Double.MAX_VALUE;
		for (Square[] sArray : allSquares)
			for (Square s : sArray)
				if (s.distance(x, y) < minDist && s.isSide != 2) {
					closest = s;
					minDist = s.distance(x, y);
				}
		return closest;
	}


	@Override
	public void update(Observable game, Object arg) {
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
		currentLevel.onCompletion();
	}
}

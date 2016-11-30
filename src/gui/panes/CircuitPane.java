package gui.panes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import actions.Action;
import actions.PieceAction;
import core.eval.Function;
import core.eval.Operation;
import core.game.Coord;
import core.game.Direction;
import core.game.Game;
import core.logic.Level;
import core.logic.PathFinder;
import tutorials.Tutorial;
import data.Reader;
import gui.graphics.NodePiece;
import gui.graphics.Square;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static java.lang.Math.*;

public class CircuitPane extends Pane implements Observer{
	public final static double 			GAME_MARGIN 	= 50;
	public final static double 			GAP 			= 0;
	
	public 	static double 				tile_size 		= 0;
	public	static Game 				game;
	public	static int					num_tiles		= 3;
	public	static Map<Coord, Square> 	squares = new HashMap<>();
	private static Map<Coord, NodePiece> node_pieces = new HashMap<>();
	//private static Map<Coord, EdgePiece> edge_pieces = new HashMap<>();	
	public	static Text[]				side_markers	= new Text[4];
	//public	static Tutorial				tutorial;
	
//	private static boolean 				gameWon 		= false;
//	public	static Coord 				lastClicked 	= null;
	public 	static CircuitPane 			cp;
	
	public CircuitPane() {
		this.getStylesheets().add("res/css/GamePane.css");		
		cp = this;
		cp.prefHeightProperty().bind(Gui.BOARD_HEIGHT);
		cp.prefHeightProperty().addListener( (num) -> { update(); } );
		cp.prefWidthProperty().bind(Gui.BOARD_WIDTH.subtract(Gui.SIDE_BAR_WIDTH));
		cp.prefWidthProperty().addListener( (num) -> { update(); } );
		cp.setId("main");
	}
	
	private static void update(){
		update_size_markers();
		update_squares();
	}
	
	public static void calc_tile_size() {
		double short_edge = min(cp.getPrefHeight(), cp.getPrefWidth());
		tile_size = (short_edge - 2*GAME_MARGIN - (num_tiles-1)*GAP) / num_tiles;
	}
	
	public static void add_piece(Operation op) {
		Coord coord = game.next_open();
		if (coord != null)
			add_piece(op, coord);
	}

	public static void add_piece(Operation op, Coord c) {
		game.add(op, c);
		NodePiece p = new NodePiece((String)game.twin_map.equiv(c));
		node_pieces.put(c, p);
		p.change_pos(squares.get(c));
		cp.getChildren().add(p);
		Action a = new PieceAction(PieceAction.Type.ADD, op, c);
	}

	public static void rotate_piece(Coord coord, int rot) {
		game.rotate(coord, rot);
		Action a = new PieceAction(PieceAction.Type.ROTATE, game.get_op(coord), coord, Direction.create(rot));
	}

	public static void move_piece(Coord from, Coord to) {
		NodePiece p = node_pieces.get(from);
		if (!game.twin_map.containsKey(to)
				&& squares.get(to).get_loc() != Square.Location.CORNER) {
			
			game.move(from, to);
			node_pieces.remove(from);
			node_pieces.put(to, p);
			p.change_pos(squares.get(to));
			Action a = new PieceAction(PieceAction.Type.MOVE, game.get_op(to), from, to);
		} else {
			p.change_pos(squares.get(from));
		}
	}

	public static void remove_piece(NodePiece p) {
		Coord pos = p.get_pos();
		String name = game.get_op(pos).get_name();
		game.remove(pos);
		cp.getChildren().remove(p);
		Action a = new PieceAction(PieceAction.Type.REMOVE, name, pos);
	}
	
	public static void toggle_piece(NodePiece p){
		game.toggle(p.get_pos());
		Action a = new PieceAction(PieceAction.Type.TOGGLE, game.get_op(p.get_pos()), p.get_pos());
	}
	
//	private static void set_chevrons(){
//		if(currentLevel == null) return;
//		
//		for(int side = 0; side < 4; side++){
//			if(currentLevel.objective.buses.get(side) == null)
//				continue;
//			else if(currentLevel.objective.buses.get(side) instanceof BusIn)
//				set_chevrons_on_side(side, Gate.mod4(side - 2));
//			else if(currentLevel.objective.buses.get(side) instanceof BusOut)
//				set_chevrons_on_side(side, side);
//		}
//	}
	
	private static void set_chevrons_on_side(Direction side, Direction dir){
		for(Coord c : Coord.over_side(num_tiles, side))
			squares.get(c).set_chevron(dir);
	}
	
	public static void new_game(Game g) {
		// loads new game into the gui and sets current game
		game = g;
		game.addObserver(cp);
		//current_game.notifyObservers(Action.NEW);

		num_tiles = g.get_size();
		cp.getChildren().clear();
		//gameWon = false;
		
		SideBarGame.inc.set(num_tiles);
		
		init_squares();

//		Coord c;
//		for (int j = 0; j < num_tiles; j++)
//			for (int i = 0; i < num_tiles; i++)		
//				if ((c = new Coord(i, j)) != null) {
//					Piece p = new Piece((String)game.twin_map.equiv(c), c);
//				}
		
		for(Direction d : Direction.values()){
			side_markers[d.value] = new Text(""+d.value);
			side_markers[d.value].setFont(Reader.loadFont("adbxtra.ttf", 50));
			side_markers[d.value].setFill(Color.WHITE);
			cp.getChildren().add(side_markers[d.value]);
		}
		
		update();
	}

	public static void update_size_markers(){
		double letter = 30.0;
		double h = cp.getPrefHeight();
		double w = cp.getPrefWidth();
		double init = (cp.getPrefHeight() - cp.getPrefWidth()) / 2.0;

		side_markers[0].setLayoutX(w/2 - letter/2);
		side_markers[0].setLayoutY(letter + max(0, init));
		
		side_markers[1].setLayoutX(w - letter);//
		side_markers[1].setLayoutY(h/2 + letter/2);//
		
		side_markers[2].setLayoutX(w/2 - letter/2);//
		side_markers[2].setLayoutY(h - max(0, init));//
		
		side_markers[3].setLayoutX(0);//
		side_markers[3].setLayoutY(h/2 + 15);//
	}
	
	public void set_num_tiles(int new_size){
		if(new_size > Game.MAX_TILES || new_size < Game.MIN_TILES)
			return;
		
		if(new_size > num_tiles){
			add_squares(new_size);
			inc_size(new_size);
		}
		else if(new_size < num_tiles){
			remove_squares(new_size);
			dec_size(new_size);
		}
		else
			return;
	}
	
	public static void inc_size(int size) {
		// increments size of board
		if (size > Game.MAX_TILES || size == num_tiles)
			return;
		
		int oldSize = num_tiles;
		num_tiles = size;

		// reposition pieces in scene
		for(NodePiece p : node_pieces.values()){
			p.change_pos(squares.get(p.get_pos()));
			p.on_resize();
		}
		
		//delete circuits on edges from game
		for(int i = 0; i < oldSize; i++)
			if(game.get_op(new Coord(i, oldSize-1)) != null)
				game.remove(new Coord(i, oldSize-1));
		
		for(int j = 0; j < oldSize; j++)
			if(game.get_op(new Coord(oldSize-1, j)) != null)
				game.remove(new Coord(oldSize-1, j));
		
		game.set_size(size);
	}

	public static void dec_size(int newSize) {
		// decrements size of board by one
		if (newSize < Game.MIN_TILES || newSize == num_tiles)
			return;
		num_tiles = newSize;

		// reposition pieces+Squares in scene
		update_squares();
		for(NodePiece p : node_pieces.values()){
			if(p.get_pos().is_within(newSize)){
				p.change_pos(squares.get(p.get_pos()));
			} else {
				game.remove(p.get_pos());
				cp.remove_piece(p);
			}
		}
		
		game.set_size(newSize);
	}

	public static void on_resize() {
		update_squares();
		
		for (Object n : cp.getChildren().toArray())
			if (n instanceof NodePiece) {
				NodePiece p = (NodePiece) n;
				p.change_pos(squares.get(p.get_pos()));
				p.on_resize();
			}
		update_size_markers();
	}

	private static void init_squares(){
		for(Coord c : Coord.over_square(num_tiles)){
			Square s = new Square(0, 0, c);
			squares.put(c, s);
			cp.getChildren().add(s);
		}		
	}
	
	private static void add_squares(int new_size){
		int old_size = num_tiles;
		
		for(int i = old_size; i < new_size; i++)
			for(int j = 0; j < old_size; j++){
				add_square(new Coord(i, j));
				add_square(new Coord(j, i));
			}
				
		for(int i = old_size; i < new_size; i++)
			for(int j = old_size; j < new_size; j++)
				add_square(new Coord(i, j));
		
		num_tiles = new_size;
		game.set_size(new_size);
		update_squares();
	}
	
	private static void add_square(Coord c){
		Square s = new Square(0, 0, c);
		squares.put(c, s);
		cp.getChildren().add(s);
	}
	
	private static void remove_squares(int new_size){
		int old_size = num_tiles;
		
		for(int i = new_size; i < old_size; i++)
			for(int j = 0; j < old_size; j++){
				remove_square(new Coord(i, j));
				remove_square(new Coord(j, i));
			}
				
		for(int i = new_size; i < old_size; i++)
			for(int j = new_size; j < old_size; j++)
				remove_square(new Coord(i, j));
		
		num_tiles = new_size;
		game.set_size(new_size);
		update_squares();
	}
	
	private static void remove_square(Coord c){
		Square s = squares.remove(c);
		cp.getChildren().remove(s);
	}
	
	/**
	 * Places and changes size of squares. Recolors if necessary.
	 * */
	private static void update_squares(){
		calc_tile_size();
		double size = tile_size + GAP;
		double init = (cp.getPrefWidth() - cp.getPrefHeight()) / 2.0;
		
		double x, y;
		for(Coord c : Coord.over_square(num_tiles)){
			Square s = squares.get(c);
			x = max(0, init)  + GAME_MARGIN + c.j*size;
			y = max(0, -init) + GAME_MARGIN + c.i*size;
			s.setLayoutX(x);
			s.setLayoutY(y);
			s.set_size(tile_size);
			s.re_color(num_tiles);
		}
		
		//set_chevrons();
//		System.out.println("(old, new): ("+ prev_size +", "+ curr_size +")");
//		System.out.println("num squares = "+allSquares.size());
	}
	
	public static Square get_closest(double x, double y) {
		Square closest = null;
		double minDist = Double.MAX_VALUE;
		for (Square s : squares.values())
			if (s.euc_dist(x, y) < minDist && s.get_loc() != Square.Location.CORNER) {
				closest = s;
				minDist = s.euc_dist(x, y);
			}
		return closest;
	}

//	public static void setTrail(Coord start, Coord finish){		
//		if(start.equals(finish))
//			return;
//		
//		squares.values().forEach(s -> s.setColor());
//		
//		PathFinder gn = new PathFinder(currentGame);		
//		
//		List<Coord> trail = gn.getTrail(start, finish);
//		
//		if(trail == null){
//			squares.get(start).square.setFill(Square.SELECTED);
//			squares.get(finish).square.setFill(Square.SELECTED);
//		} else
//			trail.forEach(c-> squares.get(c).square.setFill(Square.SELECTED));
//	}
	
//	public static void add_cable_path(Coord start, Coord finish){
//		if(start.equals(finish))
//			return;
//		
//		squares.values().forEach(s -> s.setColor());
//		
//		PathFinder gn = new PathFinder(currentGame);
//		
//		List<Cable> path = gn.create_cable_list(start, finish);
//		
//		if(path == null){
//			squares.get(start).square.setFill(Square.SELECTED);
//			squares.get(finish).square.setFill(Square.SELECTED);
//		} else
//			path.forEach(c -> addPiece(new Piece(c), c.coord));
//	}
	
	@Override
	public void update(Observable game, Object arg) {
//		if(currentLevel == null) return;
//		if(currentLevel.isComplete(currentGame)){
//			onLevelCompletion();
//			gameWon = true;
//		}
	}
	
//	public static void onLevelCompletion(){
//		if(gameWon) return;
//		((WinPane)Gui.screens.get("win_pane")).set_level(currentLevel, currentGame.n);
//		Gui.set_pane("win_pane");
////		Gui.gamePane.getChildren().add(wm);
////		wm.toFront();
//		Reader.new_circuits.clear();
//		Reader.new_circuits.addAll(currentLevel.circuitRewards);
//		currentLevel.onCompletion();
//	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.graphics;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import static java.lang.Math.max;

import core.Utilities;
import core.game.Coord;
import core.game.Direction;
import gui.panes.CircuitPane;

/**
 * @author Mitch
 */
public class Square extends Region{
	public static enum Location {CENTER, SIDE, CORNER}
	
	public final static Color 	DEFAULT		= Color.web("#DDFEFE");
	public final static Color 	SIDE		= Color.web("#9FFCFC");
	public final static Color 	CORNER		= Color.ANTIQUEWHITE;
	public final static Color 	SELECTED	= Color.RED;
	public final static Color	SIDE_ALT	= Color.web("#BFFFFF");
	
	private	Rectangle	square = new Rectangle();
	private	Polygon 	chevron;
	
	private Location loc; // 0 = def, 1 = side, 2 = null
	public 	Coord 	 coord;

	public Square(double x, double y, Coord c) {
		coord = c;
		this.setLayoutX(x);
		this.setLayoutY(y);
		initialise();
	}
	
	public void initialise() {
		double t = CircuitPane.tile_size;
		
		square.setHeight(t);
		square.setWidth(t);
		square.setStroke(Color.LIGHTGRAY);
		square.setStrokeWidth(1);
		
		this.getChildren().add(square);
		
		loc = calc_side(CircuitPane.num_tiles);
		
//		this.setOnMouseClicked(e -> {
//			if(CircuitPane.lastClicked == null){
//				CircuitPane.lastClicked = this.coord;
//				square.setFill(SELECTED);
//			}else{
//				if(this.coord == CircuitPane.lastClicked){
//					CircuitPane.squares.values().forEach(s -> s.setColor());
//					return;
//				}else{
//					CircuitPane.add_cable_path(CircuitPane.lastClicked, this.coord);
//					CircuitPane.lastClicked = null;
//					CircuitPane.squares.values().forEach(s -> s.setColor());
//					return;
//				}
//			}
//		});
		
//		this.setOnMouseEntered(e -> {
//			if(CircuitPane.lastClicked != null)
//				CircuitPane.setTrail(CircuitPane.lastClicked, this.coord);
//		});
	}
	
	/*public void on_resize(){
		double init = (CircuitPane.cp.getPrefWidth() - CircuitPane.cp.getPrefHeight()) / 2.0;
		double t = CircuitPane.tile_size;
		
		double x = max(0, init) + CircuitPane.GAME_MARGIN + coord.i
				* (t + CircuitPane.GAP);
		double y = max(0, -init) + CircuitPane.GAME_MARGIN + coord.j
				* (t + CircuitPane.GAP);
		this.setLayoutX(x);
		this.setLayoutY(y);
		
		loc = calc_side(CircuitPane.num_tiles);
		setColor();		
		
		square.setHeight(t);
		square.setWidth(t);
	}*/
	
	public Location get_loc(){
		return loc;
	}
	
	public void set_size(double w){
		square.setWidth(w);
		square.setHeight(w);
	}
	
	public void re_color(int n){
		loc = calc_side(n);
		set_color();
	}
	
	public Location calc_side(int n){
		//re calculates what what side the square is on.
		int num = 0;
		if(coord.i == 0 || coord.i == n -1) num++;
		if(coord.j == 0 || coord.j == n -1) num++;
		
		return Location.values()[num];
	}
	
	public void set_chevron(Direction dir){
		remove_chevron();
		
		double t = CircuitPane.tile_size;
		chevron = new Polygon(new double[]{
				0, 		t,
				0, 		t/2,
				t/2,	0,
				t,	 	t/2,
				t, 		t,
				t/2, 	t/2
			});
		chevron.setFill(SIDE_ALT);
		
		chevron.setRotate(90*dir.value);
		this.getChildren().add(chevron);
	}
	
	public void remove_chevron(){
		this.getChildren().remove(chevron);
	}
	
	public void set_color(){
		//set color to whatever it should be (based on side)
		switch(loc){
		case CENTER: 	square.setFill(DEFAULT);			break;
		case SIDE: 		square.setFill(SIDE);				break;
		case CORNER: 	square.setFill(CORNER);				break;
		default: 		square.setFill(Color.TRANSPARENT); 	break;
		}
	}

	// gets euclidean distance
	public double euc_dist(double x, double y) {
		x -= this.getLayoutX();
		y -= this.getLayoutY();
		return Math.sqrt(x*x + y*y);
	}
	
}

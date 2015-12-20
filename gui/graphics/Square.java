/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import circuits.Circuit;
import circuits.Coord;
import panes.CircuitPane;
import panes.Gui;
import javafx.animation.FillTransition;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import static java.lang.Math.max;

/**
 * @author Mitch
 */
public class Square extends Region{
	public final static Color 	DEFAULT		= Color.web("#DDFEFE");
	public final static Color 	SIDE		= Color.web("#9FFCFC");
	public final static Color 	CORNER		= Color.web("#000000");
	public final static Color 	SELECTED	= Color.RED;
	public final static Color	SIDE_ALT	= Color.web("#BFFFFF");
	
	public 				Rectangle	square = new Rectangle();
	public 		 		Polygon chevron;
	
	public 				int loc 		= 0; // 0 = def, 1 = side, 2 = null
	public 				Coord 	coord;

	public Square(double x, double y, Coord c) {
		coord = c;
		this.setLayoutX(x);
		this.setLayoutY(y);
		initialise();
	}
	
	public void initialise() {
		double t = CircuitPane.tileSize;
		
		square.setHeight(t);
		square.setWidth(t);
		square.setStroke(Color.LIGHTGRAY);
		square.setStrokeWidth(1);
		
		this.getChildren().add(square);
		
		loc = calc_side(CircuitPane.numTiles);
		
		setColor();
		
		this.setOnMouseClicked(e -> {
			if(CircuitPane.lastClicked == null){
				CircuitPane.lastClicked = this.coord;
				square.setFill(SELECTED);
			}else{
				if(this.coord == CircuitPane.lastClicked){
					CircuitPane.allSquares.values().forEach(s -> s.setColor());
					return;
				}else{
					CircuitPane.add_cable_path(CircuitPane.lastClicked, this.coord);
					CircuitPane.lastClicked = null;
					CircuitPane.allSquares.values().forEach(s -> s.setColor());
					return;
				}
			}
		});
		
		this.setOnMouseEntered(e -> {
			if(CircuitPane.lastClicked != null)
				CircuitPane.setTrail(CircuitPane.lastClicked, this.coord);
		});
	}
	
	public void on_resize(double init){
		double t = CircuitPane.tileSize;
		
		double x = max(0, init) + CircuitPane.GAME_MARGIN + coord.i
				* (t + CircuitPane.GAP);
		double y = max(0, -init) + CircuitPane.GAME_MARGIN + coord.j
				* (t + CircuitPane.GAP);
		this.setLayoutX(x);
		this.setLayoutY(y);
		
		loc = calc_side(CircuitPane.numTiles);
		setColor();		
		
		square.setHeight(t);
		square.setWidth(t);
	}
	
	public int calc_side(int n){
		//re calculates what what side the square is on.
		int num = 0;
		if(coord.i == 0 || coord.i == n -1) num++;
		if(coord.j == 0 || coord.j == n -1) num++;
		
		if(num != 1) remove_chevron();
		
		return num;
	}
	
	public void set_chevron(int dir){
		remove_chevron();
		
		dir = Circuit.mod4(dir);
		
		double t = CircuitPane.tileSize;
		chevron = new Polygon(new double[]{
				0, 		t,
				0, 		t/2,
				t/2,	0,
				t,	 	t/2,
				t, 		t,
				t/2, 	t/2
			});
		chevron.setFill(SIDE_ALT);
		
		chevron.setRotate(90*dir);
		this.getChildren().add(chevron);
	}
	
	public void remove_chevron(){
		this.getChildren().remove(chevron);
	}
	
	public void setColor(){
		//set color to whatever it should be (based on side)
		switch(loc){
		case 0: 	square.setFill(DEFAULT);			break;
		case 1: 	square.setFill(SIDE);				break;
		case 2: 	square.setFill(CORNER);				break;
		default: 	square.setFill(Color.TRANSPARENT); 	break;
		}
	}

	// gets euclidean distance
	public double euclideanDistance(double x, double y) {
		x -= this.getLayoutX();
		y -= this.getLayoutY();
		return Math.sqrt(x * x + y * y);
	}
	
}

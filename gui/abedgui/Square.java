/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abedgui;

import circuits.Coord;
import panes.GamePane;
import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import static java.lang.Math.max;

/**
 * @author Mitch
 */
public class Square extends Rectangle{
	public final static Color 	DEFAULT		= Color.web("#DDFEFE");
	public final static Color 	SIDE		= Color.web("#9FFCFC");
	public final static Color 	CORNER		= Color.web("#000000");
	public final static Color 	SELECTED	= Color.RED;
	
	public 				int 	isSide 		= 0; // 0 = def, 1 = side, 2 = null
	public 				double 	x;
	public 				double 	y;
	public 				Coord 	coord;

	public Square(int i, int j) {
		coord = new Coord(i, j);
		initialise();
	}
	
	public void initialise() {
		double init = (Gui.boardWidth - Gui.boardHeight - Gui.SIDE_BAR_WIDTH) / 2;
		this.x = max(0, init) + GamePane.GAME_MARGIN + coord.i
				* (GamePane.tileSize + GamePane.GAP);
		this.y = max(0, -init) + GamePane.GAME_MARGIN + coord.j
				* (GamePane.tileSize + GamePane.GAP);
		// double size = ABEDGUI.getBoard().tileSize;
		this.setHeight(GamePane.tileSize);
		this.setWidth(GamePane.tileSize);
		this.setStroke(Color.LIGHTGRAY);
		// this.setStrokeWidth(1);
		this.setLayoutX(x);
		this.setLayoutY(y);
		isSide = 0;
		if (coord.i == 0 || coord.i == GamePane.numTiles - 1)
			isSide++;
		if (coord.j == 0 || coord.j == GamePane.numTiles - 1)
			isSide++;
		revertColor();
		
		this.setOnMouseClicked(e -> {
			if(GamePane.lastClicked == null){
				GamePane.lastClicked = this.coord;
				this.setFill(SELECTED);
			}else{
				if(this.coord == GamePane.lastClicked){
					GamePane.allSquares.values().forEach(s -> s.revertColor());
					return;
				}else{
					GamePane.add_cable_path(GamePane.lastClicked, this.coord);
					GamePane.lastClicked = null;
					GamePane.allSquares.values().forEach(s -> s.revertColor());
					return;
				}
			}
		});
		
		this.setOnMouseEntered(e -> {
			if(GamePane.lastClicked != null)
				GamePane.setTrail(GamePane.lastClicked, this.coord);
		});
	}
	
	public void revertColor(){
		//set color to whatever it should be (based on side)
		switch(isSide){
		case 0: this.setFill(DEFAULT);				break;
		case 1: this.setFill(SIDE);					break;
		case 2: this.setFill(CORNER);				break;
		default: this.setFill(Color.TRANSPARENT); 	break;
		}
	}

	// gets euclidean distance
	public double euclideanDistance(double x, double y) {
		x -= this.x;
		y -= this.y;
		return Math.sqrt(x * x + y * y);
	}
}

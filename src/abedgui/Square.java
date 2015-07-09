/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abedgui;

import panes.GamePane;
import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * @author Mitch
 */
public class Square extends Rectangle{
    public int isSide = 0; //0 = def, 1 = side, 2 = null
    public double x;
    public double y;
    public Integer i;
    public Integer j;
	
    // this constructor used as a tuple. Some people like passing a raw int array or something
    // but I feel that this is a more civilized way of going about the same task. In Haskell sometimes
    // you create new data types that are basically wrappers for primitives and call them something 
    // descriptive like "Temperature" or "Weight". I guess we've got class variables for that sort of
    // thing, plus in Haskell you can create a data type in a line. Not sure if you could write a
    // wrapper in less then a dozen lines or so in java.
    
    public Square(int i, int j) {
        this.i = i;
        this.j = j;
        initialise();
    }
    
    public void initialise(){
        this.x = GamePane.GAME_MARGIN + i*(GamePane.tileSize + GamePane.GAP);
        this.y = GamePane.GAME_MARGIN + j*(GamePane.tileSize + GamePane.GAP);
        //double size = ABEDGUI.getBoard().tileSize;
        this.setHeight(GamePane.tileSize);
        this.setWidth(GamePane.tileSize);
        this.setStroke(Color.LIGHTGREY);
        //this.setStrokeWidth(1);
        this.setLayoutX(x);
        this.setLayoutY(y);
        isSide = 0;
		if(i == 0 || i == GamePane.numTiles-1) isSide++;
		if(j == 0 || j == GamePane.numTiles-1) isSide++;
		this.setId("square"+isSide);
    }
    
    //gets euclidean distance
    public double distance(double x, double y){
		x -= this.x;
		y -= this.y;
		return Math.sqrt(x*x + y*y);
    }
	
}

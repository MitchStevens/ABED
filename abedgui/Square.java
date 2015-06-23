/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abedgui;

import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * @author Mitch
 */
public class Square extends Rectangle{
    public static Color defColor = Color.SILVER;
    public static Color sideColor = Color.DARKGOLDENROD;
    public static Color nullColor = Color.BLACK;
    public static Color altColor = Color.WHITE;
    int isSide = 0; //0 = def, 1 = side, 2 = null
    double x;
    double y;
    public Integer i;
    public Integer j;
	
    // this constructor used as a tuple. Some people like passing a raw int array or something
    // but I feel that this is a more civilized way of going about the same task. In Haskell sometimes
    // you create new data types that are basically wrappers for primitives and call them something 
    // descriptive like "Temperature" or "Weight". I guess we've got class variables for that sort of
    // thing, plus in Haskell you can create a data type in a line. Not sure if you could write a
    // wrapper in less then a dozen lines or so in java.
    public Square(int i, int j){
    	this.i = i;
    	this.j = j;
    }
    
    Square(double x, double y, int i, int j) {
        this.x = x;
        this.y = y;
        //double size = ABEDGUI.getBoard().tileSize;
        this.setHeight(Gui.tileSize);
        this.setWidth(Gui.tileSize);
        this.setStroke(Color.LIGHTGREY);
        //this.setStrokeWidth(1);
        this.i = i;
        this.j = j;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setFill(defColor);
    }
	
    public void flash(){
    	if(isSide == 2) return;
//		this.setFill(altColor);
		FillTransition ft = new FillTransition(Duration.millis(500), this);
		ft.setFromValue(altColor);
		switch(isSide){
		case 0: ft.setToValue(defColor); break;
		case 1: ft.setToValue(sideColor); break;
		}
		ft.play();
    }
	
    public void setIsOnSide(){
    	switch(++isSide){
    	case 0: setFill(defColor); return;
    	case 1: setFill(sideColor); return;
    	case 2: setFill(nullColor); return;
    	}
    }
    
    //gets euclidean distance
    public double distance(double x, double y){
		x -= this.x;
		y -= this.y;
		return Math.sqrt(x*x + y*y);
    }
	
}
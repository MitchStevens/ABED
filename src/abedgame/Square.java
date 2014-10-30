/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abedgame;

import javafx.animation.FillTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author Mitch
 */
public class Square extends Rectangle{
    public static Color defColor = Color.SILVER;
    public static Color altColor = Color.WHITE;
    double x;
    double y;
    Integer i;
    Integer j;
	
    Square(double x, double y, int i, int j) {
        this.x = x;
        this.y = y;
        //double size = ABEDGUI.getBoard().tileSize;
        this.setHeight(abedGame.ABEDGUI.tileSize);
        this.setWidth(abedGame.ABEDGUI.tileSize);
        this.setStroke(Color.LIGHTGREY);
        //this.setStrokeWidth(1);
        this.i = i;
        this.j = j;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setFill(defColor);
		
        this.setOnMousePressed(event -> {
                //flash();
        });
    }
	
    public void flash(){
	this.setFill(altColor);
	FillTransition ft = new FillTransition(Duration.millis(1000), this);
	ft.setFromValue(altColor);
	ft.setToValue(defColor);
	ft.play();
    }
	
    //gets euclidean distance
    public double distance(double x, double y){
	x -= this.x;
	y -= this.y;
	return Math.sqrt(x*x + y*y);
    }
	
}

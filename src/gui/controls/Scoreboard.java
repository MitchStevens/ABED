package gui.controls;


import java.util.ArrayList;
import java.util.List;

import data.Reader;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class Scoreboard extends HBox {
	private static final double BOARD_GAP     = 2.0;
	private static final double SYMBOL_GAP 	  = 1.0;
	private static final double SYMBOL_WIDTH  = 8.0;
	private static final double SYMBOL_HEIGHT = 12.0;
	public  static final int	MIN_SYMS = 1;
	public  static final int	MAX_SYMS = 10;
	double unit;
	
	List<Pane> symbols = new ArrayList<>();
	VBox vbox = new VBox();
	
	String alphabet;
	String text;
	int n;
	
	Image a = Reader.get_image("scoreboard_a");
	Image b = Reader.get_image("scoreboard_b");
	Image c = Reader.get_image("scoreboard_c");
	Image d = Reader.get_image("scoreboard_d");
	Image e = Reader.get_image("scoreboard_e");
	
	public Scoreboard(double height, String alphabet, String text){
		this.unit = height/16;
		this.alphabet = alphabet;
		this.text = text;
		this.n = 1;
		
		this.minHeightProperty().set(height);
		
		init();		
	}
	
	public void add_sym(char c){
		this.getChildren().add(2*n, create_seperator());
		ScoreboardSym sym = new ScoreboardSym(c);
		this.getChildren().add(2*n +1, sym);
		n++;
	}
	
	private VBox create_seperator(){
		ImageView top = new ImageView(d);
		top.setFitHeight(unit*BOARD_GAP);
		top.setFitWidth(unit*SYMBOL_GAP);
		
		ImageView mid = new ImageView(e);
		mid.setFitHeight(unit*SYMBOL_HEIGHT);
		mid.setFitWidth(unit*SYMBOL_GAP);
		
		ImageView bot = new ImageView(d);
		bot.setFitHeight(unit*BOARD_GAP);
		bot.setFitWidth(unit*SYMBOL_GAP);
		bot.setRotate(180);

		return new VBox(top, mid, bot);
	}
	
	public void remove_sym(){
		
	}
	
	private void init(){
		VBox left_border = new VBox(), right_border = new VBox();
		
		ImageView[][] bdr = new ImageView[2][3];
		
		for(int i = 0; i < 2; i++){
			bdr[i][1] = new ImageView(c);
			bdr[i][1].setFitHeight(unit*SYMBOL_HEIGHT);
			bdr[i][1].setFitWidth(unit*BOARD_GAP);
			for(int j = 0; j < 2; j++){
				bdr[i][2*j] = new ImageView(a);
				bdr[i][2*j].setFitHeight(unit*BOARD_GAP);
				bdr[i][2*j].setFitWidth(unit*BOARD_GAP);
			}
		}
		
		bdr[0][2].setRotate(270);
		bdr[1][0].setRotate(90);
		bdr[1][1].setRotate(180);
		bdr[1][2].setRotate(180);
		
		left_border.getChildren().addAll(bdr[0]);
		right_border.getChildren().addAll(bdr[1]);
		
		this.getChildren().addAll(left_border, right_border);
		
	}
	
	private class ScoreboardSym extends VBox {
		Label upper, center, lower;
		
		public ScoreboardSym(char c){
			this.setMaxWidth(SYMBOL_WIDTH);
			ImageView[] borders = new ImageView[2];
			
			for(int i = 0; i < 2; i++){
				borders[i] = new ImageView(b);
				borders[i].setFitHeight(unit*BOARD_GAP);
				borders[i].setFitWidth(unit*SYMBOL_WIDTH);
			}
			borders[1].setRotate(180);
			
			center = new Label(c+"");
			center.setPrefHeight(unit*SYMBOL_HEIGHT);
			center.setPrefWidth(unit*SYMBOL_WIDTH);
			
			this.getChildren().addAll(borders[0], center, borders[1]);			
		}
		
	}
	
}









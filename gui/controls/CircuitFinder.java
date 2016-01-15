package controls;

import java.util.ArrayList;
import java.util.List;

import circuits.Circuit;
import data.Reader;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CircuitFinder extends VBox {
	private static final Font	DEF_FONT	= Reader.loadFont("AuX DotBitC.ttf", 25);
	private static final double	WIDTH	= 200.0;
	
	TextField field;
	VBox vbox = new VBox();
	List<Pane> text_boxes;
	int index = -1;
	
	public CircuitFinder(){
		super();
		this.getStylesheets().add("res/css/WinPane.css");
		
		this.setSpacing(10);
		
		field = new TextField();
		field.setPrefWidth(WIDTH);
		field.setFont(DEF_FONT);
		
		vbox = new VBox();
		vbox.prefWidth(WIDTH);
		vbox.setSpacing(5);
		
		text_boxes = new ArrayList<>();
		
		this.getChildren().addAll(field, vbox);
		
		this.setOnKeyReleased(e -> {
			key_pressed(e.getCode());
		});
	}
	
	public void key_pressed(KeyCode e){		
		switch(e){
		case ENTER:
			System.out.println("enter");
			break;
		case UP:
			scroll(-1);
			break;
		case DOWN:
			scroll(1);
			break;
		default:
			break;
		}
		
		show_suggestions(get_suggestions());
	}
	
	private void scroll(int i){
		//i == 1 -> inc, i == -1 -> dec.
		if(index >= 0)
			text_boxes.get(index).setBackground(Background.EMPTY);
			
		
		index += i;
		
		if(index < 0)
			index = -1;
		else if(index >= text_boxes.size())
			index = text_boxes.size() -1;
		
		if(index >= 0)
			text_boxes.get(index).setStyle("-fx-fill: #FF0000;");
	
		System.out.println(index);
	}
	
	private List<String> get_suggestions(){
		List<String> list = new ArrayList<>();
		if(field.getText().equals("")) return list;
		
		for(String s : Circuit.allCircuits.keySet())
			if(s.toLowerCase().startsWith(field.getText().toLowerCase()))
				list.add(s);
		return list;
	}
	
	private void show_suggestions(List<String> list){
		vbox.getChildren().clear();
		text_boxes.clear();
		
		for(String s : list){
			Text t = new Text(s);
			t.setFont(DEF_FONT);
			t.setOnMouseClicked(e -> {
				System.out.println(s);
			});
			Pane p = new Pane();
			p.getChildren().add(t);
			text_boxes.add(p);
			
			vbox.getChildren().add(p);
		}
		
		this.requestLayout();
	}
	
}

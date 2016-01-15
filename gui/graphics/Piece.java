package graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import circuits.Circuit;
import data.Reader;
import panes.CircuitPane;
import panes.Gui;
import javafx.animation.RotateTransition;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Piece extends Pane{
	private static final 	double 				FONT_CONST 	= 0.15;
	
	public 					PieceImage 			image;
	public 					Circuit 			c;

	//saves on font reading time by caching all the fonts.
	public static 			Map<Integer, Font> 	fontMap		= new HashMap<>();
	public 					boolean 			isDraggable = true;
	public static 			boolean 			isRotating 	= false;
	public 					double 				mousex;
	public 					double 				mousey;
	private 				boolean 			dragging 	= false;
	public 					Square 				closest;
	private 				Text 				gateName 	= new Text();
	private 				Text 				delete;
	private 				Text 				duplicate;
	//private					Text				rotate;
	
	public Piece(Circuit c) {
		this.c = c;		
		image = new PieceImage(c);
		image.setRotate(c.rot*90);
		this.getChildren().add(image);

		duplicate = new Text("+");
		duplicate.setLayoutX(CircuitPane.tileSize - 35);
		duplicate.setLayoutY(15);
		this.getChildren().add(duplicate);

		delete = new Text("×");
		delete.setLayoutX(CircuitPane.tileSize - 20);
		delete.setLayoutY(15);
		this.getChildren().add(delete);

		gateName.setText(c.name);
		gateName.setFont(Reader.loadFont("adbxtsc.ttf", (int)(CircuitPane.tileSize*FONT_CONST)));
		gateName.setStrokeWidth(2);
		gateName.setStyle("-fx-font-weight: bold;");
		gateName.setLayoutX(0);
		gateName.setLayoutY(CircuitPane.tileSize);
		this.getChildren().add(gateName);

//		rotate = new Text("R");
//		rotate.setLayoutX(CircuitPane.tileSize - 10);
//		rotate.setLayoutY(CircuitPane.tileSize - 10);
//		this.getChildren().add(rotate);

		setEvents();
	}

	private void setEvents() {
		this.setOnMousePressed(event -> {
			mousex = event.getSceneX();
			mousey = event.getSceneY();
			toFront();
			event.consume();
		});

		this.setOnMouseDragged(event -> {
			if (!isDraggable)
				return;

			dragging = true;
			setLayoutX(getLayoutX() + event.getSceneX() - mousex);
			setLayoutY(getLayoutY() + event.getSceneY() - mousey);
			mousex = event.getSceneX();
			mousey = event.getSceneY();
			Square newClosest = CircuitPane.getClosest(getLayoutX(), getLayoutY());
			if (closest != newClosest)
				closest = newClosest;
			event.consume();
		});

		this.setOnMouseReleased(event -> {
			if (!isDraggable)
				return;
			if (dragging) {
				CircuitPane.movePiece(this, closest.coord);
				dragging = false;
			} else {
				CircuitPane.togglePiece(this);
			}
			event.consume();
		});

		this.setOnScroll(event -> {
			int rot = event.getDeltaY() < 0 ? 1 : -1;
			CircuitPane.rotatePiece(c.coord, rot);
			event.consume();
		});

//		rotate.setOnMouseClicked(event -> {
//			CircuitPane.rotatePiece(c.coord, -1);
//			event.consume();
//		});

		duplicate.setOnMouseClicked(event -> {
			CircuitPane.addPiece(new Piece(c.clone()));
			event.consume();
		});

		delete.setOnMouseClicked(event -> {
			CircuitPane.cp.removePiece(this);
			event.consume();
		});
	}

	public void changePos(Square s) {
		CircuitPane.currentGame.move(this.c.coord, s.coord);
		setLayoutX(s.getLayoutX());
		setLayoutY(s.getLayoutY());
	}

	public void onResize() {
		this.image.onResize(c);

		duplicate.setLayoutX(CircuitPane.tileSize - 35);

		delete.setLayoutX(CircuitPane.tileSize - 20);

		int fontSize = (int)(CircuitPane.tileSize*FONT_CONST);
		if(fontMap.containsKey(fontSize))
			gateName.setFont(fontMap.get(fontSize));
		else {
			Font f = Reader.loadFont("adbxtsc.ttf", fontSize);
			gateName.setFont(f);
			fontMap.put(fontSize, f);
		}
			
		gateName.setLayoutY(CircuitPane.tileSize);

//		rotate.setLayoutX(CircuitPane.tileSize - 30);
//		rotate.setLayoutY(CircuitPane.tileSize - 30);
	}

	@Override
	public String toString() {
		return c.name + "," + c.rot + "," + c.pos();
	}

	@Override
	public Piece clone() {
		return new Piece(c);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Piece))
			return false;
		Piece p = (Piece) o;
		if (!this.c.equals(p.c))
			return false;
		if (this.getLayoutX() != p.getLayoutX())
			return false;
		if (this.getLayoutY() != p.getLayoutY())
			return false;
		return true;
	}

}
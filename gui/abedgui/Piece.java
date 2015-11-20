package abedgui;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import circuits.Circuit;
import panes.GamePane;
import javafx.animation.RotateTransition;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Reader;

public class Piece extends Pane{
	private static final double FONT_CONST = 0.15;
	
	public PieceImage image;
	public Circuit c;

	//saves on font reading time by caching all the fonts.
	public static Map<Integer, Font> fontMap;
	public boolean isDraggable = true;
	public static boolean isRotating = false;
	public double mousex; // where the cursor's x-position is
	public double mousey; // where the cursor's y-position is
	private boolean dragging = false; // whether the tile is currently being dragged
	public Square closest;
	private Text gateName = new Text();
	private Text delete;
	private Text duplicate;
//	private ImageView rotate = new ImageView();

	{
		fontMap = new HashMap<>();
	}
	
	public Piece(Circuit c) {
		this.c = c;		
		image = new PieceImage(c);
		image.setRotate(c.rot*90);
		this.getChildren().add(image);

		duplicate = new Text("+");
		duplicate.setLayoutX(GamePane.tileSize - 35);
		duplicate.setLayoutY(15);
		this.getChildren().add(duplicate);

		delete = new Text("×");
		delete.setLayoutX(GamePane.tileSize - 20);
		delete.setLayoutY(15);
		this.getChildren().add(delete);

		gateName.setText(c.name);
		gateName.setFont(Reader.loadFont("adbxtsc.ttf", (int)(GamePane.tileSize*FONT_CONST)));
		gateName.setStrokeWidth(2);
		gateName.setStyle("-fx-font-weight: bold;");
		gateName.setLayoutX(0);
		gateName.setLayoutY(GamePane.tileSize);
		this.getChildren().add(gateName);

//		rotate.setImage(Circuit.loadedImages.get("rotate"));
//		rotate.setFitHeight(30);
//		rotate.setFitWidth(30);
//		rotate.setLayoutX(GamePane.tileSize - 30);
//		rotate.setLayoutY(GamePane.tileSize - 30);
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
			Square newClosest = GamePane.getClosest(getLayoutX(), getLayoutY());
			if (closest != newClosest)
				closest = newClosest;
			event.consume();
		});

		this.setOnMouseReleased(event -> {
			if (!isDraggable)
				return;
			if (dragging) {
				GamePane.movePiece(this, closest.coord);
				dragging = false;
			} else {
				GamePane.togglePiece(this);
			}
			event.consume();
		});

		this.setOnScroll(event -> {
			int rot = event.getDeltaY() < 0 ? 1 : -1;
			GamePane.rotatePiece(c.coord, rot);
			event.consume();
		});

//		rotate.setOnMouseClicked(event -> {
//			GamePane.rotatePiece(c.i, c.j, -1);
//			event.consume();
//		});

		duplicate.setOnMouseClicked(event -> {
			GamePane.addPiece(new Piece(c.clone()));
			event.consume();
		});

		delete.setOnMouseClicked(event -> {
			Gui.abedPane.removePiece(this);
			event.consume();
		});
	}

	public void changePos(Square s) {
		GamePane.currentGame.move(this.c.coord, s.coord);
		setLayoutX(s.x);
		setLayoutY(s.y);
	}

	public void onResize() {
		this.image.onResize(c);

		duplicate.setLayoutX(GamePane.tileSize - 35);

		delete.setLayoutX(GamePane.tileSize - 20);

		int fontSize = (int)(GamePane.tileSize*FONT_CONST);
		if(fontMap.containsKey(fontSize))
			gateName.setFont(fontMap.get(fontSize));
		else {
			Font f = Reader.loadFont("adbxtsc.ttf", fontSize);
			gateName.setFont(f);
			fontMap.put(fontSize, f);
		}
			
		gateName.setLayoutY(GamePane.tileSize);

//		rotate.setLayoutX(GamePane.tileSize - 30);
//		rotate.setLayoutY(GamePane.tileSize - 30);
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
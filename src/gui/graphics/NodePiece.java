package gui.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import core.eval.Node;
import core.eval.Operation;
import core.game.Coord;
import data.Reader;
import gui.panes.CircuitPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class NodePiece extends Pane{
	private static final 	double 				FONT_CONST 	= 0.15;
	
	public PieceImage 	image;
	public String		id;
	public Coord 		pos;

	//saves on font reading time by caching all the fonts.
	public static 		Map<Integer, Font> 	fontMap		= new HashMap<>();
	
	public boolean isDraggable = true;
	public boolean isRotating  = false;
	
	private double mousex, mousey;
	private boolean	dragging 		= false;
	public	Square 	closest			= null;
	private	Text 	gate_name_text 	= new Text("NAME_0");
	private	Text 	delete_button	= new Text("x");
	private	Text 	duplicate_button= new Text("+");
	//private					Text				rotate;
	
	public NodePiece(String id) {
		this.id = id;
		image = new PieceImage(CircuitPane.game.f.get_node(id));
		//image.setRotate(c.rot*90);
		this.getChildren().add(image);

		duplicate_button.setLayoutX(CircuitPane.tile_size - 35);
		duplicate_button.setLayoutY(15);
		this.getChildren().add(duplicate_button);

		delete_button.setLayoutX(CircuitPane.tile_size - 20);
		delete_button.setLayoutY(15);
		this.getChildren().add(delete_button);

		gate_name_text.setText(CircuitPane.game.get_op(id).get_name());
		gate_name_text.setFont(Reader.loadFont("adbxtsc.ttf", (int)(CircuitPane.tile_size*FONT_CONST)));
		gate_name_text.setStrokeWidth(2);
		gate_name_text.setStyle("-fx-font-weight: bold;");
		gate_name_text.setLayoutX(0);
		gate_name_text.setLayoutY(CircuitPane.tile_size);
		this.getChildren().add(gate_name_text);

//		rotate = new Text("R");
//		rotate.setLayoutX(CircuitPane.tileSize - 10);
//		rotate.setLayoutY(CircuitPane.tileSize - 10);
//		this.getChildren().add(rotate);

		set_events();
	}
	
	private void set_events() {
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
			Square newClosest = CircuitPane.get_closest(getLayoutX(), getLayoutY());
			if (closest != newClosest)
				closest = newClosest;
			event.consume();
		});

		this.setOnMouseReleased(event -> {
			if (!isDraggable)
				return;
			if (dragging) {
				CircuitPane.move_piece(this.get_pos(), closest.coord);
				dragging = false;
			} else {
				CircuitPane.toggle_piece(this);
			}
			event.consume();
		});
		
		this.setOnScroll(event -> {
			int rot = event.getDeltaY() < 0 ? 1 : -1;
			CircuitPane.rotate_piece((Coord)CircuitPane.game.twin_map.equiv(id), rot);
			event.consume();
		});

//		rotate.setOnMouseClicked(event -> {
//			CircuitPane.rotatePiece(c.coord, -1);
//			event.consume();
//		});

//		duplicate.setOnMouseClicked(event -> {
//			CircuitPane.addPiece(new Piece(op.clone()));
//			event.consume();
//		});

		delete_button.setOnMouseClicked(event -> {
			CircuitPane.remove_piece(this);
			event.consume();
		});
	}
	
	public Coord get_pos(){
		return (Coord) CircuitPane.game.twin_map.equiv(id);
	}
	
	public void change_pos(Square s) {
		setLayoutX(s.getLayoutX());
		setLayoutY(s.getLayoutY());
		this.pos = s.coord;
	}

	public void on_resize() {
		this.image.on_resize();

		duplicate_button.setLayoutX(CircuitPane.tile_size - 35);

		delete_button.setLayoutX(CircuitPane.tile_size - 20);

		int fontSize = (int)(CircuitPane.tile_size*FONT_CONST);
		if(fontMap.containsKey(fontSize))
			gate_name_text.setFont(fontMap.get(fontSize));
		else {
			Font f = Reader.loadFont("adbxtsc.ttf", fontSize);
			gate_name_text.setFont(f);
			fontMap.put(fontSize, f);
		}
			
		gate_name_text.setLayoutY(CircuitPane.tile_size);

//		rotate.setLayoutX(CircuitPane.tileSize - 30);
//		rotate.setLayoutY(CircuitPane.tileSize - 30);
	}

	@Override
	public String toString() {
		return "piecetostring";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NodePiece))
			return false;
		NodePiece p = (NodePiece) o;
		if (!this.id.equals(p.id))
			return false;
		if (this.getLayoutX() != p.getLayoutX())
			return false;
		if (this.getLayoutY() != p.getLayoutY())
			return false;
		return true;
	}

}
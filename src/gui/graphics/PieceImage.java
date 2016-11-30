package gui.graphics;

import static core.Utilities.mod4;

import java.util.Observable;
import java.util.Observer;

import core.eval.Node;
import core.eval.Operation;
import core.operations.Input;
import core.operations.Output;
import data.Reader;
import gui.panes.CircuitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PieceImage extends Pane implements Observer{
	final double NODE_AS_PERCENTAGE = 0.5;
	final double INPUT_NODE_AS_PERCENTAGE = 0.7;
	
	double size;
	ImageView image = new ImageView();
	Node node;
	
	public PieceImage(Node node) {
		this.node = node;
		
		size = CircuitPane.tile_size;
		this.setPrefSize(size, size);
		
		image.setImage(Reader.get_image(node.op.get_name()));
		draw_node();
		
		this.getChildren().add(image);
	}

	private void draw_node() {
		Operation op = node.op;
		int rot = CircuitPane.game.get_rot(node.id);
		
		switch (op.get_name()) {
		case "BUS": break;
		case "LEFT":   case "LEFT2":   case "LEFT4":
		case "RIGHT":  case "RIGHT2":  case "RIGHT4":
		case "SUPER":  case "SUPER2":  case "SUPER4":
		case "MERGE":  case "MERGE2":  case "MERGE4":
		case "BRANCH": case "BRANCH2": case "BRANCH4":
		case "CROSS OVER":
			set_node_size(CircuitPane.tile_size * 0.5);
			break;
		case "INPUT":
			if (((Input)op).get_values().get(0))
				image.setImage(Reader.get_image("InputOn"));
			else
				image.setImage(Reader.get_image("InputOff"));
			set_node_size(CircuitPane.tile_size * INPUT_NODE_AS_PERCENTAGE);
			image.setRotate(mod4(-rot) * 90);
			break;
		case "OUTPUT":
			if (((Output)op).values())
				image.setImage(Reader.get_image("OutputOn"));
			else
				image.setImage(Reader.get_image("OutputOff"));
			set_node_size(CircuitPane.tile_size * INPUT_NODE_AS_PERCENTAGE);
			image.setRotate(mod4(-rot) * 90);
			break;
		default:
			set_node_size(CircuitPane.tile_size * NODE_AS_PERCENTAGE);
			break;
		}
	}
	
	//These arrays are only used in this method
//	final private static String[] image_names = new String[]{
//			"nub", "LEFT", "Straight", "MERGE",
//			"RIGHT", "MERGE", "MERGE", "SUPER"
//	};
//	final private static int[] image_rot = new int[]{ 0, 0, 0, 0, 0, -90, 180, 0 };
//	
//	private void draw_node_cable(int output_num){
//		node.setImage(Reader.get_image(image_names[output_num]));
//		node.setRotate(image_rot[output_num]);
//		node.toFront();
//	}
	
	public void set_node_size(double nodeSize) {
		image.setFitHeight(nodeSize);
		image.setFitWidth(nodeSize);
		image.setLayoutX((size - nodeSize) * 0.5);
		image.setLayoutY((size - nodeSize) * 0.5);
	}

	public void on_resize() {
		size = CircuitPane.tile_size;
		this.setPrefSize(size, size);

		draw_node();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		draw_node();
		//this.setRotate(CircuitPane.game.get(c)* 90);
	}
	
	@Override
	public String toString(){
		//TODO:
		return "string";
	}
}
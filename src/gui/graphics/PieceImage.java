package gui.graphics;

import static core.Utilities.init;
import static core.game.Gate.mod4;

import java.util.Observable;
import java.util.Observer;

import core.circuits.Cable;
import core.circuits.Input;
import core.game.Gate;
import data.Reader;
import gui.panes.CircuitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PieceImage extends Pane implements Observer{
	// name sucks I know. Change by right clicking on the class and clicking
	// refactor > rename.
	final double NODE_AS_PERCENTAGE = 0.5;
	final double INPUT_NODE_AS_PERCENTAGE = 0.7;
	
	double size;
	ImageView node;
	PieceBus[] buses;
	Gate c;
	
	public PieceImage(Gate c) {
		this.c = c;
		c.addObserver(this);
		
		size = CircuitPane.tileSize;
		this.setPrefSize(size, size);
		
		node = new ImageView(Reader.get_image(c.name.replaceAll("[0-9]", "")));
		draw_node();

		add_piece_buses(c);
		set_piece_bus_pos();
		this.getChildren().add(node);
	}

	private void draw_node() {
		switch (c.name) {
		case "BUS": break;
		case "LEFT":   case "LEFT2":   case "LEFT4":
		case "RIGHT":  case "RIGHT2":  case "RIGHT4":
		case "SUPER":  case "SUPER2":  case "SUPER4":
		case "MERGE":  case "MERGE2":  case "MERGE4":
		case "BRANCH": case "BRANCH2": case "BRANCH4":
		case "CROSS OVER":
			set_node_size(CircuitPane.tileSize * PieceBus.BUS_AS_PERCENTAGE);
			break;
		case "INPUT":
			if (((Input) c).value)
				node.setImage(Reader.get_image("InputOn"));
			else
				node.setImage(Reader.get_image("InputOff"));
			set_node_size(CircuitPane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
			break;
		case "OUTPUT":
			if (c.count_inputs().get(0))
				node.setImage(Reader.get_image("OutputOn"));
			else
				node.setImage(Reader.get_image("OutputOff"));
			set_node_size(CircuitPane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
			break;
		case "Display":
			int num = 0;
			for (int i = 0; i < 4; i++)
				if (c.count_inputs().get(i))
					num += (int) Math.pow(2, i);
			node.setImage(Reader.get_image("Display" + num));
			set_node_size(CircuitPane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
		case "CABLE":
			set_node_size(CircuitPane.tileSize * PieceBus.BUS_AS_PERCENTAGE);
			draw_node_cable(((Cable)c).get_output_num());
			break;
		default:
			set_node_size(CircuitPane.tileSize * NODE_AS_PERCENTAGE);
			break;
		}
	}
	
	//These arrays are only used in this method
	final private static String[] image_names = new String[]{
			"nub", "LEFT", "Straight", "MERGE",
			"RIGHT", "MERGE", "MERGE", "SUPER"
	};
	final private static int[] image_rot = new int[]{ 0, 0, 0, 0, 0, -90, 180, 0 };
	
	private void draw_node_cable(int output_num){
		node.setImage(Reader.get_image(image_names[output_num]));
		node.setRotate(image_rot[output_num]);
		node.toFront();
	}
	
	public void set_node_size(double nodeSize) {
		node.setFitHeight(nodeSize);
		node.setFitWidth(nodeSize);
		node.setLayoutX((size - nodeSize) * 0.5);
		node.setLayoutY((size - nodeSize) * 0.5);
	}

	public void add_piece_buses(Gate c) {
		buses = new PieceBus[4];
		
		switch(c.name){
		case "CABLE":
			for (int dir = 0; dir <= 2; dir++){
				buses[dir] = new PieceToggleableBus(this, dir);
				buses[dir].setRotate(dir * 90);
			}
			
			buses[3] = new PieceBus(this, 3);
			buses[3].setRotate(3 * 90);
			break;
		default:
			for (int dir = 0; dir < 4; dir++){
				buses[dir] = new PieceBus(this, dir);
				buses[dir].setRotate(dir * 90);
			}	
		}

		set_piece_bus_pos();
		for (PieceBus pb : buses)
			this.getChildren().add(pb);
	}

	private void set_piece_bus_pos() {
		double cen = size * 0.5 * (1 - PieceBus.BUS_AS_PERCENTAGE);
		double offset = size * 0.5 * (0.5 - PieceBus.BUS_AS_PERCENTAGE);
		
		buses[0].setLayoutX(cen);
		buses[0].setLayoutY(0);
		buses[1].setLayoutX(size * 0.5 + offset);
		buses[1].setLayoutY(cen - offset);
		buses[2].setLayoutX(cen);
		buses[2].setLayoutY(size * 0.5);
		buses[3].setLayoutX(0 + offset);
		buses[3].setLayoutY(cen - offset);
		
	}

	public void onResize(Gate c) {
		size = CircuitPane.tileSize;
		this.setPrefSize(size, size);

		draw_node();

		for (PieceBus pb : buses)
			if (pb != null)
				pb.onResize();
		set_piece_bus_pos();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		draw_node();
		this.setRotate(c.rot * 90);
		for(int i = 0; i < 4; i++)
			if(buses[i] != null)
				buses[i].update();
	}
	
	@Override
	public String toString(){
		String tbr = "[";
		for(PieceBus pb : this.buses)
			tbr += (pb == null ? "NULL" : pb.toString())+",";
		return init(tbr) +"]";
	}
}
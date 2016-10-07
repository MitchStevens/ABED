package graphics;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import circuits.BusIn;
import circuits.BusOut;
import circuits.Cable;
import circuits.Circuit;
import circuits.Input;
import data.Reader;
import panes.CircuitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import static circuits.Circuit.mod4;

public class PieceImage extends Pane implements Observer{
	// name sucks I know. Change by right clicking on the class and clicking
	// refactor > rename.
	final double NODE_AS_PERCENTAGE = 0.5;
	final double INPUT_NODE_AS_PERCENTAGE = 0.7;
	
	double size;
	ImageView node;
	PieceBus[] buses;
	Circuit c;
	
	public PieceImage(Circuit c) {
		this.c = c;
		c.addObserver(this);
		
		size = CircuitPane.tileSize;
		this.setPrefSize(size, size);

		node = new ImageView(Reader.ALL_IMAGES.get(c.name.replaceAll("[0-9]", "")));
		drawNode();

		addPieceBuses(c);
		setPieceBusPos();
		this.getChildren().add(node);
	}

	private void drawNode() {
		switch (c.name) {
		case "BUS": break;
		case "LEFT":   case "LEFT2":   case "LEFT4":
		case "RIGHT":  case "RIGHT2":  case "RIGHT4":
		case "SUPER":  case "SUPER2":  case "SUPER4":
		case "MERGE":  case "MERGE2":  case "MERGE4":
		case "BRANCH": case "BRANCH2": case "BRANCH4":
		case "CROSS OVER":
			setNodeSize(CircuitPane.tileSize * PieceBus.BUS_AS_PERCENTAGE);
			break;
		case "INPUT":
			if (((Input) c).value)
				node.setImage(Reader.ALL_IMAGES.get("InputOn"));
			else
				node.setImage(Reader.ALL_IMAGES.get("InputOff"));
			setNodeSize(CircuitPane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
			break;
		case "OUTPUT":
			if (c.flattenInputs().get(0))
				node.setImage(Reader.ALL_IMAGES.get("OutputOn"));
			else
				node.setImage(Reader.ALL_IMAGES.get("OutputOff"));
			setNodeSize(CircuitPane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
			break;
		case "Display":
			int num = 0;
			for (int i = 0; i < 4; i++)
				if (c.flattenInputs().get(i))
					num += (int) Math.pow(2, i);
			node.setImage(Reader.ALL_IMAGES.get("Display" + num));
			setNodeSize(CircuitPane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
		case "CABLE":
			setNodeSize(CircuitPane.tileSize * PieceBus.BUS_AS_PERCENTAGE);
			drawNodeCable(((Cable)c).get_output_num());
			break;
		default:
			setNodeSize(CircuitPane.tileSize * NODE_AS_PERCENTAGE);
			break;
		}
	}

	private void drawNodeCable(int output_num){
		//beacuse putting a switch inside a switch is just poor form
		switch(output_num){
		case 0:
			node.setImage(Reader.ALL_IMAGES.get("nub"));
			node.setRotate(0);
			break;
		case 1:
			node.setImage(Reader.ALL_IMAGES.get("LEFT"));
			node.setRotate(0);
			break;
		case 2:
			node.setImage(Reader.ALL_IMAGES.get("Straight"));
			node.setRotate(0);
			break;
		case 3:
			node.setImage(Reader.ALL_IMAGES.get("MERGE"));
			node.setRotate(0);
			break;
		case 4:
			node.setImage(Reader.ALL_IMAGES.get("RIGHT"));
			node.setRotate(0);
			break;
		case 5:
			node.setImage(Reader.ALL_IMAGES.get("MERGE"));
			node.setRotate(-90);
			break;
		case 6:
			node.setImage(Reader.ALL_IMAGES.get("MERGE"));
			node.setRotate(180);
			break;
		case 7:
			node.setImage(Reader.ALL_IMAGES.get("SUPER"));
			node.setRotate(0);
			break;
		}
		node.toFront();
	}
	
	public void setNodeSize(double nodeSize) {
		node.setFitHeight(nodeSize);
		node.setFitWidth(nodeSize);
		node.setLayoutX((size - nodeSize) * 0.5);
		node.setLayoutY((size - nodeSize) * 0.5);
	}

	public void addPieceBuses(Circuit c) {
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

		setPieceBusPos();
		for (PieceBus pb : buses)
			this.getChildren().add(pb);
	}

	private void setPieceBusPos() {
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

	public void onResize(Circuit c) {
		size = CircuitPane.tileSize;
		this.setPrefSize(size, size);

		drawNode();

		for (PieceBus pb : buses)
			if (pb != null)
				pb.onResize();
		setPieceBusPos();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		drawNode();
		this.setRotate(c.rot * 90);
		for(int i = 0; i < 4; i++)
			if(buses[i] != null)
				buses[i].update();
	}
	
	@Override
	public String toString(){
		String tbr = "";
		for(PieceBus pb : this.buses)
			tbr += (pb == null ? "NULL" : pb.toString())+", ";
		return tbr;
	}
}
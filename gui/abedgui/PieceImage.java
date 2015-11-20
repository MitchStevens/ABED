package abedgui;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import circuits.BusIn;
import circuits.BusOut;
import circuits.Cable;
import circuits.Circuit;
import circuits.Input;
import panes.GamePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import logic.Reader;
import static circuits.Circuit.mod4;

public class PieceImage extends Pane implements Observer{
	// name sucks I know. Change by right clicking on the class and clicking
	// refactor > rename.
	public static Map<String, Image> ALL_IMAGES;
	final double NODE_AS_PERCENTAGE = 0.5;
	final double INPUT_NODE_AS_PERCENTAGE = 0.7;
	
	double size;
	ImageView node;
	PieceBus[] buses;
	Circuit c;

	static {
		ALL_IMAGES = Reader.loadImages();
	}
	
	public PieceImage(Circuit c) {
		this.c = c;
		c.addObserver(this);
		
		size = GamePane.tileSize;
		this.setPrefSize(size, size);

		node = new ImageView(ALL_IMAGES.get(c.name.replaceAll("[0-9]", "")));
		drawNode();

		addPieceBuses(c);
		setPieceBusPos();
		this.getChildren().add(node);
	}

	private void drawNode() {
		switch (c.name) {
		case "Bus": break;
		case "Left":   case "Left2":   case "Left4":
		case "Right":  case "Right2":  case "Right4":
		case "Super":  case "Super2":  case "Super4":
		case "Merge":  case "Merge2":  case "Merge4":
		case "Branch": case "Branch2": case "Branch4":
		case "Cross_Over":
			setNodeSize(GamePane.tileSize * PieceBus.BUS_AS_PERCENTAGE);
			break;
		case "Input":
			if (((Input) c).value)
				node.setImage(ALL_IMAGES.get("InputOn"));
			else
				node.setImage(ALL_IMAGES.get("InputOff"));
			setNodeSize(GamePane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
			break;
		case "Output":
			if (c.flattenInputs().get(0))
				node.setImage(ALL_IMAGES.get("OutputOn"));
			else
				node.setImage(ALL_IMAGES.get("OutputOff"));
			setNodeSize(GamePane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
			break;
		case "Display":
			int num = 0;
			for (int i = 0; i < 4; i++)
				if (c.flattenInputs().get(i))
					num += (int) Math.pow(2, i);
			node.setImage(ALL_IMAGES.get("Display" + num));
			setNodeSize(GamePane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
		case "Cable":
			setNodeSize(GamePane.tileSize * PieceBus.BUS_AS_PERCENTAGE);
			drawNodeCable(((Cable)c).get_output_num());
			break;
		default:
			setNodeSize(GamePane.tileSize * NODE_AS_PERCENTAGE);
			break;
		}
	}

	private void drawNodeCable(int output_num){
		//beacuse putting a switch inside a switch is just poor form
		switch(output_num){
		case 0:
			node.setImage(ALL_IMAGES.get("nub"));
			node.setRotate(0);
			break;
		case 1:
			node.setImage(ALL_IMAGES.get("Left"));
			node.setRotate(0);
			break;
		case 2:
			node.setImage(ALL_IMAGES.get("Straight"));
			node.setRotate(0);
			break;
		case 3:
			node.setImage(ALL_IMAGES.get("Merge"));
			node.setRotate(0);
			break;
		case 4:
			node.setImage(ALL_IMAGES.get("Right"));
			node.setRotate(0);
			break;
		case 5:
			node.setImage(ALL_IMAGES.get("Merge"));
			node.setRotate(-90);
			break;
		case 6:
			node.setImage(ALL_IMAGES.get("Merge"));
			node.setRotate(180);
			break;
		case 7:
			node.setImage(ALL_IMAGES.get("Super"));
			node.setRotate(0);
			break;
		}
	}
	
	public void setNodeSize(double nodeSize) {
		node.setFitHeight(nodeSize);
		node.setFitWidth(nodeSize);
		node.setLayoutX((size - nodeSize) * 0.5);
		node.setLayoutY((size - nodeSize) * 0.5);
	}

	public void addPieceBuses(Circuit c) {
		buses = new PieceBus[4];
		
		for (int dir = 0; dir < 4; dir++){
			buses[dir] = new PieceBus(this, dir);
			buses[dir].setRotate(dir * 90);
			if(c instanceof Cable)
				buses[dir].toggleable = true;
		}

		setPieceBusPos();
		for (PieceBus pb : buses)
			if (pb != null)
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
		size = GamePane.tileSize;
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
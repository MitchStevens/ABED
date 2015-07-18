package abedgui;

import java.util.List;
import java.util.Map;

import panes.GamePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import logic.Bus;
import logic.Circuit;
import logic.Input;
import logic.Reader;
import static logic.Circuit.mod4;

public class PieceImage extends Pane {
	// name sucks I know. Change by right clicking on the class and clicking
	// refactor > rename.
	public static Map<String, Image> ALL_IMAGES;
	final double NODE_AS_PERCENTAGE = 0.5;
	final double INPUT_NODE_AS_PERCENTAGE = 0.7;
	
	double size;
	ImageView node;
	PieceBus[] buses;
	Circuit c;

	public PieceImage(Circuit c) {
		this.c = c;
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
			if (c.inputList().get(0))
				node.setImage(ALL_IMAGES.get("OutputOn"));
			else
				node.setImage(ALL_IMAGES.get("OutputOff"));
			setNodeSize(GamePane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
			break;
		case "Display":
			int num = 0;
			for (int i = 0; i < 4; i++)
				if (c.inputList().get(i))
					num += (int) Math.pow(2, i);
			node.setImage(ALL_IMAGES.get("Display" + num));
			setNodeSize(GamePane.tileSize * INPUT_NODE_AS_PERCENTAGE);
			node.setRotate(mod4(-c.rot) * 90);
		default:
			setNodeSize(GamePane.tileSize * NODE_AS_PERCENTAGE);
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

		for (int dir = 0; dir < 4; dir++) {
			if (c.inputBus.get(dir).size() > 0)
				buses[dir] = new PieceBus(c.inputBus.get(dir), dir, true);
			else if (c.outputBus.get(dir).size() > 0)
				buses[dir] = new PieceBus(c.outputBus.get(dir), dir, false);
			else
				continue;
			buses[dir].setRotate(dir * 90);
		}

		setPieceBusPos();
		for (PieceBus pb : buses)
			if (pb != null)
				this.getChildren().add(pb);
	}

	private void setPieceBusPos() {
		double cen = size * 0.5 * (1 - PieceBus.BUS_AS_PERCENTAGE);
		double offset = size * 0.5 * (0.5 - PieceBus.BUS_AS_PERCENTAGE);
		if (buses[0] != null) {
			buses[0].setLayoutX(cen);
			buses[0].setLayoutY(0);
		}
		if (buses[1] != null) {
			buses[1].setLayoutX(size * 0.5 + offset);
			buses[1].setLayoutY(cen - offset);
		}
		if (buses[2] != null) {
			buses[2].setLayoutX(cen);
			buses[2].setLayoutY(size * 0.5);
		}
		if (buses[3] != null) {
			buses[3].setLayoutX(0 + offset);
			buses[3].setLayoutY(cen - offset);
		}
	}

	public void update(Circuit c) {
		this.c = c;
		String s = c.name;

		drawNode();

		for (int i = 0; i < 4; i++)
			if (buses[i] != null)
				if (buses[i].isInput)
					buses[i].update(c.inputBus.get(i));
				else
					buses[i].update(c.outputBus.get(i));

		this.setRotate(c.rot * 90);
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
}
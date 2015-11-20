package abedgui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.border.LineBorder;

import circuits.Bus;
import circuits.BusIn;
import circuits.BusOut;
import circuits.Circuit;
import circuits.Cable;
import panes.GamePane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PieceBus extends Pane{
	final static double BUS_AS_PERCENTAGE = 0.3;
	final Color LIGHT_ON = Color.color(0.4375, 0.96875, 0.96875);
	final Color LIGHT_OFF = Color.color(0.390625, 0.46484375, 0.5078125);

	public boolean toggleable = false;
	public Bus b;
	public int size;
	public int dir;
	private PieceImage piece_image;
	private ImageView busImage;
	// the lights sit inside the fixture
	private Pane fixture;
	private Rectangle[] lights;

	public PieceBus(PieceImage piece_image, int dir) {
		this.dir = dir;
		this.piece_image = piece_image;
		this.b = piece_image.c.buses.get(dir);
		
		busImage = new ImageView();
		update_bus();

		busImage.setFitHeight(GamePane.tileSize * 0.5);
		busImage.setFitWidth(GamePane.tileSize * BUS_AS_PERCENTAGE);
		this.getChildren().add(busImage);

		fixture = new Pane();
		fixture.setStyle("-fx-background-color: #798C97;");
		fixture.setPrefSize(GamePane.tileSize * BUS_AS_PERCENTAGE,
				GamePane.tileSize * BUS_AS_PERCENTAGE * (3.0 / 9.0));
		if(b == null)
			fixture.setVisible(false);
		this.getChildren().add(fixture);
		
		init_lights();
		
		this.setOnMouseClicked(e -> toggle_bus());
	}

	private void init_lights(){
		b = piece_image.c.buses.get(dir);
		
		if(b == null){
			lights = new Rectangle[0];
			return;
		}else {
			lights = new Rectangle[b.size];
		}
		
		double lightSize = GamePane.tileSize * BUS_AS_PERCENTAGE / 9.0;
		double init = (5 - lights.length) * lightSize;
		
		for (int i = 0; i < b.size; i++) {
			lights[i] = new Rectangle(init + lightSize * 2 * i, lightSize,
					lightSize, lightSize);

			if (b.get(i))
				lights[i].setFill(LIGHT_ON);
			else
				lights[i].setFill(LIGHT_OFF);
			fixture.getChildren().add(lights[i]);
		}
		
		for (int i = 0; i < lights.length; i++){
			lights[i].setFill(b.get(i) ? LIGHT_ON : LIGHT_OFF);
		}
		
		update_lights();
	}
	
	public void onResize() {
		busImage.setFitHeight(GamePane.tileSize * 0.5);
		busImage.setFitWidth(GamePane.tileSize * BUS_AS_PERCENTAGE);

		fixture.setPrefSize(GamePane.tileSize * BUS_AS_PERCENTAGE,
				GamePane.tileSize * BUS_AS_PERCENTAGE * (3.0 / 9.0));

		double lightSize = GamePane.tileSize * BUS_AS_PERCENTAGE / 9.0;
		double init = (5 - lights.length) * lightSize;
		for (int i = 0; i < lights.length; i++) {
			lights[i].setX(init + lightSize*2*i);
			lights[i].setY(lightSize);
			lights[i].setHeight(lightSize);
			lights[i].setWidth(lightSize);
		}
	}

	public void toggle_bus(){
		if(toggleable && !(piece_image.c.buses.get(dir) instanceof BusIn))
			((Cable)piece_image.c).toggle_output(dir);
		update();
		
		b = piece_image.c.buses.get(dir);
		if(b == null){
			fixture.setVisible(false);
			piece_image.setOpacity(0.1);
		} else {
			fixture.setVisible(true);
			piece_image.setOpacity(1);
		}
		
		for(String s : piece_image.c.printCircuit())
			System.out.println(s);
	}
	
	public void update(){
		b = piece_image.c.buses.get(dir);
		if(toggleable){
			update_toggleable();
			return;
		}
		
		if(b == null){
			fixture.setVisible(false);
			busImage.setVisible(false);
			return;
		}
		
		for (int i = 0; i < lights.length; i++)
			lights[i].setFill(b.get(i) ? LIGHT_ON : LIGHT_OFF);

		String piece_id = "";
		piece_id += (b instanceof BusIn ? "PipeIn" : "PipeOut")+"_"+(b.or() ? "On" : "Off");
		busImage.setImage(PieceImage.ALL_IMAGES.get(piece_id));
		
	}
	
	private void update_toggleable(){
		
	}
}

package gui.graphics;

import core.circuits.Bus;
import core.circuits.BusIn;
import core.circuits.BusOut;
import core.circuits.Cable;
import core.game.Gate;
import data.Reader;
import gui.panes.CircuitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PieceBus extends Pane{
	final 	static 	double 		BUS_AS_PERCENTAGE = 0.3;
	final 			Color 		LIGHT_ON = Color.color(0.4375, 0.96875, 0.96875);
	final 			Color 		LIGHT_OFF = Color.color(0.390625, 0.46484375, 0.5078125);

	public 			Bus 		b;
	public 			int 		size;
	public 			int 		dir;
	protected		PieceImage 	piece_image;
	protected 		ImageView 	bus_image;
	// the lights sit inside the fixture
	protected 		Pane 		fixture;
	protected 		Rectangle[] lights;

	public PieceBus(){}
	
	public PieceBus(PieceImage piece_image, int dir) {
		this.dir = dir;
		this.piece_image = piece_image;
		this.b = piece_image.c.buses.get(dir);
		
		bus_image = new ImageView();
		bus_image.setFitHeight(CircuitPane.tileSize * 0.5);
		bus_image.setFitWidth(CircuitPane.tileSize * BUS_AS_PERCENTAGE);
		this.getChildren().add(bus_image);

		fixture = new Pane();
		fixture.setStyle("-fx-background-color: #45535A;");
		fixture.setPrefSize(CircuitPane.tileSize * BUS_AS_PERCENTAGE,
				CircuitPane.tileSize * BUS_AS_PERCENTAGE * (3.0 / 9.0));
		if(b == null)
			fixture.setVisible(false);
		this.getChildren().add(fixture);
		
		init_lights();
		update();
	}

	protected void init_lights(){
		b = piece_image.c.buses.get(dir);
		
		if(b == null){
			lights = new Rectangle[0];
			return;
		}else {
			lights = new Rectangle[b.size];
		}
		
		double lightSize = CircuitPane.tileSize * BUS_AS_PERCENTAGE / 9.0;
		double init = (5 - lights.length) * lightSize;
		
		for (int i = 0; i < b.size; i++) {
			lights[i] = new Rectangle(init + lightSize * 2 * i, lightSize,
					lightSize, lightSize);

			lights[i].setFill(b.get(i)?LIGHT_ON:LIGHT_OFF);
			fixture.getChildren().add(lights[i]);
		}
		
		for (int i = 0; i < lights.length; i++)
			lights[i].setFill(b.get(i) ? LIGHT_ON : LIGHT_OFF);
		
		update();
	}
	
	public void onResize() {
		bus_image.setFitHeight(CircuitPane.tileSize * 0.5);
		bus_image.setFitWidth(CircuitPane.tileSize * BUS_AS_PERCENTAGE);

		fixture.setPrefSize(CircuitPane.tileSize * BUS_AS_PERCENTAGE,
				CircuitPane.tileSize * BUS_AS_PERCENTAGE * (3.0 / 9.0));

		double lightSize = CircuitPane.tileSize * BUS_AS_PERCENTAGE / 9.0;
		double init = (5 - lights.length) * lightSize;
		for (int i = 0; i < lights.length; i++) {
			lights[i].setX(init + lightSize*2*i);
			lights[i].setY(lightSize);
			lights[i].setHeight(lightSize);
			lights[i].setWidth(lightSize);
		}
	}
	
	public void update(){
		b = piece_image.c.buses.get(dir);
		
		if(b == null){
			fixture.setVisible(false);
			bus_image.setVisible(false);
			return;
		}
		
		for (int i = 0; i < lights.length; i++)
			lights[i].setFill(b.get(i) ? LIGHT_ON : LIGHT_OFF);

		String piece_id = (b instanceof BusIn ? "PipeIn" : "PipeOut")+"_"+(b.or() ? "On" : "Off");
		bus_image.setImage(Reader.get_image(piece_id));
		
	}
}

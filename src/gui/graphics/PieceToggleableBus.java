package gui.graphics;

import javafx.scene.shape.Rectangle;
import core.circuits.BusIn;
import core.circuits.Cable;
import data.Reader;
import gui.panes.CircuitPane;

public class PieceToggleableBus extends PieceBus {
	final public static double DISABLED_OPACITY = 0.15;
	
	private int capacity;
	
	public PieceToggleableBus(PieceImage piece_image, int dir) {
		super(piece_image, dir);
		
		capacity = ((Cable)piece_image.c).capacity;		
		this.setOnMouseClicked(e -> toggle_bus());
		
		init_lights();
		update();
	}

	protected void init_lights(){
		b = piece_image.c.buses.get(dir);
		
		lights = new Rectangle[capacity];
		
		double lightSize = CircuitPane.tileSize * BUS_AS_PERCENTAGE / 9.0;
		double init = (5 - lights.length) * lightSize;
		
		for (int i = 0; i < capacity; i++) {
			lights[i] = new Rectangle(init + lightSize * 2 * i, lightSize,
					lightSize, lightSize);

			if(b == null)
				lights[i].setFill(LIGHT_OFF);
			else
				lights[i].setFill( b.get(i) ? LIGHT_ON : LIGHT_OFF);

			fixture.getChildren().add(lights[i]);
		}
	}
	
	public void toggle_bus(){
		if(!(piece_image.c.buses.get(dir) instanceof BusIn))
			((Cable)piece_image.c).toggle_output(dir);
		update();
	}
	
	public void update(){
		b = piece_image.c.buses.get(dir);
		boolean output_value = ((Cable)piece_image.c).output_toggle[dir];
		
		this.setOpacity( output_value ? 1.0 : DISABLED_OPACITY);
		
		for (int i = 0; i < lights.length; i++){
			lights[i].setFill(b.get(i) && output_value ? LIGHT_ON : LIGHT_OFF);
		}
		
		String piece_id = "PipeOut_Off";
		if(output_value)
			piece_id = (b instanceof BusIn ? "PipeIn" : "PipeOut")+"_"+(b.or() ? "On" : "Off");
		bus_image.setImage(Reader.get_image(piece_id));
		
	}

}

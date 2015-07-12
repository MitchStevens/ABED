package abedgui;

import panes.GamePane;
import javafx.scene.layout.Pane;
import logic.Bus;

public class PieceBus extends Pane {

	
	public int size;
	//the lights sit inside the fixture
	private Pane fixture;
	private Pane[] lights;
	
	public PieceBus(Bus b, Boolean isInput){
		fixture = new Pane();
		fixture.setPrefSize(GamePane.tileSiz*10, prefHeight);
		GamePane.tileSize
		
	}
}

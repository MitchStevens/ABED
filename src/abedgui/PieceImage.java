package abedgui;

import java.util.List;

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

import static logic.Circuit.mod4;

public class PieceImage extends Pane{
	//name sucks I know. Change by right clicking on the class and clicking refactor > rename.
	double size;
	final double NODE_AS_PERCENTAGE = 0.5;
	ImageView node;
	PieceBus[] buses;
	Circuit c;
	
	public PieceImage(Circuit c){
		this.c = c;
		this.setStyle("-fx-background-color: white;");
		this.setPrefSize(GamePane.tileSize, GamePane.tileSize);
		node = new ImageView(Circuit.loadedImages.get(c.name));
		init(GamePane.tileSize);
		addPieceBuses(c);
		setPieceBusPos();
		this.getChildren().add(node);
	}
	
	public void init(double size){
		this.size = size;
		node.setFitHeight(size*NODE_AS_PERCENTAGE);
		node.setFitWidth(size*NODE_AS_PERCENTAGE);
		node.setLayoutX(size*0.5*(1-NODE_AS_PERCENTAGE));
		node.setLayoutY(size*0.5*(1-NODE_AS_PERCENTAGE));
	}
        
	public void addPieceBuses(Circuit c) {
		buses = new PieceBus[4];
		
		for(int dir = 0; dir < 4; dir++){
			if(c.inputBus.get(dir).size() > 0)
				buses[dir] = new PieceBus(c.inputBus.get(dir), dir, true);
			else if(c.outputBus.get(dir).size() > 0)
        		buses[dir] = new PieceBus(c.outputBus.get(dir), dir, false);
			else continue;
			buses[dir].setRotate(dir*90);
		}
		
		setPieceBusPos();
		for(PieceBus pb : buses)
			if(pb != null)
				this.getChildren().add(pb);
 	}
	
	private void setPieceBusPos(){
		double cen = size*0.5*(1 - PieceBus.BUS_AS_PERCENTAGE);
		double offset = size*0.5*(0.5 - PieceBus.BUS_AS_PERCENTAGE);
		if(buses[0] != null){
			buses[0].setLayoutX(cen);
			buses[0].setLayoutY(0);
		}
		if(buses[1] != null){
			buses[1].setLayoutX(size*0.5 + offset);
			buses[1].setLayoutY(cen - offset);
		}
		if(buses[2] != null){
			buses[2].setLayoutX(cen);
			buses[2].setLayoutY(size*0.5);
		}
		if(buses[3] != null){
			buses[3].setLayoutX(0 + offset);
			buses[3].setLayoutY(cen - offset);
		}
	}
	
	public void update(Circuit c){
		this.c = c;
  		String s = c.name;
  		
    	this.setPrefSize(GamePane.tileSize, GamePane.tileSize);
        if("Bus".equals(s) || "Left".equals(s) || "Right".equals(s))
        	node.setRotate(c.rot*90);
        
        for(int i = 0; i < 4; i++)
        	if(buses[i] != null)
        		if(buses[i].isInput)
        			buses[i].update(c.inputBus.get(i));
        		else
        			buses[i].update(c.outputBus.get(i));
        
        this.setRotate(c.rot*90);
	}
	
	public void onResize(Circuit c){
		init(GamePane.tileSize);
		for(PieceBus pb : buses)
			if(pb != null)
				pb.onResize();
		setPieceBusPos();
	}
}

package abedgui;

import panes.GamePane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.Bus;
import logic.Circuit;

public class PieceBus extends Pane {
	final static double BUS_AS_PERCENTAGE = 0.3;
	final Color LIGHT_ON  = Color.color(0.4375, 0.96875, 0.96875);
	final Color LIGHT_OFF = Color.color(0.5, 0.5, 0.5);
	
	public int size;
	public int dir;
	public boolean isInput;
	private ImageView busImage;
	//the lights sit inside the fixture
	private Pane fixture;
	private Rectangle[] lights;
	
	public PieceBus(Bus b, int dir, Boolean isInput){
		this.isInput = isInput;
		this.dir = dir;
		
		busImage = new ImageView();
		if(b.or())
			if(isInput)
				busImage.setImage(Circuit.loadedImages.get("PipeIn_On"));
			else
				busImage.setImage(Circuit.loadedImages.get("PipeOut_On"));
		else
			if(isInput)
				busImage.setImage(Circuit.loadedImages.get("PipeIn_Off"));
			else
				busImage.setImage(Circuit.loadedImages.get("PipeOut_Off"));
		
		busImage.setFitHeight(GamePane.tileSize*0.5);
		busImage.setFitWidth(GamePane.tileSize*BUS_AS_PERCENTAGE);
		this.getChildren().add(busImage);
		
		fixture = new Pane();
		fixture.setStyle("-fx-background-color: black;");
		fixture.setPrefSize(GamePane.tileSize*BUS_AS_PERCENTAGE, GamePane.tileSize*BUS_AS_PERCENTAGE*(3.0/9.0));
		this.getChildren().add(fixture);
		
		lights = new Rectangle[b.size()];
		double lightSize = GamePane.tileSize*BUS_AS_PERCENTAGE/9.0;
		double init = (5-lights.length)*lightSize;
		for(int i = 0; i < b.size(); i++){
			if(dir == 0 || dir == 1)
				lights[i] = new Rectangle(init + lightSize*2*i, lightSize, lightSize, lightSize);
			else
				lights[i] = new Rectangle(init + lightSize*2*(b.size() -1 -i), lightSize, lightSize, lightSize);
			
			if(b.get(i)) lights[i].setFill(LIGHT_ON);
			else 		 lights[i].setFill(LIGHT_OFF);
			fixture.getChildren().add(lights[i]);
		}
	}
	
	public void update(Bus b){
		fixture.setPrefSize(GamePane.tileSize*BUS_AS_PERCENTAGE, GamePane.tileSize*BUS_AS_PERCENTAGE*(3.0/7.0));
		
		for(int i  = 0; i < lights.length; i++)
			lights[i].setFill( b.get(i) ? LIGHT_ON : LIGHT_OFF );
		
		if(b.or())
			if(isInput)
				busImage.setImage(Circuit.loadedImages.get("PipeIn_On"));
			else
				busImage.setImage(Circuit.loadedImages.get("PipeOut_On"));
		else
			if(isInput)
				busImage.setImage(Circuit.loadedImages.get("PipeIn_Off"));
			else
				busImage.setImage(Circuit.loadedImages.get("PipeOut_Off"));
	}
	
	public void onResize(){
		busImage.setFitHeight(GamePane.tileSize*0.5);
		busImage.setFitWidth(GamePane.tileSize*BUS_AS_PERCENTAGE);
		
		double lightSize = GamePane.tileSize*BUS_AS_PERCENTAGE/7.0;
		double initY = (4-lights.length)*lightSize;
		for(int i  = 0; i < lights.length; i++){
			lights[i].setHeight(lightSize);
			lights[i].setWidth(lightSize);
			lights[i].setLayoutX(lightSize);
			lights[i].setLayoutY(initY + lightSize*2*i);
		}
	}
}

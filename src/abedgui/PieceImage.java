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

public class PieceImage extends Pane{
	//name sucks I know. Change by right clicking on the class and clicking refactor > rename.
	double size;
	ImageView image;
	
	public PieceImage(Circuit c){
		image = new ImageView();
		init(GamePane.tileSize);
		this.getChildren().add(image);
		update(c);
	}
	
	public PieceImage(Circuit c, double size){
		image = new ImageView();
		init(size);
		this.getChildren().add(image);
		update(c);
	}
	
	public void init(double size){
		this.size = size;
		image.setFitHeight(size/2);
		image.setFitWidth(size/2);
		image.setLayoutX(size*0.25);
		image.setLayoutY(size*0.25);
	}
        
        public void circuitBuilder(Circuit c) {
            for(int dir = 0; dir < 4; dir++)
                if(c.inputBus.get(dir).size() > 0){
                    ImageView img = new ImageView();
                    
                    if(c.inputBus.get(dir).get(0))
                        img.setImage(resample(Circuit.loadedImages.get("PipeIn_On")));
                    else img.setImage(resample(Circuit.loadedImages.get("PipeIn_Off")));
                    
                    img.setRotate(90*dir+(c.rot*90));
                    img.setFitHeight(size);
                    img.setFitWidth(size);
                    this.getChildren().add(img);
                }
            
            for(int dir = 0; dir < 4; dir++)
                if(c.outputBus.get(dir).size() > 0){
                    ImageView img = new ImageView();
                    
                    if(c.outputBus.get(dir).get(0))
                        img.setImage(resample(Circuit.loadedImages.get("PipeOut_On")));
                    else img.setImage(resample(Circuit.loadedImages.get("PipeOut_Off")));
                    
                    img.setRotate(90*dir+(c.rot*90));
                    img.setFitHeight(size);
                    img.setFitWidth(size);
                    this.getChildren().add(img);
                }
            
        }
	
	public void update(Circuit c){
  		String s = c.name;
                this.getChildren().clear();
                this.getChildren().add(image);
                if("Bus".equals(s) || "Left".equals(s) || "Right".equals(s)){
                    image.setRotate(c.rot*90);
                }
                circuitBuilder(c);
		if(Circuit.loadedImages.containsKey(s)){
			image.setImage(resample(Circuit.loadedImages.get(s)));
		} else image.setImage(Circuit.loadedImages.get("EmptyGate"));
                image.toFront();
                

	}
	
	//taken from https://gist.github.com/jewelsea/5415891
    public static Image resample(Image input) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();
        final int S = 10;
        WritableImage output = new WritableImage(
            W * S,
            H * S
        );
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
            final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < S; dy++) {
                    for (int dx = 0; dx < S; dx++) {
                        writer.setArgb(x * S + dx, y * S + dy, argb);
                    }
                }
            }
        }
        return output;
    }
}

package abedgui;

import java.util.List;

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
		size = Gui.tileSize;
		image.setFitHeight(size);
		image.setFitWidth(size);
		this.getChildren().add(image);
		update(c);
	}
	
	public PieceImage(Circuit c, double size){
		image = new ImageView();
		this.size = size;
		image.setFitHeight(size);
		image.setFitWidth(size);
		this.getChildren().add(image);
		update(c);
	}
	
	public void update(Circuit c){
		String s = c.name;
		if(c instanceof Input){
			if(((Input)c).outputList().get(0))
				s += "1";
			else s += "0";
		} else{
			for(Boolean b : c.inputList())
				s += (b ? "1" : "0");}
				
		if(Circuit.loadedImages.containsKey(s)){
			image.setImage(resample(Circuit.loadedImages.get(s)));
		} else image.setImage(Circuit.loadedImages.get("EmptyGate"));
		image.setRotate(90*c.rot);
		System.out.println(s);
		System.out.println(c.printCircuit()[0]);
		System.out.println(c.printCircuit()[1]);
		System.out.println(c.printCircuit()[2]);
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

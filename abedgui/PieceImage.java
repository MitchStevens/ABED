package abedgui;

import java.util.List;

import testing.TestingPieceImage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import logic.Bus;
import logic.Circuit;

public class PieceImage extends Pane{
	//name sucks I know. Change by right clicking on the class and clicking refactor > rename.
	double size = Gui.tileSize;
	
	public PieceImage(){
		ImageView image = new ImageView(Circuit.loadedImages.get("Or10"));
		this.getChildren().add(image);
	}
	
	public static PieceImage defPiece(){
		return new PieceImage();
	}
	
	public static void main(String[] args){
		TestingPieceImage a = new TestingPieceImage();
		//a.add(PieceImage.defPiece());
		a.main(null);
	}
}

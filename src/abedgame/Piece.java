package abedGame;

import java.nio.file.FileVisitResult;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class Piece extends Parent{
    public ImageView image = new ImageView();
    public Gate gate;
    public Integer i;
    public Integer j;
        
    public boolean isDraggable = true;
    public double mousex;				// where the cursor's x-position is
    public double mousey;				// where the cursor's y-position is
    private boolean dragging = false;	// whether the tile is currently being dragged
    private Square closest;
    private Text text = new Text();
	
    public Piece(Gate g){
        this.gate = g;
        try{ updateImage(); }
        catch (IllegalArgumentException ex) {}
	this.getChildren().add(image);
		
	text.setText(g.getClass().getSimpleName());
	text.setStrokeWidth(2);
	text.setStyle("-fx-font: 20px Arial;"
                	+ "-fx-font-weight: bold;"
			+ "-fx-fill: White;"
			+ "-fx-stroke: black;"
			+ "-fx-stroke-width: 1;");
	Bounds b = text.getLayoutBounds();
	text.setLayoutX(0);
	text.setLayoutY(ABEDGUI.tileSize);
	this.getChildren().add(text);
	getEvents();
    }
    
    public void updateImage(){
	image.setImage(resample(this.gate.getSprite()));
        image.setRotate(gate.rot*90);
	image.setFitHeight(ABEDGUI.tileSize);
        image.setFitWidth(ABEDGUI.tileSize);
        
    }
    
    //taken from https://gist.github.com/jewelsea/5415891
    private Image resample(Image input) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();
        final int S = 5;
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

    private void getEvents(){
        this.setOnMousePressed(event -> {
            mousex = event.getSceneX();
            mousey = event.getSceneY();
            //closest = ABEDGUI.getBoard().getClosest(getLayoutX(), getLayoutY());
            toFront();
            event.consume();
        });
		
        this.setOnMouseDragged(event -> {
            if(!isDraggable) return;
			
            dragging = true;
            setLayoutX(getLayoutX() + event.getSceneX() - mousex);
            setLayoutY(getLayoutY() + event.getSceneY() - mousey);
            mousex = event.getSceneX();
            mousey = event.getSceneY();
            //Square newClosest = ABEDGUI.getBoard().getClosest(getLayoutX(), getLayoutY());
            if(closest != newClosest){
                if(closest != null) closest.setFill(Square.defColor);
                closest = newClosest;
                if(newClosest != null) closest.setFill(Square.altColor);
            }
            event.consume();
        });
		
        this.setOnMouseReleased(event -> {
            if(!isDraggable) return;
            if(dragging) {
                dragging = false;
                changePos(closest);
            } else {
                switch(gate.getClass().getSimpleName()){
                    case "Input": gate.isOn ^= true;
                }
            }
            if(closest != null) closest.setFill(Square.defColor);
            //ABEDGUI.getBoard().currentGame.tick(this);
            event.consume();
        });
        
        this.setOnScroll(event -> {
            if(event.getDeltaY() > 0)
                gate.rotate(-1);
            else gate.rotate(1);
            //ABEDGUI.getBoard().currentGame.tick(this);
            event.consume();
        });
    }
    
    public void changePos(Square s){
        if(s == null){
            this.delPiece();
            System.out.println(ABEDGUI.getBoard().currentGame.toString());
            return;
        }
        
        Game temp = ABEDGUI.getBoard().currentGame;
        if(temp.placed[s.i][s.j] != null && temp.placed[s.i][s.j] != this)
            return;
        if(i != null && j != null)
            ABEDGUI.getBoard().currentGame.placed[i][j] = null;
        i = s.i;
        j = s.j;
        ABEDGUI.getBoard().currentGame.placed[s.i][s.j] = this;
        setLayoutX(s.x);
        setLayoutY(s.y);
    }
    
    public void delPiece(){
        try{ ABEDGUI.getBoard().currentGame.placed[i][j] = null;}
        catch(NullPointerException ex){}
        ABEDGUI.getBoard().root.getChildren().remove(this);

    }
}

package abedgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.text.Font;
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
    public Square closest;
    private Text text = new Text();
	
    public Piece(Gate g){
        this.gate = g;
        try{ updateImage(); }
        catch (IllegalArgumentException ex) {}
	this.getChildren().add(image);
		
	text.setText(g.getClass().getSimpleName().toUpperCase());
	text.setStrokeWidth(2);
	try {
		final Font f = Font.loadFont(new FileInputStream(new File("src/fonts/AuX DotBitC.ttf")), 24);
		text.setFont(f);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	text.setStyle("-fx-font-weight: bold;"
			+ "-fx-fill: #7DF9FF;"
			+ "-fx-stroke: black;"
			+ "-fx-stroke-width: 1;");
	//Bounds b = text.getLayoutBounds();
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
    public static Image resample(Image input) {
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
            Square newClosest = (Square)ABEDGUI.getBoard().getClosest(getLayoutX(), getLayoutY());
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
                //Q: Is this the best way to toggle a boolean?
                //A: HELL YEAH.
                    case "Input": ((Input)gate).isOn ^= true;
                }
            }
            if(closest != null) closest.setFill(Square.defColor);
            //if(i != null && j != null)
            	ABEDGUI.getBoard().currentGame.tick(gate);
            event.consume();
        });
        
        this.setOnScroll(event -> {
        	gate.rotate(event.getDeltaY() > 0? -1: 1);
            //if(i != null && j != null)
            	ABEDGUI.getBoard().currentGame.tick(gate);
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
        i = s.i; j = s.j;
        gate.i = s.i; gate.j = s.j;
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

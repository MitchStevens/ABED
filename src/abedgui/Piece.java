package abedgui;

import panes.GamePane;
import data.Reader;
import javafx.animation.RotateTransition;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Circuit;

public class Piece extends Parent{
	public static Font adbxtsc = Reader.loadFont("adbxtsc.ttf");
	
    public PieceImage image;
    public Circuit c;
        
    public boolean isDraggable = true;
    public static boolean isRotating = false;
    public double mousex;				// where the cursor's x-position is
    public double mousey;				// where the cursor's y-position is
    private boolean dragging = false;	// whether the tile is currently being dragged
    public Square closest;
    private Text gateName = new Text();
    private Text delete;
    private Text duplicate;
    private ImageView rotate = new ImageView();
    
	
    public Piece(Circuit c){
    	this.c = c;
    	image = new PieceImage(c);
        this.getChildren().add(image);
		
		duplicate = new Text("+");
		duplicate.setLayoutX(GamePane.tileSize-35);
		duplicate.setLayoutY(15);
		this.getChildren().add(duplicate);
		
		delete = new Text("×");
		delete.setLayoutX(GamePane.tileSize-20);
		delete.setLayoutY(15);
		this.getChildren().add(delete);
		
        gateName.setText(c.name);
        gateName.setFont(adbxtsc);
        gateName.setStrokeWidth(2);
		gateName.setStyle("-fx-font-weight: bold;");
		gateName.setLayoutX(0);
		gateName.setLayoutY(GamePane.tileSize);
		this.getChildren().add(gateName);
		
		rotate.setImage(PieceImage.resample(Circuit.loadedImages.get("rotateSymbol")));
		rotate.setFitHeight(30);
		rotate.setFitWidth(30);
		rotate.setLayoutX(GamePane.tileSize-30);
		rotate.setLayoutY(GamePane.tileSize-30);
		this.getChildren().add(rotate);
		
		setEvents();
    	this.setId(c.pos());
    }
    
    public void updateImage(){
    	image.update(c);
    }

    private void setEvents(){
        this.setOnMousePressed(event -> {
            mousex = event.getSceneX();
            mousey = event.getSceneY();
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
            Square newClosest = GamePane.getClosest(getLayoutX(), getLayoutY());
            if(closest != newClosest)
                closest = newClosest;
            event.consume();
        });
		
        this.setOnMouseReleased(event -> {
            if(!isDraggable) return;
            if(dragging) {
            	GamePane.movePiece(this, closest.i, closest.j);
                dragging = false;
            } else {
            	GamePane.togglePiece(this);
            }
            event.consume();
        });
        
        this.setOnScroll(event -> {
        	int rot = event.getDeltaY() < 0 ? 1 : -1;
        	GamePane.rotatePiece(c.i, c.j, rot);
            event.consume();
        });
        
        rotate.setOnMouseClicked(event -> {
        	GamePane.rotatePiece(c.i, c.j, -1);
            event.consume();
        });
        
        duplicate.setOnMouseClicked(event -> {
        	GamePane.addPiece(new Piece(c.clone()));
        	event.consume();
        });
        
        delete.setOnMouseClicked(event -> {
        	Gui.abedPane.removePiece(this);
        	event.consume();
        });
    }
    
    public void changePos(Square s){
    	GamePane.currentGame.move(c, s.i, s.j);
        setLayoutX(s.x);
        setLayoutY(s.y);
        updateImage();
    }
    
    public void changeSize(){
		duplicate.setLayoutX(GamePane.tileSize-35);
		
		delete.setLayoutX(GamePane.tileSize-20);
		
		gateName.setLayoutY(GamePane.tileSize);
		
		rotate.setLayoutX(GamePane.tileSize-30);
		rotate.setLayoutY(GamePane.tileSize-30);
    }

    @Override
    public String toString(){
    	return c.name+","+c.rot+","+c.pos();
    }
       
    @Override
    public Piece clone(){
    	return new Piece(c);
    }
    
    @Override
    public boolean equals(Object o){
    	if(!(o instanceof Piece)) return false;
    	Piece p = (Piece)o;
    	if(!this.c.equals(p.c)) return false;
    	if(this.getLayoutX() != p.getLayoutX()) return false;
    	if(this.getLayoutY() != p.getLayoutY()) return false;
    	return true;
    }
    
}
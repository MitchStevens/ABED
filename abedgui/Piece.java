package abedgui;

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
	
    public ImageView image = new ImageView();
    public Circuit c;
        
    public boolean isDraggable = true;
    public static boolean isRotating = false;
    public double mousex;				// where the cursor's x-position is
    public double mousey;				// where the cursor's y-position is
    private boolean dragging = false;	// whether the tile is currently being dragged
    public Square closest;
    private Text gateName = new Text();
    private Text gateNumber = new Text();
    private Text delete;
    private Text duplicate;
    
	
    public Piece(Circuit c){
    	this.c = c;
        try{ updateImage(); }
        catch (IllegalArgumentException ex) {}
        this.getChildren().add(image);
		
		duplicate = new Text("✚ ");
		duplicate.setLayoutX(Gui.tileSize-35);
		duplicate.setLayoutY(15);
		this.getChildren().add(duplicate);
		
		delete = new Text("✖");
		delete.setLayoutX(Gui.tileSize-20);
		delete.setLayoutY(15);
		this.getChildren().add(delete);
		
        gateName.setText(c.name);
        gateName.setFont(adbxtsc);
        gateName.setStrokeWidth(2);
		gateName.setStyle("-fx-font-weight: bold;");
		gateName.setLayoutX(0);
		gateName.setLayoutY(Gui.tileSize);
		
//		if("Input".equals(g.name)){
//			gateNumber.setText(((Input)gate).inputNum+"");}
//		else if("Output".equals(g.name)){
//			gateNumber.setText(((Output)gate).outputNum+"");}
//		else gateNumber.setText("");
		gateNumber.setFont(adbxtsc);
        gateNumber.setStrokeWidth(2);
		gateNumber.setStyle("-fx-font-weight: bold; -fx-font-size: 25;");
		gateNumber.setLayoutX(5);
		gateNumber.setLayoutY(20);
	
		this.getChildren().add(gateNumber);
		this.getChildren().add(gateName);
		setEvents();
    }
    
    public void updateImage(){
    	image.setImage(c.getSprite());
        image.setRotate(c.rot*90);
        image.setFitHeight(Gui.tileSize);
        image.setFitWidth(Gui.tileSize);
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
            Square newClosest = Gui.getBoard().getClosest(getLayoutX(), getLayoutY());
            if(closest != newClosest){
                closest = newClosest;
                if(closest != null) closest.flash();
            }
            event.consume();
        });
		
        this.setOnMouseReleased(event -> {
            if(!isDraggable) return;
            if(dragging) {
            	Gui.movePiece(this, closest.i, closest.j);
                dragging = false;
            } else {
            	this.c.toggle();
            	System.out.println(Gui.currentGame.printGame());
            }
            Gui.updateBoard();
            event.consume();
        });
        
        this.setOnScroll(event -> {
//        	if(isRotating) return;
//        	System.out.println("roting");
//        	isRotating = true;
            //if(i != null && j != null)
            
//            RotateTransition rt = new RotateTransition(Duration.millis(300), image);
//            rt.setByAngle(90);
//            rt.play();
        	int rot = event.getDeltaY() < 0 ? 1 : -1;
        	Gui.rotatePiece(c.i, c.j, rot);
            //image.setRotate(c.rot*90);
            event.consume();
//        	isRotating = false;
        });
        
        duplicate.setOnMouseClicked(event -> {
        	Gui.addPiece(this.clone());
        	event.consume();
        });
        
        delete.setOnMouseClicked(event -> {
        	Gui.removePiece(this);
        	event.consume();
        });
    }
    
    public void changePos(Square s){       
        Gui.currentGame.move(c, s.i, s.j);
        setLayoutX(s.x);
        setLayoutY(s.y);
        updateImage();
    }

    @Override
    public String toString(){
    	return c.name+","+c.rot+",";
    }
       
    @Override
    public Piece clone(){
    	return new Piece(c);
    }
}
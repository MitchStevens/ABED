package abedgui;

import data.Reader;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import logic.Circuit;

public class Piece extends Parent{
	public static Font adbxtsc = Reader.loadFont("adbxtsc.ttf");
	
    public ImageView image = new ImageView();
    public Circuit c;
        
    public boolean isDraggable = true;
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
    	image.setImage(resample(c.getSprite()));
        image.setRotate(c.rot*90);
        image.setFitHeight(Gui.tileSize);
        image.setFitWidth(Gui.tileSize);
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

    private void setEvents(){
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
            	changePos(closest);
                dragging = false;
            } else {
            	this.c.toggle();
            	System.out.println(Gui.currentGame.printGame());
            	this.updateImage();
            }
            //if(i != null && j != null)
            event.consume();
        });
        
//        this.setOnKeyPressed(event ->{
//        	if(event.getCode() == KeyCode.R){
//        		gate.rotate(1);
//        		ABEDGUI.getBoard().currentGame.tick(gate);
//        	}
//        });
        
        this.setOnScroll(event -> {
        	c.addRot(event.getDeltaY() > 0? -1: 1);
            //if(i != null && j != null)
            c.game.updateGame(c.i, c.j);
            updateImage();
            event.consume();
        });
        
        duplicate.setOnMouseClicked(event -> {
        	Gui.currentGame.add(c.clone());
        });
        
        delete.setOnMouseClicked(event -> {
        	Gui.removePiece(this);
        	Gui.currentGame.remove(c.i, c.j);
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
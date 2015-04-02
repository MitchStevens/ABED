package abedgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private Text gateName = new Text();
    private Text gateNumber = new Text();
    private Text delete;
    private Text duplicate;
    
	
    public Piece(Gate g){
    	this.gate = g;
    	this.i = g.i; this.j = g.j;
        try{ updateImage(); }
        catch (IllegalArgumentException ex) {}
        this.getChildren().add(image);
		
        Font f = Font.font("Comic Sans");
		try {f = Font.loadFont(new FileInputStream(new File("src/fonts/adbxtsc.ttf")), 18);}
		catch (FileNotFoundException e) {e.printStackTrace();}
		
		duplicate = new Text("✚ ");
		duplicate.setLayoutX(ABEDGUI.tileSize-35);
		duplicate.setLayoutY(15);
		this.getChildren().add(duplicate);
		
		delete = new Text("✖");
		delete.setLayoutX(ABEDGUI.tileSize-20);
		delete.setLayoutY(15);
		this.getChildren().add(delete);
		
        gateName.setText(g.name);
        gateName.setFont(f);
        gateName.setStrokeWidth(2);
		gateName.setStyle("-fx-font-weight: bold;");
		gateName.setLayoutX(0);
		gateName.setLayoutY(ABEDGUI.tileSize);
		
		if("Input".equals(g.name)){
			gateNumber.setText(((Input)gate).inputNum+"");}
		else if("Output".equals(g.name)){
			gateNumber.setText(((Output)gate).outputNum+"");}
		else gateNumber.setText("");
		gateNumber.setFont(f);
        gateNumber.setStrokeWidth(2);
		gateNumber.setStyle("-fx-font-weight: bold; -fx-font-size: 25;");
		gateNumber.setLayoutX(5);
		gateNumber.setLayoutY(20);
	
		this.getChildren().add(gateNumber);
		this.getChildren().add(gateName);
		setEvents();
    }
    
    public void updateImage(){
    	//image.setImage(resample(this.gate.getSprite()));
        image.setRotate(gate.rot*90);
        image.setFitHeight(ABEDGUI.tileSize);
        image.setFitWidth(ABEDGUI.tileSize);
        ABEDGUI.getBoard().currentGame.printGameInfo();
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
            Square newClosest = (Square)ABEDGUI.getBoard().getClosest(getLayoutX(), getLayoutY());
            if(closest != newClosest){
                closest = newClosest;
                if(closest != null) closest.flash();
            }
            ABEDGUI.getBoard().updateGraphics();
            event.consume();
        });
		
        this.setOnMouseReleased(event -> {
            if(!isDraggable) return;
            if(dragging) {
            	ABEDGUI.getBoard().currentGame.placed[i][j] = null;
                dragging = false;
                changePos(closest);
            } else {
                switch(gate.name){
                //Q: Is this the best way to toggle a boolean?
                //A: HELL YEAH.
                    case "Input": ((Input)gate).isOn ^= true;
                }
            }
            //if(i != null && j != null)
            ABEDGUI.getBoard().currentGame.placed[i][j].tick();
            ABEDGUI.getBoard().currentGame.printGameInfo();
            ABEDGUI.getBoard().updateGraphics();
            event.consume();
        });
        
//        this.setOnKeyPressed(event ->{
//        	if(event.getCode() == KeyCode.R){
//        		gate.rotate(1);
//        		ABEDGUI.getBoard().currentGame.tick(gate);
//        	}
//        });
        
        this.setOnScroll(event -> {
        	gate.rotate.accept(event.getDeltaY() > 0? -1: 1);
            //if(i != null && j != null)
            gate.tick();
            ABEDGUI.getBoard().updateGraphics();
            event.consume();
        });
        
        duplicate.setOnMouseClicked(event -> {
        	ABEDGUI.getBoard().addPiece((Gate)gate.clone());
        });
        
        delete.setOnMouseClicked(event -> {
        	this.changePos(null);
        	event.consume();
        });
    }
    
    public void changePos(Square s){        
        if(s == null){
        	ABEDGUI.getBoard().root.getChildren().remove(this);
            return;
        }
        
        i = s.i; j = s.j;
        gate.i = s.i; gate.j = s.j;
        
        ABEDGUI.getBoard().currentGame.placed[s.i][s.j] = this.gate;
        setLayoutX(s.x);
        setLayoutY(s.y);
    }

    @Override
    public String toString(){
    	return gate.name+","+gate.rot+",";
    }
    
    
    @Override
    public Piece clone(){
    	return new Piece((Gate) Gate.allGates.get(gate.name).clone());
    }
}

package abedgui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import logic.Circuit;

public class Piece extends Parent{
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
		
        Font f = Font.font("Comic Sans");
		try {f = Font.loadFont(new FileInputStream(new File("src/fonts/adbxtsc.ttf")), 18);}
		catch (FileNotFoundException e) {e.printStackTrace();}
		
		duplicate = new Text("✚ ");
		duplicate.setLayoutX(Gui.tileSize-35);
		duplicate.setLayoutY(15);
		this.getChildren().add(duplicate);
		
		delete = new Text("✖");
		delete.setLayoutX(Gui.tileSize-20);
		delete.setLayoutY(15);
		this.getChildren().add(delete);
		
        gateName.setText(c.name);
        gateName.setFont(f);
        gateName.setStrokeWidth(2);
		gateName.setStyle("-fx-font-weight: bold;");
		gateName.setLayoutX(0);
		gateName.setLayoutY(Gui.tileSize);
		
//		if("Input".equals(g.name)){
//			gateNumber.setText(((Input)gate).inputNum+"");}
//		else if("Output".equals(g.name)){
//			gateNumber.setText(((Output)gate).outputNum+"");}
//		else gateNumber.setText("");
		gateNumber.setFont(f);
        gateNumber.setStrokeWidth(2);
		gateNumber.setStyle("-fx-font-weight: bold; -fx-font-size: 25;");
		gateNumber.setLayoutX(5);
		gateNumber.setLayoutY(20);
	
		this.getChildren().add(gateNumber);
		this.getChildren().add(gateName);
		//setEvents();
    }
    
    public void updateImage(){
    	//image.setImage(resample(this.gate.getSprite()));
        image.setRotate(c.rot*90);
        image.setFitHeight(Gui.tileSize);
        image.setFitWidth(Gui.tileSize);
        Gui.getBoard().currentGame.printGameInfo();
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
            Gui.getBoard().updateGraphics();
            event.consume();
        });
		
        this.setOnMouseReleased(event -> {
            if(!isDraggable) return;
            if(dragging) {
            	Gui.getBoard().currentGame.tileGrid[c.i][c.j] = null;
                dragging = false;
                changePos(closest);
            } else {
            	
            }
            //if(i != null && j != null)
            Gui.getBoard().currentGame.printGameInfo();
            Gui.getBoard().updateGraphics();
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
            Gui.getBoard().updateGraphics();
            event.consume();
        });
        
        duplicate.setOnMouseClicked(event -> {
        	Gui.getBoard().currentGame.add(c.clone());
        });
        
        delete.setOnMouseClicked(event -> {
        	this.changePos(null);
        	event.consume();
        });
    }
    
    public void changePos(Square s){          
        c.i = s.i; c.j = s.j;
        
        Gui.getBoard().currentGame.move(c, s.i, s.j);
        setLayoutX(s.x);
        setLayoutY(s.y);
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
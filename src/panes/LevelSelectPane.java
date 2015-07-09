package panes;

import abedgui.Gui;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import logic.Circuit;

public class LevelSelectPane extends Pane {
	int numLevels = 5;
	int curr = 0;
	double xCentre, yCentre;
	String[] levelNames = new String[]{
			"lvl1", "lvl2", "lvl3", "lvl4", "lvl5"
	};
	double[] xpos, ypos;
	ImageView[] levelImages;
	Label title;
	
	public LevelSelectPane(){
		xCentre = Gui.boardWidth/2;
		yCentre = Gui.boardHeight/2;
		xpos = new double[]{-100, 10, xCentre -300/2, Gui.boardHeight -110, Gui.boardWidth};
		ypos = new double[]{yCentre -50, yCentre -50, yCentre -150, yCentre -50, yCentre -50};
		
		
		levelImages = new ImageView[numLevels];
		for(int i = 0; i < numLevels; i++){
			levelImages[i] = new ImageView(Circuit.loadedImages.get("lvl_select"));
			levelImages[i].setFitHeight(100);
			levelImages[i].setFitWidth(100);
			this.getChildren().add(levelImages[i]);
		}
		title = new Label("TITLE");
		title.setPrefWidth(300);
		title.setLayoutX(xCentre - 300/2);
		title.setLayoutY(yCentre + 300/2 + 10);
		title.setAlignment(Pos.CENTER);
		title.setBackground(Background.EMPTY);
		this.getChildren().add(title);
		
		setCurrentLevel(0);
		System.out.println(this.getChildren());
		
		setListeners();
	}
	
	private void setCurrentLevel(int lvl){
		this.curr = lvl;
		this.title.setText(levelNames[lvl]);
		
		for(int i = 0; i < lvl -1; i++)
			setDimAndPos(i, 100, xpos[0], ypos[0]);
		
		if(lvl > 0)
			setDimAndPos(lvl-1, 100, xpos[1], ypos[1]);
		
		setDimAndPos(lvl, 300, xpos[2], ypos[2]);
		
		if(lvl < numLevels-1)
			setDimAndPos(lvl+1, 100, xpos[3], ypos[3]);
		
		for(int i = lvl+2; i < numLevels; i++)
			setDimAndPos(i, 100, xpos[4], ypos[4]);
	}
	
	private void pushNext(){
		System.out.println("next");
		if(curr == numLevels-1) return;
        TranslateTransition tt2 = new TranslateTransition(Duration.millis(800), levelImages[curr]);
        tt2.setToX(-xCentre +60);
        tt2.setToY(0);
        ScaleTransition st2 = new ScaleTransition(Duration.millis(800), levelImages[curr]);
        st2.setToX(1/3f);
        st2.setToY(1/3f);
        
	    
        TranslateTransition tt3 = new TranslateTransition(Duration.millis(800), levelImages[curr+1]);
        tt3.setToX(xpos[2]);
        tt3.setToY(ypos[2]);
        ScaleTransition st3 = new ScaleTransition(Duration.millis(800), levelImages[curr+1]);
        st3.setToX(3f);
        st3.setToY(3f);
        
        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(
        		tt2, st2,
        		tt3, st3);
        
//        if(curr >= 2){
//        	setDimAndPos(curr-2, 100, -100, yCentre - 100/2);
//            TranslateTransition tt1 = new TranslateTransition(Duration.millis(800), levelImages[curr+2]);
//            tt1.setToX(0);
//            tt1.setToY(0);
//            pt.getChildren().add(tt1);
//        }
//        
//        if(curr < numLevels-3){
//        	setDimAndPos(curr+2, 100, Gui.boardWidth, yCentre - 100/2);
//            TranslateTransition tt4 = new TranslateTransition(Duration.millis(800), levelImages[curr+2]);
//            tt4.setToX(-110);
//            tt4.setToY(0);
//            pt.getChildren().add(tt4);
//        }
        
        curr++;
        title.setText(levelNames[curr]);
        pt.play();
	}
	
	private void pushPrev(){
		System.out.println("prev");
		if(curr == 0) return;
	}
	
	
	
	private void setDimAndPos(int lvlImageNum, double size, double x, double y){
		levelImages[lvlImageNum].setFitHeight(size);
		levelImages[lvlImageNum].setFitWidth(size);
		levelImages[lvlImageNum].setLayoutX(x);
		levelImages[lvlImageNum].setLayoutY(y);
	}
	
	private void setListeners(){
		this.setOnScroll(e -> {
			if(e.getDeltaX() > 0 || e.getDeltaY() < 0)
				pushNext();
			else if(e.getDeltaX() < 0 || e.getDeltaY() > 0)
				pushPrev();
		});
	}
}

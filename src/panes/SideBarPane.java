package panes;

import data.Reader;
import abedgui.Gui;
import abedgui.Incrementor;
import abedgui.Piece;
import abedgui.PieceImage;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import logic.Circuit;
import logic.Game;

public class SideBarPane extends GridPane {
	private final double ICON_SIZE = 30;
	
	public static SideBarPane sb;
	public static Incrementor inc;
	public static double defWidth;
	public static Font adbxtsc = Reader.loadFont("adbxtsc.ttf");
	
//	{
//		sb.setStyle(value);
//	}
	
	public SideBarPane(){
		sb = this;
		sb.setId("main");
		sb.setHgap(10); //horizontal gap in pixels => that's what you are asking for
		sb.setVgap(10); //vertical gap in pixels
		this.setPrefWidth(Gui.SIDE_BAR_WIDTH);
		defWidth = Gui.SIDE_BAR_WIDTH;
		this.getStylesheets().add("css/SideBarGui.css");
		this.setPrefWidth(defWidth);
		
    	TreeItem<Node> root = new TreeItem<>(new Label("Gates"));
        
        for(int i = 0; i < Circuit.circuitTypes.length; i++){
    		Label headLabel = new Label(Circuit.circuitTypes[i]);
    		headLabel.setFont(adbxtsc);
    		root.getChildren().add(new TreeItem<>(headLabel));
        }
        
        for(Circuit c : Circuit.loadedCircuits.values()){
        	HBox hbox = new HBox();
        	
        	Label l = new Label(c.name);
        	l.setFont(adbxtsc);
        	l.setAlignment(Pos.CENTER_LEFT);
        	
            Image img = Circuit.loadedImages.get(c.name);
            if(img != null){
            	ImageView view = new ImageView(img);
            	view.setFitHeight(ICON_SIZE);
            	view.setFitWidth(ICON_SIZE);
            	l.setGraphic(view);
            }
            
        	hbox.getChildren().add(l);
        	
        	hbox.setOnMouseClicked(e -> {
        		GamePane.addPiece(new Piece(c.clone()));
          	});
        	
            TreeItem<Node> label = new TreeItem<>(hbox);
            
            root.getChildren().get(c.type).getChildren().add(label);
        }
        
        TreeView<Node> gateSelector = new TreeView<>(root);
        gateSelector.setShowRoot(false);
        gateSelector.setPrefSize(defWidth, 300);
        sb.add(gateSelector, 0, 0, 2, 1);
        
        ComboBox<String> cmb = new ComboBox<>();
        cmb.setPrefWidth(defWidth/2);
        cmb.setId("combobox");
        cmb.getItems().addAll(Game.loadedGames.keySet());
        cmb.setOnAction(e -> {
        	GamePane.newGame(Game.loadedGames.get(cmb.getValue()));
        });
        sb.add(new Label("Set Game"), 0, 1);
        GridPane.setHalignment(cmb, HPos.RIGHT);
        sb.add(cmb, 1, 1);
        
        inc = new Incrementor(Game.MIN_TILES, Game.MAX_TILES);
        inc.setWidth1(defWidth/2);
        inc.setOnInc(e -> {
        	GamePane.incSize(GamePane.numTiles+1);
        });
        inc.setOnDec(e -> {
        	GamePane.decSize(GamePane.numTiles-1);
        });
        inc.set(GamePane.numTiles);
        inc.setAlignment(Pos.CENTER_RIGHT);
        sb.add(new Label("Set Size"), 0, 2);
        sb.add(inc, 1, 2);
        
        ComboBox<String> cmbo = new ComboBox<>();
        cmbo.setPrefWidth(defWidth/2);
        cmbo.setId("combobox");
        cmbo.getItems().addAll(Circuit.loadedCircuits.keySet());
        cmbo.setOnAction(e -> {
        	GamePane.levelObjective = Circuit.loadedCircuits.get(cmbo.getValue());
        });
        sb.add(new Label("Set Objective"), 0, 3);
        GridPane.setHalignment(cmbo, HPos.RIGHT);
        sb.add(cmbo, 1, 3);
        
        Button b1 = new Button("Gate toCircuit()");
        b1.setPrefWidth(defWidth);
        b1.setOnMouseClicked(e -> {
        	Circuit c = GamePane.currentGame.toCircuit();
        	System.out.println(c.evals.get(0).logic);
        });
        sb.add(b1, 0, 4, 2, 1);
        SideBarPane.setColumnSpan(b1, 2);
        
        Button b2 = new Button("Gate toString()");
        b2.setPrefWidth(defWidth);
        b2.setOnMouseClicked(e -> {
        	System.out.println(GamePane.currentGame.toString());
        });
        sb.add(b2, 0, 5, 2, 1);
        SideBarPane.setColumnSpan(b2, 2);
        
        Button b3 = new Button("isLevelComplete");
        b3.setPrefWidth(defWidth);
        b3.setOnMouseClicked(e -> {
        	System.out.println(GamePane.isLevelComplete());
        });
        sb.add(b3, 0, 6, 2, 1);
        SideBarPane.setColumnSpan(b3, 2);
        
        Button b4 = new Button("Print Game");
        b4.setPrefWidth(defWidth);
        b4.setOnMouseClicked(e -> {
        	System.out.println(GamePane.currentGame.printGame());
        });
        sb.add(b4, 0, 7, 2, 1);
        SideBarPane.setColumnSpan(b4, 2);
        
        Button b5 = new Button("Clear Game");
        b5.setPrefWidth(defWidth);
        b5.setOnMouseClicked(e -> {
        	GamePane.newGame(new Game(GamePane.numTiles));
        });
        sb.add(b5, 0, 8, 2, 1);
        SideBarPane.setColumnSpan(b5, 2);
	}
	
}

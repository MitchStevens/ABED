package panes;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import logic.Circuit;
import logic.Game;

public class SideBarPane extends GridPane {
	public static SideBarPane sb;
	public static Incrementor inc;
	public static double defWidth;
	
	public SideBarPane(){
		sb = this;
		sb.setId("main");
		sb.setHgap(10); //horizontal gap in pixels => that's what you are asking for
		sb.setVgap(10); //vertical gap in pixels
		defWidth = Gui.boardWidth - Gui.boardHeight;
		this.getStylesheets().add("css/SideBarGui.css");
		this.setPrefWidth(defWidth);
    	TreeItem<Label> root = new TreeItem<>(new Label("Gates"));
        
        for(int i = 0; i < Circuit.circuitTypes.length; i++)
        	root.getChildren().add(new TreeItem<>(new Label(Circuit.circuitTypes[i])));
        
        for(Circuit c : Circuit.loadedCircuits.values()){
        	Label l = new Label(c.name);
            	
        	l.setOnMouseClicked(e -> {
        		GamePane.addPiece(new Piece(c.clone()));
          	});
            
        	l.setOnMouseEntered(e -> {
            	l.setFont(new Font(l.getFont().getSize() +2));
      		});
            
        	l.setOnMouseExited(e -> {
        		l.setFont(new Font(l.getFont().getSize() -2));
        	});
        		
            TreeItem<Label> label = new TreeItem<>(l);
            	
            label.setGraphic(new PieceImage(c, 20));
            	
            root.getChildren().get(c.type).getChildren().add(label);
        }
        
        TreeView<Label> gateSelector = new TreeView<>(root);
        gateSelector.setShowRoot(false);
        gateSelector.setPrefSize(defWidth, 300);
        sb.add(gateSelector, 0, 0, 2, 1);
        
        ComboBox<String> cmb = new ComboBox<>();
        cmb.setId("combobox");
        cmb.getItems().addAll(Game.loadedGames.keySet());
        cmb.setOnAction(e -> {
        	GamePane.newGame(Game.loadedGames.get(cmb.getValue()));
        });
        sb.add(new Label("Set Game"), 0, 1);
        GridPane.setHalignment(cmb, HPos.RIGHT);
        sb.add(cmb, 1, 1);
        
        inc = new Incrementor(Game.MIN_TILES, Game.MAX_TILES);
        inc.setOnInc(e -> {
        	GamePane.incSize();
        });
        inc.setOnDec(e -> {
        	GamePane.decSize();
        });
        inc.set(GamePane.numTiles);
        inc.setAlignment(Pos.CENTER_RIGHT);
        sb.add(new Label("Set Size"), 0, 2);
        sb.add(inc, 1, 2);
        
        ComboBox<String> cmbo = new ComboBox<>();
        cmbo.setId("combobox");
        cmbo.getItems().addAll(Circuit.loadedCircuits.keySet());
        cmbo.setOnAction(e -> {
        	GamePane.levelObjective = Circuit.loadedCircuits.get(cmbo.getValue());
        });
        sb.add(new Label("Set Objective"), 0, 3);
        GridPane.setHalignment(cmbo, HPos.RIGHT);
        sb.add(cmbo, 1, 3);
        
        Button b1 = new Button("Gate toString()");
        b1.setPrefWidth(defWidth);
        b1.setOnMouseClicked(e -> {
        	Circuit c = GamePane.currentGame.toCircuit();
        });
        sb.add(b1, 0, 4, 2, 1);
        SideBarPane.setRowSpan(b1, 2);
        
        Button b2 = new Button("Save Gate");
        b2.setPrefWidth(defWidth);
        b2.setOnMouseClicked(e -> {
        	System.out.println(GamePane.currentGame.toString());
        });
        sb.add(b2, 0, 6, 2, 1);
        SideBarPane.setRowSpan(b2, 2);
	}
	
}

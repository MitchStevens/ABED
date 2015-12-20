package panes;

import graphics.Piece;
import graphics.PieceImage;

import java.util.ArrayList;
import java.util.List;

import controls.Incrementor;
import circuits.Circuit;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.Game;
import logic.Level;
import logic.Reader;

public class SideBarPane extends GridPane implements SetChangeListener<Circuit> {
	private final double 			ICON_SIZE 	= 20;

	public static SideBarPane 		sb;
	public static Incrementor 		inc;
	public static TreeView<Node> 	gateSelector;
	public static TextArea 			textArea;
	public static double 			defWidth;
	public static final Font 		DEF_FONT 	= Reader.loadFont("DejaVuSans-ExtraLight.ttf", 15);

	public SideBarPane() {
		sb = this;
		sb.setId("main");
		sb.setHgap(10); // horizontal gap in pixels
		sb.setVgap(10); // vertical gap in pixels
		this.setPrefWidth(Gui.SIDE_BAR_WIDTH);
		defWidth = Gui.SIDE_BAR_WIDTH;
		this.getStylesheets().add("res/css/SideBarGui.css");
		this.setPrefWidth(defWidth);
		CircuitPane.unlockedCircuits.addListener(this);

		updateCircuits();
		
		ComboBox<String> cmb = new ComboBox<>();
		cmb.setPrefWidth(defWidth / 2);
		cmb.setId("combobox");
		cmb.getItems().addAll(Game.ALL_GAMES.keySet());
		cmb.setOnAction(e -> {
			CircuitPane.newGame(Game.ALL_GAMES.get(cmb.getValue()));
		});
		sb.add(new Label("Set Game"), 0, 1);
		GridPane.setHalignment(cmb, HPos.RIGHT);
		sb.add(cmb, 1, 1);

		inc = new Incrementor(Game.MIN_TILES, Game.MAX_TILES);
		inc.setWidth1(defWidth / 2);
		inc.setOnInc(e -> {
			CircuitPane.incSize(CircuitPane.numTiles + 1);
		});
		inc.setOnDec(e -> {
			CircuitPane.decSize(CircuitPane.numTiles - 1);
		});
		inc.set(CircuitPane.numTiles);
		inc.setAlignment(Pos.CENTER_RIGHT);
		sb.add(new Label("Set Size"), 0, 2);
		sb.add(inc, 1, 2);
		
		textArea = new TextArea();
		textArea.setFont(DEF_FONT);
		textArea.setPrefRowCount(6);
		textArea.setWrapText(true);
		textArea.setEditable(false);
		sb.add(textArea, 0, 3);
		SideBarPane.setColumnSpan(textArea, 2);

		Button b1 = new Button("Gate toCircuit()");
		b1.setPrefWidth(defWidth);
		b1.setOnMouseClicked(e -> {
			Circuit c = CircuitPane.currentGame.toCircuit();
			System.out.println(c.toString());
		});
		sb.add(b1, 0, 5, 2, 1);
		SideBarPane.setColumnSpan(b1, 2);

		Button b2 = new Button("Gate toString()");
		b2.setPrefWidth(defWidth);
		b2.setOnMouseClicked(e -> {
			System.out.println(CircuitPane.currentGame.toString());
		});
		sb.add(b2, 0, 6, 2, 1);
		SideBarPane.setColumnSpan(b2, 2);

		Button b3 = new Button("isLevelComplete");
		b3.setPrefWidth(defWidth);
		b3.setOnMouseClicked(e -> {
			System.out.println(CircuitPane.currentLevel.isComplete(CircuitPane.currentGame));
		});
		sb.add(b3, 0, 7, 2, 1);
		SideBarPane.setColumnSpan(b3, 2);

		Button b4 = new Button("Print Game");
		b4.setPrefWidth(defWidth);
		b4.setOnMouseClicked(e -> {
			CircuitPane.currentGame.printGame();
		});
		sb.add(b4, 0, 8, 2, 1);
		SideBarPane.setColumnSpan(b4, 2);

		Button b5 = new Button("Clear Game");
		b5.setPrefWidth(defWidth);
		b5.setOnMouseClicked(e -> {
			CircuitPane.newGame(new Game(CircuitPane.numTiles));
		});
		sb.add(b5, 0, 9, 2, 1);
		SideBarPane.setColumnSpan(b5, 2);
	}
	
	public void updateCircuits(){
		if(CircuitPane.unlockAllCircuits)
			for(Circuit c : Circuit.allCircuits.values())
				CircuitPane.unlockedCircuits.add(c);
		
		TreeItem<Node> root = new TreeItem<>(new Label("Gates"));
		
		List<TreeItem<Node>> headers = new ArrayList<>(Level.LEVEL_TITLES.size());
		for(int i = 0; i < Level.LEVEL_TITLES.size(); i++) headers.add(null);
		
		for(Circuit c : CircuitPane.unlockedCircuits)
			if(headers.get(c.type) == null){
				Label headLabel = new Label(Level.LEVEL_TITLES.get(c.type));
				headLabel.setFont(DEF_FONT);
				headers.set(c.type, new TreeItem<>(headLabel));
			}

		for (Circuit c : CircuitPane.unlockedCircuits) {
			HBox hbox = new HBox();
			if(CircuitPane.new_circuits.contains(c))
				hbox.setStyle("-fx-background-color: DDFEFE;");
			
			Label l = new Label(c.name);
			l.setFont(DEF_FONT);
			l.setAlignment(Pos.CENTER_LEFT);

			Image img = PieceImage.ALL_IMAGES.get(c.name);
			if (img != null) {
				ImageView view = new ImageView(img);
				view.setFitHeight(ICON_SIZE);
				view.setFitWidth(ICON_SIZE);
				l.setGraphic(view);
			}

			hbox.getChildren().add(l);

			hbox.setOnMouseClicked(e -> {
				CircuitPane.addPiece(new Piece(c.clone()));
			});

			TreeItem<Node> label = new TreeItem<>(hbox);

			if(headers.get(c.type) == null){
				Label headLabel = new Label(Level.LEVEL_TITLES.get(c.type));
				headLabel.setFont(DEF_FONT);
				root.getChildren().add(new TreeItem<>(headLabel));
			}
			
			headers.get(c.type).getChildren().add(label);
		}
		
		for(TreeItem<Node> n : headers)
			if(n != null)
				root.getChildren().add(n);
		
		gateSelector = new TreeView<>(root);
		gateSelector.setShowRoot(false);
		gateSelector.setPrefSize(defWidth, 300);
		sb.add(gateSelector, 0, 0, 2, 1);
	}

	@Override
	public void onChanged( SetChangeListener.Change<? extends Circuit> arg0) {
		updateCircuits();
	}

}

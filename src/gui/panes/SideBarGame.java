package gui.panes;

import java.util.ArrayList;
import java.util.List;

import core.eval.Operation;
import core.game.Game;
import core.logic.Level;
import data.Reader;
import data.Writer;
import gui.controls.Incrementor;
import gui.graphics.NodePiece;
import gui.graphics.PieceImage;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SideBarGame extends VBox {
	private final double 			ICON_SIZE 	= 20;

	public static SideBarGame 		sbg;
	public static Incrementor 		inc;
	public static TreeView<Node> 	gateSelector;
	public static TextArea 			textArea;
	public static double 			defWidth;
	public static final Font 		DEF_FONT 	= Reader.loadFont("DejaVuSans-ExtraLight.ttf", 15);

	public SideBarGame() {
		sbg = this;
		sbg.setId("sidebar game");
		sbg.setPadding(new Insets(20));
		defWidth = Gui.SIDE_BAR_WIDTH;
		sbg.setPrefWidth(defWidth);
		sbg.getStylesheets().add("res/css/SideBarGui.css");
		sbg.setPrefWidth(defWidth);
		sbg.setSpacing(10);
		//Reader.unlocked_circuits.addListener(this);

		gateSelector = new TreeView<>();
		gateSelector.setShowRoot(false);
		gateSelector.setPrefSize(defWidth, 300);
		sbg.getChildren().add(gateSelector);
		update_operations();

		BorderPane box = new BorderPane();
		
		inc = new Incrementor(Game.MIN_TILES, Game.MAX_TILES);
		inc.setWidth1(defWidth / 2);
		inc.setOnInc(e -> {
			CircuitPane.inc_size(CircuitPane.num_tiles + 1);
		});
		inc.setOnDec(e -> {
			CircuitPane.dec_size(CircuitPane.num_tiles - 1);
		});
		inc.set(CircuitPane.num_tiles);
		inc.setAlignment(Pos.CENTER_RIGHT);
		Label l = new Label("Set Size");
		l.setAlignment(Pos.CENTER_LEFT);
		box.setLeft(l);
		box.setRight(inc);
		box.prefWidth(defWidth);
		sbg.getChildren().add(box);

		Button b1 = new Button("Clear Game");
		b1.setPrefWidth(defWidth);
//		b1.setOnMouseClicked(e -> {
//			CircuitPane.game.clear();
//		});
		sbg.getChildren().add(b1);

		Button b2 = new Button("Back to Menu");
		b2.setPrefWidth(defWidth);
		b2.setOnMouseClicked(e -> {
			Gui.set_pane("level_select_pane");
		});
		sbg.getChildren().add(b2);
	}
	
	public void update_operations(){
//		for(Operation op : Reader.MAPPINGS.values())
//			Reader.unlocked_circuits.add(c);
		
		TreeItem<Node> root = new TreeItem<>(new Label("Operations"));

		for (Operation op : Reader.MAPPINGS.values()) {
			HBox hbox = new HBox();
			
			Label l = new Label(op.get_name());
			l.setFont(DEF_FONT);
			l.setAlignment(Pos.CENTER_LEFT);

			Image img = Reader.IMAGES.get(op.get_name());
			if (img != null) {
				ImageView view = new ImageView(img);
				view.setFitHeight(ICON_SIZE);
				view.setFitWidth(ICON_SIZE);
				l.setGraphic(view);
			}

			hbox.getChildren().add(l);

			hbox.setOnMouseClicked(e -> {
				System.out.println(op.get_name());
				CircuitPane.add_piece(op.clone());
			});

			TreeItem<Node> label = new TreeItem<>(hbox);
			root.getChildren().add(label);
		}
		
		gateSelector.setRoot(root);
	}
}

package gui.panes;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import data.Reader;

public class SideBarInfo extends VBox {
	public static SideBarInfo 		sbi;
	public static double 			defWidth;
	public static final Font 		DEF_FONT 	= Reader.loadFont("DejaVuSans-ExtraLight.ttf", 15);
	public static TextArea textArea;

	public SideBarInfo() {
		sbi = this;
		sbi.setId("sidebar info");
		sbi.setPadding(new Insets(20));
		defWidth = Gui.SIDE_BAR_WIDTH;
		sbi.setPrefWidth(defWidth);
		sbi.getStylesheets().add("res/css/SideBarGui.css");
		sbi.setPrefWidth(defWidth);
		sbi.setSpacing(10);
		
		textArea = new TextArea();
		textArea.setFont(DEF_FONT);
		textArea.setPrefRowCount(12);
		textArea.setWrapText(true);
		textArea.setEditable(false);
		sbi.getChildren().add(textArea);
		
		Button b = new Button("Get Hint");
		b.setPrefWidth(defWidth);
		b.setOnMouseClicked(e -> {
			System.out.println("HINT");
		});
		sbi.getChildren().add(b);
	}
}

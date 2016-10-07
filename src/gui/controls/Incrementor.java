package controls;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class Incrementor extends HBox {
	Label inc, dec;
	Label lbl;
	int min, max, num;

	public Incrementor(int min, int max) {
		this.min = min;
		this.max = max;
		this.num = min;

		dec = new Label("-");
		dec.setMinSize(30, 30);
		dec.setAlignment(Pos.BASELINE_CENTER);
		dec.setStyle("-fx-background-color: gainsboro ;");
		lbl = new Label(min + "");
		lbl.setMinSize(50, 30);
		lbl.setAlignment(Pos.BASELINE_CENTER);
		lbl.setStyle("-fx-background-color: white;");
		inc = new Label("+");
		inc.setMinSize(30, 30);
		inc.setAlignment(Pos.BASELINE_CENTER);
		inc.setStyle("-fx-background-color: gainsboro ;");
		this.getChildren().addAll(dec, lbl, inc);
		setListeners();
	}

	public int get() {
		return num;
	}

	public void set(int num) {
		this.num = num;
		lbl.setText(num + "");
	}

	public void setWidth1(double width) {
		lbl.setPrefWidth(width - 60);
	}

	public void setOnInc(EventHandler<MouseEvent> e) {
		inc.addEventHandler(MouseEvent.MOUSE_CLICKED, e);
	}

	public void setOnDec(EventHandler<MouseEvent> e) {
		dec.addEventHandler(MouseEvent.MOUSE_CLICKED, e);
	}

	private void setListeners() {
		dec.setOnMouseClicked(e -> {
			if (num > min) {
				num--;
				lbl.setText(num + "");
			}
		});

		inc.setOnMouseClicked(e -> {
			if (num < max) {
				num++;
				lbl.setText(num + "");
			}
		});
	}
}
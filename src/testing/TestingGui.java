package testing;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.layout.Pane;

public class TestingGui {

	@Test
	public void lookupTest() {
		Pane p = new Pane();
		Pane q = new Pane();
		q.setId("ID1");
		p.getChildren().add(q);
		assertTrue(p.lookup("#ID1") != null);
	}
}

package gui.panes;

import javafx.scene.layout.BorderPane;

public class GamePane extends BorderPane implements ScreenPane {

	public GamePane(){
		this.setLeft(new CircuitPane());
		this.setCenter(new SideBar());
	}
	
	@Override
	public void onFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offFocus() {
		// TODO Auto-generated method stub
		
	}

}

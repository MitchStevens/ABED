package panes;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SideBar extends TabPane{
	Tab game, info;
	
	public SideBar(){
		this.setPrefWidth(Gui.SIDE_BAR_WIDTH);
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		game = new Tab("Game");
		game.setContent(new SideBarGame());
		
		info = new Tab("Info");
		info.setContent(new SideBarInfo());
		
		this.getTabs().add(game);
		this.getTabs().add(info);
	}

}

package actions;

import core.game.Gate;

public abstract class Action {
	
	public Action(){
		ActionRecorder.add_action(this);
	}
	
	public abstract String discription();
}
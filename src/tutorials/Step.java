package tutorials;

import java.util.function.Predicate;

import actions.Action;
import gui.panes.Gui;
import javafx.scene.layout.Pane;

public class Step {
	String text;
	Predicate<Action> p;
	Message m;
	boolean is_completed = false;
	
	public Step(String text, Predicate<Action> p){
		this.text = text;
		this.p = p;
		if(p == null){
			this.m = new SlideMessage(text);
			is_completed = true;
		}else
			this.m = new PointerMessage(text);
	}
	
	public boolean is_satisfied(Action a){
		if(p == null)
			return true;
		else
			return is_completed |= p.test(a);
	}
}

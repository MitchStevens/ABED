package actions;

import java.util.ArrayList;
import java.util.List;

import gui.panes.CircuitPane;

public class ActionRecorder {
	
	private static List<Action> actions = new ArrayList<>();
	private static boolean print_actions = true;
	
	public static void add_action(Action a){
		actions.add(a);
		if(print_actions){
			System.out.println(a.discription());
			CircuitPane.game.print_game();
		}
	}
	
}

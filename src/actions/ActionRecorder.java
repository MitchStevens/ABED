package actions;

import java.util.ArrayList;
import java.util.List;

public class ActionRecorder {
	
	public static List<Action> actions = new ArrayList<>();
	
	public static void add_action(Action a){
		actions.add(a);
	}
	
	public static void print_actions(){
		for(Action a : actions)
			System.out.println(a.discription());
	}
	
	public static void print_actions(Class c){
		for(Action a : actions)
			if(c.isInstance(a))
				System.out.println(c);
	}
	
}

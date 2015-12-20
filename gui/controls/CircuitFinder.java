package controls;

import java.util.ArrayList;
import java.util.List;

import circuits.Circuit;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class CircuitFinder extends Text {	
	String curr_text;
	Circuit curr_circ;
	
	public CircuitFinder(String s){		
		super(s);
		curr_text = s;
		curr_circ = null;
	}
	
	public void key_pressed(KeyCode e){
		List<String> suggestions = new ArrayList<>();
		
		if(!e.isLetterKey())
			return;
					
		curr_text += e.toString();
		this.setText(curr_text);
		for(String s : Circuit.allCircuits.keySet())
			if(s.startsWith(curr_text)){
				curr_circ = Circuit.allCircuits.get(s);
				suggestions.add(s);
			}
				
		show_suggestions(suggestions);
	}
	
	private void show_suggestions(List<String> list){
		if(!list.isEmpty())
			System.out.println("Suggestion: "+list.get(0));
	}
	
}

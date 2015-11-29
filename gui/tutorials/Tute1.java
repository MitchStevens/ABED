package tutorials;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import logic.Game;
import circuits.Input;
import circuits.Output;
import panes.GamePane;

public class Tute1 extends Tutorial{

	/*Step 1: Add an Input to the board by opening the Basic Gates and clicking Input.
		The input is the basic source of electricity in ABED. You can toggle the Input by clicking on the center of the tile.
	Step 2: Move this tile by dragging it to side 3.
	Step 3: Add an Output to the board by clicking Output.
		The output accepts the electricity that is manipulated by the gates. Inputs and Outputs must be placed on side of the game board to be a valid game. 
	Step 4: Move this tile to side 1.
		You can only have one input/Output of a side
	Step 5: Add a Not gate, connected to the Input on side 3.
		Not gates are the most simple gate for manipulating power. These gates take an input and negate it, true becomes false, false becomes true. 
	Step 6: Click the Input to test that the Not gate.
		It should output false when the output is turned on.
	Step 7: Create another Not gate and complete a circuit.
		Remember that two negatives can cancel out.*/
	
	String[] step_text = new String[]{
		"Add an Input to the board by opening the Basic Gates and clicking Input.",
			"The input is the basic source of electricity in ABED. You can toggle the Input by clicking on the center of the tile.",
		"Move this tile by dragging it to side 3.",
		"Add an Output to the board by clicking Output.",
			"The output accepts the electricity that is manipulated by the gates. Inputs and Outputs must be placed on side of the game board to be a valid game.",
		"Move this tile to side 1.",
			"You can only have one input/Output of a side.",
		"Add a Not gate, connected to the Input on side 3.",
			"Not gates are the most simple gate for manipulating power. These gates take an input and negate it, true becomes false, false becomes true.",
			"It should output false when the output is turned on.",
		"Create another Not gate and complete a circuit.",
			"Remember that two negatives can cancel out."
	};
	
	public Tute1(){
		super(GamePane.currentGame);
		
		List<Predicate<Action>> step_preds = new ArrayList<>();
		step_preds.add( a -> (a == Action.ADD && a.c instanceof Input));
		step_preds.add(null);
		step_preds.add( a -> (a.c instanceof Input && a.c.coord.i == 0));
		step_preds.add( a -> (a == Action.ADD && a.c instanceof Output));
		step_preds.add(null);
		step_preds.add( a -> (a == Action.MOVE && a.c instanceof Output && a.c.coord.i == a.c.game.n -1));
		step_preds.add(null);
		step_preds.add( a -> (a == Action.ADD && a.c.name.equals("Not")));
		step_preds.add(null);
		step_preds.add(null);
		step_preds.add( a -> (a == Action.ADD && a.c.name.equals("Not")));
		step_preds.add(null);
		
		for(int i = 0; i < step_text.length; i++)
			steps.add(new Step(step_text[i], step_preds.get(i)));
	}
}

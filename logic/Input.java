package logic;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Input extends Circuit{
	boolean value = false;
	
	public Input(){
		super("Input;0,0,0,0;0,1,0,0;");
		this.evals = new ArrayList<Evaluator>();
	}
	
	public void setValue(Boolean b){
		value = b;
	}
	
	public void toggle(){
		//toggles value true->false, false->true. It is my very favorite hack.
		value ^= true;
		this.game.updateGame(i, j);
	}
	
	public String outputAsString(int direction, int index){
		if(direction == 1 && index == 0)
			return getInputNumber()+"";
		else
			throw new Error("The only valid (dir, index) dir is (1, 0) for an input. You inputted: ("+direction+", "+index+").");
	}
	
	private int getInputNumber(){
		List<Circuit> inputs = new ArrayList<>();
		for(int dir = 0; dir < 4; dir++)
			inputs.addAll(game.circuitsOnEdge(dir, "Input"));
		
		for(int i = 0; i < inputs.size(); i++)
			if(this.equals(inputs.get(i)))
				return i;
		
		throw new Error("Couldn't find input in game, input number unknown.");
	}
	
	public Image getSprite(){
		String s = "Input"+(value ? "1" : "0");
		return loadedImages.get(s);
	}
	
	@Override
	public List<Bus> eval(){
		outputBus.set(1, new Bus(new Boolean[]{value}));
		return outputBus;
	}
	
	public Circuit clone(){
		return new Input();
	}
	
}
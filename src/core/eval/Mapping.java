package core.eval;

import static core.Utilities.toInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import core.game.Direction;
import core.tokens.Composite;

/**
 * An Operation is a function that acts on booleans and provides an output of booleans. Computationally it is a list of 
 * tokens, where a token is either a Boolean or another Operation. 
 * */
public class Mapping extends Observable implements Operation {	
	protected	String			name;
	protected	int[]			inputs	= new int[4];
	protected	int[] 			outputs = new int[4];
	public 		Composite[]		evals 	= new Composite[4];
	protected 	String 			logic;
	
	protected List<Boolean> last_inputs = new ArrayList<>();
	protected Map<Direction, List<Boolean>> last_outputs = new EnumMap<>(Direction.class);
	
	public Mapping(String name, String inputs, String outputs){
		this.name = name;
		this.inputs = toInt(inputs);
		this.outputs = toInt(outputs);
	}
	
	public Mapping(String name, String inputs, String outputs, String logic){
		this.name = name;
		this.inputs = toInt(inputs);
		this.outputs = toInt(outputs);
		set_logic(logic);
	}
	
	public Mapping(String name, int[] inputs, int[] outputs, String logic){
		this.name = name;
		this.inputs = inputs;
		this.outputs = outputs;
		set_logic(logic);
	}
	
	public String get_logic(){
		return logic;
	}
	
	public void set_logic(String logic){
		try {
			String[] str = logic.split(",");
			int pos = 0;
			for(int i = 0; i < 4; i++){
				if(outputs[i] > 0){
					this.evals[i] = new Composite(str[pos++]);
				}else
					this.evals[i] = null;
			}
			
		} catch (Exception e) {
			System.err.println("Couldn't parse the string \""+ logic +"\".");
			e.printStackTrace();
		}
		
		this.logic = logic;
		this.eval(Collections.nCopies(this.sum_intputs(), false));
	}

	@Override
	public int[] inputs() {
		return inputs;
	}

	@Override
	public int[] outputs() {
		return outputs;
	}

	@Override
	public void eval(List<Boolean> inputs) {
		if(inputs.equals(last_inputs))
			return;
		
		for(Direction d : Direction.values())
			if(evals[d.value] != null){
				last_outputs.put(d, evals[d.value].eval(inputs));
			}else
				last_outputs.put(d, new ArrayList<>());
	
		this.last_inputs = inputs;
		System.out.println(get_name() +" was evaled."); //////////////////////////
	}

	@Override
	public List<Boolean> last_inputs() {
		return last_inputs;
	}

	@Override
	public List<Boolean> last_outputs(Direction d) {
		return last_outputs.get(d);
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	@Override
	public Mapping clone(){
		return new Mapping(name, inputs, outputs, logic);
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Mapping)) return false;
		Mapping m = (Mapping)o;
		
		if(!name.equals(m.name)) return false;
		if(!Arrays.equals(inputs, m.inputs)) return false;
		if(!Arrays.equals(outputs, m.outputs)) return false;
		if(!logic.equals(m.logic)) return false;
		return true;
	}

	@Override
	public String get_name() {
		return name;
	}

	@Override
	public void add_observer(Node node) {
		this.addObserver(node);
	}
}




















package core.eval;

import static core.Utilities.popn;
import static core.Utilities.toInt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import core.game.Gate;
import core.tokens.BasicOp;
import core.tokens.Bool;
import core.tokens.Composite;
import core.tokens.Flag;
import core.tokens.Token;
import data.Reader;

/**
 * An Operation is a function that acts on booleans and provides an output of booleans. Computationally it is a list of 
 * tokens, where a token is either a Boolean or another Operation. 
 * */
public class Mapping implements Operation {	
	private	String 		id;
	private	int[]		inputs	= new int[4];
	private	int[] 		outputs = new int[4];
	private Token[]		evals;
	private String 		logic;
	private	List<Token>	tokens;
	
	public Mapping(String id, String inputs, String outputs){
		this.id = id;
		this.inputs = toInt(inputs);
		this.outputs = toInt(outputs);
	}
	
	public Mapping(String id, String inputs, String outputs, String logic){
		this.id = id;
		this.inputs = toInt(inputs);
		this.outputs = toInt(outputs);
		set_logic(logic);
	}
	
	public String get_name(){
		return id;
	}
	
	public String get_logic(){
		return logic;
	}
	
	public void set_logic(String logic){
		try {
			String[] str = logic.split(",");
			evals = new Token[4];
			for(int i = 0; i < str.length; i++)
				this.evals[i] = new Composite(str[i]); 
			
		} catch (Exception e) {
			System.err.println("Couldn't parse the string \""+ logic +"\".");
			e.printStackTrace();
		}
		
		this.logic = logic;
	}

	@Override
	public String toString() {
		return id +": "+ tokens;
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
	public void eval() {
		// TODO Auto-generated method stub
		
	}
}




















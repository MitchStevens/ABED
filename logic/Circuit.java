package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import data.Reader;

public class Circuit{
	public static Circuit Input = new Input();
	public static Map<String, Circuit> loadedCircuits = Reader.loadCircuits();
	
	public String name;
	String initData;
	List<Evaluator> evals;
	public List<Bus> inputBus, outputBus;
	public Integer i, j, rot;
	//rot is the number of CLOCKWISE rotations
	Game game;
	//evals.size should be the same as outputBus.size
	
	public Circuit(){}
	public Circuit(String datum){
		init(datum);
	}

	public Circuit(String datum, Game g, int i, int j){
		init(datum);
		this.game = g;
		this.i = i;
		this.j = j;
	}
	
	public Circuit(Circuit c, Game g){
		init(c.initData);
		this.game = g;
	}
	
	public Circuit setGame(Game g){
		this.game = g;
		return this.clone();
	}
	
	public void init(String datum){
		//NAME;INPUTS;OUTPUTS;EVALS
		this.initData = datum;
		String[] data = datum.split(";");
		this.name = data[0];
		this.inputBus = createBusList(data[1]);
		this.outputBus = createBusList(data[2]);
		this.evals = new ArrayList<>();
		if(data.length == 4)
			for(String s : data[3].split(","))
				evals.add(new Evaluator(s));
		this.eval();
		this.rot = 0;
	}
	
	public void setRot(int rot){
		this.rot = mod4(rot);
	}
	
	public void addRot(int add){
		//+1 is 90 clockwise, -1 is 90 anti clockwise, etc.
		this.rot = mod4(rot + add);
	}
	
	public static int mod4(int i){		
		//even works for negatives. bitmasks FTW.
		return i & 0x0003;
	}
	
	public void toggle(){
		//This method is going to be used for the input circuit, but I like the idea of
		//being able to toggle a circuit to do other things. Making room for when my mind baby arrives.
	}
	
	protected static List<Bus> createBusList(String str){
		List<Bus> tbr = new ArrayList<>();
		for(String s : str.split(","))
			tbr.add(new Bus(Integer.parseInt(s)));
		return tbr;
	}
	
	public static List<Boolean> flatten(List<Bus> bus){
		List<Boolean> tbr = new ArrayList<>();
		for(Bus b : bus)
			tbr.addAll(b.toBooleanList());
		return tbr;
	}
	
	public void updateInputs(){
		//if there is no game enclosing the circuit, updating inputs is undefined
		if(game == null) return;
		//check directions 0-4
		for(int dir = 0; dir < 4; dir++){
			Bus b = validInputAtDir(dir);
			if(b != null)
				inputBus.set(dir, b);
			else inputBus.get(dir).clear();
		}
		eval();
	}
	
	public Bus validInputAtDir(int dir){
		//If there is a valid input from another circuit at dir return it. Else return null.
		Circuit c = circuitAtDir(dir);
		if(c == null) return null;
		if(inputBus.get(mod4(dir-rot)).size() == c.outputBus.get(mod4(dir-2-c.rot)).size() &&
				inputBus.get(mod4(dir-rot)).size() > 0)
			return c.outputBus.get(mod4(dir-2-c.rot));
		else return null;
	}
	
	public Bus validOutputAtDir(int dir){
		//The inverse of the above. looks for valid inputs.
		Circuit c = circuitAtDir(dir);
		if(c == null) return null;
		if(outputBus.get(mod4(dir-rot)).size() == c.inputBus.get(mod4(dir-2-c.rot)).size() &&
				outputBus.get(mod4(dir-rot)).size() > 0)
			return c.inputBus.get(mod4(dir-2-c.rot));
		else return null;
	}
	
	public List<Bus> eval(){
		//evaluates tile and updates outputBus
		List<Boolean> flatInput = flatten(inputBus);
		int count = 0;
		for(int dir = 0; dir < 4; dir++)
			for(int i = 0; i < outputBus.get(dir).size(); i++){
				Boolean b = evals.get(count++).eval(flatInput);
				outputBus.get(dir).set(i, b);
			}
		return outputBus;
	}
	
	public Bus inputAtAbsDir(int dir){
		//returns input at dir, taking into account rotation
		return inputBus.get(mod4(dir-rot));
	}
	
	public Bus outputAtAbsDir(int dir){
		//returns output at dir, taking into account rotation
		return outputBus.get(mod4(dir-rot));
	}
	
	//mostly used for debugging/testing
	public List<Boolean> inputList(){ return flatten(inputBus); }
	public List<Boolean> outputList(){ return flatten(outputBus); }
	
	public Circuit circuitAtDir(int dir){
		Circuit c = null;
		switch(dir){
		case 0:
			if(j != 0) c = game.tileGrid[i][j-1];
			break;
		case 1:
			if(i != game.n-1) c = game.tileGrid[i+1][j];
			break;
		case 2:
			if(j != game.n-1) c = game.tileGrid[i][j+1];
			break;
		case 3:
			if(i != 0) c = game.tileGrid[i-1][j];
			break;
		}
		return c;
	}
	
	//calling circuitAtDir with a numerical direction gets confusing after a while.
	public Circuit up(){ return circuitAtDir(0); }
	public Circuit down(){ return circuitAtDir(2); }
	public Circuit left(){ return circuitAtDir(3); }
	public Circuit right(){ return circuitAtDir(1); }
	
	public void updatePos(int i, int j){
		if(game == null) return;
		if(game.n >= i || game.n >= j) return;
		this.i = i; this.j = j;
	}
	
	@Override
	public Circuit clone(){
		return new Circuit(initData);
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Circuit)) return false;
		Circuit c = (Circuit)o;
		if(!name.equals(c.name)) return false;
		if(!initData.equals(c.initData)) return false;
//		for(int i = 0; i < evals.size(); i++)
//			if(!evals.get(i).tokens.equals(c.evals.get(i).tokens))
//				return false;
		if(i != null)
			if(!i.equals(c.i))
				return false;
		if(j != null)
			if(!j.equals(c.j))
				return false;
		return true;
	}
	
	public String[] printCircuit(){
		//prints circuit for printGame()
		String[] tbr = new String[3];
		tbr[0] = "   ";
		tbr[1] = " "+name.substring(0, 1).toLowerCase()+" ";
		tbr[2] = "   ";
		
		if(		 inputBus.get(mod4(0-rot)).size() > 0) tbr[0] = " I ";
		else if(outputBus.get(mod4(0-rot)).size() > 0) tbr[0] = " O ";
		if(		 inputBus.get(mod4(1-rot)).size() > 0) tbr[1] = tbr[1].substring(0, 2)+"I";
		else if(outputBus.get(mod4(1-rot)).size() > 0) tbr[1] = tbr[1].substring(0, 2)+"O";
		if(		 inputBus.get(mod4(2-rot)).size() > 0) tbr[2] = " I ";
		else if(outputBus.get(mod4(2-rot)).size() > 0) tbr[2] = " O ";
		if(		 inputBus.get(mod4(3-rot)).size() > 0) tbr[1] = "I"+tbr[1].substring(1, 3);
		else if(outputBus.get(mod4(3-rot)).size() > 0) tbr[1] = "O"+tbr[1].substring(1, 3);
		
		return tbr;
	}
}

class Input extends Circuit{
	boolean value = false;
	
	public Input(){
		this.initData = "Input";
		this.name = "Input";
		this.inputBus = createBusList("0,0,0,0");
		this.outputBus = createBusList("0,1,0,0");
		this.evals = new ArrayList<>();
		this.eval();
		this.rot = 0;
	}
	
	public void setValue(Boolean b){
		value = b;
	}
	
	public void toggle(){
		//toggles value true->false, false->true. It is my very favorite hack.
		value ^= true;
		this.game.updateGame(i, j);
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
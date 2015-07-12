package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import abedgui.Square;
import javafx.scene.image.Image;
import data.Reader;
import static logic.Evaluator.init;

public class Circuit{
	public static Circuit Input = new Input();
	public static Map<String, Circuit> 	loadedCircuits 	= Reader.loadCircuits();
	public static Map<String, Image>	loadedImages	= Reader.loadImages();
	public static String[] circuitTypes = new String[]{
		"Basic Circuits",
		"Single Circuits",
		"Multi Circuits"};
	
	public String name = "";
	String initData;
	public List<Evaluator> evals;
	public final List<Bus> inputBus, outputBus;
	//rot is the number of CLOCKWISE rotations
	public Integer i, j, rot;
	//used to sort circuits in the gui
	public int type = 0;
	public Game game;
	//evals.size should be the same as outputBus.size
	public List<List<String>> evalStrings = null;
	//remember to nullify this after converting from game to circuit
	
	public Circuit(String datum){
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
		this.rot = 0;
	}
	
	public void setGame(Game g){
		this.game = g;
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
			if( b != null)
				inputBus.set(mod4(dir-rot), b);
			else inputBus.set(mod4(dir-rot), new Bus(inputBus.get(mod4(dir-rot)).size()));
		}
		eval();
	}
	
	public Bus validInputAtDir(int dir){
		//If there is a valid output from another circuit at dir return it. Else return null.
		Circuit c = game.circuitAtDir(this, dir);
		if(c == null) return null;
		if(inputAtAbsDir(dir).size() == c.outputAtAbsDir(dir-2).size() &&
				inputAtAbsDir(dir).size() > 0)
			return c.outputAtAbsDir(dir-2);
		else return null;
	}
	
	public Bus validOutputAtDir(int dir){
		//The inverse of the above. looks for valid inputs.
		Circuit c = game.circuitAtDir(this, dir);
		if(c == null) return null;
		if(outputAtAbsDir(dir).size() == c.inputAtAbsDir(dir-2).size() &&
				outputAtAbsDir(dir).size() > 0)
			return c.inputAtAbsDir(dir-2);
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
	public String pos(){ return "("+i+", "+j+")"; }
	
	public void updatePos(int i, int j){
		if(game == null) return;
		if(game.n >= i || game.n >= j) return;
		this.i = i; this.j = j;
	}
	
	public String outputAsString(int direction){
		//given a (relative) direction, return the eval string for the inputs at that direction
		String tbr = "";
		
		Circuit parent;
		for(int dir = 0; dir < 4; dir++){
			parent = game.circuitAtDir(this, mod4(dir+rot));
			if(inputBus.get(dir).size() == 0) continue;
			if(parent == null || this.validInputAtDir(mod4(dir+rot)) == null){
				for(int i = 0 ; i < inputBus.get(dir).size(); i++)
					tbr += "F ";
			}else
				for(int i = 0 ; i < inputBus.get(dir).size(); i++){
					String s = parent.outputAsString(mod4(dir-rot -2 +parent.rot));
					tbr += s+" ";
				}
		}
		
		switch(name){
		case "Output":
		case "Bus":
		case "Super":
		case "Right":
		case "Left":
			return init(tbr);
		case "And": return tbr+"&";
		case "Or": 	return tbr+"|";
		case "Not": return tbr+"~";
		default:	return tbr+name;
		}
	}
	
	public Integer toIndex(int dir, int index){
		int num = index;
		for(int i = 0; i < dir; i++)
			num += outputBus.get(i).size();
		return num;
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
		if(i != null)
			if(!i.equals(c.i))
				return false;
		if(j != null)
			if(!j.equals(c.j))
				return false;
		return true;
	}
	
	public boolean equiv(Circuit c){
		for(int i = 0; i < 4; i++){
			if(inputBus.get(i).size()  != c.inputBus.get(i).size())  return false;
			if(outputBus.get(i).size() != c.outputBus.get(i).size()) return false;
		}
		if(this.evals.size() != c.evals.size()) return false;
		for(int i = 0; i < evals.size(); i++)
			if(!evals.get(i).equiv(c.evals.get(i)))
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
	
	@Override
	public String toString(){
		return name+","+rot+","+i+","+j;
	}
}
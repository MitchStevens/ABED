package abedgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.scene.image.Image;
import static abedgame.Functions.*;

/*
NOTES:
    each gate has an output at direction 0 and an input at 2.
    
    COMMENT COMMENT
*/

public class Gate {
    
    public static Map<String, Gate> allGates = new HashMap<>();
    public static Map<String, Image> allSprites = new HashMap<>();
    
    String data;
    String name;
    String logic;
    Integer i;
    Integer j;
    int rot;
//    List<Bus> inputBus;
//    List<Bus> outputBus;
    
    public Gate(String str){
    	//if(str == null) return;
    	String[] split = str.split(";");
    	this.name = split[0];
    	this.logic = split[3];
    	this.i = null;
    	this.j = null;
    	this.rot = 0;
    	this.data = str;
    	
    	//inputBus = map(s -> new Bus(Integer.parseInt(s)), split[1].split(","));
    	//outputBus = map(s -> new Bus(Integer.parseInt(s)), split[2].split(","));
    }

    //Supplier<Integer> numInputs = () -> fold((a,b) -> a+b, map(b -> b.numWires(), inputBus)); 
    
    //gets least positive modulus
    Function<Integer, Integer> mod4 = i -> (i>0?i:-i+2)%4;
    
//    public void tick(){
//    	//get all valid inputs
//    	List<Bus> inputs = allValidInputs();
//    	System.out.println("num valid inputs: "+filter(b -> b != null, inputs).size());
//    	//applies the output bus to the input bus of this gate.
//    	for(int dir = 0; dir < 4; dir++)
//    		if(inputs.get(dir) != null)
//    			this.inputingFromDir.apply(dir).wires = inputs.get(dir).wires;
//    	
//    	//get all valid outputs
//    	List<Integer> outputDirs = filter(dir -> isOutput(dir), Arrays.asList(new Integer[]{0, 1, 2, 3}));
//    	//tick the outputs
//    	outputDirs.forEach(dir -> gateAtDir(dir).tick());
//    }
//    
//    public Image getSprite(){
//    	//Create a map and put these images in it.
//    	String path = name + cat(inputBus);
//    	return allSprites.get(path+".bmp");
//    }
//
//    
//    Supplier<Image> defSprite = () -> allSprites.get(name+dup("0", numInputs.get())+".bmp");
    
    Consumer<Integer> rotate = r -> this.rot = mod4.apply(this.rot + r);

    public Gate gateAtDir(int dir){
    	//returns gate in a given direction
    	Game game = ABEDGUI.getBoard().currentGame;
    	try{
	        switch(mod4.apply(dir)){
	        case 0: return game.placed[i-1][j];
	        case 1: return game.placed[i][j+1];
	        case 2: return game.placed[i+1][j];
	        case 3: return game.placed[i][j-1];
	        default: throw new Error(dir+" is not a legit direction!");
	        }
    	}catch(NullPointerException e){
    		return null;}
    	catch(ArrayIndexOutOfBoundsException e){
    		return null;
    	}
    }
    
//    Function<Integer, Bus> outputtingAtDir = dir -> outputBus.get(mod4.apply(dir-rot));
//    Function<Integer, Bus> inputingFromDir = dir -> inputBus.get(mod4.apply(dir-rot));
//    
//    //gets all gates adj to this one. Lazy evaluation for days.
//    Supplier<List<Gate>> adjGates = () -> map(s -> gateAtDir(s), Arrays.asList(new Integer[]{0, 1, 2, 3}));
//
//    public List<Bus> allValidInputs(){
//    	//gets all inputs
//    	//also resets output buses
//    	List<Bus> tbr = new ArrayList<>();
//    	for(int dir = 0; dir < 4; dir++){
//    		tbr.add(null);
//    		Gate g = gateAtDir(dir);
//    		if(g == null) continue;
//    		if(g.outputtingAtDir.apply(mod4.apply(dir+2)).numWires() == this.inputingFromDir.apply(dir).numWires())
//    			if(this.inputingFromDir.apply(dir).numWires() > 0)
//    				tbr.set(dir, g.outputtingAtDir.apply(mod4.apply(dir+2)));
//    	}
//    	//outputBus = Logic.eval(this, tbr);
//    	return tbr;
//    }
//    
//    public boolean isOutput(int dir){
//    	Gate g = gateAtDir(dir);
//    	if(g == null) return false;
//    	if(g.inputingFromDir.apply(mod4.apply(dir+2)).numWires() == this.outputtingAtDir.apply(dir).numWires())
//    		return this.outputtingAtDir.apply(dir).numWires() > 0;
//    	return false;
//    }
    
	@Override
    public boolean equals(Object o){
    	if(!(o instanceof Gate)) return false;
    	Gate g = (Gate)o;
    	if(rot != g.rot) return false;
    	try{
    		if(!i.equals(g.i)) return false;
    	}catch(NullPointerException np) {
    		if(g.i != null) return false;}
    	try{
    		if(!j.equals(g.j)) return false;
    	}catch(NullPointerException np) {
    		if(g.j != null) return false;}
    	return true;
    }

	@Override
    public Object clone(){
    	return new Gate(this.data);
    }
}

class Bus{	
	//holder for wire data
	List<Boolean> wires;
	
	public Bus(int num){ this.wires = dup(false, num); }
	public Bus(List<Boolean> bus){ this.wires = bus;}

	public int numWires(){ return wires.size(); }
	
	public static Bus nullBus(){
		return new Bus(new ArrayList<Boolean>());
	}
	
	public String toString(){
		String tbr = "";
		for(Boolean b : wires)
			if(b != null)
				tbr += (b?"1":"0");
		return tbr;
	}
}
//
//class Input extends Gate{
//	boolean isOn = false;
//	int inputNum = 0;
//	
//	public Input() {
//		super(null);
//		String str = "Input;0,0,0,0;1,0,0,0;_,_,_,_";
//    	String[] split = str.split(";");
//    	this.name = split[0];
//    	this.logic = split[3];
//    	this.i = null;
//    	this.j = null;
//    	this.rot = 0;
//    	this.data = str;
//    	
//    	inputBus = map(s -> new Bus(Integer.parseInt(s)), split[1].split(","));
//    	outputBus = map(s -> new Bus(Integer.parseInt(s)), split[2].split(","));
//    	
//    	//Game game = ABEDGUI.getBoard().currentGame;
//    	//this.inputNum = game.countGateType.apply("Input");
//	}
//	
//	@Override
//	public void tick(){}
//	
//	public Image getSprite(){
//    	return new Image("/images/Input"+(isOn?"1":"0")+".bmp");
//    }
//}
//
//class Output extends Gate{
//	int outputNum = 0;
//	
//	public Output(){
//		super(null);
//		String str = "Output;0,0,1,0;0,0,0,0;0,_,_,_";
//    	String[] split = str.split(";");
//    	this.name = split[0];
//    	this.logic = split[3];
//    	this.i = null;
//    	this.j = null;
//    	this.rot = 0;
//    	this.data = str;
//    	
//    	inputBus = map(s -> new Bus(Integer.parseInt(s)), split[1].split(","));
//    	outputBus = map(s -> new Bus(Integer.parseInt(s)), split[2].split(","));
//    	
//    	//Game game = ABEDGUI.getBoard().currentGame;
//    	//this.outputNum = game.countGateType.apply("Output");
//	}
//	
//	@Override
//	public void tick(){}
//}






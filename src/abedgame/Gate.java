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
    
    public static String[][] gateTypes = new String[][]{
    	new String[] {"Input","Output","Not","And","Or"},
		new String[] {"Single", "Cross", "Double", "Split"},
		new String[] {"Nand", "Nor", "XOR", "NXOR"},
		new String[] {"Half Adder", "Adder"}};
	
    public static String[] gateNames =
        new String[] {"Basic Gates","Wires","Rookie Combinations","Adders"};
    
    public static Map<String, Gate> allGates = new HashMap<>();
    public static Map<String, Image> allSprites = new HashMap<>();
    
    String data;
    String name;
    String logic;
    Integer i;
    Integer j;
    int rot;
    List<Bus> inputBus;
    List<Bus> outputBus;
    Game game;
    
    public Gate(String str){
    	if(str == null) return;
    	String[] split = str.split(";");
    	this.name = split[0];
    	this.logic = split[3];
    	this.i = null;
    	this.j = null;
    	this.rot = 0;
    	this.data = str;
    	
    	inputBus = map(s -> new Bus(Integer.parseInt(s)), split[1].split(","));
    	outputBus = map(s -> new Bus(Integer.parseInt(s)), split[2].split(","));
    }

    Supplier<Integer> numInputs = () -> fold((a,b) -> a+b, map(b -> b.numWires(), inputBus)); 
    
    public void setGame(Game g){ this.game = g; }
    
    //gets least positive modulus
    Function<Integer, Integer> mod4 = i -> (i>0?i:-i+2)%4;
    
    public void tick(){
    	//check if any of the adjacent gates can output to our inputs
    	for(int dir = 0; dir < 4; dir++){
    		Gate g = gateAtDir(dir);
    		if(g == null) continue;
    		//deal only with inputs
    		if(g.outputtingAtDir.apply(mod4.apply(dir+2)).numWires() == this.inputingFromDir.apply(dir).numWires())
    			this.inputingFromDir.apply(dir).wires = g.outputtingAtDir.apply(mod4.apply(dir+2)).wires;
    	}
    }
    
    public Image getSprite(){
    	//Create a map and put these images in it.
    	String path = name + cat(inputBus);
    	System.out.println(path);
    	return new Image("/images/"+path+".bmp");
    }
    
    
    Supplier<Image> defSprite = () -> allSprites.get(name+dup("0", numInputs.get())+".bmp");
    
    Consumer<Integer> rotate = r -> {this.rot = mod4.apply(this.rot + r); };

    public Gate gateAtDir(int dir){
    	//returns gate in a given direction
    	try{
	        switch(mod4.apply(dir)){
	        case 0: return game.placed[i-1][j];
	        case 1: return game.placed[i][j+1];
	        case 2: return game.placed[i+1][j];
	        case 3: return game.placed[i][j-1];
	        default: throw new Error(dir+" is not a legit direction!");
	        }
    	}catch(NullPointerException np){
    		return null;}
    }
    
    Function<Integer, Bus> outputtingAtDir = dir -> outputBus.get(mod4.apply(dir-rot));
    Function<Integer, Bus> inputingFromDir = dir -> inputBus.get(mod4.apply(dir-rot));
    
    //gets all gates adj to this one. Lazy evaluation for days.
    Supplier<List<Gate>> adjGates = () -> map(s -> gateAtDir(s), Arrays.asList(new Integer[]{0, 1, 2, 3}));

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
	
	public String toString(){
		String tbr = "";
		for(Boolean b : wires)
			tbr += (b?"1":"0");
		return tbr;
	}
}

class Input extends Gate{
	boolean isOn = false;
	int inputNum = 0;
	
	public Input() {
		super(null);
		String str = "Input;0,0,0,0;1,0,0,0;_,_,_,_";
    	String[] split = str.split(";");
    	this.name = split[0];
    	this.logic = split[3];
    	this.i = null;
    	this.j = null;
    	this.rot = 0;
    	this.data = str;
    	
    	inputBus = map(s -> new Bus(Integer.parseInt(s)), split[1].split(","));
    	outputBus = map(s -> new Bus(Integer.parseInt(s)), split[2].split(","));
	}
	
	@Override
	public void setGame(Game g){
		this.game = g;
		this.inputNum = game.countGateType.apply("Input");
	}
	
	@Override
	public void tick(){}
	
	public Image getSprite(){
    	return new Image("/images/Input"+(isOn?"1":"0")+".bmp");
    }
}

class Output extends Gate{
	int outputNum = 0;
	
	public Output(){
		super(null);
		String str = "Output;0,0,1,0;0,0,0,0;0,_,_,_";
    	String[] split = str.split(";");
    	this.name = split[0];
    	this.logic = split[3];
    	this.i = null;
    	this.j = null;
    	this.rot = 0;
    	this.data = str;
    	
    	inputBus = map(s -> new Bus(Integer.parseInt(s)), split[1].split(","));
    	outputBus = map(s -> new Bus(Integer.parseInt(s)), split[2].split(","));
	}
	
	@Override
	public void setGame(Game g){
		this.game = g;
		this.outputNum = game.countGateType.apply("Output");
	}
	
	@Override
	public void tick(){}
}






package abedgame;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javafx.scene.image.Image;

/*
NOTES:
    each gate has an output at direction 0 and an input at 2.
    if the gate has !=1 inputs, gate must override parentCheck()
    if the gate has !=1 outputs, gate must override childCheck()
    
    COMMENT COMMENT
*/

public class Gate {
    
    public static String[][] gateTypes = new String[][]{
    	new String[] {"Input","Output","Not","And","Or"},
		new String[] {"Single"},
		new String[] {"Nand", "Nor", "XOR", "NXOR"},
		new String[] {"Half Adder", "Adder"}};
	
    public static String[] gateNames =
        new String[] {"Basic Gates","Rookie Combinations","Wires","Adders"};
    
    public static Map<String, Gate> allGates = new HashMap<>();
    public static Map<String, Image> allSprites = new HashMap<>();
            
    String name;
    String logic;
    Integer i;
    Integer j;
    int rot;
    Gate[] inputs;
    Gate[] outputs;
    int[] inputDir;
    int[] outputDir;
    
    public Gate(String str){
    	if(str.isEmpty()) return;
    	String[] data = str.split(";");
    	this.name = data[0];
    	this.logic = data[3];
    	this.i = null;
    	this.j = null;
    	this.rot = 0;
    	this.inputs = new Gate[data[1].split(",").length];
    	this.outputs = new Gate[data[2].split(",").length];
    	this.inputDir = new int[inputs.length];
    		for(int i = 0; i < inputs.length; i++)
    			inputDir[i] = Integer.parseInt(data[1].split(",")[i]);
    	this.outputDir = new int[outputs.length];
			for(int i = 0; i < outputs.length; i++)
				outputDir[i] = Integer.parseInt(data[2].split(",")[i]);
    }
    
    public static void readGates(){
    	try {
             FileReader input = new FileReader("src/data/basicGates.txt");
             BufferedReader bf = new BufferedReader(input);
             String line;
             
             while ((line = bf.readLine()) != null) {
            	 if(line.charAt(0) == '/') continue;
            	 Gate g = new Gate(line);
            	 allGates.put(g.name, g);
             }
             bf.close();
    	 } catch (IOException e) {
    		 System.err.println("reading file is fLIcked");
    	 }
    	 allGates.put("Input", new Input(""));
    	 System.out.println("doing");
    	 allGates.put("Output", new Output(""));
    }
    
    public static void readSprites(){
    	
    }
    
    public Gate singleInputCheck(int dir){
    	Game g = ABEDGUI.getBoard().currentGame;
    	Piece p = g.pieceAtDir(i, j, rot+dir);
    	
    	
    	//
    	if(p == null) return null;
    	
    	for(int k : p.gate.outputDir){
    		try{
        		if((rot+dir+2)%4 == (p.gate.rot+k)%4)
        			return p.gate;
    		}catch(NullPointerException ex) {
    			return null;
        	}
    	}
		return null;
    }
    
    public Gate singleOutputCheck(int dir){
    	Piece p = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, rot+dir);
    	if(p == null) return null;
    	for(int k : p.gate.inputDir){
    		try{
        		if((rot+dir+2)%4 == (p.gate.rot+k)%4)
        			return ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, rot+dir).gate;
    		}catch(NullPointerException ex) {
    			return null;
        	}
    	}
		return null;
    }
    
    public void inputCheck(){
    	for(int i = 0; i < inputs.length; i++)
    		inputs[i] =  this.singleInputCheck(inputDir[i]);
    }
    
    public void outputCheck(){
    	for(int i = 0; i < outputs.length; i++)
    		outputs[i] =  this.singleOutputCheck(outputDir[i]);
    }
    
    public Image getSprite(){
    	String path = "/images/"+name;
    	for(Gate g : inputs)
    		if(g != null)
    			path += g.eval() ? "1" : "0";
    		else path += "0";
    	System.out.println(path);
    	return new Image(path+".bmp");
    }
    
    public boolean eval(){
    	boolean[] bool = new boolean[inputs.length];
    	for(int i = 0; i < inputs.length; i++)
    		bool[i] =  inputs[i] != null ? inputs[i].eval() : false;
    	return new Logic(bool, logic).eval();
    }
	
    public void rotate(int r){
        this.rot += (r % 4) + 4;
        this.rot %= 4;
    }    
    
    @Override
    public String toString(){
    	return logic;
    }

    @Override
    public boolean equals(Object o){
    	if(!(o instanceof Gate)) return false;
    	Gate g = (Gate)o;
    	if(rot != g.rot) return false;
    	if(!inputs.equals(g.inputs)) return false;
    	if(!outputs.equals(g.outputs)) return false;
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
    	String tbr = this.name+";";
    	for(int i : inputDir)
    		tbr += i+",";
    	tbr += ";";
    	for(int i : outputDir)
    		tbr += i+",";
    	tbr += ";"+this.logic;
    	return new Gate(tbr);
    }
}

class Input extends Gate{
    int inputNum;
    boolean isOn;
    
    public Input(String s){
    	super(s);
        this.isOn = false;
        this.rot = 0;
        this.inputs = new Gate[]{};
        this.outputs = new Gate[]{null};
        this.inputDir = new int[]{};
        this.outputDir = new int[]{0};
    }
        
    @Override
    public void inputCheck(){}
    
    @Override
    public void outputCheck(){
    	outputs[0] =  this.singleOutputCheck(0);
    }
    
    @Override
    public Image getSprite(){
        return new Image("/images/Input"+(isOn? "1": "0")+".bmp");
    }

    @Override
    public boolean eval(){
        return isOn;
    }

    @Override
    public String toString(){
    	return inputNum+"";
    }

    public Object clone(){
    	return new Input("");
    }
}

class Output extends Gate{
	int outputNum;
	
    public Output(String s){
    	super(s);
        this.inputs = new Gate[]{null};
        this.outputs = new Gate[]{};
        this.inputDir = new int[]{2};
        this.outputDir = new int[]{};
    }
    
    @Override
    public void inputCheck(){
    	inputs[0] =  this.singleInputCheck(2);
    }
    
    @Override
    public void outputCheck(){}
    
    @Override
    public Image getSprite(){
        int p = 0;
        try{p = inputs[0].eval()? 1: 0;}
        catch(NullPointerException np){}
        
        return new Image("/images/Output"+p+".bmp");
    }

    public boolean eval(){
        if(inputs[0] == null)
            return false;
        else return inputs[0].eval();
    }

    @Override
    public String toString(){
    	return inputs[0] != null?inputs[0].toString():"(F)";
    }

    public Object clone(){
    	return new Output("");
    }
}
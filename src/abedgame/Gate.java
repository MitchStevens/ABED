package abedgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		new String[] {"Single", "Double", "Split"},
		new String[] {"Nand", "Nor", "XOR", "NXOR"},
		new String[] {"Half Adder", "Adder"}};
	
    public static String[] gateNames =
        new String[] {"Basic Gates","Wires","Rookie Combinations","Adders"};
    
    public static Map<String, Gate> allGates = new HashMap<>();
    String data;        
    String name;
    String logic;
    Integer i;
    Integer j;
    int rot;
    ArrayList<Gate> inputs = new ArrayList<Gate>();
    int[] inputDir = new int[4];
    ArrayList<Gate> outputs = new ArrayList<Gate>();
    int[] outputDir = new int[4];
    
    public Gate(String str){
    	//is there a better way of specifying size?
    	while(inputs.size() < 4){
    		inputs.add(null);
    		outputs.add(null);
    	}
    	if(str == null) return;
    	String[] split = str.split(";");
    	this.name = split[0];
    	this.logic = split[3];
    	this.i = null;
    	this.j = null;
    	this.rot = 0;
    	this.data = str;
    	
    	for(int i = 0; i < 4; i++)
    		inputDir[i] = Integer.parseInt(split[1].split(",")[i]);
       	for(int i = 0; i < 4; i++)
    		outputDir[i] = Integer.parseInt(split[2].split(",")[i]);
    		
    	
    }
    
    public Gate singleInputCheck(int dir){
    	Piece p = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, rot+dir);
    	if(p == null) return null;
    	
    	for(int k = 0; k < 4; k++){
    		if(p.gate.outputDir[k] == 0) continue;
	    	try{
	    		if((rot+dir+2)%4 != (p.gate.rot+k)%4) continue;
	    		if(this.inputDir[dir] != p.gate.outputDir[k]) continue;
	    		if(this.inputDir[dir] == 0) continue;
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
    	for(int k = 0; k < 4; k++){
    		if(p.gate.inputDir[k] == 0) continue;
	    	try{
	    		if((rot+dir+2)%4 != (p.gate.rot+k)%4) continue;
	    		if(this.outputDir[dir] != p.gate.inputDir[k]) continue;
	    		if(this.outputDir[dir] == 0) continue;
	    		return p.gate;
	    	}catch(NullPointerException ex) {
	    		return null;
	    	}
	    }
    	return null;
    }
    
    public void inputCheck(){
    	for(int i = 0; i < 4; i++)
    		if(inputDir[i] > 0){
    			inputs.set(i, this.singleInputCheck(i));}
    }
    
    public void outputCheck(){
    	for(int i = 0; i < 4; i++)
    		if(outputDir[i] > 0)
    			outputs.set(i, this.singleOutputCheck(i));
    }
    
    public Image getSprite(){
    	//Create a map and put these images in it.
    	String path = name;
    	for(int i = 0; i < 4; i++){
    		if(inputs.get(i) != null)
    			for(boolean b : inputs.get(i).eval(this)){
    				path += b ? "1" : "0";}
    		else path += n0(inputDir[i]);
    	}
    	return new Image("/images/"+path+".bmp");
    }
    
    private String n0(int n){
    	String tbr = "";
    	while(n>0){tbr+="0";n--;}
    	return tbr;
    }
    
    public boolean[] eval(Gate g){
    	List<Boolean> bList = new ArrayList<>();
    	for(int i = 0; i < 4; i++){
    		if(inputs.get(i) != null)
    			for(boolean b : inputs.get(i).eval(this))
    				bList.add(b);
    		else
    			for(int j = 0; j < this.inputDir[i]; j++)
    				bList.add(false);
    	}
    	
    	return Logic.eval(bList.toArray(new Boolean[0]), this)[0];
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
    	super(null);
        this.isOn = false;
        this.name = "Input";
        this.rot = 0;
        this.inputDir = new int[]{0, 0, 0, 0};
        this.outputDir = new int[]{1, 0, 0, 0};
    }
        
    @Override
    public void inputCheck(){}

    
    @Override
    public Image getSprite(){
        return new Image("/images/Input"+(isOn? "1": "0")+".bmp");
    }

    @Override
    public boolean[] eval(Gate g){
        return new boolean[] {isOn};
    }

    @Override
    public String toString(){
    	return inputNum+"";
    }

    public Object clone(){
    	return new Input(null);
    }
}

class Output extends Gate{
	int outputNum;
	
    public Output(String s){
    	super(null);
        this.name = "Output";
        this.rot = 0;
        this.inputDir = new int[]{0, 0, 1, 0};
        this.outputDir = new int[]{0, 0, 0, 0};
    }
    
    @Override
    public void inputCheck(){
    	//change from default
    	inputs.set(2, this.singleInputCheck(2));
    }
    
    
    @Override
    public Image getSprite(){
        int p = 0;
        try{p = inputs.get(2).eval(this.inputs.get(2))[0] ? 1: 0;}
        catch(NullPointerException np){}
        
        return new Image("/images/Output"+p+".bmp");
    }
    
    @Override
    public boolean[] eval(Gate g){
    	//An output never actually needs to be evaluated.
    	return null;
    }

    @Override
    public String toString(){
    	return inputs.get(2) != null ? inputs.get(2).toString() : "(F)";
    }

    public Object clone(){
    	return new Output(null);
    }
}









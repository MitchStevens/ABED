package abedgame;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/*
NOTES:
    each gate has an output at direction 0 and an input at 2.
    if the gate has !=1 inputs, gate must override parentCheck()
    if the gate has !=1 outputs, gate must override childCheck()
    
    COMMENT COMMENT
*/

public abstract class Gate {
    
    public static String[][] gateTypes = new String[][]{
    	new String[] {"Input","Output","Not","And","Or"},
		new String[] {"Single"},
		new String[] {"Nand", "Nor", "XOR", "NXOR"},
		new String[] {"Half Adder", "Adder"}};
	
    public static String[] gateNames =
        new String[] {"Basic Gates","Rookie Combinations","Wires","Adders"};
        
    int rot;
    Integer i;
    Integer j;
    Gate[] inputs;
    Gate[] outputs;
    int[] inputDir;
    int[] outputDir;
    
    public Gate singleInputCheck(int dir){
    	Piece p = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, rot+dir);
    	if(p == null) return null;
    	
    	for(int k : p.gate.outputDir){
    		try{
        		if((rot+dir+2)%4 == (p.gate.rot+k)%4)
        			return ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, rot+dir).gate;
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
    
    public abstract void inputCheck();
    
    public abstract void outputCheck();
    
    public abstract Image getSprite();
    
    public abstract boolean eval();
	
    public void rotate(int r){    
        this.rot += (r % 4) + 4;
        this.rot %= 4;
    }    
    
    @Override
    public String toString(){
        String tbr = "Type: "+this.getClass().getSimpleName();
        tbr += "\nParent(s):";
        for(Gate g : inputs)
        	if(g != null) tbr += "\n  "+g.getClass().getSimpleName();
        tbr += "\nChild(ren):";
        for(Gate g : outputs)
        	if(g != null) tbr += "\n  "+g.getClass().getSimpleName();
        return tbr;
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
    	return this;
    }
}

class Input extends Gate{
    int inputNum;
    boolean isOn;
    
    public Input(){
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

}

class Output extends Gate{
	
    public Output(){
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
}

class And extends Gate{
    
    public And(){
        this.inputs = new Gate[]{null, null};
        this.outputs = new Gate[]{null};
        this.inputDir = new int[]{2, 3};
        this.outputDir = new int[]{0};
    }
    
    @Override
    public void inputCheck(){
    	inputs[0] = this.singleInputCheck(2);
    	inputs[1] = this.singleInputCheck(3);     
    }
    
    @Override
    public void outputCheck(){
    	outputs[0] =  this.singleOutputCheck(0);
    }
    
    @Override
    public Image getSprite(){
        int p1 = 0; int p2 = 0;
        try{ p1 = inputs[0].eval()? 1: 0; }
        catch(NullPointerException np){}
        try{ p2 = inputs[1].eval()? 1: 0; }
        catch(NullPointerException np){}
        
        return new Image("/images/And"+p1+p2+".bmp");
    }
        
    @Override
    public boolean eval(){
        if(inputs[0] != null && inputs[1] != null)
            return inputs[0].eval() && inputs[1].eval();
        else return false;
    }
}

class Single extends Gate{
	
    public Single(){
        this.inputs = new Gate[]{null};
        this.outputs = new Gate[]{null, null, null};
        this.inputDir = new int[]{2};
        this.outputDir = new int[]{3, 0, 1};
    }
    
    @Override
    public void inputCheck(){
    	inputs[0] =  this.singleInputCheck(2);
    }
    
    public void outputCheck(){
    	outputs[0] = this.singleOutputCheck(3);
    	outputs[1] = this.singleOutputCheck(0);
    	outputs[2] = this.singleOutputCheck(1);
    }
    
    @Override
    public Image getSprite(){
        int p = 0;
        try{p = inputs[0].eval()? 1: 0;}
        catch(NullPointerException np){}
        return new Image("/images/Single"+p+".bmp");
    }

    public boolean eval(){
        if(inputs[0] == null)
            return false;
        else return inputs[0].eval();
    }
}

class Not extends Gate{

    public Not(){
        this.inputs = new Gate[]{null};
        this.outputs = new Gate[]{null};
        this.inputDir = new int[]{2};
        this.outputDir = new int[]{0};
    }
	
    @Override
    public void inputCheck(){
    	inputs[0] =  this.singleInputCheck(2);
    }
    
    @Override
    public void outputCheck(){
    	outputs[0] =  this.singleOutputCheck(0);
    }
    
	@Override
	public Image getSprite() {
		int p = 0;
        try{p = inputs[0].eval()? 1: 0;}
        catch(NullPointerException np){}
        return new Image("/images/Not"+p+".bmp");
	}

	@Override
	public boolean eval() {
        if(inputs[0] == null)
            return true;
        else return !inputs[0].eval();
	}
	
}
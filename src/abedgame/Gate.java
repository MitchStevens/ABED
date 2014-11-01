package abedGame;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/*
NOTES:
    each gate has an output at direction 0 and an input at 2.
    if the gate has !=1 inputs, gate must override parentCheck()
    if the gate has !=1 outputs, gate must override childCheck()
*/

public abstract class Gate {
    
    public static String[][] gateTypes = new String[][]{
	new String[] {"Input","Output","Not","And","Or"},
        new String[] {"Single"},
	new String[] {"Half Adder", "Adder"}};
	
    public static String[] gateNames =
        new String[] {"Basic Gates","Wires","Adders"};
        
    boolean isOn;
    int rot;
    List<Gate> inputs;
    List<Gate> outputs;
     
    public boolean outputAtDir(int dir){
        return dir == rot %4 && isOn;
    }
            
    public void parentCheck(int i, int j){
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate.outputAtDir(rot))
                inputs.set(0, ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate);
            else throw new NullPointerException();
        }catch(NullPointerException ex) {}
    }
    
    public void childCheck(int i, int j){
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, rot%4).gate.outputAtDir(rot +2))
                inputs.set(0, ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, rot%4).gate);
            else throw new NullPointerException();
        }catch(NullPointerException ex) { }
    }
    
    public abstract Image getSprite();
    
    public boolean eval(){
        if(inputs.get(0) == null)
            return false;
        else return inputs.get(0).eval();
    }
	
    @Override
    public String toString(){
        return inputs.get(0).toString();
    }
        
    public void rotate(int r){    
        this.rot += (r % 4) + 4;
        this.rot %= 4;
    }    
}

class Input extends Gate{
    int inputNum;
    
    public Input(){
        this.isOn = false;
        this.rot = 0;
        this.inputs = new ArrayList<>(0);
        this.outputs = new ArrayList<>(1);
    }
        
    @Override
    public void parentCheck(int i, int j){}
    
    @Override
    public Image getSprite(){
        return new Image("images/Input"+(isOn? "1": "0")+".bmp");
    }

    @Override
    public boolean eval(){
        return isOn;
    }
	
    @Override
    public String toString(){
	return inputNum+"";
    }
}

class Output extends Gate{
	
    public Output(){
        this.inputs = new ArrayList<>(1);
        this.outputs = new ArrayList<>(0);
    }

    @Override
    public boolean outputAtDir(int dir) {
        return false;
    }
    
    @Override
    public void childCheck(int i, int j){}
    
    @Override
    public Image getSprite(){
        int p = 0;
        try{p = inputs.get(0).eval()? 1: 0;}
        catch(NullPointerException np){}
        
        return new Image("images/Output"+p+".bmp");
    }

}

class And extends Gate{
    
    public And(){
        this.inputs = new ArrayList<>(2);
        this.outputs = new ArrayList<>(1);
    }
    
    @Override
    public void parentCheck(int i, int j){
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate.outputAtDir(rot))
                inputs.set(0, ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate);
            else throw new NullPointerException();
        }catch(NullPointerException ex) {}
        
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+3)%4).gate.outputAtDir((rot+1)%4))
                inputs.set(0, ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+3)%4).gate);
            else throw new NullPointerException();
        }catch(NullPointerException ex) {}
    }
    
    @Override
    public Image getSprite(){
        int p1 = 0; int p2 = 0;
        try{ p1 = inputs.get(0).eval()? 1: 0; }
        catch(NullPointerException np){}
        try{ p2 = inputs.get(1).eval()? 1: 0; }
        catch(NullPointerException np){}
        
        return new Image("images/And"+p1+p2+".bmp");
    }
        
    @Override
    public boolean eval(){
        if(inputs.get(0) != null && inputs.get(1) != null)
            return inputs.get(0).eval() && inputs.get(1).eval();
        else return false;
    }

    @Override
    public String toString(){
        if(inputs.get(0) != null && inputs.get(1) != null)
            return "("+inputs.get(0).toString()+"^"+inputs.get(1).toString()+")";
        else return null;
    }
}

class Single extends Gate{
	
    public Single(){
        this.inputs = new ArrayList<>(1);
        this.outputs = new ArrayList<>(1);
    }
    
    @Override
    public boolean outputAtDir(int dir) {
        return dir != 2 && eval();
    }
    
    @Override
    public Image getSprite(){
        int p = 0;
        try{p = inputs.get(0).eval()? 1: 0;}
        catch(NullPointerException np){}
        return new Image("images/Single"+p+".bmp");
    }
}
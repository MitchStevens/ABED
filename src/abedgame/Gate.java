package abedGame;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public abstract class Gate {
    
    public static String[][] gateTypes = new String[][]{
	new String[] {"Input","Output","Not","And","Or"},
        new String[] {"Single"},
	new String[] {"Half Adder", "Adder"}};
	
    public static String[] gateNames =
        new String[] {"Basic Gates","Wires","Adders"};
        
    boolean isOn;
    int rot;
    int[] inputs;
    int[] outputs;
     
    public abstract boolean outputAtDir(int dir);
    public abstract void parentCheck(int i, int j);
    public abstract List<Gate> getParents();
    public abstract Image getSprite();
    public abstract boolean eval();
    @Override
    public abstract String toString();
    
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
        this.inputs = new int[0];
        this.outputs = new int[] {1}; 
    }

    @Override
    public boolean outputAtDir(int dir) {
        return dir == rot && isOn;
    }

    @Override
    public List<Gate> getParents() {
        return new ArrayList<>();
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

class Not extends Gate{
    Gate parent;
	
    public Not(){
        this.parent = null;
        this.rot = 0;
        this.inputs = new int[] {2};
        this.outputs = new int[] {0};
    }
    
    @Override
    public boolean outputAtDir(int dir) {
        return dir == rot && eval();
    }

    @Override
    public void parentCheck(int i, int j){
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate.outputAtDir(rot)){
                parent = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate;
            }
            else throw new NullPointerException();
        }catch(NullPointerException ex) {
            parent = null;
        }
    }
    
    @Override
    public List<Gate> getParents() {
        List<Gate> tbr = new ArrayList<>();
        tbr.add(parent);
        return tbr;
    }
    
    @Override
    public Image getSprite(){
        return new Image("images/Not"+(eval()? "0": "1")+".bmp");
    }
        
    @Override
    public boolean eval(){
        if(parent != null)
            return !parent.eval();
        else return true;
    }
	
    @Override
    public String toString(){
        if(parent != null)
            return "~"+parent.toString();
        else return null;
    }
}

class Output extends Gate{
    Gate parent;
	
    public Output(){
        this.inputs = new int[0];
        this.outputs = new int[] {0};
    }

    @Override
    public boolean outputAtDir(int dir) {
        return false;
    }
	
    @Override
    public void parentCheck(int i, int j){
        if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, rot) != null)
            parent = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, rot).gate;
        else parent = null;
    }
    
    @Override
    public List<Gate> getParents() {
        List<Gate> tbr = new ArrayList<>();
        tbr.add(parent);
        return tbr;
    }
    
    @Override
    public Image getSprite(){
        int p = 0;
        try{p = parent.eval()? 1: 0;}
        catch(NullPointerException np){}
        
        return new Image("images/Output"+p+".bmp");
    }
        
    @Override
    public boolean eval(){
	if(parent != null)
            return parent.eval();
        else return false;
    }
    
    @Override
    public String toString(){
        if(parent != null)
            return parent.toString();
        else return "F";
    }
}

class And extends Gate{
    Gate parent1;
    Gate parent2;
	
    public And(){
        this.parent1 = null;
        this.parent2 = null;
        this.inputs  = new int[] {2, 3};
        this.outputs = new int[] {0};
    }

    @Override
    public boolean outputAtDir(int dir) {
        return dir == rot && eval();
    }
    
    @Override
    public void parentCheck(int i, int j){
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate.outputAtDir(rot))
                parent1 = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate;
            else throw new NullPointerException();
        }catch(NullPointerException ex) {
            parent1 = null;
        }
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+3)%4).gate.outputAtDir((rot+1)%4))
                parent2 = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+3)%4).gate;
            else throw new NullPointerException();
        }catch(NullPointerException ex) {
            parent2 = null;
        }
    }
    
    @Override
    public List<Gate> getParents() {
        List<Gate> tbr = new ArrayList<>();
        tbr.add(parent1);
        tbr.add(parent2);
        return tbr;
    }
    
    @Override
    public Image getSprite(){
        int p1 = 0; int p2 = 0;
        try{ p1 = parent1.eval()? 1: 0; }
        catch(NullPointerException np){}
        try{ p2 = parent2.eval()? 1: 0; }
        catch(NullPointerException np){}
        
        return new Image("images/And"+p1+p2+".bmp");
    }
        
    @Override
    public boolean eval(){
        if(parent1 != null && parent2 != null)
            return parent1.eval() && parent2.eval();
        else return false;
    }

    @Override
    public String toString(){
        if(parent1 != null && parent2 != null)
            return "("+parent1.toString()+"^"+parent2.toString()+")";
        else return null;
    }
}

class Or extends Gate{
    Gate parent1;
    Gate parent2;
	
    public Or(){
        this.parent1 = null;
        this.parent2 = null;
        this.inputs  = new int[] {2, 3};
        this.outputs = new int[] {0};
    }

    @Override
    public boolean outputAtDir(int dir) {
        return dir == rot && eval();
    }
    
    @Override
    public void parentCheck(int i, int j){
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate.outputAtDir(rot))
                parent1 = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate;
            else throw new NullPointerException();
        }catch(NullPointerException ex) {
            parent1 = null;
        }
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+3)%4).gate.outputAtDir((rot+1)%4))
                parent2 = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+3)%4).gate;
            else throw new NullPointerException();
        }catch(NullPointerException ex) {
            parent2 = null;
        }
    }
    
    @Override
    public List<Gate> getParents() {
        List<Gate> tbr = new ArrayList<>();
        tbr.add(parent1);
        tbr.add(parent2);
        return tbr;
    }
    
    @Override
    public Image getSprite(){
        int p1 = 0; int p2 = 0;
        try{ p1 = parent1.eval()? 1: 0; }
        catch(NullPointerException np){}
        try{ p2 = parent2.eval()? 1: 0; }
        catch(NullPointerException np){}
        
        return new Image("images/And"+p1+p2+".bmp");
    }
        
    @Override
    public boolean eval(){
        if(parent1 != null && parent2 != null)
            return parent1.eval() || parent2.eval();
        else return false;
    }

    @Override
    public String toString(){
        if(parent1 != null && parent2 != null)
            return "("+parent1.toString()+"v"+parent2.toString()+")";
        else return null;
    }
}

class Single extends Gate{
    Gate parent;
	
    public Single(){
        this.inputs = new int[] {2};
        this.outputs = new int[] {3, 0, 1};
    }
    
    @Override
    public boolean outputAtDir(int dir) {
        return dir != 2 && eval();
    }
	
    @Override
    public void parentCheck(int i, int j){
        try{
            if(ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate.outputAtDir(rot)){
                parent = ABEDGUI.getBoard().currentGame.pieceAtDir(i, j, (rot+2)%4).gate;
                System.out.println("not has some daddy");}
            else throw new NullPointerException();
        }catch(NullPointerException ex) {
            parent = null;
            System.out.println("not has no daddy");
        }
    }
    
    @Override
    public List<Gate> getParents() {
        List<Gate> tbr = new ArrayList<>();
        tbr.add(parent);
        return tbr;
    }
    
    @Override
    public Image getSprite(){
        int p = 0;
        try{p = parent.eval()? 1: 0;}
        catch(NullPointerException np){}
        return new Image("images/Single"+p+".bmp");
    }
        
    @Override
    public boolean eval(){
        if(parent == null)
            return false;
        else return parent.eval();
    }
	
    @Override
    public String toString(){
        return parent.toString();
    }
}
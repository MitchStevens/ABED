package abedgame;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static abedgame.Functions.*;

public class Game {
    Gate[][] placed;
    int n;
        
    public Game(int n){
        placed = new Gate[n][n];
        this.n = n;
    }
        
    public Game(List<Gate> gates, int n){
        placed = new Gate[n][n];
        for(Gate g : gates)
            placed[g.i][g.j] = g;
    }
    
    public boolean addGate(Gate g, int i, int j){
    	//returns true if gate was added to game, else false
    	if(i >= n || j >= n) return false;
    	if(placed[i][j] != null) return false;
    	placed[i][j] = g;
    	g.i = i; g.j = j;
    	return true;
    }
    
    public boolean removeGate(Gate g){
    	try{
    		if(placed[g.i][g.j].equals(g)){
    			placed[g.i][g.j] = null;
    			return true;
    		}
    		return false;
    	} catch(NullPointerException np){
    		return false;
    	}
    }
    
//    public void createGateAtEmpty(Gate newGate){
//    	int i, j;
//    	for(i = 0; i < n; i++)
//    		for(j = 0; j < n; j++)
//    			if(placed[i][j] == null){
//    				newGate.i = i;
//    				newGate.j = j;
//    				placed[i][j] = newGate;
//    				return;
//    			}
//    }
//    
//    public void printGame(){
//    	System.out.println(dup("__", n)+"_");
//    	for(int i = 0 ; i < n; i++){
//			System.out.print("|");
//    		for(int j = 0; j < n; j++){
//    			if(placed[i][j] != null)
//    				System.out.print(placed[i][j].name.charAt(0)+"|");
//    			else System.out.print(" |");
//    		}
//    		System.out.println();
//    	}
//    	System.out.println(dup("¯¯", n)+"¯");
//    }
    
//    public void printGameInfo(){
//    	filter(g -> g != null, flatten(placed)).forEach(g -> {
//    		System.out.println("Gate: "+g.name);
//    		System.out.println("\tInputs: "+g.allValidInputs());
//    		System.out.println("\tGate Sprite: "+g.name + cat(g.inputBus));
//    	});
//    }
    
    @Override
    public String toString(){
    	List<String> tbr = new ArrayList<>();
    	for(int i = 0 ; i < n; i++)
    		for(int j = 0; j < n; j++)
    			if(placed[i][j] != null)
    				if("Output".equals(placed[i][j].name))
    				tbr.add(placed[i][j].toString());
    	return tbr.toString();
    }
	
}

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
    
    public void createGateAtEmpty(Gate newGate){
    	int i, j;
    	for(i = 0; i < n; i++)
    		for(j = 0; j < n; j++)
    			if(placed[i][j] == null){
    				newGate.i = i;
    				newGate.j = j;
    				placed[i][j] = newGate;
    				return;
    			}
    }
    
    //SELECT Gate FROM placed WHERE Gate == <type>
    Function<String, Integer> countGateType = type -> filter(g -> type.equals(g.name), flatten(placed)).size();
  

//	public String convertToGate(){
//    	String tbr = "NAME_OF_GATE;";
//    	Integer[] inputDir = new Integer[4];
//    	Integer[] outputDir = new Integer[4];
//    	String[] logic = new String[4];
//    	
//    	//ArrayList<ArrayList<Gate>> allInputs = getSideType("Input");
//    	//ArrayList<ArrayList<Gate>> allOutputs = getSideType("Output");
//    	
//    	for(int i = 0; i < 4; i++)
//    		for(Gate g : allInputs.get(i))
//    			inputDir[i]++;
//    	
//    	for(int i = 0; i < 4; i++)
//    		for(Gate g : allOutputs.get(i)){
//    			outputDir[i]++;
//    			logic[i] = g.toString();
//    		}
//
//    	for(int i : inputDir) tbr += i+",";
//    	tbr += ";";
//    	
//    	for(int i : outputDir) tbr += i+",";
//    	tbr += ";";
//    			
//    	for(String s : logic) tbr += s+",";
//    	return tbr;
//    }
    
    public void printGame(){
    	System.out.println(dup("__", n)+"_");
    	for(int i = 0 ; i < n; i++){
			System.out.print("|");
    		for(int j = 0; j < n; j++){
    			if(placed[i][j] != null)
    				System.out.print(placed[i][j].name.charAt(0)+"|");
    			else System.out.print(" |");
    		}
    		System.out.println();
    	}
    	System.out.println(dup("¯¯", n)+"¯");
    }
    
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

package abedgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    Piece[][] placed;
    int n;
        
    public Game(int n){
        placed = new Piece[n][n];
        this.n = n;
    }
    
//    public boolean[] eval(){
//    	return true;
//    }
        
    public Game(List<Piece> pieces, int n){
        placed = new Piece[n][n];
        for(Piece p : pieces)
            placed[p.i][p.j] = p;
    }
    
    public void tick(Gate gate){
    	if(gate.i == null) return;
    	
    	for(Gate g : gate.inputs)
    		if(g != null) g.outputCheck();
    	for(Gate g : gate.outputs)
    		if(g != null) g.inputCheck();
    	
    	gate.inputCheck();
    	gate.outputCheck();
    	
    	for(Gate g : gate.inputs)
    		if(g != null) g.outputCheck();
    	for(Gate g : gate.outputs)
    		if(g != null) g.inputCheck();
    	
    	updateGame();
    	System.out.println(this.toString());
    }
    
    public void placePieceAtEmpty(Piece newPiece){
    	int i, j;
    	for(i = 0; i < n; i++)
    		for(j = 0; j < n; j++)
    			if(placed[i][j] == null){
    				newPiece.i = i;
    				newPiece.j = j;
    				newPiece.setLayoutX(ABEDGUI.allSquares.get(i*n +j).getLayoutX());
    				newPiece.setLayoutY(ABEDGUI.allSquares.get(i*n +j).getLayoutY());
    				ABEDGUI.getBoard().root.getChildren().add(newPiece);
    				placed[i][j] = newPiece;
    				return;
    			}
    }
    
    public void updateGame(){
    	//only updates graphical components
    	for(Piece[] pArray : placed)
    		for(Piece p : pArray){
    			if(p == null) continue;
    			else p.updateImage();
    		}	
    }
    
    private boolean posAtDir(int i, int j, int dir){
        switch(dir%4){
            case 0: return i > 0;
            case 1: return j < n-1;
            case 2: return i < n-1;
            case 3: return j > 0;
            default: throw new Error(dir+" is not a legit direction!");
        }
    }
    
    public Piece pieceAtDir(int i, int j, int dir){
        if(!posAtDir(i, j, dir)) return null;
            switch(dir%4){
                case 0: return placed[i-1][j];
                case 1: return placed[i][j+1];
                case 2: return placed[i+1][j];
                case 3: return placed[i][j-1];
                default: throw new Error(dir+" is not a legit direction!");
            }
    } 
    
    public List<Piece> nonNullPieces(){
    	List<Piece> tbr = new ArrayList<>();
    	for(int i = 0 ; i < n; i++)
    		for(int j = 0; j < n; j++)
    			if(placed[i][j] != null)
    				tbr.add(placed[i][j]);
    	return tbr;
    }
    
    public int inputNum(){
    	int tbr = 0;
    	for(int i = 0 ; i < n; i++)
    		for(int j = 0; j < n; j++)
    			if(placed[i][j] != null)
    				if(placed[i][j].gate instanceof Input)
    					tbr++;
    	return tbr;
    }
    
    public int outputNum(){
    	int tbr = 0;
    	for(int i = 0 ; i < n; i++)
    		for(int j = 0; j < n; j++)
    			if(placed[i][j] != null)
    				if(placed[i][j].gate instanceof Output)
    					tbr++;
    	return tbr;
    }
    
    @Override
    public String toString(){
    	List<String> tbr = new ArrayList<>();
    	for(int i = 0 ; i < n; i++)
    		for(int j = 0; j < n; j++)
    			if(placed[i][j] != null)
    				if(placed[i][j].gate instanceof Output)
    				tbr.add(placed[i][j].gate.toString());
    	return tbr.toString();
    }
	
}

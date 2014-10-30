package abedGame;

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
        
    public Game(List<Piece> pieces, int n){
        placed = new Piece[n][n];
        for(Piece p : pieces)
            placed[p.i][p.j] = p;
    }
    
//    public void tick(Piece p){
//        List<Piece> oldChildren = this.getChildren(p);
//        updateRelations();
//        for(Piece a : oldChildren)
//            tickRec(a);
//        tickRec(p);
//    }
//    
//    public void tickRec(Piece p){
//        List<Piece> kids = this.getChildren(p);
//        p.updateImage();
//        for(Piece a : kids)
//            tickRec(a);
//    }
//    
//    public void updateRelations(){
//        for(int i = 0; i < n; i++)
//            for(int j = 0; j < n; j++)
//                if(placed[i][j] != null)
//                    placed[i][j].gate.parentCheck(i, j);
//    }
    
    private boolean posAtDir(int i, int j, int dir){
        switch(dir){
            case 0: return i > 0;
            case 1: return j < n-1;
            case 2: return i < n-1;
            case 3: return j > 0;
            default: throw new Error(dir+" is not a legit direction!");
        }
    }
    
    public Piece pieceAtDir(int i, int j, int dir){
        if(!posAtDir(i, j, dir)) return null;
            switch(dir){
                case 0: return placed[i-1][j];
                case 1: return placed[i][j+1];
                case 2: return placed[i+1][j];
                case 3: return placed[i][j-1];
                default: throw new Error(dir+" is not a legit direction!");
            }
    }
    
    public List<Piece> getChildren(Piece p){
        List<Piece> tbr = new ArrayList<>();
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                if(placed[i][j] != null)
                    if(placed[i][j].gate.getParents().contains(p.gate))
                        tbr.add(placed[i][j]);
        String a = (!tbr.isEmpty())? tbr.get(0).gate.getClass().getSimpleName(): "none";
        System.out.println(p.gate.getClass().getSimpleName()+" has children: "+a);
        return tbr;
    }
    
    @Override
    public String toString(){
        String tbr = " ";
        for(Piece[] array : placed){
            for(Piece p : array){
                if(p != null)
                    tbr += p.gate.getClass().getSimpleName().charAt(0)+"  ";
                else tbr += "*  ";
            }
            tbr += "\n ";
        }
        return tbr;
    }
    
    public void relationships(){
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                if(placed[i][j] != null)
                    getChildren(placed[i][j]);

    }
	
}

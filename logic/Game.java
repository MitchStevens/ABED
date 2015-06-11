package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static logic.Circuit.flatten;
import data.Reader;

public class Game {
	
	public static Map<String, Game> loadedGames = Reader.loadGames();
	
	public Circuit[][] tileGrid;
	public int n;
	public String name;
	
	public Game(int n){
		this.n = n;
		this.tileGrid = new Circuit[n][n];
	}
	
	public Game(String datum){
		//NAME;SIZE;cNAME,ROT,iPos,jPos;cNAME,ROT,iPos,jPos;...
		String[] data = datum.split(";");
		this.name = data[0];
		this.n = Integer.parseInt(data[1]);
		this.tileGrid = new Circuit[n][n];
		for(int i = 2; i < data.length; i++){
			String[] cData = data[i].split(",");
			Circuit c = Circuit.loadedCircuits.get(cData[0]).clone();
			c.setRot(Integer.parseInt(cData[1]));
			this.add(c, cData[2], cData[3]);
		}
	}
	
	private boolean add(Circuit c, String i, String j){
		return add(c, Integer.parseInt(i), Integer.parseInt(j));
	}
	
	public boolean add(Circuit c){
		//Adds circuits to an unspecified position on the board
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(tileGrid[i][j] == null){
					add(c, i, j);
					return true;
				}
		return false;
	}
	
	public boolean add(Circuit c, int i, int j){
		//Add circuit to tileGrid if possible and return true, if not return false.
		if(i < 0 || i >= n || j < 0 || j >= n) return false;
		if(tileGrid[i][j] != null) return false;
		if(c == null) return false;
		c.i = i; c.j = j;
		c.setGame(this);
		tileGrid[i][j] = c;
		updateGame(i, j);
		return true;
	}
	
	public boolean move(Circuit c, int i, int j){
		//move c from where ever is was to (i, j)
		if(add(c, i, j)){
			tileGrid[c.i][c.j] = null;
			updateGame(i, j);
			return true;
		} else
			return false;
	}
	
	public void remove(int i, int j){
		tileGrid[i][j] = null;
	}
	
	public List<Circuit> getAdj(int i, int j){
		//gets all adjacent circuits
		List<Circuit> tbr = new ArrayList<>();
		if(j > 0) 	tbr.add(tileGrid[i][j-1]);
		if(i < n-1) tbr.add(tileGrid[i+1][j]);
		if(j < n-1)	tbr.add(tileGrid[i][j+1]);
		if(i > 0)	tbr.add(tileGrid[i-1][j]);
		
		Iterator<Circuit> iter = tbr.iterator();
		iter.forEachRemaining(c -> {if(c == null) tbr.remove(c);});
		return tbr;
	}
	
	public void updateGame(int i, int j) {
		updateGame(tileGrid[i][j]);
	}
	
	public void updateGame(Circuit c){
		if(c == null) return;
		c.updateInputs();
		
		//then update the circuits around it that c outputs to (to prevent infinite loops)
		for(int dir = 0; dir < 4; dir++)
			if(c.validOutputAtDir(dir) != null)
				updateGame(c.circuitAtDir(dir));
	}
	
	public Bus outputsAtDir(int dir){
		Bus tbr = new Bus();
		Circuit c;
		switch(dir){
		case 0:
			for(int i = 0; i < n; i++){
				c = tileGrid[i][0];
				if(c == null) continue;
				if(c.name.equals("Output"))
					tbr.add(c.inputList().get(0));
			}
			break;
		case 1:
			for(int j = 0; j < n; j++){
				c = tileGrid[n-1][j];
				if(c == null) continue;
				if(c.name.equals("Output"))
					tbr.add(c.inputList().get(0));
			}
			break;
		case 2:
			for(int i = n-1; i >= 0; i--){
				c = tileGrid[i][n-1];
				if(c == null) continue;
				if(c.name.equals("Output"))
					tbr.add(c.inputList().get(0));
			}
			break;
		case 3:
			for(int j = n-1; j >= 0; j--){
				c = tileGrid[0][j];
				if(c == null) continue;
				if(c.name.equals("Output"))
					tbr.add(c.inputList().get(0));
			}
			break;
		}
		
		return tbr;
	}
	
	public void clear(){
		//removes circuits but maintains size
		this.tileGrid = new Circuit[n][n];
	}
	
	public String printGame(){
		//returns a pretty visualisation of the current game so I know what I'm testing.
		String tbr = "|";
		for(int i = 0; i < n-1; i++)
			tbr += "----";
		tbr += "---|\n";
		
		for(int j = 0; j < n; j++){
			String[] crntRow = new String[3];
			crntRow = concat(crntRow, "|");
			for(int i = 0; i < n; i++){
				if(tileGrid[i][j] != null)
					crntRow = concat(crntRow, tileGrid[i][j].printCircuit());
				else crntRow = concat(crntRow, "   ");
				if(i != n-1)
					crntRow = concat(crntRow, "|");
			}
			tbr += crntRow[0]+"|\n"+crntRow[1]+"|\n"+crntRow[2]+"|\n";
			if(j != n-1){
				tbr += "|";
				for(int i = 0; i < n-1; i++)
					tbr += "---|";
				tbr += "---|\n";}
		}
		
		tbr += "|";
		for(int i = 0; i < n-1; i++)
			tbr += "----";
		tbr += "---|\n";
		
		return tbr;		
	}
	
	public String printGameInfo(){
		//outputs a textual representation of current game
		String tbr = "";
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(tileGrid[i][j] != null){
					Circuit c = tileGrid[i][j];
					tbr += "("+i+", "+j+"): "+c.name+"\n";
					tbr += "    rot: "+c.rot+"\n";
				}
		return tbr;	
	}
	
	private String[] concat(String[] a, String[] b){
		String[] tbr = new String[Math.min(a.length, b.length)];
		for(int i = 0; i < tbr.length; i++)
			if(a[i] == null)
				tbr[i] = b[i];
			else tbr[i] = a[i]+b[i];
		return tbr;
	}
	
	private String[] concat(String[] a, String b){
		String[] tbr = new String[a.length];
		for(int i = 0; i < tbr.length; i++)
			if(a[i] == null)
				tbr[i] = b;
			else tbr[i] = a[i]+b;
		return tbr;
	}
}











package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import abedgui.Square;
import static logic.Circuit.flatten;
import static logic.Circuit.mod4;
import data.Reader;

public class Game {
	
	public static Map<String, Game> loadedGames = Reader.loadGames();
	
	private Circuit[][] tileGrid;
	public int n;
	public String name;
	
	public Game(int n){
		name = "NAMELESS_GAME";
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
		Square s = null;
		if((s = nextOpen()) != null){
			add(c, s.i, s.j);
			return true;
		} else
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
		c.updateInputs();
		updateGame(i, j);
		return true;
	}
	
	public boolean move(int i1, int j1, int i2, int j2){
		//move c from where ever is was to (i, j)
		Circuit c = tileGrid[i1][j1];
		if(c == null) return false;
		if(tileGrid[i2][j2] == null){
			remove(c.i, c.j);
			add(c, i2, j2);
			return true;
		} else
			return false;
	}
	
	public boolean move(Circuit c, int i2, int j2){
		//move c from where ever is was to (i, j)
		if(tileGrid[i2][j2] == null){
			remove(c.i, c.j);
			add(c, i2, j2);
			return true;
		} else
			return false;
	}
	
	public void rotate(int i, int j, int rot){
		tileGrid[i][j].addRot(rot);
		updateGame(i, j);
	}
	
	public void remove(int i, int j){
		tileGrid[i][j] = null;
		updateGame(i, j);
	}
	
	public void toggle(int i, int j){
		Circuit c = tileGrid[i][j];
		if(c != null)
			c.toggle();
	}
	
	public List<Circuit> getAdj(int i, int j){
		//gets all adjacent circuits. If circuit at dir not valid, circuit is represented as a null;
		List<Circuit> tbr = new ArrayList<>();
		
		if(j > 0)
			tbr.add(tileGrid[i][j-1]);
		else tbr.add(null);
		
		if(i < n-1)
			tbr.add(tileGrid[i+1][j]);
		else tbr.add(null);
		
		if(j < n-1)
			tbr.add(tileGrid[i][j+1]);
		else tbr.add(null);
		
		if(i > 0)
			tbr.add(tileGrid[i-1][j]);
		else tbr.add(null);
		
		return tbr;
	}
	
	public void updateGame(Circuit c){
		updateGame(c.i, c.j);
	}
	
	public void updateGame(int i, int j) {
		Circuit c = tileGrid[i][j];
		if(c != null) {
			c.updateInputs();
			//then update the circuits around it that c outputs to (to prevent infinite loops)
			for(int dir = 0; dir < 4; dir++)
				if(c.validOutputAtDir(dir) != null){
					updateGame(circuitAtDir(c, dir));
					}
		} else {
			for(int dir = 0; dir < 4; dir++){
				Square s = posAtDir(i, j, dir);
				Circuit d = circuitAtPos(s.i, s.j);
				if(d == null) continue;
				if(d.inputAtAbsDir(dir-2).size() > 0){
					updateGame(d);					
				}
			}
		}
	}
	
	public Circuit circuitAtDir(Circuit c, int dir){
		try {
			Square s = posAtDir(c.i, c.j, dir);
			return tileGrid[s.i][s.j];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	public Circuit circuitAtPos(int i, int j){
		try {
			return tileGrid[i][j];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	public Square posAtDir(int i, int j, int dir){
		//returns square (read: tuple) with co-ords to the position relative to this circuit.
		switch(dir){
		case 0:
			return new Square(i, j-1);
		case 1:
			return new Square(i+1, j);
		case 2:
			return new Square(i, j+1);
		case 3:
			return new Square(i-1, j);
		default:
			return null;
		}
	}
	
	public Square nextOpen(){
		//returns the next open co-ords in the form of a Square
		for(int j = 0; j < n; j++)
			for(int i = 0; i < n; i++)
				if(tileGrid[i][j] == null){
					return new Square(i, j);
				}
		return null;
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
	
	@Override
	public String toString(){
		//NAME;SIZE;cNAME,ROT,iPos,jPos;cNAME,ROT,iPos,jPos;...
		String tbr = name+";"+n+";";
		Circuit c = null;
		for(int j = 0; j < n; j++)
			for(int i = 0; i < n; i++)
				if((c = tileGrid[i][j]) != null)
					tbr += c.toString()+";";
		return tbr;
	}
}











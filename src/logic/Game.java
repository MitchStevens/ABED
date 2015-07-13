package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import panes.GamePane;
import abedgui.Square;
import static java.util.stream.Collectors.toList;
import static logic.Evaluator.init;
import data.Reader;

public class Game {
	public final static int MIN_TILES = 3;
	public final static int MAX_TILES = 10;
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
		/* adds rot to circuit in game
		 * ORiginal code
		 * tileGrid[i][j].addRot(rot);
		 * updateGame(i, j);
		 * I shouldn't have to go to all this trouble to rotate a tile. Why aren't the children
		 * of the circuit picking up on the rotational change?
		 * */
		Circuit c = tileGrid[i][j];
		remove(i, j);
		c.addRot(rot);
		add(c, i, j);
		
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
	
	public Circuit toCircuit(){
		String tbr = "NAME_WHATEVER;";
		List<Circuit> inputs = new ArrayList<>();
		List<Circuit> outputs = new ArrayList<>();
		List<Circuit> temp;
		
		for(int dir = 0; dir < 4; dir++){
			temp = circuitsOnEdge(dir, "Input");
			inputs.addAll(temp);
			tbr += temp.size()+",";
		}
		tbr = init(tbr)+";";
		
		for(int dir = 0; dir < 4; dir++){
			temp = circuitsOnEdge(dir, "Output");
			outputs.addAll(temp);
			tbr += temp.size()+",";
		}
		tbr = init(tbr)+";";
		
		for(Circuit c : outputs){
			String logic = c.outputAsString(1);
			tbr += logic+",";
		}
		return new Circuit(init(tbr));
	}
	
	public List<Circuit> circuitsOnEdge(int edge){
		//gets all circuits on a given edge. Ignores corners
		List<Circuit> tbr = new ArrayList<>();
		Circuit c;
		switch(edge){
		case 0:
			for(int i = 1; i < n-1; i++)
				if((c = tileGrid[i][0]) != null)
					tbr.add(c);
			break;
		case 1:
			for(int j = 1; j < n-1; j++)
				if((c = tileGrid[n-1][j]) != null)
					tbr.add(c);
			break;
		case 2:
			for(int i = n-2; i >= 1; i--)
				if((c = tileGrid[i][n-1]) != null)
					tbr.add(c);
			break;
		case 3:
			for(int j = n-2; j >= 1; j--)
				if((c = tileGrid[0][j]) != null)
					tbr.add(c);
			break;
		}
		return tbr;
	}
	
	public List<Circuit> circuitsOnEdge(int edge, String name){
		return circuitsOnEdge(edge)
				.stream()
				.filter(c -> c.name.equals(name))
				.collect(toList());
	} 
	
	public Bus outputBusAtDir(int dir){
		List<Circuit> outputs = circuitsOnEdge(dir);
		outputs = outputs
				.stream()
				.filter(c -> c.name.equals("Output"))
				.collect(toList());
		Bus tbr = new Bus();
		for(Circuit c : outputs){
			tbr.add(c.inputList().get(0));
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
	public Game clone(){
		return new Game(toString());		
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











package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import tutorials.Action;
import circuits.*;
import data.Reader;
import static eval.Evaluator.init;
import static java.util.stream.Collectors.toList;
import static java.lang.Math.min;

public class Game extends Observable{
	/* There are many operations involving circuits on a given game (add, remove, rotate, move)
	 * but there are only 2 fundamental operations, add and remove. All other operation are
	 * specific removals and additions to a game. By letting add() and remove() do all the real work,
	 * we cut down on the amount of repeated code lessen confusion about what does what.
	 * */
	
	public final 	static 	int 				MIN_TILES = 3;
	public final 	static 	int 				MAX_TILES = 10;
	private 				Circuit[][] 		tileGrid;
	public 					int 				n;
	public 					String 				name;
	
	public Game(int n) {
		name = "NAMELESS_GAME";
		this.n = n;
		this.tileGrid = new Circuit[n][n];
	}

	public Game(String datum) {
		// NAME;SIZE;cNAME,ROT,iPos,jPos;cNAME,ROT,iPos,jPos;...
		String[] data = datum.split(";");
		this.name = data[0];
		this.n = Integer.parseInt(data[1]);
		this.tileGrid = new Circuit[n][n];
		for (int i = 2; i < data.length; i++) {
			String[] cData = data[i].split(",");
			Circuit c = Reader.ALL_CIRCUITS.get(cData[0]).clone();
			c.setRot(Integer.parseInt(cData[1]));
			this.add(c, cData[2], cData[3]);
		}
	}
	
	public Game(String name, int n){
		this.name = name;
		this.n = n;
	}
	
	public void set_size(int size){
		Circuit[][] grid = new Circuit[size][size];
		
		for(int i = 0; i < min(size, n)-1; i++)
			for(int j = 0; j < min(size, n)-1; j++)
				grid[i][j] = tileGrid[i][j];
		
		tileGrid = grid;
		n = size;
	}

	private boolean add(Circuit c, String i, String j) {
		return add(c, new Coord(Integer.parseInt(i), Integer.parseInt(j)));
	}
	
	public boolean add(Circuit c, Integer i, Integer j) {
		return add(c, new Coord(i, j));
	}

	public boolean add(Circuit c) {
		// Adds circuits to an unspecified position on the board
		Coord coord = nextOpen();
		if (coord != null) {
			add(c, coord);
			return true;
		} else
			return false;
	}

	//FUNDAMENTAL OPERATION
	public boolean add(Circuit c, Coord coord) {
		// Add circuit to tileGrid if possible and return true, if not return
		// false.
		if (coord.i < 0 || coord.i >= n || coord.j < 0 || coord.j >= n)
			return false;
		else if (tileGrid[coord.i][coord.j] != null)
			return false;
		else if (c == null)
			return false;
		
		c.coord = new Coord(coord.i, coord.j);
		c.setGame(this);
		tileGrid[coord.i][coord.j] = c;
		c.recoupleBuses();
		c.eval();
		this.notifyObservers(Action.add(c));
		this.setChanged();
		return true;
	}

	public boolean move(Coord c1, Coord c2) {
		// move c from where ever is was to (i, j)
		Circuit circ = tileGrid[c1.i][c1.j];
		if (circ == null)
			return false;
		
		if (tileGrid[c2.i][c2.j] == null) {
			remove(circ.coord);
			add(circ, c2);
			this.notifyObservers(Action.move(circ));
			return true;
		} else
			return false;
	}

	public void rotate(Coord coord, int rot) {
		Circuit circ = tileGrid[coord.i][coord.j];
		remove(coord);
		circ.addRot(rot);
		add(circ, coord);
		this.notifyObservers(Action.rotate(circ));
	}

	public void remove(Coord coord) {
		if(tileGrid[coord.i][coord.j] == null)
			return;
		
		Circuit c = tileGrid[coord.i][coord.j];
		tileGrid[coord.i][coord.j].uncoupleBuses();
		tileGrid[coord.i][coord.j].game = null;
		tileGrid[coord.i][coord.j] = null;
		this.notifyObservers(Action.remove(c));
		this.setChanged();
	}

	public void toggle(Coord coord) {
		Circuit circ = tileGrid[coord.i][coord.j];
		if (circ != null)
			circ.toggle();
	}
	
	public Circuit circuitAtDir(Coord coord, int dir) {
		try {
			Coord newCoord = posAtDir(coord.i, coord.j, dir);
			return tileGrid[newCoord.i][newCoord.j];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Circuit circuitAtPos(Coord coord) {
		try {
			return tileGrid[coord.i][coord.j];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void removeRange(int i1, int j1, int i2, int j2){
		for(int i = i1; i <= i2; i++)
			for(int j = j1; j <= j2; j++)
				if(tileGrid[i][j] != null)
					this.remove(new Coord(i, j));
	}
	
	public Coord posAtDir(int i, int j, int dir) {
		// returns square (read: tuple) with co-ords to the position relative to
		// this circuit.
		switch (dir % 4) {
		case 0:
			return new Coord(i, j - 1);
		case 1:
			return new Coord(i + 1, j);
		case 2:
			return new Coord(i, j + 1);
		case 3:
			return new Coord(i - 1, j);
		default:
			return null;
		}
	}

	public Coord nextOpen() {
		// returns the next open co-ords in the form of a Square. Corners are
		// ignored.
		for (int j = 0; j < n; j++)
			for (int i = 0; i < n; i++) {
				if (tileGrid[i][j] == null) {
					// this monstrosity used to make sure we don't output any
					// corner squares as "opne".
					if (i == 0 && j == 0 || i == 0 && j == n - 1 || i == n - 1
							&& j == 0 || i == n - 1 && j == n - 1)
						continue;
					return new Coord(i, j);
				}
			}
		return null;
	}

	public Circuit toCircuit() {
		//A valid game (i.e. one that can be turned into a circuit), 
		//must have exactly (0|1) input/output on each side
		String tbr = "NAME_WHATEVER;";
		List<Circuit> inputs = new ArrayList<>();
		List<Circuit> outputs = new ArrayList<>();
		List<Circuit> temp;

		for (int dir = 0; dir < 4; dir++) {
			temp = circuitsOnEdge(dir, "INPUT");
			
			if(temp.size() > 1) return null;
			
			inputs.addAll(temp);
			tbr += temp.size() + ",";
		}
		tbr = init(tbr) + ";";

		for (int dir = 0; dir < 4; dir++) {
			temp = circuitsOnEdge(dir, "OUTPUT");
			
			if(temp.size() > 1) return null;
			
			outputs.addAll(temp);
			tbr += temp.size() + ",";
		}
		tbr = init(tbr) + ";";

		for (Circuit c : outputs) {
			//String logic = c.outputAsString(1);
			tbr += c.buses.get(3).outputAsString() + ",";
		}
		return new Circuit(init(tbr));
	}

	private List<Circuit> circuitsOnEdge(int edge) {
		// gets all circuits on a given edge. Ignores corners
		List<Circuit> tbr = new ArrayList<>();
		Circuit c;
		switch (edge) {
		case 0:
			for (int i = 1; i < n - 1; i++)
				if ((c = tileGrid[i][0]) != null)
					tbr.add(c);
			break;
		case 1:
			for (int j = 1; j < n - 1; j++)
				if ((c = tileGrid[n - 1][j]) != null)
					tbr.add(c);
			break;
		case 2:
			for (int i = n - 2; i >= 1; i--)
				if ((c = tileGrid[i][n - 1]) != null)
					tbr.add(c);
			break;
		case 3:
			for (int j = n - 2; j >= 1; j--)
				if ((c = tileGrid[0][j]) != null)
					tbr.add(c);
			break;
		}
		return tbr;
	}

	public List<Circuit> circuitsOnEdge(int edge, String name) {
		return circuitsOnEdge(edge).stream().filter(c -> c.name.equals(name))
				.collect(toList());
	}

	public List<Boolean> flattenOutputs(){
		List<Boolean> tbr = new ArrayList<>();
		
		for(int i = 0; i < 4; i++)
			for(Circuit c : circuitsOnEdge(i, "Output"))
				tbr.add(((Output)c).get());
		
		return tbr;
	}
	
	public void clear() {
		// removes circuits but maintains size
		this.tileGrid = new Circuit[n][n];
        setChanged();
        notifyObservers();
	}

	public void printGame() {
		// returns a pretty visualisation of the current game so I know what I'm
		// testing.
		String tbr = "|";
		for (int i = 0; i < n - 1; i++)
			tbr += "-------";
		tbr += "------|\n";

		for (int j = 0; j < n; j++) {
			String[] crntRow = new String[3];
			crntRow = concat(crntRow, "|");
			for (int i = 0; i < n; i++) {
				if (tileGrid[i][j] != null)
					crntRow = concat(crntRow, tileGrid[i][j].printCircuit());
				else
					crntRow = concat(crntRow, "      ");
				if (i != n - 1)
					crntRow = concat(crntRow, "|");
			}
			tbr += crntRow[0] + "|\n" + crntRow[1] + "|\n" + crntRow[2] + "|\n";
			if (j != n - 1) {
				tbr += "|";
				for (int i = 0; i < n - 1; i++)
					tbr += "------|";
				tbr += "------|\n";
			}
		}

		tbr += "|";
		for (int i = 0; i < n - 1; i++)
			tbr += "-------";
		tbr += "------|\n";

		System.out.println(tbr);
	}

	private String[] concat(String[] a, String[] b) {
		String[] tbr = new String[Math.min(a.length, b.length)];
		for (int i = 0; i < tbr.length; i++)
			if (a[i] == null)
				tbr[i] = b[i];
			else
				tbr[i] = a[i] + b[i];
		return tbr;
	}

	private String[] concat(String[] a, String b) {
		String[] tbr = new String[a.length];
		for (int i = 0; i < tbr.length; i++)
			if (a[i] == null)
				tbr[i] = b;
			else
				tbr[i] = a[i] + b;
		return tbr;
	}

	@Override
	public Game clone() {
		return new Game(toString());
	}

	@Override
	public String toString() {
//		// NAME;SIZE;cNAME,ROT,iPos,jPos;cNAME,ROT,iPos,jPos;...
//		String tbr = name + ";" + n + ";";
//		Circuit c = null;
//		for (int j = 0; j < n; j++)
//			for (int i = 0; i < n; i++)
//				if ((c = tileGrid[i][j]) != null)
//					tbr += c.getData() + ";";
//		return tbr;
		return toXML();
	}
	
	public String toXML(){
		String tbr = "<game name=\""+ name +"\" size=\""+ n +"\">\n";
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				if(tileGrid[i][j] != null){
					Circuit c = tileGrid[i][j];
					tbr += "\t<circuit name=\""+ c.name +"\" pos=\""+ c.coord.i +","+ c.coord.j +"\" rot=\""+ c.rot +"\" />\n";
				}
		tbr += "</game>";
		return tbr;
	}
	
	/*<game name="Lefty Game" size="3">
		<circuit name="INPUT" pos="1,0" rot="1" />
		<circuit name="LEFT" pos="1,1" rot="1" />
		<circuit name="OUTPUT" pos="2,1" rot="0" />
	</game>*/
}

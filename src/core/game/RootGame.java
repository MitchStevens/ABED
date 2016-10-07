package core.game;

import core.circuits.Bus;

public class RootGame {

	public RootGame(int n) {
		// TODO Auto-generated constructor stub
	}

	public boolean add(Game g, Coord c){
		return false;
	}
	
	public boolean remove(Coord c){
		Game g = disconnect(c);
		return g != null;
	}
	
	public boolean rotate(Coord c, int rot){
		Evaluable g = disconnect(c);
		if(g == null)
			return false;
		else{
			g.set_rot(rot);
			connect(g, c);
			return true;
		}
	}
	
	public boolean move(Coord to, Coord from){
		Game g = disconnect(from);
		if(g == null)
			return false;
		else{
			connect(g, to);
			return true;
		}
	}
	
	public boolean connect(Evaluable g, Coord c){
		try{
			if(game_grid[c.i][c.j] != null)
				return false;
			else{
				game_grid[c.i][c.j] = g;
				
			}
		}catch(IndexOutOfBoundsException e){
			
		}
		return false;
	}
	
	public Game disconnect(Coord c){
		return null;
	}
	
	public Coord next_open() {
		// returns the next open co-ords in the form of a Square. Corners are
		// ignored.
		for (int j = 0; j < n; j++)
			for (int i = 0; i < n; i++) {
				if (game_grid[i][j] == null) {
					// this monstrosity used to make sure we don't output any
					// corner squares as "open".
					if (i == 0 && j == 0 || i == 0 && j == n - 1 || i == n - 1
							&& j == 0 || i == n - 1 && j == n - 1)
						continue;
					return new Coord(i, j);
				}
			}
		return null;
	}
	
	public Game get(Coord c){
		try{
			return game_grid[c.i][c.j];
		}catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	@Override
	public String get_name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set_name(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eval() {
		// TODO Auto-generated method stub

	}

	@Override
	public Bus[] get_buses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meta get_parent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coord get_pos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set_pos(Coord c) {
		// TODO Auto-generated method stub

	}

	@Override
	public int get_rot() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void set_rot(int rot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connect_buses() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect_buses() {
		// TODO Auto-generated method stub

	}

	@Override
	public void equiv() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}

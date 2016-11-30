package core.game;

import java.util.Iterator;

public class Coord{
	public static Coord NULL = new Coord(-1, -1);	
	public Integer i, j;
	
	public Coord(){};
	
	public Coord(Integer i, Integer j){
		this.i = i;
		this.j = j;
	}
	
	public Coord(String str){
		//"i, j"
		String[] data = str.split(",");
		this.i = Integer.parseInt(data[i]);
		this.j = Integer.parseInt(data[j]);
	}
	
	public boolean on_side(int n){
		return i == 0 | i == n-1 | j == 0 | j == n-1;
	}
	
	public boolean on_corner(int n){
		return (i == 0 | i == n-1) & (j == 0 | j == n-1);
	}
	
	public boolean is_within(int n){
		return i < n && j < n;
	}
	
	public Coord at(Direction d){
		switch(d){
		case UP: 	return new Coord(i-1, j); 
		case DOWN: 	return new Coord(i+1, j);		
		case RIGHT: return new Coord(i, j+1);
		case LEFT: 	return new Coord(i, j-1);
		default: 	return null;
		}
	}
	
	public void set(Coord c){
		this.i = c.i;
		this.j = c.j;
	}
	
	public Coord[] adj(){
		Coord[] adj = new Coord[4];
		for(Direction d : Direction.values())
			adj[d.value] = this.at(d);
		return adj;
	}
	
	public int taxi_dist(Coord c){
		return Math.abs(this.i - c.i) + Math.abs(this.j - c.j);
	}
	
	@Override
	public String toString(){
		return "("+i+", "+j+")";
	}
	
	@Override
	public int hashCode(){
		return i*(i+1)/2 + i + j;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Coord)) return false;
		Coord c = (Coord)o;
		return this.i == c.i && this.j == c.j;
	}
	
	/**
	 * 
	 * */
	public static Iterable<Coord> over_side(int n, Direction d){
		return new CoordIteratorSide(n, d);
	}
	
	/**
	 * Returns an iterators that returns all the coordinates on the perimeter of an n by n matrix. It starts with (0, 0) and 
	 * moves clockwise.
	 * */
	public static Iterable<Coord> over_perimeter(int n){
		return new CoordIteratorPerimeter(n);
	}
	
	/**
	 * Returns an iterators that returns all the coordinates of an n by n matrix.
	 * */
	public static Iterable<Coord> over_square(int n){
		return new CoordIteratorSquare(n);
	}
}

//Iterator classes
class CoordIteratorSide implements Iterable<Coord> {
	int i = 0, n;
	Direction d;
	
	public CoordIteratorSide(int n, Direction d){
		this.n = n;
		this.d = d;
	}

	@Override
	public Iterator<Coord> iterator() {
		return new Iterator<Coord>(){
			@Override
			public boolean hasNext() {
				return i < n;
			}

			@Override
			public Coord next() {
				Coord c = null;
				switch(d){
				case UP:	c = new Coord(0, i);
				case RIGHT:	c = new Coord(i, n-1);
				case DOWN:	c = new Coord(n-1, i);
				case LEFT:	c = new Coord(i, 0);
				}
				i++;
				return c;
			}
		};
	}
}

class CoordIteratorPerimeter implements Iterable<Coord> {
	int i = 0, n;
	
	public CoordIteratorPerimeter(int n){
		this.n = n;
	}

	@Override
	public Iterator<Coord> iterator() {
		return new Iterator<Coord>(){
			@Override
			public boolean hasNext() {
				return i < 4*(n-1);
			}

			@Override
			public Coord next() {
				Coord c = null;
				switch(i/(n-1)){
				case 0: c = new Coord(0, i);		 break;
				case 1: c = new Coord(i%(n-1), n-1); break;
				case 2: c = new Coord(n-1, i%(n-1)); break;
				case 3: c = new Coord(i%(n-1), 0);	 break;
				}
				i++;
				return c;
			}
		};
	}
}

class CoordIteratorSquare implements Iterable<Coord> {
	int i = 0, n;
	
	public CoordIteratorSquare(int n){ this.n = n; }

	@Override
	public Iterator<Coord> iterator() {
		return new Iterator<Coord>(){
			@Override
			public boolean hasNext() { return i < n*n; }

			@Override
			public Coord next() {
				Coord c = new Coord(i/n, i%n);
				i++;
				return c;
			}
		};
	}
	
}













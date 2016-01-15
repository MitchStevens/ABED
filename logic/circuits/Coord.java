package circuits;

import graphics.Square;

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
	
	public void set(int i, int j){
		this.i = i;
		this.j = j;
	}
	
	public int taxicabDistance(Coord c){
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
}

package logic;

import circuits.Coord;

public enum Direction {
	UP		(0),
	RIGHT	(1),
	DOWN	(2),
	LEFT	(3);
	
	public int value;
	
	Direction(int value){
		this.value = value;
	}
	
	public static Direction getDir(Coord c1, Coord c2){
		//gets direction that c2 is in relation to c1
		if(		c1.i == c2.i 	&& c1.j == c2.j -1)
			return Direction.UP;
		else if(c1.i == c2.i +1 && c1.j == c2.j)
			return Direction.RIGHT;
		else if(c1.i == c2.i	&& c1.j == c2.j +1)
			return Direction.DOWN;
		else if(c1.i == c2.i -1 && c1.j == c2.j)
			return Direction.LEFT;
		else
			return null;
	}
}

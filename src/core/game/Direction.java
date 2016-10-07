package core.game;

import static core.Utilities.mod4;

public enum Direction {
	UP		(0),
	RIGHT	(1),
	DOWN	(2),
	LEFT	(3);
	
	public int value;
	
	Direction(int value){
		this.value = value;
	}
	
	public static Direction create(int dir){
		switch(dir){
		case 0: return UP;
		case 1: return RIGHT;
		case 2: return DOWN;
		case 3: return LEFT;
		default: return null;
		}
	}
	
	public Direction rot(int rot){
		int dir = mod4(value + rot);
		return Direction.create(dir);
	}
}

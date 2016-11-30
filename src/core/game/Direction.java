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
		return Direction.values()[mod4(dir)];
	}
	
	public Direction add_rot(int rot){
		return Direction.values()[mod4(value + rot)];
	}
}

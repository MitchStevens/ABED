package tutorials;

import circuits.Circuit;

public enum Action {
	ADD, REMOVE, MOVE, ROTATE, NEW; 
	Circuit c;
	
	public static Action add(Circuit c){
		Action t = Action.ADD;
		t.c = c;
		return t;
	}
	
	public static Action remove(Circuit c){
		Action t = Action.REMOVE;
		t.c = c;
		return t;
	}
	
	public static Action move(Circuit c){
		Action t = Action.MOVE;
		t.c = c;
		return t;
	}
	
	public static Action rotate(Circuit c){
		Action t = Action.ROTATE;
		t.c = c;
		return t;
	}
	
	@Override
	public String toString(){
		return "A "+c.name+" was just "+this.name()+"D at position "+c.coord.toString();
	}
}
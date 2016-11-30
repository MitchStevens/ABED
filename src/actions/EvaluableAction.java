package actions;

import core.game.Coord;

public class EvaluableAction implements Action {
	private String type;
	private String name;
	private Coord c1, c2;
	
	private EvaluableAction(String type, String name, Coord c1, Coord c2){
		super();
		this.type = type;
		this.name = name;
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public static EvaluableAction Add(String name, Coord pos){
		return new EvaluableAction("add", name, pos, null);
	}

	public static EvaluableAction Remove(String name, Coord pos){
		return new EvaluableAction("remove", name, pos, null);
	}
	
	public static EvaluableAction Move(String name, Coord pos1, Coord pos2){
		return new EvaluableAction("move", name, pos1, pos2);
	}
	
	public static EvaluableAction Rotate(String name, Coord pos){
		return new EvaluableAction("rotate", name, pos, null);
	}
	
	@Override
	public String discription() {
		switch(type){
		case "add": 	return "A(n) "+ name +" was added at "+ c1 +".";
		case "remove": 	return "A(n) "+ name +" was removed from"+ c1 +"." ;
		case "move":	return "A(n) "+ name +" was moved from "+ c1 +" to "+ c2 +".";
		case "rotate":	return "A(n) "+ name +" was rotated.";
		default:		return "";
		}
	}

}

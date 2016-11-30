package core.eval;

public class EdgeInfo {
	public static enum EdgeType{NONE, INPUT, OUTPUT };
	private EdgeType type;
	private int		 capacity;
	
	public EdgeInfo(EdgeType type, int capacity){
		this.type = type;
		this.capacity = capacity;
	}
	
	public EdgeType get_type(){
		return type;
	}
	
	public int get_capacity(){
		return capacity;
	}
	
	public boolean could_connect_to(EdgeInfo info){
		if(this.capacity == info.get_capacity())
			if(this.type == EdgeType.INPUT && info.get_type() == EdgeType.OUTPUT)
				return true;
			else if(this.type == EdgeType.OUTPUT && info.get_type() == EdgeType.INPUT)
				return true;
		return false;
	}
	
	@Override
	public String toString(){
		switch(this.type){
		case INPUT:  return "Inputing "+ capacity;
		case OUTPUT: return "Outputing "+ capacity;
		case NONE:	 return "Doing nothing";
		default:	 return "?";
		}
	}
}

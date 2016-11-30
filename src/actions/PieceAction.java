package actions;

public class PieceAction implements Action{
	public static enum Type{ADD, REMOVE, MOVE, ROTATE, TOGGLE}
	
	private Type type;
	private Object[] objs;
	
	public PieceAction(Type type, Object... objs){
		this.type = type;
		this.objs = objs;
		ActionRecorder.add_action(this);
	}
	
	@Override
	public String discription(){
		switch(type){
		case ADD: 	 return "A(n) "+ objs[0] +" was added at "+ objs[1];
		case REMOVE: return "A(n) "+ objs[0] +" was removed at "+ objs[1];
		case MOVE: 	 return "A(n) "+ objs[0] +" was moved from "+ objs[1] +" to "+ objs[2];
		case ROTATE: return "A(n) "+ objs[0] +" was rotated at "+ objs[1] +" to direction "+ objs[2];
		case TOGGLE: return "A(n) "+ objs[0] +" was toggled at "+ objs[1];
		default:	 return "Unknown action.";
		}
	}
	
}

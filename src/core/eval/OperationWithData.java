package core.eval;

import static core.Utilities.mod4;

import java.util.List;

import core.game.Coord;
import core.game.Direction;

public class OperationWithData implements Operation {
	private Operation op;
	private String	  id;
	private int		  rot;
	private Coord	  pos;
	
	
	public OperationWithData(Operation op, String id, int rot, Coord pos){
		this.op = op;
		this.id = id;
		this.rot = rot;
		this.pos = pos;
	}
	
	public Operation get_op(){
		return op;
	}
	
	public String get_id(){
		return id;
	}
	
	public int get_rot(){
		return rot;
	}
	
	public void add_rot(int add){
		this.rot = mod4(rot + add);
	}
	
	public Coord get_pos(){
		return pos;
	}
	
	public void set_pos(Coord c){
		this.pos = c;
	}
	
	/**
	 * Returns edge information taking into account the rotation of the operation.
	 * */
	public Direction get_abs_rot(Direction d){
		return d.add_rot(rot);
	}
	
	public EdgeInfo abs_dir_edge_info(Direction d){
		return edge_info(get_abs_rot(d));
	}
	
	@Override public String get_name() { return op.get_name(); }
	@Override public int[] inputs() { return op.inputs(); }
	@Override public int[] outputs() { return op.outputs();	}
	@Override public void eval(List<Boolean> list) { op.eval(list); }
	@Override public List<Boolean> last_inputs() { return op.last_inputs(); }
	@Override public List<Boolean> last_outputs(Direction d) { return op.last_outputs(d); }
	@Override public Operation clone() { return op.clone(); }
	@Override public void add_observer(Node node) { op.add_observer(node); }
}

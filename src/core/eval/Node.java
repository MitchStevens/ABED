package core.eval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import core.game.Direction;

public class Node extends Observable implements Observer {
	public String id;
	public Operation op;
	public Edge[] edges = new Edge[4];
	
	public Node(String id, Operation op){
		this.id = id;
		this.op = op;
		op.add_observer(this);
	}
	
	public void add_edge(Edge e, Direction dir){
		edges[dir.value] = e;
	}
	
	public void remove_edge(Direction dir){
		edges[dir.value] = null;
	}
	
	public void remove_edge(Edge edge){
		Direction d = direction_of(edge);
		remove_edge(d);
	}

	public Direction direction_of(Edge edge){
		for(Direction d : Direction.values())
			if(edges[d.value] == edge)
				return d;
		return null;
	}
	
	public void eval(){
		List<Boolean> ins = new ArrayList<>();
		Edge e;
		
		for(Direction d : Direction.values()){
			if(op.inputs()[d.value] > 0){
				e = edges[d.value];
				if(e != null && e.outputs_to(this))
					ins.addAll(e.get_values());
				else
					ins.addAll(Collections.nCopies(op.inputs()[d.value], false));
			}
		}
		this.op.eval(ins);
		set_outputs();
	}
	
	
	@Override
	public void update(Observable arg0, Object arg1) {
		set_outputs();
	}
	
	public void set_outputs(){
		Edge e;
		for(Direction d : Direction.values()){
			e = edges[d.value];
			if(e != null && e.inputs_from(this)){
				e.set_values(this.op.last_outputs(d));
				this.setChanged();
				this.notifyObservers(e.output);
			}
		}
	}
	
	@Override
	public String toString(){
		String str = "";
		for(Direction d : Direction.values())
			str += d.name() +":\t"+ (edges[d.value] != null ? edges[d.value].disc(this) : "None") +"\n";
		return str;
	}
}



















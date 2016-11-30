package core.eval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import core.game.Direction;
import core.game.Game;
import core.operations.Input;
import core.operations.Output;


public class Function extends Observable implements Operation, Observer {
	//time taken (milliseconds) to eval a node.
	private double clock_speed = 50;
	public long eval_time;
	private String name;
	
	private Map<String, Node> nodes = new HashMap<>();
	private Map<String, Edge> edges = new HashMap<>();
	
	List<Boolean> last_inputs = new ArrayList<>();
	Node[] sides = new Node[4];
	
	public EvalQueue eval_queue;
	
	public Function(String name){
		this.name = name;
		calc_eval_time();
		eval_queue = new EvalQueue(this);
	}
	
	public Function(String name, double clock_speed){
		this.name = name;
		set_clock_speed(clock_speed);
		eval_queue = new EvalQueue(this);
	}
	
	public Map<String, Node> get_nodes(){ return new HashMap<String, Node>(nodes); }
	public Map<String, Edge> get_edges(){ return new HashMap<String, Edge>(edges); }
	
	public Node get_node(String id){ return nodes.get(id); }
	public Edge get_edge(String id){ return edges.get(id); }
	
	public int num_nodes(){ return nodes.size(); }
	public int num_edges(){ return edges.size(); }
	
	public double get_clock_speed(){
		return clock_speed;
	}
	
	public void set_clock_speed(double clock_speed){
		this.clock_speed = clock_speed;
		calc_eval_time();
	}
	
	private void calc_eval_time(){
		eval_time = (long) (1000.0/clock_speed);
	}
	
	public void add(Operation op, String id){
		Node n = new Node(id, op);
		n.addObserver(this);
		n.eval();
		nodes.put(id, n);
	}
	
	public void remove(String id){
		Node n = nodes.get(id);
		Edge e;
		
		for(Direction d : Direction.values())
			if((e = n.edges[d.value]) != null){
				if(e.outputs_to(n)){
					e.input.remove_edge(e);
				} else {
					e.output.remove_edge(e);
					e.output.eval();
				}
				edges.remove(e.id);
			}
					
		nodes.remove(id);
	}
	
	public void add_input(Input input, Direction d, String id){
		Node n = new Node(id, input);
		nodes.put(id, n);
		sides[d.value] = n;
	}
	
	public void add_output(Output output, Direction d, String id){
		Node n = new Node(id, output);
		nodes.put(id, n);
		sides[d.value] = n;
	}
	
	private boolean is_acyclic(){
		//learn more graph theory
		return false;
	}
	
	/**
	 * Port the output of operation op1 at direction d1, to the input of operation op2 at direction d2. This can fail if
	 * <ul>
	 * 		<li>Operation op1 is the same as Operation op2</li>
	 * 		<li>Operation op1 does not supply output at direction d1</li>
	 * 		<li>Operation op2 does not accept input at direction d2</li>
	 * 		<li>The supplied output is not of the same size as the input</li>
	 * </ul>
	 * <marquee>whee</marquee>
	 * @param node1
	 * @param d1
	 * @param node2
	 * @param d2
	 * @return bool
	 * */
	public boolean port(String id, String node1, Direction d1, String node2, Direction d2){
		try{
			Node n1 = nodes.get(node1);
			Node n2 = nodes.get(node2);			
			
			Edge edge = new Edge(id, n1, n2);
			n1.add_edge(edge, d1);
			n2.add_edge(edge, d2);
			this.edges.put(id, edge);
			
			edge.set_values(n1.op.last_outputs(d1));
			n2.eval();
			return true;
		}catch(NullPointerException e){
			return false;
		}
	}
	
	public boolean port(String node1, Direction d1, String node2, Direction d2){
		return port("a", node1, d1, node2, d2);
	}
	
	public boolean unport(String id){
		if(edges.containsKey(id)){
			Edge e = edges.remove(id);
			e.input.remove_edge(e);
			e.output.remove_edge(e);
			e.output.eval();
			return true;
		}
		return false;
	}
	
	@Override
	public int[] inputs() {
		int[] inputs = new int[4];
		
		for(Direction d : Direction.values())
			if(sides[d.value] != null && sides[d.value].op instanceof Input)
				inputs[d.value] += ((Input)sides[d.value].op).size();
		
		return inputs;
	}

	@Override
	public int[] outputs() {
		int[] outputs = new int[4];
		
		for(Direction d : Direction.values())
			if(sides[d.value] != null && sides[d.value].op instanceof Output)
				outputs[d.value] += ((Output)sides[d.value].op).size();
		
		return outputs;
	}

	@Override
	public void eval(List<Boolean> inputs) {
		int n = inputs.size();
		boolean[] toggle = new boolean[n];
		
		try{
			for(int i = 0; i < n; i++)
				toggle[i] = last_inputs.get(i) ^ inputs.get(i);
		} catch(java.lang.IndexOutOfBoundsException e){
			
		}
		
		int acc = 0;
		for(Direction d : Direction.values() )
			if(sides[d.value] != null && sides[d.value].op instanceof Input)
				if(toggle[acc]){
					((Input)sides[d.value].op).toggle();
					acc++;
				}
		
		this.last_inputs = inputs;
		this.setChanged();
		this.notifyObservers(last_inputs);
	}

	@Override
	public List<Boolean> last_inputs() {
		return last_inputs;
	}

	@Override
	public List<Boolean> last_outputs(Direction d) {
		if(sides[d.value] != null && sides[d.value].op instanceof Output && sides[d.value].edges[3] != null)
			return sides[d.value].edges[3].last_values;
		else
			return new ArrayList<>();
	}

	public List<Boolean> last_outputs(){
		List<Boolean> outputs = new ArrayList<>();
		for(Direction d : Direction.values())
			outputs.addAll(this.last_outputs(d));
		return outputs;
	}
	
	@Override
	public String toString(){
		String str = "Function Nodes:";
		
		for(String id : nodes.keySet())
			str += "\n\tNode "+ id +"\n"+nodes.get(id).toString();
		
		return str;
	}

	@Override
	public String get_name() {
		return name;
	}
	
	@Override
	public int hashCode(){
		int hash = 17;
		hash *= nodes.hashCode();
		hash *= edges.hashCode();
		hash *= name.hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		
		if(!(o instanceof Function))
			return false;
		Function f = (Function)o;
		if(!nodes.equals(f.nodes))  return false;
		if(!edges.equals(f.edges))  return false;
		if(!name.equals(f.name))	return false;
		for(Direction d : Direction.values())
			if(sides[d.value] != f.sides[d.value])
				return false;
		if(clock_speed != f.clock_speed) return false;
		
		return true;
	}
	
	@Override
	public Function clone(){
		//TODO
		return null;
	}

	@Override
	public void add_observer(Node node) {
		this.addObserver(node);
	}

	@Override
	public void update(Observable arg0, Object node) {
		this.eval_queue.add((Node)node);
	}
}






































package core.eval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import core.game.Direction;


public class Function implements Operation{
	HashMap<String, Node> nodes = new HashMap<>();
	List<Edge> edges = new ArrayList<>();
	
	List<Boolean> prev_inputs = new ArrayList<>();
	List<List<Node>> sides = new ArrayList<List<Node>>(4);
	
	public Function(){}
	
	public void add(Operation o, String id){
		nodes.put(id, new Node(o));
	}
	
	public void add_input(Input input, Direction d, String id){
		Node n = new Node(input);
		nodes.put(id, n);
		sides.get(d.value).add(n);
	}
	
	/**
	 * Port the output of operation op1 at direction d1, to the input of operation op2 at direction d2. This can fail if
	 * <ul>
	 * 		<li>Operation op1 is the same as Operation op2</li>
	 * 		<li>Operation op1 does not supply output at direction d1</li>
	 * 		<li>Operation op2 does not accept input at direction d2</li>
	 * 		<li>The supplied output is not of the same size as the input</li>
	 * </ul>
	 * 
	 * @param id1
	 * @param d1
	 * @param id2
	 * @param d2
	 * @return bool
	 * */
	public boolean port(String id1, Direction d1, String id2, Direction d2){
		try{
			Node n1 = nodes.get(id1);
			Node n2 = nodes.get(id2);
			if(n1 == n2)
				return false;
			
			if(	n1.op.outputs()[d1.value] == 0 ||
				n2.op.inputs()[d2.value]  == 0||
				n1.op.outputs()[d1.value] != n2.op.inputs()[d2.value])
				return false;
			
			Edge edge = new Edge(n1, n2);
			n1.add_edge(edge, d1);
			n2.add_edge(edge, d2);
			
			return true;
		}catch(NullPointerException e){
			return false;
		}
	}
	

	@Override
	public Stack<Boolean> eval(Stack<Boolean> stack, List<Boolean> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] inputs() {
		int[] inputs = new int[4];
		
		for(int i = 0; i < 4; i++)
			for(Node n : sides.get(i))
				if(n.op instanceof Input)
					inputs += 0
	}

	@Override
	public int[] outputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eval() {
		// TODO Auto-generated method stub
		
	}
}

class Node{
	Operation op;
	Edge[] edges = new Edge[4];
	
	public Node(Operation op){
		this.op = op;
	}
	
	public void add_edge(Edge e, Direction dir){
		edges[dir.value] = e;
	}
	
	public void remove_edge(Direction dir){
		edges[dir.value] = null;
	}
	
	public List<Node> inputs(){
		List<Node> list = new ArrayList<>();
		for(Edge e : edges)
			if(e != null && this == e.output)
				list.add(e.input);
		return list;
	}
	
	public List<Node> outputs(){
		List<Node> list = new ArrayList<>();
		for(Edge e : edges)
			if(e != null && this == e.input)
				list.add(e.output);
		return list;
	}
	
}

/**
 * An Edge is a connection from {@code input} to {@code output}. The direction parameters {@code input_dir} and 
 * {@code output_dir} represent the direction of the node the signals come/go from/to.
 * */
class Edge{
	Node input;
	Node output;
	
	public Edge(Node input, Node output){
		this.input = input;
		this.output = output;
	}
}
















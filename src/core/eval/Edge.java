package core.eval;

import java.util.ArrayList;
import java.util.List;

/**
 * An Edge is a connection from {@code input} to {@code output}. The direction parameters {@code input_dir} and 
 * {@code output_dir} represent the direction of the node the signals come/go from/to.
 * */
public class Edge{
	public String id;
	Node input;
	Node output;
	List<Boolean> last_values = new ArrayList<>();
	
	public Edge(String id, Node input, Node output){
		this.id = id;
		this.input = input;
		this.output = output;
	}
	
	public List<Boolean> get_values(){
		return last_values;
	}
	
	public void set_values(List<Boolean> values){
		this.last_values = values;
	}
	
	public boolean inputs_from(Node n){
		return n == this.input;
	}
	
	public boolean outputs_to(Node n){
		return n == this.output;
	}
	
	public String disc(Node n){
		if(input == n)
			return "Outputing to "+ output.id;
		else if(output == n)
			return "Inputing from "+ input.id;
		else
			return "No relation";
	}
}
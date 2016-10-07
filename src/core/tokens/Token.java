package core.tokens;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

public interface Token {
	
	public default Stack<Boolean> eval(List<Boolean> list){
		return eval(new Stack<Boolean>(), list);
	}
	public Stack<Boolean> eval(Stack<Boolean> stack, List<Boolean> list);
	public int sum_inputs();
	public int sum_outputs();
	
	public String toString();
}
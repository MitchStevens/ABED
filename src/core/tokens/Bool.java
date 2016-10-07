package core.tokens;

import java.util.List;
import java.util.Stack;

public class Bool implements Token {
	public static Bool False = new Bool(false);
	public static Bool True  = new Bool(true);
	
	private boolean bool;
	
	private Bool(boolean b){
		this.bool = b;
	}
	
	@Override
	public Stack<Boolean> eval(Stack<Boolean> stack, List<Boolean> list) {
		stack.push(bool);
		return stack;
	}

	@Override
	public int sum_inputs(){ return 0; }
	
	@Override
	public int sum_outputs() { return 1; }

	@Override
	public String toString() { return (bool ? "T" :  "F"); }

}

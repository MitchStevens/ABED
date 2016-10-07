package core.tokens;

import java.util.List;
import java.util.Stack;

public class BasicOp implements Token {
	public static BasicOp Not = new BasicOp("not", 	1, 	1);
	public static BasicOp Or  = new BasicOp("or", 	2,	1);
	public static BasicOp And = new BasicOp("and",	2,	1);
	int inputs, outputs;
	
	private String name;
	
	private BasicOp(String name, int inputs, int outputs){
		this.name = name;
		this.inputs = inputs;
		this.outputs = outputs;
	}

	@Override
	public Stack<Boolean> eval(Stack<Boolean> stack, List<Boolean> list) {		
		boolean a, b;
		switch(name){
		case "not":
			a = stack.pop();
			stack.push(!a);
			break;
		case "or" :
			a = stack.pop();
			b = stack.pop();
			stack.push(a || b);
			break;
		case "and":
			a = stack.pop();
			b = stack.pop();
			stack.push(a && b);
			break;
		}
		return stack;
	}

	@Override
	public int sum_inputs() { return inputs; }

	@Override
	public int sum_outputs() { return outputs;}

	@Override
	public String toString() {
		String str = "?";
		switch(name){
		case "not": str = "~"; break;
		case "or":  str = "+"; break;
		case "and": str = "x"; break;
		}
		return str;
	}
	
}

package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Operation extends Token{
	String name;
	Integer args;
	
  public static Operation and = new Operation("And", 2);
  public static Operation or  = new Operation("Or", 2);
  public static Operation not = new Operation("Not", 1);
  
  public Operation(String name){
	  this.name = name;
	  this.args = null;
  }
  
  public Operation(String name, int args){
    this.name = name;
    this.args = args;
  }
	
	public void apply(Stack<Token> stack, List<Boolean> list){
	  
		List<Flag> l;
		switch(name){
		case "And":
			l = popList(stack, 2);
			stack.push(new Flag( l.get(0).get(list) && l.get(1).get(list))); return;
		case "Or":
			l = popList(stack, 2);
			stack.push(new Flag( l.get(0).get(list) || l.get(1).get(list))); return;
		case "Not":
			l = popList(stack, 1);
			stack.push(new Flag( !l.get(0).get(list) )); return;
		default:
			Circuit c = Circuit.loadedCircuits.get(name).clone();
			if(c != null) {
				l = popList(stack, c.inputList().size());
				List<Boolean> b = new ArrayList<>();
				l.forEach(f -> b.add(f.get(list)));
				stack.push( new Flag(c.evals.get(0).eval(b)) );
			} else {
				throw new Error("Operator \'"+name+"\' not found!");
			}
		}
	}
  
  	private static List<Flag> popList(Stack<Token> stack, int num){
  		List<Flag> tbr = new ArrayList<>();
		if(stack.size() < num)
			throw new Error("");
		else
			for(int i = 0; i < num; i++)
				tbr.add((Flag)stack.pop());
		return tbr;
  	}
  
	@Override
	public String toString() {
		return name;
	}
  
  
}

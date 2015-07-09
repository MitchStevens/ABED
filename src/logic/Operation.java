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


//  public void apply(Stack<Token> stack, List<Boolean> list){
//		List<Boolean> ins = new ArrayList<>();
//		Circuit c = null;
//		if(args == null){
//			c = Circuit.loadedCircuits.get(name).clone();
//			args = c.inputList().size();
//		}
//		
//		if(stack.size() < args){ throw new Error("");
//		} else {
//			for(int i = 0; i < args; i++)
//				ins.add(((Flag)stack.pop()).get(list));
//		}
//	  
//		switch(name){
//		case "And": stack.push( new Flag( ins.get(0) && ins.get(1)) ); return;
//		case "Or":  stack.push( new Flag( ins.get(0) || ins.get(1)) ); return;
//		case "Not": stack.push( new Flag( !ins.get(0)) );			   return;
//		default:
//			if(c != null)
//				stack.push( new Flag(c.evals.get(0).eval(list)) );
//			else
//				throw new Error("Operator \""+name+"\" not found!");
//		}
//	}
	
  public void apply(Stack<Token> stack, List<Boolean> list){
	  	Circuit c = null;
		if(args == null){
			c = Circuit.loadedCircuits.get(name).clone();
			args = c.inputList().size();
		}
	  
	  	List<Boolean> ins = new ArrayList<>();
		if(stack.size() < args){ throw new Error("");
		} else {
			for(int i = 0; i < args; i++)
				ins.add(((Flag)stack.pop()).get(list));
		}
	  
		switch(name){
		case "And": stack.push(new Flag( ins.get(0) && ins.get(1))); return;
		case "Or": stack.push(new Flag( ins.get(0) || ins.get(1))); return;
		case "Not": stack.push( new Flag(!ins.get(0)) ); return;
		default:
			if(c != null)
				stack.push( new Flag(c.evals.get(0).eval(list)) );
			else
				throw new Error("Operator \""+name+"\" not found!");
		}
	}
  
	@Override
	public String toString() {
		return name;
	}
  
  
}

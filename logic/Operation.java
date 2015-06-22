package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Operation extends Token{
	String op;
	String name;
	int precedence;
	int args;
	
  public static Operation and = new Operation("and", "&", 2, 2);
  public static Operation or  = new Operation("or",  "|", 2, 2);
  public static Operation not = new Operation("not", "~", 1, 1);
  
  public Operation(String name, String op, int p, int args){
    this.name = name;
    this.op = op;
    this.precedence = p;
    this.args = args;
  }
  

  public Flag apply(List<Boolean> bools){
	  List<Flag> tbr = new ArrayList<>();
		switch(name){
		case "and":
			return new Flag( bools.get(0) && bools.get(1));
			
		case "or":
				return new Flag( bools.get(0) || bools.get(1));
		
		case "not":
			return new Flag( !bools.get(0));
			
		default:
		}
		throw new Error("Operator "+op+" not found!");
	}
	
	@Override
	public String toString() {
		return op;
	}
  
  
}

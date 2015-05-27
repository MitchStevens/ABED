package logic;

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
  

  public Input apply(Stack<Token> bools){
		switch(name){
		case "and": return new Input( ((Input)bools.pop()).b && ((Input)bools.pop()).b );
		case "or":  return new Input( ((Input)bools.pop()).b || ((Input)bools.pop()).b );
		case "not": return new Input( !((Input)bools.pop()).b );
		}
		throw new Error("Operator "+op+" not found!");
	}
	
	@Override
	public String toString() {
		return op;
	}
  
  
}

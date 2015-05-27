package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


public class Eval {
	List<Input> inputs;
	List<Token> tokens;
	
	public Eval(List<Input> inputs, String data){
		this.inputs = inputs;
		tokens = tokenize(inputs, data);
	}
	
	public static List<Token> tokenize(List<Input> inputs, String str) throws Error{
			ArrayList<Token> tokens = new ArrayList<>();
			String temp = "";
			
			int i = 0;
			do{
				String curr = str.substring(i, i+1);
				if(curr.matches("\\w")){
					i++;
					continue;
				}
				
				if(curr.matches("\\d")){
					temp += str.charAt(i++);
					continue;
				}else{
					if(!temp.isEmpty())
						tokens.add(inputs.get(Integer.parseInt(temp)));
					temp = "";
				}
				
				switch(curr){
				case "&": tokens.add(Operation.and); i++; break;
				case "|": tokens.add(Operation.or);  i++; break;
				case "~": tokens.add(Operation.not); i++; break;
				default: throw new Error("Couldn't find symbol "+curr);
				}
				
			}while(i < str.length());
			
			if(!temp.isEmpty())
				tokens.add(inputs.get(Integer.parseInt(temp)));
			
			return tokens;
	 }
	 
	 public Boolean eval(){
			//translated from psuedocode http://en.wikipedia.org/wiki/Reverse_Polish_notation		
			Stack<Token> stack = new Stack<>();
			
			for(Token t : tokens){
				if(t instanceof Input){
					stack.push(t);
				}else if(t instanceof Operation){
					Operation op = (Operation)t;
					List<Input> ins = new ArrayList<>(); 
					
					if(stack.size() < op.args){ throw new Error("");
					} else {
						for(int i = 0; i < op.args; i++)
							ins.add((Input)stack.pop());
					}
					
					Input i = op.apply(stack);
					stack.push(i);
				}
			}
			if(stack.isEmpty()) throw  new Error("");
			return ((Input)stack.pop()).b;
			
		}
}

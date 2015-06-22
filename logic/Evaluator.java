package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Evaluator {
	List<Token> tokens;
	
	public Evaluator(String data){
		tokens = tokenize(data);
	}
	
	public String toString(List<String> inputs){
		String tbr = "";
		for(Token t : tokens)
			if(t instanceof Flag) {
				tbr += inputs.get(((Flag)t).i);
			} else
				tbr += t.toString();
		return tbr;
	}
	
	public static List<Token> tokenize(String str) throws Error{
			ArrayList<Token> tokens = new ArrayList<>();
			String temp = "";
			
			int i = 0;
			do{
				String curr = str.substring(i, i+1);
				
				if(curr.matches("\\d")){
					temp += str.charAt(i++);
					continue;
				}
				
				if(curr.matches("\\s")){
					i++;
					if(!temp.isEmpty()){
						tokens.add(new Flag(temp));
						temp = "";
					}
					continue;
				}
				
				switch(curr){
				case "&": tokens.add(Operation.and); i++; break;
				case "|": tokens.add(Operation.or);  i++; break;
				case "~": tokens.add(Operation.not); i++; break;
				default: throw new Error("Couldn't find symbol \'"+curr+"\'.");
				}
				
			}while(i < str.length());
			
			if(!temp.isEmpty())
				tokens.add(new Flag(temp));
			
			return tokens;
	 }
	 
	 public Boolean eval(List<Boolean> list){
		 	//translated from psuedocode http://en.wikipedia.org/wiki/Reverse_Polish_notation		
			Stack<Token> stack = new Stack<>();
			
			for(Token t : tokens){
				if(t instanceof Flag){
					stack.push(t);
				}else if(t instanceof Operation){
					Operation op = (Operation)t;
					List<Boolean> ins = new ArrayList<>(); 
					
					if(stack.size() < op.args){ throw new Error("");
					} else {
						for(int i = 0; i < op.args; i++)
							ins.add(((Flag)stack.pop()).get(list));
					}
					
					Flag f = op.apply(ins);
					stack.push(f);
				}
			}
			if(stack.isEmpty()) throw  new Error("");
			return ((Flag)stack.pop()).get(list);
			
	}
}

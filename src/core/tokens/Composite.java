package core.tokens;

import static core.Utilities.popn;
import static core.Utilities.toInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import data.Reader;

public class Composite implements Token {
	List<Token> tokens;
	Integer sum_inputs  = null;
	Integer sum_outputs = null;
	
	public Composite(){}
	
	public Composite(String logic){
		set_logic(logic);
	}
	
	public Composite(List<Token> tokens){
		this.tokens = tokens;
	}
	
	public void set_logic(String logic){
		try {
			this.tokens = tokenize(logic);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Stack<Boolean> eval(Stack<Boolean> stack, List<Boolean> list) {
		for(Token t : tokens)
			if(t instanceof Composite){
				List<Boolean> new_bools = popn(stack, t.sum_inputs());
				stack = t.eval(stack, new_bools);
			}else
				stack = t.eval(stack, list);
		return stack;
	}

	public static List<Token> tokenize(String str) throws Exception {
		List<Token> tokens = new ArrayList<>();
		char[] chars = str.toCharArray();
		boolean collect_chars = false;
		String s = "";
		int temp;
		
		for (char c : chars) {
			//OPERATIONS
			if(collect_chars){
				if(c == ')'){
					tokens.add(Reader.get_composite(s));
					collect_chars = false;
					s = "";
				}else
					s += c;
				continue;
			}
				
			//
			switch (c) {
			case '~': tokens.add(BasicOp.Not); continue;
			case '+': tokens.add(BasicOp.Or); continue;
			case 'x': tokens.add(BasicOp.And); continue;
			case 'T': tokens.add(Bool.True); continue;
			case 'F': tokens.add(Bool.False); continue;
			case '(':
				collect_chars = true;
				continue;
			case ')':
				throw new Exception("Mismatching brackets in string \""+ str +"\".");
			default: break;
			}
			
			//FLAGS
			temp = toInt(c);
			if(temp != '_')
				tokens.add(new Flag(temp));
			else
				throw new Exception("The char \'"+ c +"\' in string \""+ str +"\" could not be parsed.");
		}

		return tokens;
	}
	
	@Override
	public int sum_inputs() {
		if(sum_inputs == null){
			int max = 0;
			for(Token t : tokens)
				if(t instanceof Flag)
					max = Math.max(max, ((Flag)t).i+1);
			this.sum_inputs = max;
		}
		return this.sum_inputs;
	}

	@Override
	public int sum_outputs() {
		if(sum_outputs == null){
			int acc = 0;
			for(Token t : tokens)
				acc = t.sum_outputs() - t.sum_inputs();
			this.sum_outputs = acc;
		}
		return this.sum_outputs;
	}

}

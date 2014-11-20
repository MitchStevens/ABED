package abedgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Logic {
	String logic;
	boolean[] inputs;
	
	public Logic(boolean[] inputs, String logic){
		this.inputs = inputs;
		this.logic = logic;
	}
	
	public boolean eval(){
		return evalString(logic);
	}
	
	public boolean evalString(String s){
		if(s.matches("\\d")) return inputs[Integer.parseInt(s)];
		List<String> unbracket = bracketSplit(s);
		switch(unbracket.size()){
		case 0:
			return false;
		case 1:
			return evalString(unbracket.get(0));
		case 2:
			if(unbracket.get(0).equals("~"))
				return !evalString(unbracket.get(1));
			else throw new Error("input string "+s+" not acceptable!");
		case 3:
			switch(unbracket.get(1)){
			case "&":
				return evalString(unbracket.get(0)) && evalString(unbracket.get(2));
			case "|":
				return evalString(unbracket.get(0)) || evalString(unbracket.get(2));
			default:
//				Gate g = gateExists(unbracket.get(1));
//				if(g != null)
//					return g.eval(new boolean[] {eval(unbracket.get(0)), eval(unbracket.get(0))});
				throw new Error("cant find operator "+unbracket.get(1));
			}
		default: throw new Error("something broke'd");
		}
	}
	
	public static List<String> bracketSplit(String s){
		List<String> tbr = new ArrayList<String>();
		int b = s.charAt(0) == '('?1:0;
		int c = b; //count
		for(int i = 1; i < s.length(); i++){
			if(s.charAt(i) == '(') {
				if(c == 0) {tbr.add(s.substring(b, i)); b=i+1;}
				c++;
			}
			else if(s.charAt(i) == ')') {
				c--;
				if(c == 0) {tbr.add(s.substring(b, i)); b=i+1;}
			}
		}
		return tbr;
	}
	
	public static void main(String[] args) {
		Logic l = new Logic(new boolean[]{true, false}, "1");
		System.out.println(l.eval());
	}

	
}

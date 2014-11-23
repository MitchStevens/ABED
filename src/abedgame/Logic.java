package abedgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

public class Logic {
	String logic;
	boolean[] inputs;
	
	public Logic(boolean[] inputs, String logic){
		this.inputs = inputs;
		this.logic = logic;
	}
	
	public boolean[] eval(){
		boolean[] tbr = new boolean[logic.split(",").length];
		for(int i = 0; i < tbr.length; i++)
			tbr[i] = evalString(logic.split(",")[i]);
		return tbr;
	}
	
	public boolean evalString(String s){
		if(s.matches("\\d")) return inputs[Integer.parseInt(s)];
		List<String> unbracket = bracketSplit(s);
		System.out.println(unbracket);
		
		switch(unbracket.get(0)){
		case "~": if(unbracket.size() == 2) return !evalString(unbracket.get(1));
		case "|": if(unbracket.size() == 3) return evalString(unbracket.get(1)) || evalString(unbracket.get(2));
		case "&": if(unbracket.size() == 3) return evalString(unbracket.get(1)) && evalString(unbracket.get(2));
		default:
			Gate g = Gate.allGates.get(unbracket.get(0));
			boolean[] b = null;
			if((g = Gate.allGates.get(unbracket.get(0))) != null)
				if(g.inputs.length == unbracket.size() -1){
					b = new boolean[inputs.length];
					for(int i = 1; i < b.length; i++)
						b[i -1] = evalString(unbracket.get(i));
				}
			
			return new Logic(b, g.logic).evalString(g.logic);
		}
	}
	
	
	public static List<String> bracketSplit(String s){
		//this is kind of shitty. You should fix it at some point.
		List<String> tbr = new ArrayList<String>();
		if(s.indexOf("(") > 0){
			tbr.add(s.substring(0, s.indexOf("(")));
			s = s.substring(s.indexOf("("));
		}
		
		int c = s.charAt(0) == '(' ? 1 : 0;
		int b = 0;
		for(int i = 1; i < s.length(); i++){
			if(s.charAt(i) == '('){
				if(c == 0) {
					tbr.add(s.substring(b+1, i));
					b = i;
				}
				c++;
			} else if(s.charAt(i) == ')'){
				c--;
				if(c == 0) {
					tbr.add(s.substring(b+1, i));
					b = i;
				}
			}
		}

		//i hate this part. So much. You can do better.
		ListIterator<String> iter = tbr.listIterator(); 

		while(iter.hasNext())
			if(iter.next().equals(""))
				iter.remove();
		
		return tbr;
	}
	
	public static void main(String[] args) {
		Logic l = new Logic(new boolean[]{true, false}, "1");
		System.out.println(bracketSplit("543(0)"));
	}

	
}

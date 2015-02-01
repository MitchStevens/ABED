package abedgame;

import java.util.ArrayList;
import java.util.List;

import static abedgame.Functions.*;
/*  1 > 2 > 3		func(arg1)(arg2)...
 *  ^       V
 *  8       4
 *  ^       V
 *  7 < 6 < 5 */


public class Logic {
	public static List<Bus> eval(Gate g, List<Boolean> inputs){
		List<Bus> tbr = new ArrayList<Bus>();
		String[] str = g.data.split(",");
		for(int i = 0; i < 4; i++){
			Bus b = new Bus(map(s -> evalString(s, inputs), str[i].split("#")));
			tbr.add(b);
		}
			
		return tbr; 
	}
	
	private static Boolean evalString(String s, List<Boolean> inputs){
		if(s.matches("\\d")) return inputs.get(Integer.parseInt(s));
		if(s.matches("_")) return null;
		List<String> unbracket = bracketSplit(s);
		System.out.println(unbracket);
		//if the operation is one of the basic ops, return whatever it is.
		switch(unbracket.get(0)){
		case "~": if(unbracket.size() == 2) return !evalString(unbracket.get(1), inputs);
		case "|": if(unbracket.size() == 3) return evalString(unbracket.get(1), inputs) || evalString(unbracket.get(2),inputs);
		case "&": if(unbracket.size() == 3) return evalString(unbracket.get(1), inputs) && evalString(unbracket.get(2),inputs);
		//if it is one of the non-basic ops, look for it in the list. If it has the right number of inputs, use it.
		default:
			if(Gate.allGates == null) new Reader().getGates();
			Gate g = Gate.allGates.get(unbracket.get(0));
			List<Boolean> b = new ArrayList<>();
			if(g != null)
				if(g.numInputs.get() == unbracket.size() -1){
					for(int i = 1; i < unbracket.size(); i++)
						b.add(evalString(unbracket.get(i), inputs));
				} else throw new Error("Error: "+g.name+" has "+g.numInputs+" inputs. You have provided "+(unbracket.size() -1)+" arguements.");
			
			throw new Error("Error: Can't find gate "+unbracket.get(0)+".");
			
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
		
		return filter(str -> !str.equals(""), tbr);
	}
}

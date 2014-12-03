package abedgame;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Logic {
	
	public static boolean[][] eval(Boolean[] inputs, Gate gate){
		String[] str = gate.logic.split(",");
		boolean[][] tbr = new boolean[4][];
		
		for(int i = 0; i < 4; i++){
			tbr[i] = new boolean[gate.outputDir[i]];
			String[] logic = str[i].split("#");
			for(int j = 0; j < logic.length; j++){
				if("_".equals(logic[j])) continue;
				tbr[i][j] = evalString(inputs, logic[j]);
			}
		}
		
		return tbr;
	}
	
	public static boolean evalString(Boolean[] inputs, String logic){
		if(logic.matches("^\\d+")) return inputs[Integer.parseInt(logic)];
		List<String> unbracket = bracketSplit(logic);
		
		switch(unbracket.get(0)){
		case "~": if(unbracket.size() == 2) return !evalString(inputs, unbracket.get(1));
		case "|": if(unbracket.size() == 3) return evalString(inputs, unbracket.get(1)) || evalString(inputs, unbracket.get(2));
		case "&": if(unbracket.size() == 3) return evalString(inputs, unbracket.get(1)) && evalString(inputs, unbracket.get(2));
		default:
			Gate g = Gate.allGates.get(unbracket.get(0));
			if(g == null) { System.out.println("couldn't find "); return false;}
			if(g.inputs.size() != unbracket.size() -1) return false;
			
			boolean[] b = new boolean[inputs.length];
			for(int i = 0; i < inputs.length; i++)
				b[i] = evalString(inputs, unbracket.get(i+1));
				
			return evalString(inputs, g.logic);
		}
	}
	
	public static void printArray(boolean[][] array){
		for(int i = 0; i < 4; i++){
			System.out.print("[");
			for(boolean b : array[i])
				System.out.print(b+",");
			System.out.print("] ");
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
		//printArray(Logic.eval(new Boolean[]{true}, new Gate("Not;0,0,1,0;1,0,0,0;~(0),_,_,_")));
		//System.out.println("\n");
		//printArray(Logic.eval(new Boolean[]{true, true}, new Gate("And;0,0,1,1;1,0,0,0;&(0)(1),_,_,_")));
	}

	
}

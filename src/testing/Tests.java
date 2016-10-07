package testing;

import static core.Utilities.hex_sum;

import java.util.List;
import java.util.Random;

import core.eval.PureOperation;
import core.game.Gate;

public abstract class Tests {
	public static int NUM_TESTS = 100;
	public static Random r = new Random(System.currentTimeMillis());
	
	public static String rand_str(int n){
		char[] chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_".toCharArray();
		int len = chars.length;
		String s = "";
		
		for(int i = 0; i < n; i++)
			s += chars[r.nextInt(len)];
		return s;
	}
	
	public static char rand_char(String str){
		return str.toCharArray()[r.nextInt(str.length())];
	}
	
	public static String rand_hex(int n){
		char[] chars = "0123456789abcdef".toCharArray();
		String str = "";
		for(int i = 0; i < n; i++)
			str += chars[r.nextInt(16)];
		return str;
		
	}
	
	public static <T> T rand_elem(List<T> list){
		int n = r.nextInt(list.size());
		return list.get(n);
	}
	
	//random objects
	public static PureOperation rand_op(){
		return rand_op(r.nextInt(17), r.nextInt(17));
	}
	
	public static PureOperation rand_op(int inputs, int outputs){
		String name = rand_str(6);
		String logic = "";
		int acc = 0;
		int num = 0;
		while(acc < outputs){
			if(acc == 0)
				num = r.nextInt(2);
			else
				num = r.nextInt(4);
			
			switch(num){
			case 0:
				acc += 1;
				logic += rand_char("TF");
				break;
			case 1:
				acc += 1;
				logic += rand_char("0123456789abcdef".substring(0, inputs));
				break;
			case 2:
				acc += 0;
				logic += "~";
				break;
			case 3:
				acc -= 1;
				logic += rand_char("x+");
				break;
			}
		}
		
		return new PureOperation(name, logic);
	}
	
	public static Gate rand_gate(){
		String name = Tests.rand_str(5);
		String ins = "", outs = "";
		for(int i = 0; i < 4; i++)
			switch(Tests.r.nextInt(3)){
			case 0:
				ins  += Tests.rand_hex(1);
				outs += "0";
				break;
			case 1:
				ins  += "0";
				outs += Tests.rand_hex(1);
			case 2:
				ins  += "0";
				outs += "0";
				break;
			default: assert false;
			}
		String logic = rand_op(hex_sum(ins), hex_sum(outs)).get_logic();
		return new Gate(name, ins, outs, logic);
	}
}

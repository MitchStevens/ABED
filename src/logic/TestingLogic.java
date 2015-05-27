package logic;

import java.util.List;
import org.junit.Before;
import org.junit.Test;


public class TestingLogic {
	public static List<Input> inputs;
	
	public static String[] qs = new String[]{
		"1 1 &"
	};
	
	public static Boolean[] as = new Boolean[]{
		true
	};
	
	@Before
	private void init(){
		inputs.add(new Input(false));
		inputs.add(new Input(true));
	}
	
	@Test
	private void testing(){
		System.out.println(new Eval(inputs, qs[0]).tokens);
	}
}

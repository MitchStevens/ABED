package core.tokens;

import static core.Utilities.toHex;

import java.util.List;
import java.util.Stack;

public class Flag implements Token {
	public Integer i;

	public Flag(int i) {
		this.i = i;
	}

	public Boolean get(List<Boolean> list) {
		return list.get(i);
	}
	
	@Override
	public Stack<Boolean> eval(Stack<Boolean> stack, List<Boolean> list){
		stack.add(this.get(list));
		return stack;
	}

	@Override
	public String toString() {
		return Character.toString(toHex(i));
	}

	@Override
	public int sum_inputs() {
		return 0;
	}

	@Override
	public int sum_outputs() {
		return 1;
	}

}
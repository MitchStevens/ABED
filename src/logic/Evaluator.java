package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Evaluator {
	List<Token> tokens;
	public String logic;

	public Evaluator(String data) {
		tokens = tokenize(data);
		logic = data;
	}

	public String toString(List<String> inputs) {
		String tbr = "";
		for (Token t : tokens)
			if (t instanceof Flag) {
				tbr += inputs.get(((Flag) t).i) + " ";
			} else
				tbr += t.toString() + " ";
		return init(tbr);
	}

	public static String init(String s) {
		return s.substring(0, s.length() - 1);
	}

	public static List<Token> tokenize(String str) throws Error {
		ArrayList<Token> tokens = new ArrayList<>();
		String[] tokStr = str.split(" ");

		for (String s : tokStr) {
			switch (s) {
			case "&":
				tokens.add(Operation.and);
				continue;
			case "|":
				tokens.add(Operation.or);
				continue;
			case "~":
				tokens.add(Operation.not);
				continue;
			case "t":
			case "T":
				tokens.add(new Flag(true));
				continue;
			case "f":
			case "F":
				tokens.add(new Flag(false));
				continue;
			}

			if (s.matches("\\d+")) {
				tokens.add(new Flag(s));
				continue;
			} else if (s.matches("\\w+>?\\d*")) {
				tokens.add(new Operation(s));
				continue;
			}
		}

		return tokens;
	}

	public Boolean eval(List<Boolean> list) {
		// translated from psuedocode
		// http://en.wikipedia.org/wiki/Reverse_Polish_notation
		Stack<Token> stack = new Stack<>();

		for (Token t : tokens) {
			if (t instanceof Flag) {
				stack.push(t);
			} else if (t instanceof Operation) {
				Operation op = (Operation) t;
				op.apply(stack, list);
			}
		}

		if (stack.isEmpty())
			throw new Error("");
		return ((Flag) stack.pop()).get(list);
		
	}

	public boolean equiv(Evaluator e) {
		// checks if the evals are logically identical using a probabilistic
		// algorithm.
		// would like to get a exact answer but function doesn't scale well,
		// would have to check
		// all possible 2^n busses for a eval with n inputs.
		for (int i = 0; i < 1000; i++) {
			Bus b = Bus.randomBus(32);
			if (this.eval(b.toBooleanList()) != e.eval(b.toBooleanList())) {
				return false;
			}
		}
		return true;
	}
}

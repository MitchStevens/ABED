package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import core.tokens.Flag;
import core.tokens.Token;

public abstract class Utilities {
	
	public static String head(String s){
		return s.substring(0, 1);
	}
	
	public static String tail(String s){
		return s.substring(1);
	}	
	
	/**
	 * Returns everything but the last char of a string/
	 * */
	public static String init(String s){
		return s.substring(0, s.length()-1);
	}
	
	public static String last(String s){
		return s.substring(s.length()-1);
	}

	public static int mod4(int n){
		return n & 0x0003;
	}
	
	/**
	 * Converts a char in the range [0-f] to an int in the range [0-15]
	 * */
	public static int toInt(char c){
		if(48 <= c && c <= 57)
			return c - 48;
		else if(97 <= c && c <= 102)
			return c - 87;
		else
			return -1;
	}
	
	public static int[] toInt(String s){
		char[] chars = s.toCharArray();
		int[] ints = new int[chars.length];
		
		for(int i = 0; i < chars.length; i++)
			ints[i] = toInt(chars[i]);
		return ints;
	}
	
	/**
	 * Converts an int in the range [0-15] to a he char in range [0-f]
	 * */
	public static char toHex(int i){
		if(0 <= i && i <= 9)
			return (char) (i + 48);
		else if(10 <= i && i <= 15)
			return (char) (i + 87);
		else
			return '_';
	}
	
	public static <T> List<T> popn(Stack<T> stack, int n){
		List<T> tbr = new ArrayList<>();
		for (int i = 0; i < n; i++)
			tbr.add(stack.pop());
		return tbr;
	}
	
	public static String[] concat(String[] a, String[] b) {
		String[] tbr = new String[Math.min(a.length, b.length)];
		for (int i = 0; i < tbr.length; i++)
			if (a[i] == null)
				tbr[i] = b[i];
			else
				tbr[i] = a[i] + b[i];
		return tbr;
	}

	public static String[] concat(String[] a, String b) {
		String[] tbr = new String[a.length];
		for (int i = 0; i < tbr.length; i++)
			if (a[i] == null)
				tbr[i] = b;
			else
				tbr[i] = a[i] + b;
		return tbr;
	}
	
	public static int hex_sum(String s){
		int acc = 0;
		for(char c : s.toCharArray())
			acc += toInt(c);
		return acc;
	}
}

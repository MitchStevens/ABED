package testing;

import static core.Utilities.hex_sum;
import static org.hamcrest.CoreMatchers.not;

import java.util.List;
import java.util.Random;

import junit.framework.AssertionFailedError;

public abstract class Tests {
	public static int NUM_TESTS = 100;
	public static Random r = new Random(System.currentTimeMillis());
	
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
	
	//next methods based on http://super-bit.blogspot.com.au/2007/08/assert-eventually.html
	 public static void assertTrueEventually(long max_wait, Runnable thread) throws Exception {
	    long time_out = max_wait + System.currentTimeMillis();
	    while(true){
	    	try{
	    		thread.run();
	    		return;
	    	}catch(AssertionFailedError e){
	    		if(System.currentTimeMillis() > time_out)
	    			throw e;
	    	}
	    	
	    	Thread.sleep(10);
	    }
	}
	 
	 public static void assertEqualsEventually(long max_wait, Runnable thread) throws Exception {
		    long time_out = max_wait + System.currentTimeMillis();
		    while(true){
		    	try{
		    		thread.run();
		    		return;
		    	}catch(AssertionFailedError e){
		    		if(System.currentTimeMillis() > time_out)
		    			throw e;
		    	}
		    	
		    	Thread.sleep(10);
		    }
		}
}

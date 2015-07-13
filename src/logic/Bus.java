package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Bus{
	private List<Boolean> bools;
	
	public Bus(){
		this.bools = new ArrayList<>();
	}
	
	public Bus(Boolean[] list){
		this.bools = Arrays.asList(list);
	}
	
	public Bus(int b){
		this.bools = new ArrayList<>();
		for(int i = 0; i < b; i++)
			bools.add(false);
	}
	
	public Bus(List<Boolean> list){
		this.bools = list;
	}
	
	public void clear(){
		for(int i = 0; i < bools.size(); i++)
			bools.set(i, false);
	}
	
	public static List<Bus> generateBuses(int size){
		/* graph showing time taken to output as a function of size
		 * size     |8|9|10|11|12|13|14|15|16|17 |18
		 * time(ms) |0|1|2 |3 |3 |6 |14|14|28|133|324
		 * really should only be used for size <= 12.
		 * Straight up runs out of memory for size = 23.
		 */

		List<Bus> tbr = new ArrayList<>();
		int[] pows = new int[size];
		for(int i = 0; i < size; i++)
			pows[i] = (int)Math.pow(2, i);
		
		for(int i = 0; i < 2*pows[size-1]; i++){
			Bus b = new Bus();
			for(int n = 0; n < size; n++)
				b.add((i & pows[n]) != 0);
			tbr.add(b);
		}
		return tbr;
	}
	
	public static Bus randomBus(int size){
		Random r = new Random(System.nanoTime());
		Bus tbr = new Bus();
		for(int i = 0; i < size; i++)
			tbr.add(r.nextBoolean());
		return tbr;
	}
	
	@Override
	public String toString() {
		if(bools.size() == 0) return "()";
		String tbr = "(";
		for(Boolean b : bools)
			tbr += (b?"T":"F")+", ";
		return tbr.substring(0, tbr.length() - 2)+")";
	}
	
	public List<Boolean> toBooleanList(){
		return this.bools;
	}
	
	public Bus clone(){
		return new Bus(bools);
	}
	
	public boolean isEmpty(){
		return bools.size() == 0;
	}
	
	public boolean or(){
		//returns true if at least one bool is true
		for(Boolean b : bools)
			if(b) return true;
		return false;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Bus)) return false;
		Bus b = (Bus)o;
		if(b.size() != this.size()) return false;
		for(int i = 0; i < b.size(); i++)
			if(b.get(i) != this.get(i))
				return false;
		return true;
	}
	
	@Override
	public int hashCode(){
		int code = (int)Math.pow(3, this.size());
		for(Boolean b : bools)
			if(b)
				code *= 2;
		return code;
	}
	
//	public boolean equals(Object o){
//		if(!(o instanceof Bus)) return false;
//		Bus c = (Bus)o;
//		for
//	}
	
	//List functions
	public void		add(Boolean b){this.bools.add(b);}
	public int 		size(){ 	return bools.size(); }
	public Boolean 	get(int i){ return bools.get(i); }
	public void 	set(int i, Boolean b){ bools.set(i, b); }
}

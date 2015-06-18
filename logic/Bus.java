package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	@Override
	public String toString() {
		if(bools.size() == 0) return "()";
		String tbr = "(";
		for(Boolean b : bools)
			tbr += b+", ";
		return tbr.substring(0, tbr.length() - 2)+")";
	}
	
	public List<Boolean> toBooleanList(){
		return this.bools;
	}
	
	public Bus clone(){
		return new Bus(bools);
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

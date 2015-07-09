package logic;

import java.util.List;

public class Flag extends Token{
	public Integer i;
	public Boolean b;
	
	Flag(Boolean b){
		this.i = null;
		this.b = b;
	}
	
	Flag(String s){
		this.i = Integer.parseInt(s);
		this.b = null;
	}
	
	Flag(Integer i){
		this.i = i;
		this.b = null;
	}
	
	public Boolean get(List<Boolean> list){
		if(b != null)
			return b;
		else if(i != null)
			return list.get(i);
		return null;
	}
	
	@Override
	public String toString() {
		return i+"";
	}
	
}
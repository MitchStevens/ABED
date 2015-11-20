package circuits;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class Bus extends Observable implements Observer{
	public int size, dir;
	public Circuit c;
	
	// List functions
	public Boolean get(int i) { return toBooleanList().get(i); } 
	
	public abstract List<Boolean> 	toBooleanList();
	public abstract String 			outputAsString();
	
	public abstract Bus				getSpouse();
	public abstract boolean 		tryCouple(Bus b);
	public abstract void 			uncouple();
	public void recouple(){
		if(getSpouse() != null)
			return;
		try{
			Bus spouse = c.game.circuitAtDir(c.coord, dir + c.rot).busAtAbsDir(dir + c.rot + 2);
			if(tryCouple(spouse) == true)
				spouse.c.eval();
			this.notifyObservers();
		}catch(NullPointerException np){
			return;
		}
	}
	
	public boolean or(){
		for(Boolean b : toBooleanList())
			if(b) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		int code = (int) Math.pow(3, size);
		for (Boolean b : toBooleanList())
			if (b) code *= 2;
		return code;
	}
	
//	@Override
//	public boolean equals(Object o){
//		if(this == o)
//			return true;
//		if(!(o instanceof this.getClass()))
//			
//	}
	
	@Override
	public String toString(){
		return toBooleanList().toString();
	}
}

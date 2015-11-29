package circuits;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class Bus extends Observable implements Observer{
	public 			int size, dir;
	public 			Circuit c;
	
	// List functions	
	public abstract List<Boolean> 	toBooleanList();
	public abstract String 			outputAsString();
	
	public abstract Bus				getSpouse();
	public abstract boolean 		tryCouple(Bus b);
	public abstract void 			uncouple();
	
	public Boolean get(int i){
		return toBooleanList().get(i);
	} 
	
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
	
	@Override
	public abstract boolean equals(Object o);
	
	@Override
	public String toString(){
		return toBooleanList().toString();
	}
	
	public static List<Boolean> randomBus(int size){
		List<Boolean> b = new ArrayList<>();
		
		for(int i = 0; i < size; i++)
			b.add(Math.random() > 0.5);
		
		return b;
			
	}
}

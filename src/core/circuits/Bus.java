package core.circuits;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import core.game.Direction;
import core.game.Evaluable;
import core.game.Gate;

/**
 * 
 * */
public abstract class Bus extends Observable implements Observer{
	public 	int 		size;
	public 	Direction 	dir;
	public 	Evaluable 	parent;
	
	/***/
	public abstract List<Boolean> 	to_list();
	
	/***/
	public abstract Bus				get_spouse();
	
	/***/
	public abstract boolean			set_spouse(Bus b);
	
	/**
	 * 
	 * @param i The index of the value of the bus
	 * @return The value requested.
	 * */
	public Boolean get(int i){
		return to_list().get(i);
	} 
	
	public Direction abs_dir(){
		try{
			return dir.rot(parent.get_rot());
		}catch(NullPointerException npe){
			return null;
		}
	}
	
	/**
	 * 
	 * */
	public boolean couple(){
		if(get_spouse() != null)
			return false;
		
		try{
			Evaluable adj = parent.adj(dir.rot(parent.get_rot()));
			Bus spouse = adj.get_bus(abs_dir().rot(2));
					
			if(spouse.get_spouse() != null || getClass().equals(spouse.getClass()))
				return false;
			
			set_spouse(spouse);
			spouse.set_spouse(this);
			
			this.setChanged();
			this.notifyObservers();
			return true;
		}catch(NullPointerException np){
			return false;
		}
	}
	
	/***/
	public boolean uncouple(){	
		if(get_spouse() != null){
			get_spouse().set_spouse(null);
			set_spouse(null);
			return true;
		}else
			return false;
	}
	
	/**
	 * @return {@code false} if all the elements in the bus of {@code false}, {@code true} otherwise. Corresponds to 
	 * the union operator.
	 * */
	public boolean or(){
		for(Boolean b : to_list())
			if(b) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		int code = (int) Math.pow(3, size);
		for (Boolean b : to_list())
			if (b) code *= 2;
		return code;
	}
	
	@Override
	public abstract boolean equals(Object o);

	@Override
	public String toString(){
		return to_list().toString();
	}
	
	public static List<Boolean> random_bus(int size){
		List<Boolean> b = new ArrayList<>();
		
		for(int i = 0; i < size; i++)
			b.add(Math.random() > 0.5);
		
		return b;
			
	}
}

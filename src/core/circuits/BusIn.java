package core.circuits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import core.game.Direction;
import core.game.Evaluable;
import core.game.Gate;

import java.math.*;

import javafx.beans.InvalidationListener;

public class BusIn extends Bus{
	private BusOut spouse;

	public BusIn(Evaluable parent, int size, Direction dir) {
		this.parent = parent;
		this.size = size;
		this.dir = dir;
	}
	
	public int size() { return size; }
	public Boolean get(int i) {
		try{
			return spouse.get(i);
		}catch(NullPointerException e){
			return null;
		}
	}

	public List<Boolean> to_list() {
		if(spouse != null)
			return new ArrayList<>(spouse.to_list());
		else
			return new ArrayList<>(Collections.nCopies(size, false));
	}
	
	@Override
	public Bus get_spouse(){
		return spouse;
	}

	@Override
	public boolean set_spouse(Bus b) {
		if(b instanceof BusOut)
			if(b.size == size){
				this.spouse = (BusOut)b;
				return true;
			}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null) return false;
		if(!(o instanceof BusIn)) return false;
		BusIn b = (BusIn)o;
		
		return
			this.size == b.size &&
			this.dir == b.dir;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}

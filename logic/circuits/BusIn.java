package circuits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javafx.beans.InvalidationListener;

public class BusIn extends Bus{
	BusOut parent;

	public BusIn(Circuit c, int size, int dir) {
		this.c = c;
		this.size = size;
		this.dir = dir;
	}

	// List functions
	public void add(Boolean b) {
		if(parent != null)
			parent.toBooleanList().add(b);
	}
	public int size() { return size; }
	public Boolean get(int i) { 
		if(parent != null)
			return parent.bools.get(i);
		else
			return false;
	}

	public List<Boolean> toBooleanList() {
		if(parent != null)
			return new ArrayList<>(parent.bools);
		else
			return new ArrayList<>(Collections.nCopies(size, false));
	}
	
//	private static <R> List<R> reverse(List<R> l){
//		R r; int len = l.size();
//		List<R> list = new ArrayList<R>(l);
//		for(int i = 0; i < len/2; i++){
//			r = list.get(i);
//			list.set(i, list.get(len - i - 1));
//			list.set(len - i - 1, r);
//		}
//		return list;
//	}  

	@Override
	public boolean tryCouple(Bus b) {
		if(b== null)
			return false;
		else if(!(b instanceof BusOut))
			return false;
		else if(this.size != b.size)
			return false;
		else if(((BusOut)b).child != null || this.parent != null)
			return false;
		else {
			this.parent = (BusOut)b;
			((BusOut)b).child = this;
			return true;
		}
	}

	@Override
	public void uncouple() {
		if(this.parent != null){
			this.parent.child = null;
			this.parent = null;
			this.c.eval();
			this.notifyObservers();
		}
	}
	
	@Override
	public Bus getSpouse(){
		// I just realised that I also used a child/parent metafore to explain the connection of buses
		// DuelingBanjos.mp3
		return parent;
	}

	@Override
	public boolean or() {
		if(parent == null)
			return false;
		else{
			for(Boolean b : parent.bools)
				if(b) return true;
			return false;
		}
	}

	@Override
	public String outputAsString() {
		if(parent != null)
			return parent.outputAsString();
		else return "F";
	}

	@Override
	public void update(Observable o, Object arg) {
		if(c != null)
			this.notifyObservers();
	}
}

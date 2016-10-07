package core.game;

import static core.Utilities.mod4;

import java.util.Observable;

public class ObservableRotation extends Observable {
	int rot;
	
	public ObservableRotation(int rot){
		this.rot = rot;
	}
	
	public int get(){
		return rot;
	}
	
	public void set(int rot){
		this.rot = mod4(rot);
		setChanged();
		notifyObservers();
	}
	
	public void add(int turn){
		this.rot = mod4(this.rot + turn);
		setChanged();
		notifyObservers();
	}
}

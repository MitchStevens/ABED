package circuits;

import static eval.Evaluator.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public class BusOut extends Bus{
	protected List<Boolean> bools;
	public BusIn child;
	
	public BusOut(Circuit c, int size, int dir){
		this.c = c;
		this.size = size;
		this.dir = dir;
		bools = new ArrayList<Boolean>(Collections.nCopies(size, false));
	}

	@Override
	public List<Boolean> toBooleanList() {
		return bools;
	}
	
	@Override
	public String outputAsString(){
		String tbr = "";
		for(Bus b : c.buses)
			if(b instanceof BusIn)
				tbr += b.outputAsString()+" ";
		
		switch (c.name) {
		case "Output":
		case "Bus":
		case "Super":
		case "Right":
		case "Left":
			return init(tbr);
		case "And":
			return tbr + "&";
		case "Or":
			return tbr + "|";
		case "Not":
			return tbr + "~";
		default:
			if(c.outputs <= 1)
				tbr += c.name;
			else{
				int outCount = 0;
				for(int i = 0; i < dir; i++)
					if(c.outputs > 0)
						outCount++;
				tbr += c.name + ">"+ outCount;
			}
		}
		
		return tbr;
	}
	
	public void setOutput(Boolean... bools){
		setOutput(Arrays.asList(bools));
	}
	
	public void setOutput(List<Boolean> newBools){
		if(bools.equals(newBools))
			return;
		else if(bools.size() != newBools.size())
			return;
		else{
			bools = newBools;
			this.notifyObservers();
			if(child != null){
				child.notifyObservers();
				child.c.eval();
			}
			this.notifyObservers();
		}
	}
	
	@Override
	public boolean tryCouple(Bus b) {
		if(b == null)
			return false;
		else if(!(b instanceof BusIn))
			return false;
		else if(this.size != b.size)
			return false;
		else if(((BusIn)b).parent != null || this.child != null)
			return false;
		else {
			this.child = (BusIn)b;
			((BusIn)b).parent = this;
			this.addObserver((BusIn)b);
			return true;
		}
	}
	
	@Override
	public void uncouple() {
		if(this.child != null){
			this.child.parent = null;
			this.child.c.eval();
			this.child = null;
			this.notifyObservers();
		}
	}
	
	@Override
	public Bus getSpouse(){
		// I just realised that I also used a child/parent metafore to explain the connection of buses
		// DuelingBanjos.mp3
		return child;
	}

	@Override
	public void update(Observable o, Object arg) {
		return;
	}
}


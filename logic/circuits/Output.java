package circuits;

import java.util.ArrayList;

public class Output extends Circuit {

	public Output() {
		super();
		this.initData = "";
		this.name = "Output";
		this.evals = new ArrayList<>();
		this.buses.set(3, new BusIn(this, 1, 3));
		this.rot = 0;
	}
	
	public boolean get(){
		return this.buses.get(3).toBooleanList().get(0);
	}
	
	@Override
	public Output clone(){
		return new Output();
	}

}

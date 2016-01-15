package circuits;

import java.util.ArrayList;

public class Output extends Circuit {

	public Output() {
		super();
		this.initData = "";
		this.name = "OUTPUT";
		this.evals = new ArrayList<>();
		initBuses("0,0,0,1", "0,0,0,0");
		this.rot = 0;
		this.type = 0; 
	}
	
	public boolean get(){
		return this.buses.get(3).toBooleanList().get(0);
	}
	
	@Override
	public Output clone(){
		return new Output();
	}

}

package core.eval;

public interface Operation{
	
	public int[] inputs();
	public int[] outputs();
	
	public default int sum_intputs(){
		int acc = 0;
		for(int i : inputs())
			acc += i;
		return acc;
	}
	
	public default int sum_outputs(){
		int acc = 0;
		for(int o : outputs())
			acc += o;
		return acc;
	}
	
	//Evaluate the operation and 
	public void eval();
}

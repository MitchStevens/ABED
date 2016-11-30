package core.eval;

import java.util.List;
import java.util.Observer;

import core.eval.EdgeInfo.EdgeType;
import core.game.Direction;

public interface Operation{
	public String get_name();
	
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
	
	public default EdgeInfo edge_info(Direction d){
		int n = d.value;
		if(inputs()[n] > 0)
			return new EdgeInfo(EdgeType.INPUT, inputs()[n]);
		else if(outputs()[n] > 0)
			return new EdgeInfo(EdgeType.OUTPUT, outputs()[n]);
		else
			return new EdgeInfo(EdgeType.NONE, 0);
	}
	
	//Evaluate the operation and 
	public void eval(List<Boolean> list);
	public List<Boolean> last_inputs();
	public List<Boolean> last_outputs(Direction d);
	
	public Operation clone();
	
	public default void toggle(){
		return;
	}

	public void add_observer(Node node);
	
}

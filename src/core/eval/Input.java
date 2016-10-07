package core.eval;

public class Input implements Operation {
	private static final int[] inputs  = new int[]{0, 0, 0, 0};
	private static final int[] outputs = new int[]{0, 1, 0, 0};
	private boolean value;
	
	public Input(){
		
	}
	
	
	
	@Override
	public int[] inputs() {
		return inputs;
	}

	@Override
	public int[] outputs() {
		// TODO Auto-generated method stub
		return outputs;
	}

	@Override
	public void eval() {
		// T
	}

}

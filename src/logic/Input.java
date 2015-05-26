package logic;

public class Input extends Token{
	int ref;
	
	Input(){}
	
	Input(int b){
		this.ref = ref;
	}
	
	public Boolean get(List<Boolean> list){
		return list.get(ref);
	}

	@Override
	public String toString() {
		return ref+"";
	}
	
}

package logic;

import java.util.List;

public class Input extends Token{
	public Boolean b;
	
	Input(){}
	
	Input(Boolean b){
		this.b = b;
		
	}

	@Override
	public String toString() {
		return b+"";
	}
	
}

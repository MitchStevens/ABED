public class Bracket extends Token{
	String bracket;
	static Bracket left = new Bracket("(");
	static Bracket right = new Bracket(")");
	
	public Bracket(String s){
		this.bracket = s;
	}

	@Override
	public String toString() {
		return "\'"+bracket+"\'";
	}
	
	public boolean equals(Object o){
		if(!(o instanceof Bracket)) return false;
		Bracket b = (Bracket)o;
		return b.bracket.equals(bracket);
	}
	
}

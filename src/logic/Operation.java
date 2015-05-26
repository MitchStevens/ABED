public class Operation extends Token{
	String op;
	String name;
	int precedence;
	int args;
	
  public static Operation and = new Operation("and", "&", 2);
  public static Operation or  = new Operation("or",  "|", 2);
  public static Operation not = new Operation("not", "~", 1)
  
  public Operation(String name, String op, int p, int args){
    this.name = name;
    this.op = op;
    this.precedence = p;
    this.args = args;
  }
  
  public Boolean apply(List<Boolean> bools){
		switch(name){
		case "and": return new Input( bools.get(0).get() && bools.get(1).get() );
		case "or":  return new Input( bools.get(0).get() || bools.get(1).get() );
		case "not": return new Input( !bools.get(0).get() );
		}
		throw new Error("Operator "+op+" not found!");
	}
	
	@Override
	public String toString() {
		return op;
	}
  
  
}

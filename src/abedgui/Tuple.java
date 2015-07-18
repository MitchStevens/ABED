package abedgui;

//can this be generalised to all types?
public class Tuple{
	public int size;
	private Integer[] list; 
	
	public Tuple(Integer...ints){
		this.list = ints;
		this.size = ints.length;
	}
	
	@Override
	public int hashCode(){
		int hash = 101;
		int[] primes = new int[]{2, 3, 5, 7, 11, 13, 17, 19};
		for(int i = 0; i < size; i++)
			if(i < primes.length)
				hash += list[i]*primes[i];
			else
				hash += list[i].hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Tuple)) return false;
		Tuple t = (Tuple)o;
		if(size != t.size) return false;
		for(int i = 0; i < size; i++)
			if(list[i] != t.list[i])
				return false;
		return true;
	}
	
	public Integer get(int i) {
		if(i < list.length)
			return list[i];
		else return null;
	}
}

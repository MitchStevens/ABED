package core.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TwinMap<K1, K2, V> {
	List<K1> keys1 = new ArrayList<>();
	List<K2> keys2 = new ArrayList<>();
	List<V>  values = new ArrayList<>();
	
	public void clear() {
		keys1.clear();
		keys2.clear();
		values.clear();
	}

	public Object equiv(Object key){
		int index1 = keys1.indexOf(key);
		int index2 = keys2.indexOf(key);
		
		if(index1 != -1)
			return keys2.get(index1);
		else if(index2 != -1)
			return keys1.get(index2);
		else
			return null;
	}
	
	public boolean containsKey(Object key) {
		return keys1.contains(key) || keys2.contains(key);
	}

	public boolean containsValue(V value) {
		return values.contains(value);
	}

	public V get(Object key) {
		int index1 = keys1.indexOf(key);
		int index2 = keys2.indexOf(key);
		if(index1 != -1)
			return values.get(index1);
		else if(index2 != -1)
			return values.get(index2);
		else
			return null;
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public Set<K1> key1Set() {
		return new HashSet<K1>(keys1);
	}

	public Set<K2> key2Set() {
		return new HashSet<K2>(keys2);
	}
	
	public V put(K1 key1, K2 key2, V value) {
		if(keys1.contains(key1) || keys2.contains(key2))
			return null;
		else{
			keys1.add(key1);
			keys2.add(key2);
			values.add(value);
			return value;
		}
	}

	public V remove(Object key) {
		int index1 = keys1.indexOf(key);
		int index2 = keys2.indexOf(key);
		if(index1 != -1){
			keys1.remove(index1);
			keys2.remove(index1);
			return values.remove(index1);
		} else if(index2 != -1){
			keys1.remove(index2);
			keys2.remove(index2);
			return values.remove(index2);
		} else
			return null;
	}

	public int size() {
		return values.size();
	}

	public Collection<V> values() {
		return new ArrayList<V>(values);
	}

	@Override
	public String toString(){
		String str = "{";
		for(int i = 0; i < values.size(); i++)
			str += "("+ keys1.get(i) +","+ keys2.get(i) +")="+ values.get(i) +" ";
		str += "}";
		return str;
	}
}

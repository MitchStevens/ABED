package core.game;

import static java.lang.Math.min;
import static core.Utilities.concat;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.circuits.Bus;
import data.Reader;

public interface Meta extends Game{
	/* There are many operations involving circuits on a given game (add, remove, rotate, move)
	 * but there are only 2 fundamental operations, add and remove. All other operation are
	 * specific removals and additions to a game. By letting add() and remove() do all the real work,
	 * we cut down on the amount of repeated code lessen confusion about what does what.
	 * */
	
	public final 	static 	int 				MIN_TILES = 3;
	public final 	static 	int 				MAX_TILES = 10;


	public Map<Coord, Evaluable> get_board();
	public void			 		 set_board(Map<Coord, Evaluable> e);
	
	public int			 get_size();
	public default void	 set_size(int size){
		Map<Coord, Evaluable> new_board = new HashMap<>();
		Map<Coord, Evaluable> old_board = get_board();

		int n = get_size();
		
		for(int i = 0; i < min(size, n)-1; i++)
			for(int j = 0; j < min(size, n)-1; j++){
				Coord pos = new Coord(i, j);
				new_board.put(pos, old_board.get(pos));
			}
		
		set_board(new_board);
		set_size(n);
	}
	
	public default boolean connect(Evaluable e, Coord c){
		if(get_board().containsKey(c))
			return false;
		
		get_board().put(c, e);
		e.set_parent(this);
		e.connect_buses();
		return true;
	}
	
	public default  Evaluable disconnect(Coord c){
		if(!get_board().containsKey(c))
			return null;
		
		Evaluable e = get_board().get(c);
		e.disconnect_buses();
		get_board().remove(c);
		return e;
	}
	
	public default boolean 		add(Evaluable e, Coord c){
		return connect(e, c);
	}
	
	public default Evaluable 	remove(Coord c){
		return disconnect(c);
	}
	
	public default boolean 		rotate(Coord c, int rot){
		Evaluable e = disconnect(c);
		if(e == null)
			return false;
		
		e.add_rot(rot);
		return connect(e, c);
	}
	
	public default boolean 		move(Coord from, Coord to){
		if(get(from) == null || get(to) != null)
			return false;
		
		Evaluable e = disconnect(from);
		return connect(e, to);
	}
	
	public default boolean 		toggle(Coord c){
		Evaluable e = disconnect(c);
		if(e == null)
			return false;
		
		e.toggle(null);
		return connect(e, c);
	}
	
	public default Evaluable get(int i, int j){
		return get(new Coord(i, j));
	}
	
	public default Evaluable get(Coord c){
		try {
			return get_board().get(c);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public default Evaluable to_evaluable(){
		boolean non_recursive = true;
		if(non_recursive)
			return null;//new Circuit();
		else
			return null;//new MetaGame();
	}
	
	public default void clear() {
		// removes circuits but maintains size
		this.set_board(new HashMap<>());
        set_changed();
        notify_observers();
	}
	
	default List<Evaluable> on_edge(int edge) {
		// gets all circuits on a given edge. Ignores corners
		List<Evaluable> tbr = new ArrayList<>();
		int n = get_size();
		Evaluable g;
		switch (edge) {
		case 0:
			for (int i = 1; i < n-1; i++)
				if ((g = get(i, 0)) != null)
					tbr.add(g);
			break;
		case 1:
			for (int j = 1; j < n-1; j++)
				if ((g = get(n-1, j)) != null)
					tbr.add(g);
			break;
		case 2:
			for (int i = n-2; i >= 1; i--)
				if ((g = get(i, n-1)) != null)
					tbr.add(g);
			break;
		case 3:
			for (int j = n-2; j >= 1; j--)
				if ((g = get(0, j)) != null)
					tbr.add(g);
			break;
		}
		return tbr;
	}

	public default List<Evaluable> on_edge(int edge, String name) {
		List<Evaluable> tbr = new ArrayList<>();
		for(Evaluable g : on_edge(edge))
			if(g.get_name().equals(name))
				tbr.add(g);
		return tbr;
	}
	
	public default void pretty_print() {
		// returns a pretty visualisation of the current game so I know what I'm
		// testing.
		int n = get_size();
		String tbr = "|";
		for (int i = 0; i < n - 1; i++)
			tbr += "-------";
		tbr += "------|\n";

		for (int j = 0; j < n; j++) {
			String[] crntRow = new String[3];
			crntRow = concat(crntRow, "|");
			for (int i = 0; i < n; i++) {
				if (get(i, j) != null)
					crntRow = concat(crntRow, get(i, j).repr());
				else
					crntRow = concat(crntRow, "      ");
				if (i != n - 1)
					crntRow = concat(crntRow, "|");
			}
			tbr += crntRow[0] + "|\n" + crntRow[1] + "|\n" + crntRow[2] + "|\n";
			if (j != n - 1) {
				tbr += "|";
				for (int i = 0; i < n - 1; i++)
					tbr += "------|";
				tbr += "------|\n";
			}
		}

		tbr += "|";
		for (int i = 0; i < n - 1; i++)
			tbr += "-------";
		tbr += "------|\n";

		System.out.println(tbr);
	}
}

package abedgui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import logic.Game;
import circuits.Circuit;
import circuits.Coord;
import static circuits.Circuit.allCircuits;
import static circuits.Circuit.mod4;

public class GameNavigator {
	//The main job of this class is to create a wire path, based on a game board and a set of squares
	Game g;
	
	public GameNavigator(Game g){
		this.g = g;
	}
	
	public List<Coord> getTrail(Coord start, Coord finish){		
		//A* algorithm, taken from pseudocode at https://en.wikipedia.org/wiki/A*_search_algorithm
		Set<Coord> closed_set 		= new HashSet<>();
		Set<Coord> open_set   		= new HashSet<>();
		Map<Coord, Coord> came_from = new HashMap<>();
		Map<Coord, Integer> g_score = new HashMap<>();
		Map<Coord, Integer> f_score = new HashMap<>();
		
		open_set.add(start);
		for(int i = 0; i < g.n; i++)
			for(int j = 0; j < g.n; j++){
				g_score.put(new Coord(i, j), Integer.MAX_VALUE/10);
				f_score.put(new Coord(i, j), Integer.MAX_VALUE/10);
			}
		
		while(!open_set.isEmpty()){
			Coord curr = open_set.iterator().next();
			for(Coord c : open_set)
				if(f_score.get(c) < f_score.get(curr))
					curr = c;
			
			if(curr.equals(finish))
				return reconstruct_path(came_from, curr);
			
			open_set.remove(curr);
			closed_set.add(curr);
			for(Coord neigh : get_neighbours(curr)){
				if(closed_set.contains(neigh))
					continue;
				
				int tentative_g_score = g_score.get(curr) + curr.taxicabDistance(neigh);
				
				if(!open_set.contains(neigh) || tentative_g_score < g_score.get(neigh)){
					came_from.put(neigh, curr);
					g_score.put(neigh, tentative_g_score);
					f_score.put(neigh, g_score.get(neigh) + 0);
					open_set.add(neigh);
				}
			}
		}
		
		return null;
	}
	
	private List<Coord> get_neighbours(Coord c){
		List<Coord> tbr = new ArrayList<>();
		
		if(c.j - 1 >= 0 && g.circuitAtPos(new Coord(c.i, c.j -1)) == null)
			tbr.add(new Coord(c.i, c.j -1));
		if(c.i + 1 < g.n && g.circuitAtPos(new Coord(c.i +1, c.j)) == null)
			tbr.add(new Coord(c.i +1, c.j));
		if(c.j + 1 < g.n && g.circuitAtPos(new Coord(c.i, c.j +1)) == null)
			tbr.add(new Coord(c.i, c.j +1));
		if(c.i - 1 >= 0 && g.circuitAtPos(new Coord(c.i -1, c.j)) == null)
			tbr.add(new Coord(c.i -1, c.j));
		
		return tbr;
	}
	
	private List<Coord> reconstruct_path(Map<Coord, Coord> came_from, Coord curr){
		List<Coord> total_path = new ArrayList<>();
		total_path.add(curr);
		while(came_from.containsKey(curr)){
			curr = came_from.get(curr);
			total_path.add(curr);
		}
		return total_path;
	}
}

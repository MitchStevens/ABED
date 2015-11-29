package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import circuits.Cable;
import circuits.Coord;

public class PathFinder {
	//The main job of this class is to create a wire path, based on a game board and a set of squares
		Game g;
		Set<Coord> empty_spaces;
		
		public PathFinder(Game g){
			this.g = g;
			empty_spaces = new HashSet<>();
			for(int i = 0; i < g.n; i++)
				for(int j = 0; j < g.n; j++)
					if(g.circuitAtPos(new Coord(i, j)) == null)
						empty_spaces.add(new Coord(i, j));
			
			empty_spaces.remove(new Coord(0,     0));
			empty_spaces.remove(new Coord(0,     g.n -1));
			empty_spaces.remove(new Coord(g.n-1, 0));
			empty_spaces.remove(new Coord(g.n-1, g.n-1));
		}
		
		public List<Coord> getTrail(Coord start, Coord finish){		
			//A* algorithm, taken from pseudocode at https://en.wikipedia.org/wiki/A*_search_algorithm
			Set<Coord> closed_set 		= new HashSet<>();
			Set<Coord> open_set			= new HashSet<>();
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
			
			if(empty_spaces.contains(new Coord(c.i   , c.j -1)))
				tbr.add(new Coord(c.i, c.j -1));
			if(empty_spaces.contains(new Coord(c.i +1, c.j)))
				tbr.add(new Coord(c.i +1, c.j));
			if(empty_spaces.contains(new Coord(c.i	 , c.j +1)))
				tbr.add(new Coord(c.i, c.j +1));
			if(empty_spaces.contains(new Coord(c.i -1, c.j)))
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
		
		public List<Cable> create_cable_list(Coord start, Coord finish){
			List<Coord> list = getTrail(start, finish);
			if(list == null)
				return null;
			
			List<Cable> tbr = new ArrayList<>();
			Cable c0 = new Cable();
			c0.coord = list.get(0);
			Direction d0 = Direction.getDir(list.get(0), list.get(1));
			c0.setRot(d0.value -1);
			tbr.add(c0);
			
			for(int i = 1; i < list.size() -1; i++){
				Cable c = new Cable();
				c.coord = list.get(i);
				Direction d_in = Direction.getDir(list.get(i), list.get(i+1));
				Direction d_out = Direction.getDir(list.get(i-1), list.get(i));
				c.setRot(d_in.value -1);
				c.output_toggle = delta_i(d_out.value - c.rot);
				tbr.add(c);
			}
			
			int n = list.size();
			
			Cable cn = new Cable();
			cn.coord = list.get(n-1);
			Direction dn = Direction.getDir(list.get(n-1), list.get(n-2));
			cn.setRot(dn.value +1);
			tbr.add(cn);
			
			return tbr;
		}
		
		private boolean[] delta_i(int i){
			switch(i & 0x0003){
			case 0:  return new boolean[]{true,  false, false};
			case 1:  return new boolean[]{false, true,  false};
			case 2:  return new boolean[]{false, false, true};
			default: return new boolean[]{false, false, false};
			}
		}
}





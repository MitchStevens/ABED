package core.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import static core.Utilities.concat;
import static core.Utilities.mod4;

import core.eval.EdgeInfo;
import core.eval.Function;
import core.eval.Node;
import core.eval.Operation;
import core.eval.OperationWithData;

public class Game extends Observable implements Observer {
	
	public static int MAX_TILES = 10;
	public static int MIN_TILES = 3;
	
	private int n;
	public Map<Coord, OperationWithData> board = new HashMap<>();
	public Function f;
	
	public Game(int n){
		this.n = n;
		this.f = new Function("root_function");
		this.f.addObserver(this);
	}
	
	public Operation get_op(String id){
		try{
			return f.get_node(id).op;
		}catch(NullPointerException e){
			return null;
		}
	}
	
	public Operation get_op(Coord c){
		try{
			return board.get(c);
		}catch(NullPointerException e){
			return null;
		}
	}
	
	public int get_rot(Coord c){
		try{
			return board.get(c).get_rot();
		}catch(NullPointerException e){
			return 0;
		}
	}
	
	public int get_size(){
		return n;
	}
	
	public void set_size(int size){
		//TODO: do;
	}
	
	public boolean add(Operation op, Coord c){
		String id = unique_id();
		OperationWithData op_data = new OperationWithData(op, id, 0, c);
		f.add(op_data, id);
		board.put(c, op_data);
		connect(c);
		return true;
	}
	
	/**
	 * 
	 * */
	public boolean remove(Coord c){
		disconnect(c);
		OperationWithData op_data = board.remove(c);
		f.remove(op_data.get_id());
		return true;
	}
	
	public boolean rotate(Coord c, int rot){
		disconnect(c);
		OperationWithData op_data = board.get(c);
		op_data.add_rot(rot);
		connect(c);
		return true;
	}
	
	public boolean move(Coord from, Coord to){
		disconnect(from);
		OperationWithData op_data = board.get(from);
		op_data.set_pos(to);
		connect(to);
		return true;
	}
	
	public boolean toggle(Coord c){
		disconnect(c);
		OperationWithData op_data = board.get(c);
		op_data.get_op().toggle();
		connect(c);
		return true;
	}
	
	/**
	 * Ports 
	 * 
	 * */
	public void connect(Coord c){
		OperationWithData op1 = board.get(c), op2;
		Direction dir1, dir2;
		EdgeInfo edge1, edge2;
		
		for(Direction abs_dir : Direction.values())
			try{
				dir1 = op1.get_abs_rot(abs_dir);
				op2  = board.get(c.at(abs_dir));
				dir2 = op2.get_abs_rot(abs_dir.add_rot(2));
				
				String id = unique_id();
				edge1 = op1.edge_info(dir1);
				edge2 = op2.edge_info(dir2);
				if(edge1.could_connect_to(edge2))
					f.port(id, op1.get_id(), dir1, op2.get_id(), dir2);
				else if(edge1.could_connect_to(edge2))
					f.port(id, op2.get_id(), dir2, op1.get_id(), dir1);
			} catch(NullPointerException e){
				continue;
			}
	}
	
	//Disconnects all edges from a given node
	public void disconnect(Coord c){
		OperationWithData op1 = board.get(c);
		f.unport(op1.get_id());
	}
	
	public Coord next_open(){
		for(Coord c : Coord.over_square(n))
			if(!board.containsKey(c) && !c.on_corner(n))
				return c;
		return Coord.NULL;
	}
	
	private String unique_id(){
		String id;
		do{
			id = core.Utilities.rand_str(8);
		}while(f.get_nodes().containsKey(id));
		return id;
	}
	
	public void print_game(){
		String tbr = "|";
		for (int i = 0; i < n - 1; i++)
			tbr += "-------";
		tbr += "------|\n";
		Coord c;
		
		for (int i = 0; i < n; i++) {
			String[] crntRow = new String[3];
			crntRow = concat(crntRow, "|");
			for (int j = 0; j < n; j++) {
				if (board.get(c = new Coord(i, j)) != null)
					crntRow = concat(crntRow, repr(c));
				else
					crntRow = concat(crntRow, "      ");
				if (j != n - 1)
					crntRow = concat(crntRow, "|");
			}
			tbr += crntRow[0] + "|\n" + crntRow[1] + "|\n" + crntRow[2] + "|\n";
			if (i != n - 1) {
				tbr += "|";
				for (int j = 0; j < n - 1; j++)
					tbr += "------|";
				tbr += "------|\n";
			}
		}

		tbr += "|";
		for (int i = 0; i < n - 1; i++)
			tbr += "-------";
		tbr += "------|\n";
		
		tbr += "Nodes: "+ f.num_nodes() +", Edges: "+ f.num_edges() +"\n";
		
		System.out.println(tbr);
	}
	
	private String[] repr(Coord c){
		String[] rep = new String[]{"      ", "      ", "      "};
		if(!board.containsKey(c))
			return rep;
		
		OperationWithData op = board.get(c);	
		String[][] a = new String[][]{
			{"/\\", "\\/"},
			{"< ", " >"},
			{"\\/", "/\\"},
			{"> ", " <"}
		};
		String[] sym = new String[4];
		
		EdgeInfo info;
		for(Direction d : Direction.values()){
			info = op.abs_dir_edge_info(d);
			switch(info.get_type()){
			case INPUT:  sym[d.value] = a[d.value][0]; break;
			case OUTPUT: sym[d.value] = a[d.value][1]; break;
			case NONE:	 sym[d.value] = "  ";		   break; 
			}
		}

		rep[0] = "  "+ sym[0] +"  ";
		rep[1] = sym[3] + op.get_op().get_name().substring(0,2) + sym[1];
		rep[2] = "  "+ sym[2] +"  ";
		return rep;
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(123);
	}
}











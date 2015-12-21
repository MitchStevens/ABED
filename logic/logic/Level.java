package logic;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import panes.CircuitPane;
import circuits.Circuit;
import circuits.Coord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public class Level {
	public static 	List<Level> 			ALL_LEVELS;
	public static 	List<String>			LEVEL_TITLES = new ArrayList<>();
	public static 	ObservableSet<Level> 	unlockedLevels;
	//public static ObservableSet<Level> completedLevels = FXCollections.observableSet();
	
	public 			String 			name			= "";
	public 			Circuit 		objective		= null;
	public 			List<Circuit> 	circuitRewards	= new ArrayList<>();
	public 			List<String> 	levelRewards	= new ArrayList<>();
	public 			String 			instructionText	= "";
	public 			String 			completionText	= "";
	public 			Integer 		gameSize		= 0;
	public 			Coord 			tuple			= null;
	
	static {
		ALL_LEVELS = Reader.loadLevels();
		unlockedLevels = Reader.loadUnlockedLevels();
	}
	
	public Level(){}
//	public Level(String datum, Coord coord) {
//		//NAME;objective;circuitRewards;levelRewards;goalText;optGame Size
//		this.datum = datum;
//		String[] data = datum.split(";");
//		name = data[0];
//		objective = Circuit.allCircuits.get(data[1]);
//		
//		circuitRewards = new ArrayList<>();
//		for (String s : data[2].split(","))
//			circuitRewards.add(Circuit.allCircuits.get(s));
//		
//		levelRewards = new ArrayList<>();
//		for(String s : data[3].split(","))
//			levelRewards.add(s);
//
//		goalText = data[4];
//		gameSize = Integer.parseInt(data[5]);
//		
//		tuple = coord;
//	}
	
	public Level nextLevel(){
		//returns level after l. If l is the last level in a set, return null.
		Coord nextTuple = new Coord(tuple.i, tuple.j + 1);
		List<Level> nextList = search(lvl -> { return lvl.tuple.equals(nextTuple); });
		if(!nextList.isEmpty())
			return nextList.get(0);
		else return null;
	}
	
	public static List<Level> search(Predicate<Level> p){
		return ALL_LEVELS.stream()
				.filter(p)
				.collect(toList());
	}
	
	public void onCompletion(){
		for(Circuit c : circuitRewards)
			CircuitPane.unlockedCircuits.add(c);
		
		for(String s : levelRewards){
			List<Level> list = search( lvl -> {return lvl.name.equals(s);} );
			if(!list.isEmpty())
				Level.unlockedLevels.add(list.get(0));
		}
		
		//completedLevels.add(this);
	}
	
	public boolean isComplete(Game g) {
		return objective.equiv(g.toCircuit());
	}
	
	@Override
	public Level clone(){
		Level tbr 			= new Level();
		tbr.name 			= this.name;
		tbr.objective 		= this.objective;
		tbr.circuitRewards 	= this.circuitRewards;
		tbr.levelRewards 	= this.levelRewards;
		tbr.instructionText = this.instructionText;
		tbr.completionText	= this.completionText;
		tbr.gameSize 		= this.gameSize;
		return tbr;
	}
	
	@Override
	public int hashCode(){
		int hash = 101;
		hash *= 2*name.hashCode();
		hash *= 3*objective.hashCode();
		hash *= 5*circuitRewards.hashCode();
		hash *= 7*levelRewards.hashCode();
		hash *= 11*instructionText.hashCode();
		hash *= 13*completionText.hashCode();
		hash *= 17*gameSize.hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Level)) 							return false;
		Level l = (Level)o;
		if(!this.name.equals(l.name))						return false;
		if(!this.objective.equals(l.objective)) 			return false;
		if(!this.circuitRewards.equals(l.circuitRewards)) 	return false;
		if(!this.levelRewards.equals(l.levelRewards)) 		return false;
		if(!this.instructionText.equals(l.instructionText)) return false;
		if(!this.completionText.equals(l.completionText)) 	return false;
		if(!this.gameSize.equals(l.gameSize)) 				return false;
		return true;
	}
	
	@Override
	public String toString(){
		return 	"{name: "+ name +"}, " +
				"{objective: "+ objective +"}, " +
				"{circuitRewards: "+ circuitRewards +"}, " +
				"{levelRewards: "+ levelRewards +"}, " +
				"{instructionText: "+ instructionText +"}, " +
				"{completionText: "+ completionText +"}, " +
				"{gameSize: "+ gameSize +"}";
	}
}

package abedgui;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import logic.Circuit;
import logic.Game;
import logic.Reader;

public class Level {
	public static List<Level> ALL_LEVELS;
	public static ObservableSet<Level> unlockedLevels;
	//public static ObservableSet<Level> completedLevels = FXCollections.observableSet();
	
	String datum;
	public String name;
	public Circuit objective;
	public List<Circuit> circuitRewards;
	public List<String> levelRewards;
	public String goalText;
	public Integer gameSize;
	public Tuple tuple;
	
	public Level(){}
	public Level(String datum, Tuple t) {
		//NAME;objective;circuitRewards;levelRewards;goalText;optGame Size
		this.datum = datum;
		String[] data = datum.split(";");
		name = data[0];
		objective = Circuit.ALL_CIRCUITS.get(data[1]);
		
		circuitRewards = new ArrayList<>();
		for (String s : data[2].split(","))
			circuitRewards.add(Circuit.ALL_CIRCUITS.get(s));
		
		levelRewards = new ArrayList<>();
		for(String s : data[3].split(","))
			levelRewards.add(s);

		goalText = data[4];
		gameSize = Integer.parseInt(data[5]);
		
		tuple = t;
	}
	
	public static Level nextLevel(Level l){
		//returns level after l. If l is the last level in a set, return null.
		Tuple nextTuple = new Tuple(l.tuple.get(0), l.tuple.get(1) + 1);
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
	
	public Level(Circuit c){
		objective = c;
	}
	
	public void onCompletion(){
		for(Circuit c : circuitRewards)
			Circuit.unlockedCircuits.add(c);
		
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
		tbr.goalText 		= this.goalText;
		tbr.gameSize 		= this.gameSize;
		return tbr;
	}
	
	@Override
	public int hashCode(){
		int hash = 101;
		hash += 2*name.hashCode();
		hash += 3*objective.hashCode();
		hash += 5*circuitRewards.hashCode();
		hash += 7*levelRewards.hashCode();
		hash += 11*goalText.hashCode();
		hash += 13*gameSize.hashCode();
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
		if(!this.goalText.equals(l.goalText)) 				return false;
		if(!this.gameSize.equals(l.gameSize)) 				return false;
		return true;
	}
	
	@Override
	public String toString(){
		return name;
	}
}

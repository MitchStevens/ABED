package circuits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Queue;

import data.Reader;
import eval.Evaluator;
import javafx.collections.ObservableSet;
import logic.Game;

public class Circuit extends Observable{
	
	public 			int 					inputs = 0, outputs = 0;
	public 			int 					type = 0;
	public 			String 					name = "";
	protected		String 					initData;
	public 			List<Evaluator> 		evals;
	public final 	List<Bus> 				buses = new ArrayList<>();
	// rot is the number of CLOCKWISE rotations
	public 			Integer 				rot;
	public 			Coord 					coord;
	// used to sort circuits in the gui
	public 			Game 					game;
	// evals.size should be the same as outputBus.size
	public 			List<List<String>> 		evalStrings = null;
	
	//Only use this constructor when creating a new circuit from a class.
	public Circuit(){}
	
	public Circuit(String datum) {
		//this.initData = datum;
		// NAME;INPUTS;OUTPUTS;EVALS
		String[] data = datum.split(";");
		this.name = data[0];
		this.evals = new ArrayList<>();
		if (data.length == 4)
			for (String s : data[3].split(","))
				evals.add(new Evaluator(s));
		initBuses(data[1], data[2]);
		this.rot = 0;
		coord = Coord.NULL;
	}
	
	public Circuit(String name, String inputs, String outputs, String evals){
		this.initData = name +";"+ inputs +";"+ outputs +";"+ evals;
		this.name = name;
		this.evals = new ArrayList<>();
		for(String s : evals.split(","))
			this.evals.add(new Evaluator(s));
		initBuses(inputs, outputs);
		this.rot = 0;
		coord = Coord.NULL;
	}

	public void setGame(Game g) {
		this.game = g;
	}

	public void setRot(int rot) {
		this.rot = mod4(rot);
	}

	public void addRot(int add) {
		// +1 is 90 clockwise, -1 is 90 anti clockwise, etc.
		this.rot = mod4(rot + add);
	}

	public static int mod4(int i) {
		// even works for negatives. bitmasks FTW.
		return i & 0x0003;
	}

	public void toggle() {
		// This method is going to be used for the input circuit, but I like the
		// idea of
		// being able to toggle a circuit to do other things. Making room for
		// when my mind baby arrives.
	}

	protected void initBuses(String ins, String outs) {
		String[] sIn  = ins.split(",");
		String[] sOut = outs.split(",");
		
		for(int dir = 0; dir < 4; dir++){
			if(Integer.parseInt(sIn[dir]) > 0){
				inputs += Integer.parseInt(sIn[dir]);
				BusIn b = new BusIn(this, Integer.parseInt(sIn[dir]), dir);
				buses.add(b);
			} else if(Integer.parseInt(sOut[dir]) > 0){
				outputs += Integer.parseInt(sOut[dir]);
				BusOut b = new BusOut(this, Integer.parseInt(sOut[dir]), dir);
				buses.add(b);
			} else {
				buses.add(null);
			}
		}
	}

	public List<Boolean> flattenInputs() {
		List<Boolean> tbr = new ArrayList<>();
		for (Bus b : buses)
			if(b instanceof BusIn)
				tbr.addAll(b.toBooleanList());
		return tbr;
	}
	
	public List<Boolean> flattenOutputs() {
		List<Boolean> tbr = new ArrayList<>();
		for (Bus b : buses)
			if(b instanceof BusOut)
				tbr.addAll(b.toBooleanList());
		return tbr;
	}
	
	public Queue<Boolean> eval() {
		// evaluates tile and updates outputBus
		List<Boolean> flatInput = flattenInputs();
		Queue<Boolean> flatOutput = new LinkedList<>();
		
		for (Evaluator e : evals)
			flatOutput.add(e.eval(flatInput));

		for(int i = 0; i < 4; i++)
			if(buses.get(i) instanceof BusOut){
				List<Boolean> values = new ArrayList<>();
				for(int j = 0; j < buses.get(i).size; j++)
					values.add(flatOutput.poll());
				((BusOut)buses.get(i)).setOutput(values);
			}
		
		this.setChanged();
		this.notifyObservers();
		return flatOutput;
	}

	public void recoupleBuses(){
		for(Bus b : buses)
			if(b != null && b instanceof BusIn)
				b.recouple();
		for(Bus b : buses)
			if(b != null && b instanceof BusOut)
				b.recouple();
	}
	
	public void uncoupleBuses(){
		for(Bus b : buses)
			if(b != null)
				b.uncouple();
	}
	
	public Bus busAtAbsDir(int dir) {
		// returns output at dir, taking into account rotation
		return buses.get(mod4(dir - rot));
	}

	public String pos() {
		
		return "(" +coord.i + ", " + coord.j + ")";
	}

	@Override
	public Circuit clone() {
		return new Circuit(this.initData);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Circuit))
			return false;
		Circuit c = (Circuit) o;
		if (!name.equals(c.name))
			return false;
		if (coord.i != null)
			if (!coord.i.equals(c.coord.i))
				return false;
		if (coord.j != null)
			if (!coord.j.equals(c.coord.j))
				return false;
		return true;
	}

	public boolean equiv(Circuit c) {
		if(c == null) return false;
		
		for (int i = 0; i < 4; i++) {
			if(buses.get(i) == null && c.buses.get(i) == null)
				continue;
			
			if(buses.get(i) == null ^ c.buses.get(i) == null)
			
			if(buses.get(i) != null)
				if(!buses.get(i).equals(c.buses.get(i)))
					return false;
			else
				if(c.buses.get(i) != null)
					return false;
		}
		
		if (this.evals.size() != c.evals.size())
			return false;
		for (int i = 0; i < evals.size(); i++)
			if (!evals.get(i).equiv(c.evals.get(i)))
				return false;
		
		return true;
	}

	public String[] printCircuit() {
		// prints circuit for printGame()
		String[] tbr = new String[3];
		tbr[0] = "      ";
		tbr[1] = "  " + name.substring(0, 2).toUpperCase() + "  ";
		tbr[2] = "      ";

		Bus b;
		if ((b = buses.get(mod4(0 - rot))) != null)
			tbr[0] = (b instanceof BusIn ? "  \\/  " : "  /\\  ");
		
		if ((b = buses.get(mod4(1 - rot))) != null)
			tbr[1] = tbr[1].substring(0, 4) + (b instanceof BusIn ? "<<" : ">>");
		
		if ((b = buses.get(mod4(2 - rot))) != null)
			tbr[2] = (b instanceof BusIn ? "  /\\  " : "  \\/  ");

		if ((b = buses.get(mod4(3 - rot))) != null)
			tbr[1] = (b instanceof BusIn ? ">>" : "<<") + tbr[1].substring(2, 6);


		return tbr;
	}

	public String getData() {
		return name + "," + rot + "," + coord.i + "," + coord.j;
	}

	@Override
	public String toString() {
		String tbr = name + ";";
		String in = "", out = "";
		
		for(int i = 0 ; i < 4; i++)
			if(buses.get(i) instanceof BusIn){
				in  += buses.size()+",";
				out += "0,";
			} else if(buses.get(i) instanceof BusOut){
				out += buses.size()+",";
				in  += "0,";
			} else {
				in  += "0,";
				out += "0,";
			}
		
		tbr += in+";"+out+";";
		
		for (Evaluator e : evals)
			tbr += e.logic + ",";
		return tbr;
	}
	
	public String toXML(){
		String tbr = "<circuit_name=\""+ this.name +"\" ";
		
		tbr += "inputs=\"";
		for(int dir = 0; dir < 4; dir++)
			tbr += (this.buses.get(dir) instanceof BusIn ? this.buses.get(dir).size : 0) +",";
		tbr += "\" ";
		
		tbr += "outputs=\"";
		for(int dir = 0; dir < 4; dir++)
			tbr += (this.buses.get(dir) instanceof BusOut ? this.buses.get(dir).size : 0) +",";
		tbr += "\" ";
		
		tbr += "evals=\"";
		for(int i = 0; i < evals.size(); i++)
			tbr += evals.get(i).logic;
		tbr += "\" \\>";
		
		return tbr;
	}
	
	/*		<circuit name="DISPLAY"
					inputs="0,0,4,0"
					outputs="0,0,0,0"
					evals="">
			</circuit>
	 */
	
}
package core.game;

import static core.Utilities.mod4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import core.circuits.Bus;
import core.circuits.Input;
import core.circuits.Output;

public class Circuit extends Observable implements Evaluable, Meta{
	private Map<Coord, Evaluable> board = new HashMap<>();
	private int n;
	private int rot = 0;
	private Coord pos;
	private String name;
	private Meta parent;
	private Bus[] buses = new Bus[4];
	private List<Input>  inputs   = new ArrayList<>();
	private List<Output> outputs = new ArrayList<>();

	@Override
	public void set_changed() {
		this.setChanged();
	}

	@Override
	public void notify_observers() {
		this.notifyObservers();
	}

	@Override
	public int get_size() {
		return n;
	}

	@Override
	public int get_rot() {
		return rot;
	}


	@Override
	public void set_rot(int rot) {
		this.rot = rot;
	}


	@Override
	public void add_rot(int r) {
		this.rot = mod4(this.rot + r);
	}


	@Override
	public Coord get_pos() {
		return pos;
	}


	@Override
	public void set_pos(Coord c) {
		this.pos.set(c);
	}


	@Override
	public String get_name() {
		return name;
	}


	@Override
	public void set_name(String name) {
		this.name = name;
	}


	@Override
	public Meta get_parent() {
		return parent;
	}


	@Override
	public void set_parent(Meta m) {
		this.parent = m;
	}


	@Override
	public Bus[] get_buses() {
		return buses;
	}


	@Override
	public boolean equiv(Evaluable e) {
		return false;
	}


	@Override
	public void eval() {
		
	}

	@Override
	public Map<Coord, Evaluable> get_board() {
		return this.board;
	}

	@Override
	public void set_board(Map<Coord, Evaluable> e) {
		this.board = e;
	}

	@Override
	public void toggle(Object o) {
		;
	}
	
}

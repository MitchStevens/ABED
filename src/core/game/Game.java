package core.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import actions.Action;
import core.circuits.*;
import data.Reader;

import static java.util.stream.Collectors.toList;
import static core.Utilities.mod4;
import static java.lang.Math.min;

public interface Game {
	
	public void set_changed();
	public void notify_observers();

	@Override
	public abstract String toString();
}

package tutorials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;

import com.sun.javafx.tk.FontMetrics;

import data.Reader;
import panes.CircuitPane;
import panes.Gui;
import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Game;

public class Tutorial implements Observer {	
	int current_step = 0;
	Message current_message;
	List<Step> steps = new ArrayList<>();
	public static final Map<String, Tutorial> ALL_TUTORIALS = new HashMap<>();
	
	static {
		ALL_TUTORIALS.put("Introduction to BabyTown", new Tute1());
	}
	
	public Tutorial(Game g){
		g.addObserver(this);
	}
	
	public void start(){
		steps.get(0).m.display(new Double[]{Gui.boardWidth - Gui.SIDE_BAR_WIDTH, 30.0});
	}

	public void next_step(){
		current_step++;
		if(current_step >= steps.size())
			return;
		
		steps.get(current_step -1).m.remove();
		steps.get(current_step).m.display(new Double[]{Gui.boardWidth - Gui.SIDE_BAR_WIDTH, 30.0});
	}
	
	public void end(){
		for(Step s : steps)
			CircuitPane.cp.getChildren().remove(s);		
	}
	
	@Override
	public void update(Observable arg0, Object action) {
		System.out.println("game changed!");
		System.out.println(action);
		
		if(action instanceof Action)
			if(steps.get(current_step).is_satisfied((Action)action)){
				System.out.println("completed step "+current_step);
				next_step();
			}
	}
}

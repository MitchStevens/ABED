package core.eval;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EvalQueue{
	Function f;
	Queue<Node> queue = new LinkedList<Node>();

	public ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	public Runnable task = new Runnable() {
	    public void run() {
			while(!queue.isEmpty()){
				queue.poll().eval();
			}
	    }
	};
	
	public EvalQueue(Function f){
		this.f = f;
		executor.scheduleAtFixedRate(task, 0, f.eval_time, TimeUnit.MILLISECONDS);
	}
	
	public void add(Node n){
		try { Thread.sleep(f.eval_time); }
		catch (InterruptedException e) { e.printStackTrace(); }
		queue.add(n);
	}
}
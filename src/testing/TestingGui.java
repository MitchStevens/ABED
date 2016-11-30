package testing;

import gui.controls.Scoreboard;
import gui.controls.Typer;
import gui.graphics.SpinEffect;
import gui.panes.Gui;

import data.Reader;
import tutorials.SlideMessage;
import tutorials.PointerMessage;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class TestingGui extends Application{
	public 				Group 			root;
	private				Scene 			scene;
	private 			String[] 		string_list = new String[]{
			"But a slightly longer note might take a number of lines to display, some perhaps two or three at a stretch.",
			"A short note",
			"But how big can a note become? It is probably better to err on the side of caution when instatiating a TutorialNote. You never really know how the text is going to segment itself before you display it, thus we have no choice but to use heuristics.",
			"Her name was Caroline Frances Hubert, and she had three claims to fame."
			+ "In the first place she was the thirty-seventh oldest living human being. Caroline herself was unimpressed by this fact."
			+ "To her way of thinking it was the result of an accident, nothing more."
			+ "In any case she had been the thirty-seventh oldest human being for a long, long time, and it got to seem more of a bore than an accomplishment after a while."
	};
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Testing");
		root = new Group();
		scene = new Scene(root, 1024 - Gui.SIDE_BAR_WIDTH, 768);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		add_scoreboard();
		
	}
	
	public void add_note(int i){
		PointerMessage t = new PointerMessage(string_list[i]);
		root.getChildren().add(t);
		
		Circle c = new Circle(200, 400, 5, Color.RED);
		root.getChildren().add(c);
	}
	
	private void set_message(int i){
		SlideMessage sm = new SlideMessage(string_list[i]);
		root.getChildren().add(sm);
	}
	
	private void add_typer(){
		Typer ty = new Typer("B04T5 4ND H035", 30);
		ty.setLayoutX(300);
		ty.setLayoutY(300);
		root.getChildren().add(ty);
		ty.play();
	}
	
	private void add_scoreboard(){
		double h = 800;
		Scoreboard sc = new Scoreboard(h, "0123456789", "777");
		root.getChildren().add(sc);
//		sc.add_sym('f');
//		sc.add_sym('u');
		
		Button b = new Button("do");
		b.setLayoutY(h);
		root.getChildren().add(b);
	}
	
//	private void add_spin(){
//		Image a = Reader.ALL_IMAGES.get("And");
//		Image b = Reader.ALL_IMAGES.get("Or");
//		
//		SpinEffect s = new SpinEffect(a, b);
//		root.getChildren().add(s);
//		s.play();
//	}
	
	public static void open() {
		Application.launch();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

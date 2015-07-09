package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.*;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import logic.Circuit;
import logic.Game;

public class Reader {
	private final static String[] circuitList = new String[]{
		"BasicCircuitList.txt",
		"SingleCircuitList.txt",
		"MultiCircuitList.txt"
	};
	
	private final static String gameList = "GameList.txt";
	
	public static Map<String, Circuit> loadCircuits(){
		Map<String, Circuit> tbr = new HashMap<>();
		
		tbr.put("Input", Circuit.Input);
		
		for(int i = 0 ; i < circuitList.length; i++){
			for(String s : readFile(circuitList[i])){
				Circuit c = new Circuit(s);
				c.type = i;
				tbr.put(c.name, c);
			}
		}
		
		return Collections.unmodifiableMap(tbr);
	}
	
	public static Map<String, Game> loadGames(){
		Map<String, Game> tbr = new HashMap<>();
		for(String s : readFile(gameList)){
			Game g = new Game(s);
			tbr.put(g.name, g);
		}
		return Collections.unmodifiableMap(tbr);
	}
	
	public static Map<String, Image> loadImages() {
		Map<String, Image> images = new HashMap<>();
		
		Path p = FileSystems.getDefault().getPath("src/images");
		try {
			Files.walkFileTree(p, new SimpleFileVisitor<Path>(){
				@Override
			     public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
					Image image = new Image(new FileInputStream(path.toFile()));
					String name = path.toFile().getName();
			        images.put(name.substring(0, name.length()-4), image);
			        return FileVisitResult.CONTINUE;
			     }
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.unmodifiableMap(images);
	}
	
	public static Font loadFont(String s){
		Path path = FileSystems.getDefault().getPath("src", "fonts", s);
		try {
			return Font.loadFont(new FileInputStream(path.toFile()), 18);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return Font.font("Arial", 18);
		}
	}
	
	private static List<String> readFile(String location){
		Path path = FileSystems.getDefault().getPath("src", "data", location);
		try {
			return Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return new ArrayList<String>();
		}
	}
}
package data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.Circuit;
import logic.Game;

public class Reader {
	private final static String circuitList = "CircuitList.txt";
	private final static String gameList = "GameList.txt";
	
	public static Map<String, Circuit> loadCircuits(){
		Path path = FileSystems.getDefault().getPath("src", "data", circuitList);
		Map<String, Circuit> tbr = new HashMap<>();
		tbr.put("Input", Circuit.Input);
		try {
			List<String> strs = Files.readAllLines(path, StandardCharsets.UTF_8);
			for(String s : strs){
				Circuit c = new Circuit(s);
				tbr.put(c.name, c);
			}
			return tbr;
		} catch (IOException e) {
			return tbr;
		}
	}
	
	public static Map<String, Game> loadGames(){
		Path path = FileSystems.getDefault().getPath("src", "data", gameList);
		Map<String, Game> tbr = new HashMap<>();
		try {
			List<String> strs = Files.readAllLines(path, StandardCharsets.UTF_8);
			for(String s : strs){
				Game g = new Game(s);
				tbr.put(g.name, g);
			}
			return tbr;
		} catch (IOException e) {
			return tbr;
		}
	}
}

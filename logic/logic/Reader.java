package logic;

import graphics.PieceImage;

import java.io.File;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import panes.LevelSelectPane;
import circuits.Cable;
import circuits.Circuit;
import circuits.Coord;
import circuits.Input;
import circuits.Output;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;

public class Reader {
	private final static String[] circuitList = new String[] {
		"BasicCircuitList.dolphin",
		"SingleCircuitList.dolphin",
		"DualCircuitList.dolphin",
		"QuadCircuitList.dolphin",
		"UserCreatedCircuits.dolphin"
	};
	
	private static final String circuitUnlocked = "circuitsUnlocked.dolphin";
	
	private final static String[] levelList = new String[] {
		"levelSet00.dolphin",
		"levelSet01.dolphin",
		"levelSet02.dolphin",
		"levelSet03.dolphin"
	};
	
	private final static String levelUnlocked = "levelsUnlocked.dolphin";

	private final static String gameList = "GameList.dolphin";
	
	public static Map<String, Circuit>  loadCircuits() {
		Map<String, Circuit> map = new HashMap<>();

		map.put("Input",  	new Input());
		map.put("Output", 	new Output());
		map.put("Cable", 	new Cable());

		for (int i = 0; i < circuitList.length; i++) {
			for (String s : readFile("circuit_data/"+circuitList[i])) {
				Circuit c = new Circuit(s);
				c.type = i;
				map.put(c.name, c);
			}
		}

		return Collections.unmodifiableMap(map);
	}
	
	public static ObservableSet<Circuit> loadUnlockedCircuits(){
		ObservableSet<Circuit> tbr = FXCollections.observableSet();

		for (String s : readFile("circuit_data/"+circuitUnlocked).get(0).split(","))
			tbr.add(Circuit.allCircuits.get(s));

		return tbr;
	}

	public static List<Level> loadLevels(){
		File file = new File("src/res/level_data/level_data.xml");
		List<Level> tbr = new ArrayList<Level>();
		
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			
			//what does this line do? research.
			doc.getDocumentElement().normalize();
			
			NodeList level_sets = doc.getElementsByTagName("level_set");
			for(int i = 0; i < level_sets.getLength(); i++){
				Level.LEVEL_TITLES.add(((Element)level_sets.item(i)).getAttribute("name"));
				NodeList levels = ((Element)level_sets.item(i)).getElementsByTagName("level");
				
				for(int j = 0; j < levels.getLength(); j++){
					Level l = new Level();
					
					Node node = levels.item(j);
					Element e = (Element)node;
					
					l.name = e.getAttribute("name");
					l.objective = Circuit.allCircuits.get(get_elem(e, "objective"));
					get_elems(e, "circuit_reward").forEach(s -> l.circuitRewards.add(Circuit.allCircuits.get(s)));
					l.levelRewards = get_elems(e, "level_reward");
					l.instructionText = get_elem(e, "instruction_text");
					l.completionText = get_elem(e, "completion_text");
					l.gameSize = Integer.parseInt(get_elem(e, "min_game_size"));
					l.tuple = new Coord(i, j);
					
					tbr.add(l);
				}
			}
			
		} catch (Exception e){}
		
		return tbr;
	}
	
	private static String get_elem(Element e, String tag){
		return e.getElementsByTagName(tag).item(0).getTextContent();
	}
	
	private static List<String> get_elems(Element e, String tag){
		List<String> list = new ArrayList<>();
		for(int i = 0; i < e.getElementsByTagName(tag).getLength(); i++)
			list.add(e.getElementsByTagName(tag).item(i).getTextContent());
		return list;
	}
	
	public static ObservableSet<Level> loadUnlockedLevels(){
		ObservableSet<Level> tbr = FXCollections.observableSet();
		
		for(String str :  readFile("level_data/"+levelUnlocked))
			for(String name : str.split(";")){
				List<Level> l = Level.search( lvl -> {return lvl.name.equals(name);} );
				if(!l.isEmpty())
					tbr.add(l.get(0));
			}
		
		return tbr;
	}
	
	public static Map<String, Game> loadGames() {
		Map<String, Game> tbr = new HashMap<>();
		for (String s : readFile("data/"+gameList)) {
			Game g = new Game(s);
			tbr.put(g.name, g);
		}
		return Collections.unmodifiableMap(tbr);
	}

	public static Map<String, Image> loadImages() {
		Map<String, Image> images = new HashMap<>();

		Path p = FileSystems.getDefault().getPath("src/res/images/");
		try {
			Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path path,
						BasicFileAttributes attrs) throws IOException {
					Image image = new Image(new FileInputStream(path.toFile()));
					String name = path.toFile().getName();
					images.put(name.substring(0, name.length() - 4),
							resample(image));
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.unmodifiableMap(images);
	}

	// taken from https://gist.github.com/jewelsea/5415891
	public static Image resample(Image input) {
		final int W = (int) input.getWidth();
		final int H = (int) input.getHeight();
		final int S = 5;
		WritableImage output = new WritableImage(W * S, H * S);
		PixelReader reader = input.getPixelReader();
		PixelWriter writer = output.getPixelWriter();
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				final int argb = reader.getArgb(x, y);
				for (int dy = 0; dy < S; dy++) {
					for (int dx = 0; dx < S; dx++) {
						writer.setArgb(x * S + dx, y * S + dy, argb);
					}
				}
			}
		}
		return output;
	}

	public static Font loadFont(String s) {
		return loadFont(s, 18);
	}

	public static Font loadFont(String s, int size) {
		Path path = FileSystems.getDefault().getPath("src/res/fonts", s);
		try {
			return Font.loadFont(new FileInputStream(path.toFile()), size);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return Font.font("Arial", size);
		}
	}

	private static List<String> readFile(String location) {
		Path path = FileSystems.getDefault().getPath("src/res/"+location);
		try {
			return Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return new ArrayList<String>();
		}
	}
	
	public static String loadCSS(String css){
		return "res/css/"+css;
	}

}

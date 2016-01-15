package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.*;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import circuits.Cable;
import circuits.Circuit;
import circuits.Coord;
import circuits.Input;
import circuits.Output;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import logic.Game;
import logic.Level;

public class Reader {
	public static 	Map<String, Circuit> 	ALL_CIRCUITS		= new LinkedHashMap<>();
	public static 	Map<String, Image> 		ALL_IMAGES			= new HashMap<>();
	public static 	Map<String, Game> 		ALL_GAMES			= new HashMap<>();
	public static	Map<String, Level>		ALL_LEVELS			= new LinkedHashMap<>();
	//public static 	List<Level> 			ALL_LEVELS			= new ArrayList<>();
	
	public static	List<String>	CIRCUIT_CATEGORIES	= new ArrayList<>();
	public static 	List<String>	LEVEL_CATEGORIES	= new ArrayList<>();
	
	public static 	ObservableSet<Level> 	unlocked_levels		= FXCollections.observableSet();
	public static 	ObservableSet<Circuit> 	unlocked_circuits 	= FXCollections.observableSet();
	public static	List<Circuit>			new_circuits		= new ArrayList<>();
	
	private static final String circuitUnlocked = "circuitsUnlocked.dolphin";
	private final static String levelUnlocked = "levelsUnlocked.dolphin";
	private final static String gameList = "GameList.dolphin";
	
	public static void readAll() {
		readCircuits();
		readUnlockedCircuits();
		readLevels();
		readUnlockedLevels();
		readGames();
		readImages();
	}
	
	//xml complient
	public static void  readCircuits() {
		ALL_CIRCUITS.put("INPUT",  	new Input());
		ALL_CIRCUITS.put("OUTPUT", 	new Output());
		ALL_CIRCUITS.put("CABLE", 	new Cable());
		
		Document doc = get_XML_doc("circuit_list");
		
		try{
			//what does this line do? research.
			doc.getDocumentElement().normalize();
			
			NodeList category = doc.getElementsByTagName("category");
			
			for(int i = 0; i < category.getLength(); i++){
				CIRCUIT_CATEGORIES.add(((Element)category.item(i)).getAttribute("name"));
				NodeList circuits = ((Element)category.item(i)).getElementsByTagName("circuit");
				
				for(int j = 0; j < circuits.getLength(); j++){
					Node node = circuits.item(j);
					Element e = (Element)node;
					
					String name 	= e.getAttribute("name");
					String inputs 	= e.getAttribute("inputs");
					String outputs 	= e.getAttribute("outputs");
					String evals 	= e.getAttribute("evals");
					
					Circuit c = new Circuit(name, inputs, outputs, evals);
					c.type = i;
					
					ALL_CIRCUITS.put(name, c);
				}
			}
			
		} catch (Exception e){}
		
	}
	
	//xml complient
	public static void readUnlockedCircuits(){
		Document doc = get_XML_doc("unlocked_circuits");
		
		try{
			//what does this line do? research.
			doc.getDocumentElement().normalize();
			
			NodeList circuits = doc.getElementsByTagName("circuit");
			
			for(int i = 0; i < circuits.getLength(); i++){
				Element e = (Element)circuits.item(i);
				String name = e.getAttribute("name");
				unlocked_circuits.add(ALL_CIRCUITS.get(name));
			}
			
		} catch (Exception e){}
		
	}
	
	//xml complient
	public static void readLevels(){
		Document doc = get_XML_doc("level_list");
		
		try{
			//what does this line do? research.
			doc.getDocumentElement().normalize();
			
			NodeList level_sets = doc.getElementsByTagName("level_set");
			for(int i = 0; i < level_sets.getLength(); i++){
				LEVEL_CATEGORIES.add(((Element)level_sets.item(i)).getAttribute("name"));
				NodeList levels = ((Element)level_sets.item(i)).getElementsByTagName("level");
				
				for(int j = 0; j < levels.getLength(); j++){
					Level l = new Level();
					
					Node node = levels.item(j);
					Element e = (Element)node;
					
					l.name = e.getAttribute("name");
					l.objective = ALL_CIRCUITS.get(get_elem(e, "objective"));
					
					for(Node n : get_elems(e, "circuit_reward"))
						l.circuitRewards.add(ALL_CIRCUITS.get(((Element)n).getTextContent()));
					
					for(Node n : get_elems(e, "level_reward"))
						l.levelRewards.add(((Element)n).getTextContent());

					l.instructionText = get_elem(e, "instruction_text");
					l.completionText = get_elem(e, "completion_text");
					l.gameSize = Integer.parseInt(get_elem(e, "min_game_size"));
					l.tuple = new Coord(i, j);
					
					ALL_LEVELS.put(l.name, l);
				}
			}
			
		} catch (Exception e){}
	}
	
	public static Document get_XML_doc(String file){
		try{
			File f = new File("src/res/xml/"+ file +".xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			return doc;
		}catch(SAXException | IOException | ParserConfigurationException e){
			return null;
		}
	}
	
	public static String get_elem(Element e, String tag){
		return e.getElementsByTagName(tag).item(0).getTextContent();
	}
	
	public static List<Node> get_elems(Element e, String tag){
		List<Node> list = new ArrayList<>();
		for(int i = 0; i < e.getElementsByTagName(tag).getLength(); i++)
			list.add(e.getElementsByTagName(tag).item(i));
		return list;
	}
	
	public static void readUnlockedLevels(){
		Document doc = get_XML_doc("unlocked_levels");
		
		try{
			//what does this line do? research.
			doc.getDocumentElement().normalize();
			
			NodeList circuits = doc.getElementsByTagName("level");
			
			for(int i = 0; i < circuits.getLength(); i++){
				Element e = (Element)circuits.item(i);
				String name = e.getAttribute("name");
				unlocked_levels.add(ALL_LEVELS.get(name));
			}
			
		} catch (Exception e){}
	}
	
	public static void readGames() {
		Document doc = get_XML_doc("game_list");
		
		try{
			//what does this line do? research.
			doc.getDocumentElement().normalize();
			
			NodeList games = doc.getElementsByTagName("game");
			for(int i = 0; i < games.getLength(); i++){
				Element e = (Element)games.item(i);
				String name = ((Element)games.item(i)).getAttribute("name");
				int n = Integer.parseInt(e.getAttribute("size"));
				Game g = new Game(name, n);
				for(Node node : get_elems(e, "circuit")){
					Element circ_data = (Element)node;
					Circuit c = ALL_CIRCUITS.get(circ_data.getAttribute("name")).clone();
					Coord coord = new Coord(circ_data.getAttribute("pos"));
					int rot = Integer.parseInt(circ_data.getAttribute("rot"));
					c.setRot(rot);
					g.add(c, coord);
				}
				
			}
			
		} catch (Exception e){}
		
//		for (String s : readFile("data/"+gameList)) {
//			Game g = new Game(s);
//			ALL_GAMES.put(g.name, g);
//		}
		
		
	}

	public static void readImages() {
		Path p = FileSystems.getDefault().getPath("src/res/images/");
		try {
			Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path path,
						BasicFileAttributes attrs) throws IOException {
					Image image = new Image(new FileInputStream(path.toFile()));
					String name = path.toFile().getName();
					ALL_IMAGES.put(name.substring(0, name.length() - 4),
							resample(image));
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

//	private static List<String> readFile(String location) {
//		Path path = FileSystems.getDefault().getPath("src/res/"+location);
//		try {
//			return Files.readAllLines(path, StandardCharsets.UTF_8);
//		} catch (IOException e) {
//			return new ArrayList<String>();
//		}
//	}
	
	public static String loadCSS(String css){
		return "res/css/"+css;
	}

}

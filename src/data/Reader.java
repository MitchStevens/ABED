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
import java.util.Collection;
import java.util.Collections;
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

import core.game.Gate;
import core.game.Game;
import core.logic.Level;
import core.tokens.Composite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;

public class Reader {
	//Files
	private final static String COMPOSITES_DOC = "composite_list";
	
	//Unmodifiable lists
	public static Map<String, Gate> 		GATES;
	public static Map<String, Image> 		IMAGES;
	public static Map<String, Game> 		GAMES;
	public static Map<String, Level>		LEVELS;
	public static Map<String, Composite>	COMPOSITES;
	
	public static	List<String>	CIRCUIT_CATEGORIES	= new ArrayList<>();
	public static 	List<String>	LEVEL_CATEGORIES	= new ArrayList<>();
	
	public static 	ObservableSet<Level> 	unlocked_levels		= FXCollections.observableSet();
	public static 	ObservableSet<Gate> 	unlocked_circuits 	= FXCollections.observableSet();
	public static	List<Gate>			new_circuits		= new ArrayList<>();
	
	static {
//		read_circuits();
//		read_images();
//		read_games();
//		read_levels();
		read_composites();
//		read_unlocked_circuits();
//		read_unlocked_levels();
	}
	
	public static Composite get_composite(String key){
		if(COMPOSITES.containsKey(key))
			return COMPOSITES.get(key);
		else
			return null;
	}
	
	public static Gate get_circuit(String key){
		if(GATES.containsKey(key))
			return GATES.get(key).clone();
		else
			return null;
	}
	
	public static Image get_image(String key){
		return IMAGES.get(key);
	}
	
//	public static Game get_game(String key){
//		if(ALL_GAMES.containsKey(key))
//			return ALL_GAMES.get(key).clone();
//		else
//			return null;
//	}
	
	/**
	 * Reads in all the circuits and saves them in the hashmap 'ALL_CIRCUITS'.
	 * */
//	private static void read_circuits() {
//		Map<String, Gate> map = new HashMap<>();
//		
//		map.put("INPUT",  	new Input());
//		map.put("OUTPUT", 	new Output());
//		map.put("CABLE", 	new Cable());
//		
//		Document doc = get_XML_doc("circuit_list");
//		
//		try{
//			//what does this line do? research.
//			doc.getDocumentElement().normalize();
//			
//			NodeList category = doc.getElementsByTagName("category");
//			
//			for(int i = 0; i < category.getLength(); i++){
//				CIRCUIT_CATEGORIES.add(((Element)category.item(i)).getAttribute("name"));
//				NodeList circuits = ((Element)category.item(i)).getElementsByTagName("circuit");
//				
//				for(int j = 0; j < circuits.getLength(); j++){
//					Node node = circuits.item(j);
//					Element e = (Element)node;
//					
//					String name 	= e.getAttribute("name");
//					String inputs 	= e.getAttribute("inputs");
//					String outputs 	= e.getAttribute("outputs");
//					String evals 	= e.getAttribute("evals");
//					
//					Gate c = new Gate(name, inputs, outputs, evals);
//					c.type = i;
//					
//					map.put(name, c);
//				}
//			}
//			
//		} catch (Exception e){
//			
//		}
//		
//		ALL_GATES = Collections.unmodifiableMap(map);
//		
//	}
	
	/**
	 * Reads in all the circuits and saves them in the hashmap 'ALL_OPERATIONS'.
	 * */
	private static void read_composites() {
		Map<String, Composite> map = new HashMap<>();
		String[] names = null, logics = null;
		
		Document doc = get_XML_doc(COMPOSITES_DOC);
		
		try{
			//what does this line do? research.
			doc.getDocumentElement().normalize();
			
			NodeList ops = doc.getElementsByTagName("operation");
			names  = new String[ops.getLength()];
			logics = new String[ops.getLength()];
			for(int i = 0; i < ops.getLength(); i++){
				Element e = (Element)ops.item(i);
				names[i] = e.getAttribute("name");
				logics[i] = e.getAttribute("logic");
				map.put(names[i], new Composite());
			}
			
		} catch(NullPointerException npe){
			System.out.println("Couldn't read in operations.");
			npe.printStackTrace();
		}
		
		COMPOSITES = Collections.unmodifiableMap(map);
		for(int i = 0; i < map.size(); i++)
			COMPOSITES.get(names[i]).set_logic(logics[i]);		
	}
	
	//xml complient
//	private static void read_unlocked_circuits(){
//		
//		
//		Document doc = get_XML_doc("unlocked_circuits");
//		
//		try{
//			//what does this line do? research.
//			doc.getDocumentElement().normalize();
//			
//			NodeList circuits = doc.getElementsByTagName("operations");
//			
//			for(int i = 0; i < circuits.getLength(); i++){
//				Element e = (Element)circuits.item(i);
//				String name = e.getAttribute("name");
//				unlocked_circuits.add(ALL_GATES.get(name));
//			}
//			
//		} catch (Exception e){}
//		
//	}
	
	//xml complient
//	private static void read_levels(){
//		Map<String, Level> map = new HashMap<>();
//		
//		Document doc = get_XML_doc("level_list");
//		
//		try{
//			//what does this line do? research.
//			doc.getDocumentElement().normalize();
//			
//			NodeList level_sets = doc.getElementsByTagName("level_set");
//			for(int i = 0; i < level_sets.getLength(); i++){
//				LEVEL_CATEGORIES.add(((Element)level_sets.item(i)).getAttribute("name"));
//				NodeList levels = ((Element)level_sets.item(i)).getElementsByTagName("level");
//				
//				for(int j = 0; j < levels.getLength(); j++){
//					Level l = new Level();
//					
//					Node node = levels.item(j);
//					Element e = (Element)node;
//					
//					l.name = e.getAttribute("name");
//					l.objective = ALL_GATES.get(get_elem(e, "objective"));
//					
//					for(Node n : get_elems(e, "circuit_reward"))
//						l.circuitRewards.add(ALL_CIRCUITS.get(((Element)n).getTextContent()));
//					
//					for(Node n : get_elems(e, "level_reward"))
//						l.levelRewards.add(((Element)n).getTextContent());
//
//					l.instructionText = get_elem(e, "instruction_text");
//					l.completionText = get_elem(e, "completion_text");
//					l.gameSize = Integer.parseInt(get_elem(e, "min_game_size"));
//					l.tuple = new Coord(i, j);
//					
//					map.put(l.name, l);
//				}
//			}
//			
//		} catch (Exception e){}
//		
//		ALL_LEVELS = Collections.unmodifiableMap(map);
//	}
	
	private static Document get_XML_doc(String file){
		File f = new File("src/res/xml/"+ file +".xml");
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			return doc;
		}catch(SAXException e){
			System.err.println("The xml file "+ f.toString() +" has syntax errors.");
		}catch(IOException e){
			System.err.println("An IO error occured when trying to read file from "+ f.toString() +".");
		}catch(ParserConfigurationException e){
			System.err.println("The xml file "+ f.toString() +" has a serious configuration error.");
		}
		
		return null;
	}
	
	private static String get_elem(Element e, String tag){
		return e.getElementsByTagName(tag).item(0).getTextContent();
	}
	
	private static List<Node> get_elems(Element e, String tag){
		List<Node> list = new ArrayList<>();
		for(int i = 0; i < e.getElementsByTagName(tag).getLength(); i++)
			list.add(e.getElementsByTagName(tag).item(i));
		return list;
	}
	
	private static void read_unlocked_levels(){
		Document doc = get_XML_doc("unlocked_levels");
		
		try{
			//what does this line do? research.
			doc.getDocumentElement().normalize();
			
			NodeList circuits = doc.getElementsByTagName("level");
			
			for(int i = 0; i < circuits.getLength(); i++){
				Element e = (Element)circuits.item(i);
				String name = e.getAttribute("name");
				unlocked_levels.add(LEVELS.get(name));
			}
			
		} catch (Exception e){}
	}
	
//	private static void read_games() {
//		Map<String, Game> map = new HashMap<>();
//		Document doc = get_XML_doc("game_list");
//		
//		try{
//			//what does this line do? research.
//			doc.getDocumentElement().normalize();
//			
//			NodeList games = doc.getElementsByTagName("game");
//			for(int i = 0; i < games.getLength(); i++){
//				Element e = (Element)games.item(i);
//				String name = ((Element)games.item(i)).getAttribute("name");
//				int n = Integer.parseInt(e.getAttribute("size"));
//				Game g = new Game(name, n);
//				for(Node node : get_elems(e, "circuit")){
//					Element circ_data = (Element)node;
//					Circuit c = ALL_CIRCUITS.get(circ_data.getAttribute("name")).clone();
//					Coord coord = new Coord(circ_data.getAttribute("pos"));
//					int rot = Integer.parseInt(circ_data.getAttribute("rot"));
//					c.setRot(rot);
//					g.add(c, coord);
//				}
//				
//			map.put(g.name, g);
//				
//			}
//			
//		} catch (Exception e){}
//		
//		ALL_GAMES = Collections.unmodifiableMap(map);
//		
//		
//	}

	private static void read_images() {
		Map<String, Image> map = new HashMap<>();
		
		Path p = FileSystems.getDefault().getPath("src/res/images/");
		try {
			Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path path,
						BasicFileAttributes attrs) throws IOException {
					Image image = new Image(new FileInputStream(path.toFile()));
					String name = path.toFile().getName();
					map.put(name.substring(0, name.length() - 4),
							resample(image));
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IMAGES = Collections.unmodifiableMap(map);
	}
	
	// taken from https://gist.github.com/jewelsea/5415891
	private static Image resample(Image input) {
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

package data;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import circuits.BusIn;
import circuits.BusOut;
import circuits.Circuit;
import logic.Level;

public class Writer {
	
	public static void save_all(){
		save_unlocked_circuits();
		save_unlocked_levels();
	}
	
	public static void save_unlocked_circuits(){
		
		try{
			File f = new File("src/res/xml/unlocked_circuits.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			
			Node root = doc.getFirstChild();
			
			while(root.hasChildNodes())
				root.removeChild(root.getFirstChild());
			
			for(Circuit c : Reader.unlocked_circuits){
				Element e = doc.createElement("circuit");
				e.setAttribute("name", c.name);
				root.appendChild(e);
				System.out.println(c.name);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
			
		}catch(Exception e){
			e.printStackTrace();
		}

    }
	
	public static void save_unlocked_levels(){
		
		try{
			File f = new File("src/res/xml/unlocked_levels.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			
			Node root = doc.getFirstChild();
			
			while(root.hasChildNodes())
				root.removeChild(root.getFirstChild());
			
			for(Level l : Reader.unlocked_levels){
				Element e = doc.createElement("level");
				e.setAttribute("name", l.name);
				root.appendChild(e);
				System.out.println(l.name);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
			
		}catch(Exception e){
			e.printStackTrace();
		}


    }
	
public static void unlock_level(Circuit c){
		
		try{
			File f = new File("src/res/xml/unlocked_circuits.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			
			Node root = doc.getFirstChild();
			
			Element e = doc.createElement("circuit");
			e.setAttribute("name", c.name);
			
			root.appendChild(e);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
			
		}catch(Exception e){
			e.printStackTrace();
		}

    }
	
	public static void write_circuit(Circuit c){
		
		try{
			File f = new File("src/res/xml/user_circuits.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			
			Node root = doc.getFirstChild();
			
			root.appendChild(circuit_to_XML(doc, c));
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
			
		}catch(Exception e){
			e.printStackTrace();
		}

    }
	
	public static Element circuit_to_XML(Document doc, Circuit c){
		Element circuit = doc.createElement("circuit");
		circuit.setAttribute("name", c.name);
		String ins = "", outs = "", evals = "";
		
		for(int dir = 0; dir < 4; dir++)
			ins += (c.buses.get(dir) instanceof BusIn ? c.buses.get(dir).size : 0) +",";
		circuit.setAttribute("inputs", ins);
		
		for(int dir = 0; dir < 4; dir++)
			outs += (c.buses.get(dir) instanceof BusOut ? c.buses.get(dir).size : 0) +",";
		circuit.setAttribute("outputs", outs);
		
		for(int i = 0; i < c.evals.size(); i++)
			evals += c.evals.get(i).logic;
		circuit.setAttribute("evals", evals);
		
		return circuit;
	}

}

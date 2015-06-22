package logic;

import java.util.List;

import javafx.scene.image.Image;

public class Input extends Circuit{
	boolean value = false;
	
	public Input(){
		super("Input;0,0,0,0;0,1,0,0;");
	}
	
	public void setValue(Boolean b){
		value = b;
	}
	
	public void toggle(){
		//toggles value true->false, false->true. It is my very favorite hack.
		value ^= true;
		this.game.updateGame(i, j);
	}
	
	public Image getSprite(){
		String s = "Input"+(value ? "1" : "0");
		return loadedImages.get(s);
	}
	
	@Override
	public List<Bus> eval(){
		outputBus.set(1, new Bus(new Boolean[]{value}));
		return outputBus;
	}
	
	public Circuit clone(){
		return new Input();
	}
	
}
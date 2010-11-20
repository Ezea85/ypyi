package ar.edu.utn.ypyi.frontend.drawing;

import org.mt4j.MTApplication;

import ar.edu.utn.ypyi.frontend.drawing.MainDrawingScene;

public class StartDrawExample extends MTApplication{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static void main(String args[]){
		initialize();
	}
	
	//@Override
	public void startUp(){
		this.addScene(new MainDrawingScene(this, "Main drawing scene"));
//		DrawingScene scene = new DrawingScene(this, "scene");
//		scene.setClear(false);
//		this.addScene(scene);
//		this.frameRate(50);
	}
	
}




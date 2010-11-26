package ar.edu.utn.ypyi.frontend.slides;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.sceneManagement.transition.FadeTransition;
import org.mt4j.sceneManagement.transition.FlipTransition;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.opengl.GLFBO;

import processing.core.PImage;

public class SlideScene2 extends AbstractScene {

	private MTApplication mtApp;
	protected Iscene nextSceneInner;

	public SlideScene2(MTApplication mtApplication, String name, PImage pImage, boolean hasPrev, PImage arrow) {
		super(mtApplication, name);
		this.mtApp = mtApplication;
		
		//Set the background color
		this.setClearColor(new MTColor(0, 0, 0, 0));
		
		this.registerGlobalInputProcessor(new CursorTracer(mtApp, this));
		
		MTRectangle rect = new MTRectangle(pImage, mtApplication);
		this.getCanvas().addChild(rect);
		rect.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		
		if(hasPrev){
			
			MTImageButton previousSceneButton = new MTImageButton(arrow, mtApplication);
			previousSceneButton.setNoStroke(true);
			if (MT4jSettings.getInstance().isOpenGlMode())
				previousSceneButton.setUseDirectGL(true);
			previousSceneButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					switch (ae.getID()) {
					case TapEvent.BUTTON_CLICKED:
					case TapEvent.BUTTON_UP:
						mtApp.popScene();
						break;
					default:
						break;
					}
				}
			});
			getCanvas().addChild(previousSceneButton);
			previousSceneButton.scale(-1, 1, 1, previousSceneButton.getCenterPointLocal(), TransformSpace.LOCAL);
			previousSceneButton.setPositionGlobal(new Vector3D(previousSceneButton.getWidthXY(TransformSpace.GLOBAL) + 5, mtApp.height - previousSceneButton.getHeightXY(TransformSpace.GLOBAL) - 5, 0));
		}	
		
		
		//Set a scene transition - Flip transition only available using opengl supporting the FBO extenstion
		if (MT4jSettings.getInstance().isOpenGlMode() && GLFBO.isSupported(mtApp))
			this.setTransition(new FlipTransition(mtApp, 700));
		else{
			this.setTransition(new FadeTransition(mtApp));
		}
	}
	
	
	public void setNextScene(Iscene nextScene, PImage arrow){
		nextSceneInner = nextScene;
		//Button to get to the next scene
		MTImageButton nextSceneButton = new MTImageButton(arrow, mtApp);
		nextSceneButton.setNoStroke(true);
		if (MT4jSettings.getInstance().isOpenGlMode())
			nextSceneButton.setUseDirectGL(true);
		nextSceneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:
				case TapEvent.BUTTON_UP:
					//Save the current scene on the scene stack before changing
					mtApp.pushScene();
					mtApp.changeScene(nextSceneInner);
					break;
				default:
					break;
				}
			}
		});
		getCanvas().addChild(nextSceneButton);
		nextSceneButton.setPositionGlobal(new Vector3D(mtApp.width - nextSceneButton.getWidthXY(TransformSpace.GLOBAL) - 5, mtApp.height - nextSceneButton.getHeightXY(TransformSpace.GLOBAL) - 5, 0));

	}
	

	@Override
	public void init() {
		System.out.println("Entered scene: " +  this.getName());
	}

	@Override
	public void shutDown() {
		System.out.println("Left scene: " +  this.getName());
	}

}

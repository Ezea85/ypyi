/***********************************************************************
 * mt4j Copyright (c) 2008 - 2010 Christopher Ruff, Fraunhofer-Gesellschaft All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package ar.edu.utn.ypyi.frontend.slides;

import javax.media.opengl.GL;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTOverlayContainer;
import org.mt4j.components.visibleComponents.widgets.MTSceneMenu;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.opengl.GLTexture;
import org.mt4j.util.opengl.GLTextureParameters;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * The Class MTSceneMenu. A menu used in scenes to close the scene and/or pop back to another scene.
 * 
 * @author Christopher Ruff
 */
public class YPYISceneMenu extends MTRectangle{
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(YPYISceneMenu.class.getName());
	static{
//		logger.setLevel(Level.ERROR);
//		logger.setLevel(Level.WARN);
//		logger.setLevel(Level.DEBUG);
		logger.setLevel(Level.INFO);
		SimpleLayout l = new SimpleLayout();
		ConsoleAppender ca = new ConsoleAppender(l);
		logger.addAppender(ca);
	}
	
	/** The app. */
	private MTApplication app;
	
	/** The scene. */
	private Iscene scene;
	
	/** The overlay group. */
	private MTComponent overlayGroup;
	
	/** The scene texture. */
	private MTSceneTexture sceneTexture;
	
	/** The windowed scene. */
	private boolean windowedScene;
	
	/** The menu image. */
	private static PImage menuImage;
	
	/** The close button image. */
	private static PImage closeButtonImage;
	
	/** The restore button image. */
	private static PImage restoreButtonImage;
	
	private SlideYPYIScene slideYPYIScene;
	
	private int nroSlide;
	
	
	//TODO maybe add minimize mode -> dont show scene but dont destroy it -> maby keep it in a MTList
	
	/**
	 * Instantiates a new mT scene menu.
	 * 
	 * @param scene the scene
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param app the app
	 */
	public YPYISceneMenu(Iscene scene, float x, float y, float width, float height, MTApplication app, SlideYPYIScene slideYPYI, int slideNumber) {
		super(x, y, width, height, app);
		this.app = app;
		this.scene = scene;
		
		this.windowedScene = false;

		slideYPYIScene = slideYPYI;
		nroSlide = slideNumber;
		
		this.init(x, y, width, height);
	}
	
	/**
	 * Instantiates a new mT scene menu.
	 * 
	 * @param sceneTexture the scene texture
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param app the app
	 */
	public YPYISceneMenu(MTSceneTexture sceneTexture, float x, float y, float width, float height, MTApplication app) {
		super(x, y, width, height, app);
		this.app = app;
		this.scene = sceneTexture.getScene();
		this.sceneTexture = sceneTexture;
		
		this.windowedScene = true;
		
		this.init(x, y, width, height);
	}
	
	
	/**
	 * Inits the.
	 * 
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
	private void init(float x, float y, float width, float height){
//		this.setNoFill(true);
		this.setNoStroke(true);
//		this.setFillColor(new MTColor(255,100,100,100));
		this.setFillColor(new MTColor(255,255,255,150));
//		this.unregisterAllInputProcessors();
//		this.setPickable(false);
		
		overlayGroup = new MTOverlayContainer(app, "Window Menu Overlay Group");
		
		if (menuImage == null){
			menuImage = app.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() +
					"blackRoundSolidCorner64sh2.png");
					
		}
		
		if (MT4jSettings.getInstance().isOpenGlMode()){
			GLTextureParameters tp = new GLTextureParameters();
			tp.wrap_s = GL.GL_CLAMP;
			tp.wrap_t = GL.GL_CLAMP;
//			GLTexture glTex = new GLTexture(app, MT4jSettings.getInstance().getDefaultImagesPath()+
//					"blackRoundSolidCorner64sh2.png", tp);
			GLTexture glTex = new GLTexture(app, menuImage.width, menuImage.height, tp);
			glTex.putPixelsIntoTexture(menuImage);
			this.setTexture(glTex);
		}else{
			this.setTexture(menuImage);
		}
		
		AbstractShape menuShape = this;
		menuShape.unregisterAllInputProcessors();
		menuShape.removeAllGestureEventListeners(TapProcessor.class);
		menuShape.registerInputProcessor(new TapProcessor(app));
		
		float buttonWidth = 80;
		float buttonHeight = 80;
		final float buttonOpacity = 170;
		
		//CLOSE BUTTON
//		Vector3D a = new Vector3D(-width * 1.2f, height/2f);
		Vector3D a = new Vector3D(-width * 1.55f, 0);
		a.rotateZ(PApplet.radians(80));
		Vector3D b = new Vector3D(-width * 1.55f, 0);
		b.rotateZ(PApplet.radians(10));
		
//		final MTRectangle closeButton = new MTRectangle(x + a.x, y + a.y, buttonWidth, buttonHeight, app);
		final MTRectangle closeButton = new MTRectangle(x + b.x, y + b.y, buttonWidth, buttonHeight, app);
		
		if (closeButtonImage == null){
			closeButtonImage = app.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() +
//					"close_32.png"));
//					"126182-simple-black-square-icon-alphanumeric-circled-x3_cr.png"));
//					"124241-matte-white-square-icon-alphanumeric-circled-x3_cr.png");
					"closeButton64.png");
		}
		
		closeButton.setTexture(closeButtonImage);
		closeButton.setFillColor(new MTColor(255, 255, 255, buttonOpacity));
		closeButton.setNoStroke(true);
		closeButton.setVisible(false);
		closeButton.scale(-1, -1, 1, closeButton.getCenterPointLocal(), TransformSpace.LOCAL);
		this.addChild(closeButton);
		
		closeButton.unregisterAllInputProcessors();
		closeButton.removeAllGestureEventListeners(TapProcessor.class);
		closeButton.registerInputProcessor(new TapProcessor(app));
		closeButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					slideYPYIScene.exitPresentation(nroSlide);	
				}
				return false;
			}
		});
		
		
			final MTRectangle restoreButton = new MTRectangle(x + a.x, y + a.y, buttonWidth, buttonHeight, app);
			
			if (restoreButtonImage == null){
				restoreButtonImage = app.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() +
						"restoreButton64.png");
			}
			
			restoreButton.setTexture(restoreButtonImage);
			restoreButton.setFillColor(new MTColor(255, 255, 255, buttonOpacity));
			restoreButton.setNoStroke(true);
			restoreButton.setVisible(false);
			restoreButton.scale(-1, -1, 1, restoreButton.getCenterPointLocal(), TransformSpace.LOCAL);
			this.addChild(restoreButton);
			
			restoreButton.unregisterAllInputProcessors();
			restoreButton.removeAllGestureEventListeners(TapProcessor.class);
			restoreButton.registerInputProcessor(new TapProcessor(app));
			restoreButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()){
						slideYPYIScene.minimizeYPYI(nroSlide);
					}
					return false;
				}
			});
			
			menuShape.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()){
						restoreButton.setVisible(!restoreButton.isVisible());
						closeButton.setVisible(!closeButton.isVisible());
					}
					return false;
				}
			});		
		
		
	}
	
//	private void restoreSceneWindow(){
//		this.removeFromScene();
//	}
//	
//	private void closeSceneWindow(){
//		MTSceneWindow.this.destroy();
//	}
	
	/**
 * Highlight button.
 * 
 * @param shape the shape
 */
private void highlightButton(AbstractShape shape){
		MTColor c = shape.getFillColor();
		c.setAlpha(255);
		shape.setFillColor(c);
	}
	
	/**
	 * Unhighlight button.
	 * 
	 * @param shape the shape
	 * @param opacity the opacity
	 */
	private void unhighlightButton(AbstractShape shape, float opacity){
		MTColor c = shape.getFillColor();
		c.setAlpha(opacity);
		shape.setFillColor(c);
	}
	
	
	/**
	 * Adds the to scene.
	 */
	public void addToScene(){
		MTComponent cursorTraceContainer = null;
		MTCanvas canvas = scene.getCanvas();
		
		/*
		//Re-use cursor trace group which is always on top for this menu
		MTComponent[] children = canvas.getChildren();
		for (int i = 0; i < children.length; i++) {
			MTComponent component = children[i];
			if (component instanceof MTOverlayContainer 
					&&
				component.getName().equalsIgnoreCase("Cursor Trace group")){
				cursorTraceContainer  = component;
				component.addChild(0, this); //add to cursor trace overlay container
			}
		}
		*/
		
//		/*
		//cursor tracer group NOT found in the scene -> add overlay container to canvas
		if (cursorTraceContainer == null){ 
			overlayGroup.addChild(this);
			canvas.addChild(overlayGroup);
		}
//		*/
	}
	
	
	/**
	 * Removes the from scene.
	 */
	public void removeFromScene(){
		MTComponent cursorTraceContainer = null;
		MTCanvas canvas = scene.getCanvas();
		
		/*
		//Re-use cursor trace group which is always on top for this menu
		MTComponent[] children = canvas.getChildren();
		for (int i = 0; i < children.length; i++) {
			MTComponent component = children[i];
			if (component instanceof MTOverlayContainer 
					&&
				component.getName().equalsIgnoreCase("Cursor Trace group")){
				cursorTraceContainer  = component;
				if (cursorTraceContainer.containsChild(this)){
					cursorTraceContainer.removeChild(this);
				}
			}
		}
		*/
		
//		/*
		//cursor tracer group NOT found in the scene -> add overlay container to canvas
		if (cursorTraceContainer == null){ 
			if (canvas.containsChild(overlayGroup)){
				canvas.removeChild(overlayGroup);
			}
		}
//		*/
	}
	
	
}
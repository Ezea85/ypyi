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
package ar.edu.utn.ypyi.frontend.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTSceneMenu;
import org.mt4j.components.visibleComponents.widgets.MTSceneWindow;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.sceneManagement.transition.BlendTransition;
import org.mt4j.sceneManagement.transition.FadeTransition;
import org.mt4j.util.MTColor;
import org.mt4j.util.ir.IREventNotifier;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLFBO;
import org.uweschmidt.wiimote.whiteboard.YPYIWiimoteWhiteboard;

import processing.core.PApplet;
import processing.core.PImage;
import sun.misc.GC;
import ar.edu.utn.ypyi.frontend.calibrar.CalibrarScene2;
import ar.edu.utn.ypyi.frontend.drawing.MainDrawingScene;
import ar.edu.utn.ypyi.frontend.flickrMT.FlickrScene;
import ar.edu.utn.ypyi.frontend.slides.SlideYPYIScene;

/**
 * The Class YPYIShellScene. A scene which displays other scenes icons and loads them.
 * 
 * @author Christopher Ruff
 */
public class YPYIShellScene extends AbstractScene {
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(YPYIShellScene.class.getName());
	static{
//		logger.setLevel(Level.WARN);
//		logger.setLevel(Level.DEBUG);
		logger.setLevel(Level.INFO);
		SimpleLayout l = new SimpleLayout();
		ConsoleAppender ca = new ConsoleAppender(l);
		logger.addAppender(ca);
	}
	
	/** The app. */
	private MTApplication app;
	
	/** The has fbo. */
	private boolean hasFBO;
	
	/** The list. */
	private MTList list;
	
	/** The font. */
	private IFont font;
	
	/** The preferred icon height. */
	private int preferredIconHeight;
	
	/** The gap between icon and reflection. */
	private int gapBetweenIconAndReflection;
	
	/** The display height of reflection. */
	private float displayHeightOfReflection;
	
	/** The list width. */
	private float listWidth;
	
	/** The list height. */
	private int listHeight;
	
	/** The preferred icon width. */
	private int preferredIconWidth;
	
	/** The switch directly to scene. */
	private boolean switchDirectlyToScene = false;

	private boolean useBackgroundGradient = true;
	
	
	//TODO (dont allow throwing stuff out of the screen)
	//TODO loading screen
	
	/**
	 * Instantiates a new mT shell scene.
	 * 
	 * @param mtApplication the mt application
	 * @param name the name
	 */
	public YPYIShellScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.app = mtApplication;
		this.hasFBO = GLFBO.isSupported(app);
		//IF we have no FBO directly switch to scene and ignore setting
		this.switchDirectlyToScene = !this.hasFBO? true : switchDirectlyToScene; 
		
//		this.switchDirectlyToScene = true; 
		
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		this.setClearColor(new MTColor(20,20,20,255));
		
		
		
		//BACKGROUND IMAGE
		MTImage background = new MTImage(app.loadImage(StartYPYIShell.getPathToIconsYPYI()+ "candidato02.jpg"/*"background_flow.jpg"*/), app);
		
		
		background.setPickable(false); 
		background.setWidthXYGlobal(app.width+60);
		background.setHeightXYGlobal(app.height+60);
		background.setPositionGlobal(new Vector3D(640,400,0));
		
		
		getCanvas().addChild(background);
		
		PImage logoInteractivaImg = app.loadImage(StartYPYIShell.getPathToIconsYPYI()+ "logo_ypyi.png");
		
//		MTRectangle logoImg = new MTRectangle(logoInteractivaImg, app);
//		
//		logoImg.setPickable(false);
//		logoImg.setWidthXYGlobal(100);
//		logoImg.setHeightXYGlobal(100);
//		logoImg.setPositionGlobal(new Vector3D(300,80,0));
//		logoImg.setNoStroke(true);
//
//		getCanvas().addChild(logoImg);
		
		final MTImageButton logoButton = new MTImageButton(logoInteractivaImg, app);
//		logoButton.setPickable(false);
		logoButton.setWidthXYGlobal(100);
		logoButton.setHeightXYGlobal(100);
		logoButton.setPositionGlobal(new Vector3D(300,80,0));
		logoButton.setNoStroke(true);
		
		logoButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (arg0.getSource() instanceof MTComponent){
						//MTBaseComponent clickedComp = (MTBaseComponent)arg0.getSource();
						switch (arg0.getID()) {
						case TapEvent.BUTTON_CLICKED:
							
							Iscene scene = new AirHockeyScene(app, "AirHockey");
							
							MTSceneWindow sceneWindow = new MTSceneWindow(scene, 100,50, app, false);
							sceneWindow.setFillColor(new MTColor(50,50,50,200));
							sceneWindow.scaleGlobal(0.5f, 0.5f, 0.5f, sceneWindow.getCenterPointGlobal());
//							sceneWindow.addGestureListener(DragProcessor.class, new InertiaDragAction());
							getCanvas().addChild(sceneWindow);
							sceneWindow.maximize();
							
							break;
						default:
							break;
						}
					}
				}
			});
							
		getCanvas().addChild(logoButton);
		
		MTColor white = new MTColor(255,255,255);
		
		IFont fontArial40 = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
				40, 	//Font size
				white,  //Font fill color
				white);	//Font outline color
		//Create a textfield
		MTTextArea textField = new MTTextArea(mtApplication, fontArial40); 
		
		textField.setNoStroke(true);
		textField.setNoFill(true);
		textField.setPickable(false);
		
		textField.setText("Your Presentation Your Interaction");
		//Center the textfield on the screen
		textField.setPositionGlobal(new Vector3D(654, 86));
		//Add the textfield to our canvas
		this.getCanvas().addChild(textField);
		
		preferredIconWidth = 256;
		preferredIconHeight = 192;
		gapBetweenIconAndReflection = 10;//9
		displayHeightOfReflection = preferredIconHeight * 0.9f;
		
		//CREATE LIST
		listWidth = preferredIconHeight + displayHeightOfReflection + gapBetweenIconAndReflection;
		listHeight = app.width;
		list = new MTList(50,-20, listWidth, listHeight, 40, mtApplication);
		list.setFillColor(new MTColor(150,150,150,200));
		list.setNoFill(true);
		list.setNoStroke(true);
		
		//TODO does the font exist on all platforms? fallback to arial.ttf?
		font = FontManager.getInstance().createFont(app, "arial", 24, new MTColor(255,255,255), new MTColor(255,255,255));
		
		final YPYIWiimoteWhiteboard wii = new YPYIWiimoteWhiteboard();
		wii.inciar();
		
		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {

				return new CalibrarScene2(app, "Calibrar2");
			}
			public String getTitle() {
				return "Calibrar dispositivo";
			}
		}, app.loadImage(StartYPYIShell.getPathToIconsYPYI() + "sceneCalibrar.png"), false);
		
		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new SlideYPYIScene(app, "Presentacion");
			}
			public String getTitle() {
				return "Presentaciones";
			}
		}, app.loadImage(StartYPYIShell.getPathToIconsYPYI() + "scenePresentacion.png"), true);
		
		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new FlickrScene(app, "Flickr");
			}
			public String getTitle() {
				return "Fotos Interactivas";
			}
		}, app.loadImage(StartYPYIShell.getPathToIconsYPYI() + "sceneFotos.png"), false);
		
		if (this.hasFBO){
			this.addScene(new ICreateScene() {
				public Iscene getNewScene() {
					return new MainDrawingScene(app, "MT Paint");
				}
				public String getTitle() {
					return "Pizarra";
				}
			}, app.loadImage(StartYPYIShell.getPathToIconsYPYI() + "scenePizarra.png"), false);
		}
		
		getCanvas().addChild(list);
		list.rotateZ(list.getCenterPointLocal(), -90, TransformSpace.LOCAL);
		list.setPositionGlobal(new Vector3D(app.width/2f, app.height/2f));
		getCanvas().setFrustumCulling(true); 
		
		//Scene transition effect
		if (this.hasFBO){
			this.setTransition(new BlendTransition(app, 730));	
		}else{
			this.setTransition(new FadeTransition(app, 730));	
		}
	}
	
	/**
	 * Retorna el path en donde se encuentran los iconos.
	 * 
	 * @return the path to icons
	 */
//	private String getPathToIconsYPYI(){
//		return System.getProperty("user.dir")+File.separator+"src"+File.separator+"ar"+File.separator+"edu"
//											 +File.separator+"utn"+File.separator+"ypyi"+File.separator+"frontend"
//											 +File.separator+"menu"+ File.separator+"images"+File.separator;
//	}
	
	/*
	 * *
	 * Adds the tap processor.
	 * 
	 * @param cell the cell
	 * @param createScene the create scene
	 */
	private void addTapProcessor(MTListCell cell, final ICreateScene createScene, final boolean procesandoPresentacion){
		cell.registerInputProcessor(new TapProcessor(app, 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					//System.out.println("Clicked cell: " + te.getTargetComponent());
					final Iscene scene = createScene.getNewScene();
							
					if (!switchDirectlyToScene){//We have FBO support -> show scene in a window first
						
						if (hasFBO && scene instanceof AbstractScene){
							((AbstractScene) scene).setTransition(new BlendTransition(app, 300));	
						}
						
						final MTSceneWindow sceneWindow = new MTSceneWindow(scene, 100,50, app, procesandoPresentacion);
						sceneWindow.setFillColor(new MTColor(50,50,50,200));
						sceneWindow.scaleGlobal(0.5f, 0.5f, 0.5f, sceneWindow.getCenterPointGlobal());
//						sceneWindow.addGestureListener(DragProcessor.class, new InertiaDragAction());
						getCanvas().addChild(sceneWindow);
						sceneWindow.maximize();
						
						if(procesandoPresentacion){
							((SlideYPYIScene)scene).setmTSceneWindow(sceneWindow);
//							IREventNotifier.getInstance().setPptSceneWindow(sceneWindow);
						}
						
					}else{
						//No FBO available -> change to the new scene fullscreen directly
						
						float menuWidth = 64;
						float menuHeight = 64;
						MTSceneMenu sceneMenu = 
						//new MTSceneMenu(this, app.width-menuWidth/2f, 0-menuHeight/2f, menuWidth, menuHeight, app);
						new MTSceneMenu(scene, app.width-menuWidth, app.height-menuHeight, menuWidth, menuHeight, app);
						sceneMenu.addToScene();
						
						app.addScene(scene);
						app.pushScene();
						app.changeScene(scene);
					}
				}
				return false;
			}
		});
	}

	
	/**
	 * Adds the scene.
	 * 
	 * @param sceneToCreate the scene to create
	 * @param icon the icon
	 */
	public void addScene(ICreateScene sceneToCreate, PImage icon, boolean procesandoPresentacion){
//		System.out.println("Width: " + width + " Height:" + height);
		
		//Create reflection image
		PImage reflection = this.getReflection(getMTApplication(), icon);
		
		float border = 1;
		float bothBorders = 2*border;
		float topShift = 30;
		float reflectionDistanceFromImage = topShift + gapBetweenIconAndReflection; //Gap width between image and reflection
		
		float listCellWidth = listWidth;		
		float realListCellWidth = listCellWidth - bothBorders;
		float listCellHeight = preferredIconWidth ;
		
		MTListCell cell = new MTListCell(realListCellWidth ,  listCellHeight, app);
		cell.setNoFill(true);
		cell.setNoStroke(true);
		
//		/*
		Vertex[] vertices = new Vertex[]{
				new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
				new Vertex(realListCellWidth-topShift, 				 listCellHeight -border,	0, 1,0),
				new Vertex(realListCellWidth-topShift - icon.height, listCellHeight -border,	0, 1,1),
				new Vertex(realListCellWidth-topShift - icon.height,	border,		  		0, 0,1),
				new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
		};
		MTPolygon p = new MTPolygon(vertices, getMTApplication());
		p.setTexture(icon);
		p.setStrokeColor(new MTColor(80,80,80, 255));
		
		Vertex[] verticesRef = new Vertex[]{
				new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage, 				 					border,	0, 	0,0),
				new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage,						listCellHeight -border,	0, 	1,0),
				new Vertex(listCellWidth - icon.height - reflection.height - reflectionDistanceFromImage, 	listCellHeight -border,	0, 	1,1),
				new Vertex(listCellWidth - icon.height - reflection.height - reflectionDistanceFromImage,				border,	0, 	0,1),
				new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage, 									border,	0, 	0,0),
		};
		MTPolygon pRef = new MTPolygon(verticesRef, getMTApplication());
		pRef.setTexture(reflection);
		pRef.setNoStroke(true);
		
		cell.addChild(p);
		cell.addChild(pRef);
		
		list.addListElement(cell);
		addTapProcessor(cell, sceneToCreate, procesandoPresentacion);
		
		///Add scene title
		MTTextArea text = new MTTextArea(app, font);
		text.setFillColor(new MTColor(150,150,250,200));
		text.setNoFill(true);
		text.setNoStroke(true);
		text.setText(sceneToCreate.getTitle());
		text.rotateZ(text.getCenterPointLocal(), 90, TransformSpace.LOCAL);
		cell.addChild(text);
		
		text.setPositionRelativeToParent(cell.getCenterPointLocal());
		text.translate(new Vector3D(realListCellWidth*0.5f - text.getHeightXY(TransformSpace.LOCAL)*0.5f, 0));
		///
	}
	
	
	/**
	 * Creates the reflection image.
	 * 
	 * @param pa the pa
	 * @param image the image
	 * @return the reflection image
	 */
	private PImage getReflection(PApplet pa, PImage image) {
		int width =  image.width; 
		int height = image.height;
		
		PImage copyOfImage = pa.createImage(image.width, image.height, PApplet.ARGB);
		image.loadPixels();
		copyOfImage.loadPixels();
		   
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int imageIndex = y*image.width+x;
//				int currA = (image.pixels[imageIndex] >> 32) & 0xFF;
				int currR = (image.pixels[imageIndex] >> 16) & 0xFF;
			    int currG = (image.pixels[imageIndex] >> 8) & 0xFF;
			    int currB = image.pixels[imageIndex] & 0xFF;
			    
			    int col = image.pixels[imageIndex];
			    float alpha = pa.alpha(col);
			    
			    int reflectImageIndex = (image.height-y-1) * image.width+x;
			    
			    if (alpha <= 0.0f){
			    	copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , 0.0f); 
			    }else{
			    	copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , Math.round(y*y*y * (0.00003f) - 60)); //WORKS	
			    }
			}
		} 
		copyOfImage.updatePixels();
		return copyOfImage;
	}
	
	
	/* (non-Javadoc)
	 * @see org.mt4j.sceneManagement.AbstractScene#init()
	 */
	@Override
	public void init() {
		getMTApplication().registerKeyEvent(this);
	}

	/* (non-Javadoc)
	 * @see org.mt4j.sceneManagement.AbstractScene#shutDown()
	 */
	@Override
	public void shutDown() {
		getMTApplication().unregisterKeyEvent(this);
	}
	
	/**
	 * Key event.
	 * 
	 * @param e the e
	 */
	public void keyEvent(KeyEvent e){
		int evtID = e.getID();
		if (evtID != KeyEvent.KEY_PRESSED)
			return;
		switch (e.getKeyCode()){
		case KeyEvent.VK_F:
			System.out.println("FPS: " + getMTApplication().frameRate);
			break;
		case KeyEvent.VK_M:
			System.out.println("Max memory: " + Runtime.getRuntime().maxMemory() + " <-> Free memory: " + Runtime.getRuntime().freeMemory());
			break;	
		case KeyEvent.VK_C:
			getMTApplication().invokeLater(new Runnable() {
				public void run() {
					System.gc();
					GC.maxObjectInspectionAge();
					System.runFinalization();
				}
			});
			break;
		default:
			break;
		}
	}

}

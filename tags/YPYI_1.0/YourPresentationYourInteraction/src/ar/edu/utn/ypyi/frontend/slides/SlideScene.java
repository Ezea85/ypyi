package ar.edu.utn.ypyi.frontend.slides;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.hslf.model.TextShape;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTSceneMenu;
import org.mt4j.components.visibleComponents.widgets.MTSceneWindow;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.LassoProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.sceneManagement.transition.FadeTransition;
import org.mt4j.sceneManagement.transition.FlipTransition;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.ir.IREventNotifier;
import org.mt4j.util.math.Tools3D;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.opengl.GLFBO;

import ar.edu.utn.ypyi.frontend.menu.StartYPYIShell;

import processing.core.PImage;

public class SlideScene extends AbstractScene {

	private MTApplication mtApp;
	protected Iscene nextSceneInner;
	private YPYISceneMenu sceneMenu;
	private SlideYPYIScene slideYPYIScene;

	
	public SlideScene(MTApplication mtApplication, SlideYPYIScene scene,String name, DiapositivaVO diapositivaVO, boolean hasPrev, 
												   PImage arrow, int xOffset, int yOffset, int nroSlide) {
		super(mtApplication, name);
		this.mtApp = mtApplication;
		this.slideYPYIScene = scene;
		//Set the background color
		this.setClearColor(new MTColor(0, 0, 0, 0));
		
		this.registerGlobalInputProcessor(new CursorTracer(mtApp, this));
		
		SceneUtils.addBackgroundToScene(mtApp, this, diapositivaVO.getBackground(), xOffset, yOffset);

		//Agrego lo necesario para agregar fotos a la escena
		PImage abrirImg = mtApp.loadImage(StartYPYIShell.getPathToIconsYPYI()+ "iconoAbrir.png");
		
		final LassoProcessor lassoProcessor	= new LassoProcessor(mtApp, this.getCanvas(), this.getSceneCam());
		this.getCanvas().registerInputProcessor(lassoProcessor);
		
		
		final MTImageButton abrirButton = new MTImageButton(abrirImg, mtApp);
		abrirButton.setHeightLocal(120);
		abrirButton.setWidthLocal(110);
		abrirButton.setFillColor(new MTColor(255,255,255,200));
		abrirButton.setName("IconoAbrir");
		abrirButton.setNoStroke(true);
		abrirButton.translateGlobal(new Vector3D(130,mtApp.height-abrirButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		
		getCanvas().addChild(abrirButton);
		
		abrirButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae) {
			final List<MTImage> mtFotos = new ArrayList<MTImage>();
			
			if(ae.getID() == TapEvent.BUTTON_CLICKED) {
				System.out.println("Entro al filechooser.");
				Collection <String> urlImagenes = SceneUtils.fileChooserImagenes();
				System.out.println("Salgo del filechooser.");
				
				for(String imagenUrl:urlImagenes) {
					MTImage photo = new MTImage(mtApp.loadImage(imagenUrl), mtApp);
					photo.setName(imagenUrl);
					mtFotos.add(photo);
				}
								
				registerPreDrawAction(new IPreDrawAction(){
					public void processAction() {
						MTImage[] fotos = mtFotos.toArray(new MTImage[mtFotos.size()]);
						for (int i = 0; i < fotos.length; i++) {
							MTImage card = fotos[i];
							card.setUseDirectGL(true);
							card.setDisplayCloseButton(true);
							card.setPositionGlobal(new Vector3D(Tools3D.getRandom(10, MT4jSettings.getInstance().getScreenWidth()-100), Tools3D.getRandom(10, MT4jSettings.getInstance().getScreenHeight()-50),0 )  );
							card.scale(0.6f, 0.6f, 0.6f, card.getCenterPointLocal(), TransformSpace.LOCAL);
							card.addGestureListener(DragProcessor.class, new InertiaDragAction());
							lassoProcessor.addClusterable(card); //make fotos lasso-able
							getCanvas().addChild(card);
						}
					}
					
					public boolean isLoop() {
						return false;
					}
				});
				
			}
		}
		});
		//FIN Agregado de imagenes
		
		//Agrego los textos a la escena
		for(TextShape texto:diapositivaVO.getTextos()) {
			SceneUtils.addTextToScene(mtApp, this, texto, xOffset, yOffset);
		}
		
//		if(!hasPrev){
			//Agrego las imagenes a la escena
			for(ImagenVO imagenVO:diapositivaVO.getImagenes()) {
				SceneUtils.addPictureToScene(mtApplication, this,imagenVO, xOffset, yOffset);
			}		
			
			SceneUtils.addUniqueEditBar(mtApp, this);
			//Se encarga de retornar al menu en donde puedo seleccionar otra diapositiva
//			addHomeButton(nroSlide);
//		}
		
		if(hasPrev){
			
//			//Agrego las imagenes editables a la escena
//			for(ImagenVO imagenVO:diapositivaVO.getImagenes()) {
////				SceneUtils.addEditableImage(mtApplication, this,imagenVO, xOffset, yOffset);
//				SceneUtils.addPictureToScene(mtApplication, this,imagenVO, xOffset, yOffset);
//			}		
//			
//			SceneUtils.addUniqueEditBar(mtApp, this);
//			addHomeButton(nroSlide);
			
			MTImageButton previousSceneButton = new MTImageButton(arrow, mtApplication);
			previousSceneButton.setNoStroke(true);
			if (MT4jSettings.getInstance().isOpenGlMode())
				previousSceneButton.setUseDirectGL(true);
			previousSceneButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					switch (ae.getID()) {
					case TapEvent.BUTTON_CLICKED:
						mtApp.popScene();
						break;
					default:
						break;
					}
				}
			});
			getCanvas().addChild(previousSceneButton);
			previousSceneButton.scale(-2f, 2f, 1, previousSceneButton.getCenterPointLocal(), TransformSpace.LOCAL);
			previousSceneButton.setPositionGlobal(new Vector3D(previousSceneButton.getWidthXY(TransformSpace.GLOBAL) - 20, (mtApp.height/2) - previousSceneButton.getHeightXY(TransformSpace.GLOBAL) - 5, 0));
		}	
		
		
		addSceneMenu(slideYPYIScene, nroSlide);
		
		
		//Set a scene transition - Flip transition only available using opengl supporting the FBO extenstion
		if (MT4jSettings.getInstance().isOpenGlMode() && GLFBO.isSupported(mtApp))
			this.setTransition(new FlipTransition(mtApp, 700));
		else{
			this.setTransition(new FadeTransition(mtApp));
		}
	}	
	
	public void addSceneMenu(SlideYPYIScene slideYPYIScene, int nroSlide){
		if (sceneMenu == null){
			float menuWidth = 64;
			float menuHeight = 64;
//			this.sceneMenu = new MTSceneMenu(this, app.width-menuWidth/2f, 0-menuHeight/2f, menuWidth, menuHeight, app);
//			this.sceneMenu = new MTSceneMenu(this, app.width-menuWidth, 0, menuWidth, menuHeight, app);
			this.sceneMenu = new YPYISceneMenu(this, 0, 0, menuWidth, menuHeight, this.mtApp,slideYPYIScene, nroSlide);
//			this.sceneMenu = new MTSceneMenu(this, app.width-menuWidth, app.height-menuHeight, menuWidth, menuHeight, app);
			this.sceneMenu.setVisible(false);
			this.sceneMenu.scale(-1, -1, 1, this.sceneMenu.getCenterPointLocal(), TransformSpace.LOCAL);
		}
		
		this.sceneMenu.addToScene();
		
			this.sceneMenu.setVisible(true);
		
	}
	
	private void addHomeButton(int nroSlide) {
		
		PImage homeButtonImg = mtApp.loadImage(StartYPYIShell.getPathToIconsYPYI() + "homeButton.png");
		
		
		final int numeroDeSlide = nroSlide;
		
		final MTImageButton homeButton = new MTImageButton(homeButtonImg, mtApp);
		homeButton.setFillColor(new MTColor(255,255,255,200));
		homeButton.setName("KeyboardButton");
		homeButton.setNoStroke(true);
		homeButton.setSizeLocal(100, 100);
		homeButton.translateGlobal(new Vector3D(-2,mtApp.height - homeButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		this.getCanvas().addChild(homeButton);
		
		
		homeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
			
				if(ae.getID() == TapEvent.BUTTON_CLICKED) {
					
//					System.out.println("Listo todas las escenas que tengo en la pila...");
//					
//					Iscene[] escenas = mtApp.getScenes();
//					
//					for(int i=0;i<escenas.length;i++) {
//						System.out.println("Escena ["+i+"]: "+escenas[i].getName());
//					}
//					
//					System.out.println("Estoy en la escena numero: "+numeroDeSlide);
//					
//					System.out.println("Voy al menu principal");
//					for(int j=0; numeroDeSlide >=j;j++) {
//						
//						mtApp.popScene();
//					}
//					mtApp.pushScene();
//					mtApp.changeScene(mtApp.getScene("Presentacion"));
				
					slideYPYIScene.minimizeYPYI(numeroDeSlide);
					
//					exitScene();
				}
				
				
			}
		});
		
		
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
		nextSceneButton.scale(2f, 2f, 1, nextSceneButton.getCenterPointLocal(), TransformSpace.LOCAL);
		nextSceneButton.setPositionGlobal(new Vector3D(mtApp.width - nextSceneButton.getWidthXY(TransformSpace.GLOBAL) + 20, (mtApp.height/2) - nextSceneButton.getHeightXY(TransformSpace.GLOBAL) - 5, 0));

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

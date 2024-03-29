package ar.edu.utn.ypyi.frontend.flickrMT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.components.visibleComponents.widgets.progressBar.MTProgressBar;
import org.mt4j.input.IMTEventListener;
import org.mt4j.input.MTEvent;
import org.mt4j.input.gestureAction.DefaultLassoAction;
import org.mt4j.input.gestureAction.DefaultPanAction;
import org.mt4j.input.gestureAction.DefaultZoomAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.LassoProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.camera.MTCamera;
import org.mt4j.util.math.Tools3D;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import ar.edu.utn.ypyi.frontend.menu.StartYPYIShell;
import ar.edu.utn.ypyi.frontend.slides.SceneUtils;

import com.aetrion.flickr.photos.SearchParameters;

public class FlickrScene extends AbstractScene {
	private MTApplication app;
	private MTProgressBar progressBar;
	
	private MTComponent pictureLayer;
	private LassoProcessor lassoProcessor;
	
	public FlickrScene(MTApplication mtAppl, String name) {
		super(mtAppl, name);
		this.app = mtAppl;
		
		//Set a zoom limit
		final MTCamera camManager = new MTCamera(mtAppl);
		this.setSceneCam(camManager);
		this.getSceneCam().setZoomMinDistance(80);
//		this.setClearColor(new MTColor(135, 206, 250, 255));
		this.setClearColor(new MTColor(70, 70, 72, 255));
		
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtAppl, this));
		
		//Add multitouch gestures to the canvas background
		lassoProcessor	= new LassoProcessor(app, this.getCanvas(), this.getSceneCam());
		this.getCanvas().registerInputProcessor(lassoProcessor);
		this.getCanvas().addGestureListener(LassoProcessor.class, new DefaultLassoAction(app, this.getCanvas().getClusterManager(), this.getCanvas()));
		
		this.getCanvas().registerInputProcessor(new PanProcessorTwoFingers(app));
		this.getCanvas().addGestureListener(PanProcessorTwoFingers.class, new DefaultPanAction());

		this.getCanvas().registerInputProcessor(new ZoomProcessor(app));
		this.getCanvas().addGestureListener(ZoomProcessor.class, new DefaultZoomAction());
		
		pictureLayer = new MTComponent(app);
		
		MTComponent topLayer = new MTComponent(app, "top layer group", new MTCamera(app));
		
		MTImage background = new MTImage(app.loadImage(StartYPYIShell.getPathToIconsYPYI() + "background_flow.jpg"), app);
		background.setPickable(false);
		background.setWidthXYGlobal(app.width+120);
		background.setHeightXYGlobal(app.height+120);
		background.setPositionGlobal(new Vector3D(640,400,0));
		
		
		pictureLayer.addChild(background);
		
		
		PImage keyboardImg = app.loadImage(StartYPYIShell.getPathToIconsYPYI() + "keyb128.png");
		
		final MTImageButton keyboardButton = new MTImageButton(keyboardImg, app);
		keyboardButton.setFillColor(new MTColor(255,255,255,200));
		keyboardButton.setName("KeyboardButton");
		keyboardButton.setNoStroke(true);
		keyboardButton.translateGlobal(new Vector3D(-2,app.height-keyboardButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		topLayer.addChild(keyboardButton);

		progressBar = new MTProgressBar(app, app.loadFont(MT4jSettings.getInstance().getDefaultFontPath()+ "Ziggurat.vlw"));
		progressBar.setDepthBufferDisabled(true);
		progressBar.setVisible(false);
		topLayer.addChild(progressBar);
		
		keyboardButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:
				case TapEvent.BUTTON_UP:
					//Flickr Keyboard
			        final MTKeyboard keyb = new MTKeyboard(app);
			        keyb.setFillColor(new MTColor(30, 30, 30, 210));
			        keyb.setStrokeColor(new MTColor(0,0,0,255));
			        
			        final MTTextArea t = new MTTextArea(app, FontManager.getInstance().createFont(app, "arial.ttf", 50, 
			        		new MTColor(0,0,0,255), //Fill color 
							new MTColor(0,0,0,255))); //Stroke color
					t.setStrokeColor(new MTColor(0,0 , 0, 255));
					t.setFillColor(new MTColor(205,200,177, 255));
					t.unregisterAllInputProcessors();
					t.setEnableCaret(true);
					t.snapToKeyboard(keyb);
					keyb.addTextInputListener(t);
			        
			        //Flickr Button for the keyboard
//			        MTSvgButton flickrButton = new MTSvgButton(System.getProperty("user.dir")+File.separator+"examples"+File.separator+"advanced"+File.separator+File.separator+"flickrMT"+File.separator +"data"+File.separator
//							+ "Flickr_Logo.svg", app);
					
					PImage interactivaImg = app.loadImage(StartYPYIShell.getPathToIconsYPYI()+"logo_ypyi.png");
							
					final MTImageButton flickrButton = new MTImageButton(interactivaImg, app);
					
					
//			        MTSvgButton flickrButton = new MTSvgButton(System.getProperty("user.dir")+File.separator+"examples"+File.separator+"advanced"+File.separator+File.separator+"flickrMT"+File.separator +"data"+File.separator
//							+ "logo4.svg", app);
			        flickrButton.scale(0.95f, 0.78f, 1, new Vector3D(0,0,0), TransformSpace.LOCAL);
			        flickrButton.translate(new Vector3D(7, -1,0));
			        flickrButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
			        flickrButton.setNoStroke(true);
			        
			        //Add actionlistener to flickr button
			        flickrButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							if (arg0.getSource() instanceof MTComponent){
								//MTBaseComponent clickedComp = (MTBaseComponent)arg0.getSource();
								switch (arg0.getID()) {
								case TapEvent.BUTTON_CLICKED:
								case TapEvent.BUTTON_UP:
									//Get current search parameters
							        SearchParameters sp = new SearchParameters();
							        sp.setText(t.getText());
							        sp.setSort(SearchParameters.RELEVANCE);
							        
							        System.out.println("Flickr search for: \"" + t.getText() + "\"");
							        
							        //Load flickr api key from file
							        String flickrApiKey = "";
							        String flickrSecret = "";
							        Properties properties = new Properties();
								    try {
								        properties.load(new FileInputStream(StartYPYIShell.getPathToIconsYPYI()+ "FlickrApiKey.txt"));
								        
								        flickrApiKey = properties.getProperty("FlickrApiKey", " ");
								        flickrSecret = properties.getProperty("FlickrSecret", " ");
								    } catch (Exception e) {
								    	System.err.println("Error while loading Settings.txt file. Using defaults.");
								    }
							        
							        //Create flickr loader thread
							        final FlickrMTFotoLoader flickrLoader = new FlickrMTFotoLoader(app, flickrApiKey, flickrSecret, sp, 300);
							        flickrLoader.setFotoLoadCount(5);
							        //Define action when loader thread finished
							        flickrLoader.addProgressFinishedListener(new IMTEventListener(){
										public void processMTEvent(MTEvent mtEvent) {
											//Add the loaded fotos in the main drawing thread to
											//avoid threading problems
											registerPreDrawAction(new IPreDrawAction(){
												public void processAction() {
													MTImage[] fotos = flickrLoader.getMtFotos();
													for (int i = 0; i < fotos.length; i++) {
														MTImage card = fotos[i];
														card.setUseDirectGL(true);
														card.setDisplayCloseButton(true);
														card.setPositionGlobal(new Vector3D(Tools3D.getRandom(10, MT4jSettings.getInstance().getScreenWidth()-100), Tools3D.getRandom(10, MT4jSettings.getInstance().getScreenHeight()-50),0 )  );
														card.scale(0.6f, 0.6f, 0.6f, card.getCenterPointLocal(), TransformSpace.LOCAL);
														card.addGestureListener(DragProcessor.class, new InertiaDragAction());
														lassoProcessor.addClusterable(card); //make fotos lasso-able
														pictureLayer.addChild(card);
													}
													progressBar.setVisible(false);
												}
												
												public boolean isLoop() {
													return false;
												}
											});
										}
							        });
							        progressBar.setProgressInfoProvider(flickrLoader);
							        progressBar.setVisible(true);
							        //Run the thread
							        flickrLoader.start();
							        //Clear textarea
							        t.clear();
							        keyb.close();
									break;
								default:
									break;
								}
							}
						}
					});
					keyb.addChild(flickrButton);
//			        getCanvas().addChild(0, keyb);
					getCanvas().addChild(keyb);
					keyb.scale(1.8f, 1.8f, 1, keyb.getCenterPointLocal(), TransformSpace.LOCAL);
					keyb.setPositionGlobal(new Vector3D(app.width/2f, app.height/2f,0));
					break;
				default:
					break;
				}
			}
		});
		
		
		
		
		PImage abrirImg = app.loadImage(StartYPYIShell.getPathToIconsYPYI() + "iconoAbrir.png");
				
		final MTImageButton abrirButton = new MTImageButton(abrirImg, app);
		abrirButton.setHeightLocal(120);
		abrirButton.setWidthLocal(110);
		abrirButton.setFillColor(new MTColor(255,255,255,200));
		abrirButton.setName("IconoAbrir");
		abrirButton.setNoStroke(true);
		abrirButton.translateGlobal(new Vector3D(130,app.height-abrirButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		
		topLayer.addChild(abrirButton);
		
		abrirButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				final List<MTImage> mtFotos = new ArrayList<MTImage>();
				
				if(ae.getID() == TapEvent.BUTTON_CLICKED || ae.getID() == TapEvent.BUTTON_UP) {
					System.out.println("Entro al filechooser.");
					Collection <String> urlImagenes = SceneUtils.fileChooserImagenes();
					System.out.println("Salgo del filechooser.");
					
					for(String imagenUrl:urlImagenes) {
						MTImage photo = new MTImage(app.loadImage(imagenUrl), app);
						photo.setName(imagenUrl);
//						mtFotos[i] = photo;
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
								pictureLayer.addChild(card);
							}
							progressBar.setVisible(false);
						}
						
						public boolean isLoop() {
							return false;
						}
					});
					
				}
			}
		});
		
		
		SceneUtils.addUniqueEditBar(mtAppl, topLayer);
		
		this.getCanvas().addChild(pictureLayer);
		this.getCanvas().addChild(topLayer);
	}
	
	/**
	 * Retorna el path en donde se encuentran los iconos.
	 * 
	 * @return the path to icons
	 */
//	private String getPathToIconsYPYI(){
//		return System.getProperty("user.dir")+File.separator+"src"+File.separator+"ar"+File.separator+"edu"
//											 +File.separator+"utn"+File.separator+"ypyi"+File.separator+"frontend"
//											 +File.separator+"flickrMT"+ File.separator+"data"+File.separator;
//	}
	
	@Override
	public void init() {
		app.registerKeyEvent(this);
	}

	@Override
	public void shutDown() {
		app.unregisterKeyEvent(this);
	}
	
	/**
	 * 
	 * @param e
	 */
	public void keyEvent(KeyEvent e){
		int evtID = e.getID();
		if (evtID != KeyEvent.KEY_PRESSED)
			return;
		switch (e.getKeyCode()){
		case KeyEvent.VK_BACK_SPACE:
			app.popScene();
			break;
		case KeyEvent.VK_F1:
			this.setClearColor(new MTColor(100, 99, 99, 255));
			break;
		case KeyEvent.VK_F2:
			this.setClearColor(new MTColor(120, 119, 119, 255));
			break;
		case KeyEvent.VK_F3:
			this.setClearColor(new MTColor(130, 129, 129, 255));
			break;
		case KeyEvent.VK_F4:
			this.setClearColor(new MTColor(160, 159, 159, 255));
			break;
		case KeyEvent.VK_F5:
			this.setClearColor(new MTColor(180, 179, 179, 255));
			break;
		case KeyEvent.VK_F6:
			this.setClearColor(new MTColor(100, 100, 102, 255));
			break;
		case KeyEvent.VK_F7:
			this.setClearColor(new MTColor(70, 70, 72, 255));
			break;
		case KeyEvent.VK_F:
			System.out.println("FPS: " + app.frameRate);
			break;
			default:
				break;
		}
	}
	

}

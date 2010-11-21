package ar.edu.utn.ypyi.frontend.slides;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTSceneWindow;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.YPYIMaximizableScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class SlideYPYIScene extends AbstractScene implements YPYIMaximizableScene{
	
	private MTApplication app;
	private SlideScene[] slideScenes;
	private Integer minimizedSlide;
	private MTSceneWindow mTSceneWindow;

	public MTSceneWindow getmTSceneWindow() {
		return mTSceneWindow;
	}

	public void setmTSceneWindow(MTSceneWindow mTSceneWindow) {
		this.mTSceneWindow = mTSceneWindow;
	}

	public Integer getActiveSlideNumber() {
		return minimizedSlide;
	}

	public SlideYPYIScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		this.app = mtApplication;
		minimizedSlide = null;
		
		MTColor white = new MTColor(255,255,255);
		MTColor black = new MTColor(0,0,0);
		this.setClearColor(new MTColor(146, 150, 188, 255));
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		//Agrego el background
		MTImage background = new MTImage(app.loadImage(System.getProperty("user.dir")+File.separator+"src"+File.separator+"ar"
														+File.separator+"edu"+File.separator+"utn"+File.separator+"ypyi"
														+File.separator+"frontend"+File.separator+"slides"+ File.separator+"data"
														+File.separator+ "scenePresentacion.jpg"), app);
		
		
		background.setPickable(false);
		background.setWidthXYGlobal(1280);
		background.setHeightXYGlobal(1024);
		background.setPositionGlobal(new Vector3D(640,400,0));
		
		getCanvas().addChild(background);
		
		IFont fontArial = FontManager.getInstance().createFont(app, "arial.ttf", 
				50, 	//Font size
				white,  //Font fill color
				white);	//Font outline color
		//Create a textfield
		MTTextArea textField = new MTTextArea(app, fontArial); 
		
		textField.setNoStroke(false);
		textField.setNoFill(false);
		textField.setFillColor(black);
		
		textField.setText("Presentaciones Interactivas by Interactiva IT");
		//Center the textfield on the screen
		textField.setPositionGlobal(new Vector3D(app.width/2f, 90));
		//Add the textfield to our canvas
		this.getCanvas().addChild(textField);
		
		PImage abrirImg = app.loadImage(System.getProperty("user.dir")+File.separator+"src"+File.separator+"ar"+File.separator+"edu"
				 											+File.separator+"utn"+File.separator+"ypyi"+File.separator+"frontend"
				 											+File.separator+"flickrMT"+ File.separator+"data"+File.separator+ "iconoAbrir.png");
				
		final MTImageButton abrirButton = new MTImageButton(abrirImg, app);
		abrirButton.setHeightLocal(120);
		abrirButton.setWidthLocal(110);
		abrirButton.setFillColor(new MTColor(255,255,255,200));
		abrirButton.setName("IconoAbrir");
		abrirButton.setNoStroke(true);
		abrirButton.translateGlobal(new Vector3D(10,app.height-abrirButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		
		this.getCanvas().addChild(abrirButton);
		
		abrirButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				if(ae.getID() == TapEvent.BUTTON_CLICKED) {
					cargarYReproducirPresentacion();
				}
			}
		});
		
	}
	
	private void cargarYReproducirPresentacion() {

		String file = fileChooser();
		SlideShow ppt;

		try {
			ppt = new SlideShow(new HSLFSlideShow(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Dimension pgsize = ppt.getPageSize();

		int wOffset = 0;
		int yOffset = 0;

		if (app.width > pgsize.width)
			wOffset = (app.width - pgsize.width) / 2;

		if (app.height > pgsize.height)
			yOffset = (app.height - pgsize.height) / 2;

		// Button to return to the previous scene
		String path = getPath();
		PImage arrow = app.loadImage(path + "arrowRight.png");

		SlideScene slideScene = null;
		String slideName = null;
		DataExtractor dataExtractor = new DataExtractor();
		Slide[] slide = ppt.getSlides();
		slideScenes = new SlideScene[slide.length];
		for (int i = 0; i < slide.length; i++) {

			slideName = "Slide_" + i;

			if (i > 0) {

				try {
					slideScene = new SlideScene(app,this, slideName,dataExtractor.extraerObjetosDeDiapositiva(slide[i],	i), 
																						true, arrow, wOffset, yOffset,i);
				} catch (Exception e) {
					System.out.println("Se rompio todo al agregar una escena: " + e.getMessage());
				}
				slideScenes[i - 1].setNextScene(slideScene, arrow);
			} else {
				try {
					slideScene = new SlideScene(app,this, slideName,dataExtractor.extraerObjetosDeDiapositiva(slide[i],i), 
																						false, arrow, wOffset, yOffset,i);
				} catch (Exception e) {
					System.out.println("Se rompio todo al agregar una escena: " + e.getMessage());
				}
			}

			slideScenes[i] = slideScene;
			app.addScene(slideScene);
		}

		app.pushScene();
		app.changeScene(app.getScene("Slide_0")); // Muestro por pantalla la
													// primer escena de la
													// presentacion
	}
	
	public void exitPresentation(int nroSlide){
		
		for (int i = nroSlide ; i>0 ; i-- ){
//			slideScenes[i].exitScene();
			
			app.popSceneYPYI();
		}
		
		app.popScene();
		
		for(SlideScene slideScene : slideScenes){
			slideScene.destroy();
		}
		
		slideScenes = null;
		
//		app.changeScene(this);
		
	}
	
	public void minimizeYPYI(int nroSlide){
		
		if(mTSceneWindow != null){
			mTSceneWindow.switchTexture(printscreen());
		}
		
		minimizedSlide = nroSlide;
		
		for (int i = nroSlide ; i>=0 ; i-- ){
//			slideScenes[i].exitScene();
			
			app.popSceneYPYI();
		}
		
//		Iscene minim = app.getScene("Presentaciones");
//		app.changeScene(minim);
		
		app.popScene();
		
	}
	
	public void maximizeYPYI(){
		app.pushScene();
		
		app.pushSceneYPYI(this);
		
		int i;
		
		for (i = 0 ; i<minimizedSlide ; i++ ){
//			slideScenes[i].exitScene();
			
			app.pushSceneYPYI(slideScenes[i]);
		}

		app.changeScene(slideScenes[i]);
		
		minimizedSlide = null;
		
	}
	
	private String getPath(){
		return (System.getProperty("user.dir") + File.separator + "src"
		+ File.separator + "ar" + File.separator + "edu"
		+ File.separator + "utn" + File.separator + "ypyi"
		+ File.separator + "frontend" + File.separator + "slides"
		+ File.separator + "data" + File.separator);
	}
	
	private String printscreen(){
		try {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();
			Rectangle screenRect = new Rectangle(screenSize);
			// create screen shot
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(screenRect);
			
//			// save captured image to PNG file
//			ImageIO.write(image, "png", new File(getPath() + "printscreen.png"));
			
			int w = image.getWidth();
			int h = image.getHeight();

			BufferedImage image2 = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);

			Graphics2D g2d = image2.createGraphics();

			AffineTransform at = new AffineTransform();
			
			at.rotate(Math.toRadians(180), w/2.0, h/2.0);
			
			at.translate( w / 2.0, h / 2.0 );
			at.scale(-1, 1);
			
			at.translate( -w / 2.0, -h / 2.0 );

			
			
			g2d.drawRenderedImage(image,at);

			ImageIO.write(image2,"png",new File(getPath() + "printscreen.png"));

			
			
			
			return getPath() + "printscreen.png";
		} catch (Exception e) {
			System.out.println("Error al hacer el printscreen");
			e.printStackTrace();
		} 
		return null;
	}
	
	private String fileChooser() {
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File("."));

	    chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
	      public boolean accept(File f) {
	        return f.getName().toLowerCase().endsWith(".ppt")
	        	|| f.getName().toLowerCase().endsWith(".pps")
	            || f.isDirectory();
	      }

	      public String getDescription() {
	        return "Power Point Presentations";
	      }
	    });

	    int r = chooser.showOpenDialog(new JFrame());
	    if (r == JFileChooser.APPROVE_OPTION) {
	      String name = chooser.getSelectedFile().getAbsolutePath();
	      System.out.println(name);
	      return name;
	    }
	    
	    return null;
	  }
	
	@Override
	public void init() {}
	@Override
	public void shutDown() {}
}

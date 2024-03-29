package ar.edu.utn.ypyi.frontend.drawing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLFBO;

import processing.core.PImage;
import ar.edu.utn.ypyi.frontend.menu.StartYPYIShell;

public class MainDrawingScene extends AbstractScene {
	private MTApplication pa;
	private MTRectangle textureBrush;
	private MTEllipse pencilBrush;
	private DrawSurfaceScene drawingScene;

	public MainDrawingScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.pa = mtApplication;
		
		if (!(MT4jSettings.getInstance().isOpenGlMode() && GLFBO.isSupported(pa))){
			System.err.println("Drawing example can only be run in OpenGL mode on a gfx card supporting the GL_EXT_framebuffer_object extension!");
			return;
		}
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		//Create window frame
        MTRoundRectangle frame = new MTRoundRectangle(-50,-50, 0, pa.width+100, pa.height+100, 25,25, pa);
        frame.setSizeXYGlobal(pa.width-10, pa.height-10);
        frame.setPickable(false);
        this.getCanvas().addChild(frame);
        //Create the scene in which we actually draw
        drawingScene = new DrawSurfaceScene(pa, "DrawSurface Scene");
        drawingScene.setClear(false);
            
        //Create texture brush
        PImage brushImage = getMTApplication().loadImage(StartYPYIShell.getPathToIconsYPYI() + "brush1.png");
		textureBrush = new MTRectangle(brushImage, getMTApplication());
		textureBrush.setPickable(false);
		textureBrush.setNoFill(false);
		textureBrush.setNoStroke(true);
		textureBrush.setDrawSmooth(true);
		textureBrush.setFillColor(new MTColor(0,0,0));
		//Set texture brush as default
		drawingScene.setBrush(textureBrush);
		
		//Create pencil brush
		pencilBrush = new MTEllipse(pa, new Vector3D(brushImage.width/2f,brushImage.height/2f,0), brushImage.width/2f, brushImage.width/2f, 60);
		pencilBrush.setPickable(false);
		pencilBrush.setNoFill(false);
		pencilBrush.setNoStroke(false);
		pencilBrush.setDrawSmooth(true);
		pencilBrush.setStrokeColor(new MTColor(0, 0, 0, 255));
		pencilBrush.setFillColor(new MTColor(0, 0, 0, 255));
		
        //Create the frame/window that displays the drawing scene through a FBO
//        final MTSceneTexture sceneWindow = new MTSceneTexture(0,0, pa, drawingScene);
		//We have to create a fullscreen fbo in order to save the image uncompressed
		final MTSceneTexture sceneTexture = new MTSceneTexture(pa,0, 0, pa.width, pa.height, drawingScene);
        sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);
        sceneTexture.setStrokeColor(new MTColor(155,155,155));
        frame.addChild(sceneTexture);
        
        //Eraser button
        PImage eraser = pa.loadImage(StartYPYIShell.getPathToIconsYPYI() + "Kde_crystalsvg_eraser.png");
        float yButton = mtApplication.height - (eraser.height/3);
        MTImageButton b = new MTImageButton(eraser, pa);
        b.setNoStroke(true);
        b.translate(new Vector3D(350,yButton,0));
        b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:
				case TapEvent.BUTTON_UP:
//					//As we are messing with opengl here, we make sure it happens in the rendering thread
					pa.invokeLater(new Runnable() {
						public void run() {
							sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);						
						}
					});
					break;
				default:
					break;
				}
			}
        });
        frame.addChild(b);
        b.scale(2f, 2f, 1, b.getCenterPointLocal(), TransformSpace.LOCAL);
        
        //Pen brush selector button
        PImage penIcon = pa.loadImage(StartYPYIShell.getPathToIconsYPYI() + "pen.png");
        final MTImageButton penButton = new MTImageButton(penIcon, pa);
        frame.addChild(penButton);
        penButton.translate(new Vector3D(450f, yButton,0));
        penButton.setNoStroke(true);
        penButton.setStrokeColor(new MTColor(0,0,0));
        penButton.scale(2f, 2f, 1, penButton.getCenterPointLocal(), TransformSpace.LOCAL);
        
        //Texture brush selector button
        PImage brushIcon = pa.loadImage(StartYPYIShell.getPathToIconsYPYI() + "paintbrush.png");
        final MTImageButton brushButton = new MTImageButton(brushIcon, pa);
        frame.addChild(brushButton);
        brushButton.translate(new Vector3D(550f, yButton,0));
        brushButton.setNoStroke(true);
        brushButton.setStrokeColor(new MTColor(0,0,0));
        brushButton.scale(1.5f, 1.5f, 1, brushButton.getCenterPointLocal(), TransformSpace.LOCAL);
        brushButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:
				case TapEvent.BUTTON_UP:
					drawingScene.setBrush(textureBrush);
					brushButton.setNoStroke(false);
					penButton.setNoStroke(true);
					break;
				default:
					break;
				}
			}
        });
        
        penButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:
				case TapEvent.BUTTON_UP:
					drawingScene.setBrush(pencilBrush);
					penButton.setNoStroke(false);
					brushButton.setNoStroke(true);
					break;
				default:
					break;
				}
			}
        });
        
        /////////////////////////
        //ColorPicker and colorpicker button
        PImage colPick = pa.loadImage(StartYPYIShell.getPathToIconsYPYI() + "colorcircle.png");
//        final MTColorPicker colorWidget = new MTColorPicker(0, pa.height-colPick.height, colPick, pa);
        final MTColorPicker colorWidget = new MTColorPicker(0, 0, colPick, pa);
        colorWidget.translate(new Vector3D(575f, mtApplication.height - (eraser.height/1.5f) - colorWidget.getHeightXY(TransformSpace.GLOBAL),0));
        colorWidget.setStrokeColor(new MTColor(0,0,0));
        colorWidget.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				if (ge.getId()== MTGestureEvent.GESTURE_ENDED){
					if (colorWidget.isVisible()){
						colorWidget.setVisible(false);
					}
				}else{
					drawingScene.setBrushColor(colorWidget.getSelectedColor());
				}
				return false;
			}
		});
        frame.addChild(colorWidget);
        colorWidget.setVisible(false);
        
        PImage colPickIcon = pa.loadImage(StartYPYIShell.getPathToIconsYPYI() + "ColorPickerIcon.png");
        MTImageButton colPickButton = new MTImageButton(colPickIcon, pa);
        frame.addChild(colPickButton);
        colPickButton.translate(new Vector3D(650f, yButton,0));
        colPickButton.setNoStroke(true);
        colPickButton.scale(2f, 2f, 1, colPickButton.getCenterPointLocal(), TransformSpace.LOCAL);
        colPickButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:
				case TapEvent.BUTTON_UP:
					if (colorWidget.isVisible()){
						colorWidget.setVisible(false);
					}else{
						colorWidget.setVisible(true);
						colorWidget.sendToFront();
					}				
					break;
				default:
					break;
				}
			}
        });
        
        //Add a slider to set the brush width
        MTSlider slider = new MTSlider(0, 0, 400, 76, 0.05f, 2.0f, pa);
        slider.setValue(0/*1.0f*/);
        frame.addChild(slider);
//        slider.rotateZ(new Vector3D(), 90, TransformSpace.LOCAL);
        slider.translate(new Vector3D(750, mtApplication.height - (eraser.height/1.5f)));
        slider.setStrokeColor(new MTColor(0,0,0));
        slider.setFillColor(new MTColor(220,220,220));
        slider.getKnob().setFillColor(new MTColor(70,70,70));
        slider.getKnob().setStrokeColor(new MTColor(70,70,70));
        slider.addPropertyChangeListener("value", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent p) {
				drawingScene.setBrushScale((Float)p.getNewValue());
			}
		});
        //Add triangle in slider to indicate brush width
        MTPolygon p = new MTPolygon(new Vertex[]{
        		new Vertex(2 + slider.getKnob().getWidthXY(TransformSpace.LOCAL), slider.getHeightXY(TransformSpace.LOCAL)/2f, 0),
        		new Vertex(slider.getWidthXY(TransformSpace.LOCAL)-3, slider.getHeightXY(TransformSpace.LOCAL)/4f +2, 0),
        		new Vertex(slider.getWidthXY(TransformSpace.LOCAL)-1, slider.getHeightXY(TransformSpace.LOCAL)/2f, 0),
        		new Vertex(slider.getWidthXY(TransformSpace.LOCAL)-3, -slider.getHeightXY(TransformSpace.LOCAL)/4f -2 + slider.getHeightXY(TransformSpace.LOCAL), 0),
        		new Vertex(2, slider.getHeightXY(TransformSpace.LOCAL)/2f, 0),
        }, pa);
        p.setFillColor(new MTColor(150,150,150, 150));
        p.setStrokeColor(new MTColor(160,160,160, 190));
        p.unregisterAllInputProcessors();
        p.setPickable(false);
        slider.getOuterShape().addChild(p);
        slider.getKnob().sendToFront();
        
	}
	
	
	/**
	 * Retorna el path en donde se encuentran los iconos.
	 * 
	 * @return the path to icons
	 */
//	private String getPathToIconsYPYI(){
//		return System.getProperty("user.dir")+File.separator+"src"+File.separator+"ar"+File.separator+"edu"
//											 +File.separator+"utn"+File.separator+"ypyi"+File.separator+"frontend"
//											 +File.separator+"drawing"+ File.separator+"images"+File.separator;
//	}
	
	
	

	public void init() {	}
	public void shutDown() {	}
	
	@Override
	public boolean destroy() {
		boolean destroyed = super.destroy();
		if (destroyed){
			drawingScene.destroy(); //Destroy the scene manually since it isnt destroyed in the MTSceneTexture atm!
		}
		return destroyed;
	}
}

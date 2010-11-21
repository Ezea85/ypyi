package ar.edu.utn.ypyi.frontend.slides;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.poi.hslf.model.TextShape;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PImage;
import ar.edu.utn.ypyi.frontend.menu.StartYPYIShell;

public class SceneUtils {

	public static void addTextToScene(MTApplication mtApplication,AbstractScene slideScene,TextShape textShape, int xOffset, int yOffset) {
		
		System.out.println("Voy a agregar el texto: "+textShape.getText());
		float xNew = new Float(textShape.getAnchor().getCenterX()).floatValue();
		float yNew = new Float(textShape.getAnchor().getCenterX()).floatValue();
		float wNew = new Float(textShape.getAnchor().getWidth()).floatValue();
		float hNew = new Float(textShape.getAnchor().getHeight()).floatValue();
		
		Rectangle2D ubicacionTexto = textShape.getLogicalAnchor2D(); 
		float x = Double.valueOf(ubicacionTexto.getX()).floatValue();
		float y = Double.valueOf(ubicacionTexto.getY()).floatValue();
		
		System.out.println("Ubicacion Texto: "+ubicacionTexto.toString());
		
		MTColor white = new MTColor(255,255,255);
		IFont font = FontManager.getInstance().createFont(mtApplication,"arial.ttf", 24, white, white);
		
//		MTTextArea textField = new MTTextArea(mtApplication, font);
		MTTextArea textField = new MTTextArea(xNew + xOffset, yNew + yOffset, wNew, hNew, font, mtApplication); 
		textField.setNoFill(true);
		textField.setNoStroke(true);
		textField.setText(textShape.getText());
//		textField.setSizeLocal(x, y);
		
//		textField.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
//		textField.setPositionGlobal(new Vector3D(200+x, 100+y));
		textField.setPositionGlobal(new Vector3D(xNew + xOffset, yNew + yOffset));
//		textField.setNoStroke(true);
		
		slideScene.getCanvas().addChild(textField);
		
	}
	
	public static void addPictureToScene(MTApplication mtApplication,AbstractScene slideScene, ImagenVO imagenVO, int xOffset, int yOffset) {
		
		if(imagenVO == null || imagenVO.getDireccionFisicaImagen() == null)
			return;
		
		PImage pImage = mtApplication.loadImage(imagenVO.getDireccionFisicaImagen());
		
//		Rectangle2D ubicacionTexto = imagenVO.getImagen().getAnchor2D(); 
//
//		float x = Double.valueOf(ubicacionTexto.getX()).floatValue();
//		float y = Double.valueOf(ubicacionTexto.getY()).floatValue();
//				
//		MTRectangle rect = new MTRectangle(pImage, mtApplication);
////		rect.setHeightXYGlobal(Double.valueOf(ubicacionTexto.getHeight()).floatValue());
//		
////		rect.setHeightXYRelativeToParent(Double.valueOf(ubicacionTexto.getHeight()).floatValue());
//		rect.setHeightLocal(Double.valueOf(ubicacionTexto.getHeight()*1.2).floatValue());
////		rect.setSizeLocal(x, y);
////		rect.setWidthXYGlobal(Double.valueOf(ubicacionTexto.getWidth()).floatValue());
////		rect.setWidthXYRelativeToParent(Double.valueOf(ubicacionTexto.getWidth()).floatValue());
//		rect.setWidthLocal(Double.valueOf(ubicacionTexto.getWidth()*1.2).floatValue());
//		
//		slideScene.getCanvas().addChild(rect);
//		rect.setPositionGlobal(new Vector3D(200+x, 100+y));
//		rect.setAnchor(rect.getAnchor());
//		rect.setNoStroke(true);
		
		
		float x = new Float(imagenVO.getImagen().getAnchor().getCenterX()).floatValue();
		float y = new Float(imagenVO.getImagen().getAnchor().getCenterY()).floatValue();
		float w = new Float(imagenVO.getImagen().getAnchor().getWidth()).floatValue();
		float h = new Float(imagenVO.getImagen().getAnchor().getHeight()).floatValue();

		MTRectangle rect = new MTRectangle( x + xOffset,y + yOffset,0,w,h,mtApplication);
		rect.setTexture(pImage);
		rect.setPickable(imagenVO.isPickable());
		rect.setNoStroke(true);
		slideScene.getCanvas().addChild(rect);
		rect.setPositionGlobal(new Vector3D(x + xOffset,y + yOffset));
		
		
	}
	
	public static void addBackgroundToScene(MTApplication mtApplication,AbstractScene slideScene, ImagenVO background, int xOffset, int yOffset) {
		
		if(background != null ){
			float x = mtApplication.getWidth()/2;
			float y = mtApplication.getHeight()/2;
			float w = mtApplication.getWidth() - (xOffset * 2);
			float h = mtApplication.getHeight() - (yOffset * 2);;
			MTRectangle rect = new MTRectangle( x ,y ,0,w,h,mtApplication);
			
			if(background.getDireccionFisicaImagen() != null){
				rect.setTexture( mtApplication.loadImage(background.getDireccionFisicaImagen()));
			}else if(background.getColor()!=null){
				rect.setFillColor(new MTColor(background.getColor().getRed(), background.getColor().getGreen(), background.getColor().getBlue()));
			}else{
				return;
			}
			
			rect.setPickable(false);
			rect.setNoStroke(true);
			slideScene.getCanvas().addChild(rect);
			rect.setPositionGlobal(new Vector3D(x ,y ));
			
		}
		
		
//		background.getFill().getPictureData().getData();
//		BufferedImage img = new BufferedImage(new Double(imagenVO.getImagen().getAnchor().getWidth()).intValue(),new Double(imagenVO.getImagen().getAnchor().getHeight()).intValue(),imagenVO.getImagen().getPictureData().getType());
//		imagenVO.getImagen().draw(img.createGraphics());
//		img.flush();
		
//		try {
//			FileOutputStream out = new FileOutputStream(getPathToIconsYPYI() +  "back-aux.png");
//			out.write(background.getFill().getPictureData().getData());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		PImage pImage = mtApplication.loadImage(imagenVO.getDireccionFisicaImagen());
//		
//		Rectangle2D ubicacionTexto = imagenVO.getImagen().getAnchor2D(); 
//
//		
//		background.getSheet().get
//		
//		
//		float x = Double.valueOf(ubicacionTexto.getX()).floatValue();
//		float y = Double.valueOf(ubicacionTexto.getY()).floatValue();
//				
//		MTRectangle rect = new MTRectangle(pImage, mtApplication);
//		
//		rect.setHeightLocal(Double.valueOf(ubicacionTexto.getHeight()*1.2).floatValue());
//		rect.setWidthLocal(Double.valueOf(ubicacionTexto.getWidth()*1.2).floatValue());
//		
//		slideScene.getCanvas().addChild(rect);
//		rect.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		
	}
	
	public static void addUniqueEditBar(final MTApplication mtApplication,AbstractScene slideScene) {
		addUniqueEditBar(mtApplication, slideScene.getCanvas());
	}
	
	public static void addUniqueEditBar(final MTApplication mtApplication,MTComponent component) {
		
		System.out.println("******************** Entré al addUniqueEditBar ********************");
		
		final MTRectangle textureBrush;
		final MTEllipse pencilBrush;
		final DrawSurfaceScene drawingScene;

		String path = StartYPYIShell.getPathToIconsYPYI();
		
//		final MTRoundRectangle frame = new MTRoundRectangle(-50,-50, 0, mtApplication.width+100,mtApplication.height+100, 25,25, mtApplication);
		final MTRectangle frame = new MTRectangle(-50,-50, 0, mtApplication.width+100,mtApplication.height+100, mtApplication);
		frame.setSizeXYGlobal(mtApplication.width, mtApplication.height);
		frame.setNoFill(true);
		frame.sendToFront();
		frame.setPickable(false);
		component.addChild(frame);
		
		
		//Create the scene in which we actually draw
        drawingScene = new DrawSurfaceScene(mtApplication, "DrawSurface Scene");
        drawingScene.setClear(false);
        
        //Create texture brush
        PImage brushImage = mtApplication.loadImage(path + "brush1.png");
		textureBrush = new MTRectangle(brushImage, mtApplication);
		textureBrush.setPickable(false);
		textureBrush.setNoFill(false);
		textureBrush.setNoStroke(true);
		textureBrush.setDrawSmooth(true);
		textureBrush.setFillColor(new MTColor(0,0,0));
		//Set texture brush as default
		drawingScene.setBrush(textureBrush);
		
		//Create pencil brush
		pencilBrush = new MTEllipse(mtApplication, new Vector3D(brushImage.width/2f,brushImage.height/2f,0), brushImage.width/2f, brushImage.width/2f, 60);
		pencilBrush.setPickable(false);
		pencilBrush.setNoFill(false);
		pencilBrush.setNoStroke(false);
		pencilBrush.setDrawSmooth(true);
		pencilBrush.setStrokeColor(new MTColor(0, 0, 0, 255));
		pencilBrush.setFillColor(new MTColor(0, 0, 0, 255));
		
        //Create the frame/window that displays the drawing scene through a FBO
//        final MTSceneTexture sceneWindow = new MTSceneTexture(0,0, pa, drawingScene);
		//We have to create a fullscreen fbo in order to save the image uncompressed
		int w = new Float(frame.getWidthXY(TransformSpace.GLOBAL)).intValue();
		int h = new Float(frame.getHeightXY(TransformSpace.GLOBAL)).intValue();
		
		final MTSceneTexture sceneTexture = new MTSceneTexture(mtApplication,0, 0, w, h, drawingScene);
        sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);
        sceneTexture.setStrokeColor(new MTColor(155,155,155));
        frame.addChild(sceneTexture);
        
        
        
        //Eraser button
        PImage eraser = mtApplication.loadImage(path + "Kde_crystalsvg_eraser.png");
        final MTImageButton eraserButton = new MTImageButton(eraser, mtApplication);
        eraserButton.setNoStroke(true);
        float yButton = mtApplication.height - (eraser.height/3);
        eraserButton.translate(new Vector3D(480,yButton,0));
        eraserButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:{
//					//As we are messing with opengl here, we make sure it happens in the rendering thread
					mtApplication.invokeLater(new Runnable() {
						public void run() {
							sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);						
						}
					});
				}break;
				default:
					break;
				}
			}
        });
        frame.addChild(eraserButton);
        
        eraserButton.scale(2f, 2f, 1, eraserButton.getCenterPointLocal(), TransformSpace.LOCAL);
        
        //Pen brush selector button
        PImage penIcon = mtApplication.loadImage(path + "pen.png");
        final MTImageButton penButton = new MTImageButton(penIcon, mtApplication);
        frame.addChild(penButton);
        penButton.translate(new Vector3D(580f, yButton,0));
        penButton.setNoStroke(true);
        penButton.setStrokeColor(new MTColor(0,0,0));
        penButton.scale(2f, 2f, 1, penButton.getCenterPointLocal(), TransformSpace.LOCAL);
        
        //Texture brush selector button
        PImage brushIcon = mtApplication.loadImage(path + "paintbrush.png");
        final MTImageButton brushButton = new MTImageButton(brushIcon, mtApplication);
        frame.addChild(brushButton);
        brushButton.translate(new Vector3D(660f, yButton,0));
        brushButton.setStrokeColor(new MTColor(0,0,0));
        brushButton.setNoStroke(true);
        brushButton.scale(1.5f, 1.5f, 1, brushButton.getCenterPointLocal(), TransformSpace.LOCAL);
        brushButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:{
					drawingScene.setBrush(textureBrush);
					brushButton.setNoStroke(false);
					penButton.setNoStroke(true);
				}break;
				default:
					break;
				}
			}
        });
        
        penButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:{
					drawingScene.setBrush(pencilBrush);
					penButton.setNoStroke(false);
					brushButton.setNoStroke(true);
				}break;
				default:
					break;
				}
			}
        });
        
        /////////////////////////
        //ColorPicker and colorpicker button
        PImage colPick = mtApplication.loadImage(path + "colorcircle.png");
//        final MTColorPicker colorWidget = new MTColorPicker(0, pa.height-colPick.height, colPick, pa);
        final MTColorPicker colorWidget = new MTColorPicker(0, 0, colPick, mtApplication);
        colorWidget.translate(new Vector3D(705f, mtApplication.height - (eraser.height/1.5f) - colorWidget.getHeightXY(TransformSpace.GLOBAL),0));
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
        
        PImage colPickIcon = mtApplication.loadImage(path + "ColorPickerIcon.png");
        final MTImageButton colPickButton = new MTImageButton(colPickIcon, mtApplication);
        frame.addChild(colPickButton);
        colPickButton.translate(new Vector3D(780f, yButton,0));
        colPickButton.setNoStroke(true);
        colPickButton.scale(2f, 2f, 1, colPickButton.getCenterPointLocal(), TransformSpace.LOCAL);
        colPickButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:{
					if (colorWidget.isVisible()){
						colorWidget.setVisible(false);
					}else{
						colorWidget.setVisible(true);
						colorWidget.sendToFront();
					}				
				}break;
				default:
					break;
				}
			}
        });
        
        //Add a slider to set the brush width
        final MTSlider slider = new MTSlider(0, 0, 400, 76, 0.05f, 2.0f, mtApplication);
        
        
        slider.setValue(0);
        frame.addChild(slider);
//        slider.rotateZ(new Vector3D(), 90, TransformSpace.LOCAL);
        slider.translate(new Vector3D(880, mtApplication.height - (eraser.height/1.5f)));
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
        }, mtApplication);
        p.setFillColor(new MTColor(150,150,150, 150));
        p.setStrokeColor(new MTColor(160,160,160, 190));
        p.unregisterAllInputProcessors();
        p.setPickable(false);
        slider.getOuterShape().addChild(p);
        slider.getKnob().sendToFront();

        PImage editIcon = mtApplication.loadImage(path + "edit_icon.png");
        final MTImageButton editButton = new MTImageButton(editIcon, mtApplication);
        frame.addChild(editButton);
        editButton.translate(new Vector3D(360f, yButton ,0));
        editButton.setNoStroke(true);
        editButton.setStrokeColor(new MTColor(0,0,0));
        editButton.scale(2f, 2f, 1, editButton.getCenterPointLocal(), TransformSpace.LOCAL);
        
        PImage handIcon = mtApplication.loadImage(path + "hand2.png");
        final MTImageButton handButton = new MTImageButton(handIcon, mtApplication);
        frame.addChild(handButton);
        handButton.translate(new Vector3D(250f, yButton,0));
        handButton.setNoStroke(true);
        handButton.setStrokeColor(new MTColor(0,0,0));
        handButton.scale(2f, 2f, 1, handButton.getCenterPointLocal(), TransformSpace.LOCAL);
        
        penButton.setVisible(false);
		brushButton.setVisible(false);
		slider.setVisible(false);
		colPickButton.setVisible(false);
		eraserButton.setVisible(false);
		sceneTexture.setVisible(false);
		
        handButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:{
					handButton.setNoStroke(false);
					penButton.setVisible(false);
					brushButton.setVisible(false);
					slider.setVisible(false);
					colPickButton.setVisible(false);
					eraserButton.setVisible(false);
					
					sceneTexture.setVisible(false);
				}break;
				default:
					break;
				}
			}
        });
        
        editButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				switch (ae.getID()) {
				case TapEvent.BUTTON_CLICKED:{
					editButton.setNoStroke(false);
					penButton.setVisible(true);
					brushButton.setVisible(true);
					slider.setVisible(true);
					colPickButton.setVisible(true);
					eraserButton.setVisible(true);
					
					sceneTexture.setVisible(true);
//					sceneTexture.sendToFront();
					frame.sendToFront();
				}break;
				default:
					break;
				}
			}
        });
        
       
	}
	
	
	public static void addEditableImage(final MTApplication mtApplication,AbstractScene slideScene, ImagenVO imagenVO , int xOffset, int yOffset) {
		
		System.out.println("******************** Entré al addEditableImage ********************");
		
		final MTRectangle textureBrush;
		final MTEllipse pencilBrush;
		final DrawSurfaceScene drawingScene;
		
		String path = StartYPYIShell.getPathToIconsYPYI();
		
		PImage pImage = mtApplication.loadImage(imagenVO.getDireccionFisicaImagen());
		float x = new Float(imagenVO.getImagen().getAnchor().getCenterX()).floatValue();
		float y = new Float(imagenVO.getImagen().getAnchor().getCenterY()).floatValue();
		float w = new Float(imagenVO.getImagen().getAnchor().getWidth()).floatValue();
		float h = new Float(imagenVO.getImagen().getAnchor().getHeight()).floatValue();
		
		final MTRectangle frame = new MTRectangle(-50,-50, 0, mtApplication.width+100,mtApplication.height+100, mtApplication);
		frame.setSizeXYGlobal(w,h);
		frame.setPositionGlobal(new Vector3D(x + xOffset,y + yOffset));
		frame.setTexture(pImage);
//		frame.setPickable(imagenVO.isPickable());
		frame.setNoStroke(true);
		slideScene.getCanvas().addChild(frame);
		
		
		//Create the scene in which we actually draw
	      drawingScene = new DrawSurfaceScene(mtApplication, "DrawSurface Scene");
	      drawingScene.setClear(false);
	      
	      //Create texture brush
	      PImage brushImage = mtApplication.loadImage(path + "brush1.png");
			textureBrush = new MTRectangle(brushImage, mtApplication);
			textureBrush.setPickable(false);
			textureBrush.setNoFill(false);
			textureBrush.setNoStroke(true);
			textureBrush.setDrawSmooth(true);
			textureBrush.setFillColor(new MTColor(0,0,0));
			//Set texture brush as default
			drawingScene.setBrush(textureBrush);
			
			//Create pencil brush
			pencilBrush = new MTEllipse(mtApplication, new Vector3D(brushImage.width/2f,brushImage.height/2f,0), brushImage.width/2f, brushImage.width/2f, 60);
			pencilBrush.setPickable(false);
			pencilBrush.setNoFill(false);
			pencilBrush.setNoStroke(false);
			pencilBrush.setDrawSmooth(true);
			pencilBrush.setStrokeColor(new MTColor(0, 0, 0, 255));
			pencilBrush.setFillColor(new MTColor(0, 0, 0, 255));
			
	      //Create the frame/window that displays the drawing scene through a FBO
	//      final MTSceneTexture sceneWindow = new MTSceneTexture(0,0, pa, drawingScene);
			//We have to create a fullscreen fbo in order to save the image uncompressed
			int wi = new Float(frame.getWidthXY(TransformSpace.GLOBAL)).intValue();
			int hi = new Float(frame.getHeightXY(TransformSpace.GLOBAL)).intValue();
			
			final MTSceneTexture sceneTexture = new MTSceneTexture(mtApplication,0, 0, wi, hi, drawingScene);
	      sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);
	      sceneTexture.setStrokeColor(new MTColor(155,155,155));
	      frame.addChild(sceneTexture);
	      
	      //Eraser button
	      PImage eraser = mtApplication.loadImage(path + "Kde_crystalsvg_eraser.png");
	      final MTImageButton eraserButton = new MTImageButton(eraser, mtApplication);
	      eraserButton.setNoStroke(true);
	      eraserButton.translate(new Vector3D(-50,70,0));
	      eraserButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae) {
					switch (ae.getID()) {
					case TapEvent.BUTTON_CLICKED:{
	//					//As we are messing with opengl here, we make sure it happens in the rendering thread
						mtApplication.invokeLater(new Runnable() {
							public void run() {
								sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);						
							}
						});
					}break;
					default:
						break;
					}
				}
	      });
	      frame.addChild(eraserButton);
	      
	      //Pen brush selector button
	      PImage penIcon = mtApplication.loadImage(path + "pen.png");
	      final MTImageButton penButton = new MTImageButton(penIcon, mtApplication);
	      frame.addChild(penButton);
	      penButton.translate(new Vector3D(-50f, 120,0));
	      penButton.setNoStroke(true);
	      penButton.setStrokeColor(new MTColor(0,0,0));
	      
	      //Texture brush selector button
	      PImage brushIcon = mtApplication.loadImage(path + "paintbrush.png");
	      final MTImageButton brushButton = new MTImageButton(brushIcon, mtApplication);
	      frame.addChild(brushButton);
	      brushButton.translate(new Vector3D(-50f, 170,0));
	      brushButton.setStrokeColor(new MTColor(0,0,0));
	      brushButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae) {
					switch (ae.getID()) {
					case TapEvent.BUTTON_CLICKED:{
						drawingScene.setBrush(textureBrush);
						brushButton.setNoStroke(false);
						penButton.setNoStroke(true);
					}break;
					default:
						break;
					}
				}
	      });
	      
	      penButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae) {
					switch (ae.getID()) {
					case TapEvent.BUTTON_CLICKED:{
						drawingScene.setBrush(pencilBrush);
						penButton.setNoStroke(false);
						brushButton.setNoStroke(true);
					}break;
					default:
						break;
					}
				}
	      });
	      
	      /////////////////////////
	      //ColorPicker and colorpicker button
	      PImage colPick = mtApplication.loadImage(path + "colorcircle.png");
	//      final MTColorPicker colorWidget = new MTColorPicker(0, pa.height-colPick.height, colPick, pa);
	      final MTColorPicker colorWidget = new MTColorPicker(0, 0, colPick, mtApplication);
	      colorWidget.translate(new Vector3D(0f, 175,0));
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
	      
	      PImage colPickIcon = mtApplication.loadImage(path + "ColorPickerIcon.png");
	      final MTImageButton colPickButton = new MTImageButton(colPickIcon, mtApplication);
	      frame.addChild(colPickButton);
	      colPickButton.translate(new Vector3D(-50f, 235,0));
	      colPickButton.setNoStroke(true);
	      colPickButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae) {
					switch (ae.getID()) {
					case TapEvent.BUTTON_CLICKED:{
						if (colorWidget.isVisible()){
							colorWidget.setVisible(false);
						}else{
							colorWidget.setVisible(true);
							colorWidget.sendToFront();
						}				
					}break;
					default:
						break;
					}
				}
	      });
	      
	      //Add a slider to set the brush width
	      final MTSlider slider = new MTSlider(0, 0, 200, 38, 0.05f, 2.0f, mtApplication);
	      slider.setValue(1.0f);
	      frame.addChild(slider);
	      slider.rotateZ(new Vector3D(), 90, TransformSpace.LOCAL);
	      slider.translate(new Vector3D(-7, 325));
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
	      }, mtApplication);
	      p.setFillColor(new MTColor(150,150,150, 150));
	      p.setStrokeColor(new MTColor(160,160,160, 190));
	      p.unregisterAllInputProcessors();
	      p.setPickable(false);
	      slider.getOuterShape().addChild(p);
	      slider.getKnob().sendToFront();
	
	      PImage editIcon = mtApplication.loadImage(path + "edit_icon.jpg");
	      final MTImageButton editButton = new MTImageButton(editIcon, mtApplication);
	      frame.addChild(editButton);
	      editButton.translate(new Vector3D(-50f, 20,0));
	      editButton.setNoStroke(true);
	      editButton.setStrokeColor(new MTColor(0,0,0));
	      
	      PImage handIcon = mtApplication.loadImage(path + "hand2.png");
	      final MTImageButton handButton = new MTImageButton(handIcon, mtApplication);
	      frame.addChild(handButton);
	      handButton.translate(new Vector3D(-50f, -40,0));
	      handButton.setNoStroke(true);
	      handButton.setStrokeColor(new MTColor(0,0,0));
	      
	      
	      penButton.setVisible(false);
			brushButton.setVisible(false);
			slider.setVisible(false);
			colPickButton.setVisible(false);
			eraserButton.setVisible(false);
			sceneTexture.setVisible(false);
			
	      handButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae) {
					switch (ae.getID()) {
					case TapEvent.BUTTON_CLICKED:{
						handButton.setNoStroke(false);
						penButton.setVisible(false);
						brushButton.setVisible(false);
						slider.setVisible(false);
						colPickButton.setVisible(false);
						eraserButton.setVisible(false);
						
						sceneTexture.setVisible(false);
					}break;
					default:
						break;
					}
				}
	      });
	      
	      editButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae) {
					switch (ae.getID()) {
					case TapEvent.BUTTON_CLICKED:{
						editButton.setNoStroke(false);
						penButton.setVisible(true);
						brushButton.setVisible(true);
						slider.setVisible(true);
						colPickButton.setVisible(true);
						eraserButton.setVisible(true);
						
						sceneTexture.setVisible(true);
	//					sceneTexture.sendToFront();
//						frame.sendToFront();
					}break;
					default:
						break;
					}
				}
	      });
	      
     
	}
	
	public static Collection<String> fileChooserImagenes() {
	    
		JFileChooser chooser = new JFileChooser();
	    Collection <String> urlImagenes = new ArrayList<String>();
		
		chooser.setCurrentDirectory(new File("."));
	    chooser.setMultiSelectionEnabled(true);

	    chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
	      public boolean accept(File f) {
	        return f.getName().toLowerCase().endsWith(".jpg")
	        	|| f.getName().toLowerCase().endsWith(".png")
	        	|| f.getName().toLowerCase().endsWith(".bmp")
	            || f.isDirectory();
	      }

	      public String getDescription() {
	        return "Imagenes";
	      }
	    });

	    int r = chooser.showOpenDialog(new JFrame());
	    if (r == JFileChooser.APPROVE_OPTION) {
//	      String name = chooser.getSelectedFile().getAbsolutePath();
	      File[] imagenesSeleccionadas = chooser.getSelectedFiles();
	      
	      for(int i = 0; i<imagenesSeleccionadas.length;i++) {
	    	  System.out.println("direccionImagen["+i+"]: "+imagenesSeleccionadas[i].getAbsolutePath());
	    	  urlImagenes.add(imagenesSeleccionadas[i].getAbsolutePath());
	      }
	      
	      return urlImagenes;
	    }
	    
	    return urlImagenes;
	  }

	
	
	
//	private static String getPathToIconsYPYI(){
//		return System.getProperty("user.dir")+File.separator+"src"+File.separator+"ar"+File.separator+"edu"
//											 +File.separator+"utn"+File.separator+"ypyi"+File.separator+"frontend"
//											 +File.separator+"slides"+ File.separator+"data"+File.separator;
//	}
	
}

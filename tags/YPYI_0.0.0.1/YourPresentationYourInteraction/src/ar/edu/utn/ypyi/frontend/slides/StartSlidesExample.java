package ar.edu.utn.ypyi.frontend.slides;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.mt4j.MTApplication;

import processing.core.PImage;

public class StartSlidesExample extends MTApplication {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initialize();
	}
	
	@Override
	public void startUp() {
		
		String file = fileChooser();
		
		SlideShow ppt;
		try {
			FileInputStream is = new FileInputStream(file);
			ppt = new SlideShow(is);
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		Dimension pgsize = ppt.getPageSize();

		//Button to return to the previous scene
		PImage arrow = loadImage(System.getProperty("user.dir") + File.separator + "data" + File.separator +
		"arrowRight.png");
		
		SlideScene slideScene = null;
		String slideName = null;
		Slide[] slide = ppt.getSlides();
		SlideScene[] slideScenes = new SlideScene[slide.length];
		for (int i = 0; i < slide.length; i++) {

		    BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
		    Graphics2D graphics = img.createGraphics();
		    //clear the drawing area
		    graphics.setPaint(Color.white);
		    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

		    //render
		    slide[i].draw(graphics);
            
		    PImage p = new PImage(img);
		    slideName = "Slide_" + i;
		    
//		    if(i>0){
//		    	slideScene = new SlideScene(this, slideName,  p, true, arrow);
//		    	slideScenes[i-1].setNextScene(slideScene, arrow);
//		    }else{
//		    	slideScene = new SlideScene(this, slideName,  p, false, arrow);
//		    }
//		    
		    slideScenes[i] = slideScene;
		    addScene(slideScene);

		}
		
		
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

}

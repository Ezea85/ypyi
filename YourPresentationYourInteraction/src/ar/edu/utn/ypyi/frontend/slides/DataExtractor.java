package ar.edu.utn.ypyi.frontend.slides;

import java.awt.Color;
import java.io.FileOutputStream;

import org.apache.poi.hslf.model.Background;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextShape;
import org.apache.poi.hslf.usermodel.PictureData;

import ar.edu.utn.ypyi.frontend.menu.StartYPYIShell;

/**
 * Demonstrates how you can extract misc embedded data from a ppt file
 *
 * @author Yegor Kozlov
 */
public final class DataExtractor {
    
    public DiapositivaVO extraerObjetosDeDiapositiva(Slide slide,int i) throws Exception{
    	
    	DiapositivaVO diapositivaVO = new DiapositivaVO();
    	
        System.out.println("Slide["+i+"]: "+slide.toString());
        Shape[] shape = slide.getShapes();
        System.out.println("Titulo: "+slide.getTitle());
            
        extraerBackground(slide.getBackground(),diapositivaVO,i);
            
        for (int j = 0; j < shape.length; j++) {
        	
        	System.out.println("ShapeType["+j+"]: "+shape[j].getShapeType());
        	System.out.println("ShapeName["+j+"]: "+shape[j].getShapeName());
        	System.out.println("ShapeId["+j+"]: "+shape[j].getShapeId());
        	
//        	slide.getPlaceholder(j).getHorizontalAlignment();
//            slide.getPlaceholder(j).getVerticalAlignment();    
        	
        	if (shape[j] instanceof TextShape) {
        		TextShape text = (TextShape) shape[j];
        		System.out.println("GetText: "+text.getText());
        		
        		//Guardo el texto en DiapositivaVO
        		diapositivaVO.getTextos().add(text);
        	}
        		
        	if (shape[j] instanceof Picture) {
                   Picture p = (Picture) shape[j];
                   PictureData data = p.getPictureData();
                   String name = p.getPictureName();
                   int type = data.getType();
                   ImagenVO imagenVO = new ImagenVO();
                   String ext;
                                      
                   switch (type) {
                       case Picture.JPEG:
                           ext = ".jpg";
                           break;
                       case Picture.PNG:
                           ext = ".png";
                           break;
                       case Picture.WMF:
                           ext = ".wmf";
                           break;
                       case Picture.EMF:
                           ext = ".emf";
                           break;
                       case Picture.PICT:
                           ext = ".pict";
                           break;
                       case Picture.DIB:
                           ext = ".dib";
                           break;
                       default:
                           continue;
                    }
                    FileOutputStream out = new FileOutputStream(StartYPYIShell.getPathToAuxYPYI() + "pict-" +i + j + name + ext);
                    out.write(data.getData());
                    out.close();
                    
                    //genero imagenVO
                    imagenVO.setDireccionFisicaImagen(StartYPYIShell.getPathToAuxYPYI() + "pict-" +i + j + name + ext);
                    imagenVO.setImagen(p);
                    //guardo la direccion en DiapositivaVO
                    diapositivaVO.getImagenes().add(imagenVO);
                    
                }
        	}
    	
        return diapositivaVO;
    }
    
    private void extraerBackground(Background background, DiapositivaVO diapositivaVO, int i) throws Exception{
    	
    	ImagenVO imagenVO = new ImagenVO();
    	
    	if( background!=null
    			&& background.getFill()!=null
    			&& background.getFill().getPictureData()!=null 
    			&& background.getFill().getPictureData().getData()!=null ){
	    	
	        PictureData data = background.getFill().getPictureData();
	        int type = data.getType();
	        String ext = ".png";
	                           
	        switch (type) {
	            case Picture.JPEG:
	                ext = ".jpg";
	                break;
	            case Picture.PNG:
	                ext = ".png";
	                break;
	            case Picture.WMF:
	                ext = ".wmf";
	                break;
	            case Picture.EMF:
	                ext = ".emf";
	                break;
	            case Picture.PICT:
	                ext = ".pict";
	                break;
	            case Picture.DIB:
	                ext = ".dib";
	                break;
	         }
	         FileOutputStream out = new FileOutputStream(StartYPYIShell.getPathToAuxYPYI() + "background-" + i + ext);
	         out.write(data.getData());
	         out.close();
	         
	         //genero imagenVO
	         imagenVO.setDireccionFisicaImagen(StartYPYIShell.getPathToAuxYPYI() + "background-" + i + ext);
	         
	         //guardo la direccion en DiapositivaVO
	         diapositivaVO.setBackground(imagenVO);
	         
    	}else{
    		imagenVO.setColor(background.getFill().getBackgroundColor());
    		diapositivaVO.setBackground(imagenVO);
    	}
    	
    }
    
}
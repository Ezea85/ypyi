package ar.edu.utn.ypyi.frontend.slides;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.hslf.model.TextShape;

public class DiapositivaVO {
	
	private Collection<ImagenVO> imagenes = new ArrayList<ImagenVO>();
	private Collection<TextShape> textos = new ArrayList<TextShape>();
	private ImagenVO background;
	
	
	public Collection<ImagenVO> getImagenes() {
		return imagenes;
	}
	public void setImagenes(Collection<ImagenVO> imagenes) {
		this.imagenes = imagenes;
	}
	public Collection<TextShape> getTextos() {
		return textos;
	}
	public void setTextos(Collection<TextShape> textos) {
		this.textos = textos;
	}
	public ImagenVO getBackground() {
		return background;
	}
	public void setBackground(ImagenVO background) {
		this.background = background;
	}
	

	

}

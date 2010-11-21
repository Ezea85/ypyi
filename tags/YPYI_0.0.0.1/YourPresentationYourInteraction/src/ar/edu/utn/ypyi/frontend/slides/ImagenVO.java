package ar.edu.utn.ypyi.frontend.slides;

import java.awt.Color;

import org.apache.poi.hslf.model.Picture;

public class ImagenVO {
	
	private String direccionFisicaImagen;
	private Picture imagen;
	private boolean pickable = true;
	private Color color;
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public boolean isPickable() {
		return pickable;
	}
	public void setPickable(boolean pickable) {
		this.pickable = pickable;
	}
	public String getDireccionFisicaImagen() {
		return direccionFisicaImagen;
	}
	public void setDireccionFisicaImagen(String direccionFisicaImagen) {
		this.direccionFisicaImagen = direccionFisicaImagen;
	}
	public Picture getImagen() {
		return imagen;
	}
	public void setImagen(Picture imagen) {
		this.imagen = imagen;
	}
	
	

}

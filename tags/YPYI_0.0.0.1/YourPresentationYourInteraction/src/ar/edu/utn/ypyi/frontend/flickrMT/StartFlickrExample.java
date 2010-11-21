package ar.edu.utn.ypyi.frontend.flickrMT;

import org.mt4j.MTApplication;

import advanced.flickrMT.FlickrScene;


public class StartFlickrExample extends MTApplication{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static void main(String args[]){
		initialize();
	}
	
	  public void inicializar() {
          initialize();
      }
	
	@Override
	public void startUp(){
		this.addScene(new FlickrScene(this, "Flickr Scene"));
	}
	
}
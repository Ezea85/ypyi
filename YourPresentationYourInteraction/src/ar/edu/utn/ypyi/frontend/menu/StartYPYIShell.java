package ar.edu.utn.ypyi.frontend.menu;

import org.mt4j.MTApplication;
import org.mt4j.util.ir.IREventNotifier;

public class StartYPYIShell extends MTApplication {
	private static final long serialVersionUID = 1L;

	public static void main(String args[]){
		initialize();
	}
	
	@Override
	public void startUp(){
		this.addScene(new YPYIShellScene(this, "YPYI Shell Scene"));
		
		IREventNotifier.getInstance().addListener(this);
	}
	
}

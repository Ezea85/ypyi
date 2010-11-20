package ar.edu.utn.ypyi.frontend.calibrar;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.uweschmidt.wiimote.whiteboard.YPYIWiimoteWhiteboard;

public class CalibrarScene extends AbstractScene {

	public CalibrarScene(MTApplication mtApplication, String name,YPYIWiimoteWhiteboard wii) {
		super(mtApplication, name);
				
		wii.getCalibration().start(wii.getDh().getConnectedWiimotes());
		
//		MTColor white = new MTColor(255,255,255);
//		this.setClearColor(new MTColor(146, 150, 188, 255));
//		//Show touches
//		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
//		
//		IFont fontArial = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
//				50, 	//Font size
//				white,  //Font fill color
//				white);	//Font outline color
//		//Create a textfield
//		MTTextArea textField = new MTTextArea(mtApplication, fontArial); 
//		
//		textField.setNoStroke(true);
//		textField.setNoFill(true);
//		
//		textField.setText("Presentaciones Interactivas by Interactiva IT");
//		//Center the textfield on the screen
//		textField.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
//		//Add the textfield to our canvas
//		this.getCanvas().addChild(textField);
	}
	@Override
	public void init() {}
	@Override
	public void shutDown() {}
}

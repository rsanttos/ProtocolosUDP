package br.com.imd.protocolos.udp.multicast.camera.view;

import javafx.application.Application;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class Main extends Application {
	private WebCamApp telaWebCam;
	
	@Override
	public void start(Stage stage) {
		try {
			telaWebCam = new WebCamApp();
			telaWebCam.start(stage);			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

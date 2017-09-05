package br.com.imd.protocolos.udp.multicast.camera;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class Teste {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Webcam webcam = Webcam.getDefault();
		webcam.open();
		ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
	}

}

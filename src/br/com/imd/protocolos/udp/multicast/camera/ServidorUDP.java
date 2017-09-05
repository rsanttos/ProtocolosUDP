package br.com.imd.protocolos.udp.multicast.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class ServidorUDP {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		int porta = 3737;
		byte[] dadosEnvio;

		DatagramSocket socketServidor = new DatagramSocket(porta);
		System.out.println("Servidor UDP em execução na porta " + porta + "...");

		InetAddress enderecoGrupoClientes = InetAddress.getByName("239.1.1.1");
		int portaClientes = 4443;

		while (true) {
			Date data = new Date();
			Long milissegundos = Date.UTC(data.getYear(), data.getMonth(), data.getDate(), data.getHours(),
					data.getMinutes(), data.getSeconds());
			milissegundos = milissegundos / 1000;

			// dadosEnvio = String.valueOf(milissegundos).getBytes();

			File imagemGerada = new File("teste.txt");

			dadosEnvio = transformaArquivoEmByte(imagemGerada);

			System.out.println(dadosEnvio.length);
			
			DatagramPacket pacoteEnviado = new DatagramPacket(dadosEnvio, dadosEnvio.length, enderecoGrupoClientes,
					portaClientes);
			
			System.out.println(pacoteEnviado.getLength());

			socketServidor.send(pacoteEnviado);
			
			System.out.println(socketServidor.getSendBufferSize());
			System.out.println("Data: " + data + " enviada no formato: " + milissegundos);

			Thread.sleep(5000);
		}
	}

	public static byte[] transformaArquivoEmByte(File arquivo) {
		int len = (int) arquivo.length();
		byte[] sendBuf = new byte[len];
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(arquivo);
			inFile.read(sendBuf, 0, len);
			inFile.close();
		} catch (FileNotFoundException fnfex) {
			fnfex.printStackTrace();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
		return sendBuf;
	}

	public static File geraImagem() throws IOException {
		Webcam webcam = Webcam.getDefault();
		webcam.open();
		File imagemGerada = new File("imagem.png");
		ImageIO.write(webcam.getImage(), "PNG", imagemGerada);
		return imagemGerada;
	}
}

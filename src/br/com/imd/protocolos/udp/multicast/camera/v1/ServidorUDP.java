package br.com.imd.protocolos.udp.multicast.camera.v1;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

public class ServidorUDP {

	public static int LIMITE_TEORICO = 64000;

	public static void main(String[] args) throws Exception {

		try {
			int porta = 3737;

			byte[] dadosTotaisImagem;
			byte[] dadosParciais;

			DatagramSocket socketServidor = new DatagramSocket(porta);
			System.out.println("Servidor UDP em execução na porta " + porta + "...");

			InetAddress enderecoGrupoClientes = InetAddress.getByName("239.1.1.1");
			int portaClientes = 4443;

			while (true) {
				
				File imagemGerada = geraImagem();
				dadosTotaisImagem = transformaArquivoEmByte(imagemGerada);

				int tamanhoTotalPacote = dadosTotaisImagem.length;
				int qtdPedacos = tamanhoTotalPacote / LIMITE_TEORICO;

				// Preparando mensagem com a imagem
				int indiceDadosTotais = 0;
				for (int i = 0; i <= qtdPedacos; i++) {
					int j = 0;
					dadosParciais = new byte[LIMITE_TEORICO];

					while (j < LIMITE_TEORICO && indiceDadosTotais < tamanhoTotalPacote) {
						dadosParciais[j] = dadosTotaisImagem[indiceDadosTotais];
						indiceDadosTotais++;
						j++;
					}

					System.out.println(tamanhoTotalPacote - indiceDadosTotais);

					if (i == qtdPedacos) {
						dadosParciais[dadosParciais.length - 4] = 'F';
						dadosParciais[dadosParciais.length - 3] = '$';
						dadosParciais[dadosParciais.length - 2] = '&';
						dadosParciais[dadosParciais.length - 1] = '%';
						j = 1;
					}
					DatagramPacket pacoteComSegmento = new DatagramPacket(dadosParciais, dadosParciais.length,
							enderecoGrupoClientes, portaClientes);
					socketServidor.send(pacoteComSegmento);
				}

				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		webcam.setCustomViewSizes(new Dimension[] { WebcamResolution.HD720.getSize() });
		webcam.setViewSize(WebcamResolution.HD720.getSize());
		webcam.open();
		File imagemGerada = new File("imagem.png");
		ImageIO.write(webcam.getImage(), "PNG", imagemGerada);
		webcam.close();
		return imagemGerada;
	}
}

package br.com.imd.protocolos.udp.multicast.camera;

import java.awt.Dimension;
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
import com.github.sarxos.webcam.WebcamResolution;

public class ServidorUDP {
	@SuppressWarnings("deprecation")
	
	public static int LIMITE_TEORICO = 64000;
	
	public static void main(String[] args) throws Exception {
		int porta = 3737;
		
		// Primeiros dados a serem enviados 
		// Formato: TAMANHO_#_MILISSEGUNDOS
		byte[] dadosIniciaisMensagem;
		
		byte[] dadosTotaisImagem;
		byte[] dadosParciais;

		DatagramSocket socketServidor = new DatagramSocket(porta);
		System.out.println("Servidor UDP em execução na porta " + porta + "...");

		InetAddress enderecoGrupoClientes = InetAddress.getByName("239.1.1.1");
		int portaClientes = 4443;

		while (true) {
			Date data = new Date();
			Long milissegundos = Date.UTC(data.getYear(), data.getMonth(), data.getDate(), data.getHours(),
					data.getMinutes(), data.getSeconds());
			milissegundos = milissegundos / 1000;

			File imagemGerada = geraImagem();

			dadosTotaisImagem = transformaArquivoEmByte(imagemGerada);
			
			int tamanhoTotalPacote = dadosTotaisImagem.length;
			int qtdPedacos = tamanhoTotalPacote/LIMITE_TEORICO;
			
			String mensagemInicial = String.valueOf(tamanhoTotalPacote) + "_#_" + milissegundos;
			
			dadosIniciaisMensagem = mensagemInicial.getBytes();
			
			// Enviando mensagem inicial
			DatagramPacket pacoteEnviado = new DatagramPacket(dadosIniciaisMensagem, dadosIniciaisMensagem.length, enderecoGrupoClientes,
					portaClientes);	
			socketServidor.send(pacoteEnviado);
			System.out.println("Mensagem inicial: " + mensagemInicial);
			
			// Preparando mensagem com a imagem
			int indiceDadosTotais = 0;			
			for(int i = 0 ; i < qtdPedacos ; i++){
				int j = 0;
				dadosParciais = new byte[LIMITE_TEORICO];
				while(j < LIMITE_TEORICO && indiceDadosTotais < tamanhoTotalPacote){
					dadosParciais[j] = dadosTotaisImagem[indiceDadosTotais];
					indiceDadosTotais++;
					j++;
				}							
				pacoteEnviado = new DatagramPacket(dadosParciais, dadosParciais.length, enderecoGrupoClientes,
						portaClientes);	
				socketServidor.send(pacoteEnviado);
			}

			Thread.sleep(2000);
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
		webcam.setCustomViewSizes(new Dimension[]{WebcamResolution.HD720.getSize()});
		webcam.setViewSize(WebcamResolution.HD720.getSize());
		webcam.open();
		File imagemGerada = new File("imagem.png");
		ImageIO.write(webcam.getImage(), "PNG", imagemGerada);
		webcam.close();
		return imagemGerada;
	}
}

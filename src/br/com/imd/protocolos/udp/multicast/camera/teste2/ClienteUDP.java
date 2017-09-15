package br.com.imd.protocolos.udp.multicast.camera.teste2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ClienteUDP {

	static Scanner leitorTeclado = new Scanner(System.in);

	public static int LIMITE_TEORICO = 64000;

	public static void main(String[] args) throws Exception {
		int portaServidor = 4443;
		MulticastSocket socketGrupo = new MulticastSocket(portaServidor);

		InetAddress enderecoGrupo = InetAddress.getByName("239.1.1.1");

		socketGrupo.joinGroup(enderecoGrupo);

		byte[] dadosRecebidos = new byte[LIMITE_TEORICO];
		List<Byte> bytesTotaisImagem = new LinkedList<Byte>();

		DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
		
		MyFrame frame = new MyFrame();

		while (true) {
			socketGrupo.receive(pacoteRecebido);
			
			for(int i = 0 ; i < dadosRecebidos.length ; i++){
				bytesTotaisImagem.add((byte) dadosRecebidos[i]);
			}
			
			if (bytesTotaisImagem.get(bytesTotaisImagem.size() - 1) == 'F') {
				File imagemRecebida = geraImagem(converteArrayDinamicoEmEstatico(bytesTotaisImagem));
				bytesTotaisImagem = new LinkedList<Byte>();
				frame.atualizaImagem(imagemRecebida.getName());
				//apagaImagem(imagemRecebida);
			}		
		}
	}

	public static File geraImagem(byte[] dadosRecebidos) throws IOException {
		Random random = new Random();
		int nAleatorio = random.nextInt(10);
		File arquivoRecebido = new File("imagemRecebida" + nAleatorio + ".png");
		OutputStream os = new FileOutputStream(arquivoRecebido);
		os.write(dadosRecebidos);
		os.flush();
		System.out.println("Imagem recebida e gravada em disco.");
		os.close();
		return arquivoRecebido;
	}
	
	public static void apagaImagem(File imagem){
		imagem.delete();
		System.out.println("Imagem recebida, mostrada e apagada do disco.");
	}
	
	public static byte[] converteArrayDinamicoEmEstatico(List<Byte> arrayDinamico){
		int tamanhoArray = arrayDinamico.size();
		byte[] arrayEstatico = new byte[tamanhoArray];
		int i = 0;
		for(Byte b : arrayDinamico){
			arrayEstatico[i] = b;
			i++;
		}
		return arrayEstatico;
	}
}

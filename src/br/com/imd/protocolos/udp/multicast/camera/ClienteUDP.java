package br.com.imd.protocolos.udp.multicast.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class ClienteUDP {

	static Scanner leitorTeclado = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		int portaServidor = 4443;
		MulticastSocket socketGrupo = new MulticastSocket(portaServidor);

		InetAddress enderecoGrupo = InetAddress.getByName("239.1.1.1");

		socketGrupo.joinGroup(enderecoGrupo);

		byte[] dadosRecebidos = new byte[512];

		DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
		while (true) {
			socketGrupo.receive(pacoteRecebido);

			lerArquivoRecebido(dadosRecebidos);
		}

	}

	public static void lerArquivoRecebido(byte[] dadosRecebidos) throws IOException {
		File arquivoRecebido = new File("arquivorecebido.txt");
		OutputStream os = new FileOutputStream(arquivoRecebido);
		os.write(dadosRecebidos);
		os.flush();
		System.out.println("Write bytes to file.");
		os.close();
	}
}

package br.com.imd.protocolos.udp.multicast.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

			String mensagemResposta = new String(dadosRecebidos);
			System.out.println("Resposta do servidor: " + mensagemResposta);
		}

	}
}

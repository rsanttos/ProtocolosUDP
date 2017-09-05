package br.com.imd.protocolos.udp.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteUDP {

	static Scanner leitorTeclado = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		int porta = 3737;
		String mensagem = "";
		
		mensagem = "Manda a hora aí!";

		byte[] dadosEnviados = mensagem.getBytes();
		byte[] dadosRecebidos = new byte[512];

		InetAddress hostDestino = InetAddress.getByName("localhost");

		DatagramSocket socketCliente = new DatagramSocket();
		DatagramPacket pacoteEnviado = new DatagramPacket(dadosEnviados, dadosEnviados.length, hostDestino, porta);

		socketCliente.send(pacoteEnviado);

		DatagramPacket pacoteRespostaRecebida = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
		socketCliente.receive(pacoteRespostaRecebida); // mÃ©todo bloqueante
		String mensagemResposta = new String(dadosRecebidos);
		System.out.println("Resposta do servidor: " + mensagemResposta);

		socketCliente.close();
	}
}

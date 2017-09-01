package br.com.imd.protocolos.udp.echo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteUDP {

	static Scanner leitorTeclado = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		int porta = 7777;
		String mensagem = "";
		
		System.out.print("Digite a mensagem para enviar ao servidor: ");
		mensagem = leitorTeclado.nextLine();

		byte[] dadosEnviados = mensagem.getBytes();
		byte[] dadosRecebidos = new byte[512];

		InetAddress hostDestino = InetAddress.getByName("localhost");

		DatagramSocket socketCliente = new DatagramSocket();
		DatagramPacket pacoteEnviado = new DatagramPacket(dadosEnviados, dadosEnviados.length, hostDestino, porta);

		socketCliente.send(pacoteEnviado);

		DatagramPacket pacoteRespostaRecebida = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
		socketCliente.receive(pacoteRespostaRecebida); // método bloqueante
		String mensagemResposta = new String(dadosRecebidos);
		System.out.println("Resposta do servidor: " + mensagemResposta);

		socketCliente.close();
	}
}

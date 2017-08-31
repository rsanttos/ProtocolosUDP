package br.com.imd.protocolos.udp.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServidorUDP {
	public static void main(String[] args) throws Exception {
		int porta = 7777;
		byte[] dadosRecebidos = new byte[512];
		byte[] dadosResposta;
		
		DatagramSocket socketServidor = new DatagramSocket(porta);
		System.out.println("Servidor UDP em execução na porta " + porta + "...");

		DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
		socketServidor.receive(pacoteRecebido); // método bloqueante

		System.out.println(new String(dadosRecebidos));

		dadosResposta = new String(dadosRecebidos).getBytes();
		
		InetAddress hostDestinoResposta = pacoteRecebido.getAddress();
		int portaDestinoResposta = pacoteRecebido.getPort();
		
		DatagramPacket pacoteResposta = new DatagramPacket(dadosResposta, dadosResposta.length, hostDestinoResposta, portaDestinoResposta);
		socketServidor.send(pacoteResposta);

		socketServidor.close();
	}
}

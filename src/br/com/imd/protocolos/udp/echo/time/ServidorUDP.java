package br.com.imd.protocolos.udp.echo.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class ServidorUDP {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		int porta = 3737;
		byte[] dadosRecebidos = new byte[512];
		byte[] dadosResposta;
		
		DatagramSocket socketServidor = new DatagramSocket(porta);
		System.out.println("Servidor UDP em execução na porta " + porta + "...");

		DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
		socketServidor.receive(pacoteRecebido); // método bloqueante

		System.out.println("Cliente " + pacoteRecebido.getAddress() + " disse: " + new String(dadosRecebidos));

		Date data = new Date();
		Long milissegundos = Date.UTC(data.getYear(), data.getMonth(), data.getDate(), data.getHours(), data.getMinutes(), data.getSeconds());
		
		dadosResposta = String.valueOf(milissegundos).getBytes();
		
		InetAddress hostDestinoResposta = pacoteRecebido.getAddress();
		int portaDestinoResposta = pacoteRecebido.getPort();
		
		DatagramPacket pacoteResposta = new DatagramPacket(dadosResposta, dadosResposta.length, hostDestinoResposta, portaDestinoResposta);
		socketServidor.send(pacoteResposta);
		
		System.out.println("Data: " + data + " enviada no formato: " + milissegundos);

		socketServidor.close();
	}
}

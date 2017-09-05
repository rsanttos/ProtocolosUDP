package br.com.imd.protocolos.udp.multicast.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class ServidorUDP {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		int porta = 3737;
		byte[] dadosEnvio;

		DatagramSocket socketServidor = new DatagramSocket(porta);
		System.out.println("Servidor UDP em execu��o na porta " + porta + "...");

		InetAddress enderecoGrupoClientes = InetAddress.getByName("239.1.1.1");
		int portaClientes = 4443;

		while (true) {
			Date data = new Date();
			Long milissegundos = Date.UTC(data.getYear(), data.getMonth(), data.getDate(), data.getHours(),
					data.getMinutes(), data.getSeconds());			
			milissegundos = milissegundos/1000;
			
			dadosEnvio = String.valueOf(milissegundos).getBytes();
			DatagramPacket pacoteEnviado = new DatagramPacket(dadosEnvio, dadosEnvio.length, enderecoGrupoClientes,
					portaClientes);

			socketServidor.send(pacoteEnviado);
			System.out.println("Data: " + data + " enviada no formato: " + milissegundos);
			
			Thread.sleep(5000);
		}
	}
}

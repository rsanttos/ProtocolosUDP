package br.com.imd.protocolos.udp.multicast.camera.teste;

import java.io.ByteArrayOutputStream;
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

	public static int LIMITE_TEORICO = 64000;

	public static void main(String[] args) throws Exception {
		int portaServidor = 4443;
		MulticastSocket socketGrupo = new MulticastSocket(portaServidor);

		InetAddress enderecoGrupo = InetAddress.getByName("239.1.1.1");

		socketGrupo.joinGroup(enderecoGrupo);

		byte[] dadosRecebidos = new byte[LIMITE_TEORICO];
		byte[] dadosParciais = new byte[LIMITE_TEORICO];
		byte[] dadosTotaisImagem = null;
		String segmentoTotal = "";


		int tamanhoTotalPacote = 0;
		int qtdPedacos = 0;
		String milissegundos = "";

		while (true) {

			DatagramPacket pacoteMensagemInicial = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
			
			socketGrupo.receive(pacoteMensagemInicial);

			String mensagem = new String(dadosRecebidos);

			// Lendo mensagem inicial no formato
			// IMD0406%$TAMANHO_#_MILISSEGUNDOS
			if (isMensagemInicial(mensagem)) {
				System.out.println("--------------------------------------------------");
				System.out.println("Mensagem inicial recebida: " + mensagem);
				tamanhoTotalPacote = retornaTamanhoMensagemInicial(mensagem);
				dadosTotaisImagem = new byte[tamanhoTotalPacote];
				// qtdPedacos = tamanhoTotalPacote / LIMITE_TEORICO;
				qtdPedacos = 10;
				milissegundos = retornaMilissegundosMensagemInicial(mensagem);
				System.out.println("Tamanho total do pacote: " + tamanhoTotalPacote);
				System.out.println("Quantidade de segmentos do pacote: " + qtdPedacos);
				System.out.println("Hora atual: " + milissegundos);
				System.out.println("--------------------------------------------------");
			} else {
				if (dadosTotaisImagem != null) {
					String segmentoRecebido = mensagem;
					System.out.println("Segmento recebidos: " + new String(dadosRecebidos));
					segmentoTotal += " - " + segmentoRecebido;
					System.out.println("Segmento total: " + segmentoTotal);
				}
			}
		}
	}

	public static int retornaTamanhoMensagemInicial(String mensagemInicial) {
		String mensagemAux = mensagemInicial.substring(9, mensagemInicial.length());
		String tamanho = "";
		int indice = mensagemAux.indexOf("_#_");

		for (int i = 0; i < indice; i++) {
			tamanho += mensagemAux.charAt(i);
		}
		return Integer.valueOf(tamanho);
	}

	public static String retornaMilissegundosMensagemInicial(String mensagemInicial) {
		String mensagemAux = mensagemInicial.substring(9, mensagemInicial.length());
		String milissegundos = "";
		int indice = mensagemAux.indexOf("_#_");

		for (int i = indice + 3; i < mensagemAux.length(); i++) {
			milissegundos += mensagemAux.charAt(i);
		}
		return milissegundos;
	}

	public static boolean isMensagemInicial(String mensagem) {
		return mensagem.startsWith("IMD0406%$");
	}

	public static void lerArquivoRecebido(byte[] dadosRecebidos) throws IOException {
		File arquivoRecebido = new File("arquivorecebidodenovo.png");
		OutputStream os = new FileOutputStream(arquivoRecebido);
		os.write(dadosRecebidos);
		os.flush();
		System.out.println("Write bytes to file.");
		os.close();
	}
}

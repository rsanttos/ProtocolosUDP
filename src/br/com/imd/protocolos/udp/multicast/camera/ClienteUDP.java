package br.com.imd.protocolos.udp.multicast.camera;

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
		byte[] dadosTotaisImagem;
		ByteArrayOutputStream arrayAux = new ByteArrayOutputStream();

		DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);

		int tamanhoTotalPacote = 0;
		int qtdPedacos = 0;
		String milissegundos = "";
		
		while (true) {
			socketGrupo.receive(pacoteRecebido);
			String mensagem = new String(dadosRecebidos);
			
			// Lendo mensagem inicial no formato TAMANHO_#_MILISSEGUNDOS
			if(isMensagemInicial(mensagem)){
				tamanhoTotalPacote = retornaTamanhoMensagemInicial(mensagem);
				qtdPedacos = tamanhoTotalPacote / LIMITE_TEORICO;
				milissegundos = retornaMilissegundosMensagemInicial(mensagem);
			} else {
				arrayAux.write(dadosRecebidos);
				dadosTotaisImagem = arrayAux.toByteArray();
				
				if(dadosTotaisImagem.length >= tamanhoTotalPacote){
					lerArquivoRecebido(dadosTotaisImagem);
					arrayAux = new ByteArrayOutputStream();
					dadosTotaisImagem = null;
				}
			}
		}
	}
	
	public static int retornaTamanhoMensagemInicial(String mensagemInicial){
		String tamanho = "";
		int indice = mensagemInicial.indexOf("_#_");
		
		for(int i = 0 ; i < indice ; i++){
			tamanho += mensagemInicial.charAt(i);
		}
		System.out.println(tamanho);
		return Integer.valueOf(tamanho);
	}

	public static String retornaMilissegundosMensagemInicial(String mensagemInicial){
		String milissegundos = "";
		int indice = mensagemInicial.indexOf("_#_");
		
		for(int i = indice + 3 ; i < mensagemInicial.length() ; i++){
			milissegundos += mensagemInicial.charAt(i);
		}
		System.out.println(milissegundos);
		return milissegundos;
	}
	
	public static boolean isMensagemInicial(String mensagem){		
		return mensagem.contains("_#_");
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

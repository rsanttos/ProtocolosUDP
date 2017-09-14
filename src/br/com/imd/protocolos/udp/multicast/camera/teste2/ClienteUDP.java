package br.com.imd.protocolos.udp.multicast.camera.teste2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.ArrayUtils;

public class ClienteUDP {

	static Scanner leitorTeclado = new Scanner(System.in);
	
	public static int LIMITE_TEORICO = 64000;
	
	public static int TAMANHO_MAXIMO_IMAGEM = 2000000;

	public static void main(String[] args) throws Exception {
		int portaServidor = 4443;
		MulticastSocket socketGrupo = new MulticastSocket(portaServidor);

		InetAddress enderecoGrupo = InetAddress.getByName("239.1.1.1");

		socketGrupo.joinGroup(enderecoGrupo);

		byte[] dadosRecebidos = new byte[LIMITE_TEORICO];
		byte[] dadosParciais = null;
		byte[] dadosTotaisImagem = null;
		ByteArrayOutputStream arrayAux = new ByteArrayOutputStream();

		DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);

		int tamanhoTotalPacote = 0;
		int qtdPedacos = 0;
		String milissegundos = "";
		
		boolean isSegmentoFinal = false;
		
		while (true) {
			socketGrupo.receive(pacoteRecebido);
			
			String mensagem = new String(dadosRecebidos);
			
			if(dadosRecebidos[0] == 'F'){
				isSegmentoFinal = true;
			}

			dadosParciais =  ArrayUtils.addAll(dadosParciais, dadosRecebidos);
			
			if(isSegmentoFinal){
				dadosTotaisImagem = ArrayUtils.addAll(dadosTotaisImagem, dadosParciais);
				lerArquivoRecebido(dadosTotaisImagem);
				dadosRecebidos = new byte[LIMITE_TEORICO];
				//dadosTotaisImagem = new byte[TAMANHO_MAXIMO_IMAGEM];
			}
		}
	}
	
	public static int retornaTamanhoMensagemInicial(String mensagemInicial){
		mensagemInicial = mensagemInicial.substring(9, mensagemInicial.length());
		String tamanho = "";
		int indice = mensagemInicial.indexOf("_#_");
		
		for(int i = 0 ; i < indice ; i++){
			tamanho += mensagemInicial.charAt(i);
		}
		System.out.println(tamanho);
		return Integer.valueOf(tamanho);
	}

	public static String retornaMilissegundosMensagemInicial(String mensagemInicial){
		mensagemInicial = mensagemInicial.substring(9, mensagemInicial.length());
		String milissegundos = "";
		int indice = mensagemInicial.indexOf("_#_");
		
		for(int i = indice + 3 ; i < mensagemInicial.length() ; i++){
			milissegundos += mensagemInicial.charAt(i);
		}
		System.out.println(milissegundos);
		return milissegundos;
	}
	
	public static boolean isMensagemInicial(String mensagem){	
		return mensagem.startsWith("IMD0406%$");
	}

	public static void lerArquivoRecebido(byte[] dadosRecebidos) throws IOException {
		File arquivoRecebido = new File("arquivorecebidodenovo.png");
		OutputStream os = new FileOutputStream(arquivoRecebido);
		os.write(dadosRecebidos);
		os.flush();
		System.out.println("Imagem criada.");
		os.close();
	}
}

package br.com.imd.protocolos.udp.multicast.camera.v1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FramePrincipal extends JFrame {
	private Icon imagem;
	private JLabel labelImagem;
	
	public FramePrincipal() {
		super("Teste Imagem");

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1200, 800));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		imagem = new ImageIcon("webcam.png");
		labelImagem = new JLabel(imagem);
		initialize();

		pack();
		setVisible(true);
	}

	private void initialize() {
		add(labelImagem, BorderLayout.CENTER);
	}
	
	public void atualizaImagem(String nomeImagem) throws InterruptedException{
		imagem = new ImageIcon(nomeImagem);
		labelImagem.setIcon(imagem);
		labelImagem.updateUI();
	}
}

package br.com.franciscochaves.votacao.teste;

import br.com.franciscochaves.votacao.socket.client.ClienteSocket;

public class TesteCliente {

	public static void main(String[] args) {

		//String host = JOptionPane.showInputDialog("Endereço do servidor:");
		new ClienteSocket("127.0.0.1", 8000).executar();
	}

}

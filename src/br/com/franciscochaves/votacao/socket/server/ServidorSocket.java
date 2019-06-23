package br.com.franciscochaves.votacao.socket.server;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import br.com.franciscochaves.votacao.Candidato;
import br.com.franciscochaves.votacao.Eleitor;
import br.com.franciscochaves.votacao.Voto;

public class ServidorSocket {

	private ServerSocket servidor;
	private Socket cliente;
	private int porta;
	private int quantidadeDeVotoMaxima = 5;

	private List<Candidato> candidatos;
	private List<Voto> votos = new ArrayList<Voto>();

	public ServidorSocket(int porta) {
		this.porta = porta;
	}

	public void executar(List<Candidato> candidatos) {

		this.candidatos = candidatos;

		try {
			servidor = new ServerSocket(porta);
			System.out.println("Servidor iniciado, funcionando na porta " + porta);

			while (true) {

				cliente = servidor.accept();
				new Thread(new TratamentoServidor(cliente)).start();

			}

		} catch (Exception e) {
			System.err.println("Erro ao executar o server: " + e.getMessage());
		}
	}

	private class TratamentoServidor implements Runnable {

		private Socket socket;
		private Scanner leitor;
		private PrintStream escritor;

		public TratamentoServidor(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {

				configurarRede(socket);

				String eleitorId = socket.getInetAddress().getHostAddress().toString();

				System.out.println("Nova conexão com o cliente: " + eleitorId);

				int quantidadeVotoCliente = contarVotoEleitor(votos, eleitorId);

				if (quantidadeVotoCliente == quantidadeDeVotoMaxima) {

					escritor.println("Atingiu o Limite de votos!!!");

				} else {

					exibirCandidatos(candidatos, escritor);

					while (leitor.hasNext()) {

						String conteudo = leitor.nextLine();
						
						if(conteudo.equals("exit")) {
							break;
						}

						Candidato candidato;
						if ((candidato = buscarCandidatos(candidatos, conteudo)) != null) {

							// TODO Lógica de tratamento e salvar

							// Criar o voto
							Voto voto = new Voto();
							voto.setEleito(new Eleitor(eleitorId));
							voto.setCandidato(candidato);

							// Salvar na lista de votos
							votos.add(voto);

							if (contarVotoEleitor(votos, eleitorId) >= quantidadeDeVotoMaxima) {
								contabilizarVoto(votos, candidatos, escritor);
								escritor.println("Atingiu o Limite de votos!!!");
								fechaSocket(socket, leitor, escritor);
							} else {
								// Retorna a lista de candidatos
								escritor.println("Voto foi registrado com sucesso");
								contabilizarVoto(votos, candidatos, escritor);
							}

						} else {
							escritor.println("Erro: candidato não cadastrado");
							exibirCandidatos(candidatos, escritor);
						}

					}
				}

				fechaSocket(socket, leitor, escritor);

			} catch (Exception e) {

				System.out.println("Exception " + e);

			} finally {

				System.err.println("\nConexão finalizada: " + socket.getRemoteSocketAddress().toString() + " "
						+ new Date().toString());

			}

		}

		private void contabilizarVoto(List<Voto> votos, List<Candidato> candidatos, PrintStream escritor) {

			for (Candidato c : candidatos) {
				
				int cont = 0;
				
				for (int i = 0; i < votos.size(); i++) {
					if (votos.get(i).getCandidato().getNome().equals(c.getNome())) {
						cont++;
					}
				}
				escritor.println(c.getNome() + " - " +  c.getNumero());
				escritor.println("quantidade de votos: "+ cont);
			}

		}

		private int contarVotoEleitor(List<Voto> votos, String eleitorId) {
			int contVotos = 0;

			for (Voto voto : votos) {
				if (voto.getEleito().getId().equals(eleitorId)) {
					contVotos++;
				}
			}
			return contVotos;
		}

		private void configurarRede(Socket socket) {
			try {
				// entrada de dados do socket server <<==
				this.leitor = new Scanner(socket.getInputStream());
				// saída de dados do socket server ==>>
				this.escritor = new PrintStream(socket.getOutputStream());
			} catch (Exception e) {
				System.out.println("Erro ao configura a rede: " + e.getMessage());
			}

		}

		private void exibirCandidatos(List<Candidato> candidatos, PrintStream escritor) {
			escritor.println("******************************");
			escritor.println("****** Seja bem-vindo *******");
			escritor.println("******************************");
			escritor.println("***** Lista de candidatos ****");

			for (int i = 0; i < candidatos.size(); i++) {
				escritor.println(" -> " + candidatos.get(i).toString());
			}

			escritor.println("******************************");
		}

		private Candidato buscarCandidatos(List<Candidato> candidatos, String numeroCandidato) {
			try {
				for (int i = 0; i < candidatos.size(); i++) {
					if (candidatos.get(i).getNumero() == Integer.parseInt(numeroCandidato)) {
						return candidatos.get(i);
					}
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}

		private void fechaSocket(Socket socket, Scanner leitor, PrintStream escritor) {
			try {
				leitor.close();
				escritor.close();
				socket.shutdownInput();
				socket.shutdownOutput();
				socket.close();
			} catch (Exception e) {
				System.out.println("Erro ao fechar o socket servidor: " + e.getMessage());
			}
		}

	}

}

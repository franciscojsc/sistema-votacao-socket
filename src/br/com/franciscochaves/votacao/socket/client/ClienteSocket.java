
	package br.com.franciscochaves.votacao.socket.client;

	import java.io.PrintStream;
	import java.net.Socket;
	import java.util.Scanner;

	import javax.swing.JOptionPane;

	public class ClienteSocket {

		private Socket socket;
		private String host;
		private int porta;

		
		boolean continuarEscritor = true;

		private Scanner leitor;
		private PrintStream escritor;

		public ClienteSocket(String ip, int porta) {
			this.host = ip;
			this.porta = porta;
		}

		public void executar() {
			try {
				// Criar o Socket
				socket = new Socket(host, porta);

				// saída de dados do socket client ==>>
				escritor = new PrintStream(socket.getOutputStream());

				// entrada de dados do socket client <<==
				leitor = new Scanner(socket.getInputStream());

				// Ficará escutando o servidor
				new Thread(new EscutaServidor()).start();

				// Ficará escrevendo no servidor
				new Thread(new EscreveServidor()).start();
			} catch (Exception e) {
				System.out.println("Erro ao configura a rede: " + e.getMessage());
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
				//System.out.println("Erro ao fechar o socket servidor: " + e.getMessage());
				System.out.println("Conexão encerrada");
			}

		}

		private class EscutaServidor implements Runnable {

			@Override
			public void run() {
				try {
					String texto;
					while ((texto = leitor.nextLine()) != null) {
						System.out.println(texto);
					}
				} catch (Exception e) {
					continuarEscritor = false;
					fechaSocket(socket, leitor, escritor);
					System.exit(0);
				}

			}

		}

		private class EscreveServidor implements Runnable {

			@Override
			public void run() {
				String texto = "";
				while (continuarEscritor) {
					texto = JOptionPane.showInputDialog("Entre com o número do candidato:");
					
					if(texto == null) {
						break;
					}
					
					escritor.println(texto);
				}
				
				//Enviar um mensagem para encerrar a conexão
				escritor.println("exit");
				
				fechaSocket(socket, leitor, escritor);
				System.exit(0);
			}

		}

	}

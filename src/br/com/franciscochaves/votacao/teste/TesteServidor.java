package br.com.franciscochaves.votacao.teste;

import java.util.ArrayList;
import java.util.List;

import br.com.franciscochaves.votacao.Candidato;
import br.com.franciscochaves.votacao.socket.server.ServidorSocket;

public class TesteServidor {
	
	public static void main(String[] args) {
		
		List<Candidato> candidatos = new ArrayList<Candidato>();
		
		candidatos.add(new Candidato("Zé", 1234));
		candidatos.add(new Candidato("Francisco", 4321));
		candidatos.add(new Candidato("Maria", 1111));
		candidatos.add(new Candidato("Carla", 3333));
		candidatos.add(new Candidato("Caio", 7777));
		candidatos.add(new Candidato("Lula", 8888));
		candidatos.add(new Candidato("Sicrano", 9090));
		candidatos.add(new Candidato("Fulano", 1900));
		candidatos.add(new Candidato("Bosolnaro", 2222));
		candidatos.add(new Candidato("Haddad", 8181));

		new ServidorSocket(8000).executar(candidatos);
		
	}
	
}

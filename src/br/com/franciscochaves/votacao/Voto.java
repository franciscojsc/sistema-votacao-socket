package br.com.franciscochaves.votacao;

public class Voto {

	private Candidato candidato;
	private Eleitor eleito;
	
	public Voto() {}
	
	public Voto(Eleitor eleito, Candidato candidato) {
		this.eleito = eleito;
		this.candidato = candidato;
	}
	
	public Candidato getCandidato() {
		return candidato;
	}

	public void setCandidato(Candidato candidato) {
		this.candidato = candidato;
	}

	public Eleitor getEleito() {
		return eleito;
	}

	public void setEleito(Eleitor eleito) {
		this.eleito = eleito;
	}

}


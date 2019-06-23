package br.com.franciscochaves.votacao;

public class Eleitor {
	
	private String id;
	
	
	public Eleitor(String indentificador) {
		this.id = indentificador;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
}

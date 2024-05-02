package pokemontextgame;

// TODO: Como de praxe, teremos que tornar abstrata mais tarde como status. Ver "Status.java"
public class Item {
	/*
	 * Armazena os itens possíveis de um pokemon carregar.
	 * Itens possuem variados efeitos.
	 * TODO: Armazenar itens e sprites possíveis numa pasta com png + json?
	 * TODO: Como fazer para cada item ter seu método exclusivo?
	 */
	private int id;
	private String nome;
	private String desc;
	
	// Apenas Getters e Setters adiante
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}

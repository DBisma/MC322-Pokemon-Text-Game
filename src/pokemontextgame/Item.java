package pokemontextgame;

// TODO: Como de praxe, teremos que tornar abstrata mais tarde como status. Ver "Status.java"
public class Item {
	/*
	 * Armazena os itens possíveis de um pokemon carregar.
	 * Itens possuem variados efeitos.
	 * TODO: Armazenar itens e sprites possíveis numa pasta com png + json?
	 * E agora, o elefante na sala:
	 * TODO: Como fazer para cada item ter seu método exclusivo?
	 */
	private int id; // https://bulbapedia.bulbagarden.net/wiki/List_of_items_by_index_number_(Generation_VIII)
	private String nome;
	private String desc;
	
	public Item(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}
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

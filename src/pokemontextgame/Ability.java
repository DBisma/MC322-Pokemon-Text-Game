package pokemontextgame;

//TODO: Como de praxe, teremos que tornar abstrata mais tarde como será feito com Status. Ver "Status.java"
public class Ability {
	/*
	 * Armazena as habilidades passivas do pokemon.
	 * TODO: Como fazer cada uma ter um método próprio?
	 * TODO: De certa forma, habilidades não são tão diferentes de held items.
	 * Cada pokemon pode ter apenas um por vez, como itens, e temos checks
	 * para verificarmos se ativam ou não, e cada um tem seu método próprio e efeito prório...
	 * talvez possamos unir os dois dentro de uma superclasse "Passivos Condicionais"?
	 */
	private int id; // https://bulbapedia.bulbagarden.net/wiki/Ability
	private String nome;
	private String desc;
	private boolean isActive;
	
	/*
	 * TODO: Habilidade deve ser uma classe abstrata.
	 * As habilidades reais são subclasses dessa,
	 * cada qual com um método CHECK e um método EFFECT
	 * exclusivos por habilidade.
	 */
	
	public Ability(int id, String nome) {
		this.id = id;
		this.nome = nome;
		boolean isActive = false;
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
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	
}

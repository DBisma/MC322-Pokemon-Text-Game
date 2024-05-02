package pokemontextgame;

//TODO: Como de praxe, teremos que tornar abstrata mais tarde como será feito com Status. Ver "Status.java"
public class Ability {
	/*
	 * Armazena as habilidades passivas do pokemon.
	 */
	private int id;
	private String nome;
	private String desc;
	private boolean isActive;
	
	/*
	 * TODO: Habilidade deve ser uma classe abstrata.
	 * As habilidades reais são subclasses dessa,
	 * cada qual com um método CHECK e um método EFFECT
	 * exclusivos por habilidade.
	 */
	
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

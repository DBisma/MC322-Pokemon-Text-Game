package pokemontextgame;

public class Treinador {
	/*
	 * Contém as informações do treinador.
	 */
	
	private int id;
	private String nome;
	private String desc;
	private boolean isPlayer;
	private Poke[] team; // time do treinador. max 6 pokemons
	int activeMon; // índice (0-5) do pokemon ativo. pode não ser usado no futuro.
	
	// TODO: INICIALIZAÇÃO
	
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
	public boolean isPlayer() {
		return isPlayer;
	}
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	public Poke[] getTeam() {
		return team;
	}
	public void setTeam(Poke[] team) { // provavelmente será desnecessário no futuro
		this.team = team;
	}
	
}

package pokemontextgame;

abstract class Move {
	/*
	 * Armazena as informações base dos ataques.
	 */
	private int id;
	private String nome;
	private String desc;
	private int tipagem;
	private int power; // dano base do ataque. pode ser 0.
	private int maxPoints; // com quantos "usos" o ataque começa;
	private int points; // quantos "usos" restam ao ataque;
	private int accuracy;
	private int priority;
	private boolean statusFx; // se o ataque possui algum efeito extra fora o dano;
	private int categ; // Se é físico (0), especial (1) ou status (2)
	// TODO: Como armazenar o método / efeito especial de cada ataque?
	// TODO: MOVE SERÁ UMA CLASSE ABSTRATA. OS MOVES EM SI SERÃO INSANIDADES DE CLASSE ÚNICA
	
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
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getMaxPoints() {
		return maxPoints;
	}
	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public boolean isFx() {
		return statusFx;
	}
	public void setFx(boolean fx) {
		this.statusFx = fx;
	}
	public int getTipagem() {
		return tipagem;
	}
	public void setTipagem(int tipagem) {
		this.tipagem = tipagem;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getCateg() {
		return categ;
	}
	public void setCateg(int categ) {
		this.categ = categ;
	}
}

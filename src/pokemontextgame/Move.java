package pokemontextgame;

//TODO: Como de praxe, teremos que tornar abstrata mais tarde como será feito com Status. Ver "Status.java"
public class Move { 
	/*
	 * Armazena as informações base dos ataques.
	 * 
	 */
	private int id; // https://bulbapedia.bulbagarden.net/wiki/List_of_moves
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
	
	// Construtor provisório; mais tarde, TODO Construir de json
	public Move(int id, String name, int type, int pwr, int maxP, int pri, int accu, int categ){
		this.id = id;
		this.nome = name;
		this.tipagem = type;
		this.power = pwr;
		this.maxPoints = maxP;
		this.points = maxP; // sempre é construído com o max
		this.priority = pri;
		this.accuracy = accu;
		this.categ = categ;
	}
	
	@Override
	
	public String toString() {
		/*
		 * Converte informações de Move numa grande string.
		 * TODO: Preparar um toString no caso de Move de categ. Stat e não SpecAtk ou Atk.
		 * TODO: Talvez para isso seja necessário dividir cada uma dessas categs em subclasses, na verdade.
		 */
		String newdesc;
		String categAndPower;
		String output;
		
		if(this.desc == null)
			newdesc = "Nenhuma descrição disponível.";
		else
			newdesc = "Descrição: " + desc;
		
		categAndPower = "Categoria: " + Move.categToString(this.categ);
		if(this.categ != 2) // adiciona Power se houver
			categAndPower += "\n" + "Dano base: " + this.power;
			
		output = "Move: " + "'"+ this.nome + "'\n"
				+ "Tipo: " + TypeChart.typeToString(this.tipagem) + "\n"
				+ categAndPower + "\n"
				+ "PP: " + this.points + " / " + this.maxPoints + "\n"
				+ "Prioridade: " + this.priority + "\n"
				+ "Precisão: " + this.accuracy + "\n"
				+ newdesc + "\n";
		
		return output;
	}
	
	static String categToString(int id) {
		/*
		 * Recebe um inteiro relativo
		 * a uma categoria e retorna
		 * seu nome correspondente.
		 */
		String out = "Placeholder";
		switch(id) {
			case 0: {out = "Physical"; break;}
			case 1: {out = "Special"; break;}
			case 2: {out = "Status"; break;}
		}
		return out;
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

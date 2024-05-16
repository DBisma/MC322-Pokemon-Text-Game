package moves;

import pokemontextgame.TypeChart;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;

//TODO: Como de praxe, teremos que tornar abstrata mais tarde como será feito com Status. Ver "Status.java"
//Mas como chamaremos o conjunto de suas subclasses se "Move" deixa de ser um tipo ao declaramos

/*
 *  TODO: Dilema: Desejamos que a classe Move nunca seja instanciada num objeto, apenas suas subclasses.
 *  Todavia, desejamos que seja possível um Array Move que armazene as instâncias de suas subclasses.
 *  O array currentMoves do tipo Moves, por exemplo, deverá guardar DamageDealing, StatChange, etc.
 *  
 *  Como podemos fazer isso?
 *  
 *  Um array de classe abstrata Move poderia armazenar subclasses. Mas e se desejarmos retornar
 *  um move específico? Há várias subclasses de moves diferentes. Como faríamos?
 */

public abstract class Move { 
	/*
	 * Armazena as informações base dos ataques.
	 * 
	 */
	private int id; // https://bulbapedia.bulbagarden.net/wiki/List_of_moves
	private String name;
	private String desc;
	private int type;
	private int maxPoints; // com quantos "usos" o ataque começa;
	private int points; // quantos "usos" restam ao ataque;
	private int accuracy; // se for 0 a 100. Se for -1, sempre acerta
	private int priority;
	// TODO: Como armazenar o método / efeito especial de cada ataque?
	// TODO: MOVE SERÁ UMA CLASSE ABSTRATA. OS MOVES EM SI SERÃO INSANIDADES DE CLASSE ÚNICA
	
	// Possíveis resultados de um move
	public enum moveResults{HIT, MISS, FAIL, HIT_SUPER, HIT_NOTVERY, HIT_IMMUNE,
		RAISE_YES, RAISE_FAIL, LOWER_YES, LOWER_FAIL}
	public enum moveCategs{PHYSICAL, SPECIAL, STATUS};
	// Construtor provisório; mais tarde, TODO Construir de json
	public Move(int id, String name, int type, int maxP, int pri, int accu){
		this.id = id;
		this.name = name;
		this.type = type;
		this.maxPoints = maxP;
		this.points = maxP; // sempre é construído com o max
		this.priority = pri;
		this.accuracy = accu; // TODO: Lidar com moves que *nunca erram*.
	}
	
	// Será overridden. TODO: Qual o melhor jeito? Interfaces? Classe abstrata e métodos abstratos?
	abstract moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart);
	
	@Override
	public String toString() {
		/*
		 * Converte informações de Move numa grande string.
		 * TODO: Preparar um toString no caso de Move de categ. Stat e não SpecAtk ou Atk.
		 * TODO: Talvez para isso seja necessário dividir cada uma dessas categs em subclasses, na verdade.
		 * TODO: Atualizar para divisão em subclasses
		 * 
		 * ISSO SERÁ OVERRIDDEN É CLARO.
		 */
		String newdesc;
		String categAndPower;
		String output;
		
		if(this.desc == null)
			newdesc = "Nenhuma descrição disponível.";
		else
			newdesc = "Descrição: " + desc;
		
//		categAndPower = "Categoria: " + Move.categToString(this.categ); // TODO: Atualizar para refletir nova divisão em subclasses
//		if(this.categ != 2) // adiciona Power se houver
//			categAndPower += "\n" + "Dano base: " + this.power; // TODO: Só aparecerá em outra subcliasse
			
		output = "Move: " + "'"+ this.name + "'\n"
				+ "Tipo: " + TypeChart.typeToString(this.type) + "\n"
//				+ categAndPower + "\n"
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
	public String getName() {
		return name;
	}
	public void setName(String nome) {
		this.name = nome;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
	public int getTipagem() {
		return type;
	}
	public void setTipagem(int tipagem) {
		this.type = tipagem;
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
	public abstract Move.moveCategs getCateg();
}

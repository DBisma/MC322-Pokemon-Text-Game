package moves;

import pokemontextgame.TypeChart;
import moves.Move.moveResults;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;

public abstract class Move { 
	/*
	 * Armazena as informações base dos ataques.
	 * É superclasse de DmgDealing e StatusGeneral,
	 * que são superclasse de outra quatro classes principais
	 * e duas outras secundárias. As quatro em questão conseguem
	 * cobrir a construção e métodos de uns 2/3 de todos os moves, 
	 * ao passo que o 1/3 restante de moves se divide em
	 * subclasses das duas secundárias com métodos próprios.
	 */
	protected int id; // https://bulbapedia.bulbagarden.net/wiki/List_of_moves
	protected String name;
	protected String desc;
	protected int type;
	protected int maxPoints; // com quantos "usos" o ataque começa;
	protected int points; // quantos "usos" restam ao ataque;
	protected int accuracy; // Usualmente, 0 a 100. Se for -1, sempre acerta
	protected int priority;
	// Possíveis resultados de um move
	public enum moveResults{HIT, MISS, HIT_SUPER, HIT_NOTVERY, HIT_IMMUNE,
		RAISE_YES, RAISE_FAIL, LOWER_YES, LOWER_FAIL, TOTAL_SUCCESS, PARTIAL_SUCCESS, TOTAL_FAILURE}
	public enum moveCategs{PHYSICAL, SPECIAL, STATUS};
	
	// Construtor provisório; mais tarde, TODO Construir de json
	public Move(int id, String name, int type, int maxP, int pri, int accu){
		this.id = id;
		this.name = name;
		this.type = type;
		this.maxPoints = maxP;
		this.points = maxP; // sempre é construído com o max
		this.priority = pri;
		this.accuracy = accu;
	}
	
	// É overridden por suas subclasses
	public moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		this.spendPp();
		field.textBufferAdd(pAtk.getName()  + " utilizou " + this.getName() + "!\n");
		// Roll de precisão; inclui evasiveness e accuracy dos dois pokemons em questão
		if(!TurnUtils.doesItHit(pAtk, pDef, this, field)) {
			field.textBufferAdd("Mas " + pAtk.getName()  + " errou!\n");
			return Move.moveResults.MISS;
		}
		return moveResults.HIT;
	};
	
	@Override
	public String toString() {
		/*
		 * Converte informações de Move numa grande string.
		 */
		String newdesc;
		String accu = "";
		String output;
		
		if(this.desc == null)
			newdesc = "Nenhuma descrição disponível.";
		else
			newdesc = "Descrição: " + desc;
		if(accuracy == -1) {
			accu = "Sempre acerta";
		}
		else
			accu += this.accuracy;
		
		output = "Move: " + "'"+ this.name + "'\n"
				+ "Tipo: " + TypeChart.typeToString(this.type) + "\n"
				+ "PP: " + this.points + " / " + this.maxPoints + "\n"
				+ "Prioridade: " + this.priority + "\n"
				+ "Precisão: " + accu + "\n"
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
	public void spendPp(){
		this.points += -1;
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

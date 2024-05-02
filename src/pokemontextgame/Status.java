package pokemontextgame;

//TODO: talvez tornar abstrata no futuro e tratar cada status como uma classe?
//TODO: Podemos fazer todos os status serem loadados de imediato no jogo e apenas fazer os pokemons terem um pointer comum para eles.
// ao invés de cada pokemon construir seu próprio objeto "status".

public class Status { 
	/*
	 * Classe que armazena os possíveis efeitos de Status
	 * não-voláteis (burn, poison, frozen, paralyzed, asleep)
	 * e voláteis (curse e confusion são os principais)
	 */
	
	// Estou ficando sem idéias agora
	private String name;
	private int id;
	
	// TODO:
	// id = -1 -> neutro
	// id = 0 -> Burn
	// id = 1 -> Frozen
	// id = 2 -> Paralyzed
	// id = 3 -> Poisoned
	// id = 4 -> Badly Poisoned?
	
	public Status() {
		name = "Neutro";
		id = -1;
	}
	
	// TODO: Talvez seja uma boa fazer funcs. para cada Status e fazer cada um ser uma subclasse.
	// Fazer função de [EffectPre] e [EffectPost] para cada Status; SOBRESCREVER CADA MÉTODO PARA SEU STATUS ESPECÍFICO.
	
	static boolean paralyzedThisTurn(Poke mon) {
		/*
		 * Função de saída aleatória que retorna "true" caso
		 * o pokemon dê azar de não poder mover neste turno.
		 * TODO: Seria legal mudar a velocidade de animação para um pokemon paralisado.
		 */ 
		if(mon.getStatusFx().getId() == 2) {
			return TurnUtils.rollChance(25);
		}
		else
			return false;
		// TODO: Otimizar checagens de stats no começo de turnos que envolva Sleep, Frozen, Paralyzed, Confused... etc
		
	}
	static boolean burnHalving(Poke mon, Move move) {
		/*
		 * Verifica se o Pokemon terá sua saída de dano
		 * diminuída por estar queimado.
		 */
		if(mon.getStatusFx().getId() != 0)
			return false;
		else if(move.getCateg() != 0)
			return false;
		else // TODO: Colocar condição extra: Habilidade Guts
			return true;
	}
	
	// Getters e Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}

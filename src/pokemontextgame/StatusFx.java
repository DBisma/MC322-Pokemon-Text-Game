package pokemontextgame;

import moves.Move;

// TODO: Talvez seja uma boa cada pokemon "criar" um objeto Status quando recebe o efeito.

public class StatusFx { 
	/*
	 * Classe que armazena os possíveis efeitos de Status
	 * não-voláteis (burn, poison, frozen, paralyzed, asleep)
	 * e voláteis (curse e confusion são os principais);
	 * TODO: Por enquanto, só aceita não voláteis
	 */
	
	// Começaremos com esses poucos stats de início
	public enum typeList{
					// Volatile, Permanent, Duration
		NEUTRAL 	(false, true, 0), 
		BURN		(false, true, 0),
		FREEZE		(false, false, 10), 
		PARALYSIS	(false, true, 0),
		POISON		(false, true, 0),
		BADPOISON	(false, true, 0),
		SLEEP		(false, false, 3);
		
		public final boolean isVolatile;
		public final boolean isPermanent;
		public final int maxDuration;
		
		typeList(boolean isVolatile, boolean isPermanent, int maxDuration){
			this.isPermanent = isPermanent;
			this.maxDuration = maxDuration;
			this.isVolatile = isVolatile;
		}
		
		public boolean isPermanent() {return this.isPermanent;}
		public boolean isVolatile() {return this.isVolatile;}
		public int getMaxDuration() {return this.maxDuration;}
		
	
	}; // TODO: Fazer todos os enums serem públicos numa classe só para isso?
	
	private typeList type;
	private int remainDuration; // tempo até o efeito desaperecer por conta
	
	// Getters e Setters
	public StatusFx(typeList type) {
		this.type = type;
		this.setRemainDuration(type.getMaxDuration());
	}
	
	// Getters e Setters
	public void setStatusFull(typeList type) {
		/*
		 * Revamp total do status para modificações.
		 * Serve para não termos que criar um objeto novo para
		 * cada mudança de status fx aplicada ao pokemon.
		 * TODO: Verificar se será útil
		 */
		this.type = type;
		this.setRemainDuration(type.getMaxDuration());
	}
	
	public void setStatusFull(typeList type, int durationMod) {
		/*
		 * Overload da função que aceita um modificador
		 * para aumentar a duração.
		 * TODO: Verificar se será útil
		 */
		this.type = type;
		this.setRemainDuration(type.getMaxDuration()*durationMod);
	}
	
	public void setStatusDefault() {
		/*
		 * Retorna ao usual.
		 * Talvez seja desnecessária.
		 */
		this.type = typeList.NEUTRAL;
	}

	public typeList getType() {
		return type;
	}

	public void setType(typeList type) {
		this.type = type;
	}

	public int getRemainDuration() {
		return remainDuration;
	}

	public void setRemainDuration(int remainDuration) {
		this.remainDuration = remainDuration;
	}
	
	// TODO: Talvez seja uma boa fazer funcs. para cada Status e fazer cada um ser uma subclasse.
	// Fazer função de [EffectPre] e [EffectPost] para cada Status; SOBRESCREVER CADA MÉTODO PARA SEU STATUS ESPECÍFICO.
	
	
	/*

	static boolean paralyzedThisTurn(Poke mon) {
		/*
		 * Função de saída aleatória que retorna "true" caso
		 * o pokemon dê azar de não poder mover neste turno.
		 * TODO: Seria legal mudar a velocidade de animação para um pokemon paralisado.
		  
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
		 
		if(mon.getStatusFx().getId() != 0)
			return false;
		else if(move.getCateg() != 0)
			return false;
		else // TODO: Colocar condição extra: Habilidade Guts
			return true;
	}
	
	 */
	// Getters e Setters
	
}

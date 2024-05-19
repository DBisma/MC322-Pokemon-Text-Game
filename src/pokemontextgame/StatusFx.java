package pokemontextgame;

public class StatusFx { 
	/*
	 * Classe que armazena os possíveis efeitos de Status
	 * não-voláteis (burn, poison e bad_poison, frozen, paralyzed, asleep).
	 * Apenas esses Statuses estão disponíveis num primeiro momento
	 */
	
	// Começaremos com esses poucos stats de início
	public enum typeList{
					// Volatile, Permanent, Duration
		NEUTRAL 	(false, true, 0), 
		BURN		(false, true, 0),
		FREEZE		(false, false, 10), 
		PARALYSIS	(false, true, 0),
		POISON		(false, true, 0),
		BAD_POISON	(false, true, 0),
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
		
	
	};
	
	private typeList type;
	private int remDuration; // tempo até o efeito desaperecer por conta
	private int timeAfflicted; // útil para calcular o dano de BAD_POISON
	
	// Getters e Setters
	public StatusFx(typeList type) {
		this.type = type;
		this.remDuration = type.getMaxDuration();
	}
	
	// Getters e Setters
	public void setStatusFull(typeList type) {
		/*
		 * Revamp total do status para modificações.
		 * Serve para não termos que criar um objeto novo para
		 * cada mudança de status fx aplicada ao pokemon.
		 */
		this.type = type;
		this.timeAfflicted = 1;
		this.setRemDuration(type.getMaxDuration());
	}
	
	public void setStatusNeutral() {
		/*
		 * Retorna ao usual.
		 * Talvez seja desnecessária.
		 */
		this.type = typeList.NEUTRAL;
		timeAfflicted = 1;
	}

	public typeList getType() {
		return type;
	}

	public void setType(typeList type) {
		/*
		 * Como o tipo mudou, timeAfflicted vai para zero.
		 */
		this.type = type;
		timeAfflicted = 1;
	}

	public int getRemDuration() {
		return remDuration;
	}

	public void setRemDuration(int duration) {
		this.remDuration = duration;
	}

	public void durationLessen() {
		/*
		 * Diminui em um turno a duração restante do StatusFx.
		 * Atualiza
		 */
		this.remDuration -= 1;
		if(remDuration == 0) {
			this.setStatusFull(typeList.NEUTRAL);
		}
	}

	public int getTimeAfflicted() {
		return timeAfflicted;
	}

	public void setTimeAfflicted(int timeAfflicted) {
		this.timeAfflicted = timeAfflicted;
	}
	
	public void timeAfflictedIncrease() {
		timeAfflicted += 1;
	}
	
	public void remDurationDecrease() {
		remDuration -= 1;
	}
	
	public void statusTurnPass() {
		/*
		 * Função que de uma vez faz todos
		 * os efeitos de passagem de turno
		 * sobre um stat.
		 */
		this.timeAfflicted += 1;
		if(!this.type.isPermanent) {
			this.remDuration -= 1;
			if(remDuration == 0) {
				this.setStatusNeutral();
			}
			else {
				// Chances de se curar do stat automaticamente
				switch(type) {
					case FREEZE:{
						if(TurnUtils.rollChance(20))
							this.setStatusNeutral();
						break;
					}
					case SLEEP:{
						if(TurnUtils.rollChance(timeAfflicted*33))
							this.setStatusNeutral();
						break;
					}
					default:{
						break;
					}
				}
			}
		}
	}
	
}

package pokemontextgame.moves;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;

final public class DmgPlusStat extends DamageDealing {
	/*
	 * Subclasse para moves que dão dano
	 * e podem afetar alguém com um Stat Change.
	 */
	
	private int statId; // qual stat
	private int boostStages; // quanto (pode ser negativo)
	private boolean boostSelf; // quem
	private int boostChance; // com qual frequência
	
	public DmgPlusStat(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp,
			int sId, int bStages, int bChance, boolean bSelf) {
		super(id, name, type, maxP, pri, accu, categ, bp);
		this.setStatId(sId);
		this.setBoostStages(bStages); 
		this.setBoostSelf(bSelf);
		this.setBoostChance(bChance);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Difere do useMove da superclasse DamageDealing
		 * ao incluir a chance de modificar stats
		 * 
		 * TODO: O resultado é sempre HIT se a parte importante ativar...
		 * O resultado ignora se o stat change foi obtido ou não.
		 */
		
		Move.moveResults result = super.useMove(field, pAtk, pDef, tchart);
		
		// Aplicação de efeitos secundários
		if(result != Move.moveResults.TOTAL_FAILURE && result != Move.moveResults.MISS && result != Move.moveResults.HIT_IMMUNE) {
			
			String who;
			// Si
			if(TurnUtils.rollChance(boostChance)) {
				if(this.isBoostSelf())
					// Verificação de limite de statBoost para Atacante
					if(Math.abs(boostChance + pAtk.getStatModGeneral(statId)) > 6){
						return result;
					}
					else {
						pAtk.boostStat(statId, boostStages);
						who = pAtk.getName();
					}
				else {
					// Verificação de limite de statBoost para Defensor
					if(Math.abs(boostChance + pDef.getStatModGeneral(statId)) > 6){
						return result;
					}
					else {
						pDef.boostStat(statId, boostStages);
						who = pDef.getName();
					}
				}
				
				String verb;
				
				if(boostStages > 0)
					verb = "aumentado";
				else
					verb = "reduzido";
				
				field.textBufferAdd(who + " teve seu" + TurnUtils.getStatName(statId) 
				+ verb + " em " + Math.abs(boostStages) + " estágios!\n");
			}
		}
		
		// TODO: Como enviar o RESULTADO de mudança de stat? Apenas enviamos HIT por enquanto...
		// Temos a notificação em texto, mas e em dados da forma RESULT?
		return result;
	}

	public int getStatId() {
		return statId;
	}

	public void setStatId(int statId) {
		this.statId = statId;
	}

	public int getBoostStages() {
		return boostStages;
	}

	public void setBoostStages(int boostStages) {
		this.boostStages = boostStages;
	}

	public boolean isBoostSelf() {
		return boostSelf;
	}

	public void setBoostSelf(boolean boostSelf) {
		this.boostSelf = boostSelf;
	}

	public int getBoostChance() {
		return boostChance;
	}

	public void setBoostChance(int boostChance) {
		this.boostChance = boostChance;
	}
	
	
}

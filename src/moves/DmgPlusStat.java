package moves;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;

public class DmgPlusStat extends DamageDealing {
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
		 */
		
		Move.moveResults result = super.useMove(field, pAtk, pDef, tchart);
		
		// Aplicação de efeitos secundários
		if(result != Move.moveResults.FAIL && result != Move.moveResults.MISS && result != Move.moveResults.HIT_IMMUNE)
			if(TurnUtils.rollChance(this.getBoostChance())) {
				// si
				if(this.isBoostSelf()) {
					pAtk.boostStat(getStatId(), getBoostStages());
				}
				// outrem
				else {
					pDef.boostStat(getStatId(), getBoostStages());
				}
			}
		
		// TODO: Como enviar a notificação de aplicação de efeito?
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

package pokemontextgame.moves;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.StatusFx;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;

public final class DmgPlusFx extends DamageDealing {
	/*
	 * Subclasse para moves que dão dano
	 * e podem afetar o oponente com um efeito
	 */
	
	// Status Effect que pode ser aplicado e sua chance
	private StatusFx.typeList fxType;
	private int fxChance;

	public DmgPlusFx(int id, String name, int type, int maxP, int pri, int accu, String desc,
			Move.moveCategs categ, int bp, StatusFx.typeList fxType, int fxChance) {
		super(id, name, type, maxP, pri, accu, desc, categ, bp);
		this.fxChance = fxChance;
		this.fxType = fxType;
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Difere do useMove da superclasse DamageDealing
		 * ao incluir a chance de aplicar o status
		 */
		
		Move.moveResults result = super.useMove(field, pAtk, pDef, tchart);
		
		// Aplicação de efeitos secundários
		if(pDef.getStatusFx().getType() == StatusFx.typeList.NEUTRAL && result != Move.moveResults.TOTAL_FAILURE 
				&& result != Move.moveResults.MISS && result != Move.moveResults.HIT_IMMUNE)
			if(TurnUtils.rollChance(this.fxChance)) {
				pDef.setStatusFx(fxType);
				field.textBufferAdd(pDef.getName() + " foi afligido com " + fxType + "!\n");
			}
		return result;
	}

	// Getters e Setters
	public StatusFx.typeList getFxType() {
		return fxType;
	}

	public void setFxType(StatusFx.typeList fxType) {
		this.fxType = fxType;
	}

	public int getFxChance() {
		return fxChance;
	}

	public void setFxChance(int fxChance) {
		this.fxChance = fxChance;
	}
	
	
}

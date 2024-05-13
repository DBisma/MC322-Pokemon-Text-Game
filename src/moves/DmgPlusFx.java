package moves;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.StatusFx;
import pokemontextgame.TurnUtils;
//import moves.DamageDealing.categs; TODO remover
import pokemontextgame.TypeChart;

public class DmgPlusFx extends DamageDealing {
	/*
	 * Subclasse para moves que dão dano
	 * e podem afetar o oponente com um efeito
	 */
	
	// Status Effect que pode ser aplicado e sua chance
	private StatusFx.typeList fxType;
	private int fxChance;
	
	// TODO: Como formatar esse construtor do modo correto? Em espaçamento, quero dizer.
	public DmgPlusFx(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp, 
					StatusFx.typeList fxType, int fxChance) {
		super(id, name, type, maxP, pri, accu, categ, bp);
		this.setFxChance(fxChance);
		this.setFxType(fxType);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Difere do useMove da superclasse DamageDealing
		 * ao incluir a chance de aplicar o status // TODO: Como enviar a notificação de aplicação de efeito?
		 */
		
		Move.moveResults result = super.useMove(field, pAtk, pDef, tchart);
		
		// Aplicação de efeitos secundários
		if(result != Move.moveResults.FAIL && result != Move.moveResults.MISS && result != Move.moveResults.HIT_IMMUNE)
			if(TurnUtils.rollChance(this.fxChance)) {
				pDef.setStatusFx(fxType, false, -1);
			}
		
		// TODO: Como enviar a notificação de aplicação de efeito?
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

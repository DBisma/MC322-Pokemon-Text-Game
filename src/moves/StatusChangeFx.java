package moves;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.StatusFx;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;

public class StatusChangeFx extends StatusChanging {
	/*
	 * Subclasse dedicada para ataques que apenas
	 * aplicam um status no em alguém.
	 */
	
	// Um tanto análogo a DmgPlusFx, mas sem a seção de dano, e com chance 100%
	private StatusFx.typeList fxType; // efeito em si

		
	public StatusChangeFx(int id, String name, int type, int maxP, int pri, int accu,
			StatusFx.typeList fxType, int fxDuration, boolean isVolatile) {
		super(id, name, type, maxP, pri, accu);
		this.fxType = fxType;
	}
	
	public moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Tenta aplicar StatusFx sempre no pokemon inimigo. Fracassa
		 * se inimigo já estiver lidando com Status volátil.
		 * TODO: Por enquanto, faremos todo StatusFx falhar se o pokemon já estiver afetado.
		 * Futuramente, apenas Não-Voláteis falharão, e voláteis serão acumulados.
		 */
		
		// Roll de precisão
		moveResults firstResu = super.useMove(field, pAtk, pDef, tchart);
		if(firstResu == moveResults.MISS || firstResu == moveResults.HIT_IMMUNE)
			return firstResu;
		else {
			if(pDef.getStatusFx().getType() != StatusFx.typeList.NEUTRAL)
				return moveResults.FAIL;
			else {
				pDef.setStatusFx(fxType); // TODO: Permitir soma de voláteis e não voláteis
				return moveResults.HIT;
			}
		}
	}
	
}

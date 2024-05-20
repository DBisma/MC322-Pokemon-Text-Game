package pokemontextgame.moves;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.StatusFx;
import pokemontextgame.TypeChart;

final public class StatusChangeFx extends StatusGeneral {
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
	
	@Override
	public moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Tenta aplicar StatusFx sempre no pokemon inimigo. Fracassa
		 * se inimigo já estiver lidando com Status volátil.
		 * Faremos todo StatusFx falhar se o pokemon já estiver afetado.
		 */
		
		// Roll de precisão
		moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		if(resu == moveResults.MISS)
			return resu;
		// Verificação de imunidades
		else if(tchart.compoundTypeMatch(type, pDef) < 0.001) {
			field.textBufferAdd("Mas não afetou " + pDef.getName()  + " !\n");
			return moveResults.HIT_IMMUNE;
		}
		// Fracassa se oponente já estiver sob efeito de status
		else if(pDef.getStatusFx().getType() != StatusFx.typeList.NEUTRAL) {
			field.textBufferAdd("Mas " + pAtk.getName() + " falhou!\n");
			return moveResults.TOTAL_FAILURE;
		}
		// Funcionou
		else {
			field.textBufferAdd(pDef.getName() + " foi afligido por " + fxType + "!\n");
			pDef.setStatusFx(fxType);
			return moveResults.HIT;
		}
	}
}

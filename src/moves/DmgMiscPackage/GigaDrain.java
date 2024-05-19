package moves.DmgMiscPackage;

import moves.Move;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;
import moves.DmgMisc;

public class GigaDrain extends DmgMisc {
	/*
	 * Giga Drain dá dano especial de planta
	 * e recupera a vida do Pokemon em metade do dano dado.
	 */
	
	public GigaDrain(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		Move.moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		if(resu != Move.moveResults.TOTAL_FAILURE && resu != Move.moveResults.MISS) {
			float typeMod = tchart.compoundTypeMatch(this.type, pDef);
			// Heal caso atinja
			int healAmount = Math.round(0.50f * TurnUtils.calcDmg(this, pAtk, pDef, typeMod));
			pAtk.healMon(healAmount);
		}
		return resu;
	}
}
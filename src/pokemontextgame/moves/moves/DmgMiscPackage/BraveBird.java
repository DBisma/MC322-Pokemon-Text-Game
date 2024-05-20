package pokemontextgame.moves.DmgMiscPackage;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.DmgMisc;
import pokemontextgame.moves.Move;

public final class BraveBird extends DmgMisc {
	/*
	 * Move tipo voador que danifica o usuário
	 * em 1/3 do dano dado ao inimigo.
	 */
	
	public BraveBird(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		Move.moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		if(resu != Move.moveResults.MISS) {
			float typeMod = tchart.compoundTypeMatch(this.type, pDef);
			int SelfDamage = Math.round(0.33f * TurnUtils.calcDmg(this, pAtk, pDef, typeMod));
			pAtk.dmgMon(SelfDamage);
			field.textBufferAdd(pAtk.getName()  + " se feriu com o recuo!");
		}
		return resu;
	}
}

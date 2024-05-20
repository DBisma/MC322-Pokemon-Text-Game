package pokemontextgame.moves.DmgMiscPackage;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.DmgMisc;
import pokemontextgame.moves.Move;

public class Superpower extends DmgMisc {
	/*
	 * Superpower aplica grande dano e depois
	 * reduz o ataque e defesa do usuário.
	 */
	
	public Superpower(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		Move.moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		String name = pAtk.getName();
		if(resu != Move.moveResults.TOTAL_FAILURE && resu != Move.moveResults.MISS) {
			if(pAtk.boostStat(0, -1)) {
				field.textBufferAdd(name + " teve seu " + TurnUtils.getStatName(0) 
				+ " reduzido em 1 estágio!\n");
			}
			if(pAtk.boostStat(1, -1)){
				field.textBufferAdd(name + " teve seu " + TurnUtils.getStatName(1) 
				+ " reduzido em 1 estágio!\n");
			}
		}
		return resu;
	}
}
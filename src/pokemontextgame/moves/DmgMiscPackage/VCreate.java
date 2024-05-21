package pokemontextgame.moves.DmgMiscPackage;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.DmgMisc;
import pokemontextgame.moves.Move;

public class VCreate extends DmgMisc {
	/*
	 * V-Create causa dano, e logo depois
	 * diminui os stats de Def, Sp. Def e Speed
	 * do usuário.
	 */
	
	public VCreate(int id, String name, int type, int maxP, int pri, int accu, String desc, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, desc, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		Move.moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		String name = pAtk.getName();
		if(resu != Move.moveResults.TOTAL_FAILURE && resu != Move.moveResults.MISS) {
			if(pAtk.boostStat(1, -1)) {
				field.textBufferAdd(name + " teve seu stat " + TurnUtils.getStatName(1) 
				+ " reduzido em 1 estágio!\n");
			}
			if(pAtk.boostStat(3, -1)){
				field.textBufferAdd(name + " teve seu stat " + TurnUtils.getStatName(3) 
				+ " reduzido em 1 estágio!\n");
			}
			if(pAtk.boostStat(4, -1)){
				field.textBufferAdd(name + " teve seu stat " + TurnUtils.getStatName(4) 
				+ " reduzido em 1 estágio!\n");
			}
		}
		return resu;
	}
}
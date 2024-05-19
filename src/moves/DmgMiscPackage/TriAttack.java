package moves.DmgMiscPackage;

import moves.Move;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.StatusFx;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;

import java.util.Random;

import moves.DmgMisc;

public class TriAttack extends DmgMisc {
	/*
	 * TriAttack dá dano especial mas possui uma chance de aplicar
	 * burn, paralysis ou freeze no pokemon inimigo.
	 */
	
	public TriAttack(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		Move.moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		if(resu != Move.moveResults.TOTAL_FAILURE && resu != Move.moveResults.MISS
				 && pDef.getStatusFx().getType() == StatusFx.typeList.NEUTRAL) {
			if(TurnUtils.rollChance(20)) {
				// Obtendo um número aleatório de 0 a 2
				Random r = new Random();
				int roll = r.nextInt(3);
				StatusFx.typeList fxType = StatusFx.typeList.FREEZE;
				switch(roll) {
					case(0):{
						break;
					}
					case(1):{
						fxType = StatusFx.typeList.BURN;
						break;
						
					}
					case(2):{
						fxType = StatusFx.typeList.PARALYSIS;
						break;
					}
				}
				pDef.setStatusFx(fxType);
				field.textBufferAdd(pDef.getName() + " foi afligido com " + fxType + "!\n");
			}
		}
		return resu;
	}
}
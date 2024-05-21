package pokemontextgame.moves.DmgMiscPackage;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.StatusFx;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.DmgMisc;
import pokemontextgame.moves.Move;

public class FlareBlitz extends DmgMisc {
	/*
	 * Flare Blitz é uma combinação única entre os moves
	 * que dão puro dano, possuem recuo (como Brave Bird e Double Edge)
	 * mas também aplica um status fx (como Fire Punch).
	 * Ainda por cima, descongela um oponente ongelado.
	 */
	
	public FlareBlitz(int id, String name, int type, int maxP, int pri, int accu, String desc, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, desc, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		Move.moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		if(resu != Move.moveResults.TOTAL_FAILURE && resu != Move.moveResults.MISS) {
			float typeMod = tchart.compoundTypeMatch(this.type, pDef);
			// auto-dano caso atinja;
			int selfDamage = Math.round(0.33f * TurnUtils.calcDmg(this, pAtk, pDef, typeMod));
			pAtk.dmgMon(selfDamage);
			field.textBufferAdd(pAtk.getName()  + " se feriu com o recuo!\n");
			// descongelar
			if(pDef.getStatusFx().getType() == StatusFx.typeList.FREEZE) {
				pDef.getStatusFx().setStatusNeutral();
				field.textBufferAdd(pDef.getName()  + " foi descongelado pelo Move!");
			}
			// chance de causar fogo
			else if((pDef.getStatusFx().getType() == StatusFx.typeList.NEUTRAL) && TurnUtils.rollChance(10)) {
				pDef.setStatusFx(StatusFx.typeList.BURN);
				field.textBufferAdd(pDef.getName() + " foi afligido por " + StatusFx.typeList.BURN + "!\n");
			}
		}
		return resu;
	}
}
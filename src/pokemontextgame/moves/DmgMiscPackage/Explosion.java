package pokemontextgame.moves.DmgMiscPackage;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.DmgMisc;
import pokemontextgame.moves.Move;

public class Explosion extends DmgMisc {
	/*
	 * Move que dá K.O. no pokemon
	 * usuário mas possui dano enorme.
	 */
	
	public Explosion(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		Move.moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		pAtk.dmgMon(pAtk.getMaxHp() + 1);
		field.textBufferAdd(pAtk.getName()  + " se auto-destruiu!\n");
		return resu;
	}
}
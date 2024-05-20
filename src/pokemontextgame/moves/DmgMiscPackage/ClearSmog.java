package pokemontextgame.moves.DmgMiscPackage;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.DmgMisc;
import pokemontextgame.moves.Move;

public class ClearSmog extends DmgMisc {
	/*
	 * TriAttack dá dano especial mas possui uma chance de aplicar
	 * burn, paralysis ou freeze no pokemon inimigo.
	 */
	
	public ClearSmog(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		Move.moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		String name = pDef.getName();
		pDef.resetStats();
		field.textBufferAdd(name + " teve seu todas suas mudanças de stats anuladas!\n");
		return resu;
	}
}

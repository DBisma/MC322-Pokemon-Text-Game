package moves;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;

final public class Struggle extends DmgMisc{
	/*
	 * Move especial que todo pokemon pode usar
	 * quando não tem mais PP disponível.
	 * Seu PP não diminui nunca e sempre acerta.
	 * É o move de "id 5" construído no Battlefield.
	 */
	
	public Struggle() {
		super(0, "Struggle", 19, 999, 0, -1, Move.moveCategs.PHYSICAL, 50);
		
	}

	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Sempre acerta, ignora imunidades.
		 * Dá dano a si mesmo de 1/4 do Hp máximo.
		 */
		field.textBufferAdd(pAtk.getName()  + " utilizou " + this.getName() + "!\n");
		int dmg = TurnUtils.calcDmg(this, pAtk, pDef, 1);
		pDef.dmgMon(dmg);
		pAtk.dmgMon( (int) (pAtk.getMaxHp() * 0.25f)); // recoil de 1/4 de maxHp
		field.textBufferAdd(pAtk.getName()  + " se feriu com o recuo!");
		return Move.moveResults.HIT;
	}
}

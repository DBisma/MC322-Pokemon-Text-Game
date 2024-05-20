package pokemontextgame.moves.DmgMiscPackage;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.DmgMisc;
import pokemontextgame.moves.Move;

public class SeismicToss extends DmgMisc {
	/*
	 * Se o oponente não for imune, 
	 * dá dano bruto igual o nível do usuário.
	 * Fora imunidades, não é afetado por resistências
	 * ou fraquezas causadas por combinações de tipos.
	 */
	
	public SeismicToss(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		int dmg = pAtk.getLevel();
		// Modificador de dano baseado em eficácia de tipos
		float typeMod = tchart.compoundTypeMatch(this.type, pDef);
		// Caso de imunidade
		float error = 0.01f;
		if(typeMod < error) {
			field.textBufferAdd("Mas não afetou " + pDef.getName()  + " !\n");
			return Move.moveResults.HIT_IMMUNE;
		}
		else {
			pDef.dmgMon(dmg);
			return Move.moveResults.HIT;
		}
	}
}
package pokemontextgame.moves.StatusMiscPackage;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.*;

public class SuperclassSelfHealingMove extends StatusMisc {
	/*
	 * Softboiled é um move que recupera o HP do usuário.
	 */
	public SuperclassSelfHealingMove(int id, String name, int type, int maxP, int pri, int accu, String desc) {
		super(id, name, type, maxP, pri, accu, desc);
	}

	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		super.useMove(field, pAtk, pDef, tchart);
		if(pAtk.getCurHp() == pAtk.getMaxHp()) {
			field.textBufferAdd("Mas " + pAtk.getName() + "falhou!\n");
			return Move.moveResults.TOTAL_FAILURE;
		}
		else {
			pAtk.healMon((int) (pAtk.getMaxHp()*0.5f));
			field.textBufferAdd(pAtk.getName() + " conseguiu se recuperar!\n");
			return Move.moveResults.HIT;
		}
	}
}

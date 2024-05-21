package pokemontextgame.moves.StatusMiscPackage;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.Move;
import pokemontextgame.moves.StatusMisc;

public class ShellSmash extends StatusMisc {
	/*
	 * Ataque único que diminui Def. e Spec. Def
	 * em um estágio cada mas aumenta Atk, Spec. Atk e Speed
	 * em dois estágios cada.
	 */
	public ShellSmash(int id, String name, int type, int maxP, int pri, int accu, String desc) {
		super(id, name, type, maxP, pri, accu, desc);
	}
	
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		super.useMove(field, pAtk, pDef, tchart);
		boolean b1, b2, b3, b4, b5;
		b1 = pAtk.boostStat(1, -1);
		b2 = pAtk.boostStat(3, -1);
		b3 = pAtk.boostStat(0, 2);
		b4 = pAtk.boostStat(2, 2);
		b5 = pAtk.boostStat(4, 2);
		
		if(b1) {
			field.textBufferAdd(name + " teve seu stat " + TurnUtils.getStatName(1) 
			+ " reduzido em 1 estágio!\n");
		}
		if(b2) {
			field.textBufferAdd(name + " teve seu stat " + TurnUtils.getStatName(3) 
			+ " reduzido em 1 estágio!\n");
		}
		if(b3){
			field.textBufferAdd(name + " teve seu stat " + TurnUtils.getStatName(0) 
			+ " aumentado em 2 estágios!\n");
		}
		if(b4) {
			field.textBufferAdd(name + " teve seu stat " + TurnUtils.getStatName(2) 
			+ " aumentado em 2 estágios!\n");
		}
		if(b5){
			field.textBufferAdd(name + " teve seu stat " + TurnUtils.getStatName(4) 
			+ " aumentado em 2 estágios!\n");
		}
		
		if(b1 == b2 == b3 == b4 == b5 == true) {
			return moveResults.TOTAL_SUCCESS;
		}
		else if(b1 == b2 == b3 == b4 == b5 == false) {
			field.textBufferAdd("Mas " + pAtk.getName() + " falhou!\n");
			return moveResults.TOTAL_FAILURE;
		}
		else {
			return moveResults.PARTIAL_SUCCESS;
		}
	}
	
}

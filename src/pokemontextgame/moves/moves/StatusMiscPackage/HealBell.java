package pokemontextgame.moves.StatusMiscPackage;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.StatusFx;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.Move;
import pokemontextgame.moves.StatusMisc;

public class HealBell extends StatusMisc {
	/*
	 * Cura a party de todos os Status Fx.
	 */
	
	public HealBell(int id, String name, int type, int maxP, int pri, int accu) {
		super(id, name, type, maxP, pri, accu);
	}
	
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		super.useMove(field, pAtk, pDef, tchart);
		int i = 0;
		for(i = 0; i < 6; i++) {
			Poke mon = field.getLoadedPlayer().getTeam()[i];
			if(mon != null && mon.getStatusFx().getType() != StatusFx.typeList.NEUTRAL) {
				mon.setStatusFx(StatusFx.typeList.NEUTRAL);
			}
		}
		
		field.textBufferAdd("O time inteiro foi curado de seus Efeitos de Status!\n");
		return moveResults.TOTAL_SUCCESS;
	}
}

package moves;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;

abstract class StatusChanging extends Move {
	/*
	 * Classe para moves que alteram status fx
	 * ou stats (dano, ataque, etc.) ou o battlefield.
	 * Subclasses serão usadas para os casos em si ou casos especiais.
	 */
	final Move.moveCategs categ = Move.moveCategs.STATUS; // não pode ser mais nada
	
	//TODO: moves de HEAL
	public StatusChanging(int id, String name, int type, int maxP, int pri, int accu) {
		super(id, name, type, maxP, pri, accu);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Contém apenas a checagem de acertar/errar.
		 * É complementado por overrides em subclasses.
		 * Retorna acerto/erro ou imunidade.
		 */
	
		// Roll de acerto
		if(!TurnUtils.rollChance(this.getAccuracy()))
			return Move.moveResults.MISS;
		else {
			// Verificação de imunidades
			float typeMod = TypeChart.typeMatch(this.getTipagem(), pDef.getTipagem()[0], tchart) * 
					TypeChart.typeMatch(this.getTipagem(), pDef.getTipagem()[1], tchart);
			float error = 0.01f;
			if(Math.abs(typeMod - 0f) < error)
				return Move.moveResults.HIT_IMMUNE;
			else
				return Move.moveResults.HIT;
		}
	}
}

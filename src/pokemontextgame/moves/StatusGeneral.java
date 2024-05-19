package pokemontextgame.moves;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;

abstract class StatusGeneral extends Move {
	/*
	 * Classe para moves que alteram status fx
	 * ou stats (dano, ataque, etc.) ou o battlefield.
	 * Subclasses serão usadas para os casos em si ou casos especiais.
	 */
	final Move.moveCategs categ = Move.moveCategs.STATUS; // não pode ser mais nada
	
	//TODO: moves de HEAL
	public StatusGeneral(int id, String name, int type, int maxP, int pri, int accu) {
		super(id, name, type, maxP, pri, accu);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Retorna o mesmo que sua superclasse. Todavia, sua diferença está em ter obrigatoriamente a categ Status.
		 */
		return super.useMove(field, pAtk, pDef, tchart);
	}
	
	public Move.moveCategs getCateg(){
		return this.categ;
	}
}

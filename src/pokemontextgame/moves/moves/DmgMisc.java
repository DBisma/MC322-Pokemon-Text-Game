package pokemontextgame.moves;

public class DmgMisc extends DamageDealing {
	/*
	 * Classe mãe de todos os Moves estranhos
	 * que são tão específicos que possuem
	 * métodos próprios para calcular dano
	 * ou efeitos que não se encaixam nas 
	 * demais classes.
	 */
	
	public DmgMisc(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
}

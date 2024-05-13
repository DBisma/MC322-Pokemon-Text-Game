package moves;

import moves.DamageDealing.categs;

public class DmgPlusStatFoe extends DamageDealing {
	/*
	 * Subclasse para moves que dão dano
	 * e podem afetar o oponente com um Stat Change.
	 */
	
	public DmgPlusStatFoe(int id, String name, int type, int maxP, int pri, int accu, categs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
}

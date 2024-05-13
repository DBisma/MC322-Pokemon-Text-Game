package moves;

abstract class StatusChanging extends Move {
	/*
	 * Classe para moves que alteram status fx
	 * ou stats (dano, ataque, etc.) ou o battlefield.
	 * Subclasses serão usadas para os casos em si ou casos especiais.
	 */
	enum categs{STATUS}
	categs categ;
	
	//TODO: moves de HEAL
	public StatusChanging(int id, String name, int type, int maxP, int pri, int accu) {
		super(id, name, type, maxP, pri, accu);
		this.categ = categs.STATUS;
	}
}

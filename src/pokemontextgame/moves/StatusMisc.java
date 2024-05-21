package pokemontextgame.moves;

public abstract class StatusMisc extends StatusGeneral {
	/*
	 * Classe mãe de todos os moves de Status
	 * que são tão específicos que usarão
	 * métodos próprios para causar seus efeitos.
	 */
	
	public StatusMisc(int id, String name, int type, int maxP, int pri, int accu, String desc) {
		super(id, name, type, maxP, pri, accu, desc);
	}
}

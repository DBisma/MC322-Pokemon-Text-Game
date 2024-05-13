package moves;

public class StatChangeFoe extends StatusChanging {
	/*
	 * Classe para Moves de categ Status que
	 * causam uma mudança de Stat no oponente.
	 */
	public StatChangeFoe(int id, String name, int type, int maxP, int pri, int accu) {
		super(id, name, type, maxP, pri, accu);
	}
}

package moves;

public class StatChangeSelf extends StatusChanging {
	/*
	 * Classe para Moves de categ Status que
	 * causam uma mudança de Stat no usuário.
	 */
	public StatChangeSelf(int id, String name, int type, int maxP, int pri, int accu) {
		super(id, name, type, maxP, pri, accu);
	}
}

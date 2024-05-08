package moves;

import pokemontextgame.TypeChart;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;

public class DamageDealing extends Move {
	/*
	 * Classe para moves que causam dano.
	 * Terá subclasses para moves mais
	 * complexos com efeitos adicionais.
	 */
	public enum categs{PHYSICAL, SPECIAL}
	private categs categ;
	private int basePower;
	
	public DamageDealing(int id, String name, int type, int maxP, int pri, int accu, categs categ, int bp) {
		super(id, name, type, maxP, pri, accu);
		this.setCateg(categ);
		this.setBasePower(bp);
	}
	
	@Override
	public boolean useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Recebe o field, o atacante e o defensor.
		 * Calcula a chance de acerto. Aplica o dano se acertar; retorna true // TODO: Ou um ENUM de resultados?
		 * Como verificar se não foi muito efetivo?
		 */
		int dmg = TurnUtils.calcDmg(this, pAtk, pDef, tchart);
	}

	public categs getCateg() {
		return categ;
	}

	public void setCateg(categs categ) {
		this.categ = categ;
	}

	public int getBasePower() {
		return basePower;
	}

	public void setBasePower(int basePower) {
		this.basePower = basePower;
	}
	
	// Getters e Setters
	
}

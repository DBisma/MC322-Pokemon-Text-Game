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
		this.categ = categ;
		this.basePower = bp;
	}
	
	@Override
	public Move.returns useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Recebe o field, o atacante e o defensor.
		 * Calcula a chance de acerto. Aplica o dano se acertar; retorna true // TODO: Ou um ENUM de resultados?
		 * Como verificar se não foi muito efetivo?
		 */
		
		// Roll de precisão
		if(!TurnUtils.rollChance(this.getAccuracy()))
			return Move.returns.MISS;
		
		// Verificação de falha TODO
		
		// Modificador de dano baseado em eficácia de tipos
		float typeMod = TypeChart.typeMatch(this.getTipagem(), pDef.getTipagem()[0], tchart) * 
				TypeChart.typeMatch(this.getTipagem(), pDef.getTipagem()[1], tchart);
		// Caso de imunidade
		float error = 0.01f;
		if(Math.abs(typeMod - 0f) < error)
			return Move.returns.HIT_IMMUNE;
		// Caso contrário, cálculo e aplicação de dano
		int dmg = TurnUtils.calcDmg(this, pAtk, pDef, typeMod);
		// Comparação de floats para retornar efetividade
		if(Math.abs(typeMod - 0.5f) < error)
			return Move.returns.HIT_NOTVERY;
		else if(Math.abs(typeMod - 1f) < error)
			return Move.returns.HIT;
		else
			return Move.returns.HIT_SUPER;
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

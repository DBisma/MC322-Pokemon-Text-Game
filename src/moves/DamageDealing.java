package moves;

import pokemontextgame.TypeChart;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import moves.Move.moveCategs;
import moves.Move.moveCategs;

public class DamageDealing extends Move {
	/*
	 * Classe para moves que causam dano.
	 * Terá subclasses para moves mais
	 * complexos com efeitos adicionais.
	 */
	private Move.moveCategs categ;
	private int basePower;
	
	public DamageDealing(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu);
		this.categ = categ;
		this.basePower = bp;
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Recebe o field, o atacante e o defensor.
		 * Calcula a chance de acerto. Aplica o dano se acertar; retorna true // TODO: Ou um ENUM de resultados?
		 * Como verificar se não foi muito efetivo?
		 */
		
		// Roll de precisão
		if(!TurnUtils.rollChance(this.getAccuracy()))
			return Move.moveResults.MISS;
		
		// Verificação de falha TODO
		
		// Modificador de dano baseado em eficácia de tipos
		float typeMod = TypeChart.typeMatch(this.getTipagem(), pDef.getTipagem()[0], tchart) * 
				TypeChart.typeMatch(this.getTipagem(), pDef.getTipagem()[1], tchart);
		// Caso de imunidade
		float error = 0.01f;
		if(Math.abs(typeMod - 0f) < error)
			return Move.moveResults.HIT_IMMUNE;
		// Caso contrário, cálculo e aplicação de dano
		int dmg = TurnUtils.calcDmg(this, pAtk, pDef, typeMod);
		pDef.dmgMon(dmg);
		// TODO: Verificar se pDef tem alguma habilidade interessante que afeta o dano. Mais pra frente.
		// Comparação de floats para retornar efetividade
		if(Math.abs(typeMod - 0.5f) < error)
			return Move.moveResults.HIT_NOTVERY;
		else if(Math.abs(typeMod - 1f) < error)
			return Move.moveResults.HIT;
		else
			return Move.moveResults.HIT_SUPER;
	}

	public Move.moveCategs getCateg() {
		return categ;
	}

	public void setCateg(Move.moveCategs categ) {
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

package moves.DmgMiscPackage;

import moves.Move;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;
import moves.DmgMisc;

public class GrassKnot extends DmgMisc {
	/*
	 * Move de Grama que dá dano baseado no peso.
	 * É semelhante a Low Kick, mas com só dois moves desse comportamento,
	 * não compensa juntar os dois numa superclasse.
	 */
	
	public GrassKnot(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		super.useMove(field, pAtk, pDef, tchart);
		// Modificando Base Power baseando-se em peso do inimigo
		int foeW = pDef.getBaseWeight();
		// 0.1 a 9.9 kg
		if(1 < foeW && foeW < 9999) {
			this.basePower = 20;
		}
		// 10.0 a 24.9 kg
		else if(10000 < foeW && foeW < 24999) {
			this.basePower = 40;
		}
		// 25.0 a 49.9 kg
		else if(25000 < foeW && foeW < 49999) {
			this.basePower = 60;
		}
		// 50.0 a 99.9 kg
		else if(50000 < foeW && foeW < 99999) {
			this.basePower = 80;
		}
		// 100.0 a 199.9 kg
		else if(100000 < foeW && foeW < 199999) {
			this.basePower = 100;
		}
		// 200.0 e além
		else{
			this.basePower = 120;
		}
		
		// Roll de precisão; inclui evasiveness e accuracy dos dois pokemons em questão
		if(!TurnUtils.doesItHit(pAtk, pDef, this, field)) {
			field.textBufferAdd("Mas " + pAtk.getName()  + " errou!\n");
			return Move.moveResults.MISS;
		}
		// Por enquanto, moves não podem falhar, apenas errar.
		// Modificador de dano baseado em eficácia de tipos
		float typeMod = tchart.compoundTypeMatch(this.type, pDef);
		// Caso de imunidade
		float error = 0.01f;
		if(typeMod < error) {
			field.textBufferAdd("Mas não afetou " + pDef.getName()  + " !\n");
			return Move.moveResults.HIT_IMMUNE;
		}
		// Caso contrário, cálculo e aplicação de dano
		int dmg = TurnUtils.calcDmg(this, pAtk, pDef, typeMod);
		pDef.dmgMon(dmg);
		
		// Retornando ao usual
		this.basePower = 20;
		// TODO: Verificar se pDef tem alguma habilidade interessante que afeta o dano. Mais pra frente.
		// Comparação de floats para retornar efetividade
		if(Math.abs(typeMod - 0.5f) < error) {
			field.textBufferAdd("Não foi muito eficaz...\n");
			return Move.moveResults.HIT_NOTVERY;
		}
		else if(Math.abs(typeMod - 1f) < error) {
			return Move.moveResults.HIT;
		}
		else {
			field.textBufferAdd("Foi super eficaz!\n");
			return Move.moveResults.HIT_SUPER;
		}
	}
}
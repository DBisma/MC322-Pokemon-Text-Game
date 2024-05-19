package moves.DmgMiscPackage;

import moves.Move;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.StatusFx;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;

import java.util.function.Function;

import moves.DmgMisc;

public class GyroBall extends DmgMisc {
	/*
	 * Gyro Ball dá dano dependendo da velocidade relativa
	 * do pokemon atual em relação à do inimigo.
	 */
	
	public GyroBall(int id, String name, int type, int maxP, int pri, int accu, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, categ, bp);
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		
		this.spendPp();
		field.textBufferAdd(pAtk.getName()  + " utilizou " + this.name + "!\n");
		
		// Modificando Base Power baseando-se na diferença de peso
		Function<Poke, Float> paralysisSlowdown = (mon) -> {
			if(mon.getStatusFx().getType() == StatusFx.typeList.PARALYSIS)
				return 0.75f;
			else
				return 1f;
		};
		float currentUserSpeed = TurnUtils.getModStat(4, pAtk)*paralysisSlowdown.apply(pAtk);
		float currentFoeSpeed = TurnUtils.getModStat(4, pDef)*paralysisSlowdown.apply(pDef);
		int oldBasePower = this.basePower;
		this.basePower = Math.min(150, (int) (25*currentUserSpeed/currentFoeSpeed + 1));
		
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
		this.basePower = oldBasePower;
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
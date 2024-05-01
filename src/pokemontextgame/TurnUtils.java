package pokemontextgame;

import java.util.Random;

final class TurnUtils{
	/*
	 * Classe que armazena funções úteis para progressão de turno.
	 * Envolve cálculo de dano, modificação de stats, etc.
	 */
	
	static boolean rollChance(int chance) {
		/*
		 * Função geral de saída aleatória.
		 * Recebe uma probabilidade 0-100 e retorna sua saída
		 */
		Random r = new Random();
		int roll = r.nextInt(100);
		if(chance > roll)
			return true;
		else
			return false;
	}
	
	static int calcDmg(Move move, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Por enquanto, recebe dois pokemons e o ID do ataque.
		 * Calcula o dano dado pelo pokemon atacante contra o defensor.
		 * Utiliza stats de danos, defesa, nível, efeitos, etc. para calcular.
		 */
		
		// Parte Base ou Necessária
		// TODO: Fazer cópias de dados com esses nomes é boa prática? Como preservar legibilidade?
		// Calculando atuais ataque e defesa dos pokemons em jogo
		
		// TODO: Verificar se usaremos Def ou SpecDec, Ataque ou Spec Attack.
		int atk, def;
		if(move.getCateg() == 0) {
			atk = TurnUtils.modStat(0, pAtk); // Attack
			def = TurnUtils.modStat(1, pDef); // Defense
		}
		else if(move.getCateg() == 1) {
			atk = TurnUtils.modStat(2, pAtk); // Special Attack
			def = TurnUtils.modStat(3, pDef); // Special Defense
		}
		else // ataque é status apenas TODO: Modificar nossa rota de calcular dano para algo mais geral
			return 1;

		int lv = pDef.getLevel();
		Move curMove = pAtk.getMoves()[move.getId()];
		int power = curMove.getPower();
		int type = curMove.getTipagem();
		float output;
		
		// Cálculo do dano em si. Fonte: https://bulbapedia.bulbagarden.net/wiki/Damage#Generation_V_onward
		output = (((2*lv)/5 + 2)*power*(atk/def))/50 + 2;
		
		// Parte Modificadora ou Contingente			
		// STAB: Same Attack Type Bonus
		if(type == pAtk.getTipagem()[0] || type == pAtk.getTipagem()[1])
			output *= 1.1; 
			// TODO: Verificar arrendondamentos. 
			// Aqui estamos multiplicando por um float, mas o dano é um int.
		
		// Verificação de super efetivo / pouco efetivo para os dois tipos do defensor
		output *= (TypeChart.typeMatch(move.getTipagem(), pDef.getTipagem()[0], tchart)
				* TypeChart.typeMatch(move.getTipagem(), pDef.getTipagem()[1], tchart));
		
		// TODO: Puxar os modificadores de Weather, Item Segurado, etc. e incluir na fórmula.
		// Isso faremos mais tarde. Talvez valha a pena ter uma tabela de Weather.
	
		// Verificar Status Burn
		if(Status.burnHalving(pAtk, curMove))
			output *= 0.5;
		
		// Arredondar output antes de saída
		return (int) Math.floor(output);
	}
	
	static int modStat(int statId, Poke mon) {
		/*
		 * Calcula a modificação do Stat de um pokemon
		 * baseado nos boost stages e no Id do stat em questão.
		 * Nem todos os stats possuem boosts calculados da mesma forma.
		 * Importante: Retorna o stat já modificado, e não o fator.
		 */
		
		// componentes de um fator multiplicativo
		int num; // cresce caso boost > 0
		int denom; // cresce caso boost < 0
		int boost = mon.getStatModGeneral(statId);
		
		// Caso Atk (0), Def (1), SpecAtk (2), SpecDef (3), Speed (4)
		if(statId < 5) {
			num = denom = 2;
			// +
			if(boost >= 0) {
				num += boost;
			}
			// -
			else {
				denom += Math.abs(boost);
			}
		}
		// Caso Accuracy (5), Evasion (6), Weight (7)
		else {
			num = denom = 3;
			// +
			if(boost >= 0) {
				num += boost;
			}
			// -
			else {
				denom += Math.abs(boost);
			}
		}
		// Devemos colocar limites sobre o output também.
		
		int output = (int) mon.getStatBasicGeneral(statId) * (num/denom);
		return output; //TODO: Novamente, dar esses "nomezinhos" é uma boa?
	}
}
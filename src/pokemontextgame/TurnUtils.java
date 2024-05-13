package pokemontextgame;

import java.util.Random;

import moves.Move;
import moves.DamageDealing.categs;
import moves.DamageDealing;
import pokemontextgame.Battlefield.Choice;
import pokemontextgame.Battlefield.Choice.choiceType;

public class TurnUtils{
	/*
	 * Classe que armazena funções úteis para progressão de turno.
	 * Envolve cálculo de dano, modificação de stats, etc.
	 */
	
	public static boolean rollChance(int chance) {
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
	
	public static int calcDmg(DamageDealing move, Poke pAtk, Poke pDef, float typeMod) {
		/*
		 * Por enquanto, recebe dois pokemons e o ID do ataque.
		 * Calcula o dano dado pelo pokemon atacante contra o defensor.
		 * Utiliza stats de danos, defesa, nível, efeitos, etc. para calcular.
		 */
		
		// Parte Base ou Necessária
		// TODO: Fazer cópias de dados com esses nomes é boa prática? Como preservar legibilidade?
		// Calculando atuais ataque e defesa dos pokemons em jogo
		
		// TODO: Verificar se usaremos Def ou SpecDec, Ataque ou Spec Attack.
		// TODO: Talvez colocar isso diretamente no argumento de entrada?
		//DamageDealing move = (DamageDealing) pAtk.getMove(id); // TODO: Isso talvez seja desnecessário no futuro
		int atk, def;
		if(move.getCateg() == DamageDealing.categs.PHYSICAL) {
			atk = TurnUtils.modStat(0, pAtk); // Attack
			def = TurnUtils.modStat(1, pDef); // Defense
		}
		else if(move.getCateg() == DamageDealing.categs.SPECIAL) {
			atk = TurnUtils.modStat(2, pAtk); // Special Attack
			def = TurnUtils.modStat(3, pDef); // Special Defense
		}
		else // ataque é status apenas TODO: Modificar nossa rota de calcular dano para algo mais geral
			return 0;

		int lv = pDef.getLevel();
		int power = move.getBasePower();
		int type = move.getTipagem();
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
		output *= typeMod;
		
		// TODO: Puxar os modificadores de Weather, Item Segurado, etc. e incluir na fórmula.
		// Isso faremos mais tarde. Talvez valha a pena ter uma tabela de Weather.
		// Verificar status como burn etc. mais tarde no cálculo de dano
		
		// Arredondar output antes de saída
		return (int) Math.floor(output);
	}
	
	public static int modStat(int statId, Poke mon) {
		/*
		 * Calcula a modificação do Stat de um pokemon
		 * baseado nos boost stages e no Id do stat em questão.
		 * Faz uso da func. Stat Calc presente na classe Pokemon
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
		int output = (int) mon.statCalc(statId) * (num/denom);
		return output; //TODO: Novamente, dar esses "nomezinhos" é uma boa?
	}

	public static int getBestMoveId() {
		/*
		 * Função que retorna o ID do ataque mais eficiente.
		 * Se não for eficiente o suficiente, retorna -1.
		 */
		return 0;
	}
	
	public static int getBestDefensiveSwitch(Battlefield field) {
		/*
		 * Retorna o ID do melhor pokemon para
		 * trocar tendo em mente estratégias defensivas.
		 */
		// Verificar se a última decisão do oponente foi algum move
		if(field.getPlayerChoice().getType() == Choice.choiceType.ATTACK) {
			// Obter o move
			Move lastMove = field.getLoadedPlayer().getActiveMon().getMove(field.getPlayerChoice().getId());
			// Se foi move de dano, deve ter sido o melhor possível; tendência é repetir
			// Se foi move de status, assume que oponente usará ataque com STAB (dois possíveis)
			// Busca no time pokemon que receberia menor dano e responderia com melhor dano
			// Se a troca for opcional e não houver pokemon bom o suficiente para justificar a troca, retorna -1
			// Se a troca for forçada, escolhe aleatoriamente
			return 0;
		}
		// Se não foi usado move, não troca o pokemon
		else
			return -1;
	}
	
	public static int getBestOffensiveSwitch(Battlefield field) {
		/*
		 * Retorna o ID do melhor pokemon para
		 * trocar tendo em mente estratégias ofensivas.
		 */
		return 0;
	}
}
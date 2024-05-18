package pokemontextgame;

import java.util.Random;

import moves.*;
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
		int atk, def;
		if(move.getCateg() == Move.moveCategs.PHYSICAL) {
			atk = TurnUtils.getModStat(0, pAtk); // Attack
			def = TurnUtils.getModStat(1, pDef); // Defense
		}
		else if(move.getCateg() == Move.moveCategs.SPECIAL) {
			atk = TurnUtils.getModStat(2, pAtk); // Special Attack
			def = TurnUtils.getModStat(3, pDef); // Special Defense
		}
		else // ataque é status apenas TODO: Modificar nossa rota de calcular dano para algo mais geral
			return 0;

		int lv = pAtk.getLevel();
		int power = move.getBasePower();
		int type = move.getTipagem();
		double startingDmg;
		float modifiers = 1f;
		
		// Cálculo do dano em si. Fonte: https://bulbapedia.bulbagarden.net/wiki/Damage#Generation_V_onward
		startingDmg = (((2*lv)/5f + 2)*power*((float)atk/def))/50 + 2;
		
		// Parte Modificadora ou Contingente			
		// STAB: Same Attack Type Bonus
		if(type == pAtk.getTipagem()[0] || type == pAtk.getTipagem()[1])
			modifiers *= 1.1; 
			// TODO: Verificar arrendondamentos. 
			// Aqui estamos multiplicando por um float, mas o dano é um int.
		
		// Verificação de super efetivo / pouco efetivo para os dois tipos do defensor
		modifiers *= typeMod;
		
		// TODO: Puxar os modificadores de Weather, Item Segurado, etc. e incluir na fórmula.
		// Isso faremos mais tarde. Talvez valha a pena ter uma tabela de Weather.
		// Verificar status como burn etc. mais tarde no cálculo de dano
		
		// Redução de dano física por Burning
		if(move.getCateg() == Move.moveCategs.PHYSICAL && pAtk.getStatusFx().getType() == StatusFx.typeList.BURN) {
			modifiers *= 0.5f;
		}
		
		// Arredondar output antes de saída
		return (int) Math.floor(startingDmg*modifiers);
	}
	
	public static int getModStat(int statId, Poke mon) {
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
		// Devemos colocar limites sobre o output também. TODO
		int output = (int)(mon.statCalc(statId) * ((float) num/denom));
		return output;
	}

	public static boolean doesItHit(Poke pAtk, Poke pDef, Move move, Battlefield field) {
		/*
		 * Calcula modificador da chance de 
		 * um move do atacante atingir
		 * o pokemon defensor.
		 * Usa para determinar se o move acerta.
		 * Retorna true se sim, false caso contrário.
		 * Ler: https://bulbapedia.bulbagarden.net/wiki/Stat_modifier#Stage_multipliers
		 */
		
		// TODO: Só está errando... hum...
		// Sempre acerta
		if(move.getAccuracy() < 0)
			return true;
		
		// Calculando modificador de precisão
		int pAtkAccu = pAtk.getModAccuracy();
		int pDefEvasion = pDef.getModEvasion();
		int boostLimited = pAtkAccu - pDefEvasion;
		if(Math.abs(boostLimited) > 6) {
			boostLimited = 6*(boostLimited/Math.abs(boostLimited)); // 6 com mesmo sinal de boostLimited
		}
		
		int denom = 3, num = 3;
		if(boostLimited > 0) {
			num += boostLimited;
		}
		else if(boostLimited < 0) {
			denom += boostLimited;
		}
		float coef = (float) num / denom;
		int chance = (int) (move.getAccuracy()*coef);
		return TurnUtils.rollChance(chance);
	}
	
	public static String getStatName(int id) {
		/* 
		 * Retorna o nome do Stat de id correspondente.
		 */
		switch(id) {
		case 0: return "ATK.";
			case 1: return "DEF. ";
			case 2: return "SPEC. ATK.";
			case 3: return "SPEC. DEF.";
			case 4: return "SPEED";
			case 5: return "WEIGHT";
			case 6: return "EVASION";
			case 7: return "ACCURACY";
		}
		return "Erro.";
	}
	
	public static String renderTextLifeBar(Poke mon) {
		/*
		 * Uma função visual que cria uma pequena
		 * barrinha de vida para display nos menus.
		 * Basea-se em quanto de vida o pokemon tem.
		 */
		String lifeBar = "";
		// Encontrando a porcentagem de vida do Pokemon
		int lifePercentage = Math.round((100*((float)mon.getCurHp()/mon.getMaxHp())));
		// Arredondando para um múltiplo de 10 e convertendo em número de 0 a 10
		lifePercentage = ((lifePercentage / 10)*10)/10;
		int i;
		for(i = 0; i < lifePercentage; i++) {
			lifeBar += "█";
		}
		for(i = 0; i < 10 - lifePercentage; i++) {
			lifeBar += "░";
		}
		
		return lifeBar;
	}
}
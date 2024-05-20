package pokemontextgame;

import java.util.Random;

import pokemontextgame.moves.*;

public final class TurnUtils{
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
		else
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
		
		// Verificação de super efetivo / pouco efetivo para os dois tipos do defensor
		modifiers *= typeMod;
		
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
		case 0: return "ATK";
			case 1: return "DEF ";
			case 2: return "SPEC. ATK";
			case 3: return "SPEC. DEF";
			case 4: return "SPEED";
			case 5: return "WEIGHT";
			case 6: return "EVASION";
			case 7: return "ACCURACY";
		}
		return "Erro.";
	}
	
	public static boolean blockMoveCheck(Poke mon, Battlefield field) {
		/*
		 * Recebe um Pokemon.
		 * Verifica se ele pode mover-se nesse turno.
		 * Caso contrário, retorna false e envia
		 * uma mensagem ao text buffer de um field.
		 */
		StatusFx.typeList statusfx = mon.getStatusFx().getType();
		statusfx = StatusFx.typeList.NEUTRAL;
		String monName = mon.getName();
		switch(statusfx){
			case StatusFx.typeList.SLEEP:{
				field.textBufferAdd(monName + " dorme como uma pedra!\n");
				return true;
			}
			case StatusFx.typeList.FREEZE:{
				field.textBufferAdd(monName + " está congelado!\n");
				return true;
			}
			case StatusFx.typeList.PARALYSIS:{
				field.textBufferAdd(monName + " está paralisado! Neste turno, falhou em se mover!\n");
				if(TurnUtils.rollChance(25)) {
					
				}
				else {
					return false;
				}
			}
			default:{
				return false;
			}
		}
	}
	
	public static void statusFxDmg(Poke mon, Battlefield field) {
		/*
		 * Recebe um Pokemon.
		 * Verifica se ele tomará dano por stats nesse turno.
		 * Neste caso, calcula o dano e envia 
		 * uma mensagem ao text buffer de um field.
		 */
		StatusFx.typeList statusfx = mon.getStatusFx().getType();
		String monName = mon.getName();
		switch(statusfx){
			case StatusFx.typeList.BURN:{
				// Danifica 1/16 da vida máxima por turno
				mon.dmgMon((int) (mon.getMaxHp()*0.0625f));
				field.textBufferAdd(monName + " toma dano com a queimadura!\n");
				break;
			}
			case StatusFx.typeList.POISON:{
				// Danifica 1/16 da vida máxima por turno
				mon.dmgMon((int) (mon.getMaxHp()*0.0625f));
				field.textBufferAdd(monName + " toma dano com veneno!\n");
				break;
			}
			case StatusFx.typeList.BAD_POISON:{
				mon.dmgMon((int) (mon.getMaxHp()*0.0625f*mon.getStatusFx().getTimeAfflicted()));
				// Danifica 1/16 da vida máxima vezes quantidade de turnos envenenado.
				// Contador reinicia na troca de pokemons
				field.textBufferAdd(monName + " toma dano com o potente veneno!\n");
				break;
			}
			default:{
				break;
			}
		}
	}

}
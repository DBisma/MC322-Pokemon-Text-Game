package pokemontextgame;

import moves.*;
import moves.Move.moveCategs;
import pokemontextgame.Battlefield.Choice;
import pokemontextgame.Battlefield.Choice.choiceType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TreinadorNpc extends Treinador{
	/*
	 * Classe que lida com o treinador especial NPC.
	 * Quase todos seus métodos são de tomada de sua decisão.
	 * Poderá possuir alguns coeficientes de dificuldade. TODO
	 */
	public TreinadorNpc(int id, String nome, boolean player) {
		super(id, nome, player);
	}
	
	public static Choice npcThink(Battlefield field) {
		/*
		 * Função de placeholder para processo de decisão do NPC.
		 * Possui uma chance de tomar a decisão otimizada
		 * e um processo para determinar a melhor decisão.
		 * Retorna uma decisão para o NPC no battlefield.
		 * Mais explicação no corpo do texto.
		 * O NPC não pode correr e não possui uma bag própria para utilizar items.
		 * TODO: Implementar pequena bag para que possa usar items?
		 * TODO: Remover items de uma vez por todas da bag para que o NPC não tenha que espelhar o jogador?
		 */
		
		// Macros
		TreinadorNpc npc = field.getLoadedNpc();
		Poke mon = npc.getActiveMon();
		Poke foe = field.getLoadedPlayer().getActiveMon();
		Choice npcChoice = field.getNpcChoice();
		int bestDefSwitchId = TreinadorNpc.getBestDefensiveTypeSwitch(field);
		int bestOffensiveSwitchId = TreinadorNpc.getBestOffensiveTypeSwitch(field);
		int bestDmgMoveId = TreinadorNpc.getBestDamageMoveId(field);

		// Determinar a troca aleatória; se for -1, sabemos que é impossível trocar nesse turno
		int rSwitch = TreinadorNpc.getRandomPossibleSwitch(field);
		
		// Verificar se pokemon ativo está fainted ou forced switch
		if((mon.isFainted() || npc.isForcedSwitch()) && rSwitch != -1) {
			// Se sim, trocar para melhor ofensivo
			npcChoice.setFullChoice(choiceType.SWITCH, bestOffensiveSwitchId);
		}
		// Se está vivo e não forçado a trocar
		else {
			// Checar vida; se for mais que metade, prioriza moves
			if(mon.getCurHp() > (int) mon.getMaxHp()*0.5f) {
				// Verifica se o próprio possui BOOST de Ataque ou Spec. Atk
				if(mon.getModSpAtk() > 0 || mon.getModAtk() > 0) {
					// Se sim, usa um move de dano;
					npcChoice.setFullChoice(choiceType.MOVE, bestDmgMoveId);
				}
				// Se não, verifica se possui BOOST de DEFESA ou Spec. Def
				else if(mon.getModDef() > 0 || mon.getModSpDef() > 0) {
					// Se sim, verificar se:
					int tryStatusFxIndex = TreinadorNpc.getRandomMoveIdFromSelectedClass(field, StatusChangeFx.class);
					if(!foe.isStatusedFx() // inimigo não está afetado por statusfx;
								&& tryStatusFxIndex > 0 // possuímos um ataque desse tipo para utilizar;
								&& !TreinadorNpc.isEnemyImmune(mon.getMove(tryStatusFxIndex), foe, field)) { // não há imunidade
						// se sim, tentamos um Status move
						npcChoice.setFullChoice(choiceType.MOVE, tryStatusFxIndex);
					}
					// Se um status não for conveniente ou possível, tentamos melhor move
					else {
						npcChoice.setFullChoice(choiceType.MOVE, bestDmgMoveId);
					}
				}
				// Se não possuir nenhum buff (Perceba que o NPC ignora debuffs se acreditar possuir buffs o suficiente)
				else {
					// Verifica se está sob grandes debuffs
					int i = 0;
					int debuffCount = 0;
					for(i = 0; i < 0; i++) {
						int currentStatModifier =  mon.getStatModArray()[i];
						if(currentStatModifier < 0)
							debuffCount += currentStatModifier;
					}
					// Se estiver, approx 2/3 de chance de tentar trocar de cara para limpar debuffs
					if(debuffCount >= 2 && TurnUtils.rollChance(66) && rSwitch != -1) {
						// para o melhor em defesa
						if(bestDefSwitchId != 0) {
							npcChoice.setFullChoice(choiceType.SWITCH, bestDefSwitchId);
						}
						// se o "melhor" em tipo for o atual, trocar para o melhor em ataque
						else if(bestOffensiveSwitchId != 0) {
							npcChoice.setFullChoice(choiceType.SWITCH, bestOffensiveSwitchId);
						}
						// se também for o atual, trocar para um aleatório
						else
							npcChoice.setFullChoice(choiceType.SWITCH, rSwitch);
					}
					// Caso contrário, checa se ainda trocar e se seus moves são ruins; se sim, troca
					else if(bestDmgMoveId < 0 && rSwitch != -1) { // se o iD for negativo, é o "melhor", mas ainda é ruim
						// 33% (1/3) de chance de usar o move sub-ótimo; 67% (2/3) de trocar
						if(TurnUtils.rollChance(33)) {
							npcChoice.setFullChoice(choiceType.MOVE, bestDmgMoveId);
						}
						// Buscando troca
						else {
							int switchIndex = bestDefSwitchId;
							// se o melhor em defesa já for o atual, trocar para outro com melhores ataques
							if(switchIndex == 0) {
								switchIndex = bestOffensiveSwitchId;
								// se ainda assim não resolver, usar o melhor ataque possível
								if(switchIndex == 0) {
									// TODO: Novamente, dar um jeito de ler struggle como MOVE de id 5... quem sabe?
									npcChoice.setFullChoice(choiceType.MOVE, bestDmgMoveId);
								}
							}
						}
					}
					// Se não puder trocar, decide em chances:
					else {
						// 40% de chance de tentar um buff ou debuff
						int lastResortId = TreinadorNpc.getRandomMoveIdFromSelectedClass(field, StatChange.class);
						if(lastResortId > -1 && TurnUtils.rollChance(40)) { //lRId > -1 significa qu o move foi encontrado e tem PP
							npcChoice.setFullChoice(choiceType.MOVE, lastResortId);
						}
						// (100% - 40%) * 50% = 30% total de tentar um StatusFx
						else{
							lastResortId = TreinadorNpc.getRandomMoveIdFromSelectedClass(field, StatusChangeFx.class);
							if(lastResortId > -1 && !foe.isStatusedFx() &&TurnUtils.rollChance(50)){
								npcChoice.setFullChoice(choiceType.MOVE, lastResortId);
							}
							// idem 30% de tentar o melhor ataque, mesmo se for sub-otimizado (multiplicado por -1)
							else {
								npcChoice.setFullChoice(choiceType.MOVE, bestDmgMoveId);
							}
						}
					}
				}
			}
			// Caso a vida seja menor que a metade, prioriza trocas
			else {
				if(rSwitch != -1) {
					// Se houver poke melhor para defesa, troca para esse
					int trySwitch = bestDefSwitchId;
					if(trySwitch != 0) {
						npcChoice.setFullChoice(choiceType.SWITCH, trySwitch);
					}
					// Se não, busca melhor poke para ataque
					else {
						trySwitch = bestOffensiveSwitchId;
						if(trySwitch != 0) {
							npcChoice.setFullChoice(choiceType.SWITCH, trySwitch);
						}
						// Se não houver nenhuma troca melhor, usa o melhor move que puder até a morte
						else {
							npcChoice.setFullChoice(choiceType.MOVE, bestDmgMoveId);
						}
					}
				}
				// Se for o último que sobrou, novamente usa o melhor move que puder
				else {
					npcChoice.setFullChoice(choiceType.MOVE, bestDmgMoveId);
				}
			}
		}
		return npcChoice;
	}
	
	public static int getRandomMoveIdFromSelectedClass(Battlefield field, Class<?> selClass) {
		/*
		 * Essa função cujo nome é um pesadelo autoexplicativo (e funcional) serve para
		 * retornar o id de um move que seja da instância de uma classe desejada.
		 * Por exemplo, podemos enviar a classe "StatChange" como parâmetro
		 * e a função retornará o ID de algum move "StatChange" no moveset.
		 * Se não houver, retorna -4.
		 * Se não existir PP mais no total, retorna -5;
		 */
		
		int i = 0;
		boolean struggle = true;
		for(i = 0; i < 4; i++) {
			Move curMove = field.getLoadedNpc().getActiveMon().getMoveset()[i];
			if(curMove != null && curMove.getPoints() > 0) { // Encontrou algum move com PP
				struggle = false;
				if(curMove.getClass().equals(selClass)) // Encontrou move com PP e da classe desejada
					return i;
			}
		}
		if(struggle)
			return -5; // Não há PP total
		else
			return -1; // Há PP, mas não para o move desejado, ou o move desejado não existe
		
		// OBS: Se esse código quebrar, podemos tentar utilizar o seguinte:
		// public static int getRandomMoveIdFromSelectedSubclass(Battlefield field, Object selObj)
		// if(curMove != null && curMove.getClass().equals(selObj.getClass())) return i;
	}
	
	public static boolean isEnemyImmune(Move move, Poke defMon, Battlefield field) {
		/*
		 * Função que recebe um Move
		 * e um inimigo e verifica imunidade.
		 */
		TypeChart tchart = field.getTchart();
		float error = 0.001f;
		if(Math.abs(tchart.compoundTypeMatch(move.getTipagem(), defMon) - 0f) < error){
			return true;
		}
		else
			return false;
	}
	
	public static int getBestDamageMoveId(Battlefield field) {
		/*
		 * Função que retorna o ID do Move mais eficiente.
		 * Se não achar eficiente o suficiente, retorna -(id).
		 */
		Poke monSelf = field.getLoadedPlayer().getActiveMon();
		Poke monFoe = field.getLoadedNpc().getActiveMon();
		int i = 0;
		int index = 0; 
		float bestF = 0f;
		float curF = 0f;
		boolean struggle = true; // flag para caso não haja PP nenhum
		
		for(i = 0; i < 4; i++) {
			Move curMove = monSelf.getMove(i);
			// Verificar se é null, se é damaging e se há pp
			if(curMove != null && curMove.getCateg() != Move.moveCategs.STATUS && curMove.getPoints() > 0) {
				// Se não houver PP em NENHUM move, retornar 5 (id do Struggle);
				struggle = false;
				curF = field.getTchart().typeMatch(curMove.getTipagem(), monFoe.getTipagem()[0])*
						field.getTchart().typeMatch(curMove.getTipagem(), monFoe.getTipagem()[1]);
				if(curF > bestF) {
					index = i;
					curF = bestF;
				}
			}
		}
		if(struggle)
			return 5;
		// Tendo achado o melhor da dano, verifica se é lá essas coisas
		// Retorna -1*index se for sub-otimizado
		float error = 0.001f;
		if(Math.abs(bestF - 1f) < error || bestF > 1f)
			return index;
		else
			return index*(-1);
	}
	
	public static int getRandomPossibleSwitch(Battlefield field) {
		/*
		 * Escolhe um pokemon aleatório para a troca.
		 * Retorna seu índice, ou -1 se a troca é impossível.
		 */
		int i;
		// TODO: int vs Integer, qual a diferença?
		List<Integer> monlist = new ArrayList<>();
		// Pula o mon ativo
		for(i = 1; i < 6; i++) {
			Poke mon = field.getLoadedNpc().getTeam()[i];
			if(mon != null && !mon.isFainted()) {
				monlist.add(i);
			}
		}
		// Se não houver algum disponível que não seja o ativo
		if(monlist.size() == 0)
			return -1;
		// Caso contrário, retorna aleatório dentro da lista
		else
			return monlist.get(ThreadLocalRandom.current().nextInt(0, monlist.size() + 1));
	}

	public static int getBestDefensiveTypeSwitch(Battlefield field) {
		/*
		 * Retorna o ID do melhor pokemon para
		 * trocar tendo em mente estratégias defensivas
		 * que consideram apenas a tipagem do pokemon em troca.
		 * Se o retorno for 0, o melhor pokemon já é o ativo,
		 * e a troca é desnecessária.
		 */
		TypeChart tchart = field.getTchart();
		// Verificar se a última decisão do oponente foi algum move
		if(field.getPlayerChoice().getType() == Choice.choiceType.MOVE) {
			// Macros
			Poke foeMon = field.getLoadedPlayer().getActiveMon();
			Move lastMove = field.getLoadedPlayer().getActiveMon().getMove(field.getPlayerChoice().getId());
			// Se foi move de status
			if(lastMove.getCateg() == moveCategs.STATUS) {
				// assume que oponente usará ataque com STAB (dois possíveis)
				int foeType1 = foeMon.getTipagem()[0];
				int foeType2 = foeMon.getTipagem()[1];
				// Caso Monotipo
				if(foeType2 == -1) {
					// retorna o poke mais resistente ao STAB inimigo
					return TreinadorNpc.getMostResistantMon(field, foeType1);
				}
				// Caso Dois Tipos
				else {
					// Busca dois pokes com resistência aos dois STABS diferentes;
					int resMon1 = TreinadorNpc.getMostResistantMon(field, foeType1);
					int resMon2 = TreinadorNpc.getMostResistantMon(field, foeType2);
					// Se forem o mesmo pokemon, retorna ele
					if(resMon1 == resMon2)
						return resMon1;
					// Caso contrário, escolhe um dos dois aleatoriamente
					else {
						if(TurnUtils.rollChance(50))
							return resMon1;
						else
							return resMon2;
					}
				}
			}
			// Caso inimigo já tenha usado um move de dano, deve ter sido o melhor; tendência é repetí-lo
			else {
				// Verificar se esse move é grande ameaça para o poke atual
				int moveType = lastMove.getTipagem();
				int ownType1 = field.getLoadedNpc().getActiveMon().getTipagem()[0];
				int ownType2 = field.getLoadedNpc().getActiveMon().getTipagem()[1];
				float safety = tchart.typeMatch(moveType, ownType1) * tchart.typeMatch(moveType, ownType2);
				float error = 0.001f;
				// Se não for grande perigo, mantem o mesmo
				if(Math.abs(safety - 1f) < error || safety > 1f)
					return 0;
				// Se for, retorna o mais resistente a ele, que pode inclusive ser o próprio
				else
					return TreinadorNpc.getMostResistantMon(field, moveType);
			}
		}
		// Caso não tenha usado move ainda, nem troca de pokemon
		else 
			return 0;
	}
	
	public static int getBestOffensiveTypeSwitch(Battlefield field) {
		/*
		 * Retorna o ID do melhor pokemon para
		 * trocar tendo em mente estratégias ofensivas.
		 */
		Poke foeMon = field.getLoadedPlayer().getActiveMon();
		int foeType1 = foeMon.getTipagem()[0];
		int foeType2 = foeMon.getTipagem()[1];
		// Achar pokemon na party com o melhor de DANO (pensando apenas em tipo) contra o inimigo
		int i, j;
		int index = 0;
		float bestF = 0f;
		float currentF = 0f;
		// Iterando sobre o time para escolher pokes
		for(i = 0; i < 6; i++) {
			Poke curMon = field.getLoadedNpc().getTeam()[i];
			if(curMon != null && !curMon.isFainted()) {
				// Iterando poke para escolher moves
				for(j = 0; j < 4; j++) {
					Move curMove = curMon.getMove(j);
					if(curMove != null && curMove.getPoints() > 0) { // o move deve ter PP naturalmente
						currentF = field.getTchart().typeMatch(curMove.getTipagem(), foeType1) * 
								field.getTchart().typeMatch(curMove.getTipagem(), foeType2);
						if(currentF > bestF) {
							index = i;
							currentF = bestF;
						}
					}
				}
			}
		}
		return index;
	}
	
	public static int getMostResistantMon(Battlefield field, int atkType) {
		/*
		 * Essa função de nome infeliz serve para
		 * encontrar o pokemon dentro de um time
		 * com a melhor resistência a um determinado tipo.
		 * Retorna o Index desse pokemon no time.
		 */
		Poke mon;
		int index = 0, i = 0;
		float currentF = 1f, lowestF = 1f;
		TypeChart tchart = field.getTchart();
		for(i = 0; i < 6; i++) {
			mon = field.getLoadedNpc().getTeam()[i];
			if(!mon.isFainted()) {
				// Se for monotipo, segunda chamada de typeMatch retorna 1 e não altera o cálculo de resistência
				currentF = tchart.typeMatch(atkType, mon.getTipagem()[0]) * tchart.typeMatch(atkType, mon.getTipagem()[1]);
				if(currentF < lowestF) {
					lowestF = currentF;
					index = i;
				}
			}
		}
		// Se a menor resistência é 1 ou mais, nem gasta turno trocando
		float error = 0.001f;
		if(Math.abs(lowestF - 1f) < error || lowestF > 1f)
			return 0;
		else
			return index;
	}
}

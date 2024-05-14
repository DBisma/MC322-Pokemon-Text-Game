package pokemontextgame;

import moves.*;
import moves.Move;
import moves.Move.moveCategs;
import pokemontextgame.Battlefield.Choice;
import pokemontextgame.Battlefield.Choice.choiceType;

public class TreinadorNpc extends Treinador{
	/*
	 * Classe que lida com o treinador especial NPC.
	 * Possui métodos de tomada de sua decisão.
	 * Poderá possuir alguns coeficientes de dificuldade. TODO
	 */
	public TreinadorNpc(int id, String nome, boolean player) {
		super(id, nome, player);
	}
	
	public static Battlefield.Choice npcThink(Battlefield field) {
		/*
		 * Função de placeholder para processo de decisão do NPC.
		 * Possui uma chance de tomar a decisão otimizada
		 * e um processo para determinar a melhor decisão.
		 * Altera a decisão do NPC no battlefield.
		 * Mais explicação no corpo do texto.
		 */
		
		// Macros
		TreinadorNpc npc = field.getLoadedNpc();
		Poke mon = npc.getActiveMon();
		Poke foe = field.getLoadedPlayer().getActiveMon();
		Battlefield.Choice npcChoice = new Choice();
		
		/*
		 *  Calcula o dano que irá receber caso inimigo repita o último move de dano.
		 *  Se tiver certeza de morte e não possuir alta velocidade, troca.
		 */
		
		// Verificar se pokemon ativo está fainted ou forced switch
		if(mon.isFainted() || npc.isForcedSwitch()) { // TODO: Consertar caso em que forcedSwitch ocorre quando só há um poke vivo
			// Se sim, melhor ofensivo
			npc.setForcedSwitch(false);
			npcChoice.setFullChoice(choiceType.SWITCH, TreinadorNpc.getBestOffensiveTypeSwitch(field));
			return npcChoice;
		}
		// Se vivo ou estável
		else {
			// Checar vida; se for mais que metade
			// TODO: CHECAR PP ANTES DE USAR MOVES
			if(mon.getCurHp() > (int) mon.getMaxHp()*0.5f) {
				// Verifica se o próprio possui BOOST de Ataque ou Spec. Atk
				if(mon.getModSpecAtk() > 0 || mon.getModAtk() > 0) {
					// Se sim, usa um move de dano;
					npcChoice.setFullChoice(choiceType.MOVE, TreinadorNpc.getBestDamageMoveId(field));
					return npcChoice;
				}
				// Se não, verifica se possui BOOST de DEFESA ou Spec. Def
				else if(mon.getModDef() > 0 || mon.getModSpecDef() > 0) {
					// Verificar se inimigo é afetado por status
					if(foe.getStatusFx().getType() != StatusFx.typeList.NEUTRAL) {
						// Se não, verifica se há StatusFx em Move para aplicar e se o oponente não é imune ao move;
						int i = 0;
						for(i = 0; i < 4; i++) {
							Move curMove = mon.getMove(i);
							// sendo válido
							if(curMove != null) {
								// TODO: COMO FORMATAR ESSE INFERNO???
								if(curMove instanceof StatusChangeFx && // StatusChange
										curMove.getPoints() > 0 && 		// com PP sobrando		
										Math.abs(field.getTchart().compoundTypeMatch(curMove.getTipagem(), foe)) > 0.0001f) { // Sem imunidade
									npcChoice.setFullChoice(choiceType.MOVE, i);
									return npcChoice;
								}
							}
						}
					}
					// Se já estiver, usar MOVE de maior dano
					else {
						npcChoice.setFullChoice(choiceType.MOVE, TreinadorNpc.getBestDamageMoveId(field));
						return npcChoice;
					}
				}
				// Se não possuir nenhum buff
				// (Perceba que o NPC ignora debuffs se acreditar possuir buffs o suficiente)
				else {
					// Verifica se está sob grandes debuffs
					int i = 0;
					int debuffCount = 0;
					for(i = 0; i < 0; i++) {
						int currentStat =  mon.getStatModArray()[i];
						if(currentStat < 0)
							debuffCount += currentStat;
					}
					// Se sim, 70% de chance de tentar trocar
					if(debuffCount >= 2 && TurnUtils.rollChance(70)) {
						int switchIndex = TreinadorNpc.getBestDefensiveTypeSwitch(field);
						npcChoice.setFullChoice(choiceType.SWITCH, switchIndex);
					}
					// Caso contrário
					// Verifica se todos seus moves são ruins; se sim, troca
					// Se não, 60% de buscar buff a si mesmo; 30% de usar um ataque, 10% de tentar aplicar um StatusFx
					// Se não houver como se buffar, 50% de chance de usar um ataque ou 50% de aplicar StatusFx
					// Se não houver 
				}
				// 
				// Busca move mais potente com STAB não-atenuado ou super efetivo
				// Se não tiver nada disso, roll de trocar pokemon
				// Se falhar nesse roll, escolhe o ataque menos pior
			}
			else {
				// Se o ataque anterior do jogador tirou muita vida, trocar para poke com resistência a esse ataque
					// Se não houver, usar ataque que cause mais dano
				// Se não causou, usar o melhor move possível
			}
			
			// TODO: Caso a troca seja forçada, BestOffensive > BestDefensive > Random
		}
		
		
	}
	
	public static int getBestDamageMoveId(Battlefield field) {
		/*
		 * Função que retorna o ID do Move mais eficiente.
		 * Se não achar eficiente o suficiente, retorna -(id).
		 * TODO: CHECAR PP ANTES DE USAR MOVES; SE NÃO HOUVER PP EM MOVE BOM, DESCARTÁ-LO. MAS COMO
		 */
		Poke monSelf = field.getLoadedPlayer().getActiveMon();
		Poke monFoe = field.getLoadedNpc().getActiveMon();
		int i = 0;
		int index = 0;
		float bestF = 0f;
		float curF = 0f;
		for(i = 0; i < 4; i++) {
			// TODO: Buscar move de Status aqui?
			Move curMove = monSelf.getMove(i);
			if(curMove != null && curMove.getCateg() != Move.moveCategs.STATUS) {
				curF = field.getTchart().typeMatch(curMove.getTipagem(), monFoe.getTipagem()[0])*
						field.getTchart().typeMatch(curMove.getTipagem(), monFoe.getTipagem()[1]);
				if(curF > bestF) {
					index = i;
					curF = bestF;
				}
			}
		}
		// Tendo achado o melhor da dano, verifica se é lá essas coisas
		float error = 0.001f;
		if(Math.abs(bestF - 1f) < error || bestF > 1f)
			return index;
		else
			return index*(-1);
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
		int index = 0; // TODO: Pode-se tornar essa func. mais complexa criando uma lista de melhores moves e escolhendo o pokemon de acordo
		float bestF = 0f;
		float currentF = 0f;
		// iterando sobre o time
		for(i = 0; i < 6; i++) { // TODO: COLOCAR EXCEÇÕES PARA NULL POINTERS NESSAS VARREDURAS
			Poke curMon = field.getLoadedNpc().getTeam()[i];
			if(!curMon.isFainted()) {
				// iterando sobre moves
				for(j = 0; j < 4; j++) { // TODO: COLOCAR EXCEÇÕES PARA NULL POINTERS NESSAS VARREDURAS
					Move curMove = curMon.getMove(j);
					currentF = field.getTchart().typeMatch(curMove.getTipagem(), foeType1) * 
							field.getTchart().typeMatch(curMove.getTipagem(), foeType2);
					if(currentF > bestF) {
						index = i;
						currentF = bestF;
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

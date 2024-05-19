package pokemontextgame;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.Function;

import pokemontextgame.moves.Move;
import pokemontextgame.moves.Struggle;

public class Battlefield {
	/*
	 * Classe responsável pelas batalhas em si.
	 * Carrega o menu e faz chamadas e leituras.
	 * Faz checagens de clima, uso de habilidades,
	 * troca de pokémon, finalização de batalha, etc.
	 */
	private int turnCount;
	private TreinadorNpc lNpc;
	private Treinador lPlayer;
	private Poke npcMon; // talvez redundante
	private Poke playerMon; // talvez redundante
	private TypeChart tchart;
	private boolean koSwitch; // flag que permite uma troca extra que não gasta a escolha para casos em que o último pokemon tomou K.O.
	private boolean end; // flag para fim da batalha ao se passarem todos os turnos
	private boolean trainerBattle; // flag para batalha contra pokemon selvagem ou contra treinador
	private Struggle struggle = new Struggle(); // ataque de emergência com PP infinito
	
	// Leitura e impressão de informação
	private Choice playerChoice;
	private Choice npcChoice;
	private TextBoxBuffer textBoxBuffer;
	
	public class Choice {
		/*
		 * Subclasse que lida com a escolha em si.
		 * Pode ser de 7 tipos.
		 * O id se refere qual decisão em si foi tomada.
		 */

		// Por enquanto, não implementaremos BAG_HEAL, BAG_STATUS, BAG_POKEBALL, BAG_BOOST;
		// RUN nunca dará certo porque por enquanto estamos apenas lidando com batalhas com treinadores.
		public enum choiceType {MOVE, SWITCH, RUN};
		private choiceType type;
		private int id;
		private Treinador owner;
		
		public Choice() {};
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public choiceType getType() {
			return type;
		}
		public void setType(choiceType type) {
			this.type = type;
		}
		public void setFullChoice(choiceType type, int id) {
			this.type = type;
			this.id = id;
		}
		public Choice getFullChoice() {
			return this;
		}
		public Treinador getOwner() {
			return owner;
		}
		public void setOwner(Treinador owner) {
			this.owner = owner;
		}
	}
	
	class TextBoxBuffer{
		/*
		 * Classe que armazena as informações textuais
		 * do turno que serão impressas. O turno acontece
		 * num único instante, mas com esta classe,
		 * temos mais controle sobre o fluxo de informações
		 * que chegam ao jogador e maior flexibilidade
		 * para mudanças futuras.
		 */
		protected LinkedList<String> textQueue;
		
		public TextBoxBuffer() {
			textQueue = new LinkedList<String>();
		}
	}
	
	public Battlefield(Treinador player, TreinadorNpc npc, boolean trainerBattle) {
		this.turnCount = 0;
		this.trainerBattle = true;
		this.end = false;
		// as próximas duas linhas talvez sejam desnecessárias
		this.lNpc = npc;
		this.lPlayer = player;
		
		// Primeiro pokemon ativo na batalha é sempre o de ID 0;
		this.playerMon = player.getTeam()[0];
		playerMon.setActive(true);
		this.npcMon = npc.getTeam()[0];
		npcMon.setActive(true);
		
		textBoxBuffer = new TextBoxBuffer();
		
		this.tchart = new TypeChart();
	}
	
	public boolean turnLoops(Scanner scan) {
		/*
		 * Função que rege um confronto inteiro.
		 * Lê as opções do jogador por meio do BattleMenu;
		 * Chama quantos turns forem necessários até o fim da batalha.
		 * Retorna true se deve continuar, false caso a batalha tenha terminado.
		 */
		
		// Registrar escolha do jogador e enviar para um turno
		String winner = "";
		while(!this.end) {
			switch(turn(scan)) {
				case(0):{
					break;
				}
				case(1):{
					winner = this.getLoadedPlayer().getName(); 
					end = true;
				}
				case(2):{
					winner = this.getLoadedNpc().getName(); 
					end = true;
				}
			}
		}
		System.out.print("O vencedor da batalha é " + winner + "!\n");
		return end;
	}
	
	public int turn(Scanner scan) {
		/*
		 * Recebe as ações do jogador e decisões do NPC.
		 * Efetua os turnos de combate. Imprime resultados.
		 * Mais explicações adiante.
		 * É uma versão simplificada disso: https://bulbapedia.bulbagarden.net/wiki/User:FIQ/Turn_sequence
		 */
		
		this.turnCount++;
		
		// Criaremos uma fila para processar de decisões
		final class ChoiceTuple {
			/*
			 * Como as escolhas não armazenam o seu dono 
			 * (desnecessário em quase todos os casos),
			 * é necessário criarmos um classe local
			 * que armazene a escolha e seu dono para
			 * uso na fila de escolhas.
			 */
			
			protected Choice choice;
			protected Treinador owner;
			
			protected ChoiceTuple(Choice choice, Treinador owner) {
				this.choice = choice;
				this.owner = owner;
			}
		}

		// Inicializando escolhas sem valores por enquanto
		playerChoice = new Choice();
		npcChoice = new Choice();
		
		// Fazer um objeto Weather? Talvez seja uma boa. TODO: Se não formos deletar weather
		
		// Atualizando
		playerMon = lPlayer.getActiveMon();
		npcMon = lNpc.getActiveMon();
		
		// Caso alguém tenha tomado um KO, deve colocar novo Pokemon no field sem gastar o turno presente
		if(koSwitch) {
			if(npcMon.isFainted()) {
				lNpc.isForcedSwitch();
				npcChoice = TreinadorNpc.npcThink(this);
				SwitchMons(lNpc, npcChoice.id);
			}
			if(playerMon.isFainted()) {
				lPlayer.isForcedSwitch();
				BattleMenu.menuDisplayTeam(scan, this); 
				SwitchMons(lPlayer, playerChoice.id);
			}
			
			koSwitch = false;
			printTurn();
			// Novamente atualizando
			playerMon = lPlayer.getActiveMon();
			npcMon = lNpc.getActiveMon();
		}
		
		// Atualizando contagem de turnos dos pokes para cálculo de alguns danos
		playerMon.turnOnFieldIncr();
		npcMon.turnOnFieldIncr();
		playerMon.getStatusFx().statusTurnPass();
		npcMon.getStatusFx().statusTurnPass();
		
		// Tomando decisões
		npcChoice = TreinadorNpc.npcThink(this);
		// Loading de opções do jogador no menu
		if(!playerMon.isFainted()) { // se mon ativo estiver vivo 
			BattleMenu.menuDisplayRoot(scan, this);
		}
		else { // força troca caso contrário TODO Talvez seja desnecessário com a nova func. ali em cima
			lPlayer.setForcedSwitch(true);
			BattleMenu.menuDisplayTeam(scan, this); 
		}
		
		// Criaremos uma fila para processar de decisões
		// Armazenando decisões e seus donos para enfileirar e processar
		ChoiceTuple npcChoiceTuple = new ChoiceTuple(npcChoice, lNpc);
		ChoiceTuple playerChoiceTuple = new ChoiceTuple(playerChoice, lPlayer);
		LinkedList<ChoiceTuple> choiceQueue = new LinkedList<ChoiceTuple>();
		
		// Prioridade do Switch do Npc é sempre a maior
		// Switch de player recebe segunda prioridade
		Poke playerMon, npcMon;
		playerMon = lPlayer.getActiveMon();
		npcMon = lNpc.getActiveMon();
		Move playerMove = null, npcMove = null;
		
		// Lidando com prioridades maiores que Move
		if(npcChoice.getType() != Choice.choiceType.MOVE)
			choiceQueue.add(npcChoiceTuple);
		else {
			npcMove = npcMon.getMove(Math.abs(npcChoice.getId())); // Conversão mencionada em TreinadorNpc.getBestDamageId();
		}
		
		if(playerChoice.getType() != Choice.choiceType.MOVE) {
			choiceQueue.add(playerChoiceTuple);
		}
		else {
			playerMove = playerMon.getMove(playerChoice.getId());
		}
		
		// Se um dos dois atacar
		if(playerMove != null || npcMove != null) {
			// Player não ataca
			if(playerMove == null) {
				choiceQueue.add(npcChoiceTuple);
			}
			// NPC não ataca
			else if(npcMove == null) {
				choiceQueue.add(playerChoiceTuple);
			}
			// Os dois atacam
			else {
				// Comparar prioridade
				if(playerMove.getPriority() > npcMove.getPriority()) {
					choiceQueue.add(playerChoiceTuple);
					choiceQueue.add(npcChoiceTuple);
				}
				else if(playerMove.getPriority() < npcMove.getPriority()) {
					choiceQueue.add(npcChoiceTuple);
					choiceQueue.add(playerChoiceTuple);
				}
				// Moves têm suas prioridades comparadas, e se forem iguais, comparamos a velocidade do Pokemon
				else {
					// Função lambda local de redução de velocidade por paralisia
					Function<Poke, Float> paralysisSlowdown = (mon) -> {
						if(mon.getStatusFx().getType() == StatusFx.typeList.PARALYSIS)
							return 0.75f;
						else
							return 1f;
					};
					int playerSpeed = (int) (TurnUtils.getModStat(4, playerMon)*paralysisSlowdown.apply(playerMon));
					int npcSpeed =  (int) (TurnUtils.getModStat(4, npcMon)*paralysisSlowdown.apply(npcMon));
					if(playerSpeed > npcSpeed) {
						choiceQueue.add(playerChoiceTuple);
						choiceQueue.add(npcChoiceTuple);
					}
					else if(playerSpeed < npcSpeed) {
						choiceQueue.add(npcChoiceTuple);
						choiceQueue.add(playerChoiceTuple);
					}
					// Se empatar, favorece o Player
					else {
						choiceQueue.add(playerChoiceTuple);
						choiceQueue.add(npcChoiceTuple);
					}
				}
			}
		}
		
		// E agora, processando a fila
		while(choiceQueue.size() != 0) {
			// Macros
			ChoiceTuple currentCT = choiceQueue.removeFirst();
			int choiceId = currentCT.choice.getId();
			Choice.choiceType choiceType = currentCT.choice.getType();
			
			// Atualizando Mons ativos desde a decisão anterior
			playerMon = lPlayer.getActiveMon();
			npcMon = lNpc.getActiveMon();
			
			// E processando decisões em si
			if(choiceType == Choice.choiceType.SWITCH) {
				this.SwitchMons(currentCT.owner, choiceId);
			}
			else { // Por enquanto, esse Else pode apenas ser um Move. Ver notas em Battlefield.Choice.choiceType
				Treinador receivingTr;
				Poke movingMon, receivingMon;
				if(currentCT.owner == lPlayer) {
					movingMon = playerMon;
					receivingTr = lNpc;
					receivingMon = npcMon;
				}
				else {
					movingMon = npcMon;
					receivingTr = lPlayer;
					receivingMon = playerMon;
				}
				
				// Settando o Move e usando
				Move qdMove;
				if(Math.abs(choiceId) > 4)
					qdMove = struggle;
				else
					qdMove = movingMon.getMove(choiceId);
				
				if(!TurnUtils.blockMoveCheck(movingMon, this)) {
					qdMove.useMove(this, movingMon, receivingMon, tchart);
				}
				// Imprimindo mensagem do poke fainted, livrando-se da escolha dele, atualizando seu ForcedSwitch
				if(receivingMon.isFainted()) {
					currentCT.owner.setForcedSwitch(true);
					koSwitch = true;
					this.getTextBoxBuffer().textQueue.add(receivingMon.getName() + " sofreu um K.O.!\n");
					if(choiceQueue.size() != 0 && choiceQueue.getFirst().owner == receivingTr) {
						choiceQueue.remove();
					}
				}
			}
		}
		
		// Consequências de todas as decisões
		if(!npcMon.isFainted()) {
			TurnUtils.statusFxDmg(npcMon, this);
		}
		
		// Verificar se resta alguém vivo para batalhar terminou
		int i;
		int result = 0;
		boolean end = false;
		for(i = 0; i < 6; i++) {
			Poke current = this.lPlayer.getTeam()[i];
			if(current != null && !current.isFainted()) 
				break;
			else if (i == 5) {
				end = true;
				result = 1; // todos pokes do npc desmaiados; vencedor = jogador
			}
		}
		for(i = 0; i < 6; i++) {
			Poke current = this.lNpc.getTeam()[i];
			if(current != null && !current.isFainted()) 
				break;
			else if (i == 5) {
				end = true;
				result = 2; // todos pokes do jogador desmaiados; vencedor = NPC
			}
		}
		
		if(!end && !playerMon.isFainted()) {
			TurnUtils.statusFxDmg(playerMon, this);
		}
		
		printTurn();
		
		return result; // se turno não é final
		
	}

	public boolean isTrainerBattle() {
		return trainerBattle;
	}

	public void setTrainerBattle(boolean trainerBattle) {
		this.trainerBattle = trainerBattle;
	}

	public Treinador getLoadedPlayer() {
		return lPlayer;
	}

	public void setLoadedPlayer(Treinador loadedPlayer) {
		this.lPlayer = loadedPlayer;
	}

	public Choice getPlayerChoice() {
		return playerChoice;
	}
	
	public TreinadorNpc getLoadedNpc() {
		return lNpc;
	}

	public void setLoadedNpc(TreinadorNpc loadedNpc) {
		this.lNpc = loadedNpc;
	}

	public TypeChart getTchart() {
		return tchart;
	}

	public void setTchart(TypeChart tchart) {
		this.tchart = tchart;
	}

	public Choice getNpcChoice() {
		return npcChoice;
	}

	public void setNpcChoice(Choice npcChoice) {
		this.npcChoice = npcChoice;
	}

	public TextBoxBuffer getTextBoxBuffer() {
		return textBoxBuffer;
	}
	
	public void textBufferAdd(String message) {
		// Recebe uma linha de texto e adiciona
		// à fila de processamento e impressão
		this.textBoxBuffer.textQueue.add(message);
	}
	
	private void printTurn() {
		/*
		 * Imprime todas as mensagens do turno de cara.
		 */
		System.out.print("# # # # # # # # # # # # # # # # "
				+ "# # # # # # # # # # # # # # # # "
				+ "TURNO " + turnCount 
				+ " # # # # # # # # # # # # # # # # "
				+ "# # # # # # # # # # # # # # # # " 
				+ "\n");
		while(textBoxBuffer.textQueue.size() != 0) {
			System.out.print(this.textBoxBuffer.textQueue.remove());
		}
	}
	
	private void SwitchMons(Treinador trainer, int newMonId) {
		/*
		 * Altera o field para realizar as trocas de pokemons.
		 */
		Poke prevMon = trainer.getActiveMon();
		prevMon.resetStats();
		prevMon.setTurnsOnField(0);
		prevMon.getStatusFx().setTimeAfflicted(0);
		trainer.setForcedSwitch(false);
		trainer.setActiveMonId(newMonId);
		this.getTextBoxBuffer().textQueue.add("Treinador " + trainer.getName()
				+ " trocou " + prevMon.getName()
				+ " por " + trainer.getActiveMon().getName() + "!\n");
		
	}
};

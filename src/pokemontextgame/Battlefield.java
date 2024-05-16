package pokemontextgame;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.Function;

import moves.Move;
import moves.Struggle;
import pokemontextgame.Battlefield.Choice.choiceType;;

public class Battlefield {
	/*
	 * Classe responsável pelas batalhas em si.
	 * Carrega o menu e faz chamadas e leituras.
	 * Faz checagens de clima, uso de habilidades,
	 * troca de pokémon, finalização de batalha, etc.
	 */
	private int turnCount;
	private Weather weather; // Provavelmente não usaremos por enquanto. TODO;
	private TreinadorNpc lNpc;
	private Treinador lPlayer;
	private Poke npcMon; // talvez redunante
	private Poke playerMon; // talvez redundante
	private TypeChart tchart;
	private boolean end; // flag para fim da batalha ao se passarem todos os turnos TODO talvez desnecessário
	private boolean trainerBattle; // flag para batalha contra pokemon selvagem ou contra treinador
	private Struggle struggle = new Struggle(); // ataque de emergência com PP infinito
	// TODO: Classe de turno. Por enquanto, tentaremos fazer a classe aqui.
	
	// Leitura de informação
	private Choice playerChoice;
	private Choice npcChoice;
	
	public class Choice {
		/*
		 * Subclasse que lida com a escolha em si.
		 * Pode ser de 7 tipos.
		 * O id se refere qual decisão em si foi tomada.
		 */
		
		// TODO: Deixar ENUM público mesmo?
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
		public void setFullChoice(choiceType type, int id, Treinador owner) {
			this.type = type;
			this.id = id;
			this.owner = owner;
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
	
	public Battlefield(Treinador player, TreinadorNpc npc, boolean trainerBattle) {
		this.turnCount = 0;
		this.weather = new Weather(true, 1, -1); // TODO: Se livrar logo.
		this.trainerBattle = true;
	
		// as próximas duas linhas talvez sejam desnecessárias
		this.lNpc = npc;
		this.lPlayer = player;
		
		// Primeiro pokemon ativo na batalha é sempre o de ID 0;
		this.playerMon = player.getTeam()[0];
		playerMon.setActive(true);
		this.npcMon = npc.getTeam()[0];
		npcMon.setActive(true);
		
		
		this.tchart = new TypeChart();
	}
	
	static boolean turnLoops(Battlefield field, Scanner scan) {
		/*
		 * Função que rege um confronto inteiro.
		 * Lê as opções do jogador por meio do BattleMenu;
		 * Chama quantos turns forem necessários até o fim da batalha.
		 * Retorna true se batalha tiver acabado, false caso contrário.
		 */
		
		// Registrar escolha do jogador e enviar para um turno
		BattleMenu.menuDisplayRoot(scan, field);
		
		
		return false;
	}
	
	public int turn(Scanner scan) {
		/*
		 * Recebe as ações do jogador e decisões do NPC.
		 * Efetua os turnos de combate.
		 * Mais explicações adiante.
		 * É uma versão simplificada disso: https://bulbapedia.bulbagarden.net/wiki/User:FIQ/Turn_sequence
		 */
		
		this.turnCount++;

		// Inicializando escolhas sem valores por enquanto
		playerChoice = new Choice();
		npcChoice = new Choice();

		// Fazer um objeto Weather? Talvez seja uma boa. TODO: Se não formos deletar weather
		
		// Atualizar decisão do NPC
		npcChoice = TreinadorNpc.npcThink(this);
		
		// Loading de opções do jogador no menu
		if(!playerMon.isFainted()) { // se mon ativo estiver vivo 
			BattleMenu.menuDisplayRoot(scan, this);
		}
		else { // força troca caso contrário
			lPlayer.setForcedSwitch(true);
			BattleMenu.menuDisplayTeam(scan, this); 
		}
		
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
		
		ChoiceTuple npcChoicePair = new ChoiceTuple(npcChoice, lNpc);
		ChoiceTuple playerChoicePair = new ChoiceTuple(playerChoice, lPlayer);
		
		// Criaremos uma fila para processar de decisões
		LinkedList<ChoiceTuple> choiceQueue = new LinkedList<ChoiceTuple>();

		// Prioridade do Switch do Npc é sempre a maior
		// Switch de player recebe segunda prioridade
		// Moves têm suas prioridades comparadas, e se forem iguais, comparamos a velocidade do Pokemon
		Poke playerMon, npcMon;
		playerMon = lPlayer.getActiveMon();
		npcMon = lNpc.getActiveMon();
		Move playerMove = null, npcMove = null;
		
		// Lidando com prioridades maiores que Move
		if(npcChoice.getType() != Choice.choiceType.MOVE)
			choiceQueue.add(npcChoicePair);
		else
			npcMove = npcMon.getMove(npcChoice.getId());
			
		if(playerChoice.getType() != Choice.choiceType.MOVE)
			choiceQueue.add(playerChoicePair);
		else
			playerMove = playerMon.getMove(playerChoice.getId());
		
		// Se um dos dois atacar
		if(playerMove != null || npcMove != null) {
			// Player não ataca
			if(playerMove == null)
				choiceQueue.add(npcChoicePair);
			// NPC não ataca
			else if(npcMove == null)
				choiceQueue.add(playerChoicePair);
			// Os dois atacam
			else {
				// Comparar prioridade
				if(playerMove.getPriority() > npcMove.getPriority()) {
					choiceQueue.add(playerChoicePair);
					choiceQueue.add(npcChoicePair);
				}
				else if(playerMove.getPriority() < npcMove.getPriority()) {
					choiceQueue.add(npcChoicePair);
					choiceQueue.add(playerChoicePair);
				}
				else {
					
					// Lambda de redução de velocidade por paralisia
					Function<Poke, Float> paralysisSlowdown = (mon) -> {
						if(mon.getStatusFx().getType() == StatusFx.typeList.PARALYSIS)
							return 0.75f;
						else
							return 1f;
					};
					
					int playerSpeed = (int) (TurnUtils.getModStat(4, playerMon)*paralysisSlowdown.apply(playerMon));
					int npcSpeed =  (int) (TurnUtils.getModStat(4, npcMon)*paralysisSlowdown.apply(npcMon));
					// Comparar velocidade, levando em conta possibilidade de Paralysis TODO
					if(playerSpeed > npcSpeed) {
						choiceQueue.add(playerChoicePair);
						choiceQueue.add(npcChoicePair);
					}
					else if(playerSpeed < npcSpeed) {
						choiceQueue.add(npcChoicePair);
						choiceQueue.add(playerChoicePair);
					}
					// Se empatar, favorece o Player
					else {
						choiceQueue.add(playerChoicePair);
						choiceQueue.add(npcChoicePair);
					}
				}
			}
		}
		
		// E agora, processando a fila
		while(choiceQueue.size() != 0) {
			ChoiceTuple currentCT = choiceQueue.removeFirst();
			int choiceId = currentCT.choice.getId();
			Choice.choiceType choiceType = currentCT.choice.getType();
			
			// Determinando quem age e quem recebe
			Treinador movingT, receivingT;
			Poke movingMon, receivingMon;
			if(currentCT.owner == lPlayer) {
				movingT = lPlayer;
				receivingT = lNpc;
			}
			else {
				movingT = lNpc;
				receivingT = lPlayer;
			}
			
			// E o que faz
			if(choiceType == Choice.choiceType.SWITCH) {
				
			}
			else { // por enquanto, esse Else pode apenas ser um Move. Ver notas em Battlefield.Choice.choiceType
				
				// TODO: Temos que atualizar NpcMon e PlayerMon. Mas como?
				movingT.setActiveMonId(choiceId);
				
			}
		}
		
		// Causando dano de fMon sobre sMon; incluir BURN no cálculo
		
		// Verificar se alguma habilidade ativou
		
		// Aplicar status ou efeito de status?
		
		// Verificar danos de habilidade sobre fMon (e.g: Aftermath)? Matou?
		
		// Verificar danos de status (burn, poison)
		
		// Verificar se alguma habilidade pode ativar;
		
		// TODO: Subtrair PP de cada move utilizado depois do uso;
		
		// TODO: Verificação de Stats voláteis e não voláteis
		
		// TODO: Verificar se habilidades ativam? No começo ou no final do turno?
		
		// TODO: E se os dois morrerem no mesmo turno? Dano de veneno ou ataques como Explosion
		
		// TODO: Toda a patacoada de display de texto.
		
		// Verificar se resta algém vivo para batalhar terminou TODO: Existe um jeito mais bonito e eficiente de fazer isso? ENUMs talvez?
		int i;
		for(i = 0; i < 6; i++) {
			if(!this.lPlayer.getTeam()[i].isFainted()) 
				break;
			else if (i == 5)
				return 1; // todos pokes do npc desmaiados; vencedor = jogador
		}
		for(i = 0; i < 6; i++) {
			if(!this.lNpc.getTeam()[i].isFainted()) 
				break;
			else if (i == 5)
				return 2; // todos pokes do jogador desmaiados; vencedor = NPC
		}
		
		return 0; // turno não é final
		
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
	
	public void setPlayerChoice(Choice choice) {
		// talvez se mostre inútil
		this.playerChoice = choice;
	}
	
	public void setFullPlayerChoice(choiceType type, int id) {
		// um tanto redundante
		this.setFullPlayerChoice(type, id);
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
	
};

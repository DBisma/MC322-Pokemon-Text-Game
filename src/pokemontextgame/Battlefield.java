package pokemontextgame;

import java.util.Scanner;

import moves.Move;
import pokemontextgame.Battlefield.Choice.choiceType;;

public class Battlefield {
	/*
	 * Classe responsável pelas batalhas em si.
	 * Carrega o menu e faz chamadas e leituras.
	 * Faz checagens de clima, uso de habilidades,
	 * troca de pokémon, finalização de batalha, etc.
	 */
	private int turnCount;
	private Weather weather;
	private TreinadorNpc loadedNpc;
	private Treinador loadedPlayer;
	private Poke npcMon; // talvez redunante
	private Poke playerMon; // talvez redundante
	private TypeChart tchart;
	private boolean end; // flag para fim da batalha ao se passarem todos os turnos
	private boolean trainerBattle; // flag para batalha contra pokemon selvagem ou contra treinador
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
		public enum choiceType {MOVE, SWITCH, RUN, BAG_HEAL, BAG_STATUS, BAG_POKEBALL, BAG_BOOST, STRUGGLE};
		private choiceType type;
		private int id;
		
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
	}
	
	
	public Battlefield(Treinador player, TreinadorNpc npc, boolean trainerBattle) {
		this.turnCount = 0;
		this.weather = new Weather(true, 1, -1); // TODO: Se livrar logo.
		this.trainerBattle = true;
	
		// as próximas duas linhas talvez sejam desnecessárias
		this.loadedNpc = npc;
		this.loadedPlayer = player;
		
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

		// Macros
		Treinador npc = loadedNpc;
		Treinador player = loadedPlayer;
		
		// Inicializando escolhas sem valores por enquanto
		playerChoice = new Choice();
		npcChoice = new Choice();

		// Fazer um objeto Weather? Talvez seja uma boa. TODO: Se não formos deletar weather
		
		// TODO: INICIALIZAR this.statMods = new int[8]; E CARREGAR TUDO COM 0 PARA TODOS OS MONS DE TODOS OS TIMES.
		// TODO: Será que é uma boa deixar isso nos POKES mesmo ao invés de só manter por aqui...? Dúvidas...
		
		// Atualizar decisão do NPC
		npcChoice = TreinadorNpc.npcThink(this);
		
		// Loading de opções do jogador no menu
		if(!playerMon.isFainted()) { // se mon ativo estiver vivo 
			BattleMenu.menuDisplayRoot(scan, this);
		}
		else { // força troca caso contrário
			loadedPlayer.setForcedSwitch(true);
			BattleMenu.menuDisplayTeam(scan, this); 
		}
		
		// Depois de o Player escolher o que fazer, processamos o Switch do NPC
		// Em teoria, é impossível o NPC escolher um Switch quando isso é impossível
		// Portanto, o seguinte código não deve falhar nunca. TODO: Testar testar e testar
		Move npcMove = null;
		if(npcChoice.getType() == Choice.choiceType.SWITCH) {
			// Atualizar poke ativo no time e no battlefield
			npc.setActiveMonId(npcChoice.getId());
			npcMon = npc.getActiveMon();
		}
		else {
			// Se não for switch, só pode ser um move
			npcMove = playerMon.getMove(Math.abs(npcChoice.getId())); // Lembrando: o ID é negativo se a decisão for sub-otimizada
		}
		
		// Trocas de pokemon são sempre processadas antes de ataques
		// Switch para escolhas feitas no Battle Menu
		Move playerMove = null;
		switch(playerChoice.getType()) { // Ignorando BAG por enquanto. TODO: Incluir a bag TODO: Criar opção struggle? Encode como move definido?
			case Choice.choiceType.MOVE:{
				System.out.print("HELP ME" + playerChoice.getType() + playerChoice.getId() + "\n");
				playerMove = playerMon.getMove(playerChoice.getId());
				System.out.print(playerMove.getName());
				break;
			}
			case Choice.choiceType.SWITCH:{ // TODO: Por enquanto, é impossível pedir uma troca que falha. TODO
				// Atualizar poke ativo no time e no battlefield
				player.setActiveMonId(playerChoice.getId());
				playerMon = player.getActiveMon();
				break;
			}
			case Choice.choiceType.RUN:{
				// Por enquanto, a opção de correr é impossível porque só existe batalha contra outro treinador TODO
				// Encerrar o turno aqui e o turn loop também, mas isso nunca será feito.
				break;
			}
		}
		
		// Sendo processadas as escolhas de maior prioridade, devemos processar os moves

		// Determinando quem age primeiro
		
		//
		//		TODO TODO TODO TODO TODO TODO TODO: Temos casos em que não temos Moves por parte de alguém!
		//		Juntar com um Switch (troca) na hora de processamento?
		//		Vai ter que desenhar mano.
		//		⣽⣿⢣⣿⡟⣽⣿⣿⠃⣲⣿⣿⣸⣷⡻⡇⣿⣿⢇⣿⣿⣿⣏⣎⣸⣦⣠⡞⣾⢧⣿⣿ 
		//		⣿⡏⣿⡿⢰⣿⣿⡏⣼⣿⣿⡏⠙⣿⣿⣤⡿⣿⢸⣿⣿⢟⡞⣰⣿⣿⡟⣹⢯⣿⣿⣿ 
		//		⡿⢹⣿⠇⣿⣿⣿⣸⣿⣿⣿⣿⣦⡈⠻⣿⣿⣮⣿⣿⣯⣏⣼⣿⠿⠏⣰⡅⢸⣿⣿⣿ 
		//		⡀⣼⣿⢰⣿⣿⣇⣿⣿⡿⠛⠛⠛⠛⠄⣘⣿⣿⣿⣿⣿⣿⣶⣿⠿⠛⢾⡇⢸⣿⣿⣿ 
		//		⠄⣿⡟⢸⣿⣿⢻⣿⣿⣷⣶⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⡋⠉⣠⣴⣾⣿⡇⣸⣿⣿⡏ 
		//		⠄⣿⡇⢸⣿⣿⢸⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣄⠘⢿⣿⠏⠄⣿⣿⣿⣹ 
		//		⠄⢻⡇⢸⣿⣿⠸⣿⣿⣿⣿⣿⣿⠿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣦⣼⠃⠄⢰⣿⣿⢯⣿ 
		//		⠄⢸⣿⢸⣿⣿⡄⠙⢿⣿⣿⡿⠁⠄⠄⠄⠄⠉⣿⣿⣿⣿⣿⣿⡏⠄⢀⣾⣿⢯⣿⣿
		//		⣾⣸⣿⠄⣿⣿⡇⠄⠄⠙⢿⣀⠄⠄⠄⠄⠄⣰⣿⣿⣿⣿⣿⠟⠄⠄⣼⡿⢫⣻⣿⣿
		//		⣿⣿⣿⠄⢸⣿⣿⠄⠄⠄⠄⠙⠿⣷⣶⣤⣴⣿⠿⠿⠛⠉⠄⠄ ⢸⣿⣿⣿⣿⠃⠄
		//		
		Poke firstMon, secondMon;
		Move firstMove, secondMove;

		
	
		// Se for ataque, verificar se a paralisia não impede o ataque; (isso é depois!!)
		
		// Se for ataque, compara prioridade
		if(playerMove.getPriority() > npcMove.getPriority()) { // TODO: Posso juntar essas linhas em duas só?
			firstMon = playerMon; 
			firstMove = playerMove;
			secondMon = npcMon;	
			secondMove = npcMove;
		}
		else if(playerMove.getPriority() < npcMove.getPriority()) {
			firstMon = npcMon; 
			firstMove = npcMove;
			secondMon = playerMon; 
			secondMove = playerMove;
		}
		
		
		
		// Se a prioridade for igual, comparar velocidade; incluir paralisia no cálculo
		// Se por algum milagre velocidades forem iguais, o player é privilegiado
		else {
			if(TurnUtils.modStat(4, playerMon) >= TurnUtils.modStat(4, npcMon)){
				firstMon = playerMon; firstMove = playerMove;
				secondMon = npcMon; secondMove = npcMove;
			}
			else {
				firstMon = playerMon; firstMove = npcMove;
				secondMon = npcMon; secondMove = playerMove;
			}
		}
		
		// Causando dano de fMon sobre sMon; incluir BURN no cálculo
//		int dmg = TurnUtils.calcDmg(firstMove, firstMon, secondMon, tchart);
//		if(dmg >= secondMon.getCurHp()) {
//			secondMon.setCurHp(0);
//			secondMon.setFainted(true);
//		}
//		else {
//			secondMon.setCurHp(secondMon.getCurHp() - dmg);
//		}
		
		// Verificar se alguma habilidade ativou
		
		// Aplicar status ou efeito de status?
		
		// Verificar danos de habilidade sobre fMon (e.g: Aftermath)? Matou?
		
		// Verificar danos de status (burn, poison)
		
		// Dano de sMon sobre fMon; incluir queda de dano físico causado por BURN no dano
//		if(!secondMon.isFainted()) {
//			dmg = TurnUtils.calcDmg(secondMove, secondMon, firstMon, tchart);
//			if(dmg >= firstMon.getCurHp()) {
//				firstMon.setCurHp(0);
//				firstMon.setFainted(true);
//			}
//			else {
//				firstMon.setCurHp(firstMon.getCurHp() - dmg);
//			}
//		}
		
		// Verificar se alguma habilidade pode ativar;
		
		// TODO: Subtrair PP de cada move utilizado depois do uso;
		
		// TODO: Verificação de Stats voláteis e não voláteis
		
		// TODO: Verificar se habilidades ativam? No começo ou no final do turno?
		
		// TODO: E se os dois morrerem no mesmo turno? Dano de veneno ou ataques como Explosion
		
		// TODO: Toda a patacoada de display de texto.
		
		// Verificar se resta algém vivo para batalhar terminou TODO: Existe um jeito mais bonito e eficiente de fazer isso? ENUMs talvez?
		int i;
		for(i = 0; i < 6; i++) {
			if(!this.loadedPlayer.getTeam()[i].isFainted()) 
				break;
			else if (i == 5)
				return 1; // todos pokes do npc desmaiados; vencedor = jogador
		}
		for(i = 0; i < 6; i++) {
			if(!this.loadedNpc.getTeam()[i].isFainted()) 
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
		return loadedPlayer;
	}

	
	public void setLoadedPlayer(Treinador loadedPlayer) {
		this.loadedPlayer = loadedPlayer;
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
		return loadedNpc;
	}

	public void setLoadedNpc(TreinadorNpc loadedNpc) {
		this.loadedNpc = loadedNpc;
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

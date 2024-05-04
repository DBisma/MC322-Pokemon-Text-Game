package pokemontextgame;

import java.util.Scanner;

public class Battlefield {
	/*
	 * Classe responsável pelas batalhas em si.
	 * Carrega o menu e faz chamadas e leituras.
	 * Faz checagens de clima, uso de habilidades,
	 * troca de pokémon, finalização de batalha, etc.
	 */
	private int turnCount;
	private Weather weather;
	private Treinador loadedNpc;
	private Treinador loadedPlayer;
	private Poke npcMon;
	private Poke playerMon;
	private boolean end; // flag para fim da batalha ao se passarem todos os turnos
	private boolean trainerBattle; // flag para batalha contra pokemon selvagem ou contra treinador
	// TODO: Classe de turno. Por enquanto, tentaremos fazer a classe aqui.
	
	// TODO: Gets e sets de cada uma dessas variáveis.
	
	public Battlefield(Treinador player, Treinador npc, boolean trainerBattle) {
		this.turnCount = 0;
		this.weather = new Weather(true, 1, -1);
		this.setTrainerBattle(trainerBattle);
	
		// as próximas duas linhas talvez sejam desnecessárias
		this.setLoadedPlayer(player);
		this.loadedNpc = npc;
		
		// pokemon ativo é sempre 0
		this.playerMon = player.getTeam()[0];
		this.npcMon = npc.getTeam()[0];
		
	}
	
	static boolean turnLoops(Battlefield field, TypeChart tchart, Scanner scan) {
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
	
	public int turn(Scanner scan, Battlefield field, TypeChart tchart) {
		/*
		 * Recebe as ações do jogador. Efetua os turnos de combate.
		 * Mais explicações adiante.
		 * Versão simplificada dessa insanidade: 
		 * https://bulbapedia.bulbagarden.net/wiki/User:FIQ/Turn_sequence
		 * 
		 * Essa função consegue ficar bastante complexa, mas não precisamos fazer tudo.
		 */
		
		this.turnCount++;
		
		// Fazer um objeto Weather? Talvez seja uma boa.
	
		// Verificar se o pokemon ativo do NPC está morto
		
		// Se o pokemon ativo do Jogador estiver vivo
		if(!field.getLoadedPlayer().getActiveMon().isFainted()) {
			BattleMenu.menuDisplayRoot(scan, field);
		}
		// força troca caso contrário
		else { 
			getLoadedPlayer().setForcedSwitch(true);
			BattleMenu.menuDisplayTeam(scan, field); 
		}
		
		// Receber a opção e verificar sua validade
		/*
		 * Opções possíveis
		 * Talvez agora seja uma boa tentar um enum. Onde deveremos guardá-lo?
		 * No próprio field! O field sempre tem acesso ao player loadado e suas opções.
		 * ATAQUE 0-3;
		 * TROCA 0-6
		 * BAG
		 * FUGIR
		 */
		
		// >Fugir, Ver Pokemons Novos, Informação sobre Pokes Novos, Enviar;
		
		// No mais das vezes, puxar um pokemon novo.
		
		// Receber a ação do jogador (trocar de pokemon, usar um move)
		// TODO: Encoding. 0-3 (4): Moves; 4-9 (6): Trocar para pokemons 0-5 (6)?
		// TODO: Função de seleção de opções? Turno começa depois de uma opção ser selecionada?
			// Alternativamente, Array de 2 dígitos.
			// index 0: 0 	(move)		ou		1 	(switch)
			// index 1: 0-3 (move) 		ou 		0-5 (switch)
		
		// Ademais, o jogador pode querer ler informações de seu pokemon.
		// Isso não avança o turno. Devemos dar um jeito de loopar.
		
		// Devemos primeiro criar um encoding para cada ação possível... 
		// ou fazer uma função para selecionar a opção.
		// Escolher move ou troca avança turno
		
		int playerOption = 0; // TODO: mudar isso depois
		Move playerMove = field.playerMon.getMove(playerOption);
		
		// Escolher move do NPC.
		int npcOption = 0; // TODO: essa escolha será feita por uma inteligência artificial 
		Move npcMove = field.npcMon.getMove(npcOption);
		
		// Troca possui prioridade exceto no caso de o move inimigo ser PURSUIT
		
		// Se os dois trocarem, a troca dos dois ocorre num turno só, mas o pokemon inimigo é renderizado primeiro;
		
		// Se 2 moves foram escolhidos...
		// Determinar quem age primeiro
		Poke firstMon, secondMon;
		Move firstMove, secondMove; //TODO: Existe problemas de fazer esses alias?
		
		// Se for ataque, verificar se a paralisia não impede o ataque;
		
		// Se for ataque, compara prioridade
		if(playerMove.getPriority() > npcMove.getPriority()) {
			firstMon = playerMon; firstMove = playerMove; //TODO: Posso deixar coisas em linha assim:
			secondMon = npcMon;	secondMove = npcMove;
		}
		else if(playerMove.getPriority() < npcMove.getPriority()) {
			firstMon = npcMon; firstMove = npcMove;
			secondMon = playerMon; secondMove = playerMove;
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
		int dmg = TurnUtils.calcDmg(firstMove, firstMon, secondMon, tchart);
		if(dmg >= secondMon.getCurHp()) {
			secondMon.setCurHp(0);
			secondMon.setFainted(true);
		}
		else {
			secondMon.setCurHp(secondMon.getCurHp() - dmg);
		}
		
		// Verificar se alguma habilidade ativou
		
		// Aplicar status ou efeito de status?
		
		// Verificar danos de habilidade sobre fMon (e.g: Aftermath)? Matou?
		
		// Verificar danos de status (burn, poison)
		
		// Dano de sMon sobre fMon; incluir queda de dano físico causado por BURN no dano
		if(!secondMon.isFainted()) {
			dmg = TurnUtils.calcDmg(secondMove, secondMon, firstMon, tchart);
			if(dmg >= firstMon.getCurHp()) {
				firstMon.setCurHp(0);
				firstMon.setFainted(true);
			}
			else {
				firstMon.setCurHp(firstMon.getCurHp() - dmg);
			}
		}
		
		// Verificar se alguma habilidade pode ativar;
		
		// TODO: Subtrair PP de cada move utilizado depois do uso;
		
		// TODO: Verificação de Stats voláteis e não voláteis
		
		// TODO: Verificar se habilidades ativam? No começo ou no final do turno?
		
		// TODO: E se os dois morrerem no mesmo turno? Dano de veneno ou ataques como Explosion
		
		// TODO: Toda a patacoada de display de texto.
		
		// Verificar se resta algém vivo para batalhar terminou TODO: Existe um jeito mais bonito e eficiente de fazer isso? ENUMs talvez?
		int i;
		for(i = 0; i < 6; i++) {
			if(!field.getLoadedPlayer().getTeam()[i].isFainted()) 
				break;
			else if (i == 5)
				return 1; // todos pokes do npc desmaiados; vencedor = jogador
		}
		for(i = 0; i < 6; i++) {
			if(!field.loadedNpc.getTeam()[i].isFainted()) 
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
	
	
};

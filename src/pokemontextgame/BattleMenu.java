package pokemontextgame;
import java.util.Scanner;

abstract class BattleMenu {
	/*
	 * Classe que lida com receber as entradas
	 * do jogador e envía-lo para outras
	 * camadas do Menu ou enviar as escolhas finais
	 * para uma função de efetuação de turno.
	 */
	
	// TODO: Filosofia do nosso design: Toda função roda uma vez só, mas pode ter
	// dentro dela um loop disparando outras funções.
	// Exemplo: Uma função de receber uma entrada sempre vai recebê-la apenas uma vez.
	// Se quisermos fazer um loop de recepção, deverá ser fora dessa função,
	// mas chamando a ela quantas vezes quisermos
	
	// TODO: Talvez possamos montar isso com ENUMs e máquinas de estado finita
	
	static void printMenuSeparator() {
		/*
		 * Imprime um separador de menu bem longo.
		 * Essa função é chamada várias vezes
		 * por vários elementos de menu.
		 */
		
		System.out.print("/ / / / / / / / / / / / / / / "
				+ "/ / / / / / / / / / / / / / / / / / /"
				+ " / / / / / / / / / / / / / / / / / / "
				+ "/ / / / / / / / / / / / / / / / / / / "
				+ "/ / / / / / / / / / / / / / / / / / \n");
	}
	
	static int scanOption(Scanner scan) {
		/*
		 * Recebe a opção de um jogador como uma linha.
		 * Retorna o valor inteiro do primeiro caractere da linha.
		 * 
		 * TODO: Deve ter uma forma mais elegante de receber o inteiro sem bugar tudo.
		 */
		
		String optString = scan.nextLine();
		if (!(optString.length() == 1))
			return -1; // check contra "enter" sem nada ou dígitos demais
		int optInt = (int) optString.charAt(0) - 48; //constante de conversão char para int
		return optInt;
	}
	
	static boolean validateOption(int opt, int optionCount) {
		/*
		 * Verifica se a opção (inteiro) do jogador está dentro
		 * da faixa de opções permitidas (no caso, 0 - num de opções)
		 * Retorna true se estiver, false se não estiver.
		 */
		
		// Por padrão, opções vão de 0 ao optionCount
		if(0 <= opt && opt < optionCount)
			return true;
		else
			return false;
	}
	
	static int scanOptionLoop(Scanner scan, int optionCount) {
		/*
		 * Função que combina scanOption e validadeOption
		 * num loop que só termina quando a opção recebida é válida.
		 * "optionCount" é o número de opções do menu analisado.
		 * Retorna -1 por padrão se a opção for inválida, 
		 * e a opção em si se for válida.
		 */
		
		boolean flag = false;
		int option = -1; // inicialização obrigatória
		// Tenta receber a entrada inteiro do jogador, não sai até receber opção válida
		while(!flag) {
			option = BattleMenu.scanOption(scan);
			flag = BattleMenu.validateOption(option, optionCount);
			if(!flag) {
				System.out.print("Opção inválida. Tente novamente: ");
			}
		}
		
		return option;
	}
	
	static void menuDisplayRoot(Scanner scan, Battlefield field) {
		/*
		 * Exibe as 4 opções básicas de menu
		 * que um jogador pode escolher.
		 */

		BattleMenu.printMenuSeparator();
		System.out.print("Pokémon ativo: " + "'" + field.getLoadedPlayer().getActiveMon().getName() + "'" + "\n");
		System.out.print("Suas opções são: \n");
		System.out.print("[0] Lutar \n");
		System.out.print("[1] Inspecionar seu time \n");
		System.out.print("[2] Acessar sua mochila \n");
		System.out.print("[3] Fugir \n");
		System.out.print("Digite sua opção e aperte ENTER: ");
		
		// Recebe a entrada de opção do jogador até que escolha uma opção válida
		int option = BattleMenu.scanOptionLoop(scan, 4);
		
		// TODO: Talvez exista uma opção mais elegante que essa das Switches
		switch(option) {
			// Lutar
			case 0:{
				menuDisplayMoveset(scan, field.getLoadedPlayer().getActiveMon(), false, field); // envia o Poke ativo
				break;
			} 
			// Ver pokes
			case 1:{
				menuDisplayTeam(scan, field); 
				break; 
			}
			// Mochila
			case 2:{
				menuDisplayBag(scan, field);  /* TODO */ 
				break;
			} 
			// Fugir
			case 3:{
				menuTryEscape(scan, field); 
				break; /*TODO*/ 
			}
		}
	}
	
	static void menuDisplayMoveset(Scanner scan, Poke mon, boolean isInspecting, Battlefield field) {
		/*
		 * Recebe um Poke e exibe informações de seu moveset.
		 * Permite que acessemos mais um menu sobre o ataque escolhido
		 * ou que voltemos para o menu anterior.
		 * 
		 * Se a flag Inspection estiver ligada, podemos apenas selecionar moves
		 * para visualização, mas nunca para uso.
		 */
		BattleMenu.printMenuSeparator();
		System.out.print("Suas opções são: \n");
		System.out.print("[0] Voltar \n");
		System.out.print("Ou explorar os Moves: \n");
		
		// Imprimir apenas os moves existentes (não importa se há PP ou não)
		Move currentMove;
		int movecount = 0;
		int i;
		for(i = 0; i < 4; i++) {
			currentMove = mon.getMove(i); // Varre moves do Poke selecionado
			if(!(currentMove == null)) {
				System.out.print("[" + String.valueOf(i + 1) + "] "  + currentMove.getNome()
				+ " | " + TypeChart.typeToString(currentMove.getTipagem())
				+ " | PP = " + currentMove.getPoints() +  "/" + currentMove.getMaxPoints() + "\n");
				movecount++;
			}
		}
		
		System.out.print("Digite sua opção e aperte ENTER: ");
		
		int option = BattleMenu.scanOptionLoop(scan, movecount + 1); // Aceita apenas quantos moves houver + opção de retorno
		
		// Disparando opção selecionada
		if(option == 0) {
			if(isInspecting)
				BattleMenu.menuDisplayMon(scan, mon, field);
			else
				BattleMenu.menuDisplayRoot(scan, field);
		}
		else {
			BattleMenu.menuDisplayMove(scan, mon, mon.getMove(option - 1), isInspecting, field); // Sendo "option - 1" o Index do Move escolhido
		}
	}
	
	static void menuDisplayMove(Scanner scan, Poke mon, Move move, boolean isInspecting, Battlefield field) {
		/*
		 * Verifica se o pokemon dono desse move é ativo.
		 * Se for, abre opções para uso.
		 * Caso contrário, apenas mostra informações sobre Move.
		 * No caso do boolean "isInspecting", não também não há opção de uso
		 */
		
		int option;
		BattleMenu.printMenuSeparator();
		if(!isInspecting) {
			// Ativo possui mais opções
			System.out.print("Selecionamos '" + move.getNome() + "'. " + "Suas opções são: \n");
			System.out.print("[0] Voltar \n");
			System.out.print("[1] Usar \n");
			System.out.print("[2] Ler informações sobre o move \n");
			System.out.print("Digite sua opção e aperte ENTER: ");
			option = BattleMenu.scanOptionLoop(scan, 3);
		}
		else {
			// Não ativo ou ativo inspecting só pode ler sobre o move e voltar ao moveset
			System.out.print(move.toString());
			System.out.print("Digite [0] e aperte ENTER para voltar: ");
			option = BattleMenu.scanOptionLoop(scan, 1);
		}
		
		// Segue com opções extra para Poké ativo
		switch(option) {
			case 0: {
				BattleMenu.menuDisplayMoveset(scan, mon, isInspecting, field); 
				break;
			}
			case 1: {
				/*TODO PREENCHER COM FUNC. PARA REGISTRAR A OPÇÃO DE ATAQUE*/
				// TODO: Escolha de move dispara ao BATTLEFIELD a opção de move
				System.out.print("Chegou aqui!\n");
				break;
			}
			case 2: {
				// Lê sobre o move, mas volta ao display MOVE, não ao display MOVESET, pois não exauriu opções
				BattleMenu.printMenuSeparator();
				System.out.print(move.toString());
				System.out.print("Digite [0] e aperte ENTER para voltar: ");
				option = BattleMenu.scanOptionLoop(scan, 1);
				//BattleMenu.menuDisplayMove(scan, mon, move, isInspecting, field);
				BattleMenu.menuDisplayMoveset(scan, mon, isInspecting, field);
				break;
			}
		}
	}
	
	static void menuDisplayTeam(Scanner scan, Battlefield field) {
		/*
		 * Recebe um Treinador e o scan e mostra todos os 
		 * pokemons disponíveis e seus statuses (hp, fainted, burned, etc)
		 * Abre opções para ver cada pokemon em detalhe (moveset, tipos, abilidade)
		 * ou trocar o pokemon ativo para o selecionado.
		 * (Se terá sucesso ou não será checado em TODO outra função).
		 */
		
		// TODO: Criar novos pokemons e novos ataques para eles para verificar se essas funções estão sendo tiro e queda mesmo
		// Parte de impressão de opções
		
		BattleMenu.printMenuSeparator();
		System.out.print("Acessando informações de time... \n");
		if(!field.getLoadedPlayer().isForcedSwitch()) {
			System.out.print("Suas opções são: \n");
			System.out.print("[0] Voltar \n");
			System.out.print("Ou inspecionar os Pokémons: \n");
		}
		else {
			System.out.print("Escolha o próximo Pokémon para batalhar: \n");
		}
		
		int monCount = BattleMenu.menuPrintTeam(field.getLoadedPlayer()); // Imprimindo e obtendo tamanho do time
		
		// Parte de recepção de opções
		System.out.print("Digite sua opção e aperte ENTER: ");
		
		int option;
		if(!field.getLoadedPlayer().isForcedSwitch()) { // Caso de troca possível
			option = BattleMenu.scanOptionLoop(scan, monCount + 1); // Aceita apenas quantos mons houver + opção de retorno
			// Disparando opção selecionada
					if(option == 0) 
						BattleMenu.menuDisplayRoot(scan, field);
					else {
						BattleMenu.menuDisplayMon(scan, field.getLoadedPlayer().getTeam()[option - 1], field); // Sendo "option - 1" o Index do Pokemon escolhido
					}
		}
		else { // Caso de troca forçada
			option = BattleMenu.scanOptionLoop(scan, monCount); // num Opções = pokemons disponíveis
			BattleMenu.menuDisplayMon(scan, field.getLoadedPlayer().getTeam()[option], field); // agora o índice do Poke escolhido é o mesmo de option
		}
		
	}
	
	static int menuPrintTeam(Treinador player) {
		/*
		 * Função que apenas imprime o time inteiro.
		 * Retorna o número de pokemons no time.
		 */
		
		Poke curMon;
		String monString;
		int monCount = 0;
		int i;
		for (i = 0; i < 6; i++) {
			// Se lá houver pokemon
			curMon = player.getTeam()[i];
			if(curMon != null) {
				monCount++;
				monString = ("[" + (i + 1) + "] " + curMon.getName() + " | Lv. " + curMon.getLevel()
				+ "| " + TypeChart.fullTypeToString(curMon));
				
				// Checando Fainted
				if(curMon.isFainted())
					monString += " | " + "FAINTED";
				else
					monString += " | Hp " + curMon.getCurHp() + "/" + curMon.getMaxHp();
				
				// Checando status.
				if(!(curMon.getStatusFx().getId() == -1))
					monString += " | Status: " + curMon.getStatusFx().getName();
				
				// Print final para este pokemon
				monString += "\n";
				System.out.print(monString);
			}
		}
		
		return monCount;
	}

	static void menuDisplayMon(Scanner scan, Poke mon, Battlefield field) {
		/*
		 * Recebe um Treinador e o ID do Pokemon a ser inspecionado.
		 * Printa suas informações e aguarda  opção por parte do jogador.
		 * TODO: Dar um jeito de "salvarmos" o menu prévio e fazer a opção voltar sempre puxar para ele.
		 * Dessa forma, podemos reutilizar o displayMoveset e displayMove para pokemons não ativos
		 * */
		BattleMenu.printMenuSeparator();
		
		// Opções: Voltar, Ver Ataques, Sumário, TROCAR (se não for ativo)
		System.out.print("Pokémon selecionado: '" + mon.getName() +  "'\n");
		System.out.print("Suas opções são: \n");
		System.out.print("[0] Voltar \n");
		System.out.print("[1] Sumário \n");
		System.out.print("[2] Explorar Moves \n");
		
		int option;
		int optionCount = 0;
	
		// if(player.isForcedSwitch()) { } // voltaremos mais tarde TODO
		
		// Se for ativo
		if(!field.getLoadedPlayer().isForcedSwitch() && mon.isActive()) {
			System.out.print("Pokémon já está em batalha. \n");
			optionCount = 3;
		}
		// Se o Poke atual não for ativo e não estiver fainted, podemos trocar para ele em batalha
		else if(mon.isFainted()){
			System.out.print("Pokémon está FAINTED. \n");
			optionCount = 3;
		}
		else {
			System.out.print("[3] Trocar Pokémon ativo para este Pokémon \n");
			optionCount = 4;
		}
		
		System.out.print("Digite sua opção e aperte ENTER: ");
		option = BattleMenu.scanOptionLoop(scan, optionCount);
		// Matriz de opções
		switch(option) {
			// Voltar
			case(0):{
				BattleMenu.menuDisplayTeam(scan, field);
				break;
			}
			// Sumário do Poke
			case(1):{
				BattleMenu.printMenuSeparator();
				System.out.print(mon.toString());
				System.out.print("Digite [0] e aperte ENTER para voltar: ");
				option = BattleMenu.scanOptionLoop(scan, 1);
				BattleMenu.menuDisplayMon(scan, mon, field);
				break;
			}
			// Explorar Moves
			case(2):{
				// PROBLEMA: O "voltar" do próximo displayMoveset deve ser para cá, não para ROOT, e não pode permitir utilizar os ataques
				// isso é resolvido com a flag "isInspecting"
				BattleMenu.menuDisplayMoveset(scan, mon, true, field); 
				break;
			}
			// Trocar para este
			case(3):{
				/*TODO Escolha de troca dispara ao BATTLEFIELD a tentativa de troca de Poke */; 
				break;
			}
		}
	}
	
	static void menuDisplayBag(Scanner scan, Battlefield field) {
		/*
		 * Leva a um menu com todos os itens da mochila
		 * divididos em compartimentos, que podem ou não
		 * estar vazios.
		 * 
		 * TODO: Por enquanto, sempre estará vazio. Deveremos implementar a bag e itens mais tarde.
		 */
		
		System.out.print("Sua mochila está vazia!\n");
		System.out.print("Digite [0] e aperte ENTER para voltar: ");
		BattleMenu.scanOptionLoop(scan, 1);
		BattleMenu.menuDisplayRoot(scan, field);
		
		// TODO: Navegação de itens
		
		// TODO: Escolha de itens dispara ao BATTLEFIELD a opção de itens
	}
	
	static void menuTryEscape(Scanner scan, Battlefield field) {
		/*
		 * Se for possível tentar, envia a opção de tentar fugir.
		 * Não é possível tentar em batalhas contra treinadores,
		 * apenas contra Pokémons selvagens, algo que não ocorrerá por ora.
		 * Não faz nada para calcular as chances de sucesso em si.
		 */
		
		if(field.isTrainerBattle()) {
			System.out.print("Não! Não se pode fugir de uma batalha contra outro treinador!\n");
			System.out.print("Digite [0] e aperte ENTER para voltar: ");
			BattleMenu.scanOptionLoop(scan, 1);
			BattleMenu.menuDisplayRoot(scan, field);
		}
		
		// Caso contrário, dispara ao BATTLEFIELD a opção de fuga
	}
	
	static void menuSelectMove() {
		/*
		 * Função que retorna o move selecionado. Talvez seja redundante.
		 */
	}

}

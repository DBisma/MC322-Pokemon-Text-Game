package pokemontextgame;
import java.util.Scanner;

import moves.Move;
import pokemontextgame.Battlefield.Choice;
import pokemontextgame.StatusFx.typeList;

public class BattleMenu {
	/*
	 * Classe que lida com receber as entradas do jogador
	 * e envía-lo para outras camadas do Menu ou enviar as escolhas finais
	 * para a classe BattleField, que tem funções de efetuação de turno.
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
	
	static boolean validateOption(int opt, int lowerBound, int optionCount) {
		/*
		 * Verifica se a opção (inteiro) do jogador está dentro
		 * da faixa de opções permitidas (no caso, 0 - num de opções)
		 * Retorna true se estiver, false se não estiver.
		 */
		
		// Por padrão, opções vão de 0 ao optionCount
		if(lowerBound <= opt && opt < lowerBound + optionCount)
			return true;
		else
			return false;
	}
	
	static int scanOptionLoop(Scanner scan, int lowerBound, int optionCount) {
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
			flag = BattleMenu.validateOption(option, lowerBound, optionCount);
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
		int option = BattleMenu.scanOptionLoop(scan, 0, 4);
		
		// TODO: Talvez exista uma opção mais elegante que essa das Switches
		switch(option) {
			// Lutar
			case 0:{
				menuDisplayMoveset(scan, 0, false, field); // envia o Poke ativo, sempre de id 0
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
	
	static void menuDisplayMoveset(Scanner scan, int id, boolean isInspecting, Battlefield field) {
		/*
		 * Recebe um Poke e exibe informações de seu moveset.
		 * Permite que acessemos mais um menu sobre o ataque escolhido
		 * ou que voltemos para o menu anterior.
		 * 
		 * Se a flag Inspection estiver ligada, podemos apenas selecionar moves
		 * para visualização, mas nunca para uso.
		 */
		
		Poke mon = field.getLoadedPlayer().getTeam()[id];
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
		
		int option = BattleMenu.scanOptionLoop(scan, 0, movecount + 1); // Aceita apenas quantos moves houver + opção de retorno
		
		// Disparando opção selecionada
		if(option == 0) {
			if(isInspecting)
				BattleMenu.menuDisplayMon(scan, id, field);
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
			option = BattleMenu.scanOptionLoop(scan, 0, 3);
		}
		else {
			// Não ativo ou ativo inspecting só pode ler sobre o move e voltar ao moveset
			System.out.print(move.toString());
			System.out.print("Digite [0] e aperte ENTER para voltar: ");
			option = BattleMenu.scanOptionLoop(scan, 0, 1);
		}
		
		// Segue com opções extra para Poké ativo
		switch(option) {
			case 0: {
				BattleMenu.menuDisplayMoveset(scan, 0, isInspecting, field); 
				break;
			}
			case 1: {
				// Escolha de move atualiza a playerChoice do Battlefield field
				// Se faltar PP
				if(move.getPoints() == 0) {
					System.out.print("Não há PP o suficiente para este move!\n");
					System.out.print("Digite [0] e aperte ENTER para voltar: ");
					option = BattleMenu.scanOptionLoop(scan, 0, 1);
					BattleMenu.menuDisplayMove(scan, mon, move, isInspecting, field);
					break;
				}
				else {
					// envia enfim a opção de ataque
					field.getPlayerChoice().setFullChoice(Choice.choiceType.ATTACK, option);
					break;
				}
			}
			case 2: {
				// Lê sobre o move, mas volta ao display MOVE, não ao display MOVESET, pois não exauriu opções
				BattleMenu.printMenuSeparator();
				System.out.print(move.toString());
				System.out.print("Digite [0] e aperte ENTER para voltar: ");
				option = BattleMenu.scanOptionLoop(scan, 0, 1);
				BattleMenu.menuDisplayMove(scan, mon, move, isInspecting, field);
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
		 * 
		 * TODO: Impedir possibilidade de escolher o pokemon atual ativo
		 * TODO: Essa função está um lixo. Talvez seja melhor reescrevê-la do zero.
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
			option = BattleMenu.scanOptionLoop(scan, 0, monCount + 1); // Aceita apenas quantos mons houver + opção de retorno
			// Disparando opção selecionada
					if(option == 0) 
						BattleMenu.menuDisplayRoot(scan, field);
					else {
						BattleMenu.menuDisplayMon(scan, option - 1, field); // Sendo "option - 1" o Index do Pokemon escolhido
					}
		}
		else { // Caso de troca forçada
			option = BattleMenu.scanOptionLoop(scan, 1, monCount); // num Opções = pokemons disponíveis com um offset
			BattleMenu.menuDisplayMon(scan, option - 1, field); // idem, mas opção "voltar" não existe
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
				typeList currentStatus = curMon.getStatusFx().getType();
				if(currentStatus != typeList.NEUTRAL)
					monString += " | Status: " + currentStatus;
				
				// Print final para este pokemon
				monString += "\n";
				System.out.print(monString);
			}
		}
		
		return monCount;
	}

	static void menuDisplayMon(Scanner scan, int id, Battlefield field) {
		/*
		 * Recebe um Treinador e o ID do Pokemon a ser inspecionado.
		 * Printa suas informações e aguarda  opção por parte do jogador.
		 * TODO: Dar um jeito de "salvarmos" o menu prévio e fazer a opção voltar sempre puxar para ele.
		 * Dessa forma, podemos reutilizar o displayMoveset e displayMove para pokemons não ativos.
		 * ID é o índice do pokemon sendo observado.
		 * */
		Poke mon = field.getLoadedPlayer().getTeam()[id];
		BattleMenu.printMenuSeparator();
		
		// Opções: Voltar, Ver Ataques, Sumário, TROCAR (só se não for ativo)
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
		option = BattleMenu.scanOptionLoop(scan, 0, optionCount);
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
				option = BattleMenu.scanOptionLoop(scan, 0, 1);
				BattleMenu.menuDisplayMon(scan, id, field);
				break;
			}
			// Explorar Moves
			case(2):{
				// O "voltar" do próximo displayMoveset deve ser para cá, não para ROOT, e não pode permitir utilizar os ataques
				// isso é resolvido com a flag "isInspecting"
				BattleMenu.menuDisplayMoveset(scan, id, true, field); 
				break;
			}
			// Trocar para este
			case(3):{
				field.setFullPlayerChoice(Choice.choiceType.SWITCH, id); // envia id do poke para qual iremos trocar
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
		BattleMenu.scanOptionLoop(scan, 0, 1);
		BattleMenu.menuDisplayRoot(scan, field);
		
		// TODO: Navegação de itens
		
		// TODO: Escolha de itens dispara ao BATTLEFIELD a opção de itens... mas isso virá mais tarde
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
			BattleMenu.scanOptionLoop(scan, 0, 1);
			BattleMenu.menuDisplayRoot(scan, field);
		}
		else
			field.setFullPlayerChoice(Choice.choiceType.RUN, 0);
	}
}


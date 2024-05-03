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
		if (optString.length() == 0)
			return -1; // check contra "enter" sem nada
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
		 * Retorna -1 por padrão se a opção for inválida, e
		 * a opção em si se for válida.
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
	
	static void menuDisplayRoot(Scanner scan, Treinador player) {
		/*
		 * Exibe as 4 opções básicas
		 * que um treinador pode efetuar.
		 */
		
		BattleMenu.printMenuSeparator();
		System.out.print("Pokémon ativo: " + "'" + player.getActiveMon().getNome() + "'" + "\n");
		System.out.print("Suas opções são: \n");
		System.out.print("[0] Lutar \n");
		System.out.print("[1] Ver seus Pokémons \n");
		System.out.print("[2] Acessar sua mochila \n");
		System.out.print("[3] Fugir \n");
		System.out.print("Digite sua opção e aperte ENTER: ");
		
		// Recebe a entrada de opção do jogador até que seja uma opção válida
		int option = BattleMenu.scanOptionLoop(scan, 4);
		
		// TODO: Talvez exista uma opção mais elegante que essa das Switches
		switch(option) {
			// Lutar
			case 0:{menuDisplayMoveset(scan, player.getActiveMon(), player); break;} // envia o poke ativo
			// Ver pokes
			case 1:{menuDisplayTeam(); break; /*TODO*/ }
			// Mochila
			case 2:{menuDisplayBag(); break; /*TODO*/ } 
			// Fugir
			case 3:{menuTryEscape(); break; /*TODO*/ }
		}
	}
	
	static void menuDisplayMoveset(Scanner scan, Poke mon, Treinador player) {
		/*
		 * Recebe um Poke e exibe informações de seu moveset.
		 * Permite que acessemos mais um menu sobre o ataque escolhido
		 * ou que voltemos para o menu anterior.
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
				System.out.print("[" + String.valueOf(i + 1) + "] "  + currentMove.getNome() + " | PP = " + currentMove.getPoints() +  " / " + currentMove.getMaxPoints() + "\n");
				movecount++;
			}
		}
		
		System.out.print("Digite sua opção e aperte ENTER: ");
		
		int option = BattleMenu.scanOptionLoop(scan, movecount + 1); // Aceita apenas quantos moves houver + opção de retorno
		
		// Disparando opção selecionada
		if(option == 0) 
			BattleMenu.menuDisplayRoot(scan, player);
		else {
			BattleMenu.menuDisplayMove(scan, mon, mon.getMove(option - 1), player); // Sendo "option - 1" o Index do Move escolhido
		}
	}
	
	static void menuDisplayMove(Scanner scan, Poke mon, Move move, Treinador player) {
		/*
		 * Verifica se o pokemon dono desse move é ativo.
		 * Se for, abre opções para uso.
		 * Caso contrário, apenas mostra informações sobre Move.
		 */
		
		int option;
		BattleMenu.printMenuSeparator();
		if(mon.isActive()) {
			// Ativo possui mais opções
			System.out.print("Selecionamos '" + move.getNome() + "'. " + "Suas opções são: \n");
			System.out.print("[0] Voltar \n");
			System.out.print("[1] Usar \n");
			System.out.print("[2] Ler informações sobre o move \n");
			System.out.print("Digite sua opção e aperte ENTER: ");
			option = BattleMenu.scanOptionLoop(scan, 3);
		}
		else {
			// Não ativo só pode ler sobre o move e voltar ao moveset
			System.out.print(move.toString());
			System.out.print("Digite [0] e aperte ENTER para voltar:");
			option = BattleMenu.scanOptionLoop(scan, 1);
			BattleMenu.menuDisplayMoveset(scan, mon, player); // sai dessa função, vai para MOVESET
		}
		
		// Segue com opções extra para Poké ativo
		switch(option) {
			case 0: {BattleMenu.menuDisplayMoveset(scan, mon, player); break;}
			case 1: {
				/*TODO PREENCHER COM FUNC. PARA REGISTRAR A OPÇÃO DE ATAQUE*/
				break;
			}
			case 2: {
				// Lê sobre o move, mas volta ao display MOVE, não ao display MOVESET, pois não exauriu opções
				BattleMenu.printMenuSeparator();
				System.out.print(move.toString());
				System.out.print("Digite [0] e aperte ENTER para voltar:");
				option = BattleMenu.scanOptionLoop(scan, 1);
				BattleMenu.menuDisplayMove(scan, mon, move, player);
				break;
			}
		}
	}
	
	static void menuDisplayTeam() {
		/*
		 * Recebe um Treinador e o scan e mostra todos os 
		 * pokemons disponíveis e seus statuses (hp, fainted, burned, etc)
		 * Abre opções para ver cada pokemon em detalhe ou 
		 * trocar o pokemon ativo 
		 * (essa possibilidade é checada em TODO outra função).
		 */
	}
	
	static void menuDisplayBag() {
		/*
		 * Leva a um menu com todos os itens da mochila
		 * divididos em compartimentos, que podem ou não
		 * estar vazios.
		 */
	}
	
	static void menuTryEscape() {
		/*
		 * Se for possível tentar, envia a opção de tentar fugir.
		 * Não é possível tentar em batalhas contra treinadores,
		 * apenas contra Pokémons selvagens, algo que não ocorrerá por ora.
		 * Não faz nada para calcular as chances de sucesso em si.
		 */
	}
	
	static void menuSelectMove() {
		/*
		 * Função que retorna o move selecionado. Talvez seja redundante.
		 */
	}

}

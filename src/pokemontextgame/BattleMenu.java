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
	
	static void menuDisplayRoot(Scanner scan) {
		/*
		 * Exibe as 4 opções básicas
		 * que um treinador pode efetuar.
		 */
		System.out.print("/ / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / \n");
		System.out.print("Suas opções são: \n");
		System.out.print("[0] Lutar \n");
		System.out.print("[1] Ver seus Pokémons \n");
		System.out.print("[2] Acessar sua mochila \n");
		System.out.print("[3] Fugir \n");
		System.out.print("Digite sua opção e aperte ENTER: ");
		
		boolean flag = false;
		int option;
		// Tenta receber a entrada inteiro do jogador, não sai até receber opção válida
		while(!flag) {
			option = BattleMenu.scanOption(scan);
			flag = BattleMenu.validateOption(option, 4);
			if(!flag) {
				System.out.print("Opção inválida. Tente novamente: ");
			}
		}
		
		System.out.print("Opção válida registrada. \n");
		
		// sendo a opção válida, podemos avançar para o menu seguinte desejado.
		
	}
	
	static void menuDisplayTeam() {}
	
	static void menuDisplayMoves() {}
}

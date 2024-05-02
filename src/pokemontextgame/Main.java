package pokemontextgame;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		/*
		 * A classe main chama as outras funções para executar a batalha em texto.
		 */
		
		// Inicializando dois treinadores
		Treinador player = new Treinador(true);
		Treinador npc = new Treinador(false);
		
		// Scan que será fechado ao final do arquivo
		Scanner scan = new Scanner(System.in);
		
		// Receber nome do jogador e do npc
		player.inputName(scan);
		npc.inputName(scan);
		
		// TODO: apagar esse checkpoint
		System.out.print("Parabéns! Nomes lidos com sucesso. \n");
		
		// Parte 2: Escolher os pokemons do Treinador
		
		// Vamos criar um pokemon qualquer e atribuí-lo ao jogador.
		Poke exampleMetang = new Poke(376); // recebe pokedex ID, mas ficará melhor mais tarde.
		player.setTeam(0, exampleMetang);
		
		/*
		 * TODO
		 * E deveremos criar um exampleMove para o Metang
		 * E uma example Ability
		 * E uma example HeldItem
		 * e um e um eum eu mem emme eu perdi o pé
		 */

		
		/*
		 * TODO: MAIS URGENTE: LER E CONSTRUIR USANDO INFORMAÇÃO DE UM JSON.
		 * CADA POKEMON POSSUI UMA LISTA NO JSON DE CADA MOVE QUE PODE APRENDER.
		 * CADA MOVE POSSUI UM JSON QUE EXPLICITA COMO A CLASSE É CONSTRUÍDA.
		 * Podemos fazer cada move ser um método próprio que recebe o Battlefield,
		 * o pokemon atacante e o pokemon receptor. Ele então aplica
		 * as mudanças sobre o battlefield e calcula efeitos sobre o pokemon receptor / atacante.
		 */
		/*
		 * TODO: Precisamos exibir uma lista de pokemons possíveis carregados de uma pasta cheia de jsons;
		 * TODO: Jogador precisa escolher entre 1 e 6 pokemons da lista, e eles devem ser construídos e postos
		 * em seu time; 
		 * TODO: Inimigo precisa obter os pokemons restantes aleatoriamente;
		 * TODO: Dar opção do jogador escolher aleatoriamente seus pokemons.
		 */
		
		/*
		 * TODO: Para cada um dos pokemons, devemos construir suas habilidades, moves e itens segurados.
		 */
		scan.close();
	}
	
}

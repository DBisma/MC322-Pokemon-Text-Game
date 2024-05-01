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
		
		/*
		 * TODO: Precisamos exibir uma lista de pokemons possíveis carregados de uma pasta cheia de jsons;
		 * TODO: Jogador precisa escolher seus 6 pokemons da lista, e eles devem ser construídos e postos
		 * em seu time;
		 * TODO: Inimigo precisa obter os pokemons restantes aleatoriamente;
		 * TODO: Dar opção do jogador escolher aleatoriamente seus pokemons.
		 */
		
		scan.close();
	}
	
}

package pokemontextgame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONException;

import pokemontextgame.moves.*;

public class Main {
	public static void main(String[] args) throws IOException, JSONException {
		/*
		 * A classe main chama as outras funções para executar a batalha em texto.
		 */	
		
		// Scan que será fechado ao final do arquivo
		Scanner scan = new Scanner(System.in);
		
		// Inicializá-los
		Treinador player = new Treinador(1, "Placeholder", true);
		TreinadorNpc npc = new TreinadorNpc(2, "Placeholder", false);
		
		// Receber nome do jogador e do npc
		
		player.inputName(scan);
		npc.inputName(scan);
		
		// Parte 2: Escolher os pokemons do Treinador
		
		// Vamos criar alguns pokemons quaisquer e atribuí-los ao jogador e ao npc

		// TODO: Talvez seja uma boa colocar isso dentro de um escopo para não gastarmos memória
		JSONReader jsonReader = new JSONReader();
		ArrayList<Move> moveArray = new ArrayList<Move>();
		ArrayList<Poke> pokeArray = new ArrayList<Poke>();
		ArrayList<Integer> pkmnUniqueIds = new ArrayList<Integer>();
		
		jsonReader.buildMoves();
		jsonReader.buildPokemons();
		
		moveArray = jsonReader.getMoveList();
		pokeArray = jsonReader.getPkmnList();
		pkmnUniqueIds = jsonReader.getPkmnUniqueIDs();
		
		// Montando times aleatórios:
		for(int i = 0; i < 6; i++) {
			// Buscando um índice aleatório dentro do Array de Ids (entre 0 e seu tamanho)
			Random r = new Random();
			int roll = r.nextInt(pkmnUniqueIds.size());
			player.setTeam(i, pokeArray.remove(roll)); // removemos esse pokemon do PokeArray, damos ao player
			pkmnUniqueIds.remove(roll); // e removemos esse ID do array de IDs únicos
			
			roll = r.nextInt(pkmnUniqueIds.size()); // novo índice aleatório com tamanho atualizado
			npc.setTeam(i, pokeArray.remove(roll)); // repete para npc
			pkmnUniqueIds.remove(roll);
		}
		
		jsonReader.atribuiMoveAPoke(player);
		jsonReader.atribuiMoveAPoke(npc);
			
		// Parte do jogo em si
		// Inicializar campo com jogadores e primeiro turno
		Battlefield field = new Battlefield(player, npc, true);
		
		// Loop de turno até a batalha acabar
		field.turnLoops(scan);
		
		// impedindo resource leak
		scan.close();
		
		// Fim.
		BattleMenu.printPokeballAscii();
	}
	
}

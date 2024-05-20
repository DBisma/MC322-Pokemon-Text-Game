package pokemontextgame;
import java.io.IOException;
import java.util.ArrayList;
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

		JSONReader jsonReader = new JSONReader();
		ArrayList<Move> moveArray = new ArrayList<Move>();
		ArrayList<Poke> pokeArray = new ArrayList<Poke>();
		ArrayList<Integer> pkmnUniqueIds = new ArrayList<Integer>();
		
		jsonReader.buildMoves();
		jsonReader.buildPokemons();
		
		moveArray = jsonReader.getMoveList();
		pokeArray = jsonReader.getPkmnList();
		pkmnUniqueIds = jsonReader.getPkmnUniqueIDs();
		
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
		    int max = pkmnUniqueIds.size();
		    int rndmIndex = random.nextInt(max);
		    int rndmPokeId = pkmnUniqueIds.remove(rndmIndex);
		    for (Poke poke : pokeArray) {
		        if (poke.getId() == rndmPokeId) {
		            player.setTeam(i, poke);
		            pkmnUniqueIds.remove(Integer.valueOf(rndmPokeId));
		            break;
		        }
		    }
		}
		
		for (int i = 0; i < 6; i++) {
		    int max = pkmnUniqueIds.size();
		    int rndmIndex = random.nextInt(max);
		    int rndmPokeId = pkmnUniqueIds.get(rndmIndex);
		    for (Poke poke : pokeArray) {
		        if (poke.getId() == rndmPokeId) {
		            npc.setTeam(i, poke);
		            pkmnUniqueIds.remove(Integer.valueOf(rndmPokeId));
		            break;
		        }
		    }
		}
		
		jsonReader.atribuiMoveAPoke(player);
		jsonReader.atribuiMoveAPoke(npc);
		
		for (int i = 0; i < 6; i++) {
			System.out.println(player.getTeam()[i].toString());
		}
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

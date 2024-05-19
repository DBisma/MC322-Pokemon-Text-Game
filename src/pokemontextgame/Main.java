package pokemontextgame;
import java.io.IOException;
import java.util.ArrayList;
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
		
		// TODO: Usar REGEX e um Matcher.
		player.inputName(scan);
		npc.inputName(scan);
		
		// Parte 2: Escolher os pokemons do Treinador
		
		// Vamos criar alguns pokemons quaisquer e atribuí-los ao jogador e ao npc

		JSONReader jsonReader = new JSONReader();
		ArrayList<Move> moveArray = new ArrayList<Move>();
		ArrayList<Poke> pokeArray = new ArrayList<Poke>();
		
		jsonReader.buildMoves();
		jsonReader.buildPokemons();
		
		moveArray = jsonReader.getMoveList();
		pokeArray = jsonReader.getPkmnList();
		
		player.setTeam(0, pokeArray.get(2));
		jsonReader.atribuiMoveAPoke(player);
		//jsonReader.atribuiMoveAPoke(npc);
		
		System.out.println(player.getTeam()[0].toString());
		
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

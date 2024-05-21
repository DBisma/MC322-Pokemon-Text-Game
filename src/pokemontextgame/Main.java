package pokemontextgame;
import java.io.IOException;
import java.util.Scanner;
import org.json.JSONException;

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
		
		// Receber nome do jogador e do npc; construir seus times
		player.inputName(scan);
		npc.inputName(scan);
		Treinador.buildRandomTeams(player, npc);
			
		// Parte do jogo em si
		// Inicializar campo com jogadores e primeiro turno
		Battlefield field = new Battlefield(player, npc, true);
		// Loop de turno até a batalha acabar
		field.turnLoops(scan);
		// Fim de jogo
		BattleMenu.printPokeballAscii();
		
		// impedindo resource leak
		scan.close();
	}
	
}

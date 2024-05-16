package pokemontextgame;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;

import moves.*;

public class Main {
	public static void main(String[] args) throws IOException, JSONException {
		/*
		 * A classe main chama as outras funções para executar a batalha em texto.
		 */
		
		//
		//		TODO: REMOVER HABILIDADES, REMOVER HELD ITEMS, REMOVER WEATHER, REMOVER A BAG. TUDO ISSO É ACESSÓRIO.
		//		⣽⣿⢣⣿⡟⣽⣿⣿⠃⣲⣿⣿⣸⣷⡻⡇⣿⣿⢇⣿⣿⣿⣏⣎⣸⣦⣠⡞⣾⢧⣿⣿ 
		//		⣿⡏⣿⡿⢰⣿⣿⡏⣼⣿⣿⡏⠙⣿⣿⣤⡿⣿⢸⣿⣿⢟⡞⣰⣿⣿⡟⣹⢯⣿⣿⣿ 
		//		⡿⢹⣿⠇⣿⣿⣿⣸⣿⣿⣿⣿⣦⡈⠻⣿⣿⣮⣿⣿⣯⣏⣼⣿⠿⠏⣰⡅⢸⣿⣿⣿ 
		//		⡀⣼⣿⢰⣿⣿⣇⣿⣿⡿⠛⠛⠛⠛⠄⣘⣿⣿⣿⣿⣿⣿⣶⣿⠿⠛⢾⡇⢸⣿⣿⣿ 
		//		⠄⣿⡟⢸⣿⣿⢻⣿⣿⣷⣶⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⡋⠉⣠⣴⣾⣿⡇⣸⣿⣿⡏ 
		//		⠄⣿⡇⢸⣿⣿⢸⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣄⠘⢿⣿⠏⠄⣿⣿⣿⣹ 
		//		⠄⢻⡇⢸⣿⣿⠸⣿⣿⣿⣿⣿⣿⠿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣦⣼⠃⠄⢰⣿⣿⢯⣿ 
		//		⠄⢸⣿⢸⣿⣿⡄⠙⢿⣿⣿⡿⠁⠄⠄⠄⠄⠉⣿⣿⣿⣿⣿⣿⡏⠄⢀⣾⣿⢯⣿⣿
		//		⣾⣸⣿⠄⣿⣿⡇⠄⠄⠙⢿⣀⠄⠄⠄⠄⠄⣰⣿⣿⣿⣿⣿⠟⠄⠄⣼⡿⢫⣻⣿⣿
		//		⣿⣿⣿⠄⢸⣿⣿⠄⠄⠄⠄⠙⠿⣷⣶⣤⣴⣿⠿⠿⠛⠉⠄⠄ ⢸⣿⣿⣿⣿⠃⠄
		//		
		
		// Scan que será fechado ao final do arquivo
		
		JSONReader json = new JSONReader();
		ArrayList<Poke> pkmn = new ArrayList<Poke>();
		ArrayList<Item> items = new ArrayList<Item>();
		
		json.buildPokemons();
		pkmn = json.getPkmnList();
		
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
		Poke exJirachi1 = new Poke(1, "Jyroo"); // recebe pokedex ID, mas ficará melhor mais tarde.
		Poke exJirachi2 = new Poke(2, "John");
		Poke exJirachi3 = new Poke(3, "James");
		player.setTeam(0, exJirachi1);
		player.setTeam(0, exJirachi2);
		npc.setTeam(0, exJirachi3);
		
		// TODO: Novamente, mais tarde devemos construir com jsons.
		//Move exFirePunch1 = new DmgPlusFx(7, "Fire Punch", 1, 15, 0, 100, Move.moveCategs.PHYSICAL, 75, 
		//								StatusFx.typeList.BURN, 30);
		//Move exFirePunch2 = new DmgPlusFx(7, "Fire Punch", 1, 15, 0, 100, Move.moveCategs.PHYSICAL, 75, 
		//		StatusFx.typeList.BURN, 30);	
		//Move exHammerArm = new DamageDealing(359, "Hammer Arm", 6, 10, 0, 90, Move.moveCategs.PHYSICAL, 100);	
		//exJirachi1.setMove(0, exHammerArm);
		//exJirachi2.setMove(0, exFirePunch1);
		//exJirachi3.setMove(0, exFirePunch2);
		
		// E agora, a parte dos held items;
		// TODO: Como fazer cada um ter seu método próprio?
		// Esses itens que estamos criando ainda não possuem métodos, mas enfim...
		// usaremos para printar coisas mais pra frente
		Item exChoiceScarf = new Item(287, "Choice Scarf");
		Item exLeftovers = new Item(234, "Leftovers");
		
		exJirachi1.setHeldItem(exLeftovers);
		exJirachi2.setHeldItem(exChoiceScarf);
		
		// Agora, vamos inventar habilidades.
		Ability exClearBody = new Ability(29, "Clear Body");
		Ability exSandStream = new Ability(45, "Sand Stream");
		exJirachi1.setAbil(exClearBody);
		exJirachi2.setAbil(exSandStream);
		
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
		
		// Parte do jogo em si
		// Inicializar campo com jogadores e primeiro turno
		Battlefield field = new Battlefield(player, npc, true);
		// Colocar os dois treinadores no campo;
		
		field.turn(scan);
		
		// Setar o pokemo ativo como o pokemon no slot[0] no começo da batalha
		// "Renderizar" menu de batalha Root e receber opções do jogador
		
		// TODO: apagar esse checkpoint
		System.out.print("O resultado do roll 50/50 é: " + TurnUtils.rollChance(50)+ "\n");
		
		// impedindo resource leak
		scan.close();
	}
	
}

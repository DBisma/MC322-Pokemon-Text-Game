package pokemontextgame;
import java.io.IOException;
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
		
		//exJirachi3.dmgMon(280);
		//player.setTeam(0, myJirachi);
		//player.setTeam(1, secondJirachi);
		//npc.setTeam(0, foeJirachi);
		
		// TODO: Novamente, mais tarde devemos construir com jsons.
		/*Move exFirePunch1 = new DmgPlusFx(7, "Fire Punch", 1, 15, 0, 100, Move.moveCategs.PHYSICAL, 75, 
										StatusFx.typeList.BURN, 100);
		Move exFirePunch2 =  new DmgPlusFx(7, "Fire Punch", 1, 15, 0, 100, Move.moveCategs.PHYSICAL, 100, 
										StatusFx.typeList.BURN, 100);
		Move exHammerArm = new DamageDealing(359, "Hammer Arm", 6, 10, 0, 90, Move.moveCategs.PHYSICAL, 1);	
		Move testingMove = new DamageDealing(2, "Testing Move", 6, 10, 0, -1, Move.moveCategs.PHYSICAL, 1);	
		Move superSwordsDance = new StatChange(999, "Swords++", 0, 5, 0, -1, 0, 3, true);
		
		
		myJirachi.setMove(0, exHammerArm);
		myJirachi.setMove(1, exFirePunch1);
		myJirachi.setMove(2, superSwordsDance);
		foeJirachi.setMove(0, exFirePunch2);
		secondJirachi.setMove(0, testingMove);
		*/
		player.setTeam(0, pokeArray.get(2));
		jsonReader.atribuiMoveAPoke(player);
		//jsonReader.atribuiMoveAPoke(npc);
		
		System.out.println(player.getTeam()[0].toString());
		
		// E agora, a parte dos held items;
		// TODO: Como fazer cada um ter seu método próprio?
		// Esses itens que estamos criando ainda não possuem métodos, mas enfim...
		// usaremos para printar coisas mais pra frente
		Item exChoiceScarf = new Item(287, "Choice Scarf");
		Item exLeftovers = new Item(234, "Leftovers");
		
		/*
		myJirachi.setHeldItem(exLeftovers);
		myJirachi.setHeldItem(exChoiceScarf);
		
		// Agora, vamos inventar habilidades.
		Ability exClearBody = new Ability(29, "Clear Body");
		Ability exSandStream = new Ability(45, "Sand Stream");
		foeJirachi.setAbil(exClearBody);
		foeJirachi.setAbil(exSandStream);
		
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
		
		// Loop de turno até a batalha acabar
		
		field.turnLoops(scan);
		
//		field.turn(scan);
		// impedindo resource leak
		scan.close();
		
		// Fim.
		BattleMenu.printPokeballAscii();
	}
	
}

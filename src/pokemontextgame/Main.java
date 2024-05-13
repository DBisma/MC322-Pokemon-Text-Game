package pokemontextgame;
import java.util.Scanner;

import moves.*;

public class Main {
	public static void main(String[] args) {
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
		
		// Vamos criar um pokemon qualquer e atribuí-lo ao jogador.
		Poke exMetagross = new Poke(376, "Metagross"); // recebe pokedex ID, mas ficará melhor mais tarde.
		player.setTeam(0, exMetagross);
		player.setActiveMonId(0);
		
		// E vamos criar um Tyranitar para o NPC
		Poke exTyranitar = new Poke(248, "Tyranitar");
		npc.setTeam(0, exTyranitar);
		
		
		player.setTeam(1, exTyranitar);
		/*
		 * TODO
		 * E deveremos criar um exampleMove para o Metang
		 * e example move para o Tyranitar
		 * E uma example Ability
		 * E uma example HeldItem
		 * e um e um eum eu mem emme eu perdi o pé
		 */
		
		// TODO: Novamente, mais tarde devemos construir com jsons.
		Move exFirePunch = new DmgPlusFx(7, "Fire Punch", 1, 15, 0, 100, Move.moveCategs.PHYSICAL, 75, StatusFx.typeList.BURN, 30);	
		Move exHammerArm = new DamageDealing(359, "Hammer Arm", 6, 10, 0, 90, Move.moveCategs.PHYSICAL, 100);	
		exMetagross.setMove(0, exHammerArm);
		exTyranitar.setMove(0, exFirePunch);
		//exMetagross.setFainted(true);
		//exMetagross.setCurHp(0);
		
		
		// E agora, parte das habilidades;
		
		// E agora, a parte dos held items;
		// TODO: Como fazer cada um ter seu método próprio?
		// Esses itens que estamos criando ainda não possuem métodos, mas enfim...
		// usaremos para printar coisas mais pra frente
		Item exChoiceScarf = new Item(287, "Choice Scarf");
		Item exLeftovers = new Item(234, "Leftovers");
		
		exMetagross.setHeldItem(exLeftovers);
		exTyranitar.setHeldItem(exChoiceScarf);
		
		// Agora, vamos inventar habilidades.
		Ability exClearBody = new Ability(29, "Clear Body");
		Ability exSandStream = new Ability(45, "Sand Stream");
		exMetagross.setAbil(exClearBody);
		exTyranitar.setAbil(exSandStream);
		
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
		
		field.turn(scan, field, null);
		
		// Setar o pokemo ativo como o pokemon no slot[0] no começo da batalha
		// "Renderizar" menu de batalha Root e receber opções do jogador
		
		
		// TODO: apagar esse checkpoint
		System.out.print("O resultado do roll 50/50 é: " + TurnUtils.rollChance(50)+ "\n");
		
		// impedindo resource leak
		scan.close();
	}
	
}

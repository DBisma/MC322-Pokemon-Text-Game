package pokemontextgame;

import java.util.Scanner;

public class Treinador {
	/*
	 * Contém as informações do treinador.
	 */
	
	private int id;
	private String name;
	private String desc;
	private boolean isPlayer;
	private Poke[] team; // time do treinador. max 6 pokemons. Pokemon ativo sempre estará na pos. 0
	private int activeMonId;
	private boolean forcedSwitch; // se o treinador é obrigado a trocar de pokemon; utilizado em menus e opções de turno
	
	// TODO: INICIALIZAÇÃO DE VERDADE
	
	public Treinador(int id, String name, boolean player) {
		this.id = id;
		this.name = name;
		this.isPlayer = player;
		this.team = new Poke[6];
	}
	
	public boolean inputName(Scanner scan) {
		/*
		 * Faz um loop de recepção de nome que não é satisfeito
		 * até que o nome recebeido seja validado. Para isso,
		 * faz uso da func. validateName();
		 * 
		 * TODO: Talvez seja má prática ter um loop infinito condicional aqui dentro.
		 */
		
		String type = (this.isPlayer) ? "jogador" : "NPC";
		String name = "Placeholder";
		boolean flag = false;

		// Não sai daqui até receber nome válido
		while (!flag) {
			System.out.print("Insira o nome do " + type + ", apenas caracteres alfabéticos sem espaço: ");
			name = scan.nextLine();
			flag = Treinador.validateName(name);
		}
		
		System.out.print("\n");
		
		this.name = name;
		return true;
	}

	static boolean validateName(String name) {
		/*
		 * Função que recebe um nome de um treinador
		 * e o valida. Nome só pode ter letras e deve
		 * ter um tamanho máximo de 12 caracteres.
		 * Imprime mensagens de erro conforme o necessário.
		 * Retorna o nome do pokemon se validado.
		 * 
		 */

		// Validar caracteres
		int i = 0;
		for(i = 0; i < name.length(); i++) {
			int asciiValueAtPos = (int) name.charAt(i);
			// Usaremos valores ASCII para checar se está entre  A-Z (64-91) ou a-z (96-123) ou se tem espaço
			if(!((64 < asciiValueAtPos && asciiValueAtPos < 91) || (96 < asciiValueAtPos && asciiValueAtPos < 123))) {
				System.out.println("Nome não é constituído apenas de letras do alfabeto. \n");
				return false;
			}
		}
		// Validar tamanho
		if(name.length() > 13) {
			System.out.print("Nome excede o limite de caracteres. \n");
			return false;
		}

		// Se não foi barrado, deve ser válido
		return true;
	}
	
	// Apenas Getters e Setters adiante
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return name;
	}
	public void setNome(String nome) {
		this.name = nome;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isPlayer() {
		return isPlayer;
	}
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	public Poke[] getTeam() {
		return team;
	}
	public void setTeam(int index, Poke mon) { // provavelmente será desnecessário no futuro
		this.team[index] = mon;
	}

	public Poke getActiveMon() {
		/*
		 * Retorna o poke ativo, e não seu ID.
		 */
		return this.team[activeMonId];
	}
	
	public int getActiveMonId() {
		return this.activeMonId;
	}

	public void setActiveMonId(int newId) {
		/*
		 * Muda o pokemon ativo para o de novo ID escolhido.
		 * Muda o pokemon ativo anterior para não-ativo.
		 */
		this.team[this.activeMonId].setActive(false);
		this.team[newId].setActive(true);
		this.activeMonId = newId;
	}

	public boolean isForcedSwitch() {
		return forcedSwitch;
	}

	public void setForcedSwitch(boolean forcedSwitch) {
		this.forcedSwitch = forcedSwitch;
	}
}

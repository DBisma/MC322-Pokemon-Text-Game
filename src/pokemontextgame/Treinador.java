package pokemontextgame;

import java.util.Scanner;

public class Treinador {
	/*
	 * Contém as informações do treinador.
	 */
	
	private int id;
	private String nome;
	private String desc;
	private boolean isPlayer;
	private Poke[] team; // time do treinador. max 6 pokemons. Pokemon ativo sempre estará na pos. 0
	
	// TODO: INICIALIZAÇÃO
	
	public Treinador(boolean player) {
		this.isPlayer = player;
	}
	
	public boolean inputName(Scanner scan) {
		/*
		 * Faz um loop de recepção de nome que não é satisfeito
		 * até que o nome recebeido seja validado. Para isso,
		 * faz uso da func. validateName();
		 */
		
		String type = (this.isPlayer) ? "jogador" : "NPC";
		System.out.print("Insira o nome do " + type + ": ");
		String name = scan.next();
		boolean flag = true;

		// não sai daqui até ser válido
		do  {
			flag = Treinador.validateName(name);
		} while (!flag);
		
		this.nome = name;
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

		// validar tamanho
		if(name.length() > 13) {
			System.out.print("Nome excede o limite de caracteres. \n");
			return false;
		}
		// validar caracteres
		else {
			int i = 0;
			for(i = 1; i < name.length(); i++) {
				char charAtPos = name.charAt(i);
				// usaremos valores ASCII para checar se está entre  A-Z (64-91) ou a-z (96-123)
				if(!( (64 < charAtPos && charAtPos < 91) || (96 < charAtPos && charAtPos < 123))) {
					System.out.println("Nome não é constituído apenas de letras do alfabeto.");
					return false;
				}
			}
		}
		
		// se não foi barrado, deve ser válido
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
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public void setTeam(Poke[] team) { // provavelmente será desnecessário no futuro
		this.team = team;
	}
	
}

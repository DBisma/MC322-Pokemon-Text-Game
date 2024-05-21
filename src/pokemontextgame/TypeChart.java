package pokemontextgame;

import java.util.Arrays;

public class TypeChart {
	/*
	 * TypeChart armazena todas as possíveis combinações de tipo
	 * para ataque e defesa numa grande matriz. É loadada no começo
	 * de cada batalha e permanece loadada até a batalha se encerrar.
	 * Ademais, possui funções para cálculo de modificação de dano.
	 */
	
	// Fazer isso com uma tabela é mais eficiente do que com Enums e Ifs
	private float chart[][];
	
	public TypeChart() {
		/*
		 * Constrói a tabela, preenchendo-na com os 
		 * valores modificadores de cada combinação de tipo.
		 */
		this.chart = new float[19][19];
		
		// Valores iniciais; preencher matrix com float 1
		for(float[] row: this.chart)
			Arrays.fill(row, 1.0f);
		
		/*
		 * Combinações relevantes;
		 * Para fins de explicação, cada tipo possível possui um índice na tabela.
		 * São eles: Normal (0), Fire (1), Water (2), Grass (3),
		 * Electric (4), Ice (5), Fighting (6), Poison (7),  Ground (8),
		 * Flying (9), Psychic (10), Bug (11), Rock (12), Ghost (13), 
		 * Dragon (14), Dark (15), Steel (16), Fairy (17), Typeless (18)
		 * Possivelmente nunca usaremos fairy por não estar disponível na Gen V, que baseou nosso projeto.
		 * As vantagens/desvantagens são tabeladas.
		 * https://upload.wikimedia.org/wikipedia/commons/9/97/Pokemon_Type_Chart.svg
		 */
		
		// Normal x Rock, Steel
		this.chart[0][12] = this.chart[0][16] = (float) 0.5;
		// Normal x Ghost
		this.chart[0][13] = (float) 0;

		// Fire x Fire, Water, Rock, Dragon
		this.chart[1][1] = this.chart[1][2] = this.chart[1][12] = this.chart[1][14] = (float) 0.5;
		// Fire x Grass, Ice, Bug, Steel
		this.chart[1][3] = this.chart[1][5] = this.chart[1][11] = this.chart[1][16] = (float) 2.0;
		
		// Water x Water, Grass, Dragon
		this.chart[2][2] = this.chart[2][3] = this.chart[2][14] = (float) 0.5;
		// Water x Fire, Ground, Rock
		this.chart[2][2] = this.chart[2][8] = this.chart[2][12] = (float) 2.0;

		// Grass x Fire, Grass, Poison, Flying, Dragon, Steel
		this.chart[3][1] = this.chart[3][3] = this.chart[3][7] = this.chart[3][9] =
				this.chart[3][14] = this.chart[3][16] = (float) 0.5;
		// Grass x Water, Ground, Rock
		this.chart[3][2] = this.chart[3][8] = this.chart[3][12] = (float) 2.0;
		
		// Electric x Grass, Electric, Dragon
		this.chart[4][3] = this.chart[4][4] = this.chart[4][14] = (float) 0.5;
		// Electric x Ground
		this.chart[4][8] = (float) 0.0;
		// Electric x Water, Flying 
		this.chart[4][2] = this.chart[4][9] = (float) 2.0;
		
		// Ice x Fire, Water, Ice, Steel
		this.chart[5][1] = this.chart[5][2] = this.chart[5][5] = this.chart[5][16] = (float) 0.5;
		// Ice x Grass, Ground, Flying, Dragon
		this.chart[5][3] = this.chart[5][8] = this.chart[5][9] = this.chart[5][14] = (float) 2.0;
		
		// Fighting x Poison, Flying, Psychic, Bug, Fairy
		this.chart[6][7] = this.chart[6][9] = this.chart[6][10] =
				this.chart[6][11] = this.chart[6][17] = (float) 0.5;
		// Fighting x Normal, Ice, Rock, Dark, Steel
		this.chart[6][0] = this.chart[6][5] = this.chart[6][12] =
				this.chart[6][15] = this.chart[6][16] = (float) 2.0;
		// Fighting x Ghost]
		this.chart[6][13] = (float) 0.0;
		
		// Poison x Poison, Ground, Rock, Ghost
		this.chart[7][7] = this.chart[7][8] = this.chart[7][12] = this.chart[7][13] = (float) 0.5;
		// Poison x Grass, Fairy
		this.chart[7][3] = this.chart[7][17] =(float) 2.0;
		// Poison x Steel
		this.chart[7][16] = (float) 0.0;
		
		// Ground x Grass, Bug
		this.chart[8][3] = this.chart[8][11] = (float) 0.5;
		// Ground x Fire, Electric, Poison, Rock, Steel;
		this.chart[8][1] = this.chart[8][4] = this.chart[8][7] =
				this.chart[8][12] = this.chart[8][16] = (float) 2.0;
		// Ground x Flying
		this.chart[8][9] = (float) 0.0;
		
		// Flying x Electric, Rock, Steel
		this.chart[9][4] = this.chart[9][12] = this.chart[9][16] = (float) 0.5;
		// Flying x Grass, Fighting, Bug
		this.chart[9][3] = this.chart[9][6] = this.chart[9][11] = (float) 2.0;
		
		// Psychic x Psychic, Steel
		this.chart[10][10] = this.chart[10][16] = (float) 0.5;
		// Psychic x Fighting, Poison
		this.chart[10][6] = this.chart[10][7] = (float) 2.0;
		// Psychic x Dark
		this.chart[10][15] = (float) 0.0;
		
		// Bug x Fire, Fighting, Poison, Flying, Ghost, Steel, Fairy
		this.chart[11][1] = this.chart[11][6] = this.chart[11][7] =
				this.chart[11][9] = this.chart[11][13] = this.chart[11][16] = 
					this.chart[11][17] = (float) 0.5;
		// Bug x Psychic, Dark
		this.chart[11][10] = this.chart[11][15] = (float) 2.0;
		
		// Rock x Fighting, Ground, Steel
		this.chart[12][6] = this.chart[12][8] = this.chart[12][16] = (float) 0.5;
		// Rock x Fire, Ice, Flying, Bug
		this.chart[12][1] = this.chart[12][5] = 
				this.chart[12][9] = this.chart[12][11] = (float) 2.0;
		
		// Ghost x Dark
		this.chart[13][15] = (float) 0.5;
		// Ghost x Psychic, Ghost
		this.chart[13][10] = this.chart[13][13] = (float) 2.0;
		// Ghost x Normal
		this.chart[13][0] = (float) 0.0;
		
		// Dragon x Steel
		this.chart[14][16] = (float) 0.5;
		// Dragon x Dragon
		this.chart[14][14] = (float) 2.0;
		// Dragon x Fairy
		this.chart[14][17] = (float) 0.0;
		
		// Dark x Fighting, Dark, Fairy
		this.chart[15][6] = this.chart[15][15] = this.chart[15][17] = (float) 0.5;
		// Dark x Psychic, Ghost
		this.chart[15][10] = this.chart[15][13] = (float) 2.0;
		
		// Steel x Fire, Water, Electric, Steel;
		this.chart[16][1] = this.chart[16][2] =
				this.chart[16][4] = this.chart[16][16] = (float) 0.5;
		// Steel x Ice, Rock, Fairy
		this.chart[16][5] = this.chart[16][12] = this.chart[16][17] = (float) 2.0;
		
		// Fairy x Fire, Poison, Steel
		this.chart[17][1] = this.chart[17][7] = this.chart[17][16] = (float) 0.5;
		// Fairy x Fighting, Dragon, Dark
		this.chart[17][6] = this.chart[17][14] = this.chart[17][15] = (float) 2.0;
	}
	
	public float typeMatch(int idAtk, int idDef) {
		/*
		 * Verifica os Ids dos tipos do atacante e defensor
		 * numa grande tabela de tipos. Retorna modificador.
		 * Tipo -1 é "não utilizado" por padrão.
		 */
		if(idDef == -1) // se for monotipo, retorna elemento neutro
			return 1;
		else
			return this.chart[idAtk][idDef];
	}
	
	public float compoundTypeMatch(int typeId, Poke mon) {
		/* 
		 * Efetua dois typeMatches e os multiplica para obter
		 * o resultado completo de um ataque contra um pokemon.
		 */
		
		return typeMatch(typeId, mon.getTipagem()[0]) * typeMatch(typeId, mon.getTipagem()[1]);
	}
	
	public static String typeToString(int id) {
		/*
		 * Recebe o ID de um tipo elemental e retorna
		 * seu nome correspondente.
		 */
		String[] convString = new String[] {
			"NORMAL", "FIRE", "WATER", "GRASS",
			"ELECTRIC", "ICE", "FIGHTING", "POISON",
			"GROUND", "FLYING", "PSYCHIC", "BUG", 
			"ROCK", "GHOST", "DRAGON", "DARK", 
			"STEEL", "FAIRY"
		};
		
		return convString[id];
	}
	
	static String fullTypeToString(Poke mon) {
		/*
		 * Recebe um pokemon e retorna uma String
		 * já formatada com todos seus tipos.
		 */
		String monString = "[" + TypeChart.typeToString(mon.getTipagem()[0]) + "]";
		monString = BattleMenu.alignString(monString, 10);
		if(mon.getTipagem()[1] != -1) { // se houver tipo secundário
			monString += " [" + TypeChart.typeToString(mon.getTipagem()[1]) + "]";
			monString = BattleMenu.alignString(monString, 10);
		}
		
		return monString;
	}
}

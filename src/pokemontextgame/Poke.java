package pokemontextgame;

import pokemontextgame.StatusFx.typeList;
import pokemontextgame.moves.Move;
import java.util.Arrays;

public class Poke {
	/*
	 * Armazena as informações dos pokemons do jogo.
	 */
	
	private int id;
	private int pokedexId;
	private String name;
	private String speciesName;
	private String pokedexEntry; // descrição do pokemon.
	private int[] tipagem; // tipos do pokemon. pode ter até 2. -1 indica que um slot de tipo está vazio.
	// tratar um tipo como um número será índice para montar uma tabela de combinações. ver TypeChart.java

	private int level; // por enquanto, pokemons não ganham experiência e portanto o nível não altera
	private boolean active; // flag de ser o pokemon ativo em batalha
	
	// Stats invariáveis
	// BaseHP(0), Atk (1), Def (2), SpAtk (3), SpecDef (4), Speed (5), Weight (6) Não mudarão para dada espécie de Poke.
	private int baseStats[]; 

	// Stats variáveis principais
	private int maxHp; // varia com Level
	private int curHp;
	private boolean fainted;
	// Vetor de modificadores de stats. Inclui: Atk (0), Def (1), SpAtk (2), SpecDef (3), Speed (4), Weight (5), 
	// e dois exclusivos para batalhas: Evasion (6) e Accuracy (7)
	// uso de vetores é justificado para realizar varreduras rápidas nos modificadores
	private int statMods[]; 
	
	// Moves disponíveis, sempre 4 no máximo (por isso um array simples), e outras informações
	private int [] movesetList; // IDs de todos os moves que ele pode aprender
	private Move[] moveset; // moveset atual
	
	private StatusFx statusFx; // por enquanto, só um status por vez pode ser um
	private int turnsOnField; // usado para calcular algumas formas de dano
	
	// Para construir os moves do pokemon
	JSONReader moveJson;
	
	public Poke(int ID, int pokedexID, String name, int sex, int lvl, String speciesName, String pokeDexEntry, int[] tipagem, int[] baseStats, int[] moveSetList) {
		/*
		 * Placeholder de construtor. Exemplo para Metang.
		 * Teremos que dar um jeito de construir de um Json com o pokedexNum + level + stats etc desejados.
		 */
				
		// Parte Variável
        this.id = ID; // Id gerado aleatoriamente
		this.name = name; // Nickname do Pokemon
		this.level = lvl;
		// Desconsiderar IVs e EVs. Complexo demais para o escopo desse trabalho.
		
		// O resto deverá vir do Json
		this.pokedexId = pokedexID; // usado para evocar o json da construção
		
		this.speciesName = speciesName;
		this.pokedexEntry = pokeDexEntry;
		this.tipagem = tipagem;
		
		/*
		 * Referência:
		 * baseStats[0] Hp
		 * baseStats[1] Atk
		 * baseStats[2] Def
		 * baseStats[3] SpAtk
		 * baseStats[4]	SpDef
		 * baseStats[5] Speed
		 * baseStats[6] Weight
		 */
		this.baseStats = baseStats;		
		
		// statMods é apenas inicializado e utilizado na luta, por padrão em 0
		this.statMods = new int[8];
		Arrays.fill(statMods, 0); 
		
		// Outros variáveis importantes
		this.maxHp = (int) (baseStats[0]*(level/100f)*2 + level + 10);// cálculo de Hp com base o statBasic de HP
		this.curHp = maxHp;
		this.active = false; // só se torna ativo em batalha

		// Moves são adicionados subsequentemente
		this.movesetList = moveSetList;
		this.moveset = new Move[4];
		
		this.statusFx = new StatusFx(StatusFx.typeList.NEUTRAL);
	}

	public int[] getMovesetList() {
		return movesetList;
	}

	public void setMovesetList(int[] movesetList) {
		this.movesetList = movesetList;
	}

	@Override
	public String toString() {
		/*
		 * Concatena informações sobre o Pokemon
		 * numa grande string e a retorna.
		 */
		
		String out;
		out = "Pokémon: '" + this.name + "' " + TypeChart.fullTypeToString(this) + "\n"
			+ "HP: " + this.curHp + "/" + this.maxHp + "\n"
			+ "ATK: " + this.statCalc(0) + "\n"
			+ "DEF:" + this.statCalc(1) + "\n"
			+ "SP. ATK: " + this.statCalc(2) + "\n"
			+ "SP. DEF: " + this.statCalc(3) + "\n"
			+ "SPEED: " + this.statCalc(4) + "\n";
		
		return out;
	}
	
	public int statCalc(int baseStatId) {
		/*
		 * Recebe um Pokemon e um stat base
		 * Calcula o número real desse stat
		 * de um pokemon com base no level
		 * e no valor do "stat base", por enquanto.
		 * Retorna essa valor real.
		 */ 
		float stat = baseStats[baseStatId]*2*this.level*(0.01f);
		return (int) (stat + 2);
	}
	
	public boolean dmgMon(int dmg) {
		/*
		 * Danifica um pokemon. Atualiza fainted se necessário.
		 * retorna true se ele estiver vivo
		 * e false se ele estiver fainted.
		 */
		int resuHp = this.curHp -= dmg;
		if(resuHp <= 0) {
			this.curHp = 0;
			this.fainted = true;
			return false;
		}
		else {
			this.curHp = resuHp;	
			return true;
		}	
	}
	
	public boolean healMon(int healNum) {
		/*
		 * Recupera um pokemon.
		 * Retorna true se conseguir recuperar,
		 * false caso contrário.
		 */
		if(this.curHp == this.maxHp)
			return false;
		else {
			int resuHp = this.curHp += healNum;
			if(resuHp >= this.maxHp) {
				this.curHp = this.maxHp;
				return true;
			}
			else {
				this.curHp = resuHp;
				return true;
			}
		}
	}
	
	public boolean isStatusedFx() {
		/*
		 * Retorna true se o pokemon em questão
		 * estiver sobre efeito de status
		 * e false caso contrário.
		 * Por ora, apenas lida com status não-voláteis.
		 */
		
		if(this.getStatusFx().getType() == StatusFx.typeList.NEUTRAL)
			return true;
		else
			return false;
	}
	
	public boolean boostStat(int statId, int statBoost) {
		/*
		 * Tenta aumentar o stat de um pokemon.
		 * Falha se o Stat já estiver com estágio máximo ou mínimo (+-6)
		 */
		
		int sum = statMods[statId] + statBoost;
		// passando do limite
		if(Math.abs(statMods[statId] + statBoost) == 6) {
			return false;
		}
		// extendendo ao limite
		else if(Math.abs(sum) >= 6) { 
			statMods[statId] = 6*(statBoost/Math.abs(statBoost)); //6 * sinal do limite
			return true;
		}
		// longe do limite
		else {
			statMods[statId] = sum;
			return true;
		}
	}
	
	public boolean isPpDepleted() {
		/*
		 * Verifica se ainda não sobrou move com PP
		 * no pokemon em questão, retorna true neste caso,
		 * false caso contrário.
		 */
		int i = 0;
		int depletion = 0;
		int moveCount = 0;
		for(i = 0; i < 4; i++) {
			Move curMove = this.getMove(i);
			if(curMove != null) {
				moveCount++;
				if(curMove.getPoints() == 0)
					depletion++;
			}
		}
		if(depletion == moveCount)
			return true;
		else
			return false;
	}
	
	// Apenas Getters e Setters adiante
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String nome) {
		this.name = nome;
	}
	
	public String getPokedexEntry() {
		return pokedexEntry;
	}
	
	public void setPokedexEntry(String pokedexEntry) {
		this.pokedexEntry = pokedexEntry;
	}
	
	public int[] getStatModArray(){
		/*
		 * Retorna o vetor de modificadores.
		 */
		return this.statMods;
	}
	
	public void resetStats() {
		Arrays.fill(statMods, 0);
	}
	
	public int getStatModGeneral(int id){
		/*
		 * Retorna algum modificador de stat com o Id escolhido.
		 */
		return this.statMods[id];
	}

	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	
	public void setMaxHp(int hp) {
		this.maxHp = hp;
	}
	
	public int getCurHp() {
		return curHp;
	}
	
	public void setCurHp(int hp) {
		this.curHp = hp;
	}
	
	public Move[] getMoveset() {
		/*
		 * Retorna o moveset inteiro.
		 * Útil para varreduras.
		 */
		return moveset;
	}
	
	public Move getMove(int index) {
		/*
		 * Retorna o moveset específico numa certa posição.
		 */
		return this.moveset[index];
	}
	
	public void setMoveset(Move[] moveset) {
		/*
		 * Recebe a posição do move (0-3)
		 * e coloca o Move desejado neste lugar.
		 */
		this.moveset = moveset;
	}
	
	public void setMove(int index, Move move) {
		/*
		 * Recebe a posição do move (0-3)
		 * e coloca o Move desejado neste lugar.
		 */
		this.moveset[index] = move;
	}
	
	public int[] getTipagem() {
		return tipagem;
	}
	
	public void setTipagem(int[] tipagem) {
		// Em teoria, nunca deveremos "mudar" a tipagem. Ela apenas é construída.
		// Mas alguns pokemons específicos podem mudar de tipo no meio da batalha (e.g: Castform)
		this.tipagem = tipagem;
	}
	
	public boolean isFainted() {
		return fainted;
	}
	
	public void setFainted(boolean fainted) {
		this.fainted = fainted;
	}
	
	public StatusFx getStatusFx() {
		return statusFx;
	}
	
	public void setStatusFx(typeList type) {
		/*
		 * Serve de ponte para modificar o StatusFx através de um método
		 * já existente em StatusFx.java
		 */
		this.statusFx.setStatusFull(type);
	}	
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	// Getters de Stats Base
	
	// Getters de Stats Modificados
	public int getModAtk() {
		return this.statMods[0];
	}
	
	public int getModDef() {
		return this.statMods[1];
	}
	
	public int getModSpAtk() {
		return this.statMods[2];
	}
	
	public int getModSpDef() {
		return this.statMods[3];
	}
	
	public int getModSpeed() {
		return this.statMods[4];
	}
	
	public int getModWeight() {
		return this.statMods[5];
	}
	
	public int getModEvasion() {
		return this.statMods[6];
	}
	
	public int getModAccuracy() {
		return this.statMods[7];
	}
	
	// Setters de Stats Modificados
	public void setModAtk(int newVal) {
		this.statMods[0] = newVal;
	}
	
	public void setModDef(int newVal) {
		this.statMods[1] = newVal;
	}
	
	public void setModSpAtk(int newVal) {
		this.statMods[2] = newVal;
	}
	
	public void setModSpDef(int newVal) {
		this.statMods[3] = newVal;
	}
	
	public void setModSpeed(int newVal) {
		this.statMods[4] = newVal;
	}
	
	public void setModWeight(int newVal) {
		this.statMods[5] = newVal;
	}
	
	public void setModEvasion(int newVal) {
		this.statMods[6] = newVal;
	}
	
	public void setModAccuracy(int newVal) {
		this.statMods[7] = newVal;
	}
	
	public int getPokedexId() {
		return pokedexId;
	}
	
	public void setPokedexId(int pokedexId) {
		this.pokedexId = pokedexId;
	}
	
	public int getBaseHp() {
		return baseStats[0];
	}

	public void setBaseHp(int baseHp) {
		this.baseStats[0] = baseHp;
	}

	public int getBaseAtk() {
		return baseStats[1];
	}

	public void setBaseAtk(int baseAtk) {
		this.baseStats[1] = baseAtk;
	}

	public int getBaseDef() {
		return baseStats[2];
	}

	public void setBaseDef(int baseDef) {
		this.baseStats[2] = baseDef;
	}

	public int getBaseSpAtk() {
		return baseStats[3];
	}

	public void setBaseSpAtk(int baseSpAtk) {
		this.baseStats[3] = baseSpAtk;
	}

	public int getBaseSpDef() {
		return baseStats[4];
	}

	public void setBaseSpDef(int baseSpDef) {
		this.baseStats[4] = baseSpDef;
	}

	public int getBaseSpeed() {
		return baseStats[5];
	}

	public void setBaseSpeed(int baseSpeed) {
		this.baseStats[5] = baseSpeed;
	}

	public int getBaseWeight() {
		return baseStats[6];
	}

	public void setBaseWeight(int baseWeight) {
		this.baseStats[6] = baseWeight;
	}
	
	public void setBaseGeneral(int baseStat, int id) {
		this.baseStats[id] = baseStat;
	}
	
	public int getBaseGeneral(int id) {
		return baseStats[id];
	}
	
	public String getSpeciesName() {
		return speciesName;
	}

	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}

	public int getTurnsOnField() {
		return turnsOnField;
	}
	
	public void setTurnsOnField(int turnsOnField) {
		this.turnsOnField = turnsOnField;
	}
	
	public void turnOnFieldIncr() {
		turnsOnField += 1;
	}
	
	public void turnPass() {
		/*
		 * Função que faz todos os efeitos de passagem
		 * de turno sobre um pokemon.
		 */
		this.turnsOnField += 1;
		this.statusFx.statusTurnPass();
	}
}



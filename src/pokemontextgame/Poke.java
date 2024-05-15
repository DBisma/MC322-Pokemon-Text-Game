package pokemontextgame;

import moves.Move;
import pokemontextgame.StatusFx.typeList;
import java.util.ArrayList;

public class Poke {
	/*
	 * Armazena as informações dos pokemons do jogo.
	 */
	
	// TODO: Implementações futuras
	// https://stackoverflow.com/questions/13543457/how-do-you-create-a-dictionary-in-java
	// https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
	
	private int id;
	private int pokedexNum;
	private String name;
	private String pokedexEntry; // descrição do pokemon.
	private int[] tipagem; // tipos do pokemon. pode ter até 2. -1 indica que um slot de tipo está vazio.
	// tratar um tipo como um número será índice para montar uma tabela de combinações. ver TypeChart.java
	private int sex; // eu juro que isso importa
	private int level; // por enquanto, pokemons não ganham experiência e portanto o nível não altera
	private boolean active; // flag de ser o pokemon ativo em batalha
	
	// Stats
	private int curHp;
	private int maxHp;
	private boolean fainted;
	
	// Atk (0), Def (1), SpecAtk (2), SpecDef (3), Speed (4), Accuracy (5), Evasion (6), Weight em kg (7), Health (8)
	// TODO:Possivelmente juntar os dois para legibilidade com um Hash Map de... tuplas?
	private int statBasic[]; // 9 posições. vetor de stats invariáveis; são construídos para um pokemon específico de acordo com o level (base + base*level/50)
	private int statMods[]; // 8 posições. modificadores. hp nunca é modificado exceto pelo nível, algo que se dá na construção
	
	/*
	 * TODO: Cada reload do pokemon na arena reseta todos os vStats, exceto vHealth.
	 * Com a nova implementação vetorial, isso é fácil! Basta usar ArrayFill.
	 */
	
	// Moves disponíveis, sempre 4 no máximo (por isso um array simples), e outras informações
	private Move[] moveset; // esse último será um pouco mais complicado. TODO: Resolver leitura e referência de moves.
	private Item heldItem;
	private Ability abil;
	
	// TODO: Refatorar seção seguinte para referência a classes sempre construídas de Status(es) diferentes
	// Deletar essa porção de getters e setters
	// TODO: Criar um modo que permita que o pokemon possua vários statusfx voláteis simultâneos mas apenas um não-volátil
	// Array que armazena todos os status FX que afetam o pokemon
	private StatusFx statusFx; // por enquanto, só um status por vez pode ser um
	//private ArrayList<StatusFx> volatileFxArray; // pode conter vários TODO: Separar voláteis e não voláteis em ArrayLists
	
	/*
	 * TODO: Construir o pokemon. Deveremos ter um método que busca a json também;
	 */
	
	public Poke(int pokedexID, String name) {
		/*
		 * Placeholder de construtor. Exemplo para Metang.
		 * Teremos que dar um jeito de construir de um Json com o pokedexNum + level + stats etc desejados.
		 * TODO: classe Enum de Natures?? IVs e EVs??
		 */
		this.id = 123456;
		this.pokedexNum = pokedexID; // usado para evocar o json da construção. talvez se torne argumento no futuro TODO
		this.name = name;
		this.pokedexEntry = "Nenhuma entrada disponível.";
		this.tipagem = new int[] {16, 10};
		this.statBasic = new int[] {135, 130, 95, 90, 70, 100, 100, 550, 80};
		// statMods é apenas inicializado e utilizado na luta, por padrão em 0
		this.sex = 2;
		this.level = 100;
		this.maxHp = statBasic[8]*(level/100)*2 + level + 10;// cálculo de Hp com base o statBasic de HP
		this.curHp = maxHp;
		this.active = true; // temporariamente, construímos assim. TODO: Isso ser decidido pela func. turno e battlefield


		this.moveset = new Move[6];
		
		// TODO: Esses são métodos genéricos que não significam nada. Teremos que criar exemplos para cada no começo, e loadar de uma .json mais tarde.
		// TODO: instanciar o Arraylist e adicionar o stat Não-Volátil nele
		this.statusFx = new StatusFx(StatusFx.typeList.NEUTRAL); // TODO: isso talvez deva mudar. ver Status.java
	
	}

	
	@Override
	public String toString() {
		/*
		 * Concatena informações sobre o Pokemon
		 * numa grande string e a retorna.
		 */
		
		String out;
		out = "Pokémon: '" + this.name + "' " + TypeChart.fullTypeToString(this) + "\n" //TODO: Deve haver um jeito de formatar de modo mais quadradinho
			+ "HP: " + this.curHp + "/" + this.maxHp + "\n"
			+ "ATK - " + this.statCalc(0) + "\n"
			+ "DEF - " + this.statCalc(1) + "\n"
			+ "SP. ATK - " + this.statCalc(2) + "\n"
			+ "SP. DEF - " + this.statCalc(3) + "\n"
			+ "SPEED - " + this.statCalc(4) + "\n" + "\n"
			+ this.getAbil().toString() + "\n";
		
		if(this.heldItem == null)
			out += "Nenhum item segurado." + "\n";
		else
			out += this.getHeldItem().toString() + "\n";
		return out;
	}
	
	public int statCalc(int statId) {
		/*
		 * Recebe um Pokemon e o id de um stat.
		 * Calcula o número real desse stat
		 * de um pokemon com base no level
		 * e no valor do "stat base", por enquanto.
		 * Retorna essa valor real.
		 */
		float stat = this.statBasic[statId]*2*this.level*(0.01f);
		return (int) stat + 2;
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
			return false;
		}
		else {
			this.curHp = resuHp;	
			return true;
		}	
	}
	
	public boolean healMon(int healNum) {
		/*
		 * Recupera um pokemon. Atualiza fainted.
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
	public int getBaseHp() {
		return statBasic[8];
	}
	//TODO: Isso será um porre, mas deveremos colocar baseHp na posição 0 do vetor e mover cada outro uma posição pra frente.
	// e depois disso refatorar todo o código que já usa esses vetores. hell.
	public void setBaseHp(int baseHp) {
		this.statBasic[8] = baseHp;
	}
	public int[] getStatBasicArray(){
		/*
		 * Retorna o vetor de stats básicos.
		 */
		return this.statBasic;
	}
	public int getStatBasicGeneral(int id){
		/*
		 * Retorna algum stat básico com o Id escolhido.
		 */
		return this.statBasic[id];
	}
	public int[] getStatModArray(){
		/*
		 * Retorna o vetor de modificadores.
		 */
		return this.statMods;
	}
	public int getStatModGeneral(int id){
		/*
		 * Retorna algum modificador de stat com o Id escolhido.
		 */
		return this.statMods[id];
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
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
	public Item getHeldItem() {
		return heldItem;
	}
	public void setHeldItem(Item heldItem) {
		this.heldItem = heldItem;
	}
	public Ability getAbil() {
		return abil;
	}
	public void setAbil(Ability abil) {
		this.abil = abil;
	}
	public Move[] getMoveset() {  // Provavelmente será desnecessário substituído no futuro
		/*
		 * Retorna o moveset inteiro.
		 */
		return moveset;
	}
	public Move getMove(int index) {  // Provavelmente será desnecessário substituído no futuro
		/*
		 * Retorna o moveset específico numa certa posição.
		 */
		return this.moveset[index];
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
	
	// Gets e Sets específicos para cada stat básicos
	public int getBaseAtk() {
		return this.statBasic[0];
	}
	public void setBaseAtk(int bAtk) {
		this.statBasic[0] = bAtk;
	}
	public int getBaseDef() {
		return statBasic[1];
	}
	public void setBaseDef(int bDef) {
		this.statBasic[1] = bDef;
	}
	public int getBaseSpecAtk() {
		return statBasic[2];
	}
	public void setBaseSpecAtk(int bSpecAtk) {
		this.statBasic[2] = bSpecAtk;
	}
	public int getBaseSpecDef() {
		return statBasic[3];
	}
	public void setBaseSpecDef(int bSpecDef) {
		this.statBasic[3] = bSpecDef;
	}
	public int getBaseSpeed() {
		return statBasic[4];
	}
	public void setBaseSpeed(int bSpeed) {
		this.statBasic[4] = bSpeed;
	}
	public int getBaseAccu() {
		return statBasic[5];
	}
	public void setBaseAccu(int bAccu) {
		this.statBasic[5] = bAccu;
	}
	public int getBaseEvasion() {
		return statBasic[6];
	}
	public void setBaseEvasion(int bEvasion) {
		this.statBasic[6] = bEvasion;
	} 
	public int getBaseWeight() {
		return statBasic[7];
	}
	public void setBaseWeight(int bWeight) {
		this.statBasic[7] = bWeight;
	}
	
	// Sets e Gets específicos para modificadores
	public int getModAtk() {
		return this.statMods[0];
	}
	public void setModAtk(int mAtk) {
		this.statMods[0] = mAtk;
	}
	public int getModDef() {
		return statMods[1];
	}
	public void setModDef(int mDef) {
		this.statMods[1] = mDef;
	}
	public int getModSpecAtk() {
		return statMods[2];
	}
	public void setModSpecAtk(int mSpecAtk) {
		this.statMods[2] = mSpecAtk;
	}
	public int getModSpecDef() {
		return statMods[3];
	}
	public void setModSpecDef(int mSpecDef) {
		this.statMods[3] = mSpecDef;
	}
	public int getModSpeed() {
		return statMods[4];
	}
	public void setModSpeed(int mSpeed) {
		this.statMods[4] = mSpeed;
	}
	public int getModAccu() {
		return statMods[5];
	}
	public void setModAccu(int mAccu) {
		this.statMods[5]= mAccu;
	}
	public int getModEvasion() {
		return statMods[6];
	}
	public void setModEvasion(int mEvasion) {
		this.statMods[6] = mEvasion;
	}
	public int getModWeight() {
		return statMods[7];
	}
	public void setModWeight(int mWeight) {
		this.statMods[7] = mWeight;
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
	
	public boolean boostStat(int statId, int statBoost) {
		/*
		 * Tenta aumentar o stat de um pokemon.
		 * Falha se o Stat já estiver com estágio máximo ou mínimo (+-6)
		 */
		
		int sum = statMods[statId] + statBoost;
		// estando no limite
		if(statBoost * statMods[statId] > 0 && Math.abs(statMods[statId]) == 6)
			return false;
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
		
		
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}



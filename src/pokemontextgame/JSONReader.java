package pokemontextgame;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pokemontextgame.moves.*;
import pokemontextgame.moves.Move.moveCategs;
import pokemontextgame.moves.StatusMiscPackage.*;
import pokemontextgame.moves.DmgMiscPackage.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader{
	ClassLoader cl = getClass().getClassLoader();

	// Procura os jsons dentro de pokeJsons na pasta src e os carrega como um arquivo
	
	String pokej = new String("src/pokemontextgame/pokeJsons/poke.json");
	String movej = new String("src/pokemontextgame/pokeJsons/moves.json");
	
	// O arquivo é recebido pelo caminho absoluto, isso foi feito para ler o caminho como o caminho interno do projeto e não puxar, por exemplo, de uma pasta src em C:/Users/User/src
	String pokePath = new File(pokej).getAbsolutePath();
	String movePath = new File(movej).getAbsolutePath();
	
	// Inicialização dos ArrayList de objetos que o JSONReader lê
	
	private ArrayList<Poke> pkmnList;
	private ArrayList<Move> moveList;
	
	private ArrayList<Integer> pkmnUniqueIds;
	
	private String pokeStr;
	private String moveStr;
	
	public JSONReader(){
		pkmnList = new ArrayList<Poke>();
		moveList = new ArrayList<Move>();
		pkmnUniqueIds = new ArrayList<Integer>();
	}
	
	// Responsável por criar os Pokémons, ela coloca esses Pokémons dentro do array de Poke e também salva seus IDs em pkmnUniqueIds
	// Sempre que for executado vai gerar IDs aleatórios para cada Poke
	
	public void buildPokemons() throws IOException, JSONException {
		
		// Transforma o arquivo em uma string usando o método StringBuilder de java.io.BufferedRender e joga um exception caso dê erro de leitura
		StringBuilder cb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(pokePath))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            cb.append(line).append("\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		pokeStr = cb.toString();                                           // Passa a string de cb para pokeStr e cria um JSONArray que vai conter as informações para a construção de diversos pokémons
		JSONArray pokeArray = new JSONArray(pokeStr);
		for (int i = 0; i < pokeArray.length(); i++) {
			JSONObject jsonObject = pokeArray.getJSONObject(i);
			
			int        pokeID =     (int) Math.random();
			
			// Aqui o buildPokemons vai buscar no arquivo .json pelos dados dentro das labels indentificadas dentro de .getTipo("nome_da_label")
			// 
			
			String     nome =       jsonObject.getString("Nome");
			int        pokedexId =  jsonObject.getInt("PokeDexId");
			int        pokeLevel =  jsonObject.getInt("Level");         	// Por padrão, inicializado com 50 no json
			String     spcName =    jsonObject.getString("SpeciesName");
			String     dexEntry =   jsonObject.getString("DexEntry");
			
			JSONArray  tipagem =    jsonObject.getJSONArray("Tipagem");		// Aqui recebe arrays de ints como JSONArray
			JSONArray  moves =      jsonObject.getJSONArray("MoveSet");
			JSONArray  bSTTS =      jsonObject.getJSONArray("BaseStats");
			
			int[] typeArray = new int[tipagem.length()];                    // Converte os JSONArray em arrays de int,
			for (int j = 0; j < tipagem.length(); j++) {                    // esse procedimento é feito pois org.json não tem métodos que recebem tipos específicos de array
			    typeArray[j] = tipagem.getInt(j);
			}
			
			int[] moveArray = new int[moves.length()];
			for (int j = 0; j < moves.length(); j++) {
			    moveArray[j] = moves.getInt(j);
			}
			
			int[] baseSTArray = new int[bSTTS.length()];
			for (int j = 0; j < bSTTS.length(); j++) {
			    baseSTArray[j] = bSTTS.getInt(j);
			}
			
			Poke novoPoke = new Poke(pokeID, pokedexId, nome, pokeLevel, spcName, dexEntry, typeArray, baseSTArray, moveArray); // Constrói Poke e coloca tanto na lista de Pokémons, quanto os Ids em pkmnUniqueIds
			pkmnList.add(novoPoke);
			pkmnUniqueIds.add(pokeID);
		}
	}
	
	
	// Faz o mesmo que buildPokemons(), porém para os moves. Possui construtores específicos que são chamados conforme um switch
	public void buildMoves() throws IOException, JSONException {
		
		StringBuilder cb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(movePath))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            cb.append(line).append("\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		moveStr = cb.toString();
		JSONArray pokeArray = new JSONArray(moveStr);
		for (int i = 0; i < pokeArray.length(); i++) {
			JSONObject jsonObject = pokeArray.getJSONObject(i);
			int moveId = jsonObject.getInt("Id");
			String moveName = jsonObject.getString("Name");
			int moveType = jsonObject.getInt("MoveType");
			int moMaxPT = jsonObject.getInt("MaxPoints");
			int movePriority = jsonObject.getInt("MovePriority");
			int moveAcc = jsonObject.getInt("Accuracy");
			
			// buildMoves() verifica se moveCat e basePower estão no JSONReader, pois existem moves que não os usam
			String moveCat = null;
			if (jsonObject.has("Category")) { moveCat = jsonObject.getString("Category");} 		// String com o valor do enum, para receber o enum em si no construtor,
			int basePower = 0;																	// é feito uma conversão de String para enum usando .ValueOf
			if (jsonObject.has("BasePower")) { basePower = jsonObject.getInt("BasePower");}
			
			// Como os moves se comportam de maneiras diferentes e não apenas como dano, é feito um switch case com o nome da subclasse do move para sabermos o construtor certo a se usar
			String subClass = jsonObject.getString("Subclass");
			switch (subClass) {                               									// Os cases estão em escopo pois alguns nomes de variáveis são reutilizados
			
				case "DamageDealing":
				{
					DamageDealing novoDmgDeal = new DamageDealing(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoDmgDeal);
				break;
				}
					
				case "DmgMisc":
				{
					DmgMisc dmgM = new DmgMisc(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(dmgM);
				break;
				}
				
				case "DmgPlusFx":
				{
					int fxChance = jsonObject.getInt("FxChance");
					String fxType = jsonObject.getString("FxType");
					DmgPlusFx novoDPFX = new DmgPlusFx(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower, StatusFx.typeList.valueOf(fxType), fxChance);
					moveList.add(novoDPFX);
				break;
				}
				
				case "DmgPlusStat":
				{
					int statId = jsonObject.getInt("statId");
					int boostSt = jsonObject.getInt("boostStages");
					int boostCh = jsonObject.getInt("boostChance");
					boolean boostSelf = jsonObject.getBoolean("boostSelf");
					DmgPlusStat novoDMS = new DmgPlusStat(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower, statId, boostSt, boostCh, boostSelf);
					moveList.add(novoDMS);
				break;
				}
				
				case "StatChange":
				{
					int statId = jsonObject.getInt("statId");
					int boostSt = jsonObject.getInt("boostStages");
					boolean boostSelf = jsonObject.getBoolean("boostSelf");
					StatChange novoSTCH = new StatChange(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, statId, boostSt, boostSelf);
					moveList.add(novoSTCH);
				break;
				}
				
				case "StatusChangeFx":
				{
					String fxType = jsonObject.getString("FxType");
					int fxDuration = StatusFx.typeList.valueOf(fxType).getMaxDuration();
					boolean voltl = StatusFx.typeList.valueOf(fxType).isVolatile();
					StatusChangeFx novoSCF = new StatusChangeFx(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, StatusFx.typeList.valueOf(fxType), fxDuration, voltl);
					moveList.add(novoSCF);
				break;
				}
				
				case "SuperclassTwoStatModifiers": 
				{
					int statIdA = jsonObject.getInt("statIdA");
					int statIdB = jsonObject.getInt("statIdB");
					int boostStA = jsonObject.getInt("boostStageA");
					int boostStB = jsonObject.getInt("boostStageB");
					boolean boostSelfA = jsonObject.getBoolean("boostSelfA");
					boolean boostSelfB = jsonObject.getBoolean("boostSelfB");
					SuperclassTwoStatModifiers novoSCTSM = new SuperclassTwoStatModifiers(moveId, moveName, moveType, moMaxPT, movePriority, 
																	 moveAcc, statIdA, statIdB, boostStA, boostStB, boostSelfA, boostSelfB);
					moveList.add(novoSCTSM);
				break;
				}
				
				case "SuperclassSelfHealingMove": 
				{
					SuperclassSelfHealingMove novoSCHM = new SuperclassSelfHealingMove(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc);
					moveList.add(novoSCHM);
				break;
				}
				
				case "HealBell":
				{
					HealBell novoHealBell = new HealBell(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc);
					moveList.add(novoHealBell);
				break;
				}
				
				case "ShellSmash":
				{
					ShellSmash novoShellSmash = new ShellSmash(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc);
					moveList.add(novoShellSmash);
				break;
				}
				
				case "BraveBird":
				{
					BraveBird novoBraveBird = new BraveBird(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoBraveBird);
				break;
				}
				
				case "DoubleEdge":
				{
					DoubleEdge novoDoubleEdge = new DoubleEdge(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoDoubleEdge);
				break;
				}
				
				case "Explosion":
				{
					Explosion novoExplosion = new Explosion(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoExplosion);
				break;
				}
				
				case "FlareBlitz":
				{
					FlareBlitz novoFlareBlitz = new FlareBlitz(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoFlareBlitz);
				break;
				}
				
				case "GigaDrain":
				{
					GigaDrain novoGigaDrain = new GigaDrain(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoGigaDrain);
				break;
				}
				
				case "GrassKnot":
				{
					GrassKnot novoGrassKnot = new GrassKnot(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoGrassKnot);
				break;
				}
				
				case "GyroBall":
				{
					GyroBall novoGyroBall = new GyroBall(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoGyroBall);
				break;
				}
				
				case "LowKick":
				{
					LowKick novoLowKick = new LowKick(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoLowKick);
				break;
				}
				
				case "SeismicToss":
				{
					SeismicToss novoSeismicToss = new SeismicToss(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoSeismicToss);
				break;
				}
				
				case "Superpower":
				{
					Superpower novoSuperpower = new Superpower(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoSuperpower);
				break;
				}
				
				case "TriAttack":
				{
					TriAttack novoTriAttack = new TriAttack(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoTriAttack);
				break;
				}
				
				case "VCreate":
				{
					VCreate novoVCreate = new VCreate(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoVCreate);
				break;
				}
				
				case "WildCharge":
				{
					WildCharge novoWildCharge = new WildCharge(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoWildCharge);
				break;
				}
				
				case "ClearSmog":
				{
					ClearSmog novoClearSmog = new ClearSmog(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoClearSmog);
				break;
				}
				
				case "CloseCombat":
				{
					CloseCombat novoCloseCombat = new CloseCombat(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, moveCategs.valueOf(moveCat), basePower);
					moveList.add(novoCloseCombat);
				break;
				}
			}
		}
	}
	
	// Função que varre o time do treinador e para cada pokémon procura seus moves registrados por um int ID dentro da array MoveSetList
	public void atribuiMoveAPoke(Treinador player) {
		for (int i = 0; i < 6; i++) {                              // 6 Pokes por time
			Poke playerPoke = player.getTeam()[i];
			for (int j = 0; j < 4; j++) {						   // 4 Moves por Poke
				Move moveFromList = null;
				int [] pokeMoveSetIds = playerPoke.getMovesetList();
				
				int k;
				for(k = 0; k < moveList.size(); k++) {
					if (moveList.get(k).getId() == pokeMoveSetIds[j]) { // Encontra o [ID] em moveList e manda para o pivô moveFromList
						moveFromList = moveList.get(k);
					}
				}
				playerPoke.setMove(j, moveFromList); // Atribui o move no index j do MoveSet de Poke
			}
		}
	}
	
	// Retornam os ArrayList criados, getPkmnUniqueIDs é o único que não devolve uma ArrayList de objetos criados
	
	public ArrayList<Integer> getPkmnUniqueIDs(){
		return pkmnUniqueIds;
	}
	
	public ArrayList<Poke> getPkmnList(){
		return pkmnList;
	}
	
	public ArrayList<Move> getMoveList(){
		return moveList;
	}
	
}
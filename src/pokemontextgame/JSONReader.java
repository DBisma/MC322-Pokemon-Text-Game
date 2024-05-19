package pokemontextgame;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import moves.*;
import moves.Move.moveCategs;

public class JSONReader{
	ClassLoader cl = getClass().getClassLoader();
	
	String pokej = new String("src/pokemontextgame/pokeJsons/poke.json");
	String itemj = new String("src/pokemontextgame/pokeJsons/item.json");
	String movej = new String("src/pokemontextgame/pokeJsons/moves.json");
	
	String pokePath = new File(pokej).getAbsolutePath();
	String itemPath = new File(itemj).getAbsolutePath();
	String movePath = new File(movej).getAbsolutePath();
	
	private ArrayList<Poke> pkmnList;
	private ArrayList<Item> itemList;
	private ArrayList<Move> moveList;
	
	private String pokeStr;
	private String itemStr;
	private String moveStr;
	
	public JSONReader(){
		pkmnList = new ArrayList<Poke>();
		itemList = new ArrayList<Item>();
		moveList = new ArrayList<Move>();
	}
	
	public void buildPokemons() throws IOException, JSONException {
		
		StringBuilder cb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(pokePath))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            cb.append(line).append("\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		pokeStr = cb.toString();
		JSONArray pokeArray = new JSONArray(pokeStr);
		for (int i = 0; i < pokeArray.length(); i++) {
			JSONObject jsonObject = pokeArray.getJSONObject(i);
			
			int        pokeID =     jsonObject.getInt("ID");
			String     nome =       jsonObject.getString("Nome");
			int        sexo =       jsonObject.getInt("Sexo");
			int        pokedexId =  jsonObject.getInt("PokeDexId");
			int        pokeLevel =  jsonObject.getInt("Level");         // por padrão, inicializado com 50 no json
			String     spcName =    jsonObject.getString("SpeciesName");
			String     dexEntry =   jsonObject.getString("DexEntry");
			
			JSONArray  tipagem =    jsonObject.getJSONArray("Tipagem");
			JSONArray  moves =      jsonObject.getJSONArray("MoveSet");
			JSONArray  bSTTS =      jsonObject.getJSONArray("BaseStats");
			
			int[] typeArray = new int[tipagem.length()];
			for (int j = 0; j < tipagem.length(); j++) {
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
			
			Poke novoPoke = new Poke(pokeID, pokedexId, nome, sexo, pokeLevel, spcName, dexEntry, typeArray, baseSTArray, moveArray);
			pkmnList.add(novoPoke);
		}
	}
	
	public void buildItems() throws IOException, JSONException {
		
		StringBuilder cb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(itemPath))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            cb.append(line).append("\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
		itemStr = cb.toString();
		JSONArray itemArray = new JSONArray(itemStr);
		for (int i = 0; i < itemArray.length(); i++) {
			JSONObject jsonObject = itemArray.getJSONObject(i);
			int itemId = jsonObject.getInt("itemId");
			String itemNome = jsonObject.getString("nome");
			Item novoItem = new Item(itemId, itemNome);
			itemList.add(novoItem);
		}
	}
	
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
			String moveCat = jsonObject.getString("Category"); // String com o valor do enum, fazer cast para enum com .valueOf(moveCat)
			int basePower = 0;
			if (jsonObject.has("BasePower")) { basePower = jsonObject.getInt("BasePower");}
			String subClass = jsonObject.getString("Subclass");
			switch (subClass) {                               // Os cases estão em escopo pois alguns nomes de variáveis são reutilizados
			
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
					int fxDuration = jsonObject.getInt("FxDuration");
					boolean voltl = jsonObject.getBoolean("IsVolatile");
					StatusChangeFx novoSCF = new StatusChangeFx(moveId, moveName, moveType, moMaxPT, movePriority, moveAcc, StatusFx.typeList.valueOf(fxType), fxDuration, voltl);
					moveList.add(novoSCF);
				break;
				}
				
				case "StatusGeneral": // classe abstrata?
				{
					//StatusGeneral novoSTGN = new StatusGeneral(moveId, moveName, -1, -1, -1, moveAcc);
					//moveList.add(novoSTGN);
				break;
				}
				
				case "StatusMisc": // classe abstrata?
				{
					//StatusGeneral novoSTGN = new StatusGeneral(moveId, moveName, -1, -1, -1, moveAcc);
					//moveList.add(novoSTGN);
				break;
				}
			}
		}
	}
	
	public void atribuiMoveAPoke(Treinador player) {
		for (int i = 0; i < player.getTeam().length; i++) {
			Poke playerPoke = player.getTeam()[i];
			for (int j = 0; j < 4; j++) {
				Move moveFromList = null;
				int [] mvList = playerPoke.getMovesetList();
				int idx = 0;
				if (moveList.get(idx).getId() == mvList[j]) {
					moveFromList = moveList.get(idx);
				}
				else {
					idx++;
				}
				playerPoke.setMove(j, moveFromList);
			}
		}
	}
	
	public ArrayList<Poke> getPkmnList(){
		return pkmnList;
	}

	public ArrayList<Item> getItemList(){
		return itemList;
	}
	
	public ArrayList<Move> getMoveList(){
		return moveList;
	}
	
}
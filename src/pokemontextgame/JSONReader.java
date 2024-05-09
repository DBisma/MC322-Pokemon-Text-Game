package pokemontextgame;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONReader{
	private ArrayList<Poke> pkmnList; 
	private ArrayList<Item> itemList;
	
	Path dirPai = Paths.get("..");
	Path pokePath = dirPai.resolve("pokeJsons/poke.json");
	Path itemPath = dirPai.resolve("pokeJsons/item.json");
	
	private String pokeStr = new String(Files.readAllBytes(pokePath));
	private String itemStr = new String(Files.readAllBytes(itemPath));
	
	public JSONReader(){
		pkmnList = new ArrayList<Poke>();
		itemList = new ArrayList<Item>();
	}
	
	public void buildPokemons() {
		JSONArray pokeArray = new JSONArray(pokeStr);
		for (int i = 0; i < pokeArray.lenght(); i++) {
			JSONObject jsonObject = pokeArray.getJSONObject(i);
			int pokedexId = jsonObject.getInt("pokedexId");
			Poke novoPoke = new Poke(pokedexId);
			pkmnList.add(novoPoke);
		}
	}
	
	public void buildItems() {
		JSONArray itemArray = new JSONArray(itemStr);
		for (int i = 0; i < itemArray.lenght(); i++) {
			JSONObject jsonObject = itemArray.getJSONObject(i);
			int itemId = jsonObject.getInt("itemId");
			String itemNome = jsonObject.getString("nome");
			Item novoItem = new Item(itemId, itemNome);
			itemList.add(novoItem);
		}
	}
	
	public ArrayList<Poke> getPkmnList(){
		return pkmnList;
	}

	public ArrayList<Item> getItemList(){
		return itemList;
	}
	
}
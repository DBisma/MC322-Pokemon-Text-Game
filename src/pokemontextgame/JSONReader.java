package pokemontextgame;

import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONReader{
	ClassLoader cl = getClass().getClassLoader();
	
	String pokej = new String("src/pokemontextgame/pokeJsons/poke.json");
	String itemj = new String("src/pokemontextgame/pokeJsons/item.json");
	
	String pokePath = new File(pokej).getAbsolutePath();
	String itemPath = new File(itemj).getAbsolutePath();
	
	private ArrayList<Poke> pkmnList;
	private ArrayList<Item> itemList;
	
	private String pokeStr;
	private String itemStr;
	
	public JSONReader(){
		pkmnList = new ArrayList<Poke>();
		itemList = new ArrayList<Item>();
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
			int pokedexId = jsonObject.getInt("pokedexId");
			Poke novoPoke = new Poke(pokedexId);
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
	
	public ArrayList<Poke> getPkmnList(){
		return pkmnList;
	}

	public ArrayList<Item> getItemList(){
		return itemList;
	}
	
}
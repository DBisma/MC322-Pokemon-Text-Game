package pokemontextgame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PokeRead {
	/* 
	 * Essa classe lida com a leitura
	 * dos dados de um arquivo de texto
	 * e instancia um Pokemon com base
	 * neles
	 */
	
	
	// OBS: não sei se é melhor retornar um Pokemon
	// ou retornar uma lista deles
	public static Poke readPoke(String nomeArquivo) {
		/* 
		 * Método que vai retornar um Pokemon
		 * com base no arquivo de texto
		 */
			
		Poke poke = new Poke();
		try {
			
		} catch (IOException excessao) {
            excessao.printStackTrace();
        }
		
		return poke;
	}
}

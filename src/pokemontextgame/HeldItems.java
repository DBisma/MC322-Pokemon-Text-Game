package pokemontextgame;

public class HeldItems extends Item{
	private int tipoDoEfeito;

	// Aumentar ou reduzir dano por tipo
	// [-18 (Fada) até -1 (Normal)] redução, 
	// [0 (Normal) até 17 (Fada)] aumento

	// Aumentar stats do pokemon
	
	
	private float modEfeito;
	
	public HeldItems(int id, String nome, String desc){
		super(id, nome);
		this.setDesc(desc);
	}

}
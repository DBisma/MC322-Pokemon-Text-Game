package moves;

public class Struggle extends DmgMisc{
	/*
	 * Move especial que todo pokemon pode usar
	 * quando não tem mais PP disponível.
	 * Seu PP não diminui nunca e sempre acerta.
	 * É o move de "id 5" construído no Battlefield.
	 */
	
	public Struggle() {
		super(0, "Struggle", 19, -1, 0, -1, Move.moveCategs.PHYSICAL, 50);
	}
}

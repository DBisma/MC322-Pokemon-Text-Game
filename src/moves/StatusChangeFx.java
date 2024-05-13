package moves;

public class StatusChangeFx extends StatusChanging {
	/*
	 * Subclasse dedicada para ataques que apenas
	 * aplicam um status no oponente.
	 */
	public StatusChangeFx(int id, String name, int type, int maxP, int pri, int accu) {
		super(id, name, type, maxP, pri, accu);
	}
	
}

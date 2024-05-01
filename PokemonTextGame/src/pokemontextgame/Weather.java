package pokemontextgame;

public class Weather {
	/*
	 * Classe que lida com mudanças de tempo atmosférico
	 * durante os turnos. Lida com permanência, turnos restantes, tipo, etc.
	 */
	private int turns;
	private boolean permanent;
	private int type;
	
	// Tipos possíveis de clima são: Clear (-1), Harsh Sunlight (0), Rain (1), Sandstorm (2), Hail (3), Snow (4), Fog (5).
	// Podemos criar os nossos próprios: Wind (Voador), Moonlight (Dark)
	
	public Weather(boolean permanent, int turns, int type) {
		permanent = true;
		turns = 1;
		type = -1;
	}
	public int getTurns() {
		return turns;
	}
	public void setTurns(int turns) {
		this.turns = turns;
	}
	public boolean isPermanent() {
		return permanent;
	}
	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}

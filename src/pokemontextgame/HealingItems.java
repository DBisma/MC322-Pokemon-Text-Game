package pokemontextgame;

public class HealingItems extends Item{
	private int healValue; // Pode restaurar tanto hp, quanto pp

	// Antidote
	// Burn heal
	// Ice Heal       ---> removem status
	// Paralyze heal
	// Awakening

	private int rmvStatus;
	private boolean holdable; // Verificar se o item está sendo carregado por um pokemon, este pode ativar automaticamente

	// Se o item não for um removedor de status, inicializar com rmvStatus = -1
	public HealingItems(int id, String nome, String desc, int healValue, int rmvStatus, Boolean holdable) {
		super(id, nome);
		this.setDesc(desc);
		this.healValue = healValue;
		this.rmvStatus = rmvStatus;
		this.holdable = holdable;
	}
	
	public int getHealValue() {
		return healValue;
	}
	
	public void setHealValue(int healing) {
		this.healValue = healing;
	}

	public boolean removerStatusFx(Poke mon){
		int sts = mon.getStatusFx().getId();
		if (this.rmvStatus == sts){
			mon.getStatusFx().setId(-1);
			return true;
		}
		return false;
	}

	public boolean isHoldable(){
		return this.holdable;
	}
	
}
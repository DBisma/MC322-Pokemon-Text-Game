package moves;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TypeChart;

public class StatChange extends StatusChanging {
	/*
	 * Classe para Moves de categ Status que
	 * causam uma mudança de Stat no oponente.
	 */
	
	// Um tanto análogo a DmgPlusStat, mas sem a seção de dano
	private int statId; // qual stat
	private int boostStages; // quanto (pode ser negativo)
	private boolean boostSelf; // quem
	// Fora a accuracy do move, a chance do efeito é sempre de 100%
	
	public StatChange(int id, String name, int type, int maxP, int pri, int accu,
			int statId, int boostStage, boolean boostSelf) {
		super(id, name, type, maxP, pri, accu);
		this.statId = statId;
		this.boostStages = boostStage;
		this.boostSelf = boostSelf;
	}
	
	public moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Tenta aumentar ou abaixar o status de algum pokemon (si ou o inimigo).
		 * Armazena o resultado da tentativa e retorna o tipo (aumento / redução)
		 * juntamente do sucesso / fracasso entre quatro enums possíveis.
		 */
		boolean output;
		moveResults resu;
		if(this.boostSelf) 
			output = pAtk.boostStat(statId, boostStages);
		else
			output = pDef.boostStat(statId, boostStages);
		
		if(boostStages > 0) {
			if(output)
				resu = moveResults.RAISE_YES;
			else
				resu = moveResults.RAISE_FAIL;
		}
		else {
			if(output)
				resu = moveResults.LOWER_YES;
			else
				resu = moveResults.LOWER_FAIL;
		}
		// TODO: Como enviar notificações de sucesso / fracasso / reduzir / aumentar stats?
		
		return resu;
	}
}

package pokemontextgame.moves;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.Move.moveResults;

final public class StatChange extends StatusGeneral {
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
	
	@Override
	public moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Tenta aumentar ou abaixar o status de algum pokemon (si ou o inimigo).
		 * Armazena o resultado da tentativa e retorna o tipo (aumento / redução)
		 * juntamente do sucesso / fracasso entre quatro enums possíveis.
		 */

		moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		if(resu == moveResults.MISS) {
			return resu;
		}
		// Verificando imunidade do inimigo apenas se ele for o alvo
		else if(!this.boostSelf && tchart.compoundTypeMatch(type, pDef) < 0.001) {
			field.textBufferAdd("Mas não afetou " + pDef.getName()  + " !\n");
			return moveResults.HIT_IMMUNE;
		}
		// TODO: Deve ter um jeito de fundir isso com a parte de StatChange dos dmgPlusStat...
		// São bastante parecidas.
		else {
			boolean sucess;
			String verb;
			String who;
			String plural = (Math.abs(boostStages) > 1 ? "estágio" : "estágios");
			if(this.boostSelf) {
				sucess = pAtk.boostStat(statId, boostStages);
				who = pAtk.getName();
			}
			else {
				sucess = pDef.boostStat(statId, boostStages);
				who = pDef.getName();
			}
			
			if(sucess) {
				if(boostStages > 0) {
					resu = moveResults.RAISE_YES;
					verb = "aumentado";
				}
				else {
					resu = moveResults.LOWER_YES;
					verb = "reduzido";
				}
				
				field.textBufferAdd(who + " teve seu " + TurnUtils.getStatName(statId) 
				+ " " + verb + " em " + Math.abs(boostStages) + " " + plural + " !\n");
			}
			else {
				if(boostStages > 0) {
					resu = moveResults.RAISE_FAIL;
					verb = "crescer";
				}
				else {
					resu = moveResults.LOWER_FAIL;
					verb = "diminuir";
				}
				field.textBufferAdd(TurnUtils.getStatName(statId) + " de " + who
				+ " não consegue " + verb + " mais!\n");
			}
			return resu;
		}
	}
	
}

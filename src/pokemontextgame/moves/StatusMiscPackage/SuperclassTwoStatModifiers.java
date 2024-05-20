package pokemontextgame.moves.StatusMiscPackage;

import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;
import pokemontextgame.TypeChart;
import pokemontextgame.moves.StatusMisc;

public class SuperclassTwoStatModifiers extends StatusMisc {
	/*
	 * Superclasse mãe de moves que recebem
	 * dois stats para modificação.
	 * São mais raros que os de stat único, mas são
	 * ainda assim suficientemente presentes para
	 * justificar uma classe comum.
	 */

	// quais
	private int[] statIds;
	// quanto
	private int[] boostStages;
	// quem
	private boolean[] boostSelf;
	
	
	public SuperclassTwoStatModifiers(int id, String name, int type, int maxP, int pri, int accu,
		int statIdA, int statIdB, int boostStageA, int boostStageB, boolean boostSelfA, boolean boostSelfB) {
	super(id, name, type, maxP, pri, accu);
	
	this.statIds = new int[2];
	this.boostStages = new int[2];
	this.boostSelf = new boolean[2];
	
	this.statIds[0] = statIdA;
	this.statIds[1] = statIdB;
	this.boostStages[0] = boostStageA;
	this.boostStages[1] = boostStageB;
	this.boostSelf[0] = boostSelfA;
	this.boostSelf[1] = boostSelfB;
	}
	
	@Override
	public moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Tenta aumentar ou abaixar o status de algum pokemon (si ou o inimigo).
		 * Retorna TOTAL_SUCCESS, TOTAL_FAILURE ou PARTIAL_SUCCESS depedendo do resultado.
		 */
		
		boolean[] successArray = new boolean[2];
		moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		if(resu == moveResults.TOTAL_FAILURE || resu == moveResults.MISS|| resu == moveResults.HIT_IMMUNE) {
			return resu;
		}
		else {
			String verb;
			String who;
			String plural;
			int i;
			for(i = 0; i < 2; i++) {
				plural = (Math.abs(boostStages[i]) > 1 ? "estágio" : "estágios");
				if(this.boostSelf[i]) {
					successArray[i] = pAtk.boostStat(statIds[i], boostStages[i]);
					who = pAtk.getName();
				}
				else {
					successArray[i] = pDef.boostStat(statIds[i], boostStages[i]);
					who = pDef.getName();
				}
				
				if(successArray[i]) {
					if(boostStages[i] > 0) {
						verb = "aumentado";
					}
					else {
						verb = "reduzido";
					}
					
					field.textBufferAdd(who + " teve seu stat " + TurnUtils.getStatName(statIds[i]) 
					+ " " + verb + " em " + Math.abs(boostStages[i]) + " " + plural + " !\n");
				}
				else {
					if(boostStages[i] > 0) {
						verb = "crescer";
					}
					else {
						verb = "diminuir";
					}
					field.textBufferAdd(TurnUtils.getStatName(boostStages[i]) + " de " + who
					+ " não consegue " + verb + " mais!\n");
				}
			}
			
			if(successArray[0] == successArray[1] == true) {
				return moveResults.TOTAL_SUCCESS;
			}
			else if(successArray[0] == successArray[1] == false) {
				return moveResults.TOTAL_FAILURE;
			}
			else {
				return moveResults.PARTIAL_SUCCESS;
			}
		}
	}
}

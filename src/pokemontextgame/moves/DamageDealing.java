package pokemontextgame.moves;

import pokemontextgame.TypeChart;
import pokemontextgame.BattleMenu;
import pokemontextgame.Battlefield;
import pokemontextgame.Poke;
import pokemontextgame.TurnUtils;

public class DamageDealing extends Move {
	/*
	 * Classe para moves que causam dano.
	 * Terá subclasses para moves mais
	 * complexos com efeitos adicionais.
	 */
	private Move.moveCategs categ;
	protected int basePower;
	
	public DamageDealing(int id, String name, int type, int maxP, int pri, int accu, String desc, Move.moveCategs categ, int bp) {
		super(id, name, type, maxP, pri, accu, desc);
		this.categ = categ;
		this.basePower = bp;
	}
	
	@Override
	public Move.moveResults useMove(Battlefield field, Poke pAtk, Poke pDef, TypeChart tchart) {
		/*
		 * Recebe o field, o atacante e o defensor.
		 * Calcula a chance de acerto. Aplica o dano se acertar.
		 * Retorna o resultado do Move.
		 */
		
		Move.moveResults resu = super.useMove(field, pAtk, pDef, tchart);
		// Checagem de Miss
		if(resu == Move.moveResults.MISS) {
			return resu;
		}
		// Caso de acerto
		else {
			// Por enquanto, moves não podem falhar, apenas errar.
			// Calcular modificador de dano baseado em eficácia de tipos
			float typeMod = tchart.compoundTypeMatch(this.type, pDef);
			// Caso de imunidade
			float error = 0.01f;
			if(typeMod < error) {
				field.textBufferAdd("Mas não afetou " + pDef.getName()  + "!\n");
				return Move.moveResults.HIT_IMMUNE;
			}
			// Caso contrário, cálculo e aplicação de dano
			int dmg = TurnUtils.calcDmg(this, pAtk, pDef, typeMod);
			pDef.dmgMon(dmg);
			// Comparação de floats para retornar efetividade
			if(Math.abs(typeMod - 0.5f) < error) {
				field.textBufferAdd("Não foi muito eficaz...\n");
				return Move.moveResults.HIT_NOTVERY;
			}
			else if(Math.abs(typeMod - 1f) < error) {
				return Move.moveResults.HIT;
			}
			else {
				field.textBufferAdd("Foi super eficaz!\n");
				return Move.moveResults.HIT_SUPER;
			}
		}
	}
	
	@Override
	public Move.moveCategs getCateg() {
		return categ;
	}

	public void setCateg(Move.moveCategs categ) {
		this.categ = categ;
	}

	public int getBasePower() {
		return basePower;
	}

	public void setBasePower(int basePower) {
		this.basePower = basePower;
	}

	@Override
	public String toString() {
		/*
		 * Semelhante ao super.toString, mas
		 * muda a ordem de algumas informações
		 * e adiciona informações novas.
		 */
		String newdesc;
		String accu = "";
		String output;
		
		if(this.desc == null)
			newdesc = "Nenhuma descrição disponível.";
		else
			newdesc = "Descrição: " + desc;
		if(accuracy == -1) {
			accu = "Sempre acerta";
		}
		else
			accu += this.accuracy;
		
		output = BattleMenu.alignString("Move:") + "'"+ this.name + "'\n"
				+ BattleMenu.alignString("Tipo:") + TypeChart.typeToString(this.type) + "\n"
				+ BattleMenu.alignString("Categoria:") + this.categ + "\n"
				+ BattleMenu.alignString("Poder:") + this.basePower + "\n"
				+ BattleMenu.alignString("PP:") + this.points + " / " + this.maxPoints + "\n"
				+ BattleMenu.alignString("Prioridade:") + this.priority + "\n"
				+ BattleMenu.alignString("Precisão:") + accu + "\n"
				+ newdesc + "\n";
		
		return output;
	}
}

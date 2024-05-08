package pokemontextgame;

public class TreinadorNpc extends Treinador{
	/*
	 * Classe que lida com o treinador especial NPC.
	 * Possui métodos de tomada de sua decisão.
	 * Possui alguns coeficientes de dificuldade.
	 */
	public TreinadorNpc(int id, String nome, boolean player) {
		super(id, nome, player);
		
	}
	
	static void npcThink(Battlefield field) {
		/*
		 * Função de placeholder para processo
		 * de decisão do NPC.
		 * Possui uma chance de tomar a decisão otimizada
		 * e um processo para determinar a melhor decisão.
		 * Altera a decisão do NPC no battlefield.
		 * Mais explicação no corpo do texto.
		 */
		
		// macros
		TreinadorNpc npc = field.getLoadedNpc();
		Poke mon = npc.getActiveMon();
		Poke foe = field.getLoadedPlayer().getActiveMon();
		
		/*
		 *  Calcula o dano que irá receber caso inimigo repita o último move de dano.
		 *  Se tiver certeza de morte e não possuir alta velocidade, troca.
		 */
		
		// Verificar se pokemon ativo está vivo
		// Checar própria vida
		// Mais da metade
		if(mon.getCurHp() > (int) mon.getMaxHp()*0.5f) {
			// Arriscar um Hit Kill se a diferença ataque seu / defesa seu com STAB / Super Efetivo for suficiente
			//if()
			// Aplicar status se não tiver aplicado ainda
			if(foe.getStatusFx().getType() != StatusFx.typeList.NEUTRAL) {
				// buscar move que dê status
			}
			// Tenta aumentar os próprios stats; essa chance diminui quanto mais altos estiverem
			// Busca move mais potente com STAB não-atenuado ou super efetivo
			// Se não tiver nada disso, roll de trocar pokemon
			// Se falhar nesse roll, escolhe o ataque menos pior
		}
		else {
			// Se o ataque anterior do jogador tirou muita vida, trocar para poke com resistência a esse ataque
			// Se não houver, pegar pokemon com alta defesa em relação ao tipo de dano utilizado no ataque anterior
			// Se não houver, tentar um ataque suicida
			// Se não houver, usar ataque que cause mais dano
		}
		
		// Caso não esteja vivo, faz a troca mais otimizada que puder
		
	}
}

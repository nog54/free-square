package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class KnowledgeInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public KnowledgeInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addKnowledge(amount * this.getInfluencePerAmount());
	}
}

package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class ConditionInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public ConditionInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addCommunication(amount * this.getInfluencePerAmount());
	}
}

package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class FlexibilityInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public FlexibilityInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addFlexibility(amount * this.getInfluencePerAmount());
	}
}

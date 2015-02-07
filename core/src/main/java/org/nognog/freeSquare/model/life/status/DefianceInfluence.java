package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class DefianceInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public DefianceInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addDefiance(amount * this.getInfluencePerAmount());
	}
}

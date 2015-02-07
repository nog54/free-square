package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class ToleranceInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public ToleranceInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addTolerance(amount * this.getInfluencePerAmount());
	}
}

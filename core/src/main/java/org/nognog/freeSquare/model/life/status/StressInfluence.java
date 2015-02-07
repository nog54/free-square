package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class StressInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public StressInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addStress(amount * this.getInfluencePerAmount());
	}
}

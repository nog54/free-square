package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class PowerInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public PowerInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addPower(amount * this.getInfluencePerAmount());
	}
}

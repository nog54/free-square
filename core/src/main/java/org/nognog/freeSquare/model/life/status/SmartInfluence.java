package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class SmartInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public SmartInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addSmart(amount * this.getInfluencePerAmount());
	}
}
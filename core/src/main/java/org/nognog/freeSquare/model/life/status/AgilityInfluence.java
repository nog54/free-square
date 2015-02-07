package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class AgilityInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public AgilityInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addAgility(amount * this.getInfluencePerAmount());
	}
}

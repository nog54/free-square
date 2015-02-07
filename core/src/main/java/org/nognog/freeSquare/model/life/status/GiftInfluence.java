package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class GiftInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public GiftInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addGift(amount * this.getInfluencePerAmount());
	}
}

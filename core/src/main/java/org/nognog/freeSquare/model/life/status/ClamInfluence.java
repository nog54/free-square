package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class ClamInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public ClamInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addClam(amount * this.getInfluencePerAmount());
	}
}

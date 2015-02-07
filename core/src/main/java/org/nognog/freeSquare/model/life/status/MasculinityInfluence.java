package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class MasculinityInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public MasculinityInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addMasculinity(amount * this.getInfluencePerAmount());
	}
}

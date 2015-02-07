package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class FortitudeInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public FortitudeInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addFortitude(amount * this.getInfluencePerAmount());
	}
}

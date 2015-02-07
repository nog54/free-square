package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi
 * 2015/02/08
 */
public class CuriosityInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public CuriosityInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addCuriosity(amount * this.getInfluencePerAmount());
	}
}

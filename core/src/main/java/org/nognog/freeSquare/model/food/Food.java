package org.nognog.freeSquare.model.food;

import static org.nognog.freeSquare.Messages.getString;

@SuppressWarnings("javadoc")
public enum Food {
	// Taste(sweetness, sourness, saltiness, bitterness, umami, hotness,
	// astringency, stimulation)
	TOFU(getString("tofu"), 100, Taste.TASTELESS), //$NON-NLS-1$
	RED_PEPPER_TOFU(getString("red-pepper-tofu"), 100, Taste.HOT), //$NON-NLS-1$
	MINT_TOFU(getString("mint-tofu"), 100, Taste.BITTER, Taste.STIMULATION), //$NON-NLS-1$
	WORMWOOD_TOFU(getString("wormwood-tofu"), 100, Taste.ASTRINGENCY), //$NON-NLS-1$
	FREEZE_DRIED_TOFU(getString("freeze-dried-tofu"), 100, Taste.SALT), //$NON-NLS-1$
	MASTATD_TOFU(getString("mastard-tofu"), 100, Taste.STIMULATION), //$NON-NLS-1$
	ICE_TOFU(getString("ice-tofu"), 100, Taste.SWEET), //$NON-NLS-1$
	GOLD_SESAME_TOFU(getString("gold-sesame-tofu"), 100, Taste.UMAMI), //$NON-NLS-1$
	BLACK_SESAME_TOFU(getString("black-sesame-tofu"), 100, Taste.UMAMI), //$NON-NLS-1$

	;

	private Food(String name, int quantity, Taste... taste) {
		this.name = name;
		this.quantity = quantity;
		this.taste = taste;
	}

	private final String name;
	private final int quantity;
	private final Taste[] taste;

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * @return the taste
	 */
	public Taste[] getTaste() {
		return this.taste;
	}
}

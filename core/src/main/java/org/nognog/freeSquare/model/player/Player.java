package org.nognog.freeSquare.model.player;

import java.util.Date;

import org.nognog.freeSquare.model.Savable;

/**
 * @author goshi 2014/10/28
 */
public class Player implements Savable {
	private static final String defaultName = "noname"; //$NON-NLS-1$

	private String name;
	private long startDate;

	private ItemBox itemBox;

	@SuppressWarnings("unused")
	private Player() {
		this(defaultName);
	}

	/**
	 * @param name
	 */
	public Player(String name) {
		if (name == null) {
			this.name = defaultName;
		} else {
			this.name = name;
		}
		this.startDate = new Date().getTime();
		this.itemBox = new ItemBox();
	}

	/**
	 * プレイヤー名を返します
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 開始日を返します
	 * 
	 * @return startDate
	 */
	public Date getStartDate() {
		return new Date(Long.valueOf(this.startDate).longValue());
	}

	@Override
	public boolean isValid() {
		return this.name != null;
	}

	/**
	 * @return itemBox
	 */
	public ItemBox getItemBox() {
		return this.itemBox;
	}

}

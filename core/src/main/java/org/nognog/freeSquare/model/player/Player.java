package org.nognog.freeSquare.model.player;

import java.util.Date;

import org.nognog.freeSquare.model.Savable;
import org.nognog.freeSquare.model.item.Item;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2014/10/28
 */
public class Player implements Savable, ItemBoxObserver {
	private static final String defaultName = "noname"; //$NON-NLS-1$

	private String name;
	private long startDate;

	private transient Array<PlayerObserver> observers;

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
		this.itemBox.addObserver(this);
		this.observers = new Array<>();
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

	/**
	 * @param item
	 * @return quantity after the put
	 */
	public <T extends Item<T, ?>> int putItem(T item) {
		final int quantity = this.itemBox.putItem(item);
		return quantity;
	}

	/**
	 * @param item
	 * @return quantity after the take out
	 */
	public <T extends Item<T, ?>> int takeOutItem(T item) {
		final int quantity = this.itemBox.takeOutItem(item);
		return quantity;
	}

	/**
	 * @param observer
	 */
	public void addObserver(PlayerObserver observer) {
		if (!this.observers.contains(observer, true)) {
			this.observers.add(observer);
		}
	}

	/**
	 * @param observer
	 */
	public void removeObserver(PlayerObserver observer) {
		this.observers.removeValue(observer, true);
	}

	/**
	 * 
	 */
	public void notifyObservers() {
		for (int i = 0; i < this.observers.size; i++) {
			this.observers.get(i).update();
		}
	}

	@Override
	public void update() {
		this.notifyObservers();
	}

	@Override
	public void reconstruction() {
		this.itemBox.addObserver(this);
	}

}

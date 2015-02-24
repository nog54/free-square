package org.nognog.freeSquare.model.player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.persist.PersistItemClass;
import org.nognog.freeSquare.model.square.Square;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2014/10/28
 */
public class Player implements PersistItemClass, ItemBoxObserver, Json.Serializable {
	private static final String defaultName = "noname"; //$NON-NLS-1$

	private String name;
	private long startDate;

	private transient Array<PlayerObserver> playerObservers;

	private ItemBox itemBox;
	private Array<Life> lifes;
	private Array<Square<?>> squares;

	@SuppressWarnings("unused")
	private Player() {
		// used by json
		this(defaultName);
	}

	/**
	 * @param name
	 */
	public Player(String name) {
		this.name = name;
		this.startDate = new Date().getTime();
		this.itemBox = new ItemBox();
		this.itemBox.addObserver(this);
		this.playerObservers = new Array<>();
		this.lifes = new Array<>();
		this.squares = new Array<>();
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
	 * @param addedLife
	 */
	public void addLife(Life addedLife) {
		if (!this.lifes.contains(addedLife, true)) {
			this.lifes.add(addedLife);
			this.notifyPlayerObservers();
		}
	}

	/**
	 * @param removedLife
	 */
	public void removeLife(Life removedLife) {
		if (this.lifes.removeValue(removedLife, true)) {
			this.notifyPlayerObservers();
		}
	}

	/**
	 * @return life array
	 */
	public Array<Life> getLifes() {
		return this.lifes;
	}

	/**
	 * @param addedSquare
	 */
	public void addSquare(Square<?> addedSquare) {
		if (!this.squares.contains(addedSquare, true)) {
			this.squares.add(addedSquare);
			this.notifyPlayerObservers();
		}
	}

	/**
	 * @param removedSquare
	 */
	public void removeSquare(Square<?> removedSquare) {
		if (this.squares.removeValue(removedSquare, true)) {
			this.notifyPlayerObservers();
		}
	}

	/**
	 * @return the squares
	 */
	public Array<Square<?>> getSquares() {
		return this.squares;
	}

	/**
	 * @param searchLife
	 * @return true if searchLife is contained
	 */
	public boolean containsLife(Life searchLife) {
		return this.lifes.contains(searchLife, true);
	}

	/**
	 * @param item
	 * @return quantity after the put
	 */
	public int putItem(Item<?, ?> item) {
		final int quantity = this.itemBox.putItem(item);
		return quantity;
	}

	/**
	 * @param item
	 * @return quantity after the take out
	 */
	public int takeOutItem(Item<?, ?> item) {
		final int quantity = this.itemBox.takeOutItem(item);
		return quantity;
	}

	/**
	 * @param observer
	 */
	public void addObserver(PlayerObserver observer) {
		if (!this.playerObservers.contains(observer, true)) {
			this.playerObservers.add(observer);
		}
	}

	/**
	 * @param observer
	 */
	public void removeObserver(PlayerObserver observer) {
		this.playerObservers.removeValue(observer, true);
	}

	/**
	 * 
	 */
	public void notifyPlayerObservers() {
		for (int i = 0; i < this.playerObservers.size; i++) {
			this.playerObservers.get(i).updatePlayer();
		}
	}

	@Override
	public void updateItemBox() {
		this.notifyPlayerObservers();
	}

	@Override
	public void write(Json json) {
		for (Field field : Player.class.getDeclaredFields()) {
			final int modifiers = field.getModifiers();
			if (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers)) {
				continue;
			}
			json.writeField(this, field.getName());
		}
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		for (Field field : Player.class.getDeclaredFields()) {
			final int modifiers = field.getModifiers();
			if (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers)) {
				continue;
			}
			json.readField(this, field.getName(), jsonData);
		}
		this.itemBox.addObserver(this);
	}
}

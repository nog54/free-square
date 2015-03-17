package org.nognog.freeSquare.model.player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.persist.PersistItemClass;
import org.nognog.freeSquare.model.square.Square;
import org.nognog.freeSquare.square2d.Square2d;

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
	 * @param life
	 */
	public void addLife(Life life) {
		if (!this.lifes.contains(life, true)) {
			this.lifes.add(life);
			this.notifyPlayerObservers();
		}
	}

	/**
	 * @param life
	 */
	public void removeLife(Life life) {
		if (this.lifes.removeValue(life, true)) {
			this.notifyPlayerObservers();
		}
	}

	/**
	 * @param oldLife
	 * @param newLife
	 */
	public void replaceLife(Life oldLife, Life newLife) {
		final int oldLifeIndex = this.lifes.indexOf(oldLife, true);
		if (oldLifeIndex == -1) {
			return;
		}
		this.lifes.set(oldLifeIndex, newLife);
	}

	/**
	 * @return life array
	 */
	public Life[] getLifes() {
		return this.lifes.toArray(Life.class);
	}

	/**
	 * @param square
	 */
	public void addSquare(Square<?> square) {
		if (!this.squares.contains(square, true)) {
			this.squares.add(square);
			this.notifyPlayerObservers();
		}
	}

	/**
	 * @param square
	 */
	public void removeSquare(Square<?> square) {
		if (this.squares.removeValue(square, true)) {
			this.notifyPlayerObservers();
		}
	}

	/**
	 * @param oldSquare
	 * @param newSquare
	 */
	public void replaceSquare(Square2d oldSquare, Square2d newSquare) {
		final int oldSquareIndex = this.squares.indexOf(oldSquare, true);
		if (oldSquareIndex == -1) {
			return;
		}
		this.squares.set(oldSquareIndex, newSquare);
	}

	/**
	 * @return the squares
	 */
	public Square<?>[] getSquares() {
		return this.squares.toArray(Square.class);
	}

	/**
	 * clear all lifes
	 */
	public void clearLifes() {
		this.lifes.clear();
		this.notifyPlayerObservers();
	}

	/**
	 * clear all squares
	 */
	public void clearSquares() {
		this.squares.clear();
		this.notifyPlayerObservers();
	}

	/**
	 * @param searchLife
	 * @return true if searchLife is contained
	 */
	public boolean containsLife(Life searchLife) {
		return this.lifes.contains(searchLife, true);
	}

	/**
	 * @param searchSquare
	 * @return true if searchSquare is contained
	 */
	public boolean containsSquare(Square<?> searchSquare) {
		return this.squares.contains(searchSquare, true);
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

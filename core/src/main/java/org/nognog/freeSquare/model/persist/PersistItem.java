package org.nognog.freeSquare.model.persist;

import org.nognog.freeSquare.model.Savable;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.PlayLog;
import org.nognog.freeSquare.model.player.Player;

/**
 * 保存項目を表すクラス 列挙型でジェネリクス使えるなら使いたい
 * 
 * @author goshi 2014/10/30
 * @param <T>
 *            save object class
 */
public class PersistItem<T extends Savable> {

	/** プレイヤー情報 */
	public static final PersistItem<Player> PLAYER = new PersistItem<>(Player.class, "player"); //$NON-NLS-1$

	/** ライフ1 */
	public static final PersistItem<Life> LIFE1 = new PersistItem<>(Life.class, "life1"); //$NON-NLS-1$

	/** 最終プレイ */
	public static final PersistItem<LastPlay> LAST_PLAY = new PersistItem<>(LastPlay.class, "lastPlay"); //$NON-NLS-1$

	/** プレイ記録 */
	public static final PersistItem<PlayLog> PLAY_LOG = new PersistItem<>(PlayLog.class, "playlog"); //$NON-NLS-1$
	
	static final PersistItem<Life> TEST_ITEM = new PersistItem<>(Life.class, "PersistManagerTestItem"); //$NON-NLS-1$

	private static final PersistItem<?>[] items = new PersistItem[] { PLAYER, LIFE1, LAST_PLAY, PLAY_LOG };

	private final Class<T> saveClass;
	private final String fileName;

	private PersistItem(Class<T> saveClass, final String fileName) {
		this.saveClass = saveClass;
		this.fileName = fileName;
	}

	/**
	 * @param saveObject
	 * @return is success
	 */
	public boolean save(T saveObject) {
		try {
			PersistManager.save(this, saveObject);
			return true;
		} catch (SaveFailureException e) {
			return false;
		}
	}

	/**
	 * @param saveObject
	 * @throws SaveFailureException
	 */
	public void saveWithException(T saveObject) throws SaveFailureException {
		PersistManager.save(this, saveObject);
	}

	/**
	 * null means failure
	 * 
	 * @return loaded item or null
	 */
	public T load() {
		try {
			T loadItem = this.loadWithException();
			return loadItem;
		} catch (LoadFailureException e) {
			return null;
		}
	}

	/**
	 * @return loaded item
	 * @throws LoadFailureException
	 */
	public T loadWithException() throws LoadFailureException {
		return PersistManager.load(this);
	}

	/**
	 * @return class of save Object
	 */
	public Class<T> getSaveClass() {
		return this.saveClass;
	}

	/**
	 * @return name of save file
	 */
	public String getFileName() {
		return this.fileName;
	}

	void changeSaveEncryptionKey(byte[] oldKey, byte[] newKey) throws SaveFailureException, LoadFailureException {
		PersistManager.save(this, PersistManager.load(this, oldKey), newKey);
	}

	/**
	 * @return all persist item
	 */
	public static PersistItem<?>[] values() {
		return items;
	}

}

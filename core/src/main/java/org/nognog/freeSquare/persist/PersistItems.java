/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.persist;

import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.PlayLog;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectTypeDictionary;
import org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectTypeDictionary;

/**
 * 保存項目を表すクラス 列挙型でジェネリクス使えるなら使いたい
 * 
 * @author goshi 2014/10/30
 * @param <T>
 *            save object class
 */
public class PersistItems<T extends PersistItemClass> {

	/** プレイヤー */
	public static final PersistItems<Player> PLAYER = new PersistItems<>(Player.class, "player"); //$NON-NLS-1$

	/** 最終プレイ */
	public static final PersistItems<LastPlay> LAST_PLAY = new PersistItems<>(LastPlay.class, "lastPlay"); //$NON-NLS-1$

	/** プレイ記録 */
	public static final PersistItems<PlayLog> PLAY_LOG = new PersistItems<>(PlayLog.class, "playlog"); //$NON-NLS-1$

	/** 外部から読み込んだLifeObjectType */
	public static final PersistItems<ExternalLifeObjectTypeDictionary> EXTERNAL_LIFE_OBJECT_TYPES = new PersistItems<>(ExternalLifeObjectTypeDictionary.class, "externalLifeObjects"); //$NON-NLS-1$

	/** 外部から読み込んだOtherObjectType */
	public static final PersistItems<ExternalOtherObjectTypeDictionary> EXTERNAL_OTHER_OBJECT_TYPES = new PersistItems<>(ExternalOtherObjectTypeDictionary.class, "externalOtherObjects"); //$NON-NLS-1$

	
	static final PersistItems<Player> TEST_ITEM = new PersistItems<>(Player.class, "PersistManagerTestItem"); //$NON-NLS-1$

	private static final PersistItems<?>[] items = new PersistItems[] { PLAYER, LAST_PLAY, PLAY_LOG };

	private final Class<T> saveClass;
	private final String fileName;

	private PersistItems(Class<T> saveClass, final String fileName) {
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
	public static PersistItems<?>[] values() {
		return items;
	}

}

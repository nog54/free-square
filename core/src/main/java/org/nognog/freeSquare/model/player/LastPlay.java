package org.nognog.freeSquare.model.player;

import java.util.Date;

import org.nognog.freeSquare.model.persist.LoadFailureException;
import org.nognog.freeSquare.model.persist.PersistItems;
import org.nognog.freeSquare.model.persist.PersistItemClass;

/**
 * @author goshi 2014/11/18
 */
public class LastPlay implements PersistItemClass {
	private long lastPlayDateTime;

	private LastPlay() {
		// used by json
	}

	private LastPlay(Date date) {
		this.lastPlayDateTime = date.getTime();
	}

	/**
	 * 最終プレイの日付を返します
	 * 
	 * @return startDate
	 * @throws LoadFailureException
	 */
	public static Date getLastPlayDate() {
		LastPlay date = PersistItems.LAST_PLAY.load();
		if (date != null) {
			return new Date(date.lastPlayDateTime);
		}
		return null;
	}

	/**
	 * persist new LastPlay and return persisted Date
	 * 
	 * @return persisted instance
	 */
	public static Date update() {
		LastPlay saveObject = new LastPlay(new Date());
		boolean isSaveSuccess = PersistItems.LAST_PLAY.save(saveObject);
		if (isSaveSuccess) {
			return new Date(saveObject.lastPlayDateTime);
		}
		return null;
	}

	@Override
	public boolean isValid() {
		long now = new Date().getTime();
		if (this.lastPlayDateTime > now) {
			return false;
		}
		return true;
	}
}

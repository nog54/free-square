package org.nognog.freeSquare.model.player;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.nognog.freeSquare.model.PersistItemClass;
import org.nognog.freeSquare.model.persist.PersistItem;

/**
 * @author goshi 2015/01/15
 */
public class PlayLog implements PersistItemClass {

	private String log;

	/**
	 * 
	 */
	private PlayLog() {

	}

	/**
	 * @return new instance
	 */
	public static PlayLog create() {
		PlayLog newInstance = new PlayLog();
		StringBuilder builder = new StringBuilder();
		String dateString = new SimpleDateFormat("yyyy/MM/dd/ ").format(new Date()); //$NON-NLS-1$
		newInstance.log = builder.append(dateString).append("プレイング開始").toString(); //$NON-NLS-1$
		return newInstance;
	}

	/**
	 * persist new LastPlay and return persisted Date
	 * 
	 * @param addLog
	 * 
	 * @return persisted instance
	 */
	public boolean update(String addLog) {
		String dateString = new SimpleDateFormat("yyyy/MM/dd/ ").format(new Date()); //$NON-NLS-1$
		this.log = new StringBuilder(this.log).append(System.getProperty("line.separator")).append(dateString).append(addLog).toString(); //$NON-NLS-1$
		return PersistItem.PLAY_LOG.save(this);
	}

	/**
	 * @return log
	 */
	public String getLog() {
		return this.log;
	}

	@Override
	public boolean isValid() {
		return this.log != null;
	}

	@Override
	public void reconstruction() {
		//
	}

}

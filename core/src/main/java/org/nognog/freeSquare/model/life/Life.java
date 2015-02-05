package org.nognog.freeSquare.model.life;

import org.nognog.freeSquare.model.PersistItemClass;

/**
 * @author goshi 2014/08/25
 */
public class Life implements PersistItemClass {
	private Family family;
	private Status status;

	@SuppressWarnings("unused")
	private Life() {
		// used by json
	}

	/**
	 * @param family
	 */
	public Life(Family family) {
		this(family, new Status());
	}

	/**
	 * @param family
	 * @param status
	 */
	public Life(Family family, Status status) {
		this.family = family;
		this.status = status;
	}

	/**
	 * 種族を返す
	 * 
	 * @return family
	 */
	public Family getFamily() {
		return this.family;
	}

	/**
	 * ステータス
	 * 
	 * @return status
	 */
	public Status getStatus() {
		return this.status;
	}

	@Override
	public boolean isValid() {
		if (this.family == null) {
			return false;
		}
		if (this.status == null) {
			return false;
		}
		if (!this.status.isValid()) {
			return false;
		}
		return true;
	}

	@Override
	public void reconstruction() {
		// 
	}
}

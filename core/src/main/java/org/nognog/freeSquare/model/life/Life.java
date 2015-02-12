package org.nognog.freeSquare.model.life;

import org.nognog.freeSquare.model.SelfValidatable;
import org.nognog.freeSquare.model.life.status.Status;

/**
 * @author goshi 2014/08/25
 */
public class Life implements SelfValidatable {
	private String name;
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
		this(family.getName(), family);
	}

	/**
	 * @param name
	 * @param family
	 */
	public Life(String name, Family family) {
		this(name, family, new Status());
	}

	/**
	 * @param name
	 * @param family
	 * @param status
	 */
	public Life(String name, Family family, Status status) {
		this.name = name;
		this.family = family;
		this.status = status;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	public String toString() {
		return this.name;
	}
	
	/**
	 * @return status string
	 */
	public String getStatusString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name :").append(this.name).append(System.getProperty("line.separator")).append(this.status); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}
}

package org.nognog.freeSquare;

/**
 * @author goshi 2014/11/21
 */
public final class RepeatRunThread extends java.lang.Thread {

	private boolean isHaltRequested = false;

	/**
	 * @param target
	 */
	public RepeatRunThread(Runnable target) {
		super(target);
	}

	@Override
	public void run() {
		while (!this.isHaltRequested) {
			super.run();
			if (Thread.interrupted()) {
				break;
			}
		}
	}

	/**
	 * スレッドを停止します。
	 */
	public void halt() {
		this.isHaltRequested = true;
	}
}

package org.nognog.freeSquare.model;


/**
 *  自己検証可能なことを示すインターフェース
 * 
 * @author goshi
 * 2014/11/18
 */
public interface SelfValidatable {
	/**
	 * 自己検証
	 * @return 有効か
	 */
	boolean isValid();
}

package org.nognog.freeSquare.model.persist;

import org.nognog.freeSquare.model.SelfValidatable;

/**
 * PersistManager経由で永続化する対象はPersistItemClassを実装しているものを対象としています。
 * また、jsonファイルから復元したオブジェクトが不正でないか検証できるように、SelfValidatableを継承します。
 * PersistManager経由でのオブジェクト復元では、復元後にオブジェクト検証が行われます。
 * 
 * @author goshi
 * 2014/11/18
 */
public interface PersistItemClass extends SelfValidatable{
	//
}

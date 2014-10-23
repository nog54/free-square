package org.nognog.freeSquare.model;

/**
 * Savableインターフェースを実装することで、PersistManager経由でjson形式で永続化できるようになります。
 * また、jsonファイルから復元したオブジェクトが不正でないか検証できるように、SelfValidatableを継承します。
 * PersistManager経由でのオブジェクト復元では、復元後に自動的にオブジェクト検証が行われます。
 * 
 * @author goshi
 * 2014/11/18
 */
public interface Savable extends SelfValidatable{
	// no method
}

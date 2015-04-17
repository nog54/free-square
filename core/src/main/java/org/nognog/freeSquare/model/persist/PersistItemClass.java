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

package org.nognog.freeSquare.model.persist;

import org.nognog.freeSquare.model.SelfValidatable;

/**
 * PersistManager経由で永続化する対象はPersistItemClassを実装しているものを対象としています。
 * また、jsonファイルから復元したオブジェクトが不正でないか検証できるように、SelfValidatableを継承します。
 * PersistManager経由でのオブジェクト復元では、復元後にオブジェクト検証が行われます。
 * 
 * @author goshi 2014/11/18
 */
public interface PersistItemClass extends SelfValidatable {
	//
}

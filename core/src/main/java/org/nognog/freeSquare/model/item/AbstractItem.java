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

package org.nognog.freeSquare.model.item;

/**
 * @author goshi 2015/01/15
 * @param <T1>
 *            item class
 * @param <T2>
 *            type in T1
 */
public abstract class AbstractItem<T1 extends AbstractItem<T1, T2>, T2 extends TypeInItem<T1, T2>> implements Item<T1, T2> {

	private T2 type;

	/**
	 * @param type
	 */
	public AbstractItem(T2 type) {
		this.type = type;
	}

	/**
	 * @return type in the item
	 */
	@Override
	public T2 getTypeInItem() {
		return this.type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSameItem(Object obj) {
		if (obj.getClass() == this.getClass()) {
			return this.isSameItem((T1) obj);
		}
		return false;
	}

}

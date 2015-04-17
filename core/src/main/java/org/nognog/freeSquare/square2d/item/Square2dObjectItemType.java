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

package org.nognog.freeSquare.square2d.item;

import org.nognog.freeSquare.model.item.TypeInItem;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectType;

/**
 * Adapter to TypeInItem　from Square2dObjectType
 * 
 * @author goshi 2015/01/16
 */
public class Square2dObjectItemType implements TypeInItem<Square2dObjectItem, Square2dObjectItemType> {

	private Square2dObjectType<?> type;

	@SuppressWarnings("unused")
	private Square2dObjectItemType() {
		// used by json
	}

	/**
	 * @param type
	 */
	Square2dObjectItemType(Square2dObjectType<?> type) {
		this.type = type;
	}

	@Override
	public boolean isSameTypeTo(Square2dObjectItem obj) {
		return this.equals(obj.getTypeInItem());
	}

	/**
	 * @param obj
	 * @return true if both object indicate same item-type
	 */
	@Override
	public boolean equals(Square2dObjectItemType obj) {
		return this.type == obj.type;
	}

	@Override
	public String toString() {
		return this.type.getName();
	}

	/**
	 * @return square2d-object-type
	 */
	public Square2dObjectType<?> getSquare2dObjectType() {
		return this.type;
	}
}

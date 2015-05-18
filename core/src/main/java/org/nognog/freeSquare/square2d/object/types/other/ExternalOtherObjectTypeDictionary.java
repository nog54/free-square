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

package org.nognog.freeSquare.square2d.object.types.other;

import org.nognog.freeSquare.square2d.object.types.ExternalSquare2dObjectTypeDictionary;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/05/07
 */
public class ExternalOtherObjectTypeDictionary extends ExternalSquare2dObjectTypeDictionary<ExternalOtherObjectType> {

	@Override
	public boolean isValid() {
		return this.hasDuplicateNameType() == false;
	}

	/**
	 * @return
	 */
	private boolean hasDuplicateNameType() {
		for (ExternalOtherObjectType type : this.getAllExternalObjectType()) {
			final int countTypeName = this.countName(type.getName());
			if (countTypeName >= 2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param name
	 * @return
	 */
	private int countName(String name) {
		int result = 0;
		for (ExternalOtherObjectType type : this.getAllExternalObjectType()) {
			if (type.getName().equals(name)) {
				result++;
			}
		}
		return result;
	}

	@Override
	protected void fixDictionaryToSavableState() {
		while (this.hasDuplicateNameType()) {
			Array<ExternalOtherObjectType> types = this.getAllExternalObjectType();
			for (int i = 0; i < types.size - 1; i++) {
				final ExternalOtherObjectType type = types.get(i);
				final int typeNameCount = this.countName(type.getName());
				if (typeNameCount < 2) {
					continue;
				}
				for (int j = i + 1; j < types.size; j++) {
					final ExternalOtherObjectType compareType = types.get(j);
					if (type.getName().equals(compareType.getName())) {
						String newName = this.addNumberingString(type.getName());
						compareType.setName(newName);
					}
				}
			}
		}
	}

	/**
	 * @param name
	 * @return
	 */
	private String addNumberingString(String name) {
		final StringBuilder sb = new StringBuilder();
		sb.append(name);
		for (int number = 1; number < Integer.MAX_VALUE; number++) {
			sb.append(number);
			final String resultCandidate = sb.toString();
			if (this.isAlreadyExistsName(resultCandidate) == false) {
				return resultCandidate;
			}
			final int numberDigits = (int) (Math.log10(number)) + 1;
			sb.delete(sb.length() - numberDigits, sb.length());
		}
		throw new RuntimeException("Failed to add numbering"); //$NON-NLS-1$
	}
}

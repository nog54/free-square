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

package org.nognog.freeSquare;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * @author goshi 2015/01/14
 */
public class Messages {
	private Messages() {
	}

	private static I18NBundle messages;
	static {
		FileHandle baseFileHandle = Gdx.files.internal(Resources.messagePath);
		messages = I18NBundle.createBundle(baseFileHandle, Locale.getDefault());
	}

	/**
	 * @param key
	 * @return string
	 */
	public static String getString(String key) {
		return messages.get(key);
	}

	/**
	 * @param key
	 * @return string
	 */
	public static String getString(String... key) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < key.length; i++) {
			sb.append(messages.get(key[i]));
		}
		return sb.toString();
	}
}

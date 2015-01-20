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

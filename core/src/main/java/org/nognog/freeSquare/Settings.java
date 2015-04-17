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

import com.badlogic.gdx.Gdx;

/**
 * @author goshi
 *
 */
public class Settings {
	private Settings() {

	}

	static {
		initialize();
	}
	private static int physicalDisplayWidth;
	private static int physicalDisplayHeight;
	private static float aspectRatio;
	private static int defaultLogicalCameraWidth;
	private static int defaultLogicalCameraHeight;
	private static float playerNameMaxTextDrawWidth;
	private static float objectNameMaxTextDrawWidth;

	private static final float goldenRatio = 1.6180339887f;

	/**
	 * 
	 */
	public static void initialize() {
		physicalDisplayWidth = Gdx.graphics.getWidth();
		physicalDisplayHeight = Gdx.graphics.getHeight();
		aspectRatio = (float) physicalDisplayHeight / physicalDisplayWidth;
		defaultLogicalCameraWidth = 1024;
		defaultLogicalCameraHeight = (int) (defaultLogicalCameraWidth * aspectRatio);
		playerNameMaxTextDrawWidth = defaultLogicalCameraWidth / goldenRatio;
		objectNameMaxTextDrawWidth = defaultLogicalCameraWidth / goldenRatio;
	}

	/**
	 * @return physical display width when {@link Settings#initialize()} is
	 *         called
	 */
	public static int getPhysicalDisplayWidth() {
		return physicalDisplayWidth;
	}

	/**
	 * @return physical display height when {@link Settings#initialize()} is
	 *         called
	 */
	public static int getPhysicalDisplayHeight() {
		return physicalDisplayHeight;
	}

	/**
	 * @return aspect ratio when {@link Settings#initialize()} is called
	 */
	public static float getAspectRatio() {
		return aspectRatio;
	}

	/**
	 * @return logical camera width when {@link Settings#initialize()} is called
	 */
	public static int getDefaultLogicalCameraWidth() {
		return defaultLogicalCameraWidth;
	}

	/**
	 * @return logical camera height when {@link Settings#initialize()} is
	 *         called
	 */
	public static int getDefaultLogicalCameraHeight() {
		return defaultLogicalCameraHeight;
	}

	/**
	 * @return max width of player name
	 */
	public static float getPlayerNameMaxTextDrawWidth() {
		return playerNameMaxTextDrawWidth;
	}

	/**
	 * @return max width of object name
	 */
	public static float getObjectNameMaxTextDrawWidth() {
		return objectNameMaxTextDrawWidth;
	}

	/**
	 * @return golden ratio
	 */
	public static float getGoldenRatio() {
		return goldenRatio;
	}

	/**
	 * @return font size
	 */
	public static int getFontSize() {
		return defaultLogicalCameraWidth / 18;
	}
}

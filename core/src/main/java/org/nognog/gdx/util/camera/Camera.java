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

package org.nognog.gdx.util.camera;

/**
 * @author goshi 2015/06/09
 */
public interface Camera {

	/**
	 * @return camera position x
	 */
	float getX();

	/**
	 * @return camera position y
	 */
	float getY();

	/**
	 * @param x
	 */
	void setX(float x);

	/**
	 * @param y
	 */
	void setY(float y);

	/**
	 * set x and y
	 * 
	 * @param x
	 * @param y
	 */
	void setPosition(float x, float y);

	/**
	 * @return current camera zoom
	 */
	float getZoom();

	/**
	 * @param x
	 * @param y
	 */
	void move(float x, float y);

	/**
	 * @param x
	 */
	void zoom(float x);

	/**
	 * @param x
	 */
	void setZoom(float x);

	/**
	 * @return width of viewport
	 */
	float getViewportWidth();

	/**
	 * @return height of viewport
	 */
	float getViewportHeight();
}
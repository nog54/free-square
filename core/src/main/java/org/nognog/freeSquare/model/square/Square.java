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

package org.nognog.freeSquare.model.square;

import org.nognog.freeSquare.model.Nameable;

/**
 * @author goshi 2014/12/17
 * @param <T>
 */
public interface Square<T extends SquareObject<?>> extends Nameable {

	/**
	 * @param object
	 */
	void addSquareObject(T object);

	/**
	 * @param object
	 * @return true if object is removed
	 */
	boolean removeSquareObject(T object);

	/**
	 * @return square objects
	 */
	T[] getObjects();

	/**
	 * @param observer
	 * 
	 */
	void addSquareObserver(SquareEventListener observer);

	/**
	 * @param observer
	 */
	void removeSquareObserver(SquareEventListener observer);

	/**
	 * @param event
	 */
	void notifyEventListeners(SquareEvent event);

}

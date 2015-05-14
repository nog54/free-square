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

package org.nognog.freeSquare.persist;

/**
 * @author goshi 2014/11/01
 */
public class SaveFailureException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SaveFailureException() {
	}

	SaveFailureException(Throwable t) {
		super(t);
	}

	SaveFailureException(String string) {
		super(string);
	}

}

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

package org.nognog.freeSquare.activity;

import java.lang.reflect.InvocationTargetException;

import org.nognog.freeSquare.FreeSquare;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author goshi 2015/04/14
 */
public class FreeSquareActivityFactory extends FlyweightFactory<FreeSquareActivity> {
	private final FreeSquare freeSquare;

	/**
	 * @param freeSquare
	 */
	public FreeSquareActivityFactory(FreeSquare freeSquare) {
		this.freeSquare = freeSquare;
	}

	/**
	 * @return MainActivity
	 */
	public MainActivity getMainActivity() {
		return getAndCache(MainActivity.class);
	}

	/**
	 * @return InitializeActivity
	 */
	public InitializeActivity getInitializeActivity() {
		return getAndCache(InitializeActivity.class);
	}

	private <T extends FreeSquareActivity> T getAndCache(Class<T> activityClass) {
		final T cacheInstance = this.get(activityClass);
		if (cacheInstance != null) {
			return cacheInstance;
		}
		T activity = null;
		try {
			activity = activityClass.getConstructor(FreeSquare.class).newInstance(this.freeSquare);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
		this.cache(activity);
		return activity;
	}

}

abstract class FlyweightFactory<T1> {
	private final ObjectMap<Class<? extends T1>, T1> pool;

	FlyweightFactory() {
		this.pool = new ObjectMap<>();
	}

	@SuppressWarnings("unchecked")
	<T2 extends T1> T2 get(Class<T2> klass) {
		return (T2) this.pool.get(klass);
	}

	@SuppressWarnings("unchecked")
	void cache(T1 instance) {
		this.pool.put((Class<? extends T1>) instance.getClass(), instance);
	}
}
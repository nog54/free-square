package org.nognog.freeSquare.activity;

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
		final MainActivity cacheInstance = this.get(MainActivity.class);
		if (cacheInstance != null) {
			return cacheInstance;
		}
		final MainActivity activity = new MainActivity(this.freeSquare);
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
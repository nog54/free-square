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

package org.nognog.freeSquare.square2d.action.object;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import mockit.NonStrictExpectations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.GdxTestRunner;
import org.nognog.freeSquare.model.life.status.Status;
import org.nognog.freeSquare.square2d.action.Square2dActionUtlls;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;
import org.nognog.freeSquare.square2d.object.types.life.LifeObjectType;
import org.nognog.freeSquare.square2d.object.types.life.LifeObjectTypeManager;

/**
 * @author goshi 2015/07/02
 */
@RunWith(GdxTestRunner.class)
public class ConstantlyTireActionTest {

	private static final int secondsPerMinute = 60;

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.action.object.ConstantlyTireAction#act(float)}
	 * .
	 */
	@SuppressWarnings("all")
	@Test
	public final void testActInSleepingLifeObject() {
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			final LifeObject object = type.create();
			final double initialTiredness = 0;
			object.getLife().getStatus().addTiredness(initialTiredness);
			final float tirednessIncreaseAmountPerMinute = 1 / 256f;
			final ConstantlyTireAction action = Square2dActionUtlls.constantlyTired(tirednessIncreaseAmountPerMinute);
			action.setActor(object);
			final float delta = 0.25f;
			new NonStrictExpectations(object) {
				{
					object.isSleeping();
					result = true;
				}
			};

			for (int i = 0; i < secondsPerMinute / delta; i++) {
				action.act(delta);
				assertThat(object.getLife().getStatus().getTiredness(), is(initialTiredness));
			}

			final float bigDelta = 20000; // not
			action.act(bigDelta);
			assertThat(object.getLife().getStatus().getTiredness(), is(initialTiredness));
		}
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.action.object.ConstantlyTireAction#act(float)}
	 * .
	 */
	@SuppressWarnings("all")
	@Test
	public final void testActInWakingLifeObject() {
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			final LifeObject object = type.create();
			final double initialTiredness = 0;
			object.getLife().getStatus().addTiredness(initialTiredness);
			final double tirednessIncreaseAmountPerMinute = 1 / 256.0;
			final ConstantlyTireAction action = Square2dActionUtlls.constantlyTired((float) tirednessIncreaseAmountPerMinute);
			action.setActor(object);
			final float delta = 0.25f;
			new NonStrictExpectations(object) {
				{
					object.isSleeping();
					result = false;
				}
			};

			for (int i = 0; i < secondsPerMinute / delta - 1; i++) {
				action.act(delta);
				assertThat(object.getLife().getStatus().getTiredness(), is(initialTiredness));
			}

			action.act(delta);
			final double expected1 = initialTiredness + tirednessIncreaseAmountPerMinute;
			assertThat(object.getLife().getStatus().getTiredness(), is(expected1));

			final float bigDelta = 20000; // not
			action.act(bigDelta);
			final double expected2 = expected1 + (int) (bigDelta / secondsPerMinute) * tirednessIncreaseAmountPerMinute;
			assertThat(object.getLife().getStatus().getTiredness(), is(expected2));
			
			final float hugeDelta = Float.MAX_VALUE;
			action.act(hugeDelta);
			final double expected3 = Status.StatusRange.TIREDNESS.getMax();
			assertThat(object.getLife().getStatus().getTiredness(), is(expected3));
		}
	}

}

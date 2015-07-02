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
 * @author goshi 2015/07/01
 */
@RunWith(GdxTestRunner.class)
public class SleepActionTest {

	private static final int secondsPerMinute = 60;

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.action.object.SleepAction#act(float)}
	 * .
	 */
	@SuppressWarnings("all")
	@Test
	public final void testActWithFixTimePolicy() {
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			final LifeObject object = type.create();
			final double initialTiredness = Status.StatusRange.TIREDNESS.getMax();
			object.getLife().getStatus().addTiredness(initialTiredness);
			final float sleepTime = 150;
			final SleepAction action = Square2dActionUtlls.sleep(sleepTime);
			action.setActor(object);
			final float delta = 1.5f;

			new NonStrictExpectations(object) {
				{
					object.isSleeping();
					result = false;
				}
			};
			assertThat(action.act(delta), is(true));
			new NonStrictExpectations(object) {
				{
					object.isSleeping();
					result = true;
				}
			};

			for (int i = 0; i < sleepTime / delta - 1; i++) {
				final boolean finishAction = action.act(delta);
				assertThat(finishAction, is(false));
				final float expectedTotalSleepTime = (i + 1) * delta;
				final float expectedTimeToNextInfluence = secondsPerMinute - expectedTotalSleepTime % secondsPerMinute;
				final double expectedTiredness = initialTiredness - SleepAction.tirednessRecoveryAmountPerMinute * ((int) expectedTotalSleepTime / 60);

				assertThat(action.getTotalSleepedTime(), is(expectedTotalSleepTime));
				assertThat(action.getDesiredSleepTime(), is(sleepTime));
				assertThat(action.getTimeToNextInfluence(), is(expectedTimeToNextInfluence));
				assertThat(object.getLife().getStatus().getTiredness(), is(expectedTiredness));
			}
			assertThat(action.act(delta), is(true));
		}
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.action.object.SleepAction#act(float)}
	 * .
	 */
	@SuppressWarnings("all")
	@Test
	public final void testActWithCompleteRecoveryPolicy() {
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			final LifeObject object = type.create();
			final double initialTiredness = Status.StatusRange.TIREDNESS.getMax();
			object.getLife().getStatus().addTiredness(initialTiredness);
			final SleepAction action = Square2dActionUtlls.sleepUntilCompleteRecovery();
			action.setActor(object);
			final float delta = 1.25f;

			assertThat(action.act(delta), is(true));
			new NonStrictExpectations(object) {
				{
					object.isSleeping();
					result = true;
				}
			};

			// In the following calculations, numerical error may occurs if recovery point is modified.
			
			final float completeTime = (float) (initialTiredness / SleepAction.tirednessRecoveryAmountPerMinute * secondsPerMinute);

			final boolean finishAction1 = action.act(completeTime / 2);
			assertThat(finishAction1, is(false));
			assertThat(object.getLife().getStatus().getTiredness(), is(Status.StatusRange.TIREDNESS.getMax() / 2));
			final boolean finishAction2 = action.act(completeTime / 4);
			assertThat(finishAction2, is(false));
			assertThat(object.getLife().getStatus().getTiredness(), is(Status.StatusRange.TIREDNESS.getMax() / 4));
			final boolean finishAction3 = action.act(completeTime / 4);
			assertThat(finishAction3, is(true));
			assertThat(object.getLife().getStatus().getTiredness(), is(0.0));
		}
	}

}

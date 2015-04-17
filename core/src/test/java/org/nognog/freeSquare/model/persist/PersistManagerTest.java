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

package org.nognog.freeSquare.model.persist;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.model.player.PlayLog;
import org.nognog.freeSquare.model.player.Player;

@SuppressWarnings({ "javadoc", "static-method", "boxing" })
@RunWith(GdxTestRunner.class)
public class PersistManagerTest {

	@Test
	public final void testSavePersistItemOfTT() throws SaveFailureException, LoadFailureException {
		boolean existedPlayLog = PersistManager.isAlreadyPersisted(PersistItems.PLAY_LOG);
		if (!existedPlayLog) {
			PlayLog log = PlayLog.create();
			log.update("This is created for PersistManager test."); //$NON-NLS-1$
		}
		try {
			PersistManager.save(PersistItems.TEST_ITEM, new Player(null));
			fail();
		} catch (SaveFailureException e) {
			assertThat(e.getMessage(), is("save object is invalid.")); //$NON-NLS-1$
		}
		try {
			PersistManager.load(PersistItems.TEST_ITEM);
			fail();
		} catch (LoadFailureException e) {
			assertThat(e.getMessage(), is("load item is not persisted.")); //$NON-NLS-1$
		}
		assertThat(PersistManager.isAlreadyPersisted(PersistItems.TEST_ITEM), is(false));
		assertThat(PersistManager.delete(PersistItems.TEST_ITEM), is(false));

		PersistManager.save(PersistItems.TEST_ITEM, new Player("test_player")); //$NON-NLS-1$
		Player revivalItem = PersistManager.load(PersistItems.TEST_ITEM);
		assertThat(revivalItem, is(not(nullValue())));
		assertThat(PersistManager.isAlreadyPersisted(PersistItems.TEST_ITEM), is(true));
		assertThat(PersistManager.delete(PersistItems.TEST_ITEM), is(true));
		if (!existedPlayLog) {
			PersistManager.delete(PersistItems.PLAY_LOG);
		}

	}

}

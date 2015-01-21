package org.nognog.freeSquare.model.persist;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.life.family.ShibaInu;
import org.nognog.freeSquare.model.player.PlayLog;

@SuppressWarnings({ "javadoc", "static-method", "boxing" })
@RunWith(GdxTestRunner.class)
public class PersistManagerTest {

	@Test
	public final void testSavePersistItemOfTT() throws SaveFailureException, LoadFailureException {
		boolean existedPlayLog = PersistManager.isAlreadyPersisted(PersistItem.PLAY_LOG);
		if (!existedPlayLog) {
			PlayLog log = PlayLog.create();
			log.update("This is created for PersistManager test."); //$NON-NLS-1$
		}
		try {
			PersistManager.save(PersistItem.TEST_ITEM, new Life(null));
			fail();
		} catch (SaveFailureException e) {
			assertThat(e.getMessage(), is("save object is invalid.")); //$NON-NLS-1$
		}
		try {
			PersistManager.load(PersistItem.TEST_ITEM);
			fail();
		} catch (LoadFailureException e) {
			assertThat(e.getMessage(), is("load item is not persisted.")); //$NON-NLS-1$
		}
		assertThat(PersistManager.isAlreadyPersisted(PersistItem.TEST_ITEM), is(false));
		assertThat(PersistManager.delete(PersistItem.TEST_ITEM), is(false));

		PersistManager.save(PersistItem.TEST_ITEM, new Life(new ShibaInu()));
		Life revivalItem = PersistManager.load(PersistItem.TEST_ITEM);
		assertThat(revivalItem, is(not(nullValue())));
		assertThat(PersistManager.isAlreadyPersisted(PersistItem.TEST_ITEM), is(true));
		assertThat(PersistManager.delete(PersistItem.TEST_ITEM), is(true));
		if (!existedPlayLog) {
			PersistManager.delete(PersistItem.PLAY_LOG);
		}

	}

}

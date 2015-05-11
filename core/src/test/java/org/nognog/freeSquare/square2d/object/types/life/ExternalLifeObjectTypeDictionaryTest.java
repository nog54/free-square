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

package org.nognog.freeSquare.square2d.object.types.life;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;

/**
 * @author goshi 2015/05/10
 */
@SuppressWarnings({ "boxing", "static-method" })
@RunWith(GdxTestRunner.class)
public class ExternalLifeObjectTypeDictionaryTest {

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectTypeDictionary#ExternalLifeObjectTypeDictionary()}
	 * .
	 */

	@Test
	public final void testExternalLifeObjectTypeDictionary() {
		final ExternalLifeObjectTypeDictionary dictionary = new ExternalLifeObjectTypeDictionary();
		ExternalLifeObjectType[] types = dictionary.getAllExternalObjectType().toArray(ExternalLifeObjectType.class);
		assertThat(types.length, is(0));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectTypeDictionary#isValid()}
	 * .
	 */
	@Test
	public final void testIsValid() {
		final ExternalLifeObjectTypeDictionary dictionary = new ExternalLifeObjectTypeDictionary();
		assertThat(dictionary.isValid(), is(true));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectTypeDictionary#addExternalLifeObjectType(org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectType)}
	 * .
	 */
	@Test
	public final void testAddExternalLifeObjectType() {
		final ExternalLifeObjectTypeDictionary dictionary = new ExternalLifeObjectTypeDictionary();

		final ExternalLifeObjectType family1MockType1 = this.createTypeMock("family1"); //$NON-NLS-1$
		dictionary.addExternalObjectType(family1MockType1);
		ExternalLifeObjectType[] types = dictionary.getAllExternalObjectType().toArray(ExternalLifeObjectType.class);
		assertThat(types.length, is(1));
		assertThat(types[0], is(family1MockType1));

		final ExternalLifeObjectType family1MockType2 = this.createTypeMock("family1"); //$NON-NLS-1$
		dictionary.addExternalObjectType(family1MockType1);
		dictionary.addExternalObjectType(family1MockType2);
		types = dictionary.getAllExternalObjectType().toArray(ExternalLifeObjectType.class);
		assertThat(types.length, is(1));
		assertThat(types[0], is(family1MockType1));

		final ExternalLifeObjectType family2MockType = this.createTypeMock("family2"); //$NON-NLS-1$
		dictionary.addExternalObjectType(family2MockType);
		types = dictionary.getAllExternalObjectType().toArray(ExternalLifeObjectType.class);
		assertThat(types.length, is(2));
		assertThat(types[0], is(family1MockType1));
		assertThat(types[1], is(family2MockType));
		
		dictionary.clear();
		types = dictionary.getAllExternalObjectType().toArray(ExternalLifeObjectType.class);
		assertThat(types.length, is(0));
	}

	private ExternalLifeObjectType createTypeMock(String familyName) {
		final ExternalLifeObjectType typeMock = mock(ExternalLifeObjectType.class);
		when(typeMock.getName()).thenReturn(familyName);
		return typeMock;
	}

}

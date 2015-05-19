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

package org.nognog.freeSquare.square2d.object.types.other;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

/**
 * @author goshi 2015/05/10
 */
@SuppressWarnings({ "boxing", "static-method" })
public class ExternalOtherObjectTypeDictionaryTest {

	@Mocked("(String, String)")
	private ExternalOtherObjectType mock;

	/**
	 * setup
	 */
	@Before
	public void setup() {
		//		this.family1MockType1.setName("family1"); //$NON-NLS-1$
		//		this.family1MockType2.setName("family1"); //$NON-NLS-1$
		//		this.family2MockType.setName("family2"); //$NON-NLS-1$
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectTypeDictionary#ExternalOtherObjectTypeDictionary()}
	 * .
	 */
	@Test
	public final void testExternalOtherObjectTypeDictionary() {
		final ExternalOtherObjectTypeDictionary dictionary = new ExternalOtherObjectTypeDictionary();
		OtherObjectType[] types = dictionary.getAllExternalObjectType().toArray(OtherObjectType.class);
		assertThat(types.length, is(0));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectTypeDictionary#isValid()}
	 * .
	 */
	@Test
	public final void testIsValid() {
		final ExternalOtherObjectTypeDictionary dictionary = new ExternalOtherObjectTypeDictionary();
		assertThat(dictionary.isValid(), is(true));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectTypeDictionary#addExternalOtherObjectType(org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectType)}
	 * .
	 */
	@Test
	public final void testAddExternalObjectType() {
		final ExternalOtherObjectTypeDictionary dictionary = new ExternalOtherObjectTypeDictionary();
		final ExternalOtherObjectType mockType1 = new ExternalOtherObjectType(null, null);
		final ExternalOtherObjectType mockType2 = new ExternalOtherObjectType(null, null);
		final ExternalOtherObjectType mockType3 = new ExternalOtherObjectType(null, null);
		mockType1.setName("family1"); //$NON-NLS-1$
		mockType2.setName("family1"); //$NON-NLS-1$
		mockType3.setName("family2"); //$NON-NLS-1$

		dictionary.addExternalObjectType(mockType1);
		ExternalOtherObjectType[] types = dictionary.getAllExternalObjectType().toArray(ExternalOtherObjectType.class);
		assertThat(types.length, is(1));
		assertThat(types[0], is(mockType1));

		dictionary.addExternalObjectType(mockType1);
		dictionary.addExternalObjectType(mockType2);
		types = dictionary.getAllExternalObjectType().toArray(ExternalOtherObjectType.class);
		assertThat(types.length, is(1));
		assertThat(types[0], is(mockType1));

		dictionary.addExternalObjectType(mockType3);
		types = dictionary.getAllExternalObjectType().toArray(ExternalOtherObjectType.class);
		assertThat(types.length, is(2));
		assertThat(types[0], is(mockType1));
		assertThat(types[1], is(mockType3));

		dictionary.clear();
		types = dictionary.getAllExternalObjectType().toArray(ExternalOtherObjectType.class);
		assertThat(types.length, is(0));
	}

	@SuppressWarnings({ "javadoc" })
	@Test
	public void testFixDictionary() {
		final ExternalOtherObjectTypeDictionary dictionary = new ExternalOtherObjectTypeDictionary();
		final ExternalOtherObjectType[] mockTypes = new ExternalOtherObjectType[105];
		for (int i = 0; i < mockTypes.length; i++) {
			mockTypes[i] = new ExternalOtherObjectType(null, null);
			mockTypes[i].setName("name" + i); //$NON-NLS-1$
			dictionary.addExternalObjectType(mockTypes[i]);
		}

		assertThat(dictionary.getAllExternalObjectType().size, is(mockTypes.length));

		for (int i = 1; i < mockTypes.length; i++) {
			mockTypes[i].setName("name0"); //$NON-NLS-1$
		}

		dictionary.fixDictionaryToSavableState();
		
		assertThat(dictionary.getAllExternalObjectType().size, is(mockTypes.length));
		assertThat(mockTypes[0].getName(), is("name0")); //$NON-NLS-1$
		for (int i = 1; i < mockTypes.length; i++) {
			assertThat(mockTypes[i].getName(), is("name0" + i)); //$NON-NLS-1$
		}
		
		final ExternalOtherObjectType mockType105 = new ExternalOtherObjectType(null, null);
		final ExternalOtherObjectType mockType106 = new ExternalOtherObjectType(null, null);
		mockType105.setName("knuth"); //$NON-NLS-1$
		mockType106.setName("dekker"); //$NON-NLS-1$
		
		dictionary.addExternalObjectType(mockType105);
		dictionary.addExternalObjectType(mockType106);
		assertThat(dictionary.getAllExternalObjectType().size, is(mockTypes.length + 2));
		
		mockType105.setName("name00"); //$NON-NLS-1$
		mockType106.setName("name0"); //$NON-NLS-1$
		
		dictionary.fixDictionaryToSavableState();
		
		assertThat(mockType105.getName(), is("name00")); //$NON-NLS-1$
		assertThat(mockType106.getName(), is("name0" + (mockTypes.length))); //$NON-NLS-1$
	}
}

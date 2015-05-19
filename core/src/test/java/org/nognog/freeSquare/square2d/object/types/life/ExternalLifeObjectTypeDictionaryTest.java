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

import java.util.Random;

import mockit.Mocked;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.square2d.object.LifeObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

/**
 * @author goshi 2015/05/10
 */
@SuppressWarnings({ "boxing", "static-method" })
@RunWith(GdxTestRunner.class)
public class ExternalLifeObjectTypeDictionaryTest {

	@Mocked("(String)")
	private Texture texture;

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
		final ExternalLifeObjectType mockType1 = new ExternalLifeObjectType("family1", null, 0, 0, LifeObject.class); //$NON-NLS-1$
		final ExternalLifeObjectType mockType2 = new ExternalLifeObjectType("family1", null, 0, 0, LifeObject.class); //$NON-NLS-1$
		final ExternalLifeObjectType mockType3 = new ExternalLifeObjectType("family2", null, 0, 0, LifeObject.class); //$NON-NLS-1$

		dictionary.addExternalObjectType(mockType1);
		ExternalLifeObjectType[] types = dictionary.getAllExternalObjectType().toArray(ExternalLifeObjectType.class);
		assertThat(types.length, is(1));
		assertThat(types[0], is(mockType1));

		dictionary.addExternalObjectType(mockType1);
		dictionary.addExternalObjectType(mockType2);
		types = dictionary.getAllExternalObjectType().toArray(ExternalLifeObjectType.class);
		assertThat(types.length, is(1));
		assertThat(types[0], is(mockType1));

		dictionary.addExternalObjectType(mockType3);
		types = dictionary.getAllExternalObjectType().toArray(ExternalLifeObjectType.class);
		assertThat(types.length, is(2));
		assertThat(types[0], is(mockType1));
		assertThat(types[1], is(mockType3));

		dictionary.clear();
		types = dictionary.getAllExternalObjectType().toArray(ExternalLifeObjectType.class);
		assertThat(types.length, is(0));
	}

	@SuppressWarnings({ "javadoc" })
	@Test
	public void testFixDictionary() {
		final ExternalLifeObjectTypeDictionary dictionary = new ExternalLifeObjectTypeDictionary();
		final ExternalLifeObjectType[] mockTypes = new ExternalLifeObjectType[105];
		for (int i = 0; i < mockTypes.length; i++) {
			mockTypes[i] = new ExternalLifeObjectType("name" + i, null, 0, 0, LifeObject.class); //$NON-NLS-1$
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

		final ExternalLifeObjectType mockType105 = new ExternalLifeObjectType("knuth", null, 0, 0, LifeObject.class); //$NON-NLS-1$
		final ExternalLifeObjectType mockType106 = new ExternalLifeObjectType("dekker", null, 0, 0, LifeObject.class); //$NON-NLS-1$

		dictionary.addExternalObjectType(mockType105);
		dictionary.addExternalObjectType(mockType106);
		assertThat(dictionary.getAllExternalObjectType().size, is(mockTypes.length + 2));

		mockType105.setName("name00"); //$NON-NLS-1$
		mockType106.setName("name0"); //$NON-NLS-1$

		dictionary.fixDictionaryToSavableState();

		assertThat(mockType105.getName(), is("name00")); //$NON-NLS-1$
		assertThat(mockType106.getName(), is("name0" + (mockTypes.length))); //$NON-NLS-1$
	}

	/**
	 * test read and write method
	 */
	@Test
	public void testReadWrite() {
		final ExternalLifeObjectTypeDictionary originDictionary = new ExternalLifeObjectTypeDictionary();
		final ExternalLifeObjectType[] mockTypes = new ExternalLifeObjectType[200];
		final Random random = new Random();
		for (int i = 0; i < mockTypes.length; i++) {
			mockTypes[i] = new ExternalLifeObjectType("name" + random.nextInt(200), null, 0, 0, LifeObject.class); //$NON-NLS-1$
			originDictionary.addExternalObjectType(mockTypes[i]);
		}
		Json json = new Json();
		final String jsonString = json.toJson(originDictionary);
		final ExternalLifeObjectTypeDictionary readDictionary = json.fromJson(ExternalLifeObjectTypeDictionary.class, jsonString);

		final Array<ExternalLifeObjectType> originDictionaryTypes = originDictionary.getAllExternalObjectType();
		final Array<ExternalLifeObjectType> readDictionaryTypes = readDictionary.getAllExternalObjectType();
		assertThat(readDictionaryTypes.size, is(originDictionaryTypes.size));
		for (int i = 0; i < originDictionaryTypes.size; i++) {
			final ExternalLifeObjectType readDictionaryType = readDictionaryTypes.get(i);
			final ExternalLifeObjectType originDictionaryType = originDictionaryTypes.get(i);
			assertThat(readDictionaryType.getName(), is(originDictionaryType.getName()));
		}
	}
}

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

package org.nognog.freeSquare.model.player;

import static org.exparity.hamcrest.date.DateMatchers.sameOrAfter;
import static org.exparity.hamcrest.date.DateMatchers.sameOrBefore;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

/**
 * @author goshi 2014/10/28
 */
@SuppressWarnings({ "static-method" })
public class PlayerTest {

	/**
	 * test of getName
	 */
	@Test
	public void testGetName() {
		String playerName = "tester"; //$NON-NLS-1$
		Player player1 = new Player(playerName);
		String actual1 = player1.getName();
		String expected1 = playerName;
		assertThat(actual1, is(expected1));
	}

	/**
	 * test of getDate
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testGetStartDate() {
		Date date1 = new Date();
		String playerName = "tester"; //$NON-NLS-1$
		Player player = new Player(playerName);
		Date date2 = new Date();
		Date actual1 = player.getStartDate();
		Date date3 = new Date();

		assertThat(actual1, is(sameOrAfter(date1)));
		assertThat(actual1, is(sameOrBefore(date2)));
		assertThat(actual1, is(sameOrBefore(date3)));

		Date actual2 = player.getStartDate();
		actual2.setYear(actual2.getYear() + 1);
		Date actual3 = player.getStartDate();

		assertThat(actual3, is(not(sameInstance(actual1)))); // 以下でクローンの確認
		assertThat(actual3, is(actual1));
		assertThat(actual3, is(not(actual2)));
	}

}

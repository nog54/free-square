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

package org.nognog.freeSquare.model.life;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.nognog.freeSquare.model.life.status.Status;

/**
 * @author goshi 2014/10/04
 */
@SuppressWarnings({ "boxing", "static-method" })
public class StatusTest {

	/**
	 * test of getAge
	 */
	@Test
	public void testGetAge() {
		final Status status = new Status();
		final int actual = status.getAge();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addAge
	 */
	@Test
	public void testAddAge() {
		final int maxAge = 1000;
		final Status status = new Status();
		for (int i = 1; i <= maxAge; i++) {
			status.addAge();
			final int actual = status.getAge();
			final int expected = i;
			assertThat(actual, is(expected));
		}
		status.addAge();
		final int actual = status.getAge();
		final int expected = maxAge;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addAge
	 */
	@Test
	public void testAddAgeInt() {
		final Status status = new Status();

		final int actual1 = status.getAge();
		final int expected1 = 0;
		assertThat(actual1, is(expected1));

		final int addend1 = 100;
		status.addAge(addend1);
		final int actual2 = status.getAge();
		final int expected2 = 100;
		assertThat(actual2, is(expected2));

		final int addend2 = 777;
		status.addAge(addend2);
		final int actual3 = status.getAge();
		final int expected3 = addend1 + addend2; // must be less than 1000
		assertThat(actual3, is(expected3));

		final int addend3 = 65536;
		status.addAge(addend3);
		final int actual4 = status.getAge();
		final int expected4 = 1000;
		assertThat(actual4, is(expected4));

	}

	/**
	 * test of getCondition
	 */
	@Test
	public void testGetCondition() {
		final Status status = new Status();
		final double actual = status.getCondition();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addCondition
	 */
	@Test
	public void testAddCondition() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addCondition(addend1);
		final double actual1 = status.getCondition();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addCondition(addend2);
		final double actual2 = status.getCondition();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addCondition(addend3);
		final double actual3 = status.getCondition();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getHunger
	 */
	@Test
	public void testGetHunger() {
		final Status status = new Status();
		final double actual = status.getHunger();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addHunger
	 */
	@Test
	public void testAddHunger() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addHunger(addend1);
		final double actual1 = status.getHunger();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addHunger(addend2);
		final double actual2 = status.getHunger();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addHunger(addend3);
		final double actual3 = status.getHunger();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getPower
	 */
	@Test
	public void testGetPower() {
		final Status status = new Status();
		final double actual = status.getPower();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addPower
	 */
	@Test
	public void testAddPower() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addPower(addend1);
		final double actual1 = status.getPower();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addPower(addend2);
		final double actual2 = status.getPower();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addPower(addend3);
		final double actual3 = status.getPower();
		final double expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getAgility
	 */
	@Test
	public void testGetAgility() {
		final Status status = new Status();
		final double actual = status.getAgility();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addAgility
	 */
	@Test
	public void testAddAgility() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addAgility(addend1);
		final double actual1 = status.getAgility();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addAgility(addend2);
		final double actual2 = status.getAgility();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addAgility(addend3);
		final double actual3 = status.getAgility();
		final double expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getFlexibility
	 */
	@Test
	public void testGetFlexibility() {
		final Status status = new Status();
		final double actual = status.getFlexibility();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addFlexibility
	 */
	@Test
	public void testAddFlexibility() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addFlexibility(addend1);
		final double actual1 = status.getFlexibility();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addFlexibility(addend2);
		final double actual2 = status.getFlexibility();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addFlexibility(addend3);
		final double actual3 = status.getFlexibility();
		final double expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of get fortitude
	 */
	@Test
	public void testGetFortitude() {
		final Status status = new Status();
		final double actual = status.getFortitude();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addFortitude
	 */
	@Test
	public void testAddFortitude() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addFortitude(addend1);
		final double actual1 = status.getFortitude();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addFortitude(addend2);
		final double actual2 = status.getFortitude();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addFortitude(addend3);
		final double actual3 = status.getFortitude();
		final double expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getKnowledge
	 */
	@Test
	public void testGetKnowledge() {
		final Status status = new Status();
		final double actual = status.getKnowledge();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addKnowledge
	 */
	@Test
	public void testAddKnowledge() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addKnowledge(addend1);
		final double actual1 = status.getKnowledge();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addKnowledge(addend2);
		final double actual2 = status.getKnowledge();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addKnowledge(addend3);
		final double actual3 = status.getKnowledge();
		final double expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getSmart
	 */
	@Test
	public void testGetSmart() {
		final Status status = new Status();
		final double actual = status.getSmart();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addSmart
	 */
	@Test
	public void testAddSmart() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addSmart(addend1);
		final double actual1 = status.getSmart();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addSmart(addend2);
		final double actual2 = status.getSmart();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addSmart(addend3);
		final double actual3 = status.getSmart();
		final double expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getCuriosity
	 */
	@Test
	public void testGetCuriosity() {
		final Status status = new Status();
		final double actual = status.getCuriosity();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addCuriosity
	 */
	@Test
	public void testAddCuriosity() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addCuriosity(addend1);
		final double actual1 = status.getCuriosity();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addCuriosity(addend2);
		final double actual2 = status.getCuriosity();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addCuriosity(addend3);
		final double actual3 = status.getCuriosity();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getCommunication
	 */
	@Test
	public void testGetCommunication() {
		final Status status = new Status();
		final double actual = status.getCommunication();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addCommunication
	 */
	@Test
	public void testAddCommunication() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addCommunication(addend1);
		final double actual1 = status.getCommunication();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addCommunication(addend2);
		final double actual2 = status.getCommunication();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addCommunication(addend3);
		final double actual3 = status.getCommunication();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getTolerance
	 */
	@Test
	public void testGetTolerance() {
		final Status status = new Status();
		final double actual = status.getTolerance();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addTolerance
	 */
	@Test
	public void testAddTolerance() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addTolerance(addend1);
		final double actual1 = status.getTolerance();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addTolerance(addend2);
		final double actual2 = status.getTolerance();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addTolerance(addend3);
		final double actual3 = status.getTolerance();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getDefiance
	 */
	@Test
	public void testGetDefiance() {
		final Status status = new Status();
		final double actual = status.getDefiance();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addDefiance
	 */
	@Test
	public void testAddDefiance() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addDefiance(addend1);
		final double actual1 = status.getDefiance();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addDefiance(addend2);
		final double actual2 = status.getDefiance();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addDefiance(addend3);
		final double actual3 = status.getDefiance();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getCalm
	 */
	@Test
	public void testGetCalm() {
		final Status status = new Status();
		final double actual = status.getCalm();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addClam
	 */
	@Test
	public void testAddClam() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addClam(addend1);
		final double actual1 = status.getCalm();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addClam(addend2);
		final double actual2 = status.getCalm();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addClam(addend3);
		final double actual3 = status.getCalm();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getStress
	 */
	@Test
	public void testGetStress() {
		final Status status = new Status();
		final double actual = status.getStress();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addStress
	 */
	@Test
	public void testAddStress() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addStress(addend1);
		final double actual1 = status.getStress();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addStress(addend2);
		final double actual2 = status.getStress();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addStress(addend3);
		final double actual3 = status.getStress();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getMasculinity
	 */
	@Test
	public void testGetMasculinity() {
		final Status status = new Status();
		final double actual = status.getMasculinity();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addMasculinity
	 */
	@Test
	public void testAddMasculinity() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addMasculinity(addend1);
		final double actual1 = status.getMasculinity();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addMasculinity(addend2);
		final double actual2 = status.getMasculinity();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addMasculinity(addend3);
		final double actual3 = status.getMasculinity();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getEffeminacy
	 */
	@Test
	public void testGetEffeminacy() {
		final Status status = new Status();
		final double actual = status.getAge();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addEffeminacy
	 */
	@Test
	public void testAddEffeminacy() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addEffeminacy(addend1);
		final double actual1 = status.getEffeminacy();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addEffeminacy(addend2);
		final double actual2 = status.getEffeminacy();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addEffeminacy(addend3);
		final double actual3 = status.getEffeminacy();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getGift
	 */
	@Test
	public void testGetGift() {
		final Status status = new Status();
		final double actual = status.getGift();

		final double expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addGift
	 */
	@Test
	public void testAddGift() {
		final Status status = new Status();

		final double addend1 = 15;
		status.addGift(addend1);
		final double actual1 = status.getGift();
		final double expected1 = addend1;
		assertThat(actual1, is(expected1));

		final double addend2 = -30;
		status.addGift(addend2);
		final double actual2 = status.getGift();
		final double expected2 = 0;
		assertThat(actual2, is(expected2));

		final double addend3 = 50000;
		status.addGift(addend3);
		final double actual3 = status.getGift();
		final double expected3 = 100;
		assertThat(actual3, is(expected3));
	}

}

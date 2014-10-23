package org.nognog.freeSquare.model.life;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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
		final int actual = status.getCondition();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addCondition
	 */
	@Test
	public void testAddCondition() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addCondition(addend1);
		final int actual1 = status.getCondition();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addCondition(addend2);
		final int actual2 = status.getCondition();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addCondition(addend3);
		final int actual3 = status.getCondition();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getHunger
	 */
	@Test
	public void testGetHunger() {
		final Status status = new Status();
		final int actual = status.getHunger();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addHunger
	 */
	@Test
	public void testAddHunger() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addHunger(addend1);
		final int actual1 = status.getHunger();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addHunger(addend2);
		final int actual2 = status.getHunger();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addHunger(addend3);
		final int actual3 = status.getHunger();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getPower
	 */
	@Test
	public void testGetPower() {
		final Status status = new Status();
		final int actual = status.getPower();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addPower
	 */
	@Test
	public void testAddPower() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addPower(addend1);
		final int actual1 = status.getPower();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addPower(addend2);
		final int actual2 = status.getPower();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addPower(addend3);
		final int actual3 = status.getPower();
		final int expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getAgility
	 */
	@Test
	public void testGetAgility() {
		final Status status = new Status();
		final int actual = status.getAgility();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addAgility
	 */
	@Test
	public void testAddAgility() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addAgility(addend1);
		final int actual1 = status.getAgility();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addAgility(addend2);
		final int actual2 = status.getAgility();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addAgility(addend3);
		final int actual3 = status.getAgility();
		final int expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getFlexibility
	 */
	@Test
	public void testGetFlexibility() {
		final Status status = new Status();
		final int actual = status.getFlexibility();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addFlexibility
	 */
	@Test
	public void testAddFlexibility() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addFlexibility(addend1);
		final int actual1 = status.getFlexibility();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addFlexibility(addend2);
		final int actual2 = status.getFlexibility();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addFlexibility(addend3);
		final int actual3 = status.getFlexibility();
		final int expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of get fortitude
	 */
	@Test
	public void testGetFortitude() {
		final Status status = new Status();
		final int actual = status.getFortitude();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addFortitude
	 */
	@Test
	public void testAddFortitude() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addFortitude(addend1);
		final int actual1 = status.getFortitude();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addFortitude(addend2);
		final int actual2 = status.getFortitude();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addFortitude(addend3);
		final int actual3 = status.getFortitude();
		final int expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getKnowledge
	 */
	@Test
	public void testGetKnowledge() {
		final Status status = new Status();
		final int actual = status.getKnowledge();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addKnowledge
	 */
	@Test
	public void testAddKnowledge() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addKnowledge(addend1);
		final int actual1 = status.getKnowledge();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addKnowledge(addend2);
		final int actual2 = status.getKnowledge();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addKnowledge(addend3);
		final int actual3 = status.getKnowledge();
		final int expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getSmart
	 */
	@Test
	public void testGetSmart() {
		final Status status = new Status();
		final int actual = status.getSmart();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addSmart
	 */
	@Test
	public void testAddSmart() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addSmart(addend1);
		final int actual1 = status.getSmart();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addSmart(addend2);
		final int actual2 = status.getSmart();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addSmart(addend3);
		final int actual3 = status.getSmart();
		final int expected3 = 1000;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getCuriosity
	 */
	@Test
	public void testGetCuriosity() {
		final Status status = new Status();
		final int actual = status.getCuriosity();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addCuriosity
	 */
	@Test
	public void testAddCuriosity() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addCuriosity(addend1);
		final int actual1 = status.getCuriosity();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addCuriosity(addend2);
		final int actual2 = status.getCuriosity();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addCuriosity(addend3);
		final int actual3 = status.getCuriosity();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getCommunication
	 */
	@Test
	public void testGetCommunication() {
		final Status status = new Status();
		final int actual = status.getCommunication();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addCommunication
	 */
	@Test
	public void testAddCommunication() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addCommunication(addend1);
		final int actual1 = status.getCommunication();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addCommunication(addend2);
		final int actual2 = status.getCommunication();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addCommunication(addend3);
		final int actual3 = status.getCommunication();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getTolerance
	 */
	@Test
	public void testGetTolerance() {
		final Status status = new Status();
		final int actual = status.getTolerance();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addTolerance
	 */
	@Test
	public void testAddTolerance() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addTolerance(addend1);
		final int actual1 = status.getTolerance();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addTolerance(addend2);
		final int actual2 = status.getTolerance();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addTolerance(addend3);
		final int actual3 = status.getTolerance();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getDefiance
	 */
	@Test
	public void testGetDefiance() {
		final Status status = new Status();
		final int actual = status.getDefiance();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addDefiance
	 */
	@Test
	public void testAddDefiance() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addDefiance(addend1);
		final int actual1 = status.getDefiance();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addDefiance(addend2);
		final int actual2 = status.getDefiance();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addDefiance(addend3);
		final int actual3 = status.getDefiance();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getCalm
	 */
	@Test
	public void testGetCalm() {
		final Status status = new Status();
		final int actual = status.getCalm();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addClam
	 */
	@Test
	public void testAddClam() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addClam(addend1);
		final int actual1 = status.getCalm();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addClam(addend2);
		final int actual2 = status.getCalm();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addClam(addend3);
		final int actual3 = status.getCalm();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getStress
	 */
	@Test
	public void testGetStress() {
		final Status status = new Status();
		final int actual = status.getStress();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addStress
	 */
	@Test
	public void testAddStress() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addStress(addend1);
		final int actual1 = status.getStress();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addStress(addend2);
		final int actual2 = status.getStress();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addStress(addend3);
		final int actual3 = status.getStress();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getMasculinity
	 */
	@Test
	public void testGetMasculinity() {
		final Status status = new Status();
		final int actual = status.getMasculinity();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addMasculinity
	 */
	@Test
	public void testAddMasculinity() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addMasculinity(addend1);
		final int actual1 = status.getMasculinity();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addMasculinity(addend2);
		final int actual2 = status.getMasculinity();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addMasculinity(addend3);
		final int actual3 = status.getMasculinity();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getEffeminacy
	 */
	@Test
	public void testGetEffeminacy() {
		final Status status = new Status();
		final int actual = status.getAge();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addEffeminacy
	 */
	@Test
	public void testAddEffeminacy() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addEffeminacy(addend1);
		final int actual1 = status.getEffeminacy();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addEffeminacy(addend2);
		final int actual2 = status.getEffeminacy();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addEffeminacy(addend3);
		final int actual3 = status.getEffeminacy();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

	/**
	 * test of getGift
	 */
	@Test
	public void testGetGift() {
		final Status status = new Status();
		final int actual = status.getGift();

		final int expected = 0;
		assertThat(actual, is(expected));
	}

	/**
	 * test of addGift
	 */
	@Test
	public void testAddGift() {
		final Status status = new Status();

		final int addend1 = 15;
		status.addGift(addend1);
		final int actual1 = status.getGift();
		final int expected1 = addend1;
		assertThat(actual1, is(expected1));

		final int addend2 = -30;
		status.addGift(addend2);
		final int actual2 = status.getGift();
		final int expected2 = 0;
		assertThat(actual2, is(expected2));

		final int addend3 = 50000;
		status.addGift(addend3);
		final int actual3 = status.getGift();
		final int expected3 = 100;
		assertThat(actual3, is(expected3));
	}

}

package org.nognog.freeSquare.ui.square2d.actions;

import org.nognog.freeSquare.ui.square2d.Direction;
import org.nognog.freeSquare.ui.square2d.EatableObject;
import org.nognog.freeSquare.ui.square2d.LifeObject;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/02/01
 */
public class EatAction extends Action {

	private EatableObject eatObject;
	private int eatAmount;

	/**
	 * 
	 */
	public EatAction() {

	}

	/**
	 * @param eatObject
	 * @param eatAmount 
	 */
	public EatAction(EatableObject eatObject, int eatAmount) {
		this.eatObject = eatObject;
		this.setEatAmount(eatAmount);
	}

	/**
	 * @return the eatObject
	 */
	public EatableObject getEatObject() {
		return this.eatObject;
	}

	/**
	 * @param eatObject
	 *            the eatObject to set
	 */
	public void setEatObject(EatableObject eatObject) {
		this.eatObject = eatObject;
	}

	/**
	 * @return the eatAmount
	 */
	public int getEatAmount() {
		return this.eatAmount;
	}

	/**
	 * @param eatAmount the eatAmount to set
	 */
	public void setEatAmount(int eatAmount) {
		this.eatAmount = eatAmount;
	}

	@Override
	public boolean act(float delta) {
		LifeObject eater = (LifeObject) this.actor;
		if(this.eatObject.getSquare() == null || eater.getSquare() != this.eatObject.getSquare()){
			return true;
		}
		Direction direction = getDirection(eater, this.eatObject);
		eater.eat(this.eatObject, this.eatAmount, direction);
		return true;
	}

	private static Direction getDirection(Actor actor1, Actor actor2) {
		final Direction defaultDirection = Direction.UP;
		final float distanceX = actor1.getX() - actor2.getX();
		final float distanceY = actor1.getY() - actor2.getY();
		if (distanceX == 0) {
			if (distanceY == 0) {
				return defaultDirection;
			}
			if (distanceY > 0) {
				return Direction.UP;
			}
			return Direction.DOWN;
		}

		final float a = distanceY / distanceX;
		if (distanceX > 0) {
			if (a <= -1) {
				return Direction.DOWN;
			}
			if (a >= 1) {
				return Direction.UP;
			}
			return Direction.RIGHT;
		}
		
		if (a <= -1) {
			return Direction.UP;
		}
		if (a >= 1) {
			return Direction.DOWN;
		}
		return Direction.LEFT;

	}

	@Override
	public void setActor(Actor actor) {
		if (actor == null || actor instanceof LifeObject) {
			super.setActor(actor);
			return;
		}
		throw new RuntimeException("actor of EatAction must be LifeObject instance."); //$NON-NLS-1$
	}
}

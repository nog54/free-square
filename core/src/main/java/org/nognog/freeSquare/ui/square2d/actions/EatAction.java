package org.nognog.freeSquare.ui.square2d.actions;

import org.nognog.freeSquare.ui.square2d.Direction;
import org.nognog.freeSquare.ui.square2d.EatableObject;
import org.nognog.freeSquare.ui.square2d.LifeObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/02/01
 */
public class EatAction extends Action {

	/**
	 * keep eating while eatObject amount is not zero.
	 */
	public static final int UNTIL_RUN_OUT = -1;
	private EatableObject eatObject;
	private int eatAmount;
	private int eatCount = UNTIL_RUN_OUT;
	private float eatInterval;
	private float moveSpeed;

	private float timeFromLastEat = 0;
	private int executedCount = 0;

	private boolean isRequestedForceFinish = false;

	/**
	 * 
	 */
	public EatAction() {
	}

	/**
	 * @param eatObject
	 * @param eatAmount
	 * @param eatInterval
	 * @param moveSpeed
	 */
	public EatAction(EatableObject eatObject, int eatAmount, float eatInterval, float moveSpeed) {
		this.eatObject = eatObject;
		this.eatAmount = eatAmount;
		this.eatInterval = eatInterval;
		this.setMoveSpeed(moveSpeed);
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
	 * @param eatAmount
	 *            the eatAmount to set
	 */
	public void setEatAmount(int eatAmount) {
		this.eatAmount = eatAmount;
	}

	/**
	 * @return the eatCount
	 */
	public int getEatCount() {
		return this.eatCount;
	}

	/**
	 * @param eatCount
	 *            the eatCount to set
	 */
	public void setEatCount(int eatCount) {
		this.eatCount = eatCount;
	}

	/**
	 * @return the moveSpeed
	 */
	public float getMoveSpeed() {
		return this.moveSpeed;
	}

	/**
	 * @param moveSpeed
	 *            the moveSpeed to set
	 */
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * @return the eatInterval
	 */
	public float getEatInterval() {
		return this.eatInterval;
	}

	/**
	 * @param eatInterval
	 *            the eatInterval to set
	 */
	public void setEatInterval(float eatInterval) {
		this.eatInterval = eatInterval;
	}

	@Override
	public boolean act(float delta) {
		if (this.isRequestedForceFinish) {
			return true;
		}
		LifeObject eater = (LifeObject) this.actor;
		if (this.eatCount != UNTIL_RUN_OUT && this.executedCount >= this.eatCount) {
			return true;
		}
		if (this.eatObject.getSquare() == null || eater.getSquare() != this.eatObject.getSquare()) {
			return true;
		}

		this.timeFromLastEat += delta;
		if (eater.getDistanceTo(this.eatObject) > this.eatObject.getWidth() / 2) {
			this.moveTowardEatObject(delta);
			return false;
		}

		if (this.timeFromLastEat < this.eatInterval) {
			return false;
		}

		Direction direction = getDirection(eater, this.eatObject);
		eater.eat(this.eatObject, this.eatAmount, direction);
		this.timeFromLastEat = 0;
		this.executedCount++;
		if (this.eatObject.getAmount() == 0) {
			return true;
		}
		if (this.eatCount != UNTIL_RUN_OUT && this.executedCount >= this.eatCount) {
			return true;
		}
		return false;
	}

	private void moveTowardEatObject(float delta) {
		if (this.moveSpeed == 0) {
			throw new RuntimeException("moveSpeed is zero."); //$NON-NLS-1$
		}
		final float targetPositionX = this.eatObject.getX();
		final float targetPositionY = this.eatObject.getY();
		final float remainingDistanceX = targetPositionX - this.actor.getX();
		final float remainingDistanceY = targetPositionY - this.actor.getY();
		final float theta = MathUtils.atan2(targetPositionY - this.actor.getY(), targetPositionX - this.actor.getX());
		final float r = delta * this.moveSpeed;
		final float moveX = r * MathUtils.cos(theta);
		if (Math.abs(remainingDistanceX) < Math.abs(moveX)) {
			this.actor.setPosition(targetPositionX, targetPositionY);
		}
		final float moveY = r * MathUtils.sin(theta);
		if (Math.abs(remainingDistanceY) < Math.abs(moveY)) {
			this.actor.setPosition(targetPositionX, targetPositionY);
		}

		this.actor.moveBy(moveX, moveY);
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
	
	@Override
	public void restart() {
		this.isRequestedForceFinish = false;
		super.restart();
	}

	/**
	 * 
	 */
	public void requestForceFinish() {
		this.isRequestedForceFinish = true;
	}

}

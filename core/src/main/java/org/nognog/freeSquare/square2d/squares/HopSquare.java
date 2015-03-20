package org.nognog.freeSquare.square2d.squares;

import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/15
 */
public class HopSquare extends SimpleSquare2d {

	@SuppressWarnings("unused")
	private HopSquare() {
		// used by json
		this(null);
	}

	/**
	 * @param type
	 */
	public HopSquare(Square2dType type) {
		super(type);
		this.addListener(new ActorGestureListener() {
			private static final float hopAmount = 10;
			private static final float hopTime = 0.2f;
			HopSquare target = HopSquare.this;

			@Override
			public void tap(InputEvent event, float x, float y, int pointer, int button) {
				if (this.target.containsPosition(x, y)) {
					Action squareHopAction = Actions.sequence(Actions.moveBy(0, hopAmount, hopTime / 2), Actions.moveBy(0, -hopAmount, hopTime / 2));
					this.target.getSquareImage().addAction(squareHopAction);
					for(Square2dObject object : this.target.getAllLandingSquareObjectsOnStage()){
						Action objectHopAction = Actions.sequence(Actions.moveBy(0, hopAmount, hopTime / 2), Actions.moveBy(0, -hopAmount, hopTime / 2));
						object.getIcon().addAction(objectHopAction);
					}
				}
			}
		});
	}
}

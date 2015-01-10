package org.nognog.freeSquare.square2d.objects;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.FreeRunningLandObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/19
 */
public class Riki extends FreeRunningLandObject {

	private static final float logicalWidth = 100;

	/**
	 * create Riki
	 */
	public Riki() {
		super(new Texture(Gdx.files.internal(Resources.rikiPath)), logicalWidth, logicalWidth);
		this.addListener(new ActorGestureListener() {
			@Override
			public boolean longPress(Actor actor, float x, float y) {
				return true;
			}
			
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if(count == 2){
					//
				}
			}
		});
	}
}

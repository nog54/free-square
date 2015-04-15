package org.nognog.freeSquare.activity;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.model.square.SquareEventListener;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * @author goshi 2015/04/14
 */
public abstract class FreeSquareActivity extends Group implements CameraObserver, SquareEventListener{
	private final FreeSquare freeSquare;

	/**
	 * @param freeSquare
	 */
	public FreeSquareActivity(FreeSquare freeSquare) {
		if(freeSquare == null){
			throw new IllegalArgumentException();
		}
		this.freeSquare = freeSquare;
	}
	
	/**
	 * @return freeSquare
	 */
	public FreeSquare getFreeSquare(){
		return this.freeSquare;
	}

	/**
	 * @return activity input processor
	 */
	public abstract InputProcessor getInputProcesser();
	
	/**
	 * Be called when activity end
	 */
	public abstract void resume();
	
	/**
	 * Be called when activity end
	 */
	public abstract void pause();
}

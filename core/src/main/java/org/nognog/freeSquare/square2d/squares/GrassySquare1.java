package org.nognog.freeSquare.square2d.squares;

import static org.nognog.freeSquare.square2d.Square2D.Vertex.vertex;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.Square2D;
import org.nognog.freeSquare.square2d.Square2DSize;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/15
 */
public class GrassySquare1 extends Square2D {

	private static final Vertex baseVertex1 = vertex(511.5f, 64f);
	private static final Vertex baseVertex2 = vertex(1010f, 272f);
	private static final Vertex baseVertex3 = vertex(511.5f, 478f);
	private static final Vertex baseVertex4 = vertex(14f, 272f);

	/**
	 * @param size
	 */
	public GrassySquare1(Square2DSize size) {
		super(size, baseVertex1, baseVertex2, baseVertex3, baseVertex4, new Texture(
				Gdx.files.internal(Resources.grassy1Path)));
		this.addListener(new ActorGestureListener() {
			private static final float hopAmount = 10;
			private static final float hopTime = 0.2f;
			GrassySquare1 target = GrassySquare1.this;

			@Override
			public void tap(InputEvent event, float x, float y, int pointer, int button) {
				if (GrassySquare1.this.containsInSquareArea(x, y)) {
					Action upDown = Actions.sequence(Actions.moveBy(0, hopAmount, hopTime / 2),
							Actions.moveBy(0, -hopAmount, hopTime / 2));
					this.target.addAction(upDown);
				}
			}
			
		});

	}
}

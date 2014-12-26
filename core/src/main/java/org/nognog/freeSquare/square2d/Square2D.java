package org.nognog.freeSquare.square2d;

import static org.nognog.freeSquare.square2d.Square2D.Vertex.vertex;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nognog.freeSquare.square.Square;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * @author goshi 2014/12/10
 */
public class Square2D extends Group implements Square<SquareObject2D> {
	/** vertex1 of Square */
	public final Vertex vertex1;
	/** vertex2 of Square */
	public final Vertex vertex2;
	/** vertex3 of Square */
	public final Vertex vertex3;
	/** vertex4 of Square */
	public final Vertex vertex4;
	private final boolean isConcave;
	private final Square2DSize size;
	private Image squareImage;

	private Array<SquareObject2D> objects;

	private ExecutorService pool = Executors.newCachedThreadPool();

	private boolean isRequestedDrawOrderUpdate = false;
	private boolean isDisposed = false;

	/**
	 * @param size
	 * @param baseVertex1
	 * @param baseVertex2
	 * @param baseVertex3
	 * @param baseVertex4
	 * @param texture
	 */
	public Square2D(Square2DSize size, Vertex baseVertex1, Vertex baseVertex2, Vertex baseVertex3, Vertex baseVertex4,
			Texture texture) {
		this.size = size;
		final float width = size.getWidth();
		final float scale = width / texture.getWidth();
		final float squareImagePositionOffsetY = width / 16;
		this.vertex1 = vertex(baseVertex1.x * scale, baseVertex1.y * scale + squareImagePositionOffsetY);
		this.vertex2 = vertex(baseVertex2.x * scale, baseVertex2.y * scale + squareImagePositionOffsetY);
		this.vertex3 = vertex(baseVertex3.x * scale, baseVertex3.y * scale + squareImagePositionOffsetY);
		this.vertex4 = vertex(baseVertex4.x * scale, baseVertex4.y * scale + squareImagePositionOffsetY);
		if (this.isInvalidVertex()) {
			throw new RuntimeException("Square corners are invalid."); //$NON-NLS-1$
		}
		if (Intersector.intersectSegments(this.vertex1.x, this.vertex1.y, this.vertex3.x, this.vertex3.y,
				this.vertex2.x, this.vertex2.y, this.vertex4.x, this.vertex4.y, null)) {
			this.isConcave = false;
		} else {
			this.isConcave = true;
		}

		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.squareImage = new Image(texture);
		this.squareImage.setScale(scale);
		this.squareImage.setY(squareImagePositionOffsetY);
		this.addActor(this.squareImage);
		this.objects = new SnapshotArray<>();
	}

	private boolean isInvalidVertex() {
		if (Intersector.intersectSegments(this.vertex1.x, this.vertex1.y, this.vertex2.x, this.vertex2.y,
				this.vertex3.x, this.vertex3.y, this.vertex4.x, this.vertex4.y, null)) {
			return true;
		}
		if (Intersector.intersectSegments(this.vertex2.x, this.vertex2.y, this.vertex3.x, this.vertex3.y,
				this.vertex4.x, this.vertex4.y, this.vertex1.x, this.vertex1.y, null)) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
		if (this.isRequestedDrawOrderUpdate) {
			this.getChildren().sort(ActorComparator.getInstance());
			moveSquareImageToBack();
			this.isRequestedDrawOrderUpdate = false;
		}
		super.draw(batch, parentAlpha);
	}

	private void moveSquareImageToBack() {
		if (this.squareImage.getZIndex() != 0) {
			this.squareImage.setZIndex(0);
		}
	}

	/**
	 * @param object
	 */
	@Override
	public void addObject(SquareObject2D object) {
		this.objects.add(object);
		this.addActor(object);
		object.setSquare(this);
	}

	/**
	 * @param object
	 */
	public void addAndRunObject(SquareObject2D object) {
		this.addObject(object);
		object.startIndependentAction();
	}

	@Override
	public Iterable<SquareObject2D> getObjects() {
		return this.objects;
	}

	/**
	 * request a change the order in which actors are drawed
	 */
	public void requestDrawOrderUpdate() {
		this.isRequestedDrawOrderUpdate = true;
	}

	/**
	 * @return size
	 */
	public Square2DSize getSize() {
		return this.size;
	}

	/**
	 * @return pool
	 */
	public ExecutorService getPool() {
		return this.pool;
	}

	/**
	 * @return square image
	 */
	public Image getImage() {
		return this.squareImage;
	}

	/**
	 * @return true if square is concave
	 */
	public boolean isConcave() {
		return this.isConcave;
	}

	/**
	 * dispose
	 */
	public void dispose() {
		this.pool.shutdownNow();
		((TextureRegionDrawable) (this.squareImage.getDrawable())).getRegion().getTexture().dispose();
		for (SquareObject2D object : this.objects) {
			if (!object.isDisposed()) {
				object.dispose();
			}
		}
		this.isDisposed = true;
	}

	/**
	 * @return whether this is disposed
	 */
	public boolean isDisposed() {
		return this.isDisposed;
	}

	/**
	 * Immutable vector
	 * 
	 * @author goshi 2014/12/17
	 */
	public static class Vertex {
		/** x-axis point */
		public final float x;
		/** y-axis point */
		public final float y;

		/**
		 * @param x
		 * @param y
		 */
		public Vertex(float x, float y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * factory method
		 * 
		 * @param x
		 * @param y
		 * @return new instance
		 */
		public static Vertex vertex(float x, float y) {
			return new Vertex(x, y);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			return sb.append("(").append(this.x).append(",").append(this.y).append(")").toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
}

/**
 * @author goshi 2014/12/19
 */
class ActorComparator implements Comparator<Actor> {

	private static ActorComparator instance = new ActorComparator();

	private ActorComparator() {
	}

	@Override
	public int compare(Actor actor1, Actor actor2) {
		if (actor1.getY() < actor2.getY()) {
			return -1;
		} else if (actor1.getY() > actor2.getY()) {
			return 1;
		}
		return 0;
	}

	/**
	 * @return get singleton instance
	 */
	public static ActorComparator getInstance() {
		return instance;
	}

}

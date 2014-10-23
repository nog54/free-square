package org.nognog.freeSquare.square2d;

import static org.nognog.freeSquare.square2d.Square2D.Vertex.vertex;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nognog.freeSquare.square.Square;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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

	private ExecutorService pool = Executors.newCachedThreadPool();

	private final FourCorner corners;
	private Image squareImage;

	private Array<SquareObject2D> objects;

	private boolean isRequestedDrawOrderUpdate = false;
	private boolean isDisposed = false;

	/**
	 * @param width
	 * @param nearBase
	 * @param rightBase
	 * @param farBase
	 * @param leftBase
	 * @param texture
	 */
	public Square2D(float width, Vertex nearBase, Vertex rightBase, Vertex farBase, Vertex leftBase, Texture texture) {
		final float scale = width / texture.getWidth();
		final float squareImagePositionOffsetY = width / 16;
		Vertex near = vertex(nearBase.x * scale, nearBase.y * scale + squareImagePositionOffsetY);
		Vertex right = vertex(rightBase.x * scale, rightBase.y * scale + squareImagePositionOffsetY);
		Vertex far = vertex(farBase.x * scale, farBase.y * scale + squareImagePositionOffsetY);
		Vertex left = vertex(leftBase.x * scale, leftBase.y * scale + squareImagePositionOffsetY);
		this.corners = new FourCorner(near, right, far, left);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.squareImage = new Image(texture);
		this.squareImage.setScale(scale);
		this.squareImage.setY(squareImagePositionOffsetY);
		this.addActor(this.squareImage);
		this.objects = new SnapshotArray<>();
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
	public void addAndStartObject(SquareObject2D object) {
		this.addObject(object);
		object.startIndependentAction();
	}

	@Override
	public Iterable<SquareObject2D> getObjects() {
		return this.objects;
	}

	/**
	 * @return corners of Square
	 */
	public FourCorner getCorners() {
		return this.corners;
	}

	/**
	 * request a change the order in which actors are drawed
	 */
	public void requestDrawOrderUpdate() {
		this.isRequestedDrawOrderUpdate = true;
	}

	/**
	 * @return pool
	 */
	public ExecutorService getPool() {
		return this.pool;
	}

	/**
	 * dispose
	 */
	public void dispose() {
		this.pool.shutdownNow();
		((TextureRegionDrawable) (this.squareImage.getDrawable())).getRegion().getTexture().dispose();
		for (SquareObject2D object : this.objects) {
			if(!object.isDisposed()){
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
	 * @author goshi 2014/12/17
	 */
	public static class FourCorner {
		/** near vertex of Square */
		public final Vertex near;
		/** right vertex of Square */
		public final Vertex right;
		/** far vertex of Square */
		public final Vertex far;
		/** left vertex of Square */
		public final Vertex left;

		/**
		 * @param near
		 * @param right
		 * @param far
		 * @param left
		 */
		public FourCorner(Vertex near, Vertex right, Vertex far, Vertex left) {
			this.near = near;
			this.right = right;
			this.far = far;
			this.left = left;
		}
	}

	/**
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

package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.square2d.SimpleSquare2d.Vertex;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.ui.SquareObserver;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author goshi
 * 2015/02/15
 */
public class CombinedSquare2d extends Group implements Square2d {

	private SimpleSquare2d base;
	private ObjectMap<Vertex, SimpleSquare2d> combinedPoints;
	
	/**
	 * @param base
	 */
	public CombinedSquare2d(SimpleSquare2d base){
		this.base = base;
		this.combinedPoints = new ObjectMap<>();
	}

	@Override
	public void addSquareObject(Square2dObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeSquareObject(Square2dObject object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Square2dObject> getObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSquareObserver(SquareObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSquareObserver(SquareObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyObservers(Square2dEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notify(Square2dEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vertex[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getImageWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getImageHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean containsInSquare(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

}

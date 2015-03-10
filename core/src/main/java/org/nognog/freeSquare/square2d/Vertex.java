package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.model.persist.PersistManager;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Immutable vector
 * 
 * @author goshi 2014/12/17
 */
public class Vertex {
	static {
		Json json = PersistManager.getUseJson();
		json.setSerializer(Vertex.class, new Json.Serializer<Vertex>() {

			@Override
			@SuppressWarnings({ "hiding", "rawtypes" })
			public void write(Json json, Vertex object, Class knownType) {
				json.writeObjectStart();
				json.writeFields(object);
				json.writeObjectEnd();
			}

			@Override
			@SuppressWarnings({ "boxing", "hiding", "rawtypes" })
			public Vertex read(Json json, JsonValue jsonData, Class type) {
				final float x = json.readValue("x", Float.class, jsonData); //$NON-NLS-1$
				final float y = json.readValue("y", Float.class, jsonData); //$NON-NLS-1$
				return new Vertex(x, y);
			}
		});
	}
	/** x-axis point */
	public final float x;
	/** y-axis point */
	public final float y;

	/**
	 * @param vertex
	 */
	public Vertex(Vertex vertex) {
		this(vertex.x, vertex.y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vertex(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param x1
	 * @param y1
	 * @return sqrt((this.x - x) ^ 2 + (this.y - y) ^ 2 )
	 */
	public float calculateR(float x1, float y1) {
		final float diffX = this.x - x1;
		final float diffY = this.y - y1;
		return (float) Math.sqrt(diffX * diffX + diffY * diffY);
	}

	/**
	 * @param vertex
	 * @return sqrt((this.x - x) ^ 2 + (this.y - y) ^ 2 )
	 */
	public float calculateR(Vertex vertex) {
		return this.calculateR(vertex.x, vertex.y);
	}

	/**
	 * @param x
	 * @param y
	 * @return true if same x,y
	 */
	@SuppressWarnings("hiding")
	public boolean equals(float x, float y) {
		return x == this.x && y == this.y;
	}

	/**
	 * @param vertex
	 * @return true if same value
	 */
	public boolean equals(Vertex vertex) {
		return this.equals(vertex.x, vertex.y);
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
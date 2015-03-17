package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.squares.Square2dType;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2014/12/10
 */
public class SimpleSquare2d extends Square2d implements Json.Serializable {

	private Square2dType type;

	private Image squareImage;

	private SimpleSquare2d() {
		// used by json.
		this.addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (SimpleSquare2d.this.containsPosition(x, y) || this.isTouchingSquareObject(x, y)) {
					return false;
				}
				event.stop();
				return false;
			}

			private boolean isTouchingSquareObject(float x, float y) {
				Actor touchedActor = SimpleSquare2d.this.hit(x, y, true);
				return touchedActor != SimpleSquare2d.this.getSquareImage();
			}
		});
	}

	/**
	 * @param type
	 */
	public SimpleSquare2d(Square2dType type) {
		this();
		setupType(type);
	}

	protected void setupType(Square2dType type) {
		if (type == null) {
			return;
		}
		if (this.type != null) {
			throw new RuntimeException("type is already setted"); //$NON-NLS-1$
		}
		this.type = type;
		this.squareImage = this.createSquareImage(type);
		super.addActorForce(this.squareImage);
		this.setName(type.getName());
	}

	private Image createSquareImage(Square2dType createSquareType) {
		Image image = new Image(createSquareType.getTexture()) {
			@Override
			public Actor hit(float x, float y, boolean touchable) {
				if (touchable && this.getTouchable() != Touchable.enabled)
					return null;
				if (SimpleSquare2d.this.containsPosition(x + this.getX(), y + this.getY())) {
					return this;
				}
				return null;
			}
		};
		image.setWidth(createSquareType.getSize().getWidth());
		image.setHeight(image.getHeight() * (createSquareType.getSize().getWidth() / createSquareType.getTexture().getWidth()));
		image.setY(createSquareType.getSquarePositionOffsetY());
		image.setName(createSquareType.getName());
		return image;
	}

	@Override
	public float getLeftEndX() {
		return this.getX();
	}

	@Override
	public float getRightEndX() {
		return this.getX() + this.squareImage.getWidth();
	}

	@Override
	public float getButtomEndY() {
		return this.getY();
	}

	@Override
	public float getTopEndY() {
		return this.getY() + this.squareImage.getHeight();
	}

	/**
	 * @return size
	 */
	public Square2dSize getSquareSize() {
		return this.type.getSize();
	}

	/**
	 * @return square image
	 */
	public Image getSquareImage() {
		return this.squareImage;
	}

	/**
	 * @return true if square is concave
	 */
	public boolean isConcave() {
		return this.type.isConcave();
	}

	@Override
	public Vertex[] getVertices() {
		return new Vertex[] { this.type.vertex1, this.type.vertex2, this.type.vertex3, this.type.vertex4 };
	}

	/**
	 * @return vertex1
	 */
	public Vertex getVertex1() {
		return this.type.vertex1;
	}

	/**
	 * @return vertex1
	 */
	public Vertex getVertex2() {
		return this.type.vertex2;
	}

	/**
	 * @return vertex1
	 */
	public Vertex getVertex3() {
		return this.type.vertex3;
	}

	/**
	 * @return vertex1
	 */
	public Vertex getVertex4() {
		return this.type.vertex4;
	}

	/**
	 * @return type
	 */
	public Square2dType getSquare2dType() {
		return this.type;
	}

	@Override
	public void write(Json json) {
		json.writeField(this, "type"); //$NON-NLS-1$
		json.writeField(this, "objects"); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, JsonValue jsonData) {
		Square2dType readType = json.readValue(Square2dType.class, jsonData.get("type")); //$NON-NLS-1$
		this.setupType(readType);
		Array<Square2dObject> readObjects = json.readValue(Array.class, jsonData.get("objects")); //$NON-NLS-1$
		for (Square2dObject object : readObjects) {
			this.addSquareObject(object, object.getX(), object.getY(), false);
		}
	}
}

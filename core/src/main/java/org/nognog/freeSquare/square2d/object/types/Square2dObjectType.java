package org.nognog.freeSquare.square2d.object.types;

import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi 2015/01/14
 */
@SuppressWarnings("javadoc")
public interface Square2dObjectType<T extends Square2dObject> {

	String getName();

	Texture getTexture();

	float getLogicalWidth();

	Color getColor();

	T create();

	public static class Manager {
		public static Square2dObjectType<?>[] getAllTypeValues() {
			int allValuesLength = EatableObjectType.values().length + LifeObjectType.values().length + OtherObjectType.values().length;
			final Square2dObjectType<?>[] allValues = new Square2dObjectType[allValuesLength];
			int i = 0;
			for (LifeObjectType type : LifeObjectType.values()) {
				allValues[i] = type;
				i++;
			}
			for (EatableObjectType type : EatableObjectType.values()) {
				allValues[i] = type;
				i++;
			}
			for (OtherObjectType type : OtherObjectType.values()) {
				allValues[i] = type;
				i++;
			}
			return allValues;

		}
		
		public static void dispose(){
			LifeObjectType.dispose();
			EatableObjectType.dispose();
			OtherObjectType.dispose();
		}
	}

}

package org.nognog.freeSquare;

import com.badlogic.gdx.Gdx;

/**
 * @author goshi
 *
 */
public class Settings {
	private Settings() {

	}

	static {
		initialize();
	}
	private static int physicalDisplayWidth;
	private static int physicalDisplayHeight;
	private static float aspectRatio;
	private static int defaultLogicalCameraWidth;
	private static int defaultLogicalCameraHeight;
	
	private static final float goldenRatio = 1.6180339887f;

	/**
	 * 
	 */
	public static void initialize() {
		physicalDisplayWidth = Gdx.graphics.getWidth();
		physicalDisplayHeight = Gdx.graphics.getHeight();
		aspectRatio = (float) physicalDisplayHeight / physicalDisplayWidth;
		defaultLogicalCameraWidth = 1024;
		defaultLogicalCameraHeight = (int) (defaultLogicalCameraWidth * aspectRatio);
	}

	/**
	 * @return physical display width when {@link Settings#initialize()} is
	 *         called
	 */
	public static int getPhysicalDisplayWidth() {
		return physicalDisplayWidth;
	}

	/**
	 * @return physical display height when {@link Settings#initialize()} is
	 *         called
	 */
	public static int getPhysicalDisplayHeight() {
		return physicalDisplayHeight;
	}

	/**
	 * @return aspect ratio when {@link Settings#initialize()} is called
	 */
	public static float getAspectRatio() {
		return aspectRatio;
	}

	/**
	 * @return logical camera width when {@link Settings#initialize()} is called
	 */
	public static int getDefaultLogicalCameraWidth() {
		return defaultLogicalCameraWidth;
	}

	/**
	 * @return logical camera height when {@link Settings#initialize()} is
	 *         called
	 */
	public static int getDefaultLogicalCameraHeight() {
		return defaultLogicalCameraHeight;
	}
	
	/**
	 * @return golden ratio
	 */
	public static float getGoldenRatio(){
		return goldenRatio;
	}
}

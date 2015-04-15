package org.nognog.freeSquare.square2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/03/22
 */
public class MultiLevelFlickButtonController extends FlickButtonController {

	private final Array<LevelInformation> levels;
	private LevelInformation showingLevelInformation;

	/**
	 * @param font
	 * @param buttonWidthHeight
	 * @param inputListener
	 */
	public MultiLevelFlickButtonController(BitmapFont font, float buttonWidthHeight) {
		super(font, buttonWidthHeight, null);
		this.levels = new Array<>();
		this.setCenterButtonText("next"); //$NON-NLS-1$
	}

	/**
	 * @param inputListener
	 * @param upButtonText
	 * @param downButtonText
	 * @param rightButtonText
	 * @param leftButtonText
	 * @param buttonUpColor
	 * @param buttonDownColor
	 */
	public void addFlickButtonController(MultiLevelFlickButtonInputListener inputListener, String upButtonText, String downButtonText, String rightButtonText, String leftButtonText) {
		this.addFlickButtonController(inputListener, upButtonText, downButtonText, rightButtonText, leftButtonText, defaultUpColor, defaultDownColor);
	}

	/**
	 * @param inputListener
	 * @param upButtonText
	 * @param downButtonText
	 * @param rightButtonText
	 * @param leftButtonText
	 * @param buttonUpColor
	 * @param buttonDownColor
	 */
	public void addFlickButtonController(MultiLevelFlickButtonInputListener inputListener, String upButtonText, String downButtonText, String rightButtonText, String leftButtonText,
			Color buttonUpColor, Color buttonDownColor) {
		FlickButtonInputListener flickButtonInputListener = toFlickButtonInputListener(inputListener);
		final TextureRegionDrawable buttonUpTexture = FlickButtonController.getPlaneTextureRegionDrawable(buttonUpColor);
		final TextureRegionDrawable buttonDownTexture = FlickButtonController.getPlaneTextureRegionDrawable(buttonDownColor);
		final LevelInformation levelInformation = new LevelInformation(flickButtonInputListener, upButtonText, downButtonText, rightButtonText, leftButtonText, buttonUpTexture, buttonDownTexture);
		this.levels.add(levelInformation);
		if (this.getListener() == null) {
			this.setShowFlickButtonController(levelInformation);
		}
	}

	private void setShowFlickButtonController(LevelInformation levelInformation) {
		this.setListener(levelInformation.inputListener);
		this.setUpButtonText(levelInformation.upButtonText);
		this.setDownButtonText(levelInformation.downButtonText);
		this.setRightButtonText(levelInformation.rightButtonText);
		this.setLeftButtonText(levelInformation.leftButtonText);
		setButtonColor(this.centerButton, levelInformation);
		setButtonColor(this.upButton, levelInformation);
		setButtonColor(this.downButton, levelInformation);
		setButtonColor(this.rightButton, levelInformation);
		setButtonColor(this.leftButton, levelInformation);
		this.showingLevelInformation = levelInformation;
	}

	private static void setButtonColor(TextButton button, LevelInformation levelInformation) {
		button.getStyle().up = levelInformation.buttonUpTexture;
		button.getStyle().down = levelInformation.buttonDownTexture;
		button.getStyle().checked = levelInformation.buttonDownTexture;
	}

	protected void showNextLevelFlickButtonController() {
		final int showingLevelIndex = this.levels.indexOf(this.showingLevelInformation, true);
		final int nextLevelIndex = (showingLevelIndex + 1) % this.levels.size;
		this.setShowFlickButtonController(this.levels.get(nextLevelIndex));
	}

	/**
	 * show first level
	 */
	public void showFirstLevel() {
		if (this.levels.size != 0) {
			this.setShowFlickButtonController(this.levels.get(0));
		}
	}

	/**
	 * @author goshi 2015/01/08
	 */
	public interface MultiLevelFlickButtonInputListener {

		/**
		 * Called when up is selected
		 */
		void up();

		/**
		 * Called when down is selected
		 */
		void down();

		/**
		 * Called when right is selected
		 */
		void right();

		/**
		 * Called when left is selected
		 */
		void left();

	}

	private FlickButtonInputListener toFlickButtonInputListener(final MultiLevelFlickButtonInputListener inputListener) {
		return new FlickButtonInputListener() {

			@Override
			public void center() {
				MultiLevelFlickButtonController.this.showNextLevelFlickButtonController();
			}

			@Override
			public void up() {
				inputListener.up();
			}

			@Override
			public void down() {
				inputListener.down();
			}

			@Override
			public void right() {
				inputListener.right();
			}

			@Override
			public void left() {
				inputListener.left();
			}

			@Override
			public boolean longPressCenter() {
				MultiLevelFlickButtonController.this.showNextLevelFlickButtonController();
				return true;
			}

		};
	}

	private class LevelInformation {

		FlickButtonInputListener inputListener;
		String upButtonText;
		String downButtonText;
		String rightButtonText;
		String leftButtonText;
		TextureRegionDrawable buttonUpTexture;
		TextureRegionDrawable buttonDownTexture;

		public LevelInformation(FlickButtonInputListener inputListener, String upButtonText, String downButtonText, String rightButtonText, String leftButtonText,
				TextureRegionDrawable buttonUpTexture, TextureRegionDrawable buttonDownTexture) {
			this.inputListener = inputListener;
			this.upButtonText = upButtonText;
			this.downButtonText = downButtonText;
			this.rightButtonText = rightButtonText;
			this.leftButtonText = leftButtonText;
			this.buttonUpTexture = buttonUpTexture;
			this.buttonDownTexture = buttonDownTexture;
		}
	}
}
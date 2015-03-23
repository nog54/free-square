package org.nognog.freeSquare.square2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2015/01/08
 */
public class FlickButtonController extends Group {

	protected static Color defaultUpColor = ColorUtils.emerald;
	protected static Color defaultDownColor = ColorUtils.nephritis;
	private FlickButtonInputListener listener;
	protected BitmapFont font;
	protected float buttonWidthHeight;
	protected TextButton centerButton;
	protected TextButton upButton;
	protected TextButton downButton;
	protected TextButton rightButton;
	protected TextButton leftButton;

	/**
	 * @param font
	 * @param buttonWidthHeight
	 * @param inputListener
	 */
	public FlickButtonController(BitmapFont font, final float buttonWidthHeight, FlickButtonInputListener inputListener) {
		this(font, buttonWidthHeight, inputListener, defaultUpColor, defaultDownColor);
	}

	/**
	 * @param font
	 * @param buttonWidthHeight
	 * @param inputListener
	 * @param upColor
	 * @param downColor
	 */
	public FlickButtonController(BitmapFont font, final float buttonWidthHeight, FlickButtonInputListener inputListener, Color upColor, Color downColor) {
		this.font = font;
		this.buttonWidthHeight = buttonWidthHeight;
		this.listener = inputListener;
		this.initializeButtons(upColor, downColor);
		this.addActor(this.centerButton);
		this.addActor(this.upButton);
		this.addActor(this.downButton);
		this.addActor(this.leftButton);
		this.addActor(this.rightButton);
	}

	private void initializeButtons(Color upColor, Color downColor) {
		this.createButtons(upColor, downColor);
		this.addListenerToButton();
	}

	private void createButtons(Color upColor, Color downColor) {
		this.centerButton = this.createMenuTextButton("Center", -this.buttonWidthHeight / 2, -this.buttonWidthHeight / 2, upColor, downColor); //$NON-NLS-1$
		this.upButton = this.createMenuTextButton("Up", this.centerButton.getX(), this.centerButton.getY() + this.buttonWidthHeight, upColor, downColor); //$NON-NLS-1$
		this.downButton = this.createMenuTextButton("Down", this.centerButton.getX(), this.centerButton.getY() - this.buttonWidthHeight, upColor, downColor); //$NON-NLS-1$
		this.rightButton = this.createMenuTextButton("Right", this.centerButton.getX() + this.buttonWidthHeight, this.centerButton.getY(), upColor, downColor); //$NON-NLS-1$ 
		this.leftButton = this.createMenuTextButton("Left", this.centerButton.getX() - this.buttonWidthHeight, this.centerButton.getY(), upColor, downColor); //$NON-NLS-1$ 
	}

	private TextButton createMenuTextButton(String text, float x, float y, Color up, Color down) {

		TextureRegionDrawable upTexture = getPlaneTextureRegionDrawable(up);
		TextureRegionDrawable downTexture = getPlaneTextureRegionDrawable(down);
		TextButtonStyle buttonStyle = new TextButtonStyle(upTexture, downTexture, downTexture, this.font);
		TextButton textButton = new TextButton(text, buttonStyle);
		textButton.setSize(this.buttonWidthHeight, this.buttonWidthHeight);
		textButton.setPosition(x, y);
		return textButton;
	}

	protected static TextureRegionDrawable getPlaneTextureRegionDrawable(Color color) {
		if (color == null) {
			return null;
		}
		final int textureRegionWidth = 256;
		final int textureRegionHeight = 128;
		return UiUtils.getPlaneTextureRegionDrawable(textureRegionWidth, textureRegionHeight, color);
	}

	private void addListenerToButton() {
		this.centerButton.addListener(new ActorGestureListener() {

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				this.handleUpButton(event, x, y);
				this.handleDownButton(event, x, y);
				this.handleRightButton(event, x, y);
				this.handleLeftButton(event, x, y);
			}

			private void handleUpButton(InputEvent event, float x, float y) {
				if (isUpButtonTouchValidRange(x, y)) {
					FlickButtonController.this.upButton.setChecked(true);
					return;
				}
				FlickButtonController.this.upButton.setChecked(false);
			}

			private void handleDownButton(InputEvent event, float x, float y) {
				if (isDownButtonTouchValidRange(x, y)) {
					FlickButtonController.this.downButton.setChecked(true);
					return;
				}
				FlickButtonController.this.downButton.setChecked(false);
			}

			private void handleRightButton(InputEvent event, float x, float y) {
				if (isRightButtonTouchValidRange(x, y)) {
					FlickButtonController.this.rightButton.setChecked(true);
					return;
				}
				FlickButtonController.this.rightButton.setChecked(false);
			}

			private void handleLeftButton(InputEvent event, float x, float y) {
				if (isLeftButtonTouchValidRange(x, y)) {
					FlickButtonController.this.leftButton.setChecked(true);
					return;
				}
				FlickButtonController.this.leftButton.setChecked(false);
			}

			private boolean isUpButtonTouchValidRange(float x, float y) {
				return (0 <= x && x <= FlickButtonController.this.buttonWidthHeight) && (FlickButtonController.this.buttonWidthHeight <= y);
			}

			private boolean isDownButtonTouchValidRange(float x, float y) {
				return (0 <= x && x <= FlickButtonController.this.buttonWidthHeight) && (y <= 0);
			}

			private boolean isRightButtonTouchValidRange(float x, float y) {
				return (FlickButtonController.this.buttonWidthHeight <= x) && (0 <= y && y <= FlickButtonController.this.buttonWidthHeight);
			}

			private boolean isLeftButtonTouchValidRange(float x, float y) {
				return (x <= 0) && (0 <= y && y <= FlickButtonController.this.buttonWidthHeight);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (FlickButtonController.this.upButton.isChecked()) {
					FlickButtonController.this.selectUpButton();
					return;
				}
				if (FlickButtonController.this.downButton.isChecked()) {
					FlickButtonController.this.selectDownButton();
					return;
				}
				if (FlickButtonController.this.rightButton.isChecked()) {
					FlickButtonController.this.selectRightButton();
					return;
				}
				if (FlickButtonController.this.leftButton.isChecked()) {
					FlickButtonController.this.selectLeftButton();
					return;
				}
			}

			@Override
			public boolean longPress(com.badlogic.gdx.scenes.scene2d.Actor actor, float x, float y) {
				return FlickButtonController.this.longPressCenterButton();
			}
		});
		this.centerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				FlickButtonController.this.selectCenterButton();
			}
		});
		this.upButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				FlickButtonController.this.selectUpButton();
			}

		});
		this.downButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				FlickButtonController.this.selectDownButton();
			}
		});
		this.rightButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				FlickButtonController.this.selectRightButton();
			}
		});
		this.leftButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				FlickButtonController.this.selectLeftButton();
			}
		});
	}

	protected void selectCenterButton() {
		this.centerButton.setChecked(false);
		this.listener.center();
	}

	protected void selectUpButton() {
		this.upButton.setChecked(false);
		this.listener.up();
	}

	protected void selectDownButton() {
		this.downButton.setChecked(false);
		this.listener.down();
	}

	protected void selectRightButton() {
		this.rightButton.setChecked(false);
		this.listener.right();
	}

	protected void selectLeftButton() {
		this.leftButton.setChecked(false);
		this.listener.left();
	}

	protected boolean longPressCenterButton() {
		this.centerButton.setChecked(false);
		return this.listener.longPressCenter();
	}

	/**
	 * @param centerButtonText
	 */
	public void setCenterButtonText(String centerButtonText) {
		this.centerButton.setText(centerButtonText);
	}

	/**
	 * @param upButtonText
	 */
	public void setUpButtonText(String upButtonText) {
		this.upButton.setText(upButtonText);
	}

	/**
	 * @param downButtonText
	 */
	public void setDownButtonText(String downButtonText) {
		this.downButton.setText(downButtonText);
	}

	/**
	 * @param rightButtonText
	 */
	public void setRightButtonText(String rightButtonText) {
		this.rightButton.setText(rightButtonText);
	}

	/**
	 * @param leftButtonText
	 */
	public void setLeftButtonText(String leftButtonText) {
		this.leftButton.setText(leftButtonText);
	}

	/**
	 * @return button width (height)
	 */
	public float getButtonWidthHeight() {
		return this.buttonWidthHeight;
	}

	/**
	 * @return the listener
	 */
	public FlickButtonInputListener getListener() {
		return this.listener;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	protected void setListener(FlickButtonInputListener listener) {
		this.listener = listener;
	}

	/**
	 * @author goshi 2015/01/08
	 */
	public interface FlickButtonInputListener {
		/**
		 * Called when center is selected
		 */
		void center();

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

		/**
		 * Called when center is long pressed
		 * 
		 * @return true if not except additional gesture
		 */
		boolean longPressCenter();

	}
}

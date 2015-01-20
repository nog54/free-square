package org.nognog.freeSquare.ui;

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

	private static Color emerald = Color.valueOf("2ecc71"); //$NON-NLS-1$
	private static Color nephritis = Color.valueOf("27ae60"); //$NON-NLS-1$
	protected FlickInputListener listener;
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
	public FlickButtonController(BitmapFont font, final float buttonWidthHeight, FlickInputListener inputListener) {
		this.font = font;
		this.buttonWidthHeight = buttonWidthHeight;
		this.listener = inputListener;
		InitializeButtons();
		this.addActor(this.centerButton);
		this.addActor(this.upButton);
		this.addActor(this.downButton);
		this.addActor(this.leftButton);
		this.addActor(this.rightButton);
	}

	private void InitializeButtons() {
		this.createButtons();
		this.addListenerToButton();
	}

	private void createButtons() {
		this.centerButton = this.createMenuTextButton("Center", -this.buttonWidthHeight / 2, -this.buttonWidthHeight / 2, emerald, nephritis); //$NON-NLS-1$
		this.upButton = this.createMenuTextButton("Up", this.centerButton.getX(), this.centerButton.getY() + this.buttonWidthHeight, emerald, nephritis); //$NON-NLS-1$
		this.downButton = this.createMenuTextButton("Down", this.centerButton.getX(), this.centerButton.getY() - this.buttonWidthHeight, emerald, nephritis); //$NON-NLS-1$
		this.rightButton = this.createMenuTextButton("Right", this.centerButton.getX() + this.buttonWidthHeight, this.centerButton.getY(), emerald, nephritis); //$NON-NLS-1$ 
		this.leftButton = this.createMenuTextButton("Left", this.centerButton.getX() - this.buttonWidthHeight, this.centerButton.getY(), emerald, nephritis); //$NON-NLS-1$ 
	}

	private TextButton createMenuTextButton(String text, float x, float y, Color up, Color down) {
		final int textureRegionWidth = 256;
		final int textureRegionHeight = 128;
		TextureRegionDrawable upTexture = UiUtils.createPlaneTextureRegionDrawable(textureRegionWidth, textureRegionHeight, up);
		TextureRegionDrawable downTexture = (down == null) ? upTexture : UiUtils.createPlaneTextureRegionDrawable(textureRegionWidth, textureRegionHeight, down);

		TextButtonStyle buttonStyle = new TextButtonStyle(upTexture, downTexture, downTexture, this.font);
		TextButton textButton = new TextButton(text, buttonStyle);
		textButton.setSize(this.buttonWidthHeight, this.buttonWidthHeight);
		textButton.setPosition(x, y);
		return textButton;
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

	void selectCenterButton() {
		this.centerButton.setChecked(false);
		this.listener.center();
	}

	void selectUpButton() {
		this.upButton.setChecked(false);
		this.listener.up();
	}

	void selectDownButton() {
		this.downButton.setChecked(false);
		this.listener.down();
	}

	void selectRightButton() {
		this.rightButton.setChecked(false);
		this.listener.right();
	}

	void selectLeftButton() {
		this.leftButton.setChecked(false);
		this.listener.left();
	}

	/**
	 * @author goshi 2015/01/08
	 */
	public interface FlickInputListener {
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
	}
}

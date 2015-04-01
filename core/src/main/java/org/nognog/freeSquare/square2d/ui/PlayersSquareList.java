package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.Messages;
import org.nognog.freeSquare.Settings;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.square.Square;
import org.nognog.freeSquare.square2d.CombineSquare2d;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.event.UpdateSquareEvent;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/22
 */
public class PlayersSquareList extends FetchableAsActorPlayerLinkingScrollList<Square<?>, Square2d> {

	// TODO extract Presenter, extract super class

	private FreeSquare freeSquare;

	/**
	 * @param freeSquare
	 * @param player
	 * @param font
	 */
	public PlayersSquareList(FreeSquare freeSquare, Player player, BitmapFont font) {
		super(freeSquare.getStage().getCamera().viewportWidth / Settings.getGoldenRatio(), freeSquare.getStage().getCamera().viewportHeight / 2, player, font);
		this.freeSquare = freeSquare;
	}

	@Override
	protected Texture getDrawTextureOf(Square<?> item) {
		if (item instanceof Square2d) {
			return ((Square2d) item).getSimpleTexture();
		}
		return null;
	}

	@Override
	protected Color getDrawTextureColorOf(Square<?> item) {
		return Color.WHITE;
	}

	@Override
	protected Square<?>[] getShowListItemsFromPlayer(Player setupPlayer) {
		Array<Square<?>> playersSquares = new Array<>(setupPlayer.getSquares());
		if (this.freeSquare != null) {
			playersSquares.removeValue(this.freeSquare.getSquare(), true);
		}
		return playersSquares.toArray(Square.class);
	}

	@Override
	protected Square2d transformToFetchActorType(Square<?> listItem) {
		return (Square2d) listItem;
	}

	@Override
	protected void beginFetchActor(float x, float y, Square2d beFetchedSquare, Square<?> beFetchedItem) {
		Vector2 stageCoodinateXY = this.getWidget().localToStageCoordinates(new Vector2(x, y));
		final float squareX = stageCoodinateXY.x - (beFetchedSquare.getMostLeftVertex().x + beFetchedSquare.getMostRightVertex().x) / 2;
		final float squareY = stageCoodinateXY.y - (beFetchedSquare.getMostTopVertex().y + beFetchedSquare.getMostBottomVertex().y) / 2;
		beFetchedSquare.setPosition(squareX, squareY);
		if (!(this.freeSquare.getSquare() instanceof CombineSquare2d)) {
			this.freeSquare.convertThisSquareToCombineSquare2d();
		}
		if (this.freeSquare.getSquare() != null) {
			this.freeSquare.getSquare().addActorForce(beFetchedSquare);
		} else {
			this.freeSquare.getStage().addActor(beFetchedSquare);
		}
		this.freeSquare.showSquareOnly();
		beFetchedSquare.toFront();
	}

	@Override
	protected boolean isMovableFetchingActor() {
		return true;
	}

	@Override
	protected void fetchingActorMoved() {
		if (this.freeSquare.getSquare() != null) {
			this.freeSquare.getSquare().notify(new UpdateSquareEvent());
		}
	}

	@Override
	protected void cameraMoved() {
		final float oldX = this.freeSquare.getStage().getCamera().position.x;
		final float oldY = this.freeSquare.getStage().getCamera().position.y;
		this.freeSquare.adjustCameraZoomAndPositionIfRangeOver();
		final float afterX = this.freeSquare.getStage().getCamera().position.x;
		final float afterY = this.freeSquare.getStage().getCamera().position.y;
		this.fetchingActor.moveBy(afterX - oldX, afterY - oldY);
		this.freeSquare.notifyCameraObservers();
	}

	@Override
	protected void putFetchingActor(Square2d putTargetFetchingActor) {
		if (this.freeSquare.getSquare() != null) {
			this.freeSquare.getSquare().removeActorForce(this.fetchingActor);
		} else {
			this.freeSquare.getStage().getRoot().removeActor(this.fetchingActor);
		}
		final boolean existsBaseSquare = this.freeSquare.getSquare() != null;
		final boolean successPut = this.freeSquare.putSquare2d(this.fetchingActor);
		final boolean executedCombine = existsBaseSquare && successPut;
		if (executedCombine) {
			this.getPlayer().removeSquare(this.fetchingActor);
		}
		this.freeSquare.adjustCameraZoomAndPositionIfRangeOver();
		this.freeSquare.showPlayersSquareList();
	}

	@Override
	protected void selectedItemLongPressed(Square<?> longPressedItem, float x, float y) {
		this.freeSquare.inputName(longPressedItem, Messages.getString("squareNameInput")); //$NON-NLS-1$
	}

	@Override
	protected void selectedItemTapped(Square<?> tappedItem, int count) {
		final boolean isDoubleTapped = (count == 2);
		if (isDoubleTapped && tappedItem instanceof Square2d) {
			this.freeSquare.setSquareWithAction((Square2d) tappedItem);
			this.freeSquare.showSquareOnly();
		}
	}

	@Override
	public void updateCamera(Camera camera) {
		super.updateCamera(camera);
		final float currentCameraZoom = ((OrthographicCamera) camera).zoom;
		final float newX = camera.position.x + currentCameraZoom * (camera.viewportWidth / 2 - this.getWidth());
		final float newY = camera.position.y - currentCameraZoom * this.getHeight();
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}

}

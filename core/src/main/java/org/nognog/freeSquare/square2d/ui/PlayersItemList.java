package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.Settings;
import org.nognog.freeSquare.model.SimpleDrawable;
import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PossessedItem;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.item.Square2dItem;
import org.nognog.freeSquare.square2d.item.Square2dObjectItem;
import org.nognog.freeSquare.square2d.object.EatableObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/02/22
 */
public class PlayersItemList extends FetchableAsActorPlayerLinkingScrollList<PossessedItem<?>, Actor> {

	// TODO extract Presenter, extract super class

	private final FreeSquare freeSquare;

	/**
	 * @param freeSquare
	 * @param player
	 * @param font
	 */
	public PlayersItemList(FreeSquare freeSquare, Player player, BitmapFont font) {
		super(freeSquare.getStage().getCamera().viewportWidth / Settings.getGoldenRatio(), freeSquare.getStage().getCamera().viewportHeight / 2, player, font);
		this.freeSquare = freeSquare;
	}

	@Override
	protected Texture getDrawTextureOf(PossessedItem<?> possessedItem) {
		Item<?, ?> item = possessedItem.getItem();
		if (item instanceof SimpleDrawable) {
			return ((SimpleDrawable) item).getSimpleTexture();
		}
		return null;
	}

	@Override
	protected Color getDrawTextureColorOf(PossessedItem<?> possessedItem) {
		Item<?, ?> item = possessedItem.getItem();
		if (item instanceof SimpleDrawable) {
			return ((SimpleDrawable) item).getColor();
		}
		return null;
	}

	@Override
	protected PossessedItem<?>[] getShowListItemsFromPlayer(Player setupPlayer) {
		return setupPlayer.getItemBox().toItemArray();
	}

	@Override
	protected Actor transformToFetchActorType(PossessedItem<?> listItem) {
		Item<?, ?> item = listItem.getItem();
		if (item instanceof Square2dObjectItem && this.freeSquare.getSquare() != null) {
			return ((Square2dObjectItem) item).createSquare2dObject();
		}
		if (item instanceof Square2dItem) {
			return ((Square2dItem) item).createSquare2d();
		}
		return null;
	}

	@Override
	protected void beginFetchActor(float x, float y, Actor beFetchedActor, PossessedItem<?> beFetchedItem) {
		Item<?, ?> item = beFetchedItem.getItem();
		if (beFetchedActor instanceof Square2dObject) {
			this.addSquare2dObjectTemporary((Square2dObject) beFetchedActor, x, y, item);
			this.freeSquare.showSquareOnly();
			return;
		}
		if (beFetchedActor instanceof Square2d) {
			this.addSquare2dTemporary((Square2d) beFetchedActor, x, y, item);
			this.freeSquare.showSquareOnly();
			return;
		}
	}

	@Override
	protected boolean isMovableFetchingActor() {
		return !this.fetchingActorIsBeingEaten();
	}

	private boolean fetchingActorIsBeingEaten() {
		return (this.fetchingActor instanceof EatableObject) && ((EatableObject) this.fetchingActor).isBeingEaten();
	}

	@Override
	protected void fetchingActorMoved() {
		if (this.fetchingActor instanceof Square2dObject && this.freeSquare.getSquare() != null) {
			this.freeSquare.getSquare().notify(new UpdateSquareObjectEvent());
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
	protected void putFetchingActor(Actor putTargetFetchingActor) {
		if (putTargetFetchingActor instanceof Square2dObject) {
			Square2dObject putSquareObject = (Square2dObject) putTargetFetchingActor;
			if (this.freeSquare.putSquareObject(putSquareObject)) {
				this.getPlayer().takeOutItem(Square2dObjectItem.getInstance(putSquareObject.getType()));
			}
		} else if (putTargetFetchingActor instanceof Square2d) {
			Square2d putSquare = (Square2d) putTargetFetchingActor;
			this.putSquareAndTakeOutItemIfSuccess(putSquare, this.fetchingItem.getItem());
		}
		this.freeSquare.adjustCameraZoomAndPositionIfRangeOver();
		this.freeSquare.showPlayerItemList();
	}

	private void addSquare2dObjectTemporary(Square2dObject pannedSquareObject, float x, float y, Item<?, ?> item) {
		pannedSquareObject.setEnabledAction(false);
		Vector2 squareCoodinateXY = this.freeSquare.getSquare().stageToLocalCoordinates(this.getWidget().localToStageCoordinates(new Vector2(x, y)));
		this.freeSquare.getSquare().addSquareObject(pannedSquareObject, squareCoodinateXY.x, squareCoodinateXY.y, false);
	}

	private void addSquare2dTemporary(Square2d pannedSquare, float x, float y, Item<?, ?> item) {
		final Vector2 stageCoodinateXY = this.getWidget().localToStageCoordinates(new Vector2(x, y));
		final float squareX = stageCoodinateXY.x - (pannedSquare.getMostLeftVertex().x + pannedSquare.getMostRightVertex().x) / 2;
		final float squareY = stageCoodinateXY.y - (pannedSquare.getMostTopVertex().y + pannedSquare.getMostBottomVertex().y) / 2;
		pannedSquare.setPosition(squareX, squareY);
		if (this.freeSquare.getSquare() != null) {
			this.freeSquare.getSquare().addActorForce(pannedSquare);
		} else {
			this.freeSquare.getStage().addActor(pannedSquare);
		}
	}

	private void putSquareAndTakeOutItemIfSuccess(Square2d addSquare, Item<?, ?> item) {
		if (this.freeSquare.putSquare2d(addSquare)) {
			this.freeSquare.getPlayer().takeOutItem(item);
		}
	}

	@Override
	protected void selectedItemTapped(PossessedItem<?> tappedItem, int count) {
		if (this.freeSquare.getSquare() != null) {
			return;
		}
		final boolean isDoubleTapped = (count == 2);
		if (isDoubleTapped) {
			if (tappedItem.getItem() instanceof Square2dItem) {
				Square2d putSquare = ((Square2dItem) tappedItem.getItem()).createSquare2d();
				this.putSquareAndTakeOutItemIfSuccess(putSquare, tappedItem.getItem());
				this.freeSquare.adjustCameraZoomAndPositionIfRangeOver();
				this.freeSquare.showSquareOnly();
			}
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

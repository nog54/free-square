/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.activity.main.ui;

import org.nognog.freeSquare.Settings;
import org.nognog.freeSquare.activity.main.MainActivity;
import org.nognog.freeSquare.model.SimpleDrawable;
import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PossessedItem;
import org.nognog.freeSquare.square2d.CombineSquare2d;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.item.Square2dItem;
import org.nognog.freeSquare.square2d.item.Square2dObjectItem;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;
import org.nognog.freeSquare.ui.PlayerItemListAllowingToFetchElementAsActor;
import org.nognog.gdx.util.camera.Camera;
import org.nognog.gdx.util.camera.ObservableOrthographicCamera;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/02/22
 */
public class PlayersItemList extends PlayerItemListAllowingToFetchElementAsActor<PossessedItem<?>, Actor> {

	private final MainActivity mainActivity;

	/**
	 * @param mainActivity
	 * @param player
	 * @param font
	 */
	public PlayersItemList(MainActivity mainActivity, Player player, BitmapFont font) {
		super(mainActivity.getCamera().getViewportWidth() / Settings.getGoldenRatio(), mainActivity.getCamera().getViewportHeight() / 2, player, font);
		this.mainActivity = mainActivity;
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
	protected PossessedItem<?>[] getListItemsFromPlayer(Player setupPlayer) {
		return setupPlayer.getItemBox().toItemArray();
	}

	@Override
	protected Actor transformToFetchedActorTypeIfFetchable(PossessedItem<?> listItem) {
		Item<?, ?> item = listItem.getItem();
		if (item instanceof Square2dObjectItem && this.mainActivity.getSquare() != null) {
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
			this.mainActivity.showSquareOnly();
			return;
		}
		if (beFetchedActor instanceof Square2d) {
			this.addSquare2dTemporary((Square2d) beFetchedActor, x, y, item);
			this.mainActivity.showSquareOnly();
			return;
		}
	}

	private void addSquare2dObjectTemporary(Square2dObject pannedSquareObject, float x, float y, Item<?, ?> item) {
		pannedSquareObject.setEnableAction(false);
		Vector2 squareCoodinateXY = this.mainActivity.getSquare().stageToLocalCoordinates(this.getWidget().localToStageCoordinates(new Vector2(x, y)));
		this.mainActivity.getSquare().addSquareObject(pannedSquareObject, squareCoodinateXY.x, squareCoodinateXY.y, false);
	}

	private void addSquare2dTemporary(Square2d pannedSquare, float x, float y, Item<?, ?> item) {
		final Vector2 stageCoodinateXY = this.getWidget().localToStageCoordinates(new Vector2(x, y));
		final float squareX = stageCoodinateXY.x - (pannedSquare.getMostLeftVertex().x + pannedSquare.getMostRightVertex().x) / 2;
		final float squareY = stageCoodinateXY.y - (pannedSquare.getMostTopVertex().y + pannedSquare.getMostBottomVertex().y) / 2;
		pannedSquare.setPosition(squareX, squareY);
		if (this.mainActivity.getSquare() != null) {
			if (!(this.mainActivity.getSquare() instanceof CombineSquare2d)) {
				this.mainActivity.convertThisSquareToCombineSquare2d();
			}
			this.mainActivity.getSquare().addActorForce(pannedSquare);
		} else {
			this.mainActivity.getStage().addActor(pannedSquare);
		}
	}

	@Override
	protected boolean isFetchingMovableActor() {
		return !this.fetchingActorIsBeingEaten();
	}

	private boolean fetchingActorIsBeingEaten() {
		return (this.fetchingActor instanceof EatableObject) && ((EatableObject) this.fetchingActor).isBeingEaten();
	}

	@Override
	protected void fetchingActorMoved() {
		if (this.fetchingActor instanceof Square2dObject && this.mainActivity.getSquare() != null) {
			this.mainActivity.getSquare().notifyEventListeners(new UpdateSquareObjectEvent((Square2dObject) this.fetchingActor));
		}
	}

	@Override
	protected void cameraMoved() {
		final float oldX = this.mainActivity.getCamera().getX();
		final float oldY = this.mainActivity.getCamera().getY();
		this.mainActivity.adjustCameraZoomAndPositionIfRangeOver(false);
		final float afterX = this.mainActivity.getCamera().getX();
		final float afterY = this.mainActivity.getCamera().getY();
		this.fetchingActor.moveBy(afterX - oldX, afterY - oldY);
		this.mainActivity.getCamera().notifyCameraObservers();
	}

	@Override
	protected void putFetchingActor(Actor putTargetFetchingActor) {
		if (putTargetFetchingActor instanceof Square2dObject) {
			Square2dObject putSquareObject = (Square2dObject) putTargetFetchingActor;
			this.putSquare2dObjectAndTakeOutItemIfSuccess(putSquareObject);
		} else if (putTargetFetchingActor instanceof Square2d) {
			Square2d putSquare = (Square2d) putTargetFetchingActor;
			this.putSquareAndTakeOutItemIfSuccess(putSquare, this.fetchingItem.getItem());
		}
		this.mainActivity.adjustCameraZoomAndPositionIfRangeOver(false);
		this.mainActivity.showPlayersItemList();
	}

	private void putSquare2dObjectAndTakeOutItemIfSuccess(Square2dObject putSquareObject) {
		if (this.putSquare2dObject(putSquareObject)) {
			this.getPlayer().takeOutItem(Square2dObjectItem.getInstance(putSquareObject.getType()));
		}
	}

	/**
	 * @param putObject
	 * @return true if success
	 */
	private boolean putSquare2dObject(Square2dObject putObject) {
		if (putObject.isLandingOnSquare()) {
			putObject.setEnableAction(true);
			final Square2dEvent event = new AddObjectEvent(putObject);
			event.addExceptObserver(putObject);
			this.mainActivity.getSquare().notifyEventListeners(event);
			return true;
		}
		if (this.mainActivity.getSquare() != null) {
			this.mainActivity.getSquare().removeSquareObject(putObject);
		}
		return false;
	}

	private void putSquareAndTakeOutItemIfSuccess(Square2d addSquare, Item<?, ?> item) {
		if (this.pubSquare2d(addSquare)) {
			this.mainActivity.getPlayer().takeOutItem(item);
		}
	}

	private boolean pubSquare2d(Square2d addSquare) {
		return this.mainActivity.putSquare2d(addSquare);
	}

	@Override
	protected void selectedItemTapped(PossessedItem<?> tappedItem, int count) {
		if (tappedItem == null) {
			return;
		}
		if (this.mainActivity.getSquare() != null) {
			return;
		}
		final boolean isDoubleTapped = (count == 2);
		if (isDoubleTapped) {
			if (tappedItem.getItem() instanceof Square2dItem) {
				Square2d putSquare = ((Square2dItem) tappedItem.getItem()).createSquare2d();
				this.putSquareAndTakeOutItemIfSuccess(putSquare, tappedItem.getItem());
				this.mainActivity.adjustCameraZoomAndPositionIfRangeOver(false);
				this.mainActivity.showSquareOnly();
			}
		}
	}

	@Override
	protected ObservableOrthographicCamera getMoveCamera() {
		return this.mainActivity.getCamera();
	}

	@Override
	public void updateCamera(Camera camera) {
		final float currentCameraZoom = camera.getZoom();
		final float newX = camera.getX() + currentCameraZoom * (camera.getViewportWidth() / 2 - this.getWidth());
		final float newY = camera.getY() - currentCameraZoom * this.getHeight();
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}

}

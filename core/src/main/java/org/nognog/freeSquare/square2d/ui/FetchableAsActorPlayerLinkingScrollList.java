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

package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.model.player.Player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/04/01
 * @param <T1>
 *            list item type
 * @param <T2>
 *            actor type of be fetched list item
 */
public abstract class FetchableAsActorPlayerLinkingScrollList<T1, T2 extends Actor> extends PlayerLinkingScrollList<T1> implements CameraObserver {

	private static final float cameraMoveAmountBase = 10;
	private static final float cameraMoveThresholdBase = 50;

	protected T1 fetchingItem;
	protected T2 fetchingActor;

	private OrthographicCamera moveCamera;

	protected boolean requestedMoveCameraToLeft;
	protected boolean requestedMoveCameraToRight;
	protected boolean requestedMoveCameraToDown;
	protected boolean requestedMoveCameraToUp;
	protected float totalActMoveXFromLastPan;
	protected float totalActMoveYFromLastPan;

	/**
	 * @param width
	 * @param height
	 * @param player
	 * @param font
	 */
	public FetchableAsActorPlayerLinkingScrollList(float width, float height, Player player, BitmapFont font) {
		super(width, height, player, font);
	}

	protected abstract T2 transformToFetchActorType(T1 listItem);

	protected abstract void beginFetchActor(float x, float y, T2 beFetchedActor, T1 beFetchedItem);

	protected abstract boolean isFetchingMovableActor();

	protected abstract void fetchingActorMoved();

	protected abstract void putFetchingActor(T2 putTargetFetchingActor);

	protected abstract void cameraMoved();

	@Override
	protected void selectedItemPanned(T1 pannedItem, float x, float y, float deltaX, float deltaY) {
		if (pannedItem == null) {
			return;
		}
		if (this.fetchingActor == null) {
			this.fetchingItem = pannedItem;
			this.fetchingActor = this.transformToFetchActorType(pannedItem);
			if (this.fetchingActor == null) {
				return;
			}
			this.beginFetchActor(x, y, this.fetchingActor, this.fetchingItem);
		} else {
			if (this.isFetchingMovableActor()) {
				this.fetchingActor.moveBy(deltaX - this.totalActMoveXFromLastPan, deltaY - this.totalActMoveYFromLastPan);
				this.totalActMoveXFromLastPan = 0;
				this.totalActMoveYFromLastPan = 0;
				this.enableMoveCameraFlagsIfPanningNearEdge(this.fetchingActor);
				this.fetchingActorMoved();
			}
		}
	}

	private void enableMoveCameraFlagsIfPanningNearEdge(Actor actor) {
		this.requestedMoveCameraToLeft = false;
		this.requestedMoveCameraToRight = false;
		this.requestedMoveCameraToDown = false;
		this.requestedMoveCameraToUp = false;
		// origin is top left corner
		final Vector2 actorStageCoordinatePosition = actor.localToStageCoordinates(new Vector2(actor.getOriginX(), actor.getOriginY()));
		final float cameraLeftEnd = this.moveCamera.position.x - this.moveCamera.viewportWidth / 2 * this.moveCamera.zoom;
		final float cameraRightEnd = this.moveCamera.position.x + this.moveCamera.viewportWidth / 2 * this.moveCamera.zoom;
		final float cameraBottomEnd = this.moveCamera.position.y - this.moveCamera.viewportHeight / 2 * this.moveCamera.zoom;
		final float cameraTopEnd = this.moveCamera.position.y + this.moveCamera.viewportHeight / 2 * this.moveCamera.zoom;
		if (actorStageCoordinatePosition.x <= cameraLeftEnd + cameraMoveThresholdBase * this.moveCamera.zoom) {
			this.requestedMoveCameraToLeft = true;
		}
		if (actorStageCoordinatePosition.x >= cameraRightEnd - cameraMoveThresholdBase * this.moveCamera.zoom) {
			this.requestedMoveCameraToRight = true;
		}
		if (actorStageCoordinatePosition.y <= cameraBottomEnd + cameraMoveThresholdBase * this.moveCamera.zoom) {
			this.requestedMoveCameraToDown = true;
		}
		if (actorStageCoordinatePosition.y >= cameraTopEnd - cameraMoveThresholdBase * this.moveCamera.zoom) {
			this.requestedMoveCameraToUp = true;
		}
	}

	@Override
	protected void touchUp(float x, float y) {
		if (this.fetchingActor == null || this.fetchingItem == null) {
			this.fetchingItem = null;
			this.fetchingActor = null;
			return;
		}
		this.putFetchingActor(this.fetchingActor);
		this.fetchingItem = null;
		this.fetchingActor = null;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (this.fetchingActor == null) {
			return;
		}
		final OrthographicCamera camera = (OrthographicCamera) this.getStage().getCamera();
		final float moveAmount = camera.zoom * cameraMoveAmountBase;
		if (this.requestedMoveCameraToRight) {
			camera.position.x += moveAmount;
			this.fetchingActor.moveBy(moveAmount, 0);
			this.totalActMoveXFromLastPan += moveAmount;
		}
		if (this.requestedMoveCameraToLeft) {
			camera.position.x -= moveAmount;
			this.fetchingActor.moveBy(-moveAmount, 0);
			this.totalActMoveXFromLastPan -= moveAmount;
		}
		if (this.requestedMoveCameraToUp) {
			camera.position.y += moveAmount;
			this.fetchingActor.moveBy(0, moveAmount);
			this.totalActMoveYFromLastPan += moveAmount;
		}
		if (this.requestedMoveCameraToDown) {
			camera.position.y -= moveAmount;
			this.fetchingActor.moveBy(0, -moveAmount);
			this.totalActMoveYFromLastPan -= moveAmount;
		}
		if (this.requestedMoveCameraToDown || this.requestedMoveCameraToUp || this.requestedMoveCameraToLeft || this.requestedMoveCameraToRight) {
			this.cameraMoved();
		}
	}

	@Override
	public void updateCamera(Camera camera) {
		this.moveCamera = (OrthographicCamera) camera;
	}
}

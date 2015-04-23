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

import org.nognog.freeSquare.Messages;
import org.nognog.freeSquare.Settings;
import org.nognog.freeSquare.activity.MainActivity;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.object.LifeObject;
import org.nognog.freeSquare.square2d.object.types.LifeObjectType;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi 2015/02/22
 */
public class PlayersLifeList extends FetchableAsActorPlayerLinkingScrollList<Life, LifeObject> {

	// TODO extract Presenter, extract super class

	private MainActivity mainActivity;

	/**
	 * @param mainActivity
	 * @param player
	 * @param font
	 */
	public PlayersLifeList(MainActivity mainActivity, Player player, BitmapFont font) {
		super(mainActivity.getFreeSquare().getStage().getCamera().viewportWidth / Settings.getGoldenRatio(), mainActivity.getFreeSquare().getStage().getCamera().viewportHeight / 2, player, font);
		this.mainActivity = mainActivity;
	}

	@Override
	protected Texture getDrawTextureOf(Life item) {
		final LifeObjectType bindingLifeObjectType = LifeObjectType.Manager.getBindingLifeObjectType(item);
		return bindingLifeObjectType.getTexture();
	}

	@Override
	protected Color getDrawTextureColorOf(Life item) {
		final LifeObjectType bindingLifeObjectType = LifeObjectType.Manager.getBindingLifeObjectType(item);
		return bindingLifeObjectType.getColor();
	}

	@Override
	protected Life[] getShowListItemsFromPlayer(Player setupPlayer) {
		return setupPlayer.getLifes();
	}

	@Override
	protected LifeObject transformToFetchActorType(Life listItem) {
		return LifeObject.create(listItem);
	}

	@Override
	protected void beginFetchActor(float x, float y, LifeObject beFetchedActor, Life beFetchedItem) {
		beFetchedActor.setEnabledAction(false);
		Vector2 squareCoodinateXY = this.mainActivity.getSquare().stageToLocalCoordinates(this.getWidget().localToStageCoordinates(new Vector2(x, y)));
		this.mainActivity.getSquare().addSquareObject(beFetchedActor, squareCoodinateXY.x, squareCoodinateXY.y, false);
		this.mainActivity.showSquareOnly();
	}

	@Override
	protected boolean isMovableFetchingActor() {
		return true;
	}

	@Override
	protected void cameraMoved() {
		final float oldX = this.mainActivity.getStage().getCamera().position.x;
		final float oldY = this.mainActivity.getStage().getCamera().position.y;
		this.mainActivity.adjustCameraZoomAndPositionIfRangeOver();
		final float afterX = this.mainActivity.getStage().getCamera().position.x;
		final float afterY = this.mainActivity.getStage().getCamera().position.y;
		this.fetchingActor.moveBy(afterX - oldX, afterY - oldY);
		this.mainActivity.getFreeSquare().notifyCameraObservers();
	}

	@Override
	protected void fetchingActorMoved() {
		this.mainActivity.getSquare().notify(new UpdateSquareObjectEvent());
	}

	@Override
	protected void putFetchingActor(LifeObject putTargetFetchingActor) {
		if (putTargetFetchingActor.isValid()) {
			this.mainActivity.getPlayer().removeLife(putTargetFetchingActor.getLife());
			putTargetFetchingActor.setEnabledAction(true);
			Square2dEvent event = new AddObjectEvent(putTargetFetchingActor);
			event.addExceptObserver(putTargetFetchingActor);
			this.mainActivity.getSquare().notifyObservers(event);
		} else {
			this.mainActivity.getSquare().removeSquareObject(putTargetFetchingActor);
		}
		this.mainActivity.adjustCameraZoomAndPositionIfRangeOver();
		this.mainActivity.showPlayersLifeList();
	}

	@Override
	protected void selectedItemLongPressed(Life longPressedItem, float x, float y) {
		this.mainActivity.getFreeSquare().inputName(longPressedItem, Messages.getString("lifeNameInput"), ""); //$NON-NLS-1$ //$NON-NLS-2$
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

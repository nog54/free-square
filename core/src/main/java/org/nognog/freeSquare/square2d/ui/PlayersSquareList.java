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

	private MainActivity mainActivity;

	/**
	 * @param mainActivity
	 * @param player
	 * @param font
	 */
	public PlayersSquareList(MainActivity mainActivity, Player player, BitmapFont font) {
		super(mainActivity.getFreeSquare().getStage().getCamera().viewportWidth / Settings.getGoldenRatio(), mainActivity.getFreeSquare().getStage().getCamera().viewportHeight / 2, player, font);
		this.mainActivity = mainActivity;
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
		if (this.mainActivity != null) {
			playersSquares.removeValue(this.mainActivity.getSquare(), true);
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
		if (!(this.mainActivity.getSquare() instanceof CombineSquare2d)) {
			this.mainActivity.convertThisSquareToCombineSquare2d();
		}
		if (this.mainActivity.getSquare() != null) {
			this.mainActivity.getSquare().addActorForce(beFetchedSquare);
		} else {
			this.mainActivity.getStage().addActor(beFetchedSquare);
		}
		this.mainActivity.showSquareOnly();
		beFetchedSquare.toFront();
	}

	@Override
	protected boolean isMovableFetchingActor() {
		return true;
	}

	@Override
	protected void fetchingActorMoved() {
		if (this.mainActivity.getSquare() != null) {
			this.mainActivity.getSquare().notify(new UpdateSquareEvent());
		}
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
	protected void putFetchingActor(Square2d putTargetFetchingActor) {
		if (this.mainActivity.getSquare() != null) {
			this.mainActivity.getSquare().removeActorForce(this.fetchingActor);
		} else {
			this.mainActivity.getStage().getRoot().removeActor(this.fetchingActor);
		}
		final boolean existsBaseSquare = this.mainActivity.getSquare() != null;
		final boolean successPut = this.mainActivity.putSquare2d(this.fetchingActor);
		final boolean executedCombine = existsBaseSquare && successPut;
		if (executedCombine) {
			this.getPlayer().removeSquare(this.fetchingActor);
		}
		this.mainActivity.adjustCameraZoomAndPositionIfRangeOver();
		this.mainActivity.showPlayersSquareList();
	}

	@Override
	protected void selectedItemLongPressed(Square<?> longPressedItem, float x, float y) {
		this.mainActivity.getFreeSquare().inputName(longPressedItem, Messages.getString("squareNameInput"), ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void selectedItemTapped(Square<?> tappedItem, int count) {
		final boolean isDoubleTapped = (count == 2);
		if (isDoubleTapped && tappedItem instanceof Square2d) {
			this.mainActivity.setSquareWithAction((Square2d) tappedItem);
			this.mainActivity.showSquareOnly();
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

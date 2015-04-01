package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.Messages;
import org.nognog.freeSquare.Settings;
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

	private FreeSquare freeSquare;

	/**
	 * @param freeSquare
	 * @param player
	 * @param font
	 */
	public PlayersLifeList(FreeSquare freeSquare, Player player, BitmapFont font) {
		super(freeSquare.getStage().getCamera().viewportWidth / Settings.getGoldenRatio(), freeSquare.getStage().getCamera().viewportHeight / 2, player, font);
		this.freeSquare = freeSquare;
	}

	@Override
	protected Texture getDrawTextureOf(Life item) {
		final LifeObjectType bindingLifeObjectType = LifeObjectType.getBindingLifeObjectType(item);
		return bindingLifeObjectType.getTexture();
	}

	@Override
	protected Color getDrawTextureColorOf(Life item) {
		final LifeObjectType bindingLifeObjectType = LifeObjectType.getBindingLifeObjectType(item);
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
		Vector2 squareCoodinateXY = this.freeSquare.getSquare().stageToLocalCoordinates(this.getWidget().localToStageCoordinates(new Vector2(x, y)));
		this.freeSquare.getSquare().addSquareObject(beFetchedActor, squareCoodinateXY.x, squareCoodinateXY.y, false);
		this.freeSquare.showSquareOnly();
	}
	
	@Override
	protected boolean isMovableFetchingActor() {
		return true;
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
	protected void fetchingActorMoved() {
		this.freeSquare.getSquare().notify(new UpdateSquareObjectEvent());
	}

	@Override
	protected void putFetchingActor(LifeObject putTargetFetchingActor) {
		if (putTargetFetchingActor.isValid()) {
			this.freeSquare.getPlayer().removeLife(putTargetFetchingActor.getLife());
			putTargetFetchingActor.setEnabledAction(true);
			Square2dEvent event = new AddObjectEvent(putTargetFetchingActor);
			event.addExceptObserver(putTargetFetchingActor);
			this.freeSquare.getSquare().notifyObservers(event);
		} else {
			this.freeSquare.getSquare().removeSquareObject(putTargetFetchingActor);
		}
		this.freeSquare.adjustCameraZoomAndPositionIfRangeOver();
		this.freeSquare.showPlayersLifeList();
	}

	@Override
	protected void selectedItemLongPressed(Life longPressedItem, float x, float y) {
		this.freeSquare.inputName(longPressedItem, Messages.getString("lifeNameInput")); //$NON-NLS-1$
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

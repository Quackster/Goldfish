package com.classichabbo.goldfish.client.game.room.model;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.client.modules.types.room.RoomCamera;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Tile extends View {
    private final RoomCamera camera;

    private Position position;
    private ImageView tile;
    private ImageView wall;

    private Image tileOutline;
    private boolean hovering;
    private boolean door;
    private List<TileAttributes> wallTypes;
    private TileState tileState;

    public Tile(RoomCamera camera, int x, int y, int z) {
        this.position = new Position(x, y, z);
        this.wallTypes = new ArrayList<>();
        this.camera = camera;
        this.hovering = false;
    }


    @Override
    public void stop() {
        super.stop();
        this.removeUpdate();
    }

    @Override
    public void registerUpdate() {
        // Queue to receive
        Movie.getInstance().getInterfaceScheduler().receiveUpdate(this);
    }

    @Override
    public void removeUpdate() {
        // Remove from update queue
        Movie.getInstance().getInterfaceScheduler().removeUpdate(this);
    }

    @Override
    public void start() {
        this.registerUpdate();
    }

    @Override
    public void update() {

    }

    void renderFloor() {
        this.tile = new ImageView();

        if (this.wallTypes.contains(TileAttributes.LOWER_STAIRS)) {
            this.tile.setImage(ResourceManager.getInstance().getFxImage("assets/views/room/parts", TileAttributes.LOWER_STAIRS.getFileName()));
        }

        if (this.wallTypes.contains(TileAttributes.UPPER_STAIRS)) {
            this.tile.setImage(ResourceManager.getInstance().getFxImage("assets/views/room/parts", TileAttributes.UPPER_STAIRS.getFileName()));
        }

        if (this.wallTypes.contains(TileAttributes.BASIC_TILE)) {
            this.tile.setImage(ResourceManager.getInstance().getFxImage("assets/views/room/parts", TileAttributes.BASIC_TILE.getFileName()));
        }

        this.tile.setX(this.getWorldX());
        this.tile.setY(this.getWorldY());
        this.getChildren().add(this.tile);
    }


        /*

        if (this.wallType != WallType.NONE) {
            if (this.wallType == WallType.RIGHT) {
                if (this.door) {
                    //graphics.drawImage(this.doorRight, isoX + 33, isoY - 124, null);
                } else {
                   // graphics.drawImage(this.wallRight, isoX + 33, isoY - 124, null); // TODO: Align door for the right side
                }
            }

            if (this.wallType == WallType.LEFT) {
                if (this.door) {
                    graphics.drawImage(this.doorLeft, isoX + 32, isoY - 107, null);
                } else {
                    graphics.drawImage(this.wallLeft, isoX - 9, isoY - 125, null);
                }
            }

            if (this.wallType == WallType.LEFT_AND_RIGHT) {
                if (this.door) {
                    graphics.drawImage(this.doorLeft, isoX + 32, isoY - 107, null);
                    graphics.drawImage(this.doorRight, isoX + 33, isoY - 124, null);
                } else {
                    graphics.drawImage(this.wallLeft, isoX - 9, isoY - 125, null);
                    graphics.drawImage(this.wallRight, isoX + 33, isoY - 124, null);
                }
            }
        }
*/

    /*
    void renderWall(Graphics graphics, RoomCamera camera) {
        //Converts 2d points to isometric
        int isoX = camera.getX() - (this.y * 32) + (this.x * 32) - 32;
        int isoY = camera.getY() + (this.y * 16) + (this.x * 16);

        if (this.wallType != WallType.NONE) {
            if (this.wallType == WallType.RIGHT) {
                if (this.door) {
                    graphics.drawImage(this.doorRight, isoX + 33, isoY - 124, null);
                } else {
                    graphics.drawImage(this.wallRight, isoX + 33, isoY - 124, null); // TODO: Align door for the right side
                }
            }

            if (this.wallType == WallType.LEFT) {
                if (this.door) {
                    graphics.drawImage(this.doorLeft, isoX + 32, isoY - 107, null);
                } else {
                    graphics.drawImage(this.wallLeft, isoX - 9, isoY - 125, null);
                }
            }

            if (this.wallType == WallType.LEFT_AND_RIGHT) {
                if (this.door) {
                    graphics.drawImage(this.doorLeft, isoX + 32, isoY - 107, null);
                    graphics.drawImage(this.doorRight, isoX + 33, isoY - 124, null);
                } else {
                    graphics.drawImage(this.wallLeft, isoX - 9, isoY - 125, null);
                    graphics.drawImage(this.wallRight, isoX + 33, isoY - 124, null);
                }
            }
        }
    }

     */

    void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    /**
     * Gets the tile wall type
     *
     * @return the type
     */
    public List<TileAttributes> getWallTypes() {
        return wallTypes;
    }

    /**
     * Gets whether this tile is a door or not
     * @return true, if successful
     */
    public boolean isDoor() {
        return door;
    }

    /**
     * Sets whether this tile is a door or not
     * @param door the door setting
     */
    public void setDoor(boolean door) {
        this.door = door;
    }

    public int getWorldX() {
        return -(this.position.getY() * 32) + (this.position.getX() * 32) - 32;
    }

    public int getWorldY() {
        return ((this.position.getY() * 16) + (this.position.getX() * 16)) - (32 * ((int)this.position.getZ() - 1));
    }

    public TileState getTileState() {
        return tileState;
    }

    public void setTileState(TileState tileState) {
        this.tileState = tileState;
    }

    public Position getPosition() {
        return position;
    }
}
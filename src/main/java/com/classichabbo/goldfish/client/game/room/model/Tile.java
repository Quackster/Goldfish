package com.classichabbo.goldfish.client.game.room.model;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.game.resources.ResourceManager;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.client.modules.types.room.RoomCamera;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Tile extends View {
    private final RoomCamera camera;

    private int x;
    private int y;
    private int z;
    private ImageView tile;
    private ImageView wall;

    private Image tileImage;
    private Image tileOutline;
    private Image doorLeft;
    private Image doorRight;
    private Image wallLeft;
    private Image wallRight;
    private boolean hovering;
    private boolean door;
    private WallType wallType;

    public Tile(RoomCamera camera, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tileImage = ResourceManager.getInstance().getFxImage("assets/views/room/parts", "tile.png");
        this.tileOutline = ResourceManager.getInstance().getFxImage("assets/views/room/parts", "tileoutline.png");
        this.doorLeft = ResourceManager.getInstance().getFxImage("assets/views/room/parts", "door_left.png");
        this.doorRight = ResourceManager.getInstance().getFxImage("assets/views/room/parts", "door_right.png");
        this.wallLeft = ResourceManager.getInstance().getFxImage("assets/views/room/parts", "wall_left.png");
        this.wallRight = ResourceManager.getInstance().getFxImage("assets/views/room/parts", "wall_right.png");
        this.wallType = WallType.NONE;
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
        this.tile.setImage(this.tileImage);
        this.tile.setX(this.getWorldX());
        this.tile.setY(this.getWorldY());
        this.getChildren().add(this.tile);
    }

    void renderWall() {
        int isoX = this.camera.getX() + (this.y * 32) + (this.x * 32) - 32;
        int isoY = this.camera.getY() + (this.y * 16) + (this.x * 16);

        this.wall = new ImageView();

        if (this.wallType != WallType.NONE && this.wallType == WallType.RIGHT) {
            if (this.door) {
                this.wall.setImage(this.doorRight);
            } else {
                this.wall.setImage(this.wallRight);
            }
        }

        if (this.wallType != WallType.NONE && this.wallType == WallType.LEFT) {
            if (this.door) {
                this.wall.setImage(this.doorLeft);
            } else {
                this.wall.setImage(this.wallLeft);
            }
        }

        this.wall.setX(isoX);
        this.wall.setY(isoY);
        this.getChildren().add(this.wall);

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
    }

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
    public WallType getWallType() {
        return wallType;
    }

    /**
     * Sets the wall type that the tile has.
     *
     * @param wallType if the tile has a wall
     */
    public void setWallType(WallType wallType) {
        this.wallType = wallType;
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
        return -(this.y * 32) + (this.x * 32) - 32;
    }

    public int getWorldY() {
        return (this.y * 16) + (this.x * 16);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "X: " + this.x + " Y: " + this.y;
    }
}
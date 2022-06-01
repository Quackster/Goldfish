package com.classichabbo.goldfish.client.game.room.model;

import com.classichabbo.goldfish.client.Movie;
import com.classichabbo.goldfish.client.modules.View;
import com.classichabbo.goldfish.client.modules.types.room.RoomCamera;

import java.util.ArrayList;
import java.util.List;

public class RoomModel extends View {
    private final String[] lines;
    private int mapSizeX;
    private int mapSizeY;

    private int doorX;
    private int doorY;

    private List<Tile> tiles;
    private Tile[][] roomTileMap;
    private TileState[][] tileStatesMap;
    private RoomCamera camera;
    private Tile hoveringTile;
    private int modelWidth;
    private int modelHeight;

    public RoomModel(String[] heightMap, RoomCamera camera) {
        this.camera = camera;
        this.doorX = 1;
        this.doorY = 11;
        this.lines = heightMap;
    }

    @Override
    public void start() {
        this.tiles = new ArrayList<>();

        this.mapSizeY = this.lines.length;
        this.mapSizeX = this.lines[0].length();

        this.tileStatesMap = new TileState[this.mapSizeX][this.mapSizeY];
        this.roomTileMap = new Tile[this.mapSizeX][this.mapSizeY];

        for (int y = 0; y < this.mapSizeY; y++) {
            String line = this.lines[y];

            for (int x = 0; x < this.mapSizeX; x++) {
                String letter = Character.toString(line.charAt(x));

                if (letter.equalsIgnoreCase("x")) {
                    this.tileStatesMap[x][y] = TileState.CLOSED;
                } else {
                    this.tileStatesMap[x][y] = TileState.OPEN;

                    var tile = new Tile(this.camera, x, y, Integer.parseInt(letter));
                    this.roomTileMap[x][y] = tile;
                    this.tiles.add(tile);

                    Movie.getInstance().createObject(tile, this);
                }
            }
        }
        this.tiles.forEach(x -> x.renderFloor());

        //this.locateDoor();
        //this.findWallTiles();

        //System.out.println("Found door with coordinates: " + this.doorX + ", " + this.doorY);

        this.registerUpdate();
        //this.addChild(new RoomToolbar());
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
    public void update() {
        this.setTranslateX(0);
        this.setTranslateY(0);

        // System.out.println(this.getHeight() + " / " + this.getWidth());
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * Get the door X coordinate of the map
     *
     * @return the x coordinate of the door
     */
    public int getDoorX() {
        return doorX;
    }

    /**
     * Get the door Y coordinate of the map
     *
     * @return the y coordinate of the door
     */
    public int getDoorY() {
        return doorY;
    }

    /**
     * Get the camera of the room
     *
     * @return the camera
     */
    public RoomCamera getCamera() {
        return this.camera;
    }

    /**
     * Get tile at coordinates
     *
     * @param x coordinate
     * @param y coordinate
     * @return the tile on the grid if exists.
     */
    public Tile getTile(int x, int y) {
        try {
            return this.roomTileMap[x][y];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return null;
        }
    }

    /**
     * Apply the hovering image over the tile.
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void setHovering(int x, int y) {
        try {
            //If hovering over something, stop.
            if (this.hoveringTile != null) {
                this.hoveringTile.setHovering(false);
            }

            //Check if new hovering tile exist, hover it, and swap the old one with new one.
            Tile newHoveringTile = this.roomTileMap[x][y];
            if (newHoveringTile != null) {
                newHoveringTile.setHovering(true);
                this.hoveringTile = newHoveringTile;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    /**
     * Finds and marks the wall tiles located on this model, it will left to right
     * and down, find the first open tile, and then mark it as a wall tile, break the loop
     * and then increment the coordinate to check the next tile.
     */
    private void findWallTiles() {
        List<Tile> tiles = new ArrayList<>();

        for (int x = 0; x < this.mapSizeX; x++) {
            for (int y = 0; y < this.mapSizeY; y++) {
                TileState state = this.tileStatesMap[x][y];

                if (state == null) {
                    return;
                }

                if (state == TileState.OPEN) {
                    Tile tile = this.roomTileMap[x][y];
                    tile.setWallType(WallType.RIGHT);
                    tiles.add(tile);
                    break;
                }
            }
        }

        for (int y = 0; y < this.mapSizeY; y++) {
            for (int x = 0; x < this.mapSizeX; x++) {

                TileState state = this.tileStatesMap[x][y];

                if (state == null) {
                    return;
                }

                if (state == TileState.OPEN) {
                    Tile tile = this.roomTileMap[x][y];

                    if (tiles.contains(tile) && (x != this.doorX && y != this.doorY)) {
                        tile.setWallType(WallType.LEFT_AND_RIGHT);
                    } else {
                        tile.setWallType(WallType.LEFT);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Locate the door by finding the first open tile
     * while iterating vertically on the tile states.
     */
    private void locateDoor() {
        try {
            for (int x = 0; x < this.mapSizeX; x++) {
                for (int y = 0; y < this.mapSizeY; y++) {
                    TileState[] states = this.tileStatesMap[x];

                    if (states.length > 0) {
                        TileState state = this.tileStatesMap[x][y];

                        if (state == TileState.OPEN) {
                            this.doorX = x;
                            this.doorY = y;
                            this.roomTileMap[x][y].setDoor(true);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getModelWidth() {
        return modelWidth;
    }

    public int getModelHeight() {
        return modelHeight;
    }
}
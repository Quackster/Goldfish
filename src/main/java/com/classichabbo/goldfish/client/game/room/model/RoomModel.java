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

        this.roomTileMap = new Tile[this.mapSizeX][this.mapSizeY];

        for (int y = 0; y < this.mapSizeY; y++) {
            String line = this.lines[y];

            for (int x = 0; x < this.mapSizeX; x++) {
                String letter = Character.toString(line.charAt(x));

                var tile = new Tile(this.camera, x, y, letter.equalsIgnoreCase("x") ? -1 : Integer.parseInt(letter));
                tile.getWallTypes().add(TileAttributes.BASIC_TILE);
                
                this.roomTileMap[x][y] = tile;
                this.tiles.add(tile);

                if (letter.equalsIgnoreCase("x")) {
                    tile.setTileState(TileState.CLOSED);
                } else {
                    tile.setTileState(TileState.OPEN);
                    Movie.getInstance().createObject(tile, this);
                }
            }
        }

        this.findStairs();

        this.tiles.forEach(x -> x.renderFloor());
        // this.walls.forEach(x -> x.renderWall());


        //this.locateDoor();


        //System.out.println("Found door with coordinates: " + this.doorX + ", " + this.doorY);

        this.registerUpdate();
        //this.addChild(new RoomToolbar());
    }

    private void findStairs() {
        var movePoints = new Position[]{
                new Position(0, -1),
                new Position(1, 0),
                new Position(0, 1),
                new Position(-1, 0)
        };

        for (var tile : this.tiles) {
            if (tile.getTileState() == TileState.CLOSED) {
                continue;
            }

            for (var point : movePoints) {
                var add = tile.getPosition().add(point);


                if (add.getX() < 0 || add.getX() >= this.mapSizeX) {
                    continue;
                }

                if (add.getY() < 0 || add.getY() >= this.mapSizeY) {
                    continue;
                }

                var newTile = this.roomTileMap[add.getX()][add.getY()];

                if (newTile == null) {
                    continue;
                }

                if (newTile.getTileState() == TileState.CLOSED) {
                    continue;
                }

                if (Math.abs(tile.getPosition().getZ() - newTile.getPosition().getZ()) == 1.0) {
                    if (newTile.getPosition().getZ() > tile.getPosition().getZ()) {
                        newTile.getWallTypes().add(TileAttributes.UPPER_STAIRS);
                        newTile.getWallTypes().remove(TileAttributes.BASIC_TILE);
                    }

                    if (tile.getPosition().getZ() > newTile.getPosition().getZ()) {
                        newTile.getWallTypes().add(TileAttributes.LOWER_STAIRS);
                        newTile.getWallTypes().remove(TileAttributes.BASIC_TILE);
                    }
                }
            }
        }
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
}
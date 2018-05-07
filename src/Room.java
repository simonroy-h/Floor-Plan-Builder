import javafx.geometry.Point2D;

public class Room {
    private static final int    MAX_ROOM_TILES = 400;

    private int             numTiles;   // the number of tiles that make up a Room
    private Point2D[]       tiles;      // all the tiles that make up a Room
    private int             colorIndex; // the color index of the Room

    private String          occupant;   // the name of the occupant of this room
    private String          position;   // the position that the occupant of this room holds
    private String          number;     // the room number (e.g., 102, 305B, etc.)

    public Room() {
        tiles = new Point2D[MAX_ROOM_TILES];
        numTiles = 0;
        colorIndex = 0;
        occupant = "";
        position = "";
        number = "";
    }

    public int getColorIndex() { return colorIndex; }
    public int getNumberOfTiles() { return numTiles; }
    public void setColorIndex(int c) { colorIndex = c; }

    public String getOccupant() { return occupant; }
    public String getPosition() { return position; }
    public String getNumber() { return number; }
    public void setOccupant(String name) { occupant = name; }
    public void setPosition(String job) { position = job; }
    public void setNumber (String num) { number = num; }

    // add a tile to the room (up until the maximum)
    public boolean addTile(int r, int c) {
        if (numTiles < MAX_ROOM_TILES) {
            tiles[numTiles++] = new Point2D(c,r);
            return true;
        }
        return false;
    }

    // remove a tile from the room
    public void removeTile(int r, int c) {
        // find the tile
        for (int i = 0; i < numTiles; i++) {
            if ((tiles[i].getX() == c) && (tiles[i].getY() == r)) {
                tiles[i] = tiles[numTiles - 1];
                tiles[numTiles - 1] = null;
                numTiles--;
                return;
            }
        }
    }

    // return whether or not the given location is part of the room
    public boolean contains(int r, int c) {
        for (int i = 0; i < numTiles; i++)
            if ((tiles[i].getX() == c) && (tiles[i].getY() == r))
                return true;
        return false;
    }
}

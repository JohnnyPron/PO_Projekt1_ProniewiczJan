package agh.cs.project.secondary_objects;
import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public static MapDirection getRandomDirection(){
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    public MapDirection Next(){
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            default -> NORTH;
        };
    }

    public MapDirection Previous(){
        return switch (this) {
            case NORTH -> NORTHWEST;
            case NORTHWEST -> WEST;
            case WEST -> SOUTHWEST;
            case SOUTHWEST -> SOUTH;
            case SOUTH -> SOUTHEAST;
            case SOUTHEAST -> EAST;
            case EAST -> NORTHEAST;
            default -> NORTH;
        };
    }

    public MapDirection OppositeDirection(){
        return switch (this) {
            case NORTH -> SOUTH;
            case NORTHEAST -> SOUTHWEST;
            case EAST -> WEST;
            case SOUTHEAST -> NORTHWEST;
            case SOUTH -> NORTH;
            case SOUTHWEST -> NORTHEAST;
            case WEST -> EAST;
            default -> SOUTHEAST;
        };
    }

    public Vector2d ToUnitVector(){
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTHEAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            default -> new Vector2d(-1, 1);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "North";
            case NORTHEAST -> "North-East";
            case EAST -> "East";
            case SOUTHEAST -> "South-East";
            case SOUTH -> "South";
            case SOUTHWEST -> "South-West";
            case WEST -> "West";
            default -> "North-West";
        };
    }
}
